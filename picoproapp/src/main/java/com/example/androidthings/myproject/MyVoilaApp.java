package com.example.androidthings.myproject;

import android.app.Activity;
import android.app.Application;
//package com.voila.voila.voilapicopro;

import android.app.Application;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by daniel on 26/11/2017.
 */

public class MyVoilaApp extends Application {

    private String username="";

    private int presenceDetected  =0; // 0 if no human presence detected, 1 if detected
    //private int steps = 0;
    private int[] steps = {0,0,0,0,0,0,0}; //steps[0] is today, steps[6] is a week ago
    private double[] kms = {0.,0.,0.,0.,0.,0.,0.}; //kms[0] is today, kms[6] is a week ago
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
    private int partOfTheDay =2; //1 morning, 2 afternoon, 3 evening, 4 night


    private List<Pair<String,String>> questionsMorning = Arrays.asList(new Pair<>("How did you sleep last night?","quality"),new Pair<>("How well did you sleep?","quality"),new Pair<>("How would you rate your sleep?","quality"));
    private List<Pair<String,String>> questionsAfternoon = Arrays.asList(new Pair<>("How do you handle stress today?","quality"),new Pair<>("How do you rate your current mood?","quality"));
    private List<Pair<String,String>> questionsEvening = Arrays.asList(new Pair<>("How was your day in general?","quality"),new Pair<>("How much sport did you do today?","quantity_gym"));
    private int indexMorningQuestion = 0;
    private int indexAfternoonQuestion = 0;
    private int indexEveningQuestion = 0;
    private String[] answerFeedback_quality = {"Awesome","Great","Very Good","Good","Bad","Very Bad","Horrible"};
    private String[] answerFeedback_quantity_gym = {"2h+","1h30-2h","1h-1h30","45min-1h","30-45min","15-30min","0min"};
    public int isASpecialQuestion =0; // Morning question or NewQuestion sent by the user
    private int newQuestionIsSent = 0;
    private String newQuestionToAsk = "";

    private static int[] indexLogoWeatherCorrespondance = {R.drawable.weather01d,R.drawable.weather01n,R.drawable.weather02d,R.drawable.weather02n,R.drawable.weather03d,R.drawable.weather03n,R.drawable.weather04d,R.drawable.weather04n,R.drawable.weather09d,R.drawable.weather09n,R.drawable.weather10d,R.drawable.weather10n,R.drawable.weather11d,R.drawable.weather11n,R.drawable.weather13d,R.drawable.weather13n,R.drawable.weather50d,R.drawable.weather50n};

    public int[] getWeatherLogoCorrespondance() {
        return indexLogoWeatherCorrespondance;
    }

    public Activity currentActivity;


    //TEMP DEMO PURPOSE
    public String tempHour="15:";
    public double tempMin=10.;

    //USERNAME
    public String getUsername() {
        return username;
    }

    public void setUsername(String usernameValue) {
        this.username= usernameValue;
    }

    //QUESTIONS
    public void initializeQuestions(){
        Collections.shuffle(questionsMorning);
        Collections.shuffle(questionsAfternoon);
        Collections.shuffle(questionsEvening);
        indexMorningQuestion = 0;
        indexAfternoonQuestion = 0;
        indexEveningQuestion = 0;
    }
    public int getIndexQuestion(int partOfTheDayValue){
        if (partOfTheDayValue==1){
            return indexMorningQuestion;
        }
        else if (partOfTheDayValue==2){
            return indexAfternoonQuestion;
        }
        else if (partOfTheDayValue==3){
            return indexEveningQuestion;
        }
        else {return 0;}
    }

    public String getRandomQuestion(){
        if (partOfTheDay==1){
            return questionsMorning.get(indexMorningQuestion).first;
        }
        else if (partOfTheDay==2){
            return questionsAfternoon.get(indexAfternoonQuestion).first;
        }
        else if (partOfTheDay==3){
            return questionsEvening.get(indexEveningQuestion).first;
        }
        else{
            return "error Get Question";
        }
    }

    public String getQuestionCategory(){ //Quantity(_gym, ...) or Quality
        if (partOfTheDay==1){
            return questionsMorning.get(indexMorningQuestion).second;
        }
        else if (partOfTheDay==2){
            return questionsAfternoon.get(indexAfternoonQuestion).second;
        }
        else if (partOfTheDay==3){
            return questionsEvening.get(indexEveningQuestion).second;
        }
        else{
            return "error Get Question";
        }
    }

