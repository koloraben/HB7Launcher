package com.app.hb7launcher.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.hb7launcher.utils.Utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginUtility {
    public static Boolean isUserLoggedIn(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getBoolean("isUserLoggedIn", false);
    }

    public static void setUserLoggedIn(Context context, Boolean isLoggedIn) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isUserLoggedIn", isLoggedIn);
        editor.commit();
    }

    public static void logout(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isUserLoggedIn", false);
        editor.commit();
    }

    public static void saveCode(Context context, String password) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CODE", password);
        editor.commit();
    }

    public static String getCode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString("CODE", null);
    }

    public static String saveIdBox(Context context) {
        String idBox = Utils.generateIdBox();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String savedidBox = prefs.getString("ID_BOX", null);
        if (savedidBox == null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ID_BOX", idBox);
            editor.commit();
        }
        return idBox;
    }

    public static String getIdBox(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String idBox = prefs.getString("ID_BOX", null);
        return idBox == null?saveIdBox(context):idBox;
    }
}
