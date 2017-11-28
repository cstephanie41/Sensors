package com.example.androidthings.myproject;

import android.util.Log;

import com.google.android.things.contrib.driver.mma8451q.Mma8451Q;
import com.google.android.things.pio.Gpio;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * HW3 Template
 * Created by bjoern on 9/12/17.
 * Wiring:
 * USB-Serial Cable:
 *   GND to GND on IDD Hat
 *   Orange (Tx) to UART6 RXD on IDD Hat
 *   Yellow (Rx) to UART6 TXD on IDD Hat
 * Accelerometer:
 *   Vin to 3V3 on IDD Hat
 *   GND to GND on IDD Hat
 *   SCL to SCL on IDD Hat
 *   SDA to SDA on IDD Hat
 * Analog sensors:
 *   Middle of voltage divider to Analog A0..A3 on IDD Hat
 */

public class accelerometerTest extends SimplePicoPro {

    Mma8451Q accelerometer;

    float[] xyz = {0.f,0.f,0.f}; //store X,Y,Z acceleration of MMA8451 accelerometer here [units: G]
    float[] last={0.f,0.f,0.f};
    long currenttime=0;
    long triggertime=0;
    public static int triggerValue =0;


    public void setup() {

        // Initialize the serial port for communicating to a PC
        uartInit(UART6,9600);


        // Initialize the Analog-to-Digital converter on the HAT
       // analogInit(); //need to call this first before calling analogRead()

        // Initialize the MMQ8451 Accelerometer
        try {
            accelerometer = new Mma8451Q("I2C1");
            accelerometer.setMode(Mma8451Q.MODE_ACTIVE);
        } catch (IOException e) {
            Log.e("HW3Template","setup",e);
        }
    }

    public void loop() {



        // read I2C accelerometer and print to UART
        try {
            xyz = accelerometer.readSample();
            println(UART6,"X: "+xyz[0]+"   Y: "+xyz[1]+"   Z: "+xyz[2]);
            println("X: "+xyz[0]+"   Y: "+xyz[1]+"   Z: "+xyz[2]);

            currenttime=millis();

            if (xyz[0]-last[0]>0.2 || last[0]-xyz[0]>0.2) {
                last = xyz;
                if (currenttime - triggertime > 4000) {
                    printStringToScreen("Triggered");
                    triggertime = millis();
                    //delay(2000);
                }
            }
            else if (xyz[1]-last[1]>0.2 || last[1]-xyz[1]>0.2) {
                last=xyz;
                if (currenttime - triggertime > 4000) {
                    printStringToScreen("Triggered");
                    triggertime=millis();
                    //delay(2000);
                }
            }
            else if (xyz[2]-last[2]>0.2 || last[2]-xyz[2]>0.2) {
                last=xyz;
                if (currenttime - triggertime > 4000) {
                    printStringToScreen("Triggered");
                    triggertime=millis();
                    //delay(2000);
                }
            }
            else {
                last=xyz;
            }


        }catch (IOException e) {
            Log.e("HW3Template","loop",e);
        }


        delay(100);
        triggerValue=0;
    }
}