    public void updateIndexQuestions(){
        if (partOfTheDay==1){
            indexMorningQuestion+=1;
        }
        else if (partOfTheDay==2){
            indexAfternoonQuestion+=1;
        }
        else if (partOfTheDay==3){
            indexEveningQuestion+=1;
        }
    }
    public int returnSizeQuestionSet(){
        if (partOfTheDay==1){
            return questionsMorning.size();
        }
        else if (partOfTheDay==2){
            return questionsAfternoon.size();
        }
        else if (partOfTheDay==3){
            return questionsEvening.size();
        }else{
            return questionsEvening.size();
        }
    }

    public double getPercentageKnowledge(){
        if (partOfTheDay==1){
            return 100* indexMorningQuestion/ questionsMorning.size();
        }
        else if (partOfTheDay==2){
            return 100* (indexMorningQuestion+indexAfternoonQuestion)/ (questionsMorning.size()+questionsAfternoon.size());
        }
        else if (partOfTheDay==3){
            return 100* (indexMorningQuestion+indexAfternoonQuestion+indexEveningQuestion)/ (questionsMorning.size()+questionsAfternoon.size()+questionsEvening.size());
        }else{
            return 100* (indexMorningQuestion+indexAfternoonQuestion+indexEveningQuestion)/ (questionsMorning.size()+questionsAfternoon.size()+questionsEvening.size());

        }
    }

    public boolean doSomeQuestionRemain(){
        boolean result;
        if (partOfTheDay==1){
            result =  indexMorningQuestion == questionsMorning.size();
        }
        else if (partOfTheDay==2){
            result =  indexAfternoonQuestion == questionsAfternoon.size();
        }
        else if (partOfTheDay==3){
            result =  indexEveningQuestion == questionsEvening.size();
        }else{
            result = true;
        }
        System.out.println("doSomeQuestionRemain: "+ !result);
        return !result;
    }

    public String getAnswerFeedback(String categoryValue){
        System.out.println("morning:"+indexMorningQuestion+"-"+questionsMorning.size());
        System.out.println("afternoon:"+indexAfternoonQuestion+"-"+questionsAfternoon.size());
        System.out.println("evening:"+indexEveningQuestion+"-"+questionsEvening.size());
        if (categoryValue == "quantity_gym"){
            return answerFeedback_quantity_gym[answerSelected-1];
        }else if(categoryValue == "quality"){
            return answerFeedback_quality[answerSelected-1];
        }
        return "error category feedback" ;
    }

    public String getAnswerFeedback(){
        System.out.println("morning:"+indexMorningQuestion+"-"+questionsMorning.size());
        System.out.println("after:"+indexAfternoonQuestion+"-"+questionsAfternoon.size());
        System.out.println("evening:"+indexEveningQuestion+"-"+questionsEvening.size());
        String category = getQuestionCategory();
        if (category == "quantity_gym"){
            return answerFeedback_quantity_gym[answerSelected-1];
        }else if(category == "quality"){
            return answerFeedback_quality[answerSelected-1];
        }
        return "error category feedback" ;
    }

    public int getColorFeedback(){
        if (answerSelected==1){
            return R.color.button1;
        } else if (answerSelected==2){
            return R.color.button2;
        } else if (answerSelected==3){
            return R.color.button3;
        } else if (answerSelected==4){
            return R.color.button4;
        } else if (answerSelected==5){
            return R.color.button5;
        } else if (answerSelected==6){
            return R.color.button6;
        } else if (answerSelected==7){
            return R.color.button7;
        } else {
            return 0;
        }
    }

    public void setNewQuestionIsSent(int value){
        newQuestionIsSent = value;
    }
    public int getNewQuestionIsSent(){
        return newQuestionIsSent;
    }
    public void setNewQuestionToAsk(String valueString){
        newQuestionToAsk = valueString;
    }
    public String getNewQuestionToAsk(){
        return newQuestionToAsk;
    }

    //ASK QUESTION
    // handle the question printed on the ask_question activity
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


    //DEMO PURPOSE: which part of the day it is // For demo purpose
    public int getPartOfTheDay() {
        return partOfTheDay;
    }

    public void setPartOfTheDay(int partOfTheDayValue) {
        this.partOfTheDay = partOfTheDayValue;
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

    //STEPS & KMS
    /*
    public int getSteps() {
        return steps;
    }

    public void setSteps(int stepsValue) {
        this.steps= stepsValue;
    }
    */
    //day is j-day number of steps
    public void setDaySteps(int day,int stepsValue) {
        this.steps[day]= stepsValue;
    }
    public int getDaySteps(int day) {
        return steps[day];
    }
    public int[] getAllSteps() {
        return steps;
    }

    public void setDayKms(int day,double kmsValue) {
        this.kms[day]= kmsValue;
    }
    public double getDayKms(int day) {
        return kms[day];
    }
    public double[] getAllKms() {
        return kms;
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


}
