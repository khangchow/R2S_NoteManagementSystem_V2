package com.r2s.notemanagementsystem.local;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefs {
    private static SharedPreferences instance;

    public static SharedPreferences getInstance(Context context)  {
        if (instance == null)   {
            instance = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
        return instance;
    }
}
