/*
  ExtendOSC.h - utility for use with OSC (see opensoundcountrol.org) and Extendo
  Created by Joshua Shinavier, 2014
  Released into the public domain.
*/

#ifndef ExtendOSC_h
#define ExtendOSC_h

#include <OSCMessage.h>
#include "Arduino.h"

class ExtendOSC
{
  public:
    ExtendOSC(const char* prefix);

    void beginSerial();

    int receiveOSC(class OSCMessage &messageIn);

    void sendOSC(class OSCMessage &messageOut);
    void sendInfo(const char *msg, ...);
    void sendError(const char *msg, ...);
    
  private:
    const char* _addressPrefix;
    char _addressBuffer[32];
    char _messageBuffer[128];
};

#endif // ExtendOSC_h