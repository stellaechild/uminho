//
// Created by vicshadow on 10-05-2023.
//

#include "light.h"

using namespace std;

void light::loadLight(pugi::xml_node myNode,short numLight) {
    glEnable(numLight);
    this->num = numLight;

    float dark[4] = {0.2, 0.2, 0.2, 1.0};
    float white[4] = {1.0, 1.0, 1.0, 1.0};

    glLightfv(numLight, GL_AMBIENT, dark);
    glLightfv(numLight, GL_DIFFUSE, white);
    glLightfv(numLight, GL_SPECULAR, white);

    string strType = myNode.attribute("type").as_string();

    if(strType == "point"){
        this->type = point;
        this->posX = myNode.attribute("posx").as_float();
        this->posY = myNode.attribute("posy").as_float();
        this->posZ = myNode.attribute("posz").as_float();
    }
    else if(strType == "directional"){
        this->type = directional;
        this->dirX = myNode.attribute("dirx").as_float();
        this->dirY = myNode.attribute("diry").as_float();
        this->dirZ = myNode.attribute("dirz").as_float();

    }
    else if(strType == "spot"){
        this->type = spotlight;
        this->posX = myNode.attribute("posx").as_float();
        this->posY = myNode.attribute("posy").as_float();
        this->posZ = myNode.attribute("posz").as_float();
        this->dirX = myNode.attribute("dirx").as_float();
        this->dirY = myNode.attribute("diry").as_float();
        this->dirZ = myNode.attribute("dirz").as_float();
        this->cutoff = myNode.attribute("cutoff").as_float();
    }
    else {
        throw std::invalid_argument("Invalid Light Type '"+strType+"'\n");
    }

}

void light::draw(bool debug){

    if(debug){ //LINHAS AMARELAS
        float
        yellow[4] = {255,255,0,1},
        blank[4] = {0,0,0,1};

        glMaterialfv(GL_FRONT, GL_SPECULAR, blank);
        glMaterialf(GL_FRONT, GL_SHININESS, 0);
        glMaterialfv(GL_FRONT,GL_EMISSION,yellow);
        glMaterialfv(GL_FRONT, GL_AMBIENT, yellow);
        glMaterialfv(GL_FRONT, GL_DIFFUSE, yellow);
    }

    if (this->type == point){
        float pos[4] = {this->posX,this->posY,this->posZ,1};
        glLightfv(this->num,GL_POSITION,pos);

        if (debug){
            glBegin(GL_LINES);
            glVertex3f(this->posX-6,this->posY,this->posZ);
            glVertex3f(this->posX+6,this->posY,this->posZ);

            glVertex3f(this->posX,this->posY-6,this->posZ);
            glVertex3f(this->posX,this->posY+6,this->posZ);

            glVertex3f(this->posX,this->posY,this->posZ-6);
            glVertex3f(this->posX,this->posY,this->posZ+6);
            glEnd();
        }
    }
    else if (this->type == directional){
        float pos[4] = {this->dirX,this->dirY,this->dirZ,0};
        glLightfv(this->num,GL_POSITION,pos);

        if (debug){
            glBegin(GL_LINES);
            glVertex3f(this->dirX*(-6),this->dirY*(-6),this->dirZ*(-6));
            glVertex3f(this->dirX*(6),this->dirY*(6),this->dirZ*(6));
            glEnd();
        }
    }
    else if (this->type == spotlight){
        float pos[4] = {this->posX,this->posY,this->posZ,1};
        float dir[3] = {this->dirX,this->dirY,this->dirZ};

        glLightfv(this->num,GL_POSITION,pos);;
        glLightfv(this->num,GL_SPOT_CUTOFF, &this->cutoff);
        glLightfv(this->num,GL_SPOT_DIRECTION, dir);

        if (debug){
            glBegin(GL_LINES);
            glVertex3f(this->posX,this->posY,this->posZ);
            glVertex3f(this->posX + this->dirX*12,this->dirY + this->dirY * 12,this->dirZ+ this->dirZ * 12);
            glEnd();
        }
    }

}

light::light(xml_node myNode,short numLight){
    this->loadLight(myNode,numLight);
}
