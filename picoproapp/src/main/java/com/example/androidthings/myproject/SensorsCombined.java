package com.example.androidthings.myproject;


import android.content.Intent;
import android.util.Log;
import com.google.android.things.contrib.driver.mma8451q.Mma8451Q;
import com.google.android.things.pio.Gpio;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;
import java.util.Date;
import java.io.IOException;

public class SensorsCombined extends SimplePicoPro{

    //Initialize values for IR Sensor
    float f3;
    ArrayList<Float> readings = new ArrayList<Float>();
    List<String> listOfStrings = new ArrayList<String>();
    int size = readings.size();
    double sum = 0;
    double sum1 = 0;
    double shortSum = 0;
    double shortSum1 = 0;
    double shortAvg = 0;
    double average = 0;
    long IrTriggerTime = 0;
    Gpio morning = GPIO_39;
    Gpio afternoon = GPIO_37;
    Gpio evening = GPIO_35;
    Gpio off = GPIO_34;
    double timeValue=0;

    //Current time for all sensors
    long currentTime=0;

    //Initialize everything for accelerometer
    Mma8451Q accelerometer;
    float[] xyz = {0.f,0.f,0.f}; //store X,Y,Z acceleration of MMA8451 accelerometer here [units: G]
    float[] last={0.f,0.f,0.f};
    long AcTriggerTime=0;
    public static int triggerValue =0;

    //Initialize for PIR
    long PirCurrentTime=0;
    long PirTriggerTime=0;



    @Override
    public void setup() {
        analogInit(); //need to call this first before calling analogRead()
        pinMode(GPIO_128, Gpio.DIRECTION_IN);
        setEdgeTrigger(GPIO_128, Gpio.EDGE_BOTH);
        pinMode(morning, Gpio.DIRECTION_OUT_INITIALLY_LOW);
        setEdgeTrigger(GPIO_39, Gpio.EDGE_BOTH);
        pinMode(afternoon, Gpio.DIRECTION_OUT_INITIALLY_LOW);
        setEdgeTrigger(GPIO_37, Gpio.EDGE_BOTH);
        pinMode(evening, Gpio.DIRECTION_OUT_INITIALLY_LOW);
        setEdgeTrigger(GPIO_35, Gpio.EDGE_BOTH);
        pinMode(off, Gpio.DIRECTION_OUT_INITIALLY_LOW);
        setEdgeTrigger(GPIO_34, Gpio.EDGE_BOTH);


        // Initialize the MMQ8451 Accelerometer
        try {
            accelerometer = new Mma8451Q("I2C1");
            accelerometer.setMode(Mma8451Q.MODE_ACTIVE);
        } catch (IOException e) {
            Log.e("HW3Template","setup",e);
        }
    }



    @Override
    public void loop() {

        // Reading the App parameters
        int sleeping = getSleepingStatus();

        //initiliaze off light pins to 0
        //digitalWrite(morning,LOW);
        //digitalWrite(afternoon,LOW);
        //digitalWrite(evening,LOW);
        digitalWrite (off, LOW); //?

        //initialize variables for IR average readings
        sum = 0;
        sum1 = 0;
        f3 = analogRead(A3);
        currentTime = millis(); //?
        readings.add(f3);
        listOfStrings.add(Float.toString(f3));

        //Get current real time
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH.mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        //System.out.println(""+dateFormat.format(date));
        String currentTimeString = dateFormat.format(date);
        timeValue = Double.parseDouble(currentTimeString);

        //Average out the past 5 readings from IR sensor
        // SEE AVERAGE LATER
        if (readings.size() <= 5) {
            for (int i = 0; i <= readings.size() - 1; i++) {
                sum += readings.get(i);
            }
            average = sum / readings.size();
        } else {
            sum1 += readings.get(readings.size() - 1) + readings.get(readings.size() - 2) + readings.get(readings.size() - 3);
            average = sum1 / 3;
        }
        shortSum = Math.floor(sum * 100) / 100; //?
        shortSum1 = Math.floor(sum1 * 100) / 100;
        shortAvg = Math.floor(average * 100) / 100;

        // read I2C accelerometer
        try {
            xyz = accelerometer.readSample();
            //println(UART6,"X: "+xyz[0]+"   Y: "+xyz[1]+"   Z: "+xyz[2]);
            //println("X: "+xyz[0]+"   Y: "+xyz[1]+"   Z: "+xyz[2]);

            currentTime=millis(); //?

            if (xyz[0]-last[0]>0.2 || last[0]-xyz[0]>0.2) {
                last=xyz;
                if (currentTime - AcTriggerTime > 4000) {
                    System.out.println("Accelerometer Triggered");
                    last = xyz;
                    AcTriggerTime = millis();
                    //delay(2000);
                    triggerValue = 1;
                }
            }
            else if (xyz[1]-last[1]>0.2 || last[1]-xyz[1]>0.2) {
                last=xyz;
                if (currentTime - AcTriggerTime > 4000) {
                    System.out.println("Accelerometer Triggered");
                    last = xyz;
                    AcTriggerTime=millis();
                    //delay(2000);
                    triggerValue = 1;
                }
            }
            else if (xyz[2]-last[2]>0.2 || last[2]-xyz[2]>0.2) {
                last=xyz;
                if (currentTime - AcTriggerTime > 4000) {
                    System.out.println("Accelerometer Triggered");
                    last = xyz;
                    AcTriggerTime=millis();
                    //delay(2000);
                    triggerValue = 1;
                }
            }
            else {
                last=xyz;
            }
        }catch (IOException e) {
            Log.e("HW3Template","loop",e);
        }

        //check if IR sensor has been activated
        //if activated, turn off lights
        if (shortAvg >= 0.4) {
            if (currentTime - IrTriggerTime > 1000) { //1000 Threshold for IR
                IrTriggerTime = millis();
                System.out.println("IR seen");
                //digitalWrite(morning, HIGH);
                if (timeValue<=24.00 && timeValue>12.00){
                    if (sleeping ==0){
                        System.out.println("afternoon");
                        digitalWrite(off,HIGH);
                        digitalWrite(afternoon,LOW);
                        digitalWrite(evening, LOW);
                        WaveHand();
                    }


                }
                else if (timeValue<=12.00){
                    digitalWrite(off,HIGH);
                    digitalWrite(morning,LOW);
                }
            }
        }

        //if its morning and you toggle the device/accelerometer
        if (triggerValue ==1){
            if(sleeping==1){
                digitalWrite(morning, HIGH);
                //ToggleWakeUp();
            }else{
                digitalWrite(evening, HIGH);
                //ToggleSleep();
            }
        }
        triggerValue=0;
        delay(100);
    }

    //Get input from PIR
    //Take action with LEDS if its evening or afternoon
    @Override
    void digitalEdgeEvent(Gpio pin, boolean value) {
        println("digitalEdgeEvent"+pin+", "+value);
        PirCurrentTime = millis();
        if(pin==GPIO_128 && value==HIGH) {
            if (PirCurrentTime - PirTriggerTime > 20000) {
                //System.out.println('a');
                System.out.println("digitalEdgeEvent " + value + '\n');
                PirTriggerTime = millis();
                if (timeValue <= 22.30 && timeValue>12.0) {
                    digitalWrite(afternoon, HIGH);
                } else if (timeValue > 22.30) {
                    digitalWrite(evening, HIGH);
                }
            }
        }
    }
}