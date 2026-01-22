import threading

class Table:
    def __init__(self):
        self.lock = threading.Lock()
        self.nodes = []
        # TODO (ip, video): {addres: "", port: ""}
        self.routes = dict()
        

    def resetCache(self, state):
        # Limpa o dicionário self.routes após 90 segundos
        # Agendamento para limpar após 120 segundos
        self.lock.acquire()
        try:
            if not state == 0:
                self.routes.clear()
        finally:
            self.lock.release()

    def forceResetCache(self):
        # Limpa o dicionário self.routes após 90 segundos
        # Agendamento para limpar após 120 segundos
        self.lock.acquire()
        try:
            self.routes.clear()
        finally:
            self.lock.release()

    # Save an available route in the database
    def add_route(self, obj):
        self.lock.acquire()
        try:
            if any(node.get('node_address') == obj.get('node_address') for node in self.nodes):
                # Node with node_address already exists
                pass   
            else:
                self.nodes.append(obj)
        finally: 
            self.lock.release()

    def set_node_alive(self, node_address):
        self.lock.acquire()
        try:
            for node in self.nodes:
                if node['node_address'] == node_address and node['alive'] == 0:
                    node['alive'] = 1
                    break
        finally:
            self.lock.release()

    def get_route_by_id(self, ip_neighbor):
        self.lock.acquire()
        try:
            for node in self.nodes:
                if node.get('node_address') == ip_neighbor:
                    return node
            return None
        finally:
            self.lock.release()

    def get_routes(self):
        self.lock.acquire()
        try:
            return self.nodes
        finally:
            self.lock.release()


    def get_routes_alive(self):
        self.lock.acquire()
        try:
            list_routes_alive = [route for route in self.nodes if route.get('alive') == 1]
            return list_routes_alive
        finally:
            self.lock.release()

    def get_routes_down(self):
        self.lock.acquire()
        try:
            list_routes_down = [route for route in self.nodes if route.get('alive') == 0]
            return list_routes_down
        finally:
            self.lock.release()
    
    # TODO acrescentar o facto de poderem ser videos diferentes
    def save_route(self, ip, address_best_neighbor, port_best_neighbor):
        self.lock.acquire()
        try:
            self.routes[ip] = (address_best_neighbor, port_best_neighbor)
        finally:
            self.lock.release()

    # TODO acrescentar o facto de poderem ser videos diferentes
    # Returns None se não encontrar o ip
    def get_best_route(self, ip):
        self.lock.acquire()
        try:
            return self.routes.get(ip)
        finally:
            self.lock.release()

    def show_routing_table(self):
        self.lock.acquire()
        try:
            print("Routing Table:")
            for route in self.nodes:
                print(f"  - Destination: {route['node_address']}:{route['node_port']}, Alive: {route['alive']}")
        finally:
            self.lock.release()
    
    def print_routing_table(self):
        self.lock.acquire()
        try:
            string = "Routing Table: "
            for route in self.nodes:
                string += "{" + f"Destination: {route['node_address']}:{route['node_port']}, Alive: {route['alive']}" + "}" + "; "
            return string
        finally:
            self.lock.release()

class Tracker:
    def __init__(self):
        self.lock = threading.Lock()
        self.packets = []
        self.replys = []
    
    def get_packets(self):
        self.lock.acquire()
        try:
            return self.packets
        finally:
            self.lock.release()

    def get_replys(self):
        self.lock.acquire()
        try:
            return self.replys
        finally:
            self.lock.release()
        
    def add_packet(self, packet):
        self.lock.acquire()
        try:
            self.packets.append(packet)
        finally:
            self.lock.release()
    
    def add_reply(self, reply):
        self.lock.acquire()
        try:
            self.replys.append(reply)
        finally:
            self.lock.release()

    def show_packets(self):
        self.lock.acquire()
        try:
            print("Stored Packets:")
            for packet in self.packets:
                print(packet)
        finally:
            self.lock.release()
    
    def show_replys(self):
        self.lock.acquire()
        try:
            print("Stored Replys:")
            for reply in self.replys:
                print(reply)
        finally:
            self.lock.release()
    
    