from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .models import Resposta, CompletEspR, DesenvolvimentoR, EscolhaMultiplaR, VerdFalsoR
from .serializers import RespostaSerializer, CompletEspRSerializer, DesenvolvimentoRSerializer, EscolhaMultiplaRSerializer, VerdFalsoRSerializer
from django.shortcuts import get_object_or_404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .models import Resposta, CompletEspR, DesenvolvimentoR, EscolhaMultiplaR, VerdFalsoR
from .serializers import RespostaSerializer, CompletEspRSerializer, DesenvolvimentoRSerializer, EscolhaMultiplaRSerializer, VerdFalsoRSerializer

class RespostaList(APIView):
    def get(self, request):
        questoes = Resposta.objects.all()
        serializer = RespostaSerializer(questoes, many=True)
        return Response(serializer.data)
class CompletEspRDetail(APIView):
    def get(self, request, id):
        try:
            complet_esp_r = get_object_or_404(CompletEspR, id=id)
        except CompletEspR.DoesNotExist:
            return Response({'message': 'Resposta não existe'}, status=status.HTTP_404_NOT_FOUND)
        serializer = CompletEspRSerializer(complet_esp_r)
        return Response(serializer.data)

    def post(self, request):
        serializer = CompletEspRSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def put(self, request, id):
        try:
            complet_esp_r = get_object_or_404(CompletEspR, id=id)
        except CompletEspR.DoesNotExist:
            return Response({'message': 'Reposta não existe'}, status=status.HTTP_404_NOT_FOUND)
        serializer = CompletEspRSerializer(complet_esp_r, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class DesenvolvimentoRDetail(APIView):
    def get(self, request, id):
        try:
            desenvolvimento_r = get_object_or_404(DesenvolvimentoR, id=id)
        except DesenvolvimentoR.DoesNotExist:
            return Response({'message': 'Resposta não existe'}, status=status.HTTP_404_NOT_FOUND)
        serializer = DesenvolvimentoRSerializer(desenvolvimento_r)
        return Response(serializer.data)

    def post(self, request):
        serializer = DesenvolvimentoRSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def put(self, request, id):
        try:
            desenvolvimento_r = get_object_or_404(DesenvolvimentoR, id=id)
        except DesenvolvimentoR.DoesNotExist:
            return Response({'message': 'Resposta não existe'}, status=status.HTTP_404_NOT_FOUND)
        serializer = DesenvolvimentoRSerializer(desenvolvimento_r, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
class EscolhaMultiplaRDetail(APIView):
    def get(self, request, id):
        try:
            escolha_multipla_r = get_object_or_404(EscolhaMultiplaR, id=id)
        except EscolhaMultiplaR.DoesNotExist:
            return Response({'message': 'Resposta não existe'}, status=status.HTTP_404_NOT_FOUND)
        serializer = EscolhaMultiplaRSerializer(escolha_multipla_r)
        return Response(serializer.data)

    def post(self, request):
        serializer = EscolhaMultiplaRSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def put(self, request, id):
        try:
            escolha_multipla_r = get_object_or_404(EscolhaMultiplaR, id=id)
        except EscolhaMultiplaR.DoesNotExist:
            return Response({'message': 'Resposta não existe'}, status=status.HTTP_404_NOT_FOUND)
        serializer = EscolhaMultiplaRSerializer(escolha_multipla_r, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class VerdFalsoRDetail(APIView):
    def get(self, request, id):
        try:
            verd_falso_r = get_object_or_404(VerdFalsoR, id=id)
        except VerdFalsoR.DoesNotExist:
            return Response({'message': 'Resposta não existe'}, status=status.HTTP_404_NOT_FOUND)
        serializer = VerdFalsoRSerializer(verd_falso_r)
        return Response(serializer.data)

    def post(self, request):
        serializer = VerdFalsoRSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def put(self, request, id):
        try:
            verd_falso_r = get_object_or_404(VerdFalsoR, id=id)
        except VerdFalsoR.DoesNotExist:
            return Response({'message': 'Resposta não existe'}, status=status.HTTP_404_NOT_FOUND)
        serializer = VerdFalsoRSerializer(verd_falso_r, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)