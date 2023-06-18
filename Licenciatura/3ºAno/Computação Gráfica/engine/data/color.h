//
// Created by vicshadow on 10-05-2023.
//

#ifndef ENGINE_COLOR_H
#define ENGINE_COLOR_H

#include "../pugixml/pugixml.hpp"

using namespace pugi;

class color {
public:
    int diffuseR = 200,diffuseG = 200,diffuseB = 200;
    int ambientR = 50,ambientG = 50,ambientB = 50;
    int specularR = 0,specularG = 0,specularB = 0;
    int emissiveR = 0,emissiveG = 0,emissiveB = 0;
    int shininess = 0;

    color();
    void loadColor(xml_node myNode);

    void getDiffuse(float* diffuse) const;
    void getAmbient(float* ambient) const;
    void getSpecular(float* specular) const;
    void getEmissive(float* emissive) const;

};


#endif //ENGINE_COLOR_H
