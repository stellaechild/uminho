import logger
import time
import subprocess
class Packet():
    #tipo 0 : connection ; 3 : unconnected
    def __init__(self,**args):
        if "bytes" not in args:
            self.packetID = args.get("packetID", 0)
            self.type = args.get("type",0)
            self.ip_origin = args.get("ip_origin", '')
            self.ip_destination = args.get("ip_destination", '')
            self.port = args.get("port", 0)
            self.rtpPort = args.get("rtpPort", 0)
            self.seq = args.get("seq",0)
            self.payload = args.get("payload", '')
        else:
            self.bytesToPacket(args.get("bytes"))


    def getPacketID(self):
        return self.packetID
    
    def getType(self):
        return self.type

    def getIpDestination(self):
        return self.ip_destination

    def getIpOrigin(self):
        return self.ip_origin
    
    def getSeq(self):
        return self.seq

    def getRtpPort(self):
        return self.rtpPort

    def getPayload(self):
        return self.payload

    def bytesToPacket(self, bytes):
        msg = bytes.decode('utf8').split(';')
        self.packetID = msg[0]
        self.type = msg[1]
        self.ip_origin = msg[2]
        self.ip_destination = msg[3]
        self.port = msg[4]
        self.rtpPort = msg[5]
        self.seq = msg[6]
        self.payload = msg[7]

    def packetToBytes(self):
        msg = (";".join([
            str(self.packetID), 
            str(self.type), 
            str(self.ip_origin), 
            str(self.ip_destination), 
            str(self.port),
            str(self.rtpPort),
            str(self.seq),
            str(self.payload)
        ])) + ";"
        return msg.encode('utf8')

    def toString(self):
        return ";".join([str(self.packetID), str(self.type), str(self.ip_origin), str(self.ip_destination), str(self.port),
                        str(self.rtpPort), str(seq), str(self.payload)])

    
    def setType(self, type):
        self.type = type

    def setIpDestino(self,ip):
        self.ip_destination = ip

    def setIpOrigin(self, ip):
        self.ip_destination = ip

    def printInfo(self):
        print(self.packetID)
        print(self.type)
        print(self.ip_origin)
        print(self.ip_destination)
        print(self.port)
        print(self.rtpPort)
        print(self.seq)
        print(self.payload)

    def isPacket(self,message):
        parts = message.decode('utf-8').split(';')  # Split the message by ';'
        # Check if the message has the expected number of parts
        if len(parts) == 9:  # Adjust this condition based on your `Packet` structure
            return True
        else:
            return False