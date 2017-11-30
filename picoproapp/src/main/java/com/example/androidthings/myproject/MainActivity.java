package com.example.androidthings.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.things.pio.PeripheralManagerService;

import java.util.Calendar;
import java.util.Date;

/**
 * The main Android Things activity.
 * Students should change which application class is loaded below, but otherwise leave this unchanged.
 *
 */

//public class MainActivity extends Activity {
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    /** CHANGE THE RIGHT-HAND SIDE OF THIS LINE TO THE NAME OF YOUR APPLICATION CLASS **/
    private SimplePicoPro myBoardApp = new SensorsCombined();


    /** DON'T CHANGE THE CODE BELOW - PUT YOUR CODE INTO YOUR APPLICATION CLASS **/
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "java.lang.ObjectonCreate");
        setContentView(R.layout.activity_main);

        //DA
        myBoardApp.setActivity(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Log.d(TAG, "Display height in pixels: "+ dm.heightPixels);
        Log.d(TAG, "Display width in pixels: "+ dm.widthPixels);
        Log.d(TAG, "Display density in dpi: "+ dm.densityDpi);


        PeripheralManagerService service = new PeripheralManagerService();
        Log.d(TAG, "Available GPIO: " + service.getGpioList());
        Log.d(TAG, "Available I2C: " + service.getI2cBusList());
        Log.d(TAG, "Available PWM: " + service.getPwmList());
        Log.d(TAG, "Available SPI: " + service.getSpiBusList());
        Log.d(TAG, "Available UART: " + service.getUartDeviceList());

        myBoardApp.setup();
        handler.post(loopRunnable);
        //

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
        System.out.println("HumanPresenceOn called");
        ((MyVoilaApp) this.getApplication()).setPresence(1);
        //int presenceDetected = ((MyVoilaApp) this.getApplication()).getPresence();
        System.out.println("presenceDetected: 1");


        ((MyVoilaApp) this.getApplication()).setQuestion("How was your day?");
        ((MyVoilaApp) this.getApplication()).setQuestionExtra("Question");

        Intent intentToAskQuestion = new Intent(this, AskQuestion.class);
        startActivity(intentToAskQuestion);

    }

    /** When we suppose there is no Human Presence*/
    public void HumanPresenceOff(View view){
        ((MyVoilaApp) this.getApplication()).setPresence(0);
        //int presenceDetected = ((MyVoilaApp) this.getApplication()).getPresence();
        System.out.println("presenceDetected: 0" );
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        myBoardApp.teardown();
        try {
            handler.removeCallbacks(loopRunnable);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, "onDestroy");

    }

    Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                myBoardApp.loop();
                handler.post(this);
            } catch(Exception e) {
                Log.e(TAG,e.getMessage());
            }
        }

    };
}
