#ifndef ENGINE_MODEL_H
#define ENGINE_MODEL_H


#include <GL/glew.h>
#include <GL/glut.h>
#include <IL/il.h>
#include <string>
#include "../pugixml/pugixml.hpp"
#include "color.h"

using namespace std;
using namespace pugi;

class model {
public:
    string name;

    string textureFileName = "";
    unsigned int t, tw, th;
    unsigned char *texData;
    unsigned int texture;

    class color color;

    int numPoints;
    GLuint buffer[3]{};

    explicit model(xml_node myNode);

    void drawModel() const;
};


#endif //ENGINE_MODEL_H
