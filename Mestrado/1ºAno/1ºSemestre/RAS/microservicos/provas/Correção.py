from provas.Resolucao import Resolucao
from provas.Resposta import Resposta
from provas.TipoQ import *
from provas.TipoR import *

class Correcao:
    def __init__(self, id_aluno):
        self.id_aluno = id_aluno
        self.prova = Resolucao(id_aluno)
        self.cotacao_total = 0
        
    def atribuir_cotacao_des(self, q, cot):
        if q.isinstance(DesenvolvimentoQ):
            self.cotacao_total += cot
            
    def atribuir_cotacao_esc_mult(self, q):
        if q.isinstance(EscolhaMultiplaQ):
            resposta = Resposta(q)
            if resposta.EscolhaMultiplaR().get_resposta() == -1:
                self.cotacao_total += 0
            else:
                self.cotacao_total += q.get_cot(resposta.EscolhaMultiplaR().get_resposta())
                
    def atribuir_cotacao_verd_falso(self, q):
        if q.isinstance(VerdFalsoQ):
            resposta = Resposta(q)
            if resposta.VerdFalsoR().get_resposta() == None:
                self.cotacao_total += 0
            else:
                self.cotacao_total += q.get_cot(resposta.VerdFalsoR().get_resposta())
                
    def atribuir_cotacao_complet_esp(self, q, cot):
        if q.isinstance(CompletEspQ):
            self.cotacao_total += cot
    
    def atribuir_cotacao_auto(self):
        for q in self.prova.questoes:
            if q.isinstance(EscolhaMultiplaQ):
                self.atribuir_cotacao_esc_mult(q)
            elif q.isinstance(VerdFalsoQ):
                self.atribuir_cotacao_verd_falso(q)
            else:
                print("Tipo de questão inválido")    
        if self.cotacao_total < 0:
            self.cotacao_total = 0
        return self.cotacao_total
    
            
