from Questao import Questao
from TipoQ import *
from contas import *
from calendarizacao import *
import uuid
import random
from datetime import datetime

class Prova:
    def __init__(self, nome, duracao, data, salas, id_docente, hora):
        self.id_prova = uuid.uuid4()
        self.nome = nome
        self.id_alunos = []
        self.duracao = duracao
        self.data = data
        self.salas = salas
        self.id_docente = id_docente
        self.hora = hora
        self.nversoes = 1
        self.retroceder = True # true se se poder retroceder, false se não

        self.versoes_questoes = self.createVersions(self.nversoes) # dicionario que contem as questoes de cada uma das versoes
        self.versoes_alunos = self.set_versoes_alunos() # dicionario que contem a versao de cada um dos alunos
        
        self.resolucoes = []

    def get_id_prova(self):
        return self.id_prova

    def set_id_prova(self, novo_id_prova):
        self.id_prova = novo_id_prova

    def get_nome(self):
        return self.nome

    def set_nome(self, novo_nome):
        self.nome = novo_nome

    def get_id_alunos(self):
        return self.id_alunos

    def set_id_alunos(self, novos_id_alunos):
        self.id_alunos = novos_id_alunos
    
    def add_aluno(self, aluno_id):
        self.id_alunos.append(aluno_id)
    
    def remove_aluno(self, aluno_id):
        if aluno_id in self.id_alunos:
            self.id_alunos.remove(aluno_id)

    def get_duracao(self):
        return self.duracao

    def set_duracao(self, nova_duracao):
        self.duracao = nova_duracao

    def get_data(self):
        return self.data

    def set_data(self, nova_data):
        self.data = nova_data

    def get_salas(self):
        return self.salas

    def set_salas(self, novas_salas):
        self.salas = novas_salas

    def get_id_docente(self):
        return self.id_docente

    def set_id_docente(self, novo_id_docente):
        self.id_docente = novo_id_docente

    def get_hora(self):
        return self.hora

    def set_hora(self, nova_hora):
        self.hora = nova_hora
        
    def get_nversoes(self):
        return self.nversoes
    
    def set_nversoes(self, novo_nversoes):
        self.nversoes = novo_nversoes

    def get_retroceder(self):
        return self.retroceder
    
    def set_retroceder(self, novo_retroceder):
        self.retroceder = novo_retroceder
        
    def get_versoes_alunos(self):
        return self.versoes_alunos
    
    def set_versoes_alunos(self):
        for aluno in self.id_alunos:
            versao = random.choice(list(self.versoes_alunos.keys()))
            self.versoes_alunos[versao]["alunos"].append(aluno) 
    
    def print_prova(self, versao):
        print(f"ID Prova: {self.id_prova}")
        print(f"Nome: {self.nome}")
        print(f"ID Alunos: {self.id_alunos}")
        print(f"Duracao: {self.duracao}")
        print(f"Data: {self.data}")
        print(f"Salas: {self.salas}")
        print(f"ID Docente: {self.id_docente}")
        print(f"Hora: {self.hora}")
        print(f"Versão: {versao}")
        print(f"Questoes: {self.versoes_questoes[versao]}")

    def iterate_id_alunos(self):
        for aluno_id in self.id_alunos:
            print(f"Aluno ID: {aluno_id}")

    def consultarProvaCorrigida(self):
        print(f"Consultar Prova Corrigida {self.id_prova}...")

        for questao in self.versoes_questoes['questoes']:
            print(f"\nQuestão {questao.id_questao} :")
            print(f"Descrição: {questao.descricao}")
            print(f"Tipo de Questão: {questao.tipo}")

            if isinstance(questao.tipo, CompletEspQ):
                CompletEspQ.printRespostas(questao.tipo.respostas)

            elif isinstance(questao.tipo, DesenvolvimentoQ):
                DesenvolvimentoQ.printRes(questao.tipo.resposta)

            elif isinstance(questao.tipo, EscolhaMultiplaQ):
                print("Opções:")
                for opcao, cotacao in questao.tipo.opcao.items():
                    print(f"{opcao}: Cotação - {cotacao}")

            elif isinstance(questao.tipo, VerdFalsoQ):
                print("Pergunta:")
                questao.tipo.imprimir_pergunta()
                print("Opções:")
                for opcao, cotacao in questao.tipo.opcao.items():
                    print(f"{opcao}: Cotação - {cotacao}")


    def apresentaRespostasDadas(self):
        for versao, alunos_info in self.versoes_alunos.items():
            print(f"\nVersão: {versao}")
            for aluno_id in alunos_info["alunos"]:
                print(f"\nRespostas do Aluno {aluno_id}:")
                questoes_do_aluno = self.questoes_do_aluno(aluno_id)

                if questoes_do_aluno == "Aluno não encontrado na prova":
                    print(questoes_do_aluno)
                else:
                    for questao in questoes_do_aluno:
                        print(f"\nQuestão {questao.id_questao} :")
                        print(f"Descrição: {questao.descricao}")
                        print(f"Tipo de Questão: {questao.tipo}")

                        if isinstance(questao.tipo, CompletEspQ):
                            resposta_aluno = questao.tipo.respostas.get(aluno_id, "Sem resposta")
                            print(f"Resposta do Aluno: {resposta_aluno}")

                        elif isinstance(questao.tipo, DesenvolvimentoQ):
                            resposta_aluno = questao.tipo.resposta.get(aluno_id, "Sem resposta")
                            print(f"Resposta do Aluno: {resposta_aluno}")

                        elif isinstance(questao.tipo, EscolhaMultiplaQ):
                            resposta_aluno = questao.tipo.opcao.get(aluno_id, "Sem resposta")
                            print(f"Resposta do Aluno: {resposta_aluno}")

                        elif isinstance(questao.tipo, VerdFalsoQ):
                            resposta_aluno = questao.tipo.opcao.get(aluno_id, "Sem resposta")
                            print(f"Resposta do Aluno: {resposta_aluno}")
    
    def apresentaProvas(self,prova):
        print(f"ID Prova: {prova.get_id_prova()}")
        print(f"Nome: {prova.get_nome()}")
        print(f"Duração: {prova.get_duracao()} minutos")
        print(f"Data: {prova.get_data()}")
        print(f"Salas: {', '.join(prova.get_salas())}")
        print(f"ID Docente: {prova.get_id_docente()}")
        print(f"Hora: {prova.get_hora()}")
        print(f"Versões: {prova.get_nversoes()}")

        for versao, questoes in prova.versoes_questoes.items():
            print(f"\nVersão {versao} - Questões:")
            for questao in questoes["questoes"]:
                print(f"\nQuestão {questao.id_questao} :")
                print(f"Descrição: {questao.descricao}")
                print(f"Tipo de Questão: {questao.tipo}")
                print(f"Cotação: {questao.tipo.cot}")

        print("\nAlunos:")
        for aluno_id in prova.get_id_alunos():
            print(f"Aluno ID: {aluno_id}")

            
    def createVersions(self, num):
        for i in range(num):
            self.versoes_questoes[i] = {"versao": i, "questoes": []}
            self.versoes_alunos[i] = {"versao": i, "alunos": []}
    
            
    def randomizeQ(self):
        prova = Prova(self.nome, self.duracao, self.data, self.salas, self.id_docente, self.hora)
        prova.versoes_questoes['questoes'] = random.sample(self.versoes_questoes['questoes'], len(self.versoes_questoes['questoes']))
        for questao in prova.versoes_questoes['questoes']:
            if isinstance(questao.tipo, EscolhaMultipla):
                questao.tipo.opcoes = random.sample(questao.tipo.opcoes, len(questao.tipo.opcoes))
            elif isinstance(questao.tipo, VerdFalso):
                questao.tipo.opcoes = random.sample(questao.tipo.opcoes, len(questao.tipo.opcoes))
        return prova

    
    def insertQ(self, numero_versao, q):
        if numero_versao in self.versoes_questoes.keys():
            self.versoes_questoes[numero_versao]["questoes"].append(q)
        else:
            print("Versão não encontrada.")
    
    def get_questoes_versao(self, versao):
        return self.versoes_questoes[versao]["questoes"]
        

    def remove_questao(self, versao, questao):
        if versao in self.versoes_questoes.keys():
            if questao in self.versoes_questoes[versao]["questoes"]:
                self.versoes_questoes[versao]["questoes"].remove(questao)
                
    def questoes_do_aluno(self, id_aluno):
        for versao, alunos in self.versoes_alunos.items():
            if id_aluno in alunos:
                questoes_do_aluno = self.versoes_questoes[versao]["questoes"]
                return questoes_do_aluno
        return "Aluno não encontrado na prova"