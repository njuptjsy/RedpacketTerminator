package com.tx.njuptjsy.redpacketterminator;

import android.app.Application;

/**
 * Created by Administrator on 2016-02-05.
 */
public class MyApplication extends Application {
    private static volatile boolean running;

    public static void setRunning(boolean isRunning){
        running = isRunning;
    }
    public static boolean getRunning(){
        return running;
    }
}
