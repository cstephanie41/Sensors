package com.example.androidthings.myproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//package com.voila.voila.voilapicopro;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.github.lzyzsd.circleprogress.DonutProgress;

public class AskQuestion extends AppCompatActivity {

    Animation animationButton;

    public int whichWeatherLogo(int indexLogo){
        int[] indexLogoWeatherCorrespondance = ((MyVoilaApp) this.getApplication()).getWeatherLogoCorrespondance();
        return indexLogoWeatherCorrespondance[indexLogo];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        ((MyVoilaApp) this.getApplication()).currentActivity = this;

        // Write the Question
        TextView textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
        String currentQuestion = ((MyVoilaApp) this.getApplication()).getQuestion();
        textViewQuestion.setText(currentQuestion);
        // Write the Question
        TextView textViewQuestionExtra = (TextView) findViewById(R.id.textViewQuestionExtra);
        String currentQuestionExtra = ((MyVoilaApp) this.getApplication()).getQuestionExtra();
        textViewQuestionExtra.setText(currentQuestionExtra);

        // Write the current number of steps in the activity
        TextView textViewSteps = (TextView) findViewById(R.id.textViewSteps);
        int currentSteps = ((MyVoilaApp) this.getApplication()).getDaySteps(0);
        textViewSteps.setText(currentSteps+" Steps");

        // Write the current temperature in the activity
        TextView textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        int currentTemperature = ((MyVoilaApp) this.getApplication()).getTemperature();
        textViewTemperature.setText(currentTemperature+"°C");

        // Display the logo weather
        ImageView imageViewWeather = (ImageView) findViewById(R.id.imageViewWeather);
        int currentIndexLogoWeather = ((MyVoilaApp) this.getApplication()).getLogoWeather(); //Index
        int RDrawableIcon = whichWeatherLogo(currentIndexLogoWeather);
        imageViewWeather.setImageResource(RDrawableIcon);

        //TEMP Put 0 instead
        ((MyVoilaApp) this.getApplication()).setAnswerSelected(0);

        //TEMP Clock: DEMO PURPOSE
        TextView textViewTempClock = (TextView) findViewById(R.id.textViewTempClock);
        textViewTempClock.setText(((MyVoilaApp) this.getApplication()).tempHour+((int) ((MyVoilaApp) this.getApplication()).tempMin));

        animationButton = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

        //DEMO PURPOSE
        int partOfTheDay = ((MyVoilaApp) this.getApplication()).getPartOfTheDay();

        //Progress Bar
        DonutProgress donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        double percentKnowledge = ((MyVoilaApp) this.getApplication()).getPercentageKnowledge();
        donutProgress.setDonut_progress(""+(int) percentKnowledge);

        int answerSelected =  ((MyVoilaApp) this.getApplication()).getAnswerSelected();


        /*
        Button button1 = (Button) findViewById(R.id.button1);
        button1.postDelayed(new Runnable(){
            @Override
            public void run()
            {
                System.out.println(""+getAnswerSelected());
                if (getAnswerSelected()==1){
                    System.out.println("animation button1");
                    button1.startAnimation(animationButton);
                }
            }
        }, 1000);
        */
    }


    public int getAnswerSelected(){
        return ((MyVoilaApp) this.getApplication()).getAnswerSelected();
    }

    /** Selection of the answer */
    public void selectAnswer1(View view) {
        view.startAnimation(animationButton);
        ((MyVoilaApp) this.getApplication()).setAnswerSelected(1);
    }
    public void selectAnswer2(View view) {
        view.startAnimation(animationButton);
        ((MyVoilaApp) this.getApplication()).setAnswerSelected(2);
    }
    public void selectAnswer3(View view) {
        view.startAnimation(animationButton);
        ((MyVoilaApp) this.getApplication()).setAnswerSelected(3);
    }
    public void selectAnswer4(View view) {
        view.startAnimation(animationButton);
        ((MyVoilaApp) this.getApplication()).setAnswerSelected(4);
    }
    public void selectAnswer5(View view) {
        view.startAnimation(animationButton);
        ((MyVoilaApp) this.getApplication()).setAnswerSelected(5);
    }
    public void selectAnswer6(View view) {
        view.startAnimation(animationButton);
        ((MyVoilaApp) this.getApplication()).setAnswerSelected(6);
    }
    public void selectAnswer7(View view) {
        view.startAnimation(animationButton);
        ((MyVoilaApp) this.getApplication()).setAnswerSelected(7);
    }


    //The user waves his/her hand to answer the question after selecting the answer
    public void waveHand(View view) {
        int answerSelected = ((MyVoilaApp) this.getApplication()).getAnswerSelected();
        System.out.println("answerSelected: "+ answerSelected);

        Intent intentToMain = new Intent(this, MainActivity.class);
        startActivity(intentToMain);
    }

}
