from random import randint
import sys, traceback, threading, socket
import logger


from VideoStream import VideoStream
from RtpPacket import RtpPacket
from Packet import Packet


class Server:
	# Tipo de mensagens
	SETUP = 0
	PLAY = 1
	
	# state
	INIT = 0
	READY = 1
	PLAYING = 2
	state = INIT

	# Tipo de replys
	OK = 0
	FILE_NOT_FOUND = 3
	VIDEO_LIST = 4
	END_VIDEO = 5
	

	def __init__(self, server_addr, server_port, videos):
		self.logger = logger.define_logger(server_addr)
		self.serverDic = {}
		self.rpInfo = {}
		self.address = server_addr
		self.port = server_port
		self.listVideos = videos
		self.socket = None
		self.frameNbr = 0

	def run(self):
		threading.Thread(target=self.recvRtspRequest).start()
	
	"""Receive RTSP request from the client."""
	def recvRtspRequest(self):
		self.logger.info(f'Listening on port {self.port}' )
		self.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
		self.socket.bind((self.address, self.port))
		while True:
			try:
				data, addr_port = self.socket.recvfrom(1024)
				self.logger.info(f"Received {data.decode('utf-8')} from {addr_port}")
			
				if data:
					self.rpInfo['socketInfo'] = addr_port
					self.rpInfo['packet'] = data
					threading.Thread(target=self.processRtspRequest, args=(data.decode("utf-8"),)).start()
			except socket.error:
				break

	"""Process RTSP request sent from the client."""
	def processRtspRequest(self, data):
		# Get the request type
		request = data.split('\n')
		line1 = request[0].split(' ')
		requestType = int(line1[0])
		
		
		# Get the RTSP sequence number 
		seq = request[1].split(' ')
		transport = request[2].split(' ')
		rtpPort = transport[1].split(';')[2]
		
		# Process SETUP request
		if requestType == self.SETUP:
			# Update state
			self.logger.info("Processing SETUP")
			
			address = self.rpInfo['socketInfo'][0]
			port = int(self.rpInfo['socketInfo'][1])
			
			# Send RTSP reply
			self.replyRtsp(self.VIDEO_LIST, seq[1])
			self.state = self.READY

		# Process PLAY request 		
		elif requestType == self.PLAY:
			# Get the media file name
			filename = line1[1]
			# Tanto pode receber um play do rp quando esta no inicio ou quando ja acabou a stream
			if self.state == self.READY:
				self.logger.info("Processing PLAY")
				
				self.state = self.PLAYING
				
				# TODO Try/catch para verificar se o video existe
				self.rpInfo['videoStream'] = VideoStream("videos/" + filename)
				self.rpInfo['rtpPort'] = rtpPort
				
				# Create a new thread and start sending RTP packets
				self.rpInfo['event'] = threading.Event()
				self.rpInfo['worker']= threading.Thread(target=self.sendRtp) 
				self.rpInfo['worker'].start()
				
				self.replyRtsp(self.OK, seq[1])


	"""Send RTP packets over UDP."""
	def sendRtp(self):
		while True:
			self.rpInfo['event'].wait(0.05) 
			
			# Stop sending if request is PAUSE or EXIT
			if self.rpInfo['event'].isSet(): 
				break 
				
			#data = self.rpInfo['videoStream'].nextFrame()
			data = self.rpInfo['videoStream'].next_frame()
			if data == None:
				self.logger.info("Video ended")
				self.state = self.READY
				self.replyRtsp(self.END_VIDEO, self.frameNbr)
				self.frameNbr = 0
				break
			else:
				#frameNumber = self.rpInfo['videoStream'].frameNbr()
				frameNumber = self.rpInfo['videoStream'].frame_nbr()
				try:
					address = self.rpInfo['socketInfo'][0]
					port = int(self.rpInfo['rtpPort'])
					self.frameNbr += 1
					self.socket.sendto(self.makeRtp(data, frameNumber),(address,port))
					# self.logger.info(f"Sent {data} to {(address, port)}")
					self.logger.info(f"Sent Seq Num: {frameNumber} to {(address, port)}")
				except:
					print("Connection Error")
					print('-'*60)
					traceback.print_exc(file=sys.stdout)
					print('-'*60)

	"""RTP-packetize the video data."""
	def makeRtp(self, payload, frameNbr):
		version = 2
		padding = 0
		extension = 0
		cc = 0
		marker = 0
		pt = 26 # MJPEG type
		seqnum = frameNbr
		ssrc = 0 
		
		rtpPacket = RtpPacket()
		
		rtpPacket.encode(version, padding, extension, cc, seqnum, marker, pt, ssrc, payload)
		
		return rtpPacket.getPacket()
		
	"""Send RTSP reply to the client."""
	def replyRtsp(self, code, seq):
		if code == self.OK:

			reply = 'RTSP/1.0 200 OK\nCSeq: ' + str(seq)
			
			address = self.rpInfo['socketInfo'][0]
			port = int(self.rpInfo['socketInfo'][1])
			
			self.socket.sendto(reply.encode('utf-8'), (address, port))
			self.logger.info(f"Sent {reply} to {(address, port)}")

		elif code == self.VIDEO_LIST:
			
			videos = ";".join(self.listVideos) 
			reply = 'RTSP/1.0 200 VIDEO LIST\nCSeq: ' + str(seq) + '\nFiles: ' + videos
			
			address = self.rpInfo['socketInfo'][0]
			port = int(self.rpInfo['socketInfo'][1])
			
			self.socket.sendto(reply.encode('utf-8'), (address, port))
			self.logger.info(f"Sent {reply} to {(address, port)}")

		elif code == self.END_VIDEO:
			
			reply = 'RTSP/1.0 201 VIDEO ENDED\nCSeq: ' + str(seq)
			
			address = self.rpInfo['socketInfo'][0]
			port = int(self.rpInfo['socketInfo'][1])
			
			self.socket.sendto(reply.encode('utf-8'), (address, port))
			self.logger.info(f"Sent {reply} to {(address, port)}")

		# Error messages
		elif code == self.FILE_NOT_FOUND:
			self.logger.error("404 NOT FOUND")

		elif code == self.CON_ERR_500:
			self.logger.error("500 CONNECTION ERROR")
