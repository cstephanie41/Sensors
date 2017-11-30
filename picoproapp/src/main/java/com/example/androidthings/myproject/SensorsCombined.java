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
    ArrayList<Float> readings = new ArrayList<Float>();
    int irTriggered = 0; //1 if a hand gesture is recongnized, 0 if not
    long irTriggerTime = 0;
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
    int toggleTriggered =0; // old triggerValue
    int startAcReading = 0; //makes us able to avoid the first reading of the accelerometer value that automatically create a toggle action

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

        lightOff();


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

        // Reading current Time in ms
        currentTime = millis();

        // Reading the App parameters
        int sleeping = getSleepingStatus();
        int isAnsweringQuestion = isAnsweringQuestion();
        int answerSelected = getAnswerSelected();

        //digitalWrite (off, LOW);

        //Get current real time
        timeValue = getCurrentTimeValue();

        // Is the user's hand waved over the IR Sensor ? 1 if yes, O if not
        irTriggered = returnIrTriggered();

        //check if IR sensor has been activated
        //if activated, turn off lights
        if (irTriggered ==1) {
            System.out.println(answerSelected);
            if (sleeping == 0){ // No waving hand is accepted if the user is sleeping
                if (isAnsweringQuestion==1 && answerSelected >0){ //If the device asks a question to the user
                    confirmAnswer();
                    lightOff();
                }
                else if(isAnsweringQuestion==0 && answerSelected ==0){ // If the user wants to pop up a question
                    popQuestion();
                    lightOn(2);
                    /*
                    if (timeValue<=24.00 && timeValue>12.00){
                        System.out.println("afternoon");
                        digitalWrite(off,HIGH);
                        digitalWrite(afternoon,LOW);
                        digitalWrite(evening, LOW);
                    }
                    else if (timeValue<=12.00){
                        digitalWrite(off,HIGH);
                        digitalWrite(morning,LOW);
                    }
                    */
                }
            }
        }



        // read I2C accelerometer
        try {

            xyz = accelerometer.readSample();
            toggleTriggered = isAccelerometerTriggered();

        }catch (IOException e) {
            Log.e("HW3Template","loop",e);
        }


        //if its morning or evening and you toggle the device/accelerometer
        if (toggleTriggered ==1){
            if(sleeping==1){
                lightOn(1);
                ToggleWakeUp();
            }else{
                if (isAnsweringQuestion ==0){
                    lightOn(2);
                    ToggleSleep();
                }
            }
        }
        delay(100);
    }

    //Get input from PIR
    //Take action with LEDS if its evening or afternoon
    @Override
    void digitalEdgeEvent(Gpio pin, boolean value) {
        //println("digitalEdgeEvent"+pin+", "+value);
        PirCurrentTime = millis();
        if(pin==GPIO_128 && value==HIGH) {
            if (PirCurrentTime - PirTriggerTime > 20000) {
                //System.out.println('a');
                System.out.println("change presence detection");
                setPresenceDetected(1);
                System.out.println("digitalEdgeEvent " + value + '\n');
                PirTriggerTime = millis();
                /*
                if (timeValue <= 22.30 && timeValue>12.0) {
                    digitalWrite(afternoon, HIGH);
                } else if (timeValue > 22.30) {
                    digitalWrite(evening, HIGH);
                }
                */
            }
        }
    }

    public double getCurrentTimeValue(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH.mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        //System.out.println(""+dateFormat.format(date));
        String currentTimeString = dateFormat.format(date);
        return Double.parseDouble(currentTimeString);
    }

    public int returnIrTriggered(){
        float f3 = analogRead(A3);
        readings.add(f3);
        double sum = 0;
        double average = 0;
        int readingSize = readings.size();
        if (readingSize <= 5) {
            for (int i = 0; i <= readingSize - 1; i++) {
                sum += readings.get(i);
            }
            average = sum / readingSize;
        } else {
            sum += readings.get(readingSize - 1) + readings.get(readingSize - 2) + readings.get(readingSize - 3);
            average = sum / 3;
        }
        //shortSum = Math.floor(sum * 100) / 100;
        if (Math.floor(average * 100) / 100 >= 0.4 && currentTime - irTriggerTime > 1000){
            irTriggerTime = millis();
            System.out.println("IR seen");
            return 1;
        }else{
            return 0;
        }
    }

    // partOfTheDay : // 1 for morning // 2 for afternoon // 3 for evening
    public void lightOn(int partOfTheDay){
        digitalWrite (off, LOW);
        if (partOfTheDay==1){digitalWrite(afternoon,LOW);digitalWrite(evening,LOW);digitalWrite(morning,HIGH);System.out.println("light on: morning");}
        if (partOfTheDay==2){digitalWrite(morning,LOW);digitalWrite(evening,LOW);digitalWrite(afternoon,HIGH);System.out.println("light on: afternoon");}
        if (partOfTheDay==3){digitalWrite(morning,LOW);digitalWrite(afternoon,LOW);digitalWrite(evening,HIGH);System.out.println("light on: evening");}
    }

    public void lightOff(){
        System.out.println("light off");
        digitalWrite(morning,LOW);
        digitalWrite(afternoon,LOW);
        digitalWrite(evening,LOW);
        digitalWrite (off, HIGH);
    }

    public int isAccelerometerTriggered(){
        //println(UART6,"X: "+xyz[0]+"   Y: "+xyz[1]+"   Z: "+xyz[2]);
        //println("X: "+xyz[0]+"   Y: "+xyz[1]+"   Z: "+xyz[2]);
        int acTriggerValue= 0;
        if (startAcReading==1){
            if (xyz[0]-last[0]>0.2 || last[0]-xyz[0]>0.2 || xyz[1]-last[1]>0.2 || last[1]-xyz[1]>0.2 || xyz[2]-last[2]>0.2 || last[2]-xyz[2]>0.2) {
                if (currentTime - AcTriggerTime > 4000) {
                    System.out.println("Accelerometer Triggered0 "+(xyz[0]-last[0]));
                    last = xyz;
                    AcTriggerTime = millis();
                    //delay(2000);
                    acTriggerValue = 1;
                }
            }
            last = xyz;
        }else{
            startAcReading =1;
            last = xyz;
        }
        return acTriggerValue;
    }

}