import socket
import sys
import logger

logger = logger.define_logger('client')
neighbors_list = []

def connect_to_neighbor(s : socket.socket, endereco : str, porta : int):
    connect_msg : str
    
    # Tipo de mensagem para conexão
    connect_msg = "Connect"

    # Obtenho a lista de vizinhos
    print(neighbors_list)
    for neighbor in neighbors_list:
        # Envio para todos os vizinhos da lista
        s.sendto(connect_msg.encode('utf-8'), (neighbor,30001))
        logger.info(f"Sent {connect_msg} to {(neighbor,30001)}")
    
    # Espero obter um primeira resposta
    msg, addr = s.recvfrom(1024)
    logger.info(f"Received {msg_decoded} from {addr}")

def main():
    s : socket.socket
    endereco : str
    porta : int
    msg : str
    msg_received : str
    addr : str

    # Abrir um socket
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    
    # Recolha dos dados para onde a mensagem será enviada
    addr_port = sys.argv[1]
    ap = addr_port.split(':')
    endereco = ap[0]
    porta = int(ap[1])
    
    # Tipo de mensagem para obtenção de vizinhos
    msg = "Hello"

    # Envio de pedido de vizinhos
    s.sendto(msg.encode('utf-8'), (endereco,porta))
    logger.info(f"Sent {msg} to {(endereco,porta)}")
    
    # Receção da resposta ao pedido de vizinhos
    msg_received, addr = s.recvfrom(1024)
    msg_decoded = msg_received.decode('utf-8')
    logger.info(f"Received {msg_decoded} from {addr}")

    # Separar a string recebida e guarda-la numa lista
    # neighbors = str(msg_decoded).split(';')

    # Adiciono os vizinhos a lista de vizinhos
    #for ip in neighbors:
    #    neighbor_list.append(ip)

    # Função auxiliar para conexão de um nodo
    # connect_to_neighbor(s,endereco,porta)

    # Adicionar a parte de pedir para se conectar ao seu vizinho
    # Adicionar a parte de pedir a stream

    # Fechar um socket
    s.close()

if __name__ == '__main__':
    main()