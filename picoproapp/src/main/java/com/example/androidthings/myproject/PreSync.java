package com.example.androidthings.myproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//package com.voila.voila.voilapicopro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import android.os.Bundle;


public class PreSync extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sync);

        // Set global variables
        //// Human Presence
        ((MyVoilaApp) this.getApplication()).setPresence(0);
    }

    /** When BLE Sync is made */
    public void PreBLESync(View view) {

        ((MyVoilaApp) this.getApplication()).setDaySteps(0,1343);
        ((MyVoilaApp) this.getApplication()).setTemperature(27);

        Intent intentToMain = new Intent(this, MainActivity.class);
        startActivity(intentToMain);
    }
}
