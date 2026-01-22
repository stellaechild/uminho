from TipoR import *

class Resposta:
    def __init__(self, Questao):
        self.questao = Questao
        self.tipo = set_tipo()
        self.resposta = set_resposta()
        
        def set_tipo(self):
            if self.questao.tipo == "CompletEspQ":
                self.tipo = CompletEspR()
            elif self.questao.tipo == "DesenvolvimentoQ":
                self.tipo = DesenvolvimentoR()
            elif self.questao.tipo == "EscolhaMultiplaQ":
                self.tipo = EscolhaMultiplaR()
            elif self.questao.tipo == "VerdFalsoQ":
                self.tipo = VerdFalsoR()
            else:   
                print("Tipo de questão inválido")
        
        def set_resposta(self, resposta):
            if self.questao.tipo == "CompletEspQ":
                self.tipo = CompletEspR().get_respostas()
            elif self.questao.tipo == "DesenvolvimentoQ":
                self.tipo = DesenvolvimentoR().get_resposta()
            elif self.questao.tipo == "EscolhaMultiplaQ":
                self.tipo = EscolhaMultiplaR().get_resposta()
            elif self.questao.tipo == "VerdFalsoQ":
                self.tipo = VerdFalsoR().get_resposta()
            else:   
                print("Tipo de questão inválido")       
        