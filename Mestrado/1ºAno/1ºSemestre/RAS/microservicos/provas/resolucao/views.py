from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from django.shortcuts import get_object_or_404
from .models import Resolucao
from .serializers import ResolucaoSerializer

class ResolucaoDetail(APIView):
    def get(self, request, id):
        resolucao = get_object_or_404(Resolucao, id_resolucao=id)
        serializer = ResolucaoSerializer(resolucao)
        return Response(serializer.data)

    def post(self, request):
        serializer = ResolucaoSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def put(self, request, id):
        resolucao = get_object_or_404(Resolucao, id_resolucao=id)
        serializer = ResolucaoSerializer(resolucao, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, id):
        resolucao = get_object_or_404(Resolucao, id_resolucao=id)
        resolucao.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

class ViewResolucao_Id(APIView):
    def get(self, request, id):
        resolucao = get_object_or_404(Resolucao, id_resolucao=id)
        serializer = ResolucaoSerializer(resolucao)
        return Response(serializer.data)
    
class ViewResolucao_Aluno(APIView):
    def get(self, request, id):
        resolucao = get_object_or_404(Resolucao, id_aluno=id)
        serializer = ResolucaoSerializer(resolucao)
        return Response(serializer.data)
    
class AdicionarDocente(APIView):
    def post(self, request, id):
        resolucao = get_object_or_404(Resolucao, id_resolucao=id)
        resolucao.add_docente_id(request.data)
        return Response(status=status.HTTP_204_NO_CONTENT)