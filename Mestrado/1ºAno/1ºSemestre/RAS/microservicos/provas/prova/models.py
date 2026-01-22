import json
import uuid
from django.db import models
from random import *

from questao.models import *

class Prova(models.Model):
    id_prova = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False, unique=True)
    nome = models.CharField(max_length=100)
    duracao = models.IntegerField()
    data = models.DateField()
    salas = models.CharField(max_length=100)
    id_docente = models.UUIDField(default=uuid.uuid4, editable=False, unique=True)
    id_alunos = models.TextField(default='[]') # list of uuid 
    hora = models.TimeField()
    nversoes = models.IntegerField(default=1)

    def __str__(self):
        return self.nome
    
    def add_aluno_id(self, new_id):
        id_list = json.loads(self.id_alunos)
        id_list.append(new_id)
        self.id_alunos = json.dumps(id_list)
        self.save()
        #return self.id_alunos

    def remove_aluno_id(self, id_to_remove):
        id_list = json.loads(self.id_alunos)
        id_list = [id_ for id_ in id_list if id_ != str(id_to_remove)]
        self.id_alunos = json.dumps(id_list)
        self.save()
        #return self.id_alunos

    def add_docente_id(self, new_id):
        id_list = json.loads(self.id_docente)
        id_list.append(new_id)
        self.id_docente = json.dumps(id_list)
        self.save()
        #return self.id_docente

    def remove_docente_id(self, id_to_remove):
        id_list = json.loads(self.id_docente)
        id_list = [id_ for id_ in id_list if id_ != str(id_to_remove)]
        self.id_docente = json.dumps(id_list)
        self.save()
        #return self.id_docente
    
    def save(self, *args, **kwargs):
        super().save(*args, **kwargs)

class Versao(models.Model):
    detalhes_prova = models.ForeignKey(Prova, on_delete=models.CASCADE)
    id_versao = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False, unique=True)
    numero_versao = models.IntegerField()  # Version number
    questoes = models.JSONField(default=list)  # Questions for this version
    alunos = models.JSONField(default=list)   # Students for this version
    
    def create_versions(self):
        for i in range(self.detalhes_prova.nversoes):
            self.numero_versao = i
            self.questoes = []
            self.alunos = []
        self.save()
        

    def randomizeQ(self):
        new_prova = Prova(
            nome=self.nome,
            duracao=self.duracao,
            data=self.data,
            salas=self.salas,
            id_docente=self.id_docente,
            hora=self.hora
        )

        new_prova.versoes_questoes = self.versoes_questoes.copy()

        self.questoes = new_prova.questoes.shuffle()
        for questao in self.questoes:
            if isinstance(questao.tipo, EscolhaMultiplaQ):
                questao.tipo.opcoes = questao.tipo.opcoes.shuffle()
        self.save()

    def insert_q(self, q):
        self.questoes.append(q)
        self.save()

            
    def remove_questao(self, q):
        if q in self.questoes:
            self.questoes.remove(q)
            self.save()


    def questoes_do_aluno(self, id_aluno):
        for aluno in self.alunos:
            if aluno.id_aluno == id_aluno:
                return aluno.questoes


    def atribuir_versoes_alunos(self):
        id_alunos_list = json.loads(self.detalhes_prova.id_alunos)
        
        shuffle(id_alunos_list) 
        
        num_versoes = self.detalhes_prova.nversoes
        alunos_per_versao = len(id_alunos_list) // num_versoes  # Distribute IDs evenly among versions
        i = 0
        while i < alunos_per_versao:
            for j in id_alunos_list:
                self.alunos.append(j)
                id_alunos_list.remove(j)
                i += 1

        self.save()