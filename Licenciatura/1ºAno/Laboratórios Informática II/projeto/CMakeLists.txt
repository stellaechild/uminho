cmake_minimum_required(VERSION 3.15)
project(li2PL11 C)

set(CMAKE_C_STANDARD 99)

add_executable(li2PL11 main.c camadadados.c logicaprograma.c camadainterface.c listasgenerica.c botsimples.c botavancado.c gestaodeficheiros.c)

target_link_libraries(li2PL11 m)