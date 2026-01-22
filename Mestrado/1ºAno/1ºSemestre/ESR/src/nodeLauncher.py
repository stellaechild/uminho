import sys, socket
from Node import Node

class nodeLauncher:	
	
	def main(self):
		node_port : int
		try:
			info_bootstrapper = sys.argv[1].split(':')
			info_node = sys.argv[2].split(':')
			
			address_bootstrapper = info_bootstrapper[0]
			port_bootstrapper = int(info_bootstrapper[1])
			
			node_address = info_node[0]
			node_port = int(info_node[1])

			client_port = int(sys.argv[3])
			rtp_port = int(sys.argv[4])

		except:
			print("[Usage: rpLauncher.py bootstrapper_address:bootstrapper_port node_address:node_port client_port rtp_port]\n")

		Node(address_bootstrapper,port_bootstrapper, node_address, node_port, client_port, rtp_port).run()

if __name__ == "__main__":
	(nodeLauncher()).main()