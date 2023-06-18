//
// Created by vicshadow on 25-02-2023.
//

#include "window.h"

//Default Values
window::window() {
    this->width = 512;
    this->height = 512;
}

void window::setDims(int argWidth, int argHeight) {
    this->width = argWidth;
    this->height = argHeight;

}
