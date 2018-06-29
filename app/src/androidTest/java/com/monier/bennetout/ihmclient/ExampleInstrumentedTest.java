package com.monier.bennetout.ihmclient;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        ConfigManager.initConfig(appContext);

        //*************FLECHE***************************************************
        ConfigManager.model.FLECHE_CALLIB_ZERO = -30;
        ConfigManager.model.FLECHE_CALLIB_CENT = 30;

        double result = MainActivity.calculPosFleche(30);
        assertEquals(45, result, 0);

        result = MainActivity.calculPosFleche(-30);
        assertEquals(-45, result, 0);

        result = MainActivity.calculPosFleche(0);
        assertEquals(0, result, 0);

        //*************LEVAGE***************************************************
        ConfigManager.model.LEVAGE_CALLIB_ZERO = 10;
        ConfigManager.model.LEVAGE_CALLIB_CENT = 40;
        result = MainActivity.calculPosLevage(10);
        assertEquals(RemorqueDesigner.BORNE_MIN, result, 0);

        result = MainActivity.calculPosLevage(40);
        assertEquals(RemorqueDesigner.BORNE_MAX, result, 0);

        //*************BENNE***************************************************
        ConfigManager.model.PORTE_CALLIB_ZERO = 5;
        ConfigManager.model.PORTE_CALLIB_CENT = 80;
        result = MainActivity.calculPosPorte(5);
        assertEquals(RemorqueDesigner.BORNE_MIN_BENNE, result, 0);

        result = MainActivity.calculPosPorte(80);
        assertEquals(RemorqueDesigner.BORNE_MAX_BENNE, result, 0.1);

    }
}
