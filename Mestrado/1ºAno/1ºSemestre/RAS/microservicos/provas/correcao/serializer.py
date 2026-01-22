from rest_framework import serializers
from .models import Correcao

class CorrecaoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Correcao
        fields = ['id_correcao', 'docentes_associados', 'resolucao', 'cotacao', 'nota_final']