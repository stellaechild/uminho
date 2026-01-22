import uuid
from django.db import models
from resposta.models import *
from resolucao.models import *

class Correcao(models.Model):
    id_correcao = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False, unique=True)
    docentes_associados = models.JSONField(default=list)
    resolucao = models.ForeignKey(Resolucao, on_delete=models.CASCADE)
    cotacao = models.JSONField(default=dict)
    nota_final = models.FloatField(default=0)
    
    def add_docente(self, id_docente):
        self.docentes_associados.append(id_docente)
        self.save()
        
    def remove_docente(self, id_docente):
        self.docentes_associados.remove(id_docente)
        self.save()
        
    def set_cot_des(self, r, cot):
        if r.isinstance(DesenvolvimentoR):
            self.cotacao[r] = cot
            self.save()
    
    def set_cot_esp_single(self, r, index, cot):
        if r.isinstance(CompletEspR):
            self.cotacao[r] = {r.resposta_esp[index]: cot}
            self.save() 

    def corr_auto(self):
        for r in self.resolucao.respostas:
            if r.isinstance(EscolhaMultiplaR):
                self.cotacao[r] = r.questao.opcao[r.resposta_esc]
                r.save()
            elif r.isinstance(VerdFalsoR):
                self.cotacao[r] = r.questao.opcao[r.resposta_vf]
                r.save()
            else:
                pass
            self.save()
    
    def cotacao_final(self):
        cot_fin = sum(self.cotacao.values())
        if cot_fin < 0:
            self.nota_final = 0
        else :
            self.nota_final = cot_fin
        self.save()