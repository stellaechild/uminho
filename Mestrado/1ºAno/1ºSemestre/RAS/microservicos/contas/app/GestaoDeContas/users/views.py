from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.exceptions import AuthenticationFailed
from .serializers import DocenteSerializer, AlunoSerializer, TecnicoSerializer
from .models import Utilizador #, Docente, Aluno, Tecnico

import jwt
import datetime
import os
        
class ListarUtilizadores(APIView):
    def get(self, request):
        users = Utilizador.objects.all()
        
        serializer = DocenteSerializer(users, many=True)
        return Response(serializer.data)
        
#TODO: chamar endpoint de notificar registo
class RegistarDocente(APIView):
    def post(self, request):
        user = DocenteSerializer(data=request.data)
        user.is_valid(raise_exception=True)
        user.save()
        
        return Response(user.data)
    
class RegistarAluno(APIView):
    def post(self, request):
        user = AlunoSerializer(data=request.data)
        user.is_valid(raise_exception=True)
        user.save()
        
        return Response(user.data)
    

class RegistarTecnico(APIView):
    def post(self, request):
        user = TecnicoSerializer(data=request.data)
        user.is_valid(raise_exception=True)
        user.save()
        
        return Response(user.data)
    
    
class Login(APIView):
    def post(self, request):
        email = request.data['email']
        password = request.data['password']
        
        user = Utilizador.objects.filter(email=email).first()
        
        print(user)
        
        if user is None:
            raise AuthenticationFailed('User not found!')
        
        if not user.check_password(password):
            raise AuthenticationFailed('Incorrect password!')

        payload = {
            'id': f'{user.numero}',
            'exp': datetime.datetime.utcnow() + datetime.timedelta(minutes=60),
            'sub': f'{user.email};{user.role}'
        }

        key = os.getenv('JWT')
        algorithm = 'HS256'

        token = jwt.encode(payload, key, algorithm=algorithm) #.decode('utf-8')
            
        return Response({'token': token})
        

class DecodeUser(APIView):

    def get(self, request):
        token = request.data['token']
        print(token)

        payload = {}
        
        if not token:
            return AuthenticationFailed('Nao esta autenticado!')

        try:
            key = os.getenv('JWT')
            payload = jwt.decode(token, key, algorithms='HS256')
        except jwt.ExpiredSignatureError:
           raise AuthenticationFailed('Nao esta autenticado!')

        print(payload)
        
        user = Utilizador.objects.filter(numero = payload['id']).first()

        serializer = None

        if user.role == 1:
            serializer  = DocenteSerializer(user)
        if user.role == 2:
            serializer = AlunoSerializer(user)
        if user.role == 3:
            serializer = TecnicoSerializer(user)
        
        
        return Response(serializer.data)


class EditarEmail(APIView):

    def post(self,request):
        old_email = request.data['old_email']
        new_email = request.data['new_email']
        
        user = Utilizador.objects.filter(email = old_email).first()

        if user is None:
            raise AuthenticationFailed('User not found!')
        
        user.email = new_email

        user.save()
        
        return Response({'email': user.email, 'numero': user.numero, 'name': user.name, 'role': user.role})


class EditarPassword(APIView):
    
    def post(self,request):
        email = request.data['email']
        old_password = request.data['old_password']
        new_password = request.data['new_password']
        
        user = Utilizador.objects.filter(email = email).first()

        if user is None:
            raise AuthenticationFailed('User not found!')
        
        if not user.check_password(old_password):
            raise AuthenticationFailed('Incorrect password!')
        
        user.set_password(new_password)

        user.save()
        
        return Response({'email': user.email, 'numero': user.numero, 'name': user.name, 'role': user.role})




class VerificarDocentes(APIView):
    
    def get(self,request):
        
        emails = request.data.get('emails', [])

        # unchecked_emails = []
        emailsFound = True

        for email in emails:
            user = Utilizador.objects.filter(email=email, role=1).first()
            if user is None:
                # unchecked_emails.append(email)
                emailsFound = False
                break

        
        return Response(emailsFound)


# o poderá levar um ficheiro em vez de uma lista de emails
class ValidaInicial(APIView):

    def get(self,request):
        
        emails = request.data.get('emails', [])

        # unchecked_emails = []
        emailsFound = True

        for email in emails:
            user = Utilizador.objects.filter(email=email, role=2).first()
            if user is None:
                # unchecked_emails.append(email)
                emailsFound = False
                break

        
        return Response(emailsFound)


class pedirInfoAlunos(APIView):

    def get(self, request):
        numeros = request.data.get('numeros', [])

        alunosInfos = {}

        for numero in numeros:
            user = Utilizador.objects.filter(numero=numero, role=2).first()
            
            alunosInfos[user.numero] = {'Nome': user.name, 'Email': user.email}
            
        return Response(alunosInfos)


class acedeInformacoes(APIView):
    
    def get(self, request):
        numero = request.data['numero']
        user = Utilizador.objects.filter(numero=numero).first()
        
        if user is None:
            raise AuthenticationFailed('User not found!')
        
        info = {'Numero' : user.numero, 'Nome': user.name, 'Email': user.email}
        
        return Response(info)