"""
URL configuration for calendarizacao project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""

from django.urls import path



from .views import ListarSalas, ListarReservas, DeleteReserva, DeleteSala, ValidarSalas


urlpatterns = [
    path('salas/', ListarSalas.as_view(), name='listar_salas'),
    path('reservas/', ListarReservas.as_view(), name='listar_reservas'),
    path('delete_reserva/<int:id_reserva>/', DeleteReserva.as_view(), name='delete_reserva'),
    path('delete_sala/<int:id_sala>/', DeleteSala.as_view(), name='eliminar_sala'),
    path('validar_salas/', ValidarSalas.as_view(), name='validar_salas'),
]