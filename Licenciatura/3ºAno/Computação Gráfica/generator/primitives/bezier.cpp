#include <string>
#include <fstream>
#include <sstream>
#include "../pointHolder.h"
#include "../bezierHolder.h"

using namespace std;

// reads the input file given by the user
BezierHolder readBezierPatch (char* inputFile) {

    string line;
    ifstream inFile(inputFile);
    if (inFile) {

        BezierHolder bezier = initBezierHolder();

        // get number of patches
        getline(inFile, line);

        // initialize array for storing indexes of each patch
        setNumPatches(bezier, atoi(line.c_str()));

        // get patches indexes
        for (int i=0; i<bezier->numPatches; i++) {
            getline(inFile, line);

            istringstream indexes (line);
            for (int idx = 0; indexes.good(); idx++) {
                string substr;
                getline(indexes, substr, ',');
                bezier->patchIndexes[i][idx] = atoi(substr.c_str());
            }
        }


        // get number of control points
        getline(inFile, line);

        // initialize array for storing control points
        setNumControlPoints(bezier, atoi(line.c_str()));

        // get control points
        for (int i=0; i<bezier->numControlPoints; i++) {
            getline(inFile, line);

            istringstream points (line);
            for (int idx = 0; points.good(); idx++) {
                string substr;
                getline(points, substr, ',');
                bezier->controlPoints[i][idx] = atof(substr.c_str());
            }
        }

        inFile.close();
        return bezier;
    }
}


// Multiply a matrix with a vector and store the result in the third argument
void multMatrixVector(float** m, float *v, float *res) {
    for (int j = 0; j < 4; ++j) {
        res[j] = 0;
        for (int k = 0; k < 4; ++k) res[j] += v[k] * m[k][j];
    }
}


float* bezier(float t, float* p0, float* p1, float* p2, float* p3) {
    float* points[4] = { p0,p1,p2,p3 };
    float vectorT[4] = { static_cast<float>(pow((1 - t),3)), static_cast<float>(3 * t * pow((1 - t),2)), static_cast<float>(3 * (1 - t) * pow(t,2)), static_cast<float>(pow(t,3)) };

    auto* res = (float*)malloc(sizeof(float*) * 3);
    multMatrixVector(points, vectorT, res);
    return res;
}

float* bezierPatch(float u, float t, float** points, int* index) {
    float* controlPoints[4];
    for (int i = 0;i < 4;i++) {    // for every 4 points
        float* p0 = points[index[4 * i]];
        float* p1 = points[index[4 * i + 1]];
        float* p2 = points[index[4 * i + 2]];
        float* p3 = points[index[4 * i + 3]];

        float* point = bezier(u, p0, p1, p2, p3);
        controlPoints[i] = point;
    }
    return bezier(t, controlPoints[0], controlPoints[1], controlPoints[2], controlPoints[3]);
}



void generateBezier (int tessellation, char* inputFile, FILE* file) {

    // reads patch info from input file and stores it
    BezierHolder bezier = readBezierPatch(inputFile);

    PointHolder ph = initPointHolder();

    float step = 1.0 / tessellation;
    float u, u1, v, v1;

    for (int i = 0; i < bezier->numPatches; i++) {
        for (int j = 0; j < tessellation; j++) {
            for (int k = 0; k < tessellation; k++) {
                u = step * (float)j;
                u1 = step * (float)(j + 1);
                v = step * (float)k;
                v1 = step * (float)(k + 1);

                auto* aux = bezierPatch(u, v, bezier->controlPoints, bezier->patchIndexes[i]);
                auto* aux1 = bezierPatch(u, v1, bezier->controlPoints, bezier->patchIndexes[i]);
                auto* aux2 = bezierPatch(u1, v, bezier->controlPoints, bezier->patchIndexes[i]);
                auto* aux3 = bezierPatch(u1, v1, bezier->controlPoints, bezier->patchIndexes[i]);

                addPoint(ph,aux[0],aux[1],aux[2]
                        ,0,0,0,
                         u,v);
                addPoint(ph,aux2[0],aux2[1],aux2[2]
                        ,0,0,0,
                         u1,v);
                addPoint(ph,aux3[0],aux3[1],aux3[2]
                        ,0,0,0,
                         u1,v1);

                addPoint(ph,aux[0],aux[1],aux[2]
                        ,0,0,0,
                         u,v);
                addPoint(ph,aux3[0],aux3[1],aux3[2]
                        ,0,0,0,
                         u1,v1);
                addPoint(ph,aux1[0],aux1[1],aux1[2]
                        ,0,0,0,
                         u,v1);
            }
        }
    }

    printToFile(ph,file);
}