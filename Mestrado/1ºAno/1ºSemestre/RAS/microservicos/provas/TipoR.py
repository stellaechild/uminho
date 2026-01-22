class TipoR:
    pass

class CompletEspR(TipoR):
    def __init__(self):
        self.respostas = []
    
    def addResposta(self, r, index):
        self.respostas[index] = r

    def removeResposta(self, index):
        if len(self.respostas) >= index:
            del self.respostas[index]

    def get_respostas(self):
        return self.respostas
    
    def print_respostas(self):
        for i in self.respostas:
            print(i)
    

class DesenvolvimentoR(TipoR):
    resposta = str

    def __init__(self):
        self.resposta = ""

    def get_resposta(self):
        return self.resposta
    
    def set_resposta(self,ans):
        self.resposta = ans

class EscolhaMultiplaR(TipoR):
    def __init__(self):
        self.resposta = -1 # equivale a não selecionar nada
          
    def get_resposta(self):
        return self.resposta
    
    def set_resposta(self,ans):
        self.resposta = ans


class VerdFalsoR(TipoR):
    def __init__(self):
        self.resposta = None # equivale a não selecionar nada
        
    def selecionar_verdadeiro(self):
        self.resposta = "Verdadeiro"
        
    def selecionar_falso(self):
        self.resposta = "Falso"
        
    def get_resposta(self): 
        return self.resposta
    