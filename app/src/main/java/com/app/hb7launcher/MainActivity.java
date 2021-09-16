package com.app.hb7launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.hb7launcher.login.LoginActivity;
import com.app.hb7launcher.login.LoginUtility;
import com.app.hb7launcher.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.app.hb7launcher.login.LoginUtility.getIdBox;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String code = LoginUtility.getCode(getBaseContext());

        if (!LoginUtility.isUserLoggedIn(this) && code == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            if (code != null) {
                try {
                    authenticate(code, getBaseContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        setContentView(R.layout.main_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String code = LoginUtility.getCode(getBaseContext());

        if (!LoginUtility.isUserLoggedIn(this) && code == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            if (code != null) {
                try {
                    authenticate(code, getBaseContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginUtility.setUserLoggedIn(getBaseContext(), false);
    }

    public void authenticate(String codeValidation, Context context) throws IOException {
        String url = getResources().getString(R.string.base_url) + "/api/validation/serial?code=" + codeValidation + "&serial=" + Utils.getSerialNumber() + "&macwlan0=" + Utils.getMACAddress("wlan0") + "&maceth0=" + Utils.getMACAddress("eth0");
        String urlBoxValidation = getResources().getString(R.string.base_url) + "/api/validation/serial?idbox=" + getIdBox(context);
        System.out.println(url);
        Log.e(TAG + " URLvalid ", url);
        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .build();
        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        LoginUtility.setUserLoggedIn(getBaseContext(), false);
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                        Log.e("onFailure", "fail");
                        Toast.makeText(getApplicationContext(), "Désolé le code n'est pas bon!", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {

                }
            }
        };

        asyncTask.execute();
    }

}
