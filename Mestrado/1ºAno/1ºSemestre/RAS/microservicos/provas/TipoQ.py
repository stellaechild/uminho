class TipoQ:
   pass

class CompletEspQ(TipoQ):
    valor_espaco = tuple
    espaco = str
    cot = 0.0
    espacos = []
    
    def mudaCot(self,espaco,cot):
        for i in self.respostas:
            if i[0] == espaco :
                i[1] = cot

    

class DesenvolvimentoQ(TipoQ):
    def __init__(self, cot):
        self.cot = cot
    
    def get_cot (self):
        return self.cot
    
    def set_cot (self,c):
        self.cot = c

    # def printRes(self):
    #    print(f"Resposta: {self.resposta}, Cotação Atribuída: {self.cot}")
        
    

class EscolhaMultiplaQ(TipoQ):
    def __init__(self):
        self.opcao = {}
        self.opcoes = []
        
    def nova_opcao(self, q, cot):
        nova = self.opcao[q] = cot
        self.opcoes.append(nova)
    
    def get_opcoes(self):
        return self.opcoes

    def get_cot(self, q):
        return self.opcao[q]
    
    def set_cot(self, q, cot):
        self.opcao[q] = cot
            
    
    #def imprimir_opcoes(self):
    #    for idx, opcao in enumerate(self.opcoes, start=1):
    #        print(f"Opção {idx}: {opcao}")




class VerdFalsoQ(TipoQ):
    def __init__(self, pergunta):
        self.pergunta = pergunta
        self.opcao = {}  
        
    def nova_pergunta(self, pergunta, cot_v, cot_f):
        self.pergunta = pergunta
        self.opcao = {"Verdadeiro": cot_v, "Falso": cot_f} 
        
    def get_cot(self, op):
        if op == "Verdadeiro":
            return self.opcao["Verdadeiro"]
        elif op == "Falso":
            return self.opcao["Falso"]
        else:
            print("Opção inválida")
        
         
    def imprimir_pergunta(self):
        print(f"{self.pergunta}")
        