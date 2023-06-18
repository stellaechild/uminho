#include <stdio.h>
#include <math.h>
#include "../pointHolder.h"

void generateCylinder(float radius, float height, int slices, FILE* file) {

    PointHolder ph = initPointHolder();

    float angle = (M_PI * 2) / slices;
    float subTextCoordX = 1.0 / slices;

    for (int i = 1; i <= slices; i++) {

        float actualNormalX = sin(angle * (float)i);
        float actualNormalZ = cos(angle * (float)i);

        float nextNormalX = sin(angle * ((float)i + 1));
        float nextNormalZ = cos(angle * ((float)i + 1));

        //Valores "atuais" de X e Z
        float actualPointX = radius * actualNormalX;
        float actualPointZ = radius * actualNormalZ;

        // Valores "seguintes" de X e Z
        float nextPointX = radius * nextNormalX;
        float nextPointZ = radius * nextNormalZ;


		addPoint(ph, 0.0f, height, 0.0f,
                 0.0,1.0,0.0,
                 0.5,1);
		addPoint(ph, actualPointX, height, actualPointZ,
                 0.0,1.0,0.0,
                 0.5,1);
		addPoint(ph, nextPointX, height, nextPointZ,
                 0.0,1.0,0.0,
                 0.5,1);


		addPoint(ph, nextPointX, 0, nextPointZ,
                 nextNormalX, 0.0, nextNormalZ,
                 subTextCoordX * (i + 1) ,1);
		addPoint(ph, nextPointX, height, nextPointZ,
                 nextNormalX, 0.0, nextNormalZ,
                 subTextCoordX * (i + 1),0);
		addPoint(ph, actualPointX, height, actualPointZ,
                 actualNormalX, 0.0, actualNormalZ,
                 subTextCoordX * i,0);


		addPoint(ph, actualPointX, height, actualPointZ,
                 actualNormalX, 0.0, actualNormalZ,
                 subTextCoordX * i,0);
		addPoint(ph, actualPointX, 0, actualPointZ,
                 actualNormalX, 0.0, actualNormalZ,
                 subTextCoordX * i,1);
		addPoint(ph, nextPointX, 0, nextPointZ,
                 nextNormalX, 0.0, nextNormalZ,
                 subTextCoordX * (i + 1),1);


		addPoint(ph, actualPointX, 0, actualPointZ,
                 0.0, -1.0, 0.0,
                 0.5,0);
		addPoint(ph, 0.0, 0, 0.0,
                 0.0, -1.0, 0.0,
                 0.5,0);
		addPoint(ph, nextPointX, 0, nextPointZ,
                 0.0, -1.0, 0.0,
                 0.5,0);
    }

    printToFile(ph, file);
}