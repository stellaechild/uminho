

#include <GL/glew.h>
#include <GL/glut.h>
#include <cmath>
#include <cstring>
#include "data/world.h"
#include "pugixml/pugixml.hpp"

world myWorld;
char fileName[64];

int camBeta = 45, camAlpha = 0, camRadius = 100.0f;
int startX, startY, tracking = 0;
bool freeCamMode = false;

int timebase = 0,fpsTimeBase = 0,timeSpeedFact = 0;
float frame;

bool debug = false;

float toRadians(float degree) {
    return (float) (degree * 0.01745329252); // 2*PI / 360
}

void setNewPosXYZ () {
    float actX = camRadius * cos(toRadians(camBeta)) * sin(toRadians(camAlpha));
    float actY = camRadius * sin(toRadians(camBeta));
    float actZ = camRadius * cos(toRadians(camBeta)) * cos(toRadians(camAlpha));
    myWorld.myCamera.setPosition(actX,actY,actZ);
    myWorld.myCamera.setLookAt(0,0,0);
    myWorld.myCamera.setUp(0,1,0);
}


void updateFPSCounter(){
    frame++;
    int time = glutGet(GLUT_ELAPSED_TIME);
    if (time - fpsTimeBase > 1000) {
        float fps = float(frame) / (float(time - fpsTimeBase) / 1000);
        char windowTitle[128];
        sprintf(windowTitle,"CG 22/23 G30 - Engine - %s - FPS: %.2f - Speed: %.4fx",fileName,fps,pow(2,timeSpeedFact));
        glutSetWindowTitle(windowTitle);
        fpsTimeBase = time;
        frame = 0;
    }
}

void changeSize(int w, int h) {

    // Prevent a divide by zero, when window is too short
    // (you cant make a window with zero width).
    if(h == 0)
        h = 1;

    // compute window's aspect ratio
    float ratio = float(w) / float(h);

    // Set the projection matrix as current
    glMatrixMode(GL_PROJECTION);
    // Load Identity Matrix
    glLoadIdentity();

    // Set the viewport to be the entire window
    glViewport(0, 0, w, h);

    // Set perspective
    gluPerspective(myWorld.myCamera.fov ,ratio, myWorld.myCamera.near ,myWorld.myCamera.far);

    // return to the model view matrix mode
    glMatrixMode(GL_MODELVIEW);
}


void renderScene() {

    // clear buffers
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    // set the camera
    glLoadIdentity();
    gluLookAt(myWorld.myCamera.posX,myWorld.myCamera.posY,myWorld.myCamera.posZ,
              myWorld.myCamera.lookX,myWorld.myCamera.lookY,myWorld.myCamera.lookZ,
              myWorld.myCamera.upX,myWorld.myCamera.upY,myWorld.myCamera.upZ);


    //Calculate elapsed time between frames with Multiplier
    int time = glutGet(GLUT_ELAPSED_TIME);
    float elapsedTime = (float(time - timebase) / 1000) * pow(2,timeSpeedFact);
    timebase = time;

    //Draw World
    myWorld.draw(elapsedTime,debug);
    updateFPSCounter();

    // End of frame
    glutSwapBuffers();
}



// write function to process keyboard events
void keyboardHandler(unsigned char key, int x, int y){
    //if(!freeCamMode) return;
    switch (key) {
        case '+': if (camRadius > 5) camRadius -= 5; break;
        case '-': camRadius += 5; break;
        case '0': if (timeSpeedFact < 6) timeSpeedFact++; break;
        case '9': if (timeSpeedFact > -10) timeSpeedFact--; break;

        default:
            break;
    }

    setNewPosXYZ();
    glutPostRedisplay();
}


void keyboardHandlerSpecial(int key_code, int x, int y){
    if(!freeCamMode && key_code != GLUT_KEY_F4 && key_code != GLUT_KEY_F12) return;

    switch (key_code) {
        case GLUT_KEY_F1: glPolygonMode(GL_FRONT,GL_FILL); break;
        case GLUT_KEY_F2: glPolygonMode(GL_FRONT,GL_LINE); break;
        case GLUT_KEY_F3: glPolygonMode(GL_FRONT,GL_POINT); break;
        case GLUT_KEY_F4: freeCamMode = !freeCamMode; break;
        case GLUT_KEY_F12: debug = !debug; glutPostRedisplay(); return;

        case (GLUT_KEY_UP): camBeta += 5; break;
        case (GLUT_KEY_DOWN): camBeta -= 5; break;
        case (GLUT_KEY_LEFT):  camAlpha -= 10; break;
        case (GLUT_KEY_RIGHT): camAlpha += 10; break;

        default:
            break;
    }

    //Free Cam Boundary Check
    if(camBeta >= 85) camBeta = 85;
    if(camBeta <= -85) camBeta = -85;
    if(camAlpha >= 360) camAlpha -= 360;
    if(camAlpha < 0) camAlpha += 360;

    setNewPosXYZ();
    glutPostRedisplay();
}

