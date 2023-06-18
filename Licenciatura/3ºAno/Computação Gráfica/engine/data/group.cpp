//
// Created by vicshadow on 25-02-2023.
//

#include <cstring>
#include "group.h"


group::group(xml_node myNode){
    this->loadTransforms(myNode.child("transform"));
    this->loadModels(myNode.child("models"));
    this->loadSubGroups(myNode);
}

void group::loadTransforms(xml_node transformNode) {

    int currOrder = 0;
    for(xml_node node = transformNode.first_child(); node; node = node.next_sibling()){

        if(currOrder >= 3) break;

        //Translate
        if (!strcmp(node.name(),"translate")){
            this->translate.loadTranslate(node);
            this->transformOrder[currOrder] = 't';
            currOrder++;
        }

        //Rotate
        else if(!strcmp(node.name(),"rotate")){
            this->rotate.loadRotate(node);
            this->transformOrder[currOrder] = 'r';
            currOrder++;
        }

        //Scale
        else if(!strcmp(node.name(),"scale")){
            this->scale.loadScale(node);
            this->transformOrder[currOrder] = 's';
            currOrder++;
        }

    }
}

void group::loadModels(xml_node modelsNode){
    for(xml_node node = modelsNode.child("model"); node; node = node.next_sibling("model")){
        this->models.emplace_back(node);
    }
}

void group::loadSubGroups(xml_node myNode){
    for(xml_node node = myNode.child("group"); node; node = node.next_sibling("group")){
        this->subgroups.emplace_back(node);
    }
}

void group::drawGroup(float elapsedTime, bool debug) {
    glPushMatrix();

    for(char c : this->transformOrder){
        if (c == 't') this->translate.calculateAndApplyTranslate(elapsedTime,debug);
        else if (c == 'r') this->rotate.calculateAndApplyRotate(elapsedTime);
        else if (c == 's') this->scale.calculateAndApplyScale();
        else break;
    }

    for (auto & model : models){
        model.drawModel();
    }

    for (auto & subgroup : subgroups){
        subgroup.drawGroup(elapsedTime,debug);
    }

    glPopMatrix();
}