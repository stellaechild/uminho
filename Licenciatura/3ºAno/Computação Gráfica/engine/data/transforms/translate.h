//
// Created by vicshadow on 29-04-2023.
//

#ifndef ENGINE_TRANSLATE_H
#define ENGINE_TRANSLATE_H

#include "../../pugixml/pugixml.hpp"
#include <GL/glut.h>
#include <list>

using namespace pugi;
using namespace std;

class translate {
public:
    float transX = 0, transY = 0, transZ = 0;

    float time = 0;
    float currTime = 0;
    bool align = false;
    bool line = false;

    float previousY[3] = {0,1,0};

    int numPoints = 0;
    float** points;

    translate();
    void loadTranslate(xml_node myNode);
    void calculateAndApplyTranslate(float elapsedTime,bool debug);
};


#endif //ENGINE_TRANSLATE_H
