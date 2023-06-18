#include <math.h>
#include "../pointHolder.h"

/*
 * order of points generated:
 * for each stack of each slice:
 *
 * P4 ------- P3
 * |          |
 * |          |
 * |          |
 * P1 ------- P2
 *
 */

void generateTorus (float fullRadius, float smallRadius, int slices, int stacks, FILE *file) {
    float x1, x2, x3, x4, y1, y2, y3, y4, z1, z2, z3, z4;
    float theta = 0, phi = 0;
    float sliceAngle = 2 * M_PI / slices;
    float stackAngle = 2 * M_PI / stacks;
    // stacks == phi, slices == theta

    float subAlphaTextCoord = 1 / ((float) slices);
    float subBetaTextCoord = 1 / ((float) stacks);

    PointHolder ph = initPointHolder();

    for (int i = 0; i < slices; i++) {
        for (int j = 0; j < stacks; j++) {
            float n1[3],n2[3],n3[3],n4[3];

            n1[0] = cos(phi) * cos(theta);
            n1[1] = sin(phi);
            n1[2] = cos(phi) * sin(theta);

            n2[0] = cos(phi) * cos(theta + sliceAngle);
            n2[1] = sin(phi);
            n2[2] = cos(phi) * sin(theta + sliceAngle);

            n3[0] = cos(phi + stackAngle) * cos(theta + sliceAngle);
            n3[1] = sin(phi + stackAngle);
            n3[2] = cos(phi + stackAngle) * sin(theta + sliceAngle);

            n4[0] = cos(phi + stackAngle) * cos(theta);
            n4[1] = sin(phi + stackAngle);
            n4[2] = cos(phi + stackAngle) * sin(theta);

            normalize(n1);normalize(n2);normalize(n3);normalize(n4);

            x1 = (fullRadius + smallRadius * cos(phi)) * cos(theta);
            y1 = smallRadius * sin(phi);
            z1 = (fullRadius + smallRadius * cos(phi)) * sin(theta);

            x2 = (fullRadius + smallRadius * cos(phi)) * cos(theta + sliceAngle);
            y2 = smallRadius * sin(phi);
            z2 = (fullRadius + smallRadius * cos(phi)) * sin(theta + sliceAngle);

            x3 = (fullRadius + smallRadius * cos(phi + stackAngle)) * cos(theta + sliceAngle);
            z3 = (fullRadius + smallRadius * cos(phi + stackAngle)) * sin(theta + sliceAngle);
            y3 = smallRadius * sin(phi + stackAngle);

            x4 = (fullRadius + smallRadius * cos(phi + stackAngle)) * cos(theta);
            z4 = (fullRadius + smallRadius * cos(phi + stackAngle)) * sin(theta);
            y4 = smallRadius * sin(phi + stackAngle);


            addPoint(ph, x1, y1, z1,
                     n1[0], n1[1], n1[2],
                     subAlphaTextCoord * i,1 - subBetaTextCoord * j);
            addPoint(ph, x2, y2, z2,
                     n2[0], n2[1], n2[2],
                     subAlphaTextCoord * (i + 1),1 - subBetaTextCoord * j);
            addPoint(ph, x3, y3, z3,
                     n3[0], n3[1], n3[2],
                     subAlphaTextCoord * (i + 1),1 - subBetaTextCoord * (j + 1));

            addPoint(ph, x3, y3, z3,
                     n3[0], n3[1], n3[2],
                     subAlphaTextCoord * (i + 1),1 - subBetaTextCoord * (j + 1));
            addPoint(ph, x4, y4, z4,
                     n4[0], n4[1], n4[2],
                     subAlphaTextCoord * i,1 - subBetaTextCoord * (j + 1));
            addPoint(ph, x1, y1, z1,
                     n1[0], n1[1], n1[2],
                     subAlphaTextCoord * i,1 - subBetaTextCoord * 1);

            phi = stackAngle * (float) (j + 1);
        }
        theta = sliceAngle * (float) (i + 1);
    }

    printToFile(ph, file);
}