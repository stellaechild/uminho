//
// Created by vicshadow on 30-04-2023.
//

#ifndef ENGINE_CATMULLROM_H
#define ENGINE_CATMULLROM_H


#include "math.h"

void buildRotMatrix(float *x, float *y, float *z, float *m);

void cross(float *a, float *b, float *res);

void normalize(float *a);

void multMatrixVector(float *m, float *v, float *res);

void getCatmullRomPoint(float t, float *p0, float *p1, float *p2, float *p3, float *pos, float *deriv);

void getGlobalCatmullRomPoint(float gt,float **p,int pc, float *pos, float *deriv);

#endif //ENGINE_CATMULLROM_H
