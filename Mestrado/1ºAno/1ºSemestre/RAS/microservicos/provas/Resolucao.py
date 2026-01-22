from Prova import Prova
from Resposta import Resposta
from TipoR import *

class Resolucao:
    def __init__(self, id_aluno):
        self.id_aluno = id_aluno
        self.questoes = Prova.questoes_do_aluno(id_aluno)
        self.respostas = {}
    
    def set_respostas(self):
        for q in self.questoes:
            self.respostas[q] = Resposta(q)
            
      
    
        
        
    