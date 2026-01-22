

from django.urls import path
from .views import RegistarDocente, RegistarAluno, RegistarTecnico, ListarUtilizadores, Login, DecodeUser, EditarEmail, EditarPassword, VerificarDocentes, ValidaInicial, pedirInfoAlunos, acedeInformacoes

urlpatterns = [
    path('login', Login.as_view(), name='login'),
    # path('logout/', views.logout, name='logout'),
    path('registar/docente', RegistarDocente.as_view(), name='registar_docente'),
    path('registar/aluno', RegistarAluno.as_view(), name='registar_aluno'),
    path('registar/tecnico', RegistarTecnico.as_view(), name='registar_tecnico'),
    path('listar/utilizadores', ListarUtilizadores.as_view(), name='listar_utilizadores'),
    path('user', DecodeUser.as_view(), name = 'user'),
    path('editar/email', EditarEmail.as_view(), name='editar_email'),
    path('editar/password', EditarPassword.as_view(), name='editar_password'),
   
    path('verificar/docentes', VerificarDocentes.as_view(), name='verificar_docentes'),
    path('verificar/validainicial', ValidaInicial.as_view(), name='valida_inicial'),
    path('pedirInfoAlunos', pedirInfoAlunos.as_view(), name='pedir_info_alunos'),
    path('acedeInformacoes', acedeInformacoes.as_view(), name='acede_informacoes')
] 
