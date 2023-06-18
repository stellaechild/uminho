cmake_minimum_required(VERSION 3.5)
project(generator)

set(CMAKE_CXX_STANDARD 14)

add_executable(generator
        main.cpp pointHolder.cpp bezierHolder.cpp)
