package com.example.androidthings.myproject;

        import com.google.android.things.pio.Gpio;

public class PIR_test extends SimplePicoPro {
    long PirCurrentTime=0;
    long PirTriggerTime=0;
    //initiallize global variables
    //
    @Override
    public void setup() {
        //set two GPIOs to input
        pinMode(GPIO_128, Gpio.DIRECTION_IN);
        setEdgeTrigger(GPIO_128, Gpio.EDGE_BOTH);
    }

    @Override
    public void loop() {
    }

    @Override
    void digitalEdgeEvent(Gpio pin, boolean value) {
        println("digitalEdgeEvent"+pin+", "+value);
        PirCurrentTime = millis();
        if(pin==GPIO_128 && value==HIGH) {
            if (PirCurrentTime - PirTriggerTime > 10000) {
                printCharacterToScreen('a');
                printStringToScreen("digitalEdgeEvent" + value + '\n');
                PirTriggerTime = millis();
            }

        }
    }

}
