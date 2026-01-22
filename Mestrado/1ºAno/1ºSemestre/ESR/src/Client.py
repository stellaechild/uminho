from tkinter import *
from tkinter import messagebox
from random import randint
from PIL import Image, ImageTk
import socket, threading, sys, traceback, os
import logger
import time 
import os

from RtpPacket import RtpPacket
from Packet import Packet
import subprocess

CACHE_FILE_NAME = "cache-"
CACHE_FILE_EXT = ".jpg"

class Client:
    INIT = 0
    READY = 1
    PLAYING = 2
    state = INIT
    
    SETUP = 0
    PLAY = 1
    PAUSE = 2
    EXIT = 3
    
    HELLO = 4


    def __init__(self, master, filename, address_bootstrapper, port_bootstrapper, clientAddr, rtpport):
        self.logger = logger.define_logger(clientAddr)
        self.master = master
        self.master.protocol("WM_DELETE_WINDOW", self.handler)
        self.createWidgets()
        self.info_neighbors = []
        # Generate a random Id for the client
        self.clientID = randint(100000, 999999)
        self.bootstrapperAddr = address_bootstrapper
        self.bootstrapperPort = int(port_bootstrapper)
        self.serverAddr = None
        self.serverPort = None
        self.clientAddr = clientAddr
        self.rtpPort = int(rtpport)
        self.fileName = filename
        self.rtspSeq = 1
        self.packetId = 0
        self.requestSent = -1
        self.teardownAcked = 0
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.socketVideo = None
        self.frameNbr = 0
        self.videoEnded = False
        self.setupMovie()
    
    def createWidgets(self):
        """Build GUI."""
        # Create Play button		
        self.start = Button(self.master, width=20, padx=3, pady=3)
        self.start["text"] = "Play"
        self.start["command"] = self.playMovie
        self.start.grid(row=1, column=1, padx=2, pady=2)
        
        # Create Pause button			
        self.pause = Button(self.master, width=20, padx=3, pady=3)
        self.pause["text"] = "Pause"
        self.pause["command"] = self.pauseMovie
        self.pause.grid(row=1, column=2, padx=2, pady=2)
        

        # Create a label to display the movie
        self.label = Label(self.master, height=19)
        self.label.grid(row=0, column=0, columnspan=4, sticky=W+E+N+S, padx=5, pady=5) 


    def setupMovie(self):
        """Setup button handler."""
        if self.state == self.INIT:
            # Procurar a lista de vizinhos
            self.getNeighbors()
            self.sendRtspRequest(self.SETUP)
    
    def exitClient(self):
        """Teardown button handler."""
        self.sendRtspRequest(self.EXIT)		
        try:
            self.master.destroy() # Close the gui window
            os.remove(CACHE_FILE_NAME + str(self.clientID) + CACHE_FILE_EXT) # Delete the cache image from video
        except:
            self.logger.warning(f"Can't delete the cache image from video: {CACHE_FILE_NAME + str(self.clientID) + CACHE_FILE_EXT}" )

    def pauseMovie(self):
        """Pause button handler."""
        if self.state == self.PLAYING:
            self.sendRtspRequest(self.PAUSE)

    def playMovie(self):
        """Play button handler."""
        if self.state == self.READY:
            # Create a new thread to listen for RTP packets
            threading.Thread(target=self.listenRtp).start()
            self.playEvent = threading.Event()
            self.playEvent.clear()
            self.sendRtspRequest(self.PLAY)
    
    def listenRtp(self):		
        """Listen for RTP packets."""
        while True:
            try:
                # NAO DIMINUIR VALOR DO BUFFER, valor minimo para correr o video "movie.Mjpeg"
                data, addr_port = self.socketVideo.recvfrom(20480)
                if data:
                    rtpPacket = RtpPacket()
                    rtpPacket.decode(data)
                
                    currFrameNbr = rtpPacket.seqNum()
                        
                    if currFrameNbr > self.frameNbr: # Discard the late packet
                        self.frameNbr = currFrameNbr
                        self.logger.info("Current Seq Num: " + str(currFrameNbr))
                        self.updateMovie(self.writeFrame(rtpPacket.getPayload()))
                    
                    self.videoEnded = False
            except Exception as e:
                # Stop listening upon requesting PAUSE or TEARDOWN
                if self.playEvent.isSet(): 
                    self.logger.info("Video Paused")
                    break
                # Quando o video termina
                else:
                    # Aguardar pela ultima mensagem
                    time.sleep(2)
                    if self.videoEnded:
                        self.logger.info("Video Ended")
                        self.state = self.INIT
                        break 

                    else:
                        self.logger.error("Video Not Found")
                        self.socketVideo.close()
                        self.state = self.INIT
                        self.sendRtspRequest(self.SETUP)
                        break

    def writeFrame(self, data):
        """Write the received frame to a temp image file. Return the image file."""
        cachename = CACHE_FILE_NAME + str(self.clientID) + CACHE_FILE_EXT
        file = open(cachename, "wb")
        file.write(data)
        file.close()
        
        return cachename
    
    def updateMovie(self, imageFile):
        """Update the image file as video frame in the GUI."""
        photo = ImageTk.PhotoImage(Image.open(imageFile))
        self.label.configure(image = photo, height=288) 
        self.label.image = photo

    def sendRtspRequest(self, requestCode):
        """Send RTSP request to the server."""	

        request : str
        # Generate a random id for the packet
        packetID = randint(100000, 999999)
        self.socket.settimeout(5)
        # Setup request
        if requestCode == self.SETUP and self.state == self.INIT:

            # Update RTSP sequence number.
            threading.Thread(target=self.recvRtspReply).start()
            
            self.rtspSeq += 1
            
            # Write the RTSP request to be sent.
            request = f"{self.clientID} {self.fileName}"
            packet = Packet(
                packetID = packetID,
                type = self.SETUP,
                ip_origin = self.clientAddr,
                ip_destination = self.serverAddr,
                port = self.serverPort,
                rtpPort = self.rtpPort,
                seq = self.rtspSeq,
                payload = request
            )

            self.packetId = packetID
            # Keep track of the sent request.
            self.requestSent = self.SETUP

            for neighbor in self.info_neighbors:
                self.socket.sendto(packet.packetToBytes(), (neighbor[0], int(neighbor[1])))
                self.logger.info(f"Sent SETUP Packet to {(neighbor[0], int(neighbor[1]))}")
            
        # Play request
        elif requestCode == self.PLAY and self.state == self.READY:
            # Update RTSP sequence number.
            self.rtspSeq += 1
            
            # Write the RTSP request to be sent.
            request = f"{self.clientID} {self.fileName}"
            
            packet = Packet(
                packetID = packetID,
                type = self.PLAY,
                ip_origin = self.clientAddr,
                ip_destination = self.serverAddr,
                port = self.serverPort,
                rtpPort = self.rtpPort,
                seq = self.rtspSeq,
                payload = request
            )
            self.packetId = packetID
            # Keep track of the sent request.
            self.requestSent = self.PLAY
            self.state = self.PLAYING

            # Send the RTSP request using rtspSocket.
            self.socket.sendto(packet.packetToBytes(), (self.serverAddr, self.serverPort))
            self.logger.info(f"Sent PLAY Packet to {(self.serverAddr, self.serverPort)}")


        # Pause request
        elif requestCode == self.PAUSE and self.state == self.PLAYING:
            # Update RTSP sequence number.
            self.rtspSeq += 1
            
            # Write the RTSP request to be sent.
            request = f"{self.clientID} {self.fileName}"
            
            packet = Packet(
                packetID = packetID,
                type = self.PAUSE,
                ip_origin = self.clientAddr,
                ip_destination = self.serverAddr,
                port = self.serverPort,
                rtpPort = self.rtpPort,
                seq = self.rtspSeq,
                payload = request
            )
            self.packetId = packetID
            # Keep track of the sent request.
            self.requestSent = self.PAUSE

            # Send the RTSP request using rtspSocket.
            self.socket.sendto(packet.packetToBytes(), (self.serverAddr, self.serverPort))
            self.logger.info(f"Sent PAUSE Packet to {(self.serverAddr, self.serverPort)}")


        # Exit request
        elif requestCode == self.EXIT :
            # Update RTSP sequence number.
            self.rtspSeq += 1
            
            # Write the RTSP request to be sent.
            request = f"{self.clientID} {self.fileName}"
            
            packet = Packet(
                packetID = packetID,
                type = self.EXIT,
                ip_origin = self.clientAddr,
                ip_destination = self.serverAddr,
                port = self.serverPort,
                rtpPort = self.rtpPort,
                seq = self.rtspSeq,
                payload = request
            )
            self.packetId = packetID
            # Keep track of the sent request.
            self.requestSent = self.EXIT
            # Send the RTSP request using rtspSocket.
            self.socket.sendto(packet.packetToBytes(), (self.serverAddr, self.serverPort))
            self.logger.info(f"Sent EXIT Packet to {(self.serverAddr, self.serverPort)}")
        

    def recvRtspReply(self):
        """Receive RTSP reply from the server."""
        
        while True:
            try:
                reply, addr_port = self.socket.recvfrom(2024)
                # Desligar o timeout predefinido quando recebo um reply
                self.logger.info("Timeout Canceled")
                self.socket.settimeout(None)
                if reply: 
                    if self.requestSent == self.SETUP:
                        self.serverAddr = addr_port[0]
                        for neighbor in self.info_neighbors:
                            if neighbor[0] == self.serverAddr:
                                self.serverPort = int(neighbor[1])

                    self.parseRtspReply(reply.decode('utf-8'), addr_port)
                    
                    if self.requestSent == self.EXIT and self.state == self.INIT:
                        os._exit(0)
                
            # Caso de erro
            except socket.timeout:
                self.socket.close()
                self.logger.error("Timeout when sending RTSP request")
                os._exit(0)
                break
   

    def parseRtspReply(self, data, addr_port):
        """Parse the RTSP reply from the server."""
        lines = data.split('\n')
        code = int(lines[0].split(' ')[1]) 
        seqNum = int(lines[1].split(' ')[1])
        
        # Process only if the server reply's sequence number is the same as the request's
        if seqNum == self.rtspSeq:
            packetID = int(lines[2].split(' ')[1])
            # Process only if the packet ID is the same
            if self.packetId == packetID:
                if code == 200: 
                    if self.requestSent == self.SETUP:
                        # Update RTSP state.
                        self.state = self.READY
                        # Open RTP port.
                        self.openRtpPort() 
                    elif self.requestSent == self.PLAY:
                        self.state = self.PLAYING
                    
                    elif self.requestSent == self.PAUSE:
                        self.state = self.READY
                        # The play thread exits. A new thread is created on resume.
                        self.playEvent.set()
                    
                    elif self.requestSent == self.EXIT:
                        self.state = self.INIT 
                        #v self.socket.close()
                        # Flag the teardownAcked to close the socket.
                        self.socketVideo.close()
                        self.logger.info("Socket Closed")      
                        self.socket.close()
                        #break
            self.logger.info(f"Received{data} from {addr_port}")
                
        if code == 201:
            if not self.videoEnded:
                self.logger.info(f"Received{data} from {addr_port}")
                self.videoEnded = True
                
    def openRtpPort(self):
        """Open RTP socket binded to a specified port."""
        # Create a new datagram socket to receive RTP packets from the server
        self.socketVideo = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        
        # Set the timeout value of the socket to 0.5sec
        self.socketVideo.settimeout(0.5)
        
        try:
            # Bind the socket to the address using the RTP port given by the client user
            # '' assumes the address of the machine running
            self.socketVideo.bind((self.clientAddr, self.rtpPort)) 

            self.logger.info('RTP socket bound to port %d' % self.rtpPort)
        except:
            messagebox.showwarning('Unable to Bind', 'Unable to bind PORT=%d' %self.rtpPort)

    def handler(self):
        """Handler on explicitly closing the GUI window."""
        self.pauseMovie()
        if messagebox.askokcancel("Quit?", "Are you sure you want to quit?"):
            self.exitClient()
        else: # When the user presses cancel, resume playing.
            self.playMovie()

    def getNeighbors(self):
            
            self.socket.settimeout(5)
            self.socket.sendto(str(self.HELLO).encode('utf-8'), (self.bootstrapperAddr, self.bootstrapperPort))
            self.logger.info(f"Sent HELLO to {(self.bootstrapperAddr,self.bootstrapperPort)}")
            try:    
                listNeighbors, info_bootstrapper = self.socket.recvfrom(1024)
                self.logger.info(f"Receive {listNeighbors.decode('utf-8')} from {(self.bootstrapperAddr,self.bootstrapperPort)}")
                
                if listNeighbors.decode('utf-8'):
                    neighbors = listNeighbors.decode('utf-8').split(';')
                    for n in neighbors:
                        self.info_neighbors.append(n.split(':'))
                else:
                    self.logger.warning('No neighbors found')
            except socket.timeout:
                self.logger.error("Can't reach bootstrapper")
                exit()




