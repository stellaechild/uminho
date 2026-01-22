from django.db import models
from datetime import datetime, timedelta

# Create your models here.
class Sala(models.Model):
    id_sala = models.IntegerField(primary_key=True)
    capacidade = models.IntegerField()
    edificio = models.IntegerField()
    numero = models.IntegerField()
    andar = models.IntegerField()

class ReservedSala(models.Model):
    id_reserva = models.AutoField(primary_key=True)
    id_sala = models.ForeignKey(Sala, on_delete=models.CASCADE)
    data = models.DateField()
    hora_inicio = models.TimeField()
    hora_fim = models.TimeField()