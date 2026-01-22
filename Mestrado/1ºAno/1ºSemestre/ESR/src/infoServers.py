from tkinter import *
from tkinter import messagebox
from typing import Any
from PIL import Image
import socket, threading, sys, traceback, os
import logger

from RtpPacket import RtpPacket
from Packet import Packet
import subprocess
import time

class InfoServers:
    lock : threading.Lock 
    size : int #size do dicionario
    listVideos = [str]
    state : int
    metrica : float #calculada no rp
    ipServer : int #chave
    servers : dict

    def __init__(self):
        self.lock = threading.Lock()
        self.size = 0
        self.listVideos = []
        self.state = 0
        self.metrica = 0.0
        self.serverPort = 0
        self.ipServer = 0
        self.servers = dict()


    def get_size(self):
        return self.size
    
    def any_server_down(self):
        self.lock.acquire()
        try:
            for ip, server_info in self.servers.items():
                state = server_info.get('state', 0)
                if state == 0:
                    return True  # Se encontrar um servidor inativo, retorna True
        finally:
            self.lock.release()
        return False  # Caso não encontre nenhum servidor inativo, retorna False

    def get_all_inactive_servers(self):
        inactive_servers = []
        self.lock.acquire()
        try:
            for ip, server_info in self.servers.items():
                state = server_info.get('state', 0)
                if state == 0:
                    inactive_servers.append((ip, server_info["port"]))  # Adiciona o servidor inativo à lista
        finally:
            self.lock.release()
        return inactive_servers
        
    def get_allServers(self):
        result = []
        for key, value in self.servers.items():
            result.append((key, value['port']))
        return result
    
    def getServerbyVideo(self,video):
        # verifica que servidor tem o video pedido
        # se ambos tiverem recolhe a metrica de ambos para saber qual o melhor
        # retorna um tuplo (server_address, server_port), informações sobre o melhor servidor para transmitir o video

        best_server = None
        best_metrica = float('inf')  # Start with a high metrica value

        self.lock.acquire()
        try:
            for ip, server_info in self.servers.items():
                videos = server_info['videos']

                if video in videos:
                    metrica = server_info['metrica']
                    # Check if the current server has a better metrica
                    if metrica < best_metrica:
                        best_metrica = metrica
                        best_server = ip
        finally:
            self.lock.release()

        if best_server is not None:
            return best_server, self.servers[best_server]['port']
        else:
            return None  # No server has the requested video


    def get_listVideos(self,ip):
        return self.servers[ip]['videos']

    def get_allVideos(self):
        videosList = []
        for value in self.servers.values():
            videosList.extend(value['videos'])
        print(videosList)
        return videosList
        
    def get_port(self,ip):
        return self.servers[ip]['port']
    
    def get_state(self,ip):
        return self.servers[ip]['state']
    
    def get_metrica(self,ip):
        return self.servers[ip]['metrica']
    
    def set_size(self,s):
        self.lock.acquire()
        self.size = s
        self.lock.release()
    
    def acrescentaVideo(self,ip,video):
        self.lock.acquire()
        try:
            self.servers[ip]['videos'].append(video)
        finally:
            self.lock.release()
    
    def remover_video(self, ip, video):
        self.lock.acquire()
        try: 
            video_list = self.servers[ip]['videos']

            if video in video_list:
                video_list.remove(video)
            else:
                print(f"Video '{video}' not found in server '{ip}'.")
        finally:
            self.lock.release()
    

    def set_port(self,ip,port):
        self.lock.acquire()
        try:
            self.servers[ip]['port'] = port
        finally:
            self.lock.release()
    

    def set_state(self,ip,s):
        self.lock.acquire()
        try:
            self.servers[ip]['state'] = s
        finally:
            self.lock.release()
    
    def set_metrica(self,ip,m):
        self.lock.acquire()
        try:
            self.servers[ip]['metrica'] = m
        finally:
            self.lock.release()


    def acrescentaServidor(self, ip, objeto):
        self.lock.acquire()
        try:
            self.size+=1 
            self.servers[ip] = objeto
        finally:
            self.lock.release()

    def removeServidor(self, ip):
        self.lock.acquire()
        try:
            self.size-=1 
            self.servers.pop(ip)
        finally:
            self.lock.release()
    
    def show(self):
        self.lock.acquire()
        try:
            print(f"Tenho armazenado informação de {self.size} servidor/es.\n")

            for chave,valor in self.servers.items():
                print(f"O servidor com ip {chave} tem as restantes informações : {valor}")
                time.sleep(2)
            print("")
        finally:
            self.lock.release()
        
        time.sleep(3)