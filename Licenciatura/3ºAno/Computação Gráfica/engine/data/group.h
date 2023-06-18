//
// Created by vicshadow on 25-02-2023.
//

#ifndef ENGINE_GROUP_H
#define ENGINE_GROUP_H

#include "../pugixml/pugixml.hpp"
#include "model.h"
#include "transforms/translate.h"
#include "transforms/rotate.h"
#include "transforms/scale.h"
#include <list>

using namespace std;
using namespace pugi;


class group {
public:
    //transform functions
    class translate translate;
    class rotate rotate;
    class scale scale;
    char transformOrder[3] = {'x','x','x'};

    list<model> models;
    list<group> subgroups;

    explicit group(xml_node myNode);

    void loadTransforms(xml_node transformNode);
    void loadModels(xml_node modelsNode);
    void loadSubGroups(xml_node myNode);

    void drawGroup(float elapsedTime,bool debug);
};


#endif //ENGINE_GROUP_H
