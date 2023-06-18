#include <math.h>
#include "../pointHolder.h"

void addSphere(float radius, int slices, int stacks,float posX, float posY, float  posZ, bool facingOut,PointHolder ph){
    float subAlpha = M_PI * 2 / ((float) slices);
    float subBeta = M_PI / ((float) stacks);
    float delta = (M_PI / ((float) 2));

    float subAlphaTextCoord = 1 / ((float) slices);
    float subBetaTextCoord = 1 / ((float) stacks);

    for(int actSlice = 0; actSlice < slices; actSlice++){
        for(int actStack = 0; actStack < stacks;actStack++){
            //Calcular Normais
            float n1X = cos(subBeta * actStack - delta ) * sin(subAlpha * actSlice);
            float n1Y = sin(subBeta * actStack - delta);
            float n1Z = cos(subBeta * actStack - delta) * cos(subAlpha * actSlice);

            float n2X = cos(subBeta * actStack - delta) * sin(subAlpha * (actSlice+1));
            float n2Y = sin(subBeta * actStack - delta);
            float n2Z = cos(subBeta * actStack - delta) * cos(subAlpha * (actSlice+1));

            float n3X = cos(subBeta * (actStack+1) - delta) * sin(subAlpha * (actSlice+1));
            float n3Y = sin(subBeta * (actStack+1) - delta);
            float n3Z = cos(subBeta * (actStack+1) - delta) * cos(subAlpha * (actSlice+1));

            float n4X = cos(subBeta * (actStack+1) - delta) * sin(subAlpha * actSlice);
            float n4Y = sin(subBeta * (actStack+1) - delta);
            float n4Z = cos(subBeta * (actStack+1) - delta) * cos(subAlpha * actSlice);

            //Calcular Pontos
            float p1X = posX + radius * n1X;
            float p1Y = posY + radius * n1Y;
            float p1Z = posZ + radius * n1Z;

            float p2X = posX + radius * n2X;
            float p2Y = posY + radius * n2Y;
            float p2Z = posZ + radius * n2Z;

            float p3X = posX + radius * n3X;
            float p3Y = posY + radius * n3Y;
            float p3Z = posZ + radius * n3Z;

            float p4X = posX + radius * n4X;
            float p4Y = posY + radius * n4Y;
            float p4Z = posZ + radius * n4Z;

            if(!facingOut){
                addPoint(ph,p1X,p1Y,p1Z,
                         -n1X,-n1Y,-n1Z,
                         subAlphaTextCoord * actSlice,1 - subBetaTextCoord * actStack);
                addPoint(ph,p3X,p3Y,p3Z,
                         -n3X,-n3Y,-n3Z,
                         subAlphaTextCoord * (actSlice + 1),1 - subBetaTextCoord * (actStack +1));
                addPoint(ph,p2X,p2Y,p2Z,
                         -n2X,-n2Y,-n2Z,
                         subAlphaTextCoord * (actSlice + 1),1 - subBetaTextCoord * actStack);


                addPoint(ph,p1X,p1Y,p1Z,
                         -n1X,-n1Y,-n1Z,
                         subAlphaTextCoord * actSlice,1 - subBetaTextCoord * actStack);
                addPoint(ph,p4X,p4Y,p4Z,
                         -n4X,-n4Y,-n4Z,
                         subAlphaTextCoord * actSlice,1 - subBetaTextCoord * (actStack +1));
                addPoint(ph,p3X,p3Y,p3Z,
                         -n3X,-n3Y,-n3Z,
                         subAlphaTextCoord * (actSlice + 1),1 - subBetaTextCoord * (actStack +1));
            }
            else{
                addPoint(ph,p1X,p1Y,p1Z,
                         n1X,n1Y,n1Z,
                         subAlphaTextCoord * actSlice,1 - subBetaTextCoord * actStack);
                addPoint(ph,p2X,p2Y,p2Z,
                         n2X,n2Y,n2Z,
                         subAlphaTextCoord * (actSlice + 1),1 - subBetaTextCoord * actStack);
                addPoint(ph,p3X,p3Y,p3Z,
                         n3X,n3Y,n3Z,
                         subAlphaTextCoord * (actSlice + 1),1 - subBetaTextCoord * (actStack +1));

                addPoint(ph,p1X,p1Y,p1Z,
                         n1X,n1Y,n1Z,
                         subAlphaTextCoord * actSlice,1 - subBetaTextCoord * actStack);
                addPoint(ph,p3X,p3Y,p3Z,
                         n3X,n3Y,n3Z,
                         subAlphaTextCoord * (actSlice + 1),1 - subBetaTextCoord * (actStack +1));
                addPoint(ph,p4X,p4Y,p4Z,
                         n4X,n4Y,n4Z,
                         subAlphaTextCoord * actSlice,1 - subBetaTextCoord * (actStack +1));
            }
        }
    }
}

