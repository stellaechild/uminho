from django.contrib import admin

class ProvaAdmin(admin.ModelAdmin):
    list_display = ('id_prova', 'nome', 'duracao', 'data', 'salas', 'id_docente', 'hora')

    
