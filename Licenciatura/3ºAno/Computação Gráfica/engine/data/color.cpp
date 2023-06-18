//
// Created by vicshadow on 10-05-2023.
//

#include "color.h"

#define normVal 0.0039215686

void color::loadColor(xml_node myNode){

    //Load diffuse
    xml_node diffuse = myNode.child("diffuse");
    if (diffuse){
        this->diffuseR = diffuse.attribute("R").as_int(200);
        this->diffuseG = diffuse.attribute("G").as_int(200);
        this->diffuseB = diffuse.attribute("B").as_int(200);
    }

    //Load ambient
    xml_node ambient = myNode.child("ambient");
    if (ambient){
        this->ambientR = ambient.attribute("R").as_int(50);
        this->ambientG = ambient.attribute("G").as_int(50);
        this->ambientB = ambient.attribute("B").as_int(50);
    }

    //Load specular
    xml_node specular = myNode.child("specular");
    if (specular){
        this->specularR = specular.attribute("R").as_int(0);
        this->specularG = specular.attribute("G").as_int(0);
        this->specularB = specular.attribute("B").as_int(0);
    }

    //Load emissive
    xml_node emissive = myNode.child("emissive");
    if (emissive){
        this->emissiveR = emissive.attribute("R").as_int(0);
        this->emissiveG = emissive.attribute("G").as_int(0);
        this->emissiveB = emissive.attribute("B").as_int(0);
    }

    //Load shininess
    xml_node shininess = myNode.child("shininess");
    if (shininess){
        this->shininess = shininess.attribute("value").as_int(0);
    }

}

void color::getDiffuse(float* diffuse) const {
    diffuse[0] = this->diffuseR * normVal;
    diffuse[1] = this->diffuseG * normVal;
    diffuse[2] = this->diffuseB * normVal;
    diffuse[3] = 1;
}

void color::getAmbient(float* ambient) const {
    ambient[0] = this->ambientR * normVal;
    ambient[1] = this->ambientG * normVal;
    ambient[2] = this->ambientB * normVal;
    ambient[3] = 1;
}

void color::getSpecular(float* specular) const {
    specular[0] = this->specularR * normVal;
    specular[1] = this->specularG * normVal;
    specular[2] = this->specularB * normVal;
    specular[3] = 1;
}

void color::getEmissive(float* emissive) const {
    emissive[0] = this->emissiveR * normVal;
    emissive[1] = this->emissiveG * normVal;
    emissive[2] = this->emissiveB * normVal;
    emissive[3] = 1;
}


color::color() = default;