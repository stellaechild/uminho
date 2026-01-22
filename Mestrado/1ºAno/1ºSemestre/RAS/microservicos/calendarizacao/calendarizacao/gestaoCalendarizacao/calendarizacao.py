from datetime import datetime, timedelta
from typing import List
from calendarizacao import Sala  # Certifique-se de que a importação está correta
from provas import Prova  # Certifique-se de que a importação está correta

class Calendarizacao:
    def __init__(self):
        self._salas = []
        self._provas = []

    def verificaDisponibilidade(self, nome, duracao, data, horario, salas):
        # Verifica se a sala está na lista de salas
        for sala in salas:
            if sala.get_nome() == nome:
                # Calcula a data de início com base na data e horário da prova
                data_inicio = datetime.combine(data, horario)
                # Calcula a data de fim com base na data de início e na duração da prova
                data_fim = data_inicio + timedelta(minutes=duracao)

                # Verifica se há conflito de horários com as provas já agendadas na sala
                for prova in sala.get_provas():
                    if self.haConflito(data_inicio, data_fim, prova.get_data_inicio(), prova.get_duracao()):
                        return False  # Conflito de horários, sala indisponível

                return True  # Não há conflito de horários, sala disponível

        return False  # Sala não encontrada, considera-se indisponível

    def haConflito(self, inicio1, fim1, inicio2, duracao2):
        fim2 = inicio2 + timedelta(minutes=duracao2)

        # Verifica se há sobreposição de horários
        return (inicio1 < fim2) and (fim1 > inicio2)

#propor salas com espaço suficiente para o número de alunos
#salas que estejam livres 30 min antes e após a hora de começo, escolher de preferência salas do mesmo andar e edifício, caso contrário apenas edificio ou apenas uma disponivel
    def valida(self, data, hora, tempo, nrAlunos):
        data_hora_inicio = datetime.combine(self.data, self.hora)
        data_hora_inicio_anterior = data_hora_inicio - timedelta(minutes=tempo)
        data_hora_fim = data_hora_inicio + timedelta(minutes=self.duracao + tempo)

        salas_disponiveis = []
        for sala in Calendarizacao.salas:
            sala_ocupada = False
            for prova_agendada in sala._provas:
                data_hora_inicio_agendada = datetime.combine(prova_agendada.data, prova_agendada.hora)
                data_hora_fim_agendada = data_hora_inicio_agendada + timedelta(minutes=prova_agendada.duracao)

                if (
                    (data_hora_inicio_agendada <= data_hora_inicio <= data_hora_fim_agendada or
                     data_hora_inicio_agendada <= data_hora_fim <= data_hora_fim_agendada) or
                    (data_hora_inicio <= data_hora_inicio_agendada <= data_hora_fim or
                     data_hora_inicio <= data_hora_fim_agendada <= data_hora_fim)
                ):
                    sala_ocupada = True
                    break

            if not sala_ocupada:
                salas_disponiveis.append(sala)

        # Preferência por salas no mesmo andar e edifício
        if self.salas:
            andar_preferido = self.salas[0]._andar
            edificio_preferido = self.salas[0]._edificio
            salas_preferidas = [sala for sala in salas_disponiveis if sala._andar == andar_preferido and sala._edificio == edificio_preferido]
        else:
            salas_preferidas = []

        if salas_preferidas:
            sala_escolhida = salas_preferidas[0]
        else:
            sala_escolhida = salas_disponiveis[0] if salas_disponiveis else None

#verificar metodos da prova 
    def agendaprova(self, nome, duracao, data, horario, salas):
        # Verifica se a sala está disponível para a prova
        disponivel = self.verificaDisponibilidade(nome, duracao, data, horario, salas)

        if disponivel:
            # Encontrar a sala pelo nome
            sala_agendada = next((sala for sala in salas if sala.get_nome() == nome), None)

            if sala_agendada:
                # Criar uma instância de Prova
                nova_prova = Prova(data_inicio=datetime.combine(data, horario), duracao=duracao)

                # Adicionar a prova à sala
                sala_agendada.adicionar_prova(nova_prova)

                # Adicionar a prova à lista de provas na calendarização
                self._provas.append(nova_prova)

                print(f"Prova agendada com sucesso na {nome}!")
            else:
                print(f"Sala {nome} não encontrada.")
        else:
            print(f"Conflito de horários, a sala {nome} está indisponível.")


    #def provaAgendada(self, nome, duracao, data, horario, salas):


    #def get_template_import(self, salas):

    #função que adiciona à lista de salas da classe as salas de um 
    #ficheiro em que os parâmetros estão separados por ";" e os ids das provas agendadas dentro de []
    def import_salas(self, ficheiro):
        try:
            with open(ficheiro, 'r') as arquivo:
                linhas = arquivo.readlines()
                for linha in linhas:
                    parametros = linha.strip().split(';')
                    capacidade, edificio, numero, andar = map(int, parametros[:4])
                    ids_provas = parametros[4][1:-1].split(',') if len(parametros) > 4 else []
                    
                    provas = [Prova(id_prova.strip()) for id_prova in ids_provas]
                    sala = Sala(capacidade, edificio, numero, andar, provas)
                    self.salas.append(sala)
                    
                print("Salas adicionadas com sucesso.")
        except FileNotFoundError:
            print(f"O arquivo '{ficheiro}' não foi encontrado.")
        except Exception as e:
            print(f"Ocorreu um erro ao processar o arquivo: {e}")


    def get_salas(self):
        if not self.salas:
            print("Não há salas registradas.")
        else:
            print("Lista de Salas:")
            for sala in self.salas:
                print(f"ID: {sala._idSala}, Capacidade: {sala._capacidade}, Edifício: {sala._edificio}, Número: {sala._numero}, Andar: {sala._andar}, Provas: {[prova._idProva for prova in sala._provas]}")



    def delete_salas(self, salas):
        # Remover salas da lista de salas
        salas_restantes = [sala for sala in self.salas if sala not in salas]

        if salas_restantes != self.salas:
            self.salas = salas_restantes
            print("Salas removidas com sucesso.")
        else:
            print("Nenhuma sala foi removida.")

