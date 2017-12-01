package com.example.androidthings.myproject;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.UUID;

import static java.lang.Math.pow;


public class CustomProfile {
    private static final String TAG = CustomProfile.class.getSimpleName();
    private static int mCounter = 0;

    /* Generate your own with www.uuidgenerator.net */
    public static UUID CUSTOM_SERVICE = UUID.fromString("52b3ca23-6396-4ddc-bb67-9eb1fbba28a7");
    public static UUID WRITE_COUNTER = UUID.fromString("59bd6d7a-09d1-45ec-bfee-17344ad33116");
    public static UUID READ_COUNTER = UUID.fromString("e0c8ec1e-40b3-4794-aa0b-936b83633219");


    /**
     * Return a configured {@link BluetoothGattService} instance for the
     * a custom Service.
     */
    public static BluetoothGattService createCustomService() {
        BluetoothGattService service = new BluetoothGattService(CUSTOM_SERVICE,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        // Input Characteristic
        BluetoothGattCharacteristic inputCharacteristic = new BluetoothGattCharacteristic(READ_COUNTER,
                //Read-only characteristic, supports notifications
                BluetoothGattCharacteristic.PROPERTY_READ,
                BluetoothGattCharacteristic.PERMISSION_READ);

        // Output Characteristic
        BluetoothGattCharacteristic outputCharacteristic = new BluetoothGattCharacteristic(WRITE_COUNTER,
                //write characteristic,
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);

        service.addCharacteristic(inputCharacteristic);
        service.addCharacteristic(outputCharacteristic);

        return service;
    }


    // Is in Byte still !!!!
    public static byte[] getInputValue() {

        byte[] field = new byte[1];
        field[0] = (byte) mCounter;
        mCounter = (mCounter+1) %128;
        Log.i(TAG,"compteur: "+mCounter);
        return field;
    }

    public static void setOutputValue (byte[] value) {
        //handle output here
        System.out.println(value.length);
        //mCounter = value[0];
        mCounter = bitArrayToInt(value);
        Log.i(TAG,"compteur: "+mCounter);
    }

    public static int bitArrayToInt(byte[] value){
        int result = 0;
        for (int i=0;i<value.length;i++){
            result += value[value.length-i-1]*pow(8,i);
        }
        return result;
    }



}
