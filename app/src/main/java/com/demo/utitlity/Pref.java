package com.demo.utitlity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rahul on 14/9/17.
 */

public class Pref {
    private final static String IsLogin = "isLogin";
    private static SharedPreferences getSharedPreferenceInstanced(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isLogin(Context context) {

        return  getSharedPreferenceInstanced(context).getBoolean(IsLogin, false);
    }
    public static void setLogin(Context context, boolean status) {
        SharedPreferences.Editor editor = getSharedPreferenceInstanced(context).edit();
        editor.putBoolean(IsLogin,status );
        editor.apply();
    }
}