void generateSphere(int radius, int slices, int stacks,int facingOut,FILE *file){

    bool facingOutBool = (facingOut != 0);
    PointHolder ph = initPointHolder();
    addSphere(float(radius),slices,stacks,0,0,0, facingOutBool,ph);
    printToFile(ph,file);
}

/*
void generateSphere(int radius, int slices, int stacks,FILE* file){
    PointHolder ph = initPointHolder();

    float subAlpha = M_PI * 2 / ((float) slices);
    float subBeta = M_PI / ((float) stacks);
    float delta = (M_PI / ((float) 2));

    for(int actSlice = 0; actSlice < slices; actSlice++){
        for(int actStack = 0; actStack < stacks;actStack++){
            float p1X = radius * cos(subBeta * actStack - delta ) * sin(subAlpha * actSlice);
            float p1Y = radius * sin(subBeta * actStack - delta);
            float p1Z = radius * cos(subBeta * actStack - delta) * cos(subAlpha * actSlice);

            float p2X = radius * cos(subBeta * actStack - delta) * sin(subAlpha * (actSlice+1));
            float p2Y = radius * sin(subBeta * actStack - delta);
            float p2Z = radius * cos(subBeta * actStack - delta) * cos(subAlpha * (actSlice+1));

            float p3X = radius * cos(subBeta * (actStack+1) - delta) * sin(subAlpha * (actSlice+1));
            float p3Y = radius * sin(subBeta * (actStack+1) - delta);
            float p3Z = radius * cos(subBeta * (actStack+1) - delta) * cos(subAlpha * (actSlice+1));

            float p4X = radius * cos(subBeta * (actStack+1) - delta) * sin(subAlpha * actSlice);
            float p4Y = radius * sin(subBeta * (actStack+1) - delta);
            float p4Z = radius * cos(subBeta * (actStack+1) - delta) * cos(subAlpha * actSlice);

            addPoint(ph,p1X,p1Y,p1Z,
                     cos(subBeta * actStack - delta ) * sin(subAlpha * actSlice), sin(subBeta * actStack - delta), cos(subBeta * actStack - delta) * cos(subAlpha * actSlice));
            addPoint(ph,p2X,p2Y,p2Z,
                     cos(subBeta * actStack - delta) * sin(subAlpha * (actSlice+1)), sin(subBeta * actStack - delta), cos(subBeta * actStack - delta) * cos(subAlpha * (actSlice+1)));
            addPoint(ph,p3X,p3Y,p3Z,
                     cos(subBeta * (actStack+1) - delta) * sin(subAlpha * (actSlice+1)), sin(subBeta * (actStack+1) - delta), cos(subBeta * (actStack+1) - delta) * cos(subAlpha * (actSlice+1)));

            addPoint(ph,p1X,p1Y,p1Z,
                     cos(subBeta * actStack - delta ) * sin(subAlpha * actSlice), sin(subBeta * actStack - delta), cos(subBeta * actStack - delta) * cos(subAlpha * actSlice));
            addPoint(ph,p3X,p3Y,p3Z,
                     cos(subBeta * (actStack+1) - delta) * sin(subAlpha * (actSlice+1)), sin(subBeta * (actStack+1) - delta), cos(subBeta * (actStack+1) - delta) * cos(subAlpha * (actSlice+1)));
            addPoint(ph,p4X,p4Y,p4Z,
                     cos(subBeta * (actStack+1) - delta) * sin(subAlpha * actSlice), sin(subBeta * (actStack+1) - delta), cos(subBeta * (actStack+1) - delta) * cos(subAlpha * actSlice));
        }
    }

    printToFile(ph,file);
}
 */