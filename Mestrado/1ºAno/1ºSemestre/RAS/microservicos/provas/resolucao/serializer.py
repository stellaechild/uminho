from rest_framework import serializers
from .models import Resolucao

class ResolucaoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Resolucao
        fields = ['id_resolucao', 'id_aluno', 'versao_prova', 'respostas']