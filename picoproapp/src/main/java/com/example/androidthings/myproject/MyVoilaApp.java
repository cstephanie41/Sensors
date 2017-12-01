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
    private int logoWeather = 0;
    private int answerSelected = 0; // 0 if no question is asked, 1-7: according to the answer selected
    private int isAnsweringQuestion =0;
    private int sleeping = 0; //0 if not sleeping, 1 if the user is sleeping
    private Date sleepStart,sleepEnd;
    private String question = "";
    private String questionExtra = "";
    private int bluetoothActivated = 0;
    private int sensorsActivated = 0; // We launch the sensors reading only once (at the launch of the device)

    private static int[] indexLogoWeatherCorrespondance = {R.drawable.weather01d,R.drawable.weather01n,R.drawable.weather02d,R.drawable.weather02n,R.drawable.weather03d,R.drawable.weather03n,R.drawable.weather04d,R.drawable.weather04n,R.drawable.weather09d,R.drawable.weather09n,R.drawable.weather10d,R.drawable.weather10n,R.drawable.weather11d,R.drawable.weather11n,R.drawable.weather13d,R.drawable.weather13n,R.drawable.weather50d,R.drawable.weather50n};

    public int[] getWeatherLogoCorrespondance() {
        return indexLogoWeatherCorrespondance;
    }

    //Sensors ACTIVATED
    public int getSensorsStatus() {
        return sensorsActivated;
    }

    public void setSensorsStatus(int sensorsValue) {
        this.sensorsActivated = sensorsValue;
    }

    //BLUETOOTH ACTIVATED
    public int getBluetoothStatus() {
        return bluetoothActivated;
    }

    public void setBluetoothStatus(int bluetoothValue) {
        this.bluetoothActivated = bluetoothValue;
    }

    //HUMAN PRESENCE
    public int getPresence() {
        return presenceDetected;
    }

    public void setPresence(int presenceValue) {
        this.presenceDetected = presenceValue;
    }

    //THE USER IS ANSWERING A QUESTION
    public int getIsAnsweringQuestion() {
        return isAnsweringQuestion;
    }

    public void setIsAnsweringQuestion(int isAnsweringQuestionValue) {
        this.isAnsweringQuestion = isAnsweringQuestionValue;
    }

    //STEPS
    public int getSteps() {
        return steps;
    }

    public void setSteps(int stepsValue) {
        this.steps= stepsValue;
    }

    // LOGO WEATHER
    public int getLogoWeather() {
        return logoWeather;
    }

    public void setLogoWeather(int LogoWeatherValue) {
        this.logoWeather= LogoWeatherValue;
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
