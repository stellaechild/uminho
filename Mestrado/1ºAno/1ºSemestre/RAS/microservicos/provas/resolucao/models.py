import uuid
from django.db import models

from prova.models import Versao

class Resolucao(models.Model):
    id_resolucao = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False, unique=True)
    id_aluno = models.UUIDField(default=uuid.uuid4, editable=False, unique=True) #foreign key 
    versao_prova = models.ForeignKey(Versao, on_delete=models.CASCADE)
    respostas = models.JSONField(default=list)
    
