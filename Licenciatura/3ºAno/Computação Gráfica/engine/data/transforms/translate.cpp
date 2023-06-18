//
// Created by vicshadow on 29-04-2023.
//

#include "translate.h"
#include "../../catmullRom.h"

void translate::loadTranslate(xml_node myNode){

    this->transX = myNode.attribute("x").as_float();
    this->transY = myNode.attribute("y").as_float();
    this->transZ = myNode.attribute("z").as_float();

    this->align = myNode.attribute("align").as_bool();
    this->line = myNode.attribute("line").as_bool();
    this->time = myNode.attribute("time").as_float();


    list<float> tmpList;
    for(xml_node point = myNode.first_child(); point; point = point.next_sibling()) {
        float x = point.attribute("x").as_float();
        float y = point.attribute("y").as_float();
        float z = point.attribute("z").as_float();

        tmpList.emplace_back(x);
        tmpList.emplace_back(y);
        tmpList.emplace_back(z);
    }

    this->numPoints = int(tmpList.size()) / 3;
    if(this->numPoints != 0 && this->numPoints < 4) throw std::invalid_argument("Translate: Invalid number of points");

    this->points = (float**) malloc(sizeof(float *) * this->numPoints);
    for(int i = 0;i<this->numPoints;i++){
        this->points[i] = (float*) malloc(sizeof(float) * 3);
        this->points[i][0] = tmpList.front();tmpList.pop_front();
        this->points[i][1] = tmpList.front();tmpList.pop_front();
        this->points[i][2] = tmpList.front();tmpList.pop_front();
    }

}

void translate::calculateAndApplyTranslate(float elapsedTime,bool debug){

    if(debug){ //LINHAS BRANCAS
        float
        white[4] = {255,255,255,1},
        blank[4] = {0,0,0,1};

        glMaterialfv(GL_FRONT, GL_SPECULAR, blank);
        glMaterialf(GL_FRONT, GL_SHININESS, 0);
        glMaterialfv(GL_FRONT,GL_EMISSION,white);
        glMaterialfv(GL_FRONT, GL_AMBIENT, white);
        glMaterialfv(GL_FRONT, GL_DIFFUSE, white);
    }

    if(time == 0){
        glTranslatef(this->transX,this->transY,this->transZ);
    }
    else {
        float res[3] = {0,0,0};
        float deriv[3];

        currTime += elapsedTime;
        if(currTime > time) currTime -= time;
        float normalizedTime = (1 / time) * currTime;

        getGlobalCatmullRomPoint(normalizedTime,this->points,this->numPoints,res,deriv);

        if(line || debug){
            float lineRes[3] = {0,0,0};
            float lineDeriv[3];
            glBegin(GL_LINE_LOOP);
            glColor3f(1.0f, 1.0f, 1.0f);
            for (int i = 0; i < 100; i++) {
                getGlobalCatmullRomPoint(float(float(i) / 100.0),this->points,this->numPoints, lineRes, lineDeriv);
                glVertex3f(this->transX +lineRes[0], this->transY +lineRes[1], this->transZ +lineRes[2]);
            }
            glEnd();

            glBegin(GL_LINES);
            glColor3f(0.5f,0.7f,0.2f);
            glVertex3f( this->transX + res[0],this->transY + res[1], this->transZ + res[2]);
            glVertex3f(deriv[0] + this->transX + res[0],deriv[1] + this->transY + res[1],deriv[2] + this->transZ + res[2]);
            glEnd();
        }

        glTranslatef(this->transX + res[0],this->transY + res[1],this->transZ + res[2]);

        if(align){

            float* li1M = deriv;
            normalize(li1M);

            float li3M[3] ;
            cross(li1M, previousY, li3M);
            normalize(li3M);

            float li2M[3] = { 0,0,0 };
            cross(li3M, li1M, li2M);
            previousY[0] = li2M[0];
            previousY[1] = li2M[1];
            previousY[2] = li2M[2];
            normalize(li2M);

            float* m = (float* )malloc(sizeof(float) * 20);
            buildRotMatrix(li1M, li2M, li3M, m);
            glMultMatrixf(m);
        }

    }


}


translate::translate() = default;
