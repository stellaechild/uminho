from TipoQ import *
import uuid

class Questao:
    def __init__(self, descricao, tipo, imagem=None):
        self.id_questao = uuid.uuid4()
        self.descricao = descricao
        self.tipo = tipo 
        self.imagem = imagem

    def get_id_questao(self):
        return self._id_questao

    def set_id_questao(self, novo_id_questao):
        self._id_questao = novo_id_questao

    def get_descricao(self):
        return self._descricao

    def set_descricao(self, nova_descricao):
        self._descricao = nova_descricao

    def get_tipo(self):
        return self._tipo

    def set_tipo(self, novo_tipo):
        self._tipo = novo_tipo

    def get_imagem(self):
        return self._imagem

    def set_imagem(self, nova_imagem):
        self._imagem = nova_imagem
        