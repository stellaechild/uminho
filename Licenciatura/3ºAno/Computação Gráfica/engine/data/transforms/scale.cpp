//
// Created by vicshadow on 29-04-2023.
//

#include "scale.h"


void scale::loadScale(xml_node myNode){

    this->scaleX = myNode.attribute("x").as_float(1);
    this->scaleY = myNode.attribute("y").as_float(1);
    this->scaleZ = myNode.attribute("z").as_float(1);

}

void scale::calculateAndApplyScale(){
    glScalef(this->scaleX,this->scaleY,this->scaleZ);
}

scale::scale() = default;
