from rest_framework import serializers
from .models import Sala, ReservedSala

class SalaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Sala
        fields = ['id_sala','capacidade', 'edificio', 'numero', 'andar']

class ReservedSalaSerializer(serializers.ModelSerializer):
    class Meta:
        model = ReservedSala
        fields = ['id_reserva', 'id_sala', 'data', 'hora_inicio', 'hora_fim']
