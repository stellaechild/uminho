from django.urls import path
from . import views

urlpatterns = [
    path('provas/', views.ListProvas.as_view()),
    path('provas/<str:prova_id>/alunos/', views.ListAlunos.as_view()),
    path('provas/<str:prova_id>/alunos/<str:student_id>/', views.AddStudentToProva.as_view()),
    path('provas/<str:prova_id>/alunos/<str:student_id>/', views.RemoveStudentFromProva.as_view()),
    path('provas/<str:prova_id>/alunos/<str:student_id>/', views.AddDocenteToProva.as_view()),
    path('provas/<str:prova_id>/alunos/<str:docente_id>/', views.RemoveDocenteFromProva.as_view()),
    path('provas/<str:prova_id>/questoes/<str:questao_id>/', views.AdicionarQuestaoAVersao.as_view()),
    path('provas/<str:prova_id>/questoes/<str:questao_id>/', views.RemoverQuestaoDeVersao.as_view()),
    path('provas/<str:prova_id>/questoes/<str:questao_id>/', views.RandomizeQuestions.as_view())
    path('provas/<str:prova_id>/versoes/',views.CreateVersoes.as_view())

]