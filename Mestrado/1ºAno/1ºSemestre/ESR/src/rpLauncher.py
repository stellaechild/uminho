import sys, socket
from RP import RP

class rpLauncher:	
	
	def main(self):
		rp_port : int
		try:
			info_bootstrapper = sys.argv[1].split(':')
			info_rp = sys.argv[2].split(':')
			
			address_bootstrapper = info_bootstrapper[0]
			port_bootstrapper = int(info_bootstrapper[1])
			
			rp_address = info_rp[0]
			rp_port = int(info_rp[1])

			rtp_port = int(sys.argv[3])
		except:
			print("[Usage: rpLauncher.py rp_port]\n")

		RP(address_bootstrapper,port_bootstrapper,rp_address,rp_port, rtp_port).run()

if __name__ == "__main__":
	(rpLauncher()).main()