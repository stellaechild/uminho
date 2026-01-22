from django.db import models
from questao.models import *
from resolucao.models import Resolucao


class Resposta(models.Model):
    id_resposta = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False, unique=True)
    id_aluno = models.UUIDField(default=uuid.uuid4, editable=False, unique=True)
    questao = models.ForeignKey(Questao, on_delete=models.CASCADE)
    resolucao = models.ForeignKey(Resolucao, on_delete=models.CASCADE)
    
    def add_resposta_resolucao(self, r):
        self.resolucao.respostas.append(r)
        self.resolucao.save()
        
    def remove_resposta_resolucao(self, r):
        self.resolucao.respostas.remove(r)
        self.resolucao.save()
        
    class Meta:
        unique_together = ('questao', 'resolucao')

class TipoR(models.Model):
    resposta = models.OneToOneField(Resposta, on_delete=models.CASCADE, primary_key=True)
    
    class Meta:
        abstract = True
        

class CompletEspR(TipoR):
    resposta_esp = models.JSONField(default=list)

    def add_resposta(self, r, index):
        if index < len(self.resposta_esp):
            self.resposta_esp[index] = r
            self.save()

    def remove_resposta(self, index):
        if index < len(self.resposta_esp):
            del self.resposta_esp[index]
            self.save() 

class DesenvolvimentoR(TipoR):
    resposta_des = models.CharField(max_length=600)
    
    def set_resposta(self, new_resposta):
        self.resposta_des = new_resposta

class EscolhaMultiplaR(TipoR):
    resposta_esc = models.IntegerField(null=True)
    
    def set_resposta(self, value):
        self.resposta_esc = value
        self.save()

class VerdFalsoR(TipoR):
    resposta_vf = models.CharField(max_length=20, null=True, blank=True)

    def selecionar_verdadeiro(self):
        self.resposta = "Verdadeiro"
        self.save()  

    def selecionar_falso(self):
        self.resposta = "Falso"
        self.save()  
