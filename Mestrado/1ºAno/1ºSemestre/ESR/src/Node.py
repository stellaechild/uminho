from tkinter import *
from tkinter import messagebox
from random import randint
from PIL import Image, ImageTk
import socket, threading, sys, traceback, os
import logger
import time 
import sys

from RtpPacket import RtpPacket
from Packet import Packet
from ConnectedClients import ConnectedClients
import subprocess
from database import Table
from database import Tracker

#Node quando se liga pede os seus vizinhos ao bootstrapper
class Node:
    # states
    INIT = 0
    READY = 1
    PLAYING = 2
    state = INIT

    # message type
    SETUP = 0
    PLAY = 1
    PAUSE = 2
    EXIT = 3
    HELLO = 4
    ALIVE = 5

    # reply types
    OK = 0
    ALIVE_REPLY = 1
    FILE_FOUND = 2
    FILE_NOT_FOUND = 3
    END_VIDEO = 5


    def __init__(self, bootstrapper_address, bootstrapper_port, address, port, clientPort, rtpPort):
        self.logger = logger.define_logger(address)
        self.bootstrapperAddr = bootstrapper_address
        self.bootstrapperPort = bootstrapper_port
        self.address = address
        self.port = port
        self.clientPort = clientPort
        self.rtpPort = rtpPort
        self.socketNode = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.socketClient = None
        self.socketVideo = None
        self.requestSent = 0 
        self.info_neighbors = []
        self.clients = ConnectedClients()
        self.routingTable = Table()
        self.tracker = Tracker()
        self.videoPlaying = False
        self.alreadyOpen=0
        self.rtspSeq = 0
        self.frameNbr = 0
        self.bestRoute = ('',0,'')
        self.videoEnded = False

    # Função principal
    def run(self):
        self.getNeighbors()
        threading.Thread(target=self.cleanCache).start()
        threading.Thread(target=self.recvRtspRequest).start()
        threading.Thread(target=self.handleAliveRequest).start()
        #threading.Thread(target=self.handleKeepAliveRequest).start()
    
    # Função responsavel por limpara a cache da tabela de routing
    def cleanCache(self):
        while True:
            # Agendamento para limpar após 120 segundos caso o estado no momento nao seja de setup
            time.sleep(120)
            # Limpar apenas se tiver algo lá dentro
            if self.routingTable.get_best_route(self.address):
                self.routingTable.resetCache(self.requestSent)
                self.logger.info("Clear cache")

    # Recolher a lista dos vizinhos do bootstrapper
    # A lista de vizinhos será guardada na classe Table através da variavel routingTable
    def getNeighbors(self):
        self.socketClient = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.socketClient.bind((self.address, self.clientPort))

        
        self.socketClient.sendto(str(self.HELLO).encode('utf-8'), (self.bootstrapperAddr, self.bootstrapperPort))
        self.logger.info(f"Sent HELLO to {(self.bootstrapperAddr,self.bootstrapperPort)}")
        
        listNeighbors, info_bootstrapper = self.socketClient.recvfrom(1024)
        self.logger.info(f"Receive {listNeighbors.decode('utf-8')} from {(self.bootstrapperAddr,self.bootstrapperPort)}")
        
        if listNeighbors.decode('utf-8'):
            neighbors = listNeighbors.decode('utf-8').split(';')
            for n in neighbors:
                data = n.split(':')
                info = {}
                info['node_address'] = data[0]
                info['node_port'] = data[1]
                info['client_port'] = self.clientPort
                info['alive'] = 0
                self.routingTable.add_route(info)

        else:
            self.logger.warning('No neighbors found')
        
        self.state = self.READY

    # Função para enviar repetidamente um pedido de Alive para os seus vizinhos
    # De forma a inidcar aos vizinhos que este nodo está ativo
    # Se o vizinhos não responderem a 12 pedidos de Alive o nodo é considerado inativo
    def handleAliveRequest(self):
        self.socketNode = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.socketNode.bind((self.address, self.port))

        time_to_sleep = 5
        n_requests = 0
        while self.routingTable.get_routes_down() and n_requests <= 12:
            self.sendNodeMessage(self.ALIVE)
            n_requests += 1
            time.sleep(time_to_sleep)
            self.logger.info(self.routingTable.print_routing_table())

    def listenRtp(self):		
        """Listen for RTP packets."""
        while True:
            try:
                data, addr_port = self.socketVideo.recvfrom(20480)
                listClients = self.clients.get_client_list()
                if data:
                    rtpPacket = RtpPacket()
                    decodedPacket = rtpPacket 
                    decodedPacket.decode(data)
                    currFrameNbr = decodedPacket.seqNum()
                    
                    if currFrameNbr > self.frameNbr: # Discard the late packet
                        self.frameNbr = currFrameNbr
                        threading.Thread(target=self.sendRtp, args=(rtpPacket.getPacket(), listClients)).start()
                    self.videoEnded = False
            except Exception as e:
                
                if self.videoEnded:
                    self.logger.info("Video ended")
                    self.frameNbr = 0 
                    self.state = self.READY
                    self.videoPlaying = False
                    break
                else:
                    self.logger.error("Video Not Found")
                    self.routingTable.forceResetCache()
                    self.frameNbr = 0 
                    self.state = self.READY
                    self.videoPlaying = False
                    break


    def sendRtp(self,rtpPacket, listClients):
        for client in listClients:
            if client[1] == 1:
                # TODO verificar se isto esta correto
                address = self.clients.get_client_neighbor(int(client[0]))
                port = int(self.clients.get_client_rtpPort(int(client[0])))

                self.socketClient.sendto(rtpPacket,(address, port))
                #self.logger.info(f"Sent {rtpPacket} to {(address, port)}")
                self.logger.info(f"Sent Seq Num: {self.frameNbr} to {(address, port)}")
            
    def openRtpPort(self):
        """Open RTP socket binded to a specified port."""
        # Create a new datagram socket to receive RTP packets from the server
        self.socketVideo = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        
        # Set the timeout value of the socket to 0.5sec
        self.socketVideo.settimeout(1)
        
        try:
            # Bind the socket to the address using the RTP port given by the client user
            self.socketVideo.bind((self.address, int(self.rtpPort))) 

            self.logger.info(f'RTP socket bound to address {self.address} and port {self.rtpPort}')
        except:
            self.logger.warning('Unable to Bind', 'Unable to bind PORT=%d' %self.rtpPort)

    # Envio de mensagens do Nodo consoante a variável requestCode
    def sendNodeMessage(self, requestCode):
        request : str
        
        self.rtspSeq += 1
        
        # Alive message
        if requestCode == self.ALIVE:

            threading.Thread(target=self.recvRtspReply).start()
            # Write the RTSP request to be sent.
            request = f"{self.ALIVE} {self.address} RTSP/1.0 \nCSeq: {self.rtspSeq}\nTransport: RTP/AVP;unicast;{self.rtpPort}"
            
            # Keep track of the sent request.
            self.requestSent = self.ALIVE
        
            # Envio da mensagem de ALIVE para todos os vizinhos
            for neighbor in self.routingTable.get_routes_down():
                # Utilização do socketNode para a definição de uma porta aleatória
                self.socketNode.sendto(request.encode('utf-8'), (neighbor['node_address'], int(neighbor['node_port'])))
                self.logger.info(f"Sent {request} to {(neighbor['node_address'], int(neighbor['node_port']))}")

    # Reencaminho de pacotes dos clientes
    def sendPacket(self, requestCode, data):
        """Send RTSP request to the neighbor."""	

        if requestCode == self.SETUP:
                
            listRoutes = self.routingTable.get_routes_alive()
            for route in listRoutes:
                self.socketNode.sendto(data, (route['node_address'], int(route['client_port'])))
                self.logger.info(f"Sent SETUP Packet to {(route['node_address'], int(route['client_port']))}")
            
        # Play request
        elif requestCode == self.PLAY:
            
            address = self.bestRoute[0]
            port = int(self.bestRoute[1])
            self.socketNode.sendto(data, (address, int(port)))
            self.logger.info(f"Sent PLAY Packet to {(address, port)}")
            
        # Pause request
        elif requestCode == self.PAUSE:
            # Verificar se estamos no caso em que mais ninguem quer a stream
            if self.clients.actives() == 0:
                # Enviar para a rota que nos esta a fornecer o video, ou seja, a melhor rota
                address = self.bestRoute[0]
                port = int(self.bestRoute[1])
                self.socketNode.sendto(data, (address, int(port)))
                self.logger.info(f"Sent PAUSE Packet to {(address, port)}")
            
              
        # Exit request
        elif requestCode == self.EXIT and self.state == self.PLAYING:        
            # Verificar se estamos no caso em que mais ninguem quer a stream    
            if self.clients.actives() == 0:
                # Enviar para a rota que nos esta a fornecer o video, ou seja, a melhor rota
                address = self.bestRoute[0]
                port = int(self.bestRoute[1])
                self.socketNode.sendto(data, (address, int(port)))
                self.logger.info(f"Sent EXIT Packet to {(address, port)}")
            
        else:
            return

    # Receber mensagens enviadas para a porta 3001
    # Estas mensagens podem vir de um cliente ou de outro nodo
    def recvRtspRequest(self):
        packet = Packet()
        
        self.logger.info(f"Listening on port {self.port}")
        
        while True:
            
            data, addr_port = self.socketClient.recvfrom(2048)
            if data:
                # Verificar que tipo de mensagem recebeu
                # Packet => Cliente; Mensagem => Nodo  
                if packet.isPacket(data):
                    packet.bytesToPacket(data)
                    packetID = packet.getPacketID() 
                    if packetID not in self.tracker.get_packets():
                        self.logger.info(f"Received {data} from {addr_port}")
                        self.tracker.add_packet(packetID)
                        threading.Thread(target=self.processClientRequest, args=(packet, data, addr_port)).start()
                
    # Processar uma mensagem proveniente de outro Nodo
    def processNodeRequest(self, data, addr_port : tuple):
        
        request = data.decode('utf-8').split('\n')
        
        # Tipo da mensagem
        requestType =int(request[0].split(' ')[0])
        # Ip do Nodo que enviou a mensagem
        ip_origin = request[0].split(' ')[1]
        
        # Get the media file name
        # filename = line1[1]
        
        # Get the RTSP sequence number 
        cseq = request[1].split(' ')[1]
        
        # Informações de transporte
        transport = request[2].split(' ')
        rtpPort = int(transport[1].split(';')[2])

        # Mensagem de tipo Alive recebida de um nodo
        if requestType == self.ALIVE:
            if self.state == self.READY or self.state == self.PLAYING:
                self.logger.info("processing ALIVE")
                    
                # Salvar os nodos que estão ativos
                self.routingTable.set_node_alive(ip_origin)

                # Reply informando que o nodo também está ativo
                self.replyRtsp(self.ALIVE_REPLY, cseq, 0, addr_port)

    # Processar um pacote proveniente de um Cliente
    def processClientRequest(self, packet, data, addr_port : tuple):
        
        # Tipo da mensagem
        packetID = int(packet.getPacketID())
        requestType = int(packet.getType())
        ip_origin = packet.getIpOrigin()
        rtpPort = int(packet.getRtpPort())
        cseq = packet.getSeq()

        payload = packet.getPayload()

        request = payload.split(' ')
        clientID = int(request[0])
        filename = request[1]

        # Pedidos do cliente
        if requestType == self.SETUP:
            if self.state == self.READY or self.state == self.PLAYING:
                self.logger.info("processing SETUP")

                info = {} 
                info['address'] = ip_origin
                info['port'] = int(addr_port[1])
                info['rtpPort'] = rtpPort
                info['neighbor'] = addr_port[0]
                info['videoStream'] = filename
                self.clients.new_client(clientID, info)

                # Verificar se o video pretendido é o que está a ser transmitido
                if self.videoPlaying:   
                    # Send RTSP reply
                    self.replyRtsp(self.FILE_FOUND, cseq, packetID, addr_port)
                # Caso não esteja a transmitir o video pedido
                else:
                    self.replyRtsp(self.FILE_NOT_FOUND, cseq, packetID, addr_port)
                    self.sendPacket(self.SETUP, data)

                # Add the client to the list of clients waiting for some ack response
                # self.clients.append((clientID, addr_port[0],addr_port[1], filename, 0))
                
                self.requestSent = self.SETUP 
		# Fazer um pedido ao servidor por determinado video
        elif requestType == self.PLAY:

            self.logger.info("processing PLAY")
            if not self.videoPlaying: 
                # Obter a melhor rota guardada
                self.sendPacket(self.PLAY, data)
                self.videoPlaying = True
            
                if self.state == self.READY:

                    threading.Thread(target=self.listenRtp).start()

            self.clients.change_state(clientID, self.PLAY)
            
            # request = f"PLAY {filename} RTSP/1.0\nCSeq: {cseq}\nTransport: RTP/AVP;unicast;{self.rtpPort}"
            self.requestSent = self.PLAY
            
            self.state = self.PLAYING
            # Send RTSP reply
            self.replyRtsp(self.OK, cseq, packetID, addr_port)

        # Definir o que realizar quando receber um pause
        elif requestType == self.PAUSE:
            if self.state == self.PLAYING:
                    
                self.logger.info("processing PAUSE")
                # Indicar que o cliente nao quer receber a stream
                self.clients.change_state(clientID, self.PAUSE)

                self.requestSent = self.PAUSE
                
                # Caso nenhum vizinho queira o video pedir aos nodos para o pararem de transmitir
                # Desta forma diminuimos o trafego na rede
            
            self.sendPacket(self.PAUSE, data)
            
            # Send RTSP reply
            self.replyRtsp(self.OK, cseq, packetID, addr_port)
            
		# Remover um determinado nodo do dicionario de streaming
        elif requestType == self.EXIT:
            
            self.logger.info("processing EXIT")
            self.clients.remove_client(clientID)

            self.requestSent = self.EXIT

            # Caso nenhum vizinho queira o video pedir aos nodos para o pararem de transmitir
            # Desta forma diminuimos o trafego na rede
            self.sendPacket(self.EXIT, data)

            # Send RTSP reply
            self.replyRtsp(self.OK, cseq, packetID, addr_port)
    
    
    # Receção de um reply a uma mensagem enviada anteriormente
    # Recebido pelo socketNode uma vez que foi este socket que enviou
    def recvRtspReply(self):
        """Receive RTSP reply from the server or other node."""

        while True:
            
            reply, addr_port = self.socketNode.recvfrom(4048)
            
            if reply: 
                tipo = reply.decode('utf-8').split(' ')
                if tipo[0] == str(self.ALIVE):
                    threading.Thread(target=self.processNodeRequest, args=(reply, addr_port)).start()
                    self.logger.info(f"Received {reply} from {addr_port}")

                else:
                    self.parseRtspReply(reply.decode('utf-8'), addr_port)
                    # threading.Thread(target = self.parseRtspReply, args = (reply.decode('utf-8'), addr_port))
            
    # Tratamento do Reply recebido 
    def parseRtspReply(self, data, addr_port : tuple):
        """Parse the RTSP."""
        lines = data.split('\n')
        # Código da mensagem de reply
        code = int(lines[0].split(' ')[1])
        # Tipo da mensagem de reply {OK; ALIVE; FOUND FILE; NOT FOUND FILE}
        tipo = lines[0].split(' ')[2] 
        seqNum = int(lines[1].split(' ')[1])

        # Processar mensagens com o código 200
        if code == 200: 
            if self.requestSent == self.SETUP:
                packetID = int(lines[2].split(' ')[1])
                if not self.alreadyOpen:
                    # Open RTP port.
                    self.openRtpPort()
                    self.alreadyOpen = 1

                # Se não é um reply repetido
                if packetID not in self.tracker.get_replys():
                    self.logger.info(f"Received {data} from {addr_port}")
                    if self.routingTable.get_best_route(self.address) == None:
                        # Adiciona a lista de replys respondidos
                        self.tracker.add_reply(packetID)
                        # Obter a rota do nodo
                        node = self.routingTable.get_route_by_id(addr_port[0])
                        # Guardar a rota como a melhor
                        self.routingTable.save_route(self.address, node['node_address'], node['client_port'])
                        self.logger.info(f"Best Route: ({node['node_address']}:{node['client_port']})")

                        # Salvar a melhor rota
                        # TODO alterar para quando receber mais que um video diferente 
                        self.bestRoute = (node['node_address'], node['client_port'])
                    for client in self.clients.get_client_list():
                        address = self.clients.get_client_neighbor(int(client[0]))
                        port = self.clients.get_client_port(int(client[0]))
                        if not address == addr_port[0]:
                            self.replyRtsp(self.FILE_FOUND,seqNum, packetID, (address, port))        
            elif self.requestSent == self.PLAY:
                self.state = self.PLAYING
                self.logger.info(f"Received {data} from {addr_port}")
                    
            elif self.requestSent == self.PAUSE:
                # TODO verificar o que fazer nesta situação
                self.logger.info(f"Received {data} from {addr_port}")

            
            elif self.requestSent == self.EXIT:
                self.logger.info(f"Received {data} from {addr_port}")
                if self.clients.actives() == 0:
                    self.state = self.READY

            # Caso em que o requestSent é ALIVE
            else:
                self.logger.info(f"Received {data} from {addr_port}")
                ip_origin = lines[2].split(' ')[1]
                self.routingTable.set_node_alive(ip_origin)
        elif code == 201:
            if not self.videoEnded:
                self.videoEnded = True
                self.logger.info(f"Received {data} from {addr_port}")
            
                listClients = self.clients.get_client_list()
                for client in listClients:
                    if client[1] == 1:
                        # TODO verificar se isto esta correto
                        address = self.clients.get_client_neighbor(int(client[0]))
                        port = int(self.clients.get_client_port(int(client[0])))
                        self.replyRtsp(self.END_VIDEO, self.frameNbr, 0000, (address, port))

    # Função para o envio de mensagens de acknowlegment, alive, not_found ou error
    def replyRtsp(self, code, seq, packetID, addr_port):
        """Send RTSP reply to the client."""
        if code == self.OK:    
            reply = 'RTSP/1.0 200 OK\nCSeq: ' + str(seq)+ '\nSession: ' + str(packetID)
			
            address = addr_port[0]
            port = int(addr_port[1])

            self.socketNode.sendto(reply.encode('utf-8'), (address, port))
            self.logger.info(f"Sent {reply} to {(address, port)}")

        elif code == self.ALIVE_REPLY:    
            reply = 'RTSP/1.0 200 ALIVE\nCSeq: ' + str(seq) + '\nIP: ' + self.address
			
            address = addr_port[0]
            port = int(addr_port[1])

            self.socketNode.sendto(reply.encode('utf-8'), (address, port))
            self.logger.info(f"Sent {reply} to {(address, port)}")

        
        elif code == self.FILE_FOUND:
            reply = 'RTSP/1.0 200 FOUND\nCSeq: ' + str(seq) + '\nSession: ' + str(packetID)
			
            address = addr_port[0]
            port = int(addr_port[1])

            self.socketNode.sendto(reply.encode('utf-8'), (address, port))
            self.logger.info(f"Sent {reply} to {(address, port)}")
        
        elif code == self.END_VIDEO:    
            reply = 'RTSP/1.0 201 VIDEO ENDED\nCSeq: ' + str(seq) + '\nSession: ' + str(packetID)
			
            address = addr_port[0]
            port = int(addr_port[1])

            self.socketNode.sendto(reply.encode('utf-8'), (address, port))
            self.logger.info(f"Sent {reply} to {(address, port)}")
        
        elif code == self.FILE_NOT_FOUND:    
            reply = 'RTSP/1.0 404 NOT FOUND\nCSeq: ' + str(seq) + '\nSession: ' + str(packetID)
			
            address = addr_port[0]
            port = int(addr_port[1])

            self.socketNode.sendto(reply.encode('utf-8'), (address, port))
            self.logger.info(f"Sent {reply} to {(address, port)}")
