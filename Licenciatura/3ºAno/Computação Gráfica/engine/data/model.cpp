//
// Created by vicshadow on 25-02-2023.
//

#include <cstdio>
#include <cstdlib>
#include <stdexcept>
#include "model.h"

model::model(xml_node myNode) {
    string filename = myNode.attribute("file").as_string();
    FILE* file = fopen(filename.c_str(),"r");
    if(!file) throw std::invalid_argument("Model: Model Name '"+filename+"' not found");
    this->name = filename;
    int numFloats = 0;

    fread(&numFloats,sizeof (int), 1,file);
    auto *points = (float*) malloc(sizeof (float) * numFloats);
    auto *normal = (float*) malloc(sizeof (float) * numFloats);
    auto *textCoord = (float*) malloc(sizeof (float) * numFloats);

    fread(points,sizeof (float ), numFloats,file);
    fread(normal,sizeof (float ), numFloats,file);
    fread(textCoord,sizeof (float ), numFloats,file);
    this->numPoints = numFloats/3;

    this->textureFileName = myNode.child("texture").attribute("file").as_string();
    if (!textureFileName.empty()){
        ilGenImages(1, &t);
        ilBindImage(t);
        ilLoadImage((ILstring) textureFileName.c_str());
        tw = ilGetInteger(IL_IMAGE_WIDTH);
        th = ilGetInteger(IL_IMAGE_HEIGHT);
        ilConvertImage(IL_RGBA, IL_UNSIGNED_BYTE);
        texData = ilGetData();

        glGenTextures(1, &texture);

        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_NEAREST_MIPMAP_NEAREST, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tw, th, 0, GL_RGBA, GL_UNSIGNED_BYTE, texData);
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    glGenBuffers(3,buffer);

    glBindBuffer(GL_ARRAY_BUFFER, buffer[0]);
    glBufferData(GL_ARRAY_BUFFER, (long) (sizeof(float) * numFloats), points, GL_STATIC_DRAW);

    glBindBuffer(GL_ARRAY_BUFFER, buffer[1]);
    glBufferData(GL_ARRAY_BUFFER, (long) (sizeof(float) * numFloats), normal, GL_STATIC_DRAW);

    glBindBuffer(GL_ARRAY_BUFFER, buffer[2]);
    glBufferData(GL_ARRAY_BUFFER, (long) (sizeof(float) * numFloats), textCoord, GL_STATIC_DRAW);

    this->color.loadColor(myNode.child("color"));
    free(points);free(normal);free(textCoord);
}

void model::drawModel() const{
    float diffuseP[4],ambientP[4],specularP[4],emissiveP[4];
    this->color.getDiffuse(diffuseP);
    this->color.getAmbient(ambientP);
    this->color.getSpecular(specularP);
    this->color.getEmissive(emissiveP);

    glMaterialfv(GL_FRONT, GL_AMBIENT, ambientP);
    glMaterialfv(GL_FRONT, GL_DIFFUSE, diffuseP);
    glMaterialfv(GL_FRONT, GL_SPECULAR, specularP);
    glMaterialfv(GL_FRONT,GL_EMISSION,emissiveP);
    glMaterialf(GL_FRONT, GL_SHININESS, float(this->color.shininess));
    glPolygonMode(GL_FRONT_AND_BACK, GL_POLYGON);

    glBindTexture(GL_TEXTURE_2D, texture);

    glBindBuffer(GL_ARRAY_BUFFER, buffer[0]);
    glVertexPointer(3, GL_FLOAT, 0, nullptr);

    glBindBuffer(GL_ARRAY_BUFFER, buffer[1]);
    glNormalPointer( GL_FLOAT, 0, nullptr);

    glBindBuffer(GL_ARRAY_BUFFER, buffer[2]);
    glTexCoordPointer(2, GL_FLOAT, 0, 0);

    glDrawArrays(GL_TRIANGLES, 0, this->numPoints);

    glBindTexture(GL_TEXTURE_2D, 0);
}
