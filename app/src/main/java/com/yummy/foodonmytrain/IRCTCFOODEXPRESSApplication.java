package com.yummy.foodonmytrain;

import android.app.Application;
import android.content.Context;

public class IRCTCFOODEXPRESSApplication extends Application {

    private static Context mIRCTCFOODEXPRESSApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mIRCTCFOODEXPRESSApplicationContext=getApplicationContext();
    }

    public static Context getIRCTCFOODEXPRESSApplicationContext(){
        return mIRCTCFOODEXPRESSApplicationContext;
    }

}
