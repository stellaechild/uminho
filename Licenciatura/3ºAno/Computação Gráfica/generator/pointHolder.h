#include <stdio.h>

#ifndef GENERATOR_POINTHOLDER_H
#define GENERATOR_POINTHOLDER_H

typedef struct pointHolder{
    float* list;
    float* normal;
    float* textCoord;

    int size;
    int current;
    int sizeTextCoord;
    int currentTextCoord;
} *PointHolder;

PointHolder initPointHolder();

void checkMem(PointHolder self);

void addPoint(PointHolder self,float x,float y,float z,
                                float nx,float ny,float nz,
                                float tx,float ty);

void printToFile(PointHolder self,FILE* file);

#endif //GENERATOR_POINTHOLDER_H
