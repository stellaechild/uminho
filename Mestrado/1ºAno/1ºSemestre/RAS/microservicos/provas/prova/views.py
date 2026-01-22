import json
from django.shortcuts import get_object_or_404, render
from django.http import HttpResponse, JsonResponse

from prova.models import *

from django.http import HttpResponse, JsonResponse
from django.shortcuts import get_object_or_404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status

from prova.serializers import VersaoSerializer
from .models import Prova, Versao  # Import your models here
import json

class StoreProvas(APIView):
    def export_provas_to_csv(request):
        provas = Prova.objects.all()
        data = serialize('csv', provas)
        
        response = HttpResponse(data, content_type='text/csv')
        response['Content-Disposition'] = 'attachment; filename="provas.csv"'
        return response    
class ListProvas(APIView):
    def get(self, request):
        provas = Prova.objects.all()
        serializer = VersaoSerializer(provas, many=True)
        return Response(serializer.data)
    
    def post(self, request):
        serializer = VersaoSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
class ListAlunos(APIView):
    def get(self, request, prova_id):
        try:
            prova = get_object_or_404(Prova, id_prova=prova_id)
        except Prova.DoesNotExist:
            return Response({"error": f"A prova com o ID {prova_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)
        id_alunos = prova.id_alunos
        # Process 'id_alunos' as needed
        return Response({"id_alunos": id_alunos})

class AddStudentToProva(APIView):
    def post(self, request, prova_id, student_id):
        try:
            prova = get_object_or_404(Prova, id_prova=prova_id)
        except Prova.DoesNotExist:
            return Response({"error": f"A prova com o ID {prova_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)
        prova.add_aluno_id(student_id)
        return HttpResponse("Estudante adicionado à prova com sucesso!")

class RemoveStudentFromProva(APIView):
    def post(self, request, prova_id, student_id):
        try:
            prova = get_object_or_404(Prova, id_prova=prova_id)
        except Prova.DoesNotExist:
            return Response({"error": f"A prova com o ID {prova_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)
        prova.remove_aluno_id(student_id)
        return HttpResponse("Estudante removido da prova com sucesso!")

class AddDocenteToProva(APIView):
    def post(self, request, prova_id, docente_id):
        try:
            prova = get_object_or_404(Prova, id_prova=prova_id)
        except Prova.DoesNotExist:
            return Response({"error": f"A prova com o ID {prova_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)
        prova.add_docente_id(docente_id)
        return HttpResponse("Docente adicionado à prova com sucesso!")

class RemoveDocenteFromProva(APIView):
    def post(self, request, prova_id, docente_id):
        try:
            prova = get_object_or_404(Prova, id_prova=prova_id)
        except Prova.DoesNotExist:
            return Response({"error": f"A prova com o ID {prova_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)
        prova.remove_docente_id(docente_id)
        return HttpResponse("Docente removido da prova com sucesso!")

class CreateVersoes(APIView):
    def post(self, request, prova_id):
        prova = get_object_or_404(Prova, id_prova=prova_id)
        prova.create_versions()
        return HttpResponse("Versões criadas com sucesso!")

class RandomizeQuestions(APIView):
    def get(self, request, versao_id):
        versao = get_object_or_404(Versao, id_versao=versao_id)
        versao.randomizeQ()
        return HttpResponse("Questions randomized for Versao successfully!")

class InsertQuestion(APIView):
    def post(self, request, versao_id):
        if request.method == 'POST':
            data = json.loads(request.body)
            question = data.get('question')
            versao = get_object_or_404(Versao, id_versao=versao_id)
            versao.insert_q(question)
            return JsonResponse({"message": "Questão inserida na versão com sucesso!"})
        return HttpResponse(status=400)

class RemoveQuestion(APIView):
    def post(self, request, versao_id):
        if request.method == 'POST':
            data = json.loads(request.body)
            question = data.get('question')
            try:
                versao = get_object_or_404(Versao, id_versao=versao_id)
            except Versao.DoesNotExist:
                return Response({"error": f"A versão com o ID {versao_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)
            versao.remove_questao(question)
            return JsonResponse({"message": "Questão removida da versão com sucesso!"})
        return HttpResponse(status=400)

class ApresentaProva(APIView):
    def get(self, request, prova_id):

        try:
                prova = get_object_or_404(Prova, id_prova=prova_id)
        except Versao.DoesNotExist:
                return Response({"error": f"A versão com o ID {versao_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)
        prova.apresentaProvas(prova)
        return HttpResponse("Prova apresentada com sucesso!")

    def post(self, request, prova_id):
        try:
                prova = get_object_or_404(Prova, id_prova=prova_id)
        except Versao.DoesNotExist:
                return Response({"error": f"A versão com o ID {versao_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)
        prova_id_aluno = request.data.get('aluno_id')
        prova.add_aluno(prova_id_aluno)
        return HttpResponse(f"Aluno {prova_id_aluno} adicionado à prova com sucesso!")

    def delete(self, request, prova_id):
        try:
                prova = get_object_or_404(Prova, id_prova=prova_id)
        except Versao.DoesNotExist:
                return Response({"error": f"A versão com o ID {versao_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)
        prova_id_aluno = request.data.get('aluno_id')
        prova.remove_aluno(prova_id_aluno)
        return HttpResponse(f"Aluno {prova_id_aluno} removido da prova com sucesso!")

    def put(self, request, prova_id):
        try:
                prova = get_object_or_404(Prova, id_prova=prova_id)
        except Versao.DoesNotExist:
                return Response({"error": f"A versão com o ID {versao_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)
        nova_duracao = request.data.get('nova_duracao')
        prova.set_duracao(nova_duracao)
        return HttpResponse(f"Duração da prova atualizada para {nova_duracao} minutos com sucesso!")
class ConsultarProvaCorrigida(APIView):
    def get(self, request, prova_id):
        try:
            prova = Prova.objects.get(id_prova=prova_id)
        except Prova.DoesNotExist:
            return Response({"error": f"A prova com o ID {prova_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)

        result = {
            "id_prova": prova.get_id_prova(),
            "questoes": []
        }

        for questao in prova.versoes_questoes['questoes']:
            questao_info = {
                "id_questao": questao.id_questao,
                "descricao": questao.descricao,
                "tipo_questao": str(questao.tipo),  
                "respostas": None
            }

            if isinstance(questao.tipo, CompletEspQ):
                questao_info["respostas"] = questao.tipo.respostas

            elif isinstance(questao.tipo, DesenvolvimentoQ):
                questao_info["respostas"] = questao.tipo.resposta

            elif isinstance(questao.tipo, EscolhaMultiplaQ):
                questao_info["opcoes"] = [{"opcao": opcao, "cotacao": cotacao} for opcao, cotacao in questao.tipo.opcao.items()]

            elif isinstance(questao.tipo, VerdFalsoQ):
                questao_info["pergunta"] = questao.tipo.pergunta
                questao_info["opcoes"] = [{"opcao": opcao, "cotacao": cotacao} for opcao, cotacao in questao.tipo.opcao.items()]

            result["questoes"].append(questao_info)

        return Response(result, status=status.HTTP_200_OK)
    
class CriarProva(APIView):
    def post(self, request):
        if request.method == 'POST':
            data = json.loads(request.body)
            prova = data.get('prova')
            prova = Prova(nome=prova['nome'], duracao=prova['duracao'], data=prova['data'], salas=prova['salas'], id_docente=prova['id_docente'], hora=prova['hora'])
            prova.save()
            return JsonResponse({"message": "Prova criada com sucesso!"})
        return HttpResponse(status=400)
    
class AdicionarQuestaoAVersao(APIView):
    def post(self, request, versao_id):
        if request.method == 'POST':
            data = json.loads(request.body)
            questao = data.get('questao')
            versao = get_object_or_404(Versao, id_versao=versao_id)
            versao.questoes.append(questao)
            versao.save()
            return JsonResponse({"message": "Questão adicionada à versão com sucesso!"})
        return HttpResponse(status=400)
    
class RemoverQuestaoDeVersao(APIView):
    def post(self, request, versao_id):
        if request.method == 'POST':
            data = json.loads(request.body)
            questao = data.get('questao')
            versao = get_object_or_404(Versao, id_versao=versao_id)
            versao.questoes.remove(questao)
            versao.save()
            return JsonResponse({"message": "Questão removida da versão com sucesso!"})
        return HttpResponse(status=400)
    
class ListVersoes(APIView):
    def get(self, request, prova_id):
        try:
            prova = get_object_or_404(Prova, id_prova=prova_id)
        except Prova.DoesNotExist:
            return Response({"error": f"A prova com o ID {prova_id} não foi encontrada."}, status=status.HTTP_404_NOT_FOUND)
        versoes = prova.versoes
        return Response({"versoes": versoes})