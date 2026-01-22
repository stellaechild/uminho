from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .models import Questao, CompletEspQ, DesenvolvimentoQ, EscolhaMultiplaQ, VerdFalsoQ
from .serializers import QuestaoSerializer, CompletEspQSerializer, DesenvolvimentoQSerializer, EscolhaMultiplaQSerializer, VerdFalsoQSerializer
from django.shortcuts import get_object_or_404

class QuestaoList(APIView):
    def get(self, request):
        questoes = Questao.objects.all()
        serializer = QuestaoSerializer(questoes, many=True)
        return Response(serializer.data)
    
from rest_framework.response import Response
from rest_framework import status
from .models import Questao, CompletEspQ, DesenvolvimentoQ, EscolhaMultiplaQ, VerdFalsoQ
from .serializers import QuestaoSerializer, CompletEspQSerializer, DesenvolvimentoQSerializer, EscolhaMultiplaQSerializer, VerdFalsoQSerializer

class QuestaoViews(APIView):
    def post(self, request):
        serializer = QuestaoSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class CompletEspQViews(APIView):
    def post(self, request):
        serializer = CompletEspQSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class DesenvolvimentoQViews(APIView):
    def post(self, request):
        serializer = DesenvolvimentoQSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class EscolhaMultiplaQViews(APIView):
    def post(self, request):
        serializer = EscolhaMultiplaQSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class VerdFalsoQViews(APIView):
    def post(self, request):
        serializer = VerdFalsoQSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class CompletEspQDetail(APIView):
    def get(self, request, id):
        questao = get_object_or_404(CompletEspQ, id_questao=id)
        serializer = CompletEspQSerializer(questao)
        return Response(serializer.data)

    def put(self, request, id):
        questao = get_object_or_404(CompletEspQ, id_questao=id)
        serializer = CompletEspQSerializer(questao, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class DesenvolvimentoQDetail(APIView):
    def get(self, request, id):
        questao = get_object_or_404(DesenvolvimentoQ, id_questao=id)
        serializer = DesenvolvimentoQSerializer(questao)
        return Response(serializer.data)

    def put(self, request, id):
        questao = get_object_or_404(DesenvolvimentoQ, id_questao=id)
        serializer = DesenvolvimentoQSerializer(questao, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class EscolhaMultiplaQDetail(APIView):
    def get(self, request, id):
        questao = get_object_or_404(EscolhaMultiplaQ, id_questao=id)
        serializer = EscolhaMultiplaQSerializer(questao)
        return Response(serializer.data)

    def put(self, request, id):
        questao = get_object_or_404(EscolhaMultiplaQ, id_questao=id)
        serializer = EscolhaMultiplaQSerializer(questao, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class VerdFalsoQDetail(APIView):
    def get(self, request, id):
        questao = get_object_or_404(VerdFalsoQ, id_questao=id)
        serializer = VerdFalsoQSerializer(questao)
        return Response(serializer.data)

    def put(self, request, id):
        questao = get_object_or_404(VerdFalsoQ, id_questao=id)
        serializer = VerdFalsoQSerializer(questao, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

