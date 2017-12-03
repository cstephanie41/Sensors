package com.example.androidthings.myproject;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;

import com.google.android.things.pio.PeripheralManagerService;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.pow;

/**
 * The main Android Things activity.
 * Students should change which application class is loaded below, but otherwise leave this unchanged.
 *
 */

//public class MainActivity extends Activity {
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private SimplePicoPro myBoardApp = new SensorsCombined();

    /* Bluetooth API */
    private BluetoothManager mBluetoothManager;
    private BluetoothGattServer mBluetoothGattServer;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;

    public int whichWeatherLogo(int indexLogo){
        int[] indexLogoWeatherCorrespondance = ((MyVoilaApp) this.getApplication()).getWeatherLogoCorrespondance();
        return indexLogoWeatherCorrespondance[indexLogo];
    }


    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "java.lang.ObjectonCreate");
        setContentView(R.layout.activity_main);


        int sensorsActivated = ((MyVoilaApp) this.getApplication()).getSensorsStatus();
        int bluetoothActivated = ((MyVoilaApp) this.getApplication()).getBluetoothStatus();

        //DEMO PURPOSE
        int partOfTheDay = ((MyVoilaApp) this.getApplication()).getPartOfTheDay();

        if(sensorsActivated==0){
            myBoardApp.setActivity(this);
            myBoardApp.setup();
            handler.post(loopRunnable);
            ((MyVoilaApp) this.getApplication()).setSensorsStatus(1);
        }

        if(bluetoothActivated==0){
            // Bluetooth initialization
            mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();
            // We can't continue without proper Bluetooth support
            if (!checkBluetoothSupport(bluetoothAdapter)) {
                finish();
            }

            bluetoothAdapter.setName("Voila");

            // Register for system Bluetooth events
            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBluetoothReceiver, filter);
            if (!bluetoothAdapter.isEnabled()) {
                Log.d(TAG, "Bluetooth is currently disabled...enabling");
                bluetoothAdapter.enable();
            } else {
                Log.d(TAG, "Bluetooth enabled...starting services");
                startAdvertising();
                startServer();
                ((MyVoilaApp) this.getApplication()).setBluetoothStatus(1);
            }
        }

        //LAYOUT
        // Write the current number of steps in the activity
        TextView textViewSteps = (TextView) findViewById(R.id.textViewSteps);
        int currentSteps = ((MyVoilaApp) this.getApplication()).getSteps();
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


    }


    /** When BLE Sync is made
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
     */

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


    // BLUETOOTH FUNCTIONS
    /**
     * Verify the level of Bluetooth support provided by the hardware.
     * @param bluetoothAdapter System {@link BluetoothAdapter}.
     * @return true if Bluetooth is properly supported, false otherwise.
     */
    private boolean checkBluetoothSupport(BluetoothAdapter bluetoothAdapter) {

        if (bluetoothAdapter == null) {
            Log.w(TAG, "Bluetooth is not supported");
            return false;
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.w(TAG, "Bluetooth LE is not supported");
            return false;
        }

        return true;
    }

    /**
     * Listens for Bluetooth adapter events to enable/disable
     * advertising and server functionality.
     */
    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);

            switch (state) {
                case BluetoothAdapter.STATE_ON:
                    startAdvertising();
                    startServer();
                    break;
                case BluetoothAdapter.STATE_OFF:
                    stopServer();
                    stopAdvertising();
                    break;
                default:
                    // Do nothing
            }

        }
    };

    /**
     * Begin advertising over Bluetooth that this device is connectable
     * and supports the Current Time Service.
     */
    private void startAdvertising() {
        BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();

        mBluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();

        if (mBluetoothLeAdvertiser == null) {
            Log.w(TAG, "Failed to create advertiser");
            return;
        }

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .build();

        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(false)
                .addServiceUuid(new ParcelUuid(CustomProfile.CUSTOM_SERVICE)) /** IDD: ADD YOUR CUSTOM SERVICE UUID HERE **/
                .build();

        mBluetoothLeAdvertiser
                .startAdvertising(settings, data, mAdvertiseCallback);
    }

    /**
     * Stop Bluetooth advertisements.
     */
    private void stopAdvertising() {
        if (mBluetoothLeAdvertiser == null) return;

        mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
    }

    /**
     * Initialize the GATT server instance with the services/characteristics
     * from the Time Profile.
     */
    private void startServer() {
        mBluetoothGattServer = mBluetoothManager.openGattServer(this, mGattServerCallback);
        if (mBluetoothGattServer == null) {
            Log.w(TAG, "Unable to create GATT server");
            return;
        }

        /** IDD: ADD YOUR CUSTOM SERVICE HERE **/
        mBluetoothGattServer.addService(CustomProfile.createCustomService());


    }

    /**
     * Shut down the GATT server.
     */
    private void stopServer() {
        if (mBluetoothGattServer == null) return;

        mBluetoothGattServer.close();
    }

    /**
     * Callback to receive information about the advertisement process.
     */
    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            Log.i(TAG, "LE Advertise Started.");
        }

        @Override
        public void onStartFailure(int errorCode) {
            Log.w(TAG, "LE Advertise Failed: "+errorCode);
        }
    };



    public static int bitArrayToInt(byte[] value){
        int result = 0;
        // we start to look at the digits starting index 1 (because index 0 is dataType)
        for (int i=1;i<value.length;i++){
            result += value[value.length-i]*pow(8,i-1);
        }
        return result;
    }
    public void reloadMainActivity(){
        Intent intentToMain = new Intent(this, MainActivity.class);
        startActivity(intentToMain);
    }

    public void updateNumberOfSteps(int newNumber){
        ((MyVoilaApp) this.getApplication()).setSteps(newNumber);
        //textViewNewSteps.setText(newNumber+" Steps");
    }
    public void updateTemperature(int newTemp){
        ((MyVoilaApp) this.getApplication()).setTemperature(newTemp);
    }
    public void updateLogoWeather(int newLogo){
        ((MyVoilaApp) this.getApplication()).setLogoWeather(newLogo);
    }
    public void updatePartOfTheDay(int newPart){
        ((MyVoilaApp) this.getApplication()).setPartOfTheDay(newPart);
    }

    /**
     * Callback to handle incoming requests to the GATT server.
     * All read/write requests for characteristics are handled here.
     */
    private BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {

        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "BluetoothDevice CONNECTED: " + device);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "BluetoothDevice DISCONNECTED: " + device);
            }
        }

        /**
         * IDD: CHECK FOR ALL WRITABLE CHARACTERISTICS YOU DEFINED HERE
         **/
        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            if (CustomProfile.WRITE_COUNTER.equals(characteristic.getUuid())) {
                Log.i(TAG, "Write Output Characteristic");

                if(value[0]==1){ // steps info received
                    int numberOfStepsUpdated = bitArrayToInt(value);
                    System.out.println("new steps BLE: "+numberOfStepsUpdated);
                    updateNumberOfSteps(numberOfStepsUpdated);
                }else if (value[0]==2){ //temperature info received
                    int temperatureUpdated = bitArrayToInt(value);
                    System.out.println("new Temperature BLE: "+temperatureUpdated);
                    updateTemperature(temperatureUpdated);
                }else if (value[0]==3){ //logo weather info received
                    int logoWeatherUpdated = bitArrayToInt(value);
                    System.out.println("new Logo Weather BLE: "+logoWeatherUpdated);
                    updateLogoWeather(logoWeatherUpdated);
                }else if (value[0]==4){ //logo weather info received //DEMO PURPOSE
                    int partOfTheDayUpdated = bitArrayToInt(value);
                    System.out.println("new part of the day BLE: "+partOfTheDayUpdated);
                    updatePartOfTheDay(partOfTheDayUpdated);
                }
                reloadMainActivity();

                // use it later for stockage
                //CustomProfile.setOutputValue(value);

                if (responseNeeded) {
                    mBluetoothGattServer.sendResponse(device,
                            requestId,
                            BluetoothGatt.GATT_SUCCESS,
                            0,
                            value);
                }

            }

        }

        /**
         * IDD: CHECK FOR ALL READABLE CHARACTERISTICS YOU DEFINED HERE
         **/
        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset,
                                                BluetoothGattCharacteristic characteristic) {

            if (CustomProfile.READ_COUNTER.equals(characteristic.getUuid())) {
                Log.i(TAG, "Read Input Characteristic: ");
                mBluetoothGattServer.sendResponse(device,
                        requestId,
                        BluetoothGatt.GATT_SUCCESS,
                        0,
                        CustomProfile.getInputValue());
            } else {
                // Invalid characteristic
                Log.w(TAG, "Invalid Characteristic Read: " + characteristic.getUuid());
                mBluetoothGattServer.sendResponse(device,
                        requestId,
                        BluetoothGatt.GATT_FAILURE,
                        0,
                        null);
            }
        }
    };

    public void displayDeviceInformation(){
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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //DA
        BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();
        if (bluetoothAdapter.isEnabled()) {
            stopServer();
            stopAdvertising();
        }

        unregisterReceiver(mBluetoothReceiver);

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
