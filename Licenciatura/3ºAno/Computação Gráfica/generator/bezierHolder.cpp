#include <cstdlib>
#include "bezierHolder.h"


bezierHolder* initBezierHolder() {
    auto res = (BezierHolder) malloc(sizeof (bezierHolder));
    res->numPatches = 0;
    res->numControlPoints = 0;
    return res;
}

void setNumPatches (bezierHolder* bezier, int numPatches) {
    bezier->numPatches = numPatches;
    bezier->patchIndexes = (int **) malloc(sizeof (int *) * numPatches);
    for (int i=0; i<numPatches; i++) bezier->patchIndexes[i] = (int *) malloc(sizeof(int) * 16);

}

void setNumControlPoints (bezierHolder* bezier, int numPoints) {
    bezier->numControlPoints = numPoints;
    bezier->controlPoints = (float **) malloc(sizeof (float *) * numPoints);
    for (int i=0; i<numPoints; i++) bezier->controlPoints[i] = (float *) malloc(sizeof(float) * 3);
}