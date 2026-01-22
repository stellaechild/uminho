from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from datetime import datetime, timedelta
from .models import Sala, ReservedSala
from .serializer import SalaSerializer, ReservedSalaSerializer
#from gestao_de_provas.models import Prova  # Substitua pelo caminho correto para o modelo Prova
#from disseminacao_de_eventos.servicos import notificar_docente  # Substitua pelo método correto de notificação
import requests



class ListarSalas(APIView):
    def get(self, request):
        salas = Sala.objects.all()
        serializer = SalaSerializer(salas, many=True)
        return Response(serializer.data)

    def post(self, request):
        serializer = SalaSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors)
'''
class AdicionarSalasPorArquivo(APIView):
    parser_classes = (FileUploadParser,)

    def post(self, request, *args, **kwargs):
        file_obj = request.FILES['file']
        
        # Lógica para processar o conteúdo do arquivo e adicionar as salas
        try:
            salas_adicionadas = 0
            for line in file_obj:
                # Lógica para processar cada linha do arquivo e criar instâncias de Sala
                # Certifique-se de ajustar conforme necessário
                capacidade, edificio, numero, andar = map(int, line.strip().split(';'))
                sala = Sala(capacidade=capacidade, edificio=edificio, numero=numero, andar=andar)
                sala.full_clean()  # Validar os dados do modelo
                
                sala.save()
                salas_adicionadas += 1

            return Response({'message': f'{salas_adicionadas} salas adicionadas com sucesso.'}, status=status.HTTP_201_CREATED)

        except ValidationError as e:
            return Response({'error': f'Erro de validação: {e}'}, status=status.HTTP_400_BAD_REQUEST)
        
        except Exception as e:
            return Response({'error': f'Ocorreu um erro ao processar o arquivo: {e}'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
'''

class ListarReservas(APIView):
    def get(self, request):
        reservas = ReservedSala.objects.all()
        serializer = ReservedSalaSerializer(reservas, many=True)
        return Response(serializer.data)

    def post(self, request):
        serializer = ReservedSalaSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors)

class DeleteSala(APIView):
    def delete(self, request, id_sala):
        sala = Sala.objects.filter(idSala=id_sala).first()
        if sala:
            sala.delete()
            return Response({'message': 'Sala deletada com sucesso'})
        return Response({'message': 'Sala não encontrada'})

class DeleteReserva(APIView):
    def delete(self, request, id_reserva):
        reserva = ReservedSala.objects.filter(id_reserva=id_reserva).first()
        if reserva:
            reserva.delete()
            return Response({'message': 'Reserva deletada com sucesso'})
        return Response({'message': 'Reserva não encontrada'})

class ValidarSalas(APIView):
    def post(self, request):
        # Obter os parâmetros da requisição
        data = request.data.get('data')
        hora = request.data.get('hora')
        tempo = request.data.get('tempo')
        numero_alunos = request.data.get('numero_alunos')

        # Validar parâmetros (certifique-se de adicionar validações adequadas)
        if not all([data, hora, tempo, numero_alunos]):
            return Response({'error': 'Parâmetros inválidos'}, status=400)

        # Chamar a função valida
        ids_salas_selecionadas = self.valida(datetime.strptime(data, '%Y-%m-%d').date(), hora, tempo, numero_alunos)

        # Adicionar as salas às reservas
        for id_sala in ids_salas_selecionadas:
            sala = Sala.objects.get(id=id_sala)
            reserva = ReservedSala(id_sala=sala, data=data, hora_inicio=hora, hora_fim=(datetime.strptime(hora, '%H:%M') + timedelta(minutes=tempo)).strftime('%H:%M'))
            reserva.save()

        return Response({'ids_salas_selecionadas': ids_salas_selecionadas}, status=200)


    
    def valida(self, data, hora, tempo, numero_alunos):
        # Convertendo data e hora para objetos datetime
        data_hora_inicio = datetime.combine(data, hora)
        data_hora_fim = data_hora_inicio + timedelta(minutes=tempo)

        # Obtendo todas as salas
        salas_disponiveis = Sala.objects.all()

        # Filtrando salas que não têm reservas na data e horário fornecidos
        for reserva in ReservedSala.objects.filter(data=data):
            reserva_inicio = datetime.combine(data, reserva.hora_inicio)
            reserva_fim = datetime.combine(data, reserva.hora_fim)

        # Verificando se há sobreposição de horários
            if (data_hora_inicio < reserva_fim) and (data_hora_fim > reserva_inicio):
                salas_disponiveis = salas_disponiveis.exclude(id=reserva.id_sala.id)

    # Inicializando variáveis
        ids_salas_selecionadas = []
        alunos_restantes = numero_alunos

        # Alocando alunos em salas
        for sala in salas_disponiveis:
            if alunos_restantes <= 0:
                break

        # Calculando o número de alunos a alocar nesta sala
            alunos_a_alocar = min(alunos_restantes, sala.capacidade)

        # Adicionando sala ao resultado com o número de alunos alocados
            ids_salas_selecionadas.append({'id_sala': sala.id, 'alunos_a_alocar': alunos_a_alocar})

        # Reduzindo o número de alunos restantes
            alunos_restantes -= alunos_a_alocar

        return ids_salas_selecionadas

'''
class EliminarSala(APIView):
    def delete(self, request, id_sala):
        # Verificar se a sala existe
        try:
            sala = Sala.objects.get(id=id_sala)
        except Sala.DoesNotExist:
            return Response({'message': 'Sala não encontrada'}, status=status.HTTP_404_NOT_FOUND)

        # Verificar se há reservas associadas
        reservas = ReservedSala.objects.filter(id_sala=id_sala)
        if reservas.exists():
            # Notificar a gestão de provas e obter o ID do docente
            try:
                prova_associada = Prova.objects.get(sala=sala)
                id_docente = prova_associada.id_docente
                notificar_docente(id_docente, f"A sala {sala.id} foi eliminada e a sua prova foi afetada.")
            except Prova.DoesNotExist:
                pass  # Não há prova associada, continuar com a eliminação

            # Eliminar reservas associadas
            reservas.delete()

        # Eliminar a sala
        sala.delete()

        return Response({'message': 'Sala eliminada com sucesso'})
'''

