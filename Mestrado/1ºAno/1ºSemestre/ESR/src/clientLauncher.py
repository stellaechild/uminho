import sys
from tkinter import Tk
from Client import Client
# from Client import Client


if __name__ == "__main__":
    try:
        info_bootstrapper = sys.argv[1].split(':')
        info_client = sys.argv[2].split(':')
        filename = sys.argv[3]
        
        address_bootstrapper = info_bootstrapper[0]
        port_bootstrapper = int(info_bootstrapper[1])
        client_address = info_client[0]
        rtp_port = info_client[1]
    except:
        print("[Usage: clientLauncher.py bootstrapper_address:bootstrapper_port ip_client:rtp_port video]\n")


    tk = Tk()

    app = Client(tk, filename, address_bootstrapper, port_bootstrapper, client_address, rtp_port)
    app.master.title("RTPClient")
    tk.mainloop()