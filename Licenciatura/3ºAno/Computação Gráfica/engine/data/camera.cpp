//
// Created by vicshadow on 25-02-2023.
//

#include "camera.h"

//Default Values
camera::camera(){
    this->posX=5;this->posY=5;this->posZ=5;
    this->lookX=0;this->lookY=0;this->lookZ=0;
    this->upX=0;this->upY=1;this->upZ=0;
    this->fov=60;this->near=1;this->far=1000;
}

void camera::setPosition(float argPosX, float argPosY, float argPosZ){
    this->posX=argPosX;this->posY=argPosY;this->posZ=argPosZ;
}

void camera::setLookAt(float argLookX,float argLookY,float argLookZ){
    this->lookX=argLookX;this->lookY=argLookY;this->lookZ=argLookZ;
}
void camera::setUp(float argUpX,float argUpY,float argUpZ){
    this->upX=argUpX;this->upY=argUpY;this->upZ=argUpZ;
}
void camera::setFov(float argFov){
    this->fov=argFov;
}
void camera::setNearFar(float argNear, float argFar){
    this->near=argNear;this->far=argFar;
}
