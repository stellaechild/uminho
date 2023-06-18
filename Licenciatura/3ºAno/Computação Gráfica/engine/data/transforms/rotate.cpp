//
// Created by vicshadow on 29-04-2023.
//

#include "rotate.h"


void rotate::loadRotate(xml_node myNode){

    this->rotX = myNode.attribute("x").as_float();
    this->rotY = myNode.attribute("y").as_float();
    this->rotZ = myNode.attribute("z").as_float();

    this->rotAngle = myNode.attribute("angle").as_float();
    this->time = myNode.attribute("time").as_float();

    if(this->time != 0){
        this->angPerTime = 360 / this->time;
    }


    //if (this->rotAngle != 0 && this->time != 0) throw std::invalid_argument("Rotation: No support for both Time and Angle");
}

void rotate::calculateAndApplyRotate(float timeElapsed){
    this->rotAngle += timeElapsed * this->angPerTime;

    if (this->rotAngle >= 360) this->rotAngle -= 360;
    if (this->rotAngle < 0) this->rotAngle += 360;

    glRotatef(this->rotAngle,this->rotX,this->rotY,this->rotZ);
}

rotate::rotate() = default;
