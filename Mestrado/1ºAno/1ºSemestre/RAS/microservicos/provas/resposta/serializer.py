from rest_framework import serializers
from .models import Resposta, CompletEspR, DesenvolvimentoR, EscolhaMultiplaR, VerdFalsoR

class RespostaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Resposta
        fields = [ 'id_resposta', 'id_aluno', 'questao', 'resolucao']

class CompletEspRSerializer(serializers.ModelSerializer):
    class Meta:
        model = CompletEspR
        fields = '__all__'

class DesenvolvimentoRSerializer(serializers.ModelSerializer):
    class Meta:
        model = DesenvolvimentoR
        fields = '__all__'

class EscolhaMultiplaRSerializer(serializers.ModelSerializer):
    class Meta:
        model = EscolhaMultiplaR
        fields = '__all__'

class VerdFalsoRSerializer(serializers.ModelSerializer):
    class Meta:
        model = VerdFalsoR
        fields = '__all__'