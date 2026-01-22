import socket
import threading
import sys
import logger


class Bootstrapper:
    def __init__(self, bootstrapper):
        self.logger = logger.define_logger('bootstrapper')
        self.neighbors = dict()
        self.setup(bootstrapper)

    def setup(self, bootstrapperfile):
        f = open(bootstrapperfile, 'r')
        data = f.readlines()
        # Salvar a lista de vizinhos lida num dicionario (vizinhos)
        for line in data:
            line = line.strip()
            if not line or line.startswith('#'):
                pass
            else:
                parts = line.split('=')
                node = parts[0]
                for ip in parts[1].split(';'):
                    ip = ip.strip()
                    if node in self.neighbors:
                        self.neighbors[node].append(ip)
                    else:
                        self.neighbors[node] = [ip]
        
    def multi_thread_client(self, addr, s):
        try:
            neighborsList = self.neighbors[addr[0]]
            # Passagem para string para ser codificada
            response = ";".join(neighborsList) 
            # Envio da lista de vizinhos para um nodo
            s.sendto(response.encode('utf-8'), addr)
            self.logger.info(f"Sent {response} to {addr}")
        except socket.error as e:
            print(str(e))

    def bootstrapper(self, address, port):
        s : socket.socket
        msg : bytes
        addr : tuple

        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        
        s.bind((address, port))

        self.logger.info(f'Bootstrapper listening on {address}:{port}')

        while True:
            try: 
                # Receção de um pedido de obtenção de vizinhos
                msg, addr = s.recvfrom(1024)
                # Mensagem apenas serve para pedir os vizinhos, não será necessária a sua análise para já
                self.logger.info(f"Received HELLO from {addr}")
                # Utilização de uma thread para tratar os pedidos recebidos
                threading.Thread(target=self.multi_thread_client, args=(addr, s)).start()
            except socket.error:
                break

        s.close()

def main():
    port : int
    address : str


    info_bootstrapper = sys.argv[1].split(':')
    
    addr_bootstrapper = info_bootstrapper[0] 
    port_bootstrapper = int(info_bootstrapper[1])
    bootstrapperfile = sys.argv[2]

    bootstrapper = Bootstrapper(bootstrapperfile)
    
    bootstrapper.bootstrapper(addr_bootstrapper, port_bootstrapper)

if __name__ == '__main__':
    main()
