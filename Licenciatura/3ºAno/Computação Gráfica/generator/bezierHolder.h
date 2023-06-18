#ifndef GENERATOR_BEZIERHOLDER_H
#define GENERATOR_BEZIERHOLDER_H

typedef struct bezierHolder{
    int numPatches;
    int numControlPoints;
    int** patchIndexes;
    float** controlPoints;
} *BezierHolder;

BezierHolder initBezierHolder();
void setNumPatches (BezierHolder bezier, int numPatches);
void setNumControlPoints (BezierHolder bezier, int numPoints);

#endif //GENERATOR_BEZIERHOLDER_H
