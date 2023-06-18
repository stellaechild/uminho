//
// Created by vicshadow on 10-05-2023.
//

#ifndef ENGINE_LIGHT_H
#define ENGINE_LIGHT_H

#include <GL/glew.h>
#include <GL/glut.h>
#include "../pugixml/pugixml.hpp"
#include <iostream>

using namespace pugi;

class light {
public:
    enum type {
        point = 1,
        directional = 2,
        spotlight = 3,
    };

    type type = point;
    short num;
    float posX = 0,posY = 0,posZ = 0;
    float dirX = 0,dirY = 0,dirZ = 0;
    float cutoff = 100;

    explicit light(xml_node myNode,short numLight);
    void draw(bool debug);
    void loadLight(xml_node myNode,short numLight);
};


#endif //ENGINE_LIGHT_H