void processMouseButtons(int button, int state, int xx, int yy) {

    if(!freeCamMode) return;

    if (state == GLUT_DOWN)  {
        startX = xx;
        startY = yy;
        if (button == GLUT_LEFT_BUTTON)
            tracking = 1;
        else if (button == GLUT_RIGHT_BUTTON)
            tracking = 2;
        else
            tracking = 0;
    }
    else if (state == GLUT_UP) {
        if (tracking == 1) {
            camAlpha += (xx - startX);
            camBeta += (yy - startY);
        }
        else if (tracking == 2) {

            camRadius -= yy - startY;
            if (camRadius < 1)
                camRadius = 1;
        }
        tracking = 0;
    }
}

void processMouseMotion(int xx, int yy) {

    int deltaX, deltaY;
    int alphaAux, betaAux;
    int rAux;

    if(!freeCamMode) return;

    if (!tracking)
        return;

    deltaX = xx - startX;
    deltaY = yy - startY;

    if (tracking == 1) {


        alphaAux = camAlpha + deltaX;
        betaAux = camBeta + deltaY;

        if (betaAux > 85.0)
            betaAux = 85.0;
        else if (betaAux < -85.0)
            betaAux = -85.0;

        rAux = camRadius;
    }
    else if (tracking == 2) {

        alphaAux = camAlpha;
        betaAux = camBeta;
        rAux = camRadius - deltaY;
        if (rAux < 3)
            rAux = 3;
    }
    float actX = rAux * sin(alphaAux * 3.14 / 180.0) * cos(betaAux * 3.14 / 180.0);
    float actY = rAux * 							     sin(betaAux * 3.14 / 180.0);
    float actZ = rAux * cos(alphaAux * 3.14 / 180.0) * cos(betaAux * 3.14 / 180.0);

    myWorld.myCamera.setPosition(actX,actY,actZ);
    myWorld.myCamera.setLookAt(0,0,0);
    myWorld.myCamera.setUp(0,1,0);
}


int main(int argc, char **argv) {

    using namespace pugi;
    using namespace std;

    if(argc != 2) {
        printf("World Configuration File missing\n");
        exit(1);
    }
    strcpy(fileName,argv[1]);


// init GLUT
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DEPTH|GLUT_DOUBLE|GLUT_RGBA);
    glutInitWindowPosition(0,0);
    glutInitWindowSize(myWorld.myWindow.width,myWorld.myWindow.height);

    float ratio = ((float) (myWorld.myWindow.width * 1.0)) / ((float) myWorld.myWindow.height);
    gluPerspective(myWorld.myCamera.fov ,ratio, myWorld.myCamera.near ,myWorld.myCamera.far);
    char windowTitle[128];
    sprintf(windowTitle,"CG 22/23 G30 - Engine - %s - FPS: 0 - Speed: 1x",fileName);
    glutCreateWindow(windowTitle);

// Required callback registry
    glutDisplayFunc(renderScene);
    glutIdleFunc(renderScene);
    glutReshapeFunc(changeSize);

// put here the registration of the keyboard callbacks
    glutKeyboardFunc(keyboardHandler);
    glutSpecialFunc(keyboardHandlerSpecial);
    glutMouseFunc(processMouseButtons);
    glutMotionFunc(processMouseMotion);


// GLEW
    if (glewInit() != GLEW_OK) {
        printf("Glew Failed to load\n");
        return -1;
    }

    ilInit();

    // OpenGL settings
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_CULL_FACE);
    glEnable(GL_LIGHTING);
    glEnable(GL_TEXTURE_2D);
    glEnable(GL_RESCALE_NORMAL);

    glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState(GL_NORMAL_ARRAY);
    glEnableClientState(GL_TEXTURE_COORD_ARRAY);

    //float black[4] = {0.0f, 0.0f, 0.0f, 0.0f};
    float amb[4] = { 1.0f, 1.0f, 1.0f, 1.0f };
    glLightModelfv(GL_LIGHT_MODEL_AMBIENT, amb);
    //glLightModelfv(GL_LIGHT_MODEL_AMBIENT, black);

// Load World
    myWorld.loadXML(fileName);
    glutReshapeWindow(myWorld.myWindow.width,myWorld.myWindow.height);

// enter GLUT's main cycle
    glutMainLoop();

    return 1;
}
