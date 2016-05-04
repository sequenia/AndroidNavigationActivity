package com.navigationactivity;

import android.app.Application;
import android.content.Context;

/**
 * Приложение
 * Created by chybakut2004 on 28.03.16.
 */
public class MainApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
