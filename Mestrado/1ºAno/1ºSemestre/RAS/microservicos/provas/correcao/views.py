from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from .models import Correcao
from .serializer import CorrecaoSerializer

# Create your views here.

class CorrecaoView(APIView):
    def get(self, request):
        correcao = Correcao.objects.all()
        serializer = CorrecaoSerializer(correcao, many=True)
        return Response(serializer.data)
    
class AdicionarDocente(APIView):
    def post(self, request):
        correcao = CorrecaoSerializer(data=request.data)
        correcao.is_valid(raise_exception=True)
        correcao.save()

        return Response(correcao.data)
    
class RemoverDocente(APIView):
    def delete(self, request, id_docente):
        try:
            correcao = Correcao.objects.get(id_docente=id_docente)
        except Correcao.DoesNotExist:
            return Response({'message': 'Docente não encontrado'})

        correcao.delete()

        return Response({'message': 'Docente removido com sucesso'})

class AdicionarCotacao(APIView):
    def post(self, request):
        correcao = CorrecaoSerializer(data=request.data)
        correcao.is_valid(raise_exception=True)
        correcao.save()

        return Response(correcao.data)

