from django.db import models
from django.contrib.auth.models import AbstractUser

class Utilizador(AbstractUser):
    
    name = models.CharField(max_length=200)
    email = models.CharField(max_length=200, unique=True)
    password = models.CharField(max_length=200)
    numero = models.CharField(max_length=200, unique=True)
    username=None
    role = models.IntegerField() # 1- Docente | 2- Aluno | 3- Tecnico
    
    # o email vai ser usado para a autenticacao
    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []
    
    
    


