package com.example.androidthings.myproject;

import android.app.Application;
//package com.voila.voila.voilapicopro;

import android.app.Application;

import java.util.Date;

/**
 * Created by daniel on 26/11/2017.
 */

public class MyVoilaApp extends Application {

    private int presenceDetected  =0; // 0 if no human presence detected, 1 if detected
    private int steps = 0;
    private int temperature = 0;
    private int answerSelected = 0; // 0 if no question is asked, 1-7: according to the answer selected
    private int sleeping = 0; //0 if not sleeping, 1 if the user is sleeping
    private Date sleepStart,sleepEnd;
    private String question = "";
    private String questionExtra = "";

    //HUMAN PRESENCE
    public int getPresence() {
        return presenceDetected;
    }

    public void setPresence(int presenceValue) {
        this.presenceDetected = presenceValue;
    }

    //STEPS
    public int getSteps() {
        return steps;
    }

    public void setSteps(int stepsValue) {
        this.steps= stepsValue;
    }

    // TEMPERATURE
    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int TemperatureValue) {
        this.temperature= TemperatureValue;
    }


    // ANSWER QUESTION
    public int getAnswerSelected() {
        return answerSelected;
    }

    public void setAnswerSelected(int answerValue) {
        this.answerSelected= answerValue;
    }

    // SLEEPING
    public int getSleepingStatus() {
        return sleeping;
    }

    public void setSleepingStatus(int sleepingvalue) {
        this.sleeping= sleepingvalue;
    }

    public Date getSleepStartTime() {
        return sleepStart;
    }

    public void setSleepStartTime(Date sleepStartValue) {
        this.sleepStart= sleepStartValue;
    }
    public Date getSleepEndTime() {
        return sleepEnd;
    }

    public void setSleepEndTime(Date sleepEndValue) {
        this.sleepEnd= sleepEndValue;
    }

    //ASK QUESTION
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String questionValue) {
        this.question= questionValue;
    }

    public String getQuestionExtra() {
        return questionExtra;
    }

    public void setQuestionExtra(String questionExtraValue) {
        this.questionExtra= questionExtraValue;
    }
}
