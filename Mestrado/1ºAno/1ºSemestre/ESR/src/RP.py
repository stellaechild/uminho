from random import randint
import sys, traceback, threading, socket
import time
import logger

from RtpPacket import RtpPacket
from Packet import Packet
from ConnectedClients import ConnectedClients
from infoServers import InfoServers

from database import Table
from database import Tracker

import subprocess

class RP:
    # message type
    SETUP = 0
    PLAY = 1
    PAUSE = 2
    EXIT = 3
    HELLO = 4
    ALIVE = 5

    # states
    INIT = 0
    READY = 1
    PLAYING = 2
    state = INIT
    
    # reply types
    OK = 0
    ALIVE_REPLY = 1
    FILE_FOUND = 2
    FILE_NOT_FOUND = 3
    END_VIDEO = 5

    def __init__(self, addr_bootstrapper, port_bootstrapper,address, port, rtp_port):
        self.logger = logger.define_logger(address)
        self.bootstrapperAddr = addr_bootstrapper
        self.bootstrapperPort = port_bootstrapper
        self.serverAddr = None
        self.serverPort = 0
        self.videos = dict()
        self.address = address
        self.port = port
        self.rtpPort = rtp_port
        self.socketClient = None
        self.socketServer = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.socketVideo = None
        self.requestSent = 0
        self.clients = ConnectedClients()
        self.servers = InfoServers()
        self.routingTable = Table()
        self.tracker = Tracker()
        self.frameNbr = 0
        self.metric = 0.0
        self.packetsLoss = 0
        self.latency = 0.0
        self.socketOpen = False
        self.videoPlaying = False

    def run(self):
        self.getServers()
        threading.Thread(target=self.handleSetupServers()).start()
        threading.Thread(target=self.checkMetric()).start()
    
    # Pedir ao bootstrapper a lista de servidores
    def getServers(self):
        self.socketClient = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.socketClient.bind((self.address, self.port))

        self.socketClient.sendto(str(self.HELLO).encode('utf-8'), (self.bootstrapperAddr, self.bootstrapperPort))
        self.logger.info(f"Sent HELLO to {(self.bootstrapperAddr,self.bootstrapperPort)}")

        listServers, info_bootstrapper = self.socketClient.recvfrom(1024)
        self.logger.info(f"Receive {listServers.decode('utf-8')} from {(self.bootstrapperAddr,self.bootstrapperPort)}")
        
        # Guardar a lista de servidores na variavel self.servers da classe InfoServes
        if listServers.decode('utf-8'):
            servers = listServers.decode('utf-8').split(';')
            for s in servers:
                info_server = s.split(':')
                info = {}
                info["port"] = info_server[1]
                info["state"] = self.state
                info["metrica"] = 0
                info["videos"] = []
                info["state"] = 0
                self.servers.acrescentaServidor(info_server[0],info)
        else:
            self.logger.warning('No servers found')

        self.state = self.READY
    
    # Função para enviar pedidos de setup enquanto os dois servidores nao estiverem ligados
    def handleSetupServers(self):
        time_to_sleep = 10
        n_requests = 0
        while self.servers.any_server_down() and n_requests <= 6:
            self.setup()
            n_requests += 1
            time.sleep(time_to_sleep)
            
    # Realização de um setup inicial com o servidor para obtenção da lista de vídeos disponivel
    # Após o setup o servidor passa para o estado READY
    def setup(self):
        
        request = f"{self.SETUP} RTSP/1.0\nCSeq: 0\nTransport: RTP/AVP;unicast;{self.rtpPort}"
            
        # Envio de mensagem de setup para todos os servidores
        for server in self.servers.get_all_inactive_servers():
            self.socketServer.sendto(request.encode('utf-8'), (server[0], int(server[1])))
            self.logger.info(f"Sent Request for SETUP to {server[0], server[1]}")
            
        # Mudança de estado
        self.requestSent = self.SETUP
        threading.Thread(target=self.recvRtspReply).start()
    
    # Função que a cada 15 segundos lança uma thread para executar o cálculo da métrica       
    def checkMetric(self):
        try:
            while True:
                for server in self.servers.get_allServers():
                    threading.Thread(target=self.edit_metrica, args=(server[0],)).start()
                time.sleep(15)
                
        except Exception as e:
            self.logger.error(f"Erro ao executar ping: {e}")
    
    # Função que calcula a latencia mediae a perda de pacotes através do envio de 4 pacotes de ping
    def verificar_latencia(self, ip_servidor):
        try:
            # Executa o comando ping
            resultado = subprocess.run(['ping', '-c', '4', ip_servidor], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)

            # Analisa a saída para extrair a latência
            if '100% packet loss' in resultado.stderr:
                perda_pacotes = 100.0
            else:
                # Extrai a porcentagem de perda de pacotes
                perda_pacotes = float(resultado.stdout.split('packet loss')[0].split()[-1].strip('%'))
                
            # Extrai a latência média
            latencia_media = float(resultado.stdout.split('rtt min/avg/max/mdev = ')[1].split('/')[1])

            return latencia_media, perda_pacotes

        except Exception as e:
            self.logger.error(f"Erro ao executar ao verificar latencia: {e}")
            return None, None

    # Função que calcula a metrica composta tendo como base a latencia e a perda de pacotes
    def calcular_metrica_composta(self, latencia, perda_pacotes):
        # Normaliza a perda de pacotes para estar entre 0 e 1
        perda_normalizada = perda_pacotes / 100.0

        # Calcula a métrica composta
        metrica = (0.8 * latencia) + (0.2 * perda_normalizada)

        return metrica
    
    # Função que edita a métrica de cada servidor, definindo assim, de 15 em 15 segundos, qual melhor servidor para transmitir a stream 
    def edit_metrica(self, ip_servidor):
        latencia, perda = self.verificar_latencia(ip_servidor)
        resultado = self.calcular_metrica_composta(latencia,perda)
        self.servers.set_metrica(ip_servidor, resultado)
        self.logger.info(f"Ping {ip_servidor}: {resultado}" )

    # Função que aguarda por um pedido tanto de um cliente como de um pacote        
    def recvFromClients(self):
        packet = Packet()
        self.logger.info(f"Listening on port {self.address,self.port}")
        
        while True:
            # Servidor à escuta na porta definida para a receção de pedidos ({self.port})
            data, addr_port = self.socketClient.recvfrom(2048)
        
            if data:
                # Verificar se recebemos um packet
                if packet.isPacket(data):
                    packet.bytesToPacket(data)
                    packetID = packet.getPacketID() 
                    
                    # Verificar se o id do packet não é repetido
                    # Responder só se não for repetido
                    if packetID not in self.tracker.get_packets():
                        self.logger.info(f"Received {data} from {addr_port}")
                        self.tracker.add_packet(packetID)

                        threading.Thread(target=self.processClientRequest, args=(packet, data, addr_port)).start()

                # Caso não recebemos um packet, recebemos uma mensagem de outro nodo
                else:
                    self.logger.info(f"Received {data} from {addr_port}")
                    threading.Thread(target=self.processNodeRequest, args=(data, addr_port)).start()
    
    # Função à escuta de mensagens Rtp (video)
    def listenRtp(self):		
        while True:
            try:
                data, addr_port = self.socketVideo.recvfrom(20480)
                # Recolher a lista de clientes
                listClients = self.clients.get_client_list()
                if data:
                    rtpPacket = RtpPacket()

                    # Utilização de uma variável à parte unicamente para a obtenção do seqNum
                    decodedPacket = rtpPacket 
                    decodedPacket.decode(data)
                    currFrameNbr = decodedPacket.seqNum()
                
                    # Discard the late packet
                    if currFrameNbr > self.frameNbr: 
                        self.frameNbr = currFrameNbr     
                        threading.Thread(target=self.sendRtp, args=(rtpPacket.getPacket(), listClients)).start()
                          
            except:
                self.logger.info("Video ended")
                self.frameNbr = 0
                self.state = self.READY
                self.videoPlaying = False
                break
    
    # Função responsável por enviar pacotes Rtp para todos os clientes ativos
    def sendRtp(self, rtpPacket, listClients):
        for client in listClients:
            # Verificar se o cliente quer receber a stream (alive == 1)
            if client[1] == 1:
                
                # Obter o endereço do nodo/cliente que lhe pediu a stream
                address = self.clients.get_client_neighbor(client[0])
                # Obter a porta do nodo/cliente que lhe pediu a stream
                port = self.clients.get_client_rtpPort(client[0])

                self.socketClient.sendto(rtpPacket,(address, port))
                self.logger.info(f"Sent Seq Num: {self.frameNbr} to {(address, port)}")

    # Abertura do socket Rtp numa porta especifica ({self.rtpPort})
    def openRtpPort(self):
        
        # Criação de um socket Udp para receber pacotes Rtp do servidor
        self.socketVideo = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        
        # Definir um valor de timeout para o socket
        self.socketVideo.settimeout(0.5)
        
        try:
            # Bind do socket a um endereço e porta. 
            self.socketVideo.bind(('', self.rtpPort)) 

            self.logger.info(f'RTP socket bound to address {self.address} and port {self.rtpPort}')
        except:
            self.logger.warning('Unable to Bind', 'Unable to bind PORT=%d' %self.rtpPort)

    # Processamento de uma mensagem proveniente de um Nodo
    def processNodeRequest(self, data, addr_port : tuple):
        
        request = data.decode('utf-8').split('\n')
        
        # Tipo da mensagem
        requestType = int(request[0].split(' ')[0])
        ip_origin = request[0].split(' ')[1]
    
        # Get the RTSP sequence number 
        cseq = request[1].split(' ')[1]
        
        transport = request[2].split(' ')
        rtpPort = int(transport[1].split(';')[2])

        # Tipo de mensagem Alive 
        if requestType == self.ALIVE:
            if self.state == self.READY or self.state == self.PLAYING:
                self.logger.info("Processing ALIVE")
                    
                # Salvar os nodos que estão ativos
                self.routingTable.set_node_alive(ip_origin)
                self.replyRtsp(self.ALIVE_REPLY, cseq, 0, 0, addr_port)

    # Processamento de um packet proveniente de um cliente
    def processClientRequest(self, packet, data, addr_port : tuple):
        
        # Recolher o id do Packet
        packetID = int(packet.getPacketID())
        requestType = int(packet.getType())
        ip_origin = packet.getIpOrigin()
        rtpPort = int(packet.getRtpPort())
        cseq = packet.getSeq()

        payload = packet.getPayload()

        request = payload.split(' ')
        clientID = request[0]
        filename = request[1]
        
        # Receção de um pacote de setup com o RP
        # Implica adicionar um novo cliente ao dicionario de rotas
        if requestType == self.SETUP:
            if self.state == self.READY or self.state == self.PLAYING:
                self.logger.info("Processing SETUP")
                
                info = {} 
                info['address'] = ip_origin
                info['port'] = int(addr_port[1])
                info['rtpPort'] = rtpPort
                info['neighbor'] = addr_port[0]
                print(info)
                # Verificar se o RP tem o video pedido
                if filename in self.servers.get_allVideos():
                    info['videoStream'] = filename
                    # Envia um reply RTSP 
                    self.replyRtsp(self.FILE_FOUND, cseq, clientID, packetID, addr_port)
                else:
                    # Envia um reply RTSP 
                    self.replyRtsp(self.FILE_NOT_FOUND, cseq,  clientID, packetID, addr_port)
				
                # Adicionar um novo cliente a base de dados da classe ConnectionClients
                self.clients.new_client(clientID, info)
            
            
		# Receção de um pacote de PLAY por de parte do cliente
        # Fazer um pedido ao servidor por determinado video
        elif requestType == self.PLAY:
            # Receção de um pedido de Play no estado READY
            self.logger.info("Processing PLAY")
            if self.state == self.READY:

                # Colocar uma thread à escuta de pacotes Rtps
                threading.Thread(target=self.listenRtp).start()
            
            if not self.videoPlaying:
                self.videoPlaying = True

                request = f"{self.PLAY} {filename} RTSP/1.0\nCSeq: {cseq}\nTransport: RTP/AVP;unicast;{self.rtpPort}"

                # verificar para qual servidor fazer o pedido  através da métrica
                bestServer = self.servers.getServerbyVideo(filename)
                self.logger.info(f"Best Server: {bestServer}")
                self.serverAddr = bestServer[0]
                self.serverPort = int(bestServer[1])
                
                # Envio de um pedido de play ao servidor
                self.socketServer.sendto(request.encode('utf-8'), (self.serverAddr, self.serverPort))
                self.logger.info(f"Sent {request} to {(self.serverAddr, self.serverPort)}")
                
            self.requestSent = self.PLAY
            
            self.clients.change_state(clientID, self.PLAY)
            # Envia um reply RTSP
            self.replyRtsp(self.OK, cseq, clientID, packetID, addr_port)

        # Definir o que realizar quando receber um pause
        elif requestType == self.PAUSE:
            if self.state == self.PLAYING:
                    
                self.logger.info("Processing PAUSE")
                # Indicar que o cliente nao quer receber a stream
                self.clients.change_state(clientID, self.PAUSE)

                # Envia um reply RTSP
                self.replyRtsp(self.OK, cseq, clientID, packetID, addr_port)
                
		# Remover um determinado nodo do dicionario de streaming
        elif requestType == self.EXIT:
            
            self.logger.info("Processing EXIT")

            # Mudar o estado do cliente para EXIT (3)
            self.clients.change_state(clientID, self.EXIT)
            # Envia um reply RTSP
            self.replyRtsp(self.OK, cseq, clientID, packetID, addr_port)
                
    # Receção de replys RTSP do servidor
    def recvRtspReply(self):
        while True:
            reply, addr_port = self.socketServer.recvfrom(2024)
            self.logger.info(f"Received {reply.decode('utf-8')} from {addr_port}")
            if reply: 
                self.parseRtspReply(reply.decode('utf-8'), addr_port)
    

    # Processamento do Reply recebido 
    def parseRtspReply(self, data, addr_port : tuple):
        # Parse do reply RTSP recebido do servidor
        lines = data.split('\n')
        # Código da mensagem de reply
        code = int(lines[0].split(' ')[1])
        # Tipo da mensagem de reply {OK; ALIVE; FOUND FILE; NOT FOUND FILE}
        tipo = lines[0].split(' ')[2] 
        
        # Processamento de mensagens com código 200
        if code == 200: 
            if self.requestSent == self.SETUP:
                # Mudar o estado do servidor caso ele responda
                self.servers.set_state(addr_port[0],1)

                if not self.socketOpen:
                    # Apenas fica a espera de clientes quando receber a resposta do servidor
                    threading.Thread(target=self.recvFromClients).start()
                    # Abrir porta RTP
                    self.openRtpPort() 
                    # Mudar a flag
                    self.socketOpen = True

                # Parse da lista de videos recebida
                videos = lines[2].split(' ')

                # Guardar a lista de videos do endereço presente em addr_port[0]
                self.videos[addr_port[0]] = videos[1].split(';')
                
                # Alteração da informação do servidor, junção da lista de videos
                listVideos = videos[1].split(';')
                for video in listVideos:
                    self.servers.acrescentaVideo(addr_port[0], video)
                
            
            elif self.requestSent == self.PLAY:
                self.state = self.PLAYING

            # Reply de uma mensagem de Alive
            else:
                ip_origin = lines[2].split(' ')[1]
                self.routingTable.set_node_alive(ip_origin)  
        elif code == 201:
            listClients = self.clients.get_client_list()
            for client in listClients:
                if client[1] == 1:
                    # TODO verificar se isto esta correto
                    address = self.clients.get_client_neighbor(client[0])
                    port = int(self.clients.get_client_port(client[0]))
                    self.replyRtsp(self.END_VIDEO, self.frameNbr, 0000, 0000, (address, port))
                    

	# Função para o envio de mensagens de reply (acknowlegment, alive, not_found, error)
    def replyRtsp(self, code, seq, clientID, packetID, addr_port):
        
        if code == self.OK:
            reply = 'RTSP/1.0 200 OK\nCSeq: ' + str(seq) + '\nSession: ' + str(packetID)  
			
            address = addr_port[0]
            port = int(addr_port[1])

            self.socketClient.sendto(reply.encode('utf-8'), (address, port))
            self.logger.info(f"Sent {reply} to {(address, port)}")
        
        elif code == self.ALIVE_REPLY:    
            reply = 'RTSP/1.0 200 ALIVE\nCSeq: ' + str(seq) + '\nIP: ' + self.address
			
            address = addr_port[0]
            port = int(addr_port[1])

            self.socketClient.sendto(reply.encode('utf-8'), (address, port))
            self.logger.info(f"Sent {reply} to {(address, port)}")

        elif code == self.FILE_FOUND:
            reply = 'RTSP/1.0 200 FOUND\nCSeq: ' + str(seq) + '\nSession: ' + str(packetID)
			
            address = addr_port[0]
            port = int(addr_port[1])

            self.socketClient.sendto(reply.encode('utf-8'), (address, port))
            self.logger.info(f"Sent {reply} to {(address, port)}")

        elif code == self.END_VIDEO:
            reply = 'RTSP/1.0 201 VIDEO ENDED\nCSeq: ' + str(seq) + '\nSession: ' + str(packetID)
			
            address = addr_port[0]
            port = int(addr_port[1])

            self.socketClient.sendto(reply.encode('utf-8'), (address, port))
            self.logger.info(f"Sent {reply} to {(address, port)}")

        # Error messages
        elif code == self.FILE_NOT_FOUND:
            self.logger.error("404 NOT FOUND")

