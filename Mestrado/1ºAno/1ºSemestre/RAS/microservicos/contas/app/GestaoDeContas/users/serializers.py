from rest_framework import serializers
from .models import Utilizador #, Docente, Aluno, Tecnico


class DocenteSerializer(serializers.ModelSerializer):
    class Meta:
        model = Utilizador
        fields = ['name', 'email', 'password', 'numero']
        extra_kwargs = {'password': {'write_only': True}}
        
    def create(self, validated_data):
        password = validated_data.pop('password', None)
        user = self.Meta.model(**validated_data)
        
        if password is not None:
            user.set_password(password)
        
        user.role = 1
        
        user.save()
        
        # docente = Docente.objects.create(utilizador=user)
        # docente.save()
        return user
    
    
class AlunoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Utilizador
        fields = ['name', 'email', 'password', 'numero']
        extra_kwargs = {'password': {'write_only': True}}
        
    def create(self, validated_data):
        password = validated_data.pop('password', None)
        user = self.Meta.model(**validated_data)
        
        if password is not None:
            user.set_password(password)
        
        user.role = 2
        
        user.save()
        
        # aluno = Aluno.objects.create(utilizador=user)
        # aluno.save()
        return user
    

class TecnicoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Utilizador
        fields = ['name', 'email', 'password', 'numero']
        extra_kwargs = {'password': {'write_only': True}}
        
    def create(self, validated_data):
        password = validated_data.pop('password', None)
        user = self.Meta.model(**validated_data)
        
        if password is not None:
            user.set_password(password)
        
        user.role = 3
        
        user.save()
        
        # tecnico = Tecnico.objects.create(utilizador=user)
        # tecnico.save()
        return user
    