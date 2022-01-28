package com.monier.bennetout.ihmclient.configuration.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.monier.bennetout.ihmclient.R;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;
import com.monier.bennetout.ihmclient.configuration.ConfigModel;

import java.lang.reflect.Field;

public class ParamsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.params);

        Field[] fields = ConfigModel.class.getFields();

        for (Field field:fields) {
            try {
                String valueName = field.getName();

                if (field.getType().isAssignableFrom(String.class)) {
                    String strValue = (String) field.get(ConfigManager.model);
                }

                if (field.getType().isAssignableFrom(double.class)) {
                    double doubleValue = field.getDouble(ConfigManager.model);
                }

                if (field.getType().isAssignableFrom(double[].class)) {
                    double[] doubleValue = (double[]) field.get(ConfigManager.model);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
