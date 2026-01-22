from rest_framework import serializers
from .models import Prova, Versao

class ProvaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Prova
        fields = ('id', 'nome', 'email', 'telefone', 'data_criacao', 'data_atualizacao', 'ativo')

class VersaoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Versao
        fields = ('id_versao', 'detalhes_prova', 'numero_versao', 'questoes', 'alunos')

