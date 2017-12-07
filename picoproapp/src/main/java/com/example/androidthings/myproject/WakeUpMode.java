package com.example.androidthings.myproject;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by daniel on 06/12/2017.
 */


public class WakeUpMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake_up_mode);

        String username = ((MyVoilaApp) this.getApplication()).getUsername();

        TextView textViewGoodMorning= (TextView) findViewById(R.id.textViewGoodMorning);
        System.out.println("GoodMorning Mode");
        textViewGoodMorning.setText("Good Morning "+username+" !");

        /* OTHER WAY TO ANIMATE
        Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        textViewGoodNight.startAnimation(animationOut);
        textViewGoodNight.setVisibility(View.INVISIBLE);
        */
        Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        textViewGoodMorning.startAnimation(animationIn);

        ((MyVoilaApp) this.getApplication()).setSleepingStatus(0);
        Date currentTime = Calendar.getInstance().getTime();
        ((MyVoilaApp) this.getApplication()).setSleepEndTime(currentTime);
        System.out.println("sleeping : "+0);
        System.out.println("toggling time: "+currentTime);

        Date sleepStart = ((MyVoilaApp) this.getApplication()).getSleepStartTime();
        long sleepDuration =  currentTime.getTime()- sleepStart.getTime() ;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long SleepElapsedHours = sleepDuration / hoursInMilli;
        sleepDuration = sleepDuration % hoursInMilli;
        long SleepElapsedMinutes = sleepDuration / minutesInMilli;
        sleepDuration = sleepDuration % minutesInMilli;
        long SleepElapsedSeconds = sleepDuration / secondsInMilli;
        System.out.println("Sleep Duration: "+ sleepDuration+" ms");
        System.out.println("Sleep Duration: "+ SleepElapsedHours+" h, "+ SleepElapsedMinutes+" min, "+ SleepElapsedSeconds+" s");

        ((MyVoilaApp) this.getApplication()).setIsAnsweringQuestion(0);
        ((MyVoilaApp) this.getApplication()).setQuestion("How did you sleep?");
        ((MyVoilaApp) this.getApplication()).setQuestionExtra("Sleep Duration: "+SleepElapsedHours+" h, "+ SleepElapsedMinutes+" min");


        textViewGoodMorning.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToQuestionMorning();
            }
        }, 4000);

    }

    public void goToQuestionMorning(){
        Intent intentToAskQuestion = new Intent(this, AskQuestion.class);
        this.startActivity(intentToAskQuestion);
    }



}
