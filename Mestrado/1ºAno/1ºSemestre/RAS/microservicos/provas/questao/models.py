from django.db import models
import uuid

class Questao(models.Model):
    id_questao = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False, unique=True)
    descricao = models.TextField()
    imagem = models.ImageField(upload_to='questao_images/', blank=True, null=True)

    def save(self, *args, **kwargs):
        if not self.id_questao:
            self.id_questao = uuid.uuid4()
        super().save(*args, **kwargs)

    def __str__(self):
        return f"Questao {self.id_questao}"

class TipoQ(Questao):
    class Meta:
        abstract = True

class CompletEspQ(TipoQ):
    espaco_cot = models.JSONField()

    def set_espaco_cot(self, espaco, cot):
        self.espaco_cot = {'espaco': espaco, 'cot': cot}
    
    
    def muda_cot(self, espaco, cot):
        espaco_cot_pairs = self.espaco_cot
        for item in espaco_cot_pairs:
            if item['espaco'] == espaco:
                item['cot'] = cot
        self.espaco_cot = espaco_cot_pairs
        self.save()

class DesenvolvimentoQ(TipoQ):
    cot = models.FloatField()

class EscolhaMultiplaQ(TipoQ):
    opcao = models.JSONField(default=dict)
    opcoes = models.JSONField(default=list)

    def add_opcao(self, q, cot):
        self.opcao[q] = cot
        self.opcoes.append({q: cot})
        self.save()

class VerdFalsoQ(TipoQ):
    pergunta = models.CharField(max_length=255)
    opcao = models.JSONField(default=dict)

    def add_pergunta(self, pergunta, cot_v, cot_f):
        self.pergunta = pergunta
        self.opcao = {"Verdadeiro": cot_v, "Falso": cot_f}
        self.save()