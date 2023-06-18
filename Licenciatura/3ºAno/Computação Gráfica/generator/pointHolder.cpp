#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include "pointHolder.h"

PointHolder initPointHolder(){
    PointHolder res = (PointHolder) malloc(sizeof (pointHolder));
    res->size = 3;
    res->sizeTextCoord = 2;

    res->current = 0;
    res->currentTextCoord = 0;

    res->list = (float*) malloc(sizeof(float) * res->size);
    res->normal = (float*) malloc(sizeof(float) * res->size);
    res->textCoord = (float*) malloc(sizeof(float) * res->sizeTextCoord);
    return res;
}

void checkMem(PointHolder self){
    if (self->current > self->size - 3){
        self->size *= 2;
        self->list = (float*) realloc(self->list,sizeof(float) * self->size);
        self->normal = (float*) realloc(self->normal,sizeof(float) * self->size);

        self->sizeTextCoord *= 2;
        self->textCoord = (float*) realloc(self->textCoord,sizeof(float) * self->sizeTextCoord);
    }
}

void addPoint(PointHolder self,float x,float y,float z,float nx,float ny,float nz,float tx,float ty){
    checkMem(self);
    self->list[self->current] = x;
    self->normal[self->current] = nx;
    self->current++;

    self->list[self->current] = y;
    self->normal[self->current] = ny;
    self->current++;

    self->list[self->current] = z;
    self->normal[self->current] = nz;
    self->current++;

    self->textCoord[self->currentTextCoord] = tx;
    self->currentTextCoord++;
    self->textCoord[self->currentTextCoord] = ty;
    self->currentTextCoord++;
}

void printToFile(PointHolder self,FILE* file){

    fwrite(&self->current,sizeof (int),1,file);
    fwrite(self->list,sizeof (float ),self->current,file);
    fwrite(self->normal,sizeof (float ),self->current,file);
    fwrite(self->textCoord,sizeof (float ),self->currentTextCoord,file);
}