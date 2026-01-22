import threading

class ConnectedClients:
    def __init__(self):
        self.clientsDict = dict()
        self.clientsList = [] # Lista de tuplos com o estado de cada cliente
        self.lock = threading.Lock()  

    def new_client(self, clientID, client: object):
        self.lock.acquire()  
        try:
            # Área crítica protegida pelo lock: Inserção de novo cliente no dicionário e na lista
            self.clientsDict[clientID] = client
            self.clientsList.append((clientID, 0)) #(IdCLiente, estado)
        finally:
            self.lock.release()  

    def remove_client(self, clientID):
        self.lock.acquire()
        try:
            # Área crítica protegida pelo lock: Remoção do cliente do dicionário e da lista
            if clientID in self.clientsDict:
                del self.clientsDict[clientID]
            
            index = 0
            while index < len(self.clientsList):
                client, _ = self.clientsList[index]
                if client == clientID:
                    removed_client = self.clientsList.pop(index)
                else:
                    index += 1
        finally:
            self.lock.release()

    def get_client_info(self, clientID):
        self.lock.acquire()
        try:
            return self.clientsDict[clientID]
        finally:
            self.lock.release()    
    def get_client_rtpPort(self, clientID):
        self.lock.acquire()
        try:
            return self.clientsDict[clientID]['rtpPort']
        finally:
            self.lock.release()    
    def get_client_address(self, clientID):
        self.lock.acquire()
        try:
            return self.clientsDict[clientID]['address']
        finally:
            self.lock.release()            
    def get_client_port(self, clientID):
        self.lock.acquire()
        try:
            return self.clientsDict[clientID]['port']
        finally:
            self.lock.release()    
    def get_client_hops(self, clientID):
        self.lock.acquire()
        try:
            return self.clientsDict[clientID]['hops']
        finally:
            self.lock.release()    
    def get_client_neighbor(self, clientID):
        self.lock.acquire()
        try:
            return self.clientsDict[clientID]['neighbor']
        finally:
            self.lock.release()    
    def get_client_list(self):
        self.lock.acquire()
        try:
            return self.clientsList
        finally:
            self.lock.release()            

    def change_state(self, clientID, state):
        self.lock.acquire()
        try:
            # Área crítica protegida pelo lock: Manipulação da lista para pausar um cliente
            for index, (client,_) in enumerate(self.clientsList):
                if client == clientID:
                    self.clientsList.pop(index)
                    self.clientsList.append((clientID, state))
        finally:
            self.lock.release()


    # Verificar se existem clientes ativos
    def actives(self):
        ativo = 0
        for client in self.clientsList:
            if client[1] == 1:
                ativo = 1      
        return ativo