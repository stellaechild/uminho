#include <stdio.h>
#include <math.h>
#include "../pointHolder.h"


void generateCone(float radius, float height, float slices, float stacks, FILE* file) {
    PointHolder ph = initPointHolder();
	float doisPI = M_PI * 2;
	float slice = (doisPI / slices);
	float stack = height / stacks;
	float raio = radius / stacks;

    float normY = sin(atan(radius/height));

    float subTextCoordX = (1 / doisPI);
    float subTextCoordY = (1 / stacks) * 2;

    for (float i = 0; i < doisPI; i += slice) {
        addPoint(ph, 0.0f, 0.0f, 0.0f
                 ,0,-1,0,
                 0.5,0);
        addPoint(ph, (sin(i + slice) * radius),0.0f, (cos(i + slice) * radius)
                ,0,-1,0,
                 0.5,0);
        addPoint(ph, sin(i) * radius,0.0f, cos(i) * radius
                ,0,-1,0,
                 0.5,0);
    }
    float r = 0;
    for (float j = 0; j < (height); j += stack) {
        for (float i = 0; i < doisPI; i += slice) {
            float normAct[3] = {sin(i),normY ,cos(i)};
            float normNext[3] = {sin(i + slice),normY ,cos(i + slice)};
            normalize(normAct);normalize(normNext);

            addPoint(ph, sin(i) * (radius - (r + raio)), j + stack, cos(i) * (radius - (r + raio))
                    ,normAct[0],normAct[1],normAct[2],
                     subTextCoordX * i,1 - subTextCoordY * (j + stack));
            addPoint(ph, sin(i) * (radius - r), j, cos(i) * (radius - r)
                    ,normAct[0],normAct[1],normAct[2],
                     subTextCoordX * i,1 - subTextCoordY * j);
            addPoint(ph,sin(i + slice) * (radius - r), j, cos(i + slice) * (radius - r)
                    ,normNext[0],normNext[1],normNext[2],
                     subTextCoordX * (i + slice),1 - subTextCoordY * j);

            addPoint(ph, sin(i) * (radius - (r + raio)), j + stack, cos(i) * (radius - (r + raio))
                    ,normAct[0],normAct[1],normAct[2],
                     subTextCoordX * i,1 - subTextCoordY * (j + stack));
            addPoint(ph,sin(i + slice) * (radius - r), j, cos(i + slice) * (radius - r)
                    ,normNext[0],normNext[1],normNext[2],
                     subTextCoordX * (i + slice),1 - subTextCoordY * j);
            addPoint(ph, sin(i + slice) * (radius - (r + raio)), j + stack, cos(i + slice) * (radius - (r + raio))
                    ,normNext[0],normNext[1],normNext[2],
                     subTextCoordX * (i + slice),1 - subTextCoordY * (j + stack));
        }
        r += raio;
    }

    /*
    r = 0;
    for (float j = 0 ;j < (height -stack); j += stack) {
        for (float i = 0; i < doisPI; i += slice) {
            float normAct[3] = {sin(i),normY ,cos(i)};
            float normNext[3] = {sin(i + slice),normY ,cos(i + slice)};
            normalize(normAct);normalize(normNext);

            addPoint(ph, sin(i) * (radius - (r + raio)), j + stack, cos(i) * (radius - (r + raio))
                    ,normAct[0],normAct[1],normAct[2]);
            addPoint(ph,sin(i + slice) * (radius - r), j, cos(i + slice) * (radius - r)
                    ,normNext[0],normNext[1],normNext[2]);
            addPoint(ph, sin(i + slice) * (radius - (r + raio)), j + stack, cos(i + slice) * (radius - (r + raio))
                    ,normNext[0],normNext[1],normNext[2]);
        }
        r += raio;
    }
     */

	


    printToFile(ph, file);
}
