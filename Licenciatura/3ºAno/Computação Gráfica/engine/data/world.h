//
// Created by vicshadow on 25-02-2023.
//

#ifndef ENGINE_WORLD_H
#define ENGINE_WORLD_H

#include "model.h"
#include "group.h"
#include "camera.h"
#include "window.h"
#include "light.h"
#include "../pugixml/pugixml.hpp"

#include <iostream>
#include <list>

using namespace std;
using namespace pugi;

class world {
public:
    window myWindow;
    camera myCamera;
    list<light> lights;
    list<group> groups;
    bool axis = false;

    void loadXML(const string& filename);
    void draw(float elapsedTime,bool debug);

private:
    static void resetMaterials();
    static void drawAxis();
    void loadWindow(xml_node windowNode);
    void loadCamera(xml_node cameraNode);
    void loadLights(xml_node lightsNode);
};

#endif //ENGINE_WORLD_H
