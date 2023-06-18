#include "../pointHolder.h"
#include "sphere.cpp"
#include "math.h"

void generateAsteroidBelt (float fullRadius, float smallRadius, int slices, int stacks, FILE *file) {
    float x1, y1, z1;
    float theta = 0, phi = 0;
    float sliceAngle = 2 * M_PI / slices;
    float stackAngle = 2 * M_PI / stacks;
    // stacks == phi, slices == theta

    PointHolder ph = initPointHolder();

    for (int i = 0; i < slices; i++) {
        for (int j = 0; j < stacks; j++) {

            float r = rand()*(1/RAND_MAX) + 0.5;
            int slice = 4; int stack = 5;

            x1 = ((fullRadius + smallRadius * cos(phi)) * cos(theta)) + float(rand()*(1/RAND_MAX) -0.5);
            z1 = ((fullRadius + smallRadius * cos(phi)) * sin(theta)) + float(rand()*(1/RAND_MAX) -0.5);
            y1 = (smallRadius * sin(phi)) + float(rand()*(1/RAND_MAX) -0.5);
            addSphere(r, slice, stack,x1, y1, z1, true ,ph);

            j += rand()%(3-1 + 1) + 1;
            phi = stackAngle * (float) (j + 1);
        }
        theta = sliceAngle * (float) (i + 1);
    }

    printToFile(ph, file);
}