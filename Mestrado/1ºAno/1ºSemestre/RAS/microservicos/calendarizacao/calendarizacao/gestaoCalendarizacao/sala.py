from typing import List
import uuid
from provas import Prova

class Sala:
    def __init__(self, capacidade, edificio, numero, andar):
        self._idSala = uuid.uuid4()
        self._capacidade = capacidade
        self._edificio = edificio
        self._numero = numero
        self._andar = andar
        self._provas = []  # Lista de instâncias de Prova

    def get_id_sala(self):
        return self._idSala

    def set_id_sala(self, novo_id_sala):
        self._idSala = novo_id_sala

    def get_capacidade(self):
        return self._capacidade

    def set_capacidade(self, nova_capacidade):
        self._capacidade = nova_capacidade

    def get_edificio(self):
        return self._edificio

    def set_edificio(self, novo_edificio):
        self._edificio = novo_edificio

    def get_numero(self):
        return self._numero

    def set_numero(self, novo_numero):
        self._numero = novo_numero

    def get_andar(self):
        return self._andar

    def set_andar(self, novo_andar):
        self._andar = novo_andar

    def get_provas(self):
        return self._provas

    def set_provas(self, novas_provas):
        self._provas = novas_provas

    def adicionar_prova(self, nova_prova):
        self._provas.append(nova_prova)

    def remover_prova(self, prova):
        if prova in self._provas:
            self._provas.remove(prova)

    def imprimir_sala(self):
        print(f"ID Sala: {self._idSala}")
        print(f"Capacidade: {self._capacidade}")
        print(f"Edifício: {self._edificio}")
        print(f"Número: {self._numero}")
        print(f"Andar: {self._andar}")
        print(f"Provas: {self._provas}")

    def iterar_provas(self):
        for prova in self._provas:
            prova.imprimir_prova()
