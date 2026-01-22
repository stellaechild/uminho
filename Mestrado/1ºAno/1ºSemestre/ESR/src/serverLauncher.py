import sys, socket
from Server import Server
# from Server import Server

class ServerLauncher:	
	def read_files(self, file_name):
		video_list = []
		try:
			with open(file_name, 'r') as file:
				for line in file:
					video_list.append(line.strip())
		except FileNotFoundError:
			print("[File Not Found]")
		return video_list

	def main(self):
		server_port : int
		try:
			info_server = sys.argv[1].split(':')
			server_address = info_server[0]
			server_port = int(info_server[1])

			videos = self.read_files(sys.argv[2])
		except:
			print("[Usage: serverLauncher.py server_address:server_port videos.txt]\n")
		
		Server(server_address,server_port,videos).run()

if __name__ == "__main__":
	(ServerLauncher()).main()