from rest_framework import serializers
from .models import Questao, CompletEspQ, DesenvolvimentoQ, EscolhaMultiplaQ, VerdFalsoQ

class QuestaoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Questao
        fields = ['id_questao', 'descricao', 'imagem']

class CompletEspQSerializer(serializers.ModelSerializer):
    class Meta:
        model = CompletEspQ
        fields = '__all__'

class DesenvolvimentoQSerializer(serializers.ModelSerializer):
    class Meta:
        model = DesenvolvimentoQ
        fields = '__all__'

class EscolhaMultiplaQSerializer(serializers.ModelSerializer):
    class Meta:
        model = EscolhaMultiplaQ
        fields = '__all__'

class VerdFalsoQSerializer(serializers.ModelSerializer):
    class Meta:
        model = VerdFalsoQ
        fields = '__all__'
