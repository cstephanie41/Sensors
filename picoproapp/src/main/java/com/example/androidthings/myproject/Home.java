package com.example.androidthings.myproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//package com.voila.voila.voilapicopro;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;


public class Home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();


        // Write the current number of steps in the activity
        TextView textViewSteps = (TextView) findViewById(R.id.textViewSteps);
        int currentSteps = ((MyVoilaApp) this.getApplication()).getSteps();
        textViewSteps.setText(currentSteps+" Steps");

        // Write the current temperature in the activity
        TextView textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        int currentTemperature = ((MyVoilaApp) this.getApplication()).getTemperature();
        textViewTemperature.setText(currentTemperature+"°C");

        //Human presence detection
        int presenceDetected = ((MyVoilaApp) this.getApplication()).getPresence();
        System.out.println("presenceDetected: "+ presenceDetected);

    }

    /** When BLE Sync is made */
    public void BLESync(View view) {
        TextView textViewSteps = (TextView) findViewById(R.id.textViewSteps);
        int newSteps = 1657;
        ((MyVoilaApp) this.getApplication()).setSteps(newSteps);
        textViewSteps.setText(newSteps+" Steps");

        TextView textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        int newTemperature = 31;
        ((MyVoilaApp) this.getApplication()).setTemperature(newTemperature);
        textViewTemperature.setText(newTemperature+"°C");


        Date currentTime = Calendar.getInstance().getTime();
        System.out.println("current time: "+currentTime);
    }

    /** When toggle action is made */
    public void Toggle(View view) {
        int sleeping = ((MyVoilaApp) this.getApplication()).getSleepingStatus();
        if (sleeping == 0){
            ((MyVoilaApp) this.getApplication()).setSleepingStatus(1);
            Date currentTime = Calendar.getInstance().getTime();
            ((MyVoilaApp) this.getApplication()).setSleepStartTime(currentTime);
            System.out.println("sleeping : "+((MyVoilaApp) this.getApplication()).getSleepingStatus());
            System.out.println("toggling time: "+currentTime);

            Intent intentToGoodNight = new Intent(this, SleepingMode.class);
            startActivity(intentToGoodNight);
        }
    }

    /** When hand is waved */
    public void WaveHand(View view) {
        ((MyVoilaApp) this.getApplication()).setQuestion("How was your day?");
        ((MyVoilaApp) this.getApplication()).setQuestionExtra("Question");

        Intent intentToAskQuestion = new Intent(this, AskQuestion.class);
        startActivity(intentToAskQuestion);
    }

    /** When Human Presence is detected */
    public void HumanPresenceOn(View view){
        ((MyVoilaApp) this.getApplication()).setPresence(1);
        int presenceDetected = ((MyVoilaApp) this.getApplication()).getPresence();
        System.out.println("presenceDetected: "+ presenceDetected);

        ((MyVoilaApp) this.getApplication()).setQuestion("How was your day?");
        ((MyVoilaApp) this.getApplication()).setQuestionExtra("Question");

        Intent intentToAskQuestion = new Intent(this, AskQuestion.class);
        startActivity(intentToAskQuestion);
    }

    /** When we suppose there is no Human Presence*/
    public void HumanPresenceOff(View view){
        ((MyVoilaApp) this.getApplication()).setPresence(0);
        int presenceDetected = ((MyVoilaApp) this.getApplication()).getPresence();
        System.out.println("presenceDetected: "+ presenceDetected);
    }


}
