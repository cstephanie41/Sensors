package com.example.androidthings.myproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//package com.voila.voila.voilapicopro;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class SleepingMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeping_mode);

        String username = ((MyVoilaApp) this.getApplication()).getUsername();

        TextView textViewGoodNight = (TextView) findViewById(R.id.textViewGoodNight);
        textViewGoodNight.setText("Good Night "+username+" !");

        /* OTHER WAY TO ANIMATE
        Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        textViewGoodNight.startAnimation(animationOut);
        textViewGoodNight.setVisibility(View.INVISIBLE);

        TextView textClock = (TextView) findViewById(R.id.textClock);
        Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        textClock.startAnimation(animationIn);
        */



        ValueAnimator valueAnimatorGoodNight = ValueAnimator.ofFloat(1f, 0f);
        valueAnimatorGoodNight.setDuration(3000);
        valueAnimatorGoodNight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                TextView textViewGoodNight = (TextView) findViewById(R.id.textViewGoodNight);
                float alpha = (float) animation.getAnimatedValue();
                textViewGoodNight.setAlpha(alpha);
            }
        });
        valueAnimatorGoodNight.start();

        //TEMP CLock
        //TEMP Clock: DEMO PURPOSE
        TextView textViewTempClock = (TextView) findViewById(R.id.textViewTempClock);
        textViewTempClock.setText(((MyVoilaApp) this.getApplication()).tempHour+((int) ((MyVoilaApp) this.getApplication()).tempMin));

        ValueAnimator valueAnimatorClock = ValueAnimator.ofFloat(0f, 1f);
        valueAnimatorClock.setDuration(6000);
        valueAnimatorClock.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                TextView textViewTempClock = (TextView) findViewById(R.id.textViewTempClock);
                float alpha = (float) animation.getAnimatedValue();
                textViewTempClock.setAlpha(alpha);
            }
        });
        valueAnimatorClock.start();

        /*
        ValueAnimator valueAnimatorClock = ValueAnimator.ofFloat(0f, 1f);
        valueAnimatorClock.setDuration(6000);
        valueAnimatorClock.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                TextView textClock = (TextView) findViewById(R.id.textClock);
                float alpha = (float) animation.getAnimatedValue();
                textClock.setAlpha(alpha);
            }
        });
        valueAnimatorClock.start();
        */


    }

    public void Toggle(View view) {
        ((MyVoilaApp) this.getApplication()).setSleepingStatus(0);
        Date currentTime = Calendar.getInstance().getTime();
        ((MyVoilaApp) this.getApplication()).setSleepEndTime(currentTime);
        System.out.println("sleeping : "+((MyVoilaApp) this.getApplication()).getSleepingStatus());
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


        ((MyVoilaApp) this.getApplication()).setQuestion("How did you sleep?");
        ((MyVoilaApp) this.getApplication()).setQuestionExtra("Sleep Duration: "+SleepElapsedHours+" h, "+ SleepElapsedMinutes+" min");

        Intent intentToAskQuestion = new Intent(this, AskQuestion.class);
        startActivity(intentToAskQuestion);

    }


}
