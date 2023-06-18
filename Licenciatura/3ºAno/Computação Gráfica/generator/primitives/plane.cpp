#include <stdio.h>
#include "../pointHolder.h"


void planeYZ(PointHolder ph,int length, int divisions,bool facing,float distance){
    float subDivisionLen = ((float) length)/((float) divisions);
    float subDivisionTextCoord = 1.0f /((float) divisions);
    for (int line = 0; line < divisions;line++){
        //Valores "atuais" de X e Y
        float actY = ((float) length)/2 -  ((float) line) * subDivisionLen;
        float textCoordY =  ((float) line) * subDivisionTextCoord;
        for(int col = 0; col < divisions;col++){
            float actZ = ((float) length)/2 -  ((float) col) * subDivisionLen;
            float textCoordZ =  ((float) col) * subDivisionTextCoord;

            if(facing){
                addPoint(ph,distance,actY,actZ,
                         1.0, 0.0, 0.0,
                         textCoordY,textCoordZ);
                addPoint(ph,distance,actY-subDivisionLen,actZ-subDivisionLen,
                         1.0, 0.0, 0.0,
                         textCoordY+subDivisionTextCoord,textCoordZ+subDivisionTextCoord);
                addPoint(ph,distance,actY,actZ-subDivisionLen,
                         1.0, 0.0, 0.0,
                         textCoordY,textCoordZ+subDivisionTextCoord);

                addPoint(ph,distance,actY,actZ,
                         1.0, 0.0, 0.0,
                         textCoordY,textCoordZ);
                addPoint(ph,distance,actY-subDivisionLen,actZ,
                         1.0, 0.0, 0.0,
                         textCoordY+subDivisionTextCoord,textCoordZ);
                addPoint(ph,distance,actY-subDivisionLen,actZ-subDivisionLen,
                         1.0, 0.0, 0.0,
                         textCoordY+subDivisionTextCoord,textCoordZ+subDivisionTextCoord);
            }
            else{
                addPoint(ph,distance,actY,actZ,
                         -1.0, 0.0, 0.0,
                         textCoordY,textCoordZ);
                addPoint(ph,distance,actY,actZ-subDivisionLen,
                         -1.0, 0.0, 0.0,
                         textCoordY,textCoordZ+subDivisionTextCoord);
                addPoint(ph,distance,actY-subDivisionLen,actZ-subDivisionLen,
                         -1.0, 0.0, 0.0,
                         textCoordY+subDivisionTextCoord,textCoordZ+subDivisionTextCoord);

                addPoint(ph,distance,actY,actZ,
                         -1.0, 0.0, 0.0,
                         textCoordY,textCoordZ);
                addPoint(ph,distance,actY-subDivisionLen,actZ-subDivisionLen,
                         -1.0, 0.0, 0.0,
                         textCoordY+subDivisionTextCoord,textCoordZ+subDivisionTextCoord);
                addPoint(ph,distance,actY-subDivisionLen,actZ,
                         -1.0, 0.0, 0.0,
                         textCoordY+subDivisionTextCoord,textCoordZ);
            }

        }
    }
}


void planeXZ(PointHolder ph,int length, int divisions,bool facing,float distance){
    float subDivisionLen = ((float) length)/((float) divisions);
    float subDivisionTextCoord = 1.0f /((float) divisions);
    for (int line = 0; line < divisions;line++){
        //Valores "atuais" de X e Z
        float actZ = ((float) length)/2 -  ((float) line) * subDivisionLen;
        float textCoordZ =  ((float) line) * subDivisionTextCoord;
        for(int col = 0; col < divisions;col++){
            float actX = ((float) length)/2 -  ((float) col) * subDivisionLen;
            float textCoordX =  ((float) col) * subDivisionTextCoord;

            if(facing){
                addPoint(ph,actX,distance,actZ,
                         0.0, 1.0, 0.0,
                         textCoordX,textCoordZ);
                addPoint(ph,actX,distance,actZ-subDivisionLen,
                         0.0, 1.0, 0.0,
                         textCoordX,textCoordZ+subDivisionTextCoord);
                addPoint(ph,actX-subDivisionLen,distance,actZ-subDivisionLen,
                         0.0, 1.0, 0.0,
                         textCoordX+subDivisionTextCoord,textCoordZ+subDivisionTextCoord);

                addPoint(ph,actX,distance,actZ,
                         0.0, 1.0, 0.0,
                         textCoordX,textCoordZ);
                addPoint(ph,actX-subDivisionLen,distance,actZ-subDivisionLen,
                         0.0, 1.0, 0.0,
                         textCoordX+subDivisionTextCoord,textCoordZ+subDivisionTextCoord);
                addPoint(ph,actX-subDivisionLen,distance,actZ,
                         0.0, 1.0, 0.0,
                         textCoordX+subDivisionTextCoord,textCoordZ);
            }
            else{
                addPoint(ph,actX,distance,actZ,
                         0.0, -1.0, 0.0,
                         textCoordX,textCoordZ);
                addPoint(ph,actX-subDivisionLen,distance,actZ-subDivisionLen,
                         0.0, -1.0, 0.0,
                         textCoordX+subDivisionTextCoord,textCoordZ+subDivisionTextCoord);
                addPoint(ph,actX,distance,actZ-subDivisionLen,
                         0.0, -1.0, 0.0,
                         textCoordX,textCoordZ+subDivisionTextCoord);

                addPoint(ph,actX,distance,actZ,
                         0.0, -1.0, 0.0,
                         textCoordX,textCoordZ);
                addPoint(ph,actX-subDivisionLen,distance,actZ,
                         0.0, -1.0, 0.0,
                         textCoordX+subDivisionTextCoord,textCoordZ);
                addPoint(ph,actX-subDivisionLen,distance,actZ-subDivisionLen,
                         0.0, -1.0, 0.0,
                         textCoordX+subDivisionTextCoord,textCoordZ+subDivisionTextCoord);
            }

        }
    }
}

