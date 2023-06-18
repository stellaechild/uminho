//
// Created by vicshadow on 29-04-2023.
//

#ifndef ENGINE_SCALE_H
#define ENGINE_SCALE_H

#include "../../pugixml/pugixml.hpp"
#include <GL/glut.h>
#include <list>

using namespace pugi;
using namespace std;

class scale {
public:

    float scaleX = 1, scaleY = 1, scaleZ = 1;

    scale();
    void loadScale(xml_node myNode);
    void calculateAndApplyScale();
};


#endif //ENGINE_SCALE_H
