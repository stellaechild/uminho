//
// Created by vicshadow on 25-02-2023.
//

#ifndef ENGINE_CAMERA_H
#define ENGINE_CAMERA_H


class camera {
public:
    float posX,posY,posZ;
    float lookX,lookY,lookZ;
    float upX,upY,upZ;
    float fov,near,far;

    camera();
    void setPosition(float argPosX, float posY, float posZ);
    void setLookAt(float lookX,float lookY,float lookZ);
    void setUp(float upX,float upY,float upZ);
    void setFov(float fov);
    void setNearFar(float near, float far);
};


#endif //ENGINE_CAMERA_H
