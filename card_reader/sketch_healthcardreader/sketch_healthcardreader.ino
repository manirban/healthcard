#include "FPS_GT511C3.h"
#include <SPI.h>
#include <FreeStack.h>
#include <MinimumSerial.h>
#include <SdFat.h>
#include <SdFatConfig.h>


//Declare variables
FPS_GT511C3 fps(0, 1);
SoftwareSerial gtSerial(0, 1);
boolean readSD = false, writeSD = false, fingerScan = false, soh = true;
String DBNAME;
long fileLength = 0;
int ledB = 4, ledR = 3;
SdFat SD;
File myFile;
int chipSelect = 5;
int lastbuf, iterator;


void setup() {
  pinMode(ledB, OUTPUT);
  pinMode(ledR, OUTPUT);
  digitalWrite(ledR, HIGH);
  Serial.begin(115200);
  while (!Serial) {
    SysCall::yield();
  }
  fps.UseSerialDebug = false; // so you can see the messages in the serial debug screen
  fps.Open();
  fps.ChangeBaudRate(9600);
  delay(100);
  fps.SetLED(false); // turn on the LED inside the fps
  delay(1000);
}

void loop() {
 if (fingerScan) {
    if (gtSerial.available() > 0) {
      byte fingerBytes[52200]; // size:52122
      int count = gtSerial.readBytes(fingerBytes, 52200);
      Serial.write(fingerBytes, count);
      fps.SetLED(false);
      fps.ChangeBaudRate(9600);
      fingerScan = false;
      memset(fingerBytes, 0, sizeof(fingerBytes));
      Serial.flush();
      digitalWrite(ledB, LOW);
    }
  }
  else {
    if (Serial.available() > 0) {   
      String command = Serial.readString();
      String header = command.substring(0, 2);
      if (header.equals("RF")) { // Request for read file from card
        DBNAME = command.substring(2, 17);
        doFileRead();
      }
      else if (header.equals("WF")) { // Request for write file into card "RW123456789ABC.db98765432";
        DBNAME = command.substring(2, 17);
        String dblength = command.substring(17);
        fileLength = dblength.toInt();
        doFileWrite();
      }
      else if (header.equals("FP")) { // Request for fingerprint scan
        Serial.flush();
        doFingerScan();
      } else {}
    }
  }
}


void doFingerScan() {
  digitalWrite(ledB, HIGH);
  fps.ChangeBaudRate(57600);
  gtSerial.begin(57600);// Start serial comms for scanner
  fps.SetLED(true);
  boolean stat = true;
  do {
    delay(100);
    if (fps.IsPressFinger()) {
      stat = false;
      fingerScan = true;
      fps.CaptureFinger(true);
      fps.GetImage();
    }
  } while (stat);
}
void doFileWrite() {
  Serial.flush();
  if (!SD.begin(chipSelect, SPI_FULL_SPEED)) {
    SD.initErrorHalt();
    //Serial.println("initialization failed!");
    return;
  }
  myFile = SD.open(DBNAME, O_RDWR | O_CREAT);
  if (myFile) {
    //writeSD = true;
    digitalWrite(ledB, HIGH);
    boolean stat = true;
    int bufsize =  4096;
    lastbuf =  fileLength % bufsize;
    iterator = fileLength / bufsize;
    //byte rawBuf[bufsize];
    while (stat) {
      if (Serial.available() > 0) {      

        for (int i = 0; i <= iterator; i++) {
          if (i < iterator) {
            byte rawBuf[bufsize];
            int count = Serial.readBytes((char*)rawBuf, bufsize);
            myFile.write(rawBuf, count);
          } else {
            if(lastbuf > 0){
            byte tempbuf[lastbuf];
            int count = Serial.readBytes((char*)tempbuf, lastbuf);
            myFile.write(tempbuf, count);}
            myFile.close();
            digitalWrite(ledB, LOW);
            stat = false;
          }
        }

      }
    }  
    Serial.flush();
  }
}
void doFileRead() {
  Serial.flush();
  if (!SD.begin(chipSelect, SPI_FULL_SPEED)) {
    SD.initErrorHalt();
    //Serial.println("initialization failed!");
    return;
  }
 // Serial.println("initialization done."+DBNAME);
  myFile = SD.open(DBNAME, O_READ);
 // if (myFile) {
    //readSD = true;
    fileLength = myFile.fileSize();
    String s1 = "DBFILE:";
    String s2 = s1 + fileLength;
    Serial.println(s2);    
    Serial.flush();
    delay(1000);
    int bufsize = 4096;
    byte readbuf[bufsize];
    while (myFile.available()) {
      digitalWrite(ledB, HIGH);
      int nr = myFile.read(readbuf, bufsize);
      Serial.write(readbuf, nr);
    }
    myFile.close();
    memset(readbuf, 0, sizeof(readbuf));
    //readSD = false;
    digitalWrite(ledB, LOW);
    Serial.flush();
 // }

}

