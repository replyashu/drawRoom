package com.b75f;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by apple on 24/03/17.
 */

public class Controller extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
