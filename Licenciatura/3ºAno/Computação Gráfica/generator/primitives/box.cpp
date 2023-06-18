#include <stdio.h>
#include "../pointHolder.h"
#include "plane.cpp"

void generateBox(int length,int divisions,FILE* file){
    PointHolder ph = initPointHolder();
    float subLength = ((float) length) / ((float) 2);
    addGenericPlane(ph,length,divisions,'y', true,subLength);
    addGenericPlane(ph,length,divisions,'y', false,-subLength);

    addGenericPlane(ph,length,divisions,'x', true,subLength);
    addGenericPlane(ph,length,divisions,'x', false,-subLength);

    addGenericPlane(ph,length,divisions,'z', true,subLength);
    addGenericPlane(ph,length,divisions,'z', false,-subLength);

    printToFile(ph,file);
}