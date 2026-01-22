import socket
import threading
import database

def processamento1(msg : bytes, addr : tuple, s : socket.socket,cenas : database.database ): 
    print(f"Recebi {msg.decode('utf-8')} e recebi isto do {addr}")
    
    cenas.acrescentar(addr)
    
    # Função para enviar uma mensagem pelo mesmo socket
    # Recebe dois argumentos, a mensagem a enviar
    s.sendto("Correu bem!".encode('utf-8'), addr)


def processamento2(msg : bytes, addr : tuple, s : socket.socket, cenas : database.database ): 
    print("SUCESSO!")
    
    cenas.remover(addr)
    s.sendto("SUCESSO".encode('utf-8'), addr)


def servico1(cenas : database.database):
    s : socket.socket
    endereco : str
    porta : int
    msg : bytes
    addr : tuple

    # Dois argumentos, a familia do socker Ipv4, e o tipo de socket, datagram uma vez que utilizamos udp
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # hardcoded, podiamos escrever em ficheiro ou utilizar o bootstrapper
    endereco = '10.0.0.10'

    porta = 3000
    
    # associar um endereco e uma porta ao socket
    s.bind((endereco, porta))

    print(f'Estou à escuta em {endereco}:{porta}')

    # Permitir que o servidor esteja sempre à escuta
    # O socket apenas é fechado em caso de uma exceção genérica
    while True:
        try: 
            # Esta função devolve a mensagem que recebemos e qual o endereco e porta, num tuplo, do dispositivo que a enviou
            # Argumento é o numero em bytes para o buffer que recebe a mensagem
            msg, addr = s.recvfrom(1024)

            # Esta função executa numa nova thread
            threading.Thread(target=processamento1, args=(msg, addr, s, cenas)).start()


        except socket.error:
            break

    s.close()


def servico2(cenas : database.database):
    s : socket.socket
    endereco : str
    porta : int
    msg : bytes
    addr : tuple

    # Dois argumentos, a familia do socker Ipv4, e o tipo de socket, datagram uma vez que utilizamos udp
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # hardcoded, podiamos escrever em ficheiro ou utilizar o bootstrapper
    endereco = '10.0.0.10'

    porta = 4000
    
    # associar um endereco e uma porta ao socket
    s.bind((endereco, porta))

    print(f'Estou à escuta em {endereco}:{porta}')

    # Permitir que o servidor esteja sempre à escuta
    # O socket apenas é fechado em caso de uma exceção genérica
    while True:
        try: 
            # Esta função devolve a mensagem que recebemos e qual o endereco e porta, num tuplo, do dispositivo que a enviou
            # Argumento é o numero em bytes para o buffer que recebe a mensagem
            msg, addr = s.recvfrom(1024)

            # Esta função executa numa nova thread
            threading.Thread(target=processamento2, args=(msg, addr, s, cenas)).start()


        except socket.error:
            break

    s.close()


def servico3(cenas : database.database):
    while True:
        cenas.show()


def main():
    cenas = database.database()
    threading.Thread(target=servico1, args=(cenas,)).start()
    threading.Thread(target=servico2, args=(cenas,)).start()
    threading.Thread(target=servico3, args=(cenas,)).start()

if __name__ == '__main__':
    main()