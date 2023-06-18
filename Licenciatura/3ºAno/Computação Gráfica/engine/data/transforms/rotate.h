//
// Created by vicshadow on 29-04-2023.
//

#ifndef ENGINE_ROTATE_H
#define ENGINE_ROTATE_H

#include "../../pugixml/pugixml.hpp"
#include <GL/glut.h>
#include <list>

using namespace pugi;
using namespace std;

class rotate {
public:
    float rotX = 0,rotY = 0, rotZ = 0;
    float rotAngle = 0,time = 0;

    float angPerTime = 0;

    rotate();
    void loadRotate(xml_node myNode);

    void calculateAndApplyRotate(float timeElapsed);
};


#endif //ENGINE_ROTATE_H