void planeXY(PointHolder ph,int length, int divisions,bool facing,float distance){
    float subDivisionLen = ((float) length)/((float) divisions);
    float subDivisionTextCoord = 1.0f /((float) divisions);
    for (int line = 0; line < divisions;line++){
        //Valores "atuais" de X e Z
        float actY = ((float) length)/2 -  ((float) line) * subDivisionLen;
        float textCoordY =  ((float) line) * subDivisionTextCoord;
        for(int col = 0; col < divisions;col++){
            float actX = ((float) length)/2 -  ((float) col) * subDivisionLen;
            float textCoordX =  ((float) col) * subDivisionTextCoord;

            if(facing){
                addPoint(ph,actX,actY,distance,
                         0.0, 0.0, 1.0,
                         textCoordX,textCoordY);
                addPoint(ph,actX-subDivisionLen,actY-subDivisionLen,distance,
                         0.0, 0.0, 1.0,
                         textCoordX+subDivisionTextCoord,textCoordY+subDivisionTextCoord);
                addPoint(ph,actX,actY-subDivisionLen,distance,
                         0.0, 0.0, 1.0,
                         textCoordX,textCoordY+subDivisionTextCoord);

                addPoint(ph,actX,actY,distance,
                         0.0, 0.0, 1.0,
                         textCoordX,textCoordY);
                addPoint(ph,actX-subDivisionLen,actY,distance,
                         0.0, 0.0, 1.0,
                         textCoordX+subDivisionTextCoord,textCoordY);
                addPoint(ph,actX-subDivisionLen,actY-subDivisionLen,distance,
                         0.0, 0.0, 1.0,
                         textCoordX+subDivisionTextCoord,textCoordY+subDivisionTextCoord);
            }
            else{
                addPoint(ph,actX,actY,distance,
                         0.0, 0.0, -1.0,
                         textCoordX,textCoordY);
                addPoint(ph,actX,actY-subDivisionLen,distance,
                         0.0, 0.0, -1.0,
                         textCoordX,textCoordY+subDivisionTextCoord);
                addPoint(ph,actX-subDivisionLen,actY-subDivisionLen,distance,
                         0.0, 0.0, -1.0,
                         textCoordX+subDivisionTextCoord,textCoordY+subDivisionTextCoord);

                addPoint(ph,actX,actY,distance,
                         0.0, 0.0, -1.0,
                         textCoordX,textCoordY);
                addPoint(ph,actX-subDivisionLen,actY-subDivisionLen,distance,
                         0.0, 0.0, -1.0,
                         textCoordX+subDivisionTextCoord,textCoordY+subDivisionTextCoord);
                addPoint(ph,actX-subDivisionLen,actY,distance,
                         0.0, 0.0, -1.0,
                         textCoordX+subDivisionTextCoord,textCoordY);
            }

        }
    }
}


void addGenericPlane(PointHolder ph, int length, int divisions, char axis,bool direction,float distance){

    if(axis != 'x' && axis != 'y' && axis != 'z') throw 100;

    if (axis == 'x') planeYZ(ph,length,divisions,direction,distance);
    else if(axis == 'y') planeXZ(ph,length,divisions,direction,distance);
    else if(axis == 'z') planeXY(ph,length,divisions,direction,distance);
}

void generateXZPlane(int length,int divisions,FILE* file){
    PointHolder ph = initPointHolder();
    addGenericPlane(ph,length,divisions,'y', true,0);
    printToFile(ph,file);
}