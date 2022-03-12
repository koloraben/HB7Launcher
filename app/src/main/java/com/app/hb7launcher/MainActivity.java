package com.app.hb7launcher;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.hb7launcher.login.LoginActivity;
import com.app.hb7launcher.login.LoginUtility;
import com.app.hb7launcher.model.FunctionModel;
import com.app.hb7launcher.utils.Utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Settings.Secure.getString(getContentResolver(), "default_input_method").equals("")) {
            startActivity(new Intent("android.settings.INPUT_METHOD_SETTINGS"));
            for (int i = 0; i < 4; i++) {
                Toast.makeText(this, "Merci d'activer le clavier !", Toast.LENGTH_LONG).show();
            }
        } else {
            String code = LoginUtility.getCode(getBaseContext());
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (!LoginUtility.isUserLoggedIn(this) && code == null) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                if (code != null) {
                    try {
                        authenticate(code);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                setContentView(R.layout.main_activity);
            }

        }

    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Settings.Secure.getString(getContentResolver(), "default_input_method").equals("")) {
            startActivity(new Intent("android.settings.INPUT_METHOD_SETTINGS"));
            for (int i = 0; i < 4; i++) {
                Toast.makeText(this, "Merci d'activer le clavier !", Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginUtility.setUserLoggedIn(getBaseContext(), false);
    }

    public void authenticate(String codeValidation) throws IOException {
        String url = getResources().getString(R.string.base_url) + "/api/validation/serial?code=" + codeValidation + "&serial=" + Utils.getSerialNumber() + "&macwlan0=" + Utils.getMACAddress("wlan0") + "&maceth0=" + Utils.getMACAddress("eth0");
        System.out.println(url);
        Log.e(TAG + " URLvalid ", url);
        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .build();
        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
            ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //this method will be running on UI thread
                pdLoading.setMessage("\tChargement...");
                pdLoading.show();
            }

            @Override
            protected void onCancelled() {
                Toast.makeText(getApplicationContext(), "Vous n'êtes pas autorisé à accéder !", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.code() == 404 || response.code() == 401 || response.code() == 403) {
                        LoginUtility.setUserLoggedIn(getBaseContext(), false);
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                        Log.e("onFailure", "fail");
                        cancel(true);
                        return null;
                    } else {
                        Log.e("onFailure", "cccccccccccccc " + response.code());
                    }
                    return response.body().string();
                } catch (Exception e) {
                    Log.e("onFailure", "fffffffffffffffffff");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pdLoading.dismiss();
            }
        };

        asyncTask.execute();
    }

}
