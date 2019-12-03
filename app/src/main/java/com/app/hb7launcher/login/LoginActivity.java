package com.app.hb7launcher.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.app.hb7launcher.MainActivity;
import com.app.hb7launcher.R;
import com.app.hb7launcher.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Method;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginActivity extends Activity  {
    RelativeLayout loginButton;
    EditText code;

    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        code = (EditText) findViewById(R.id.username);
        code.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    try {
                        checkLogin();
                        return false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        loginButton =(RelativeLayout)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    checkLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            });

    }

    public boolean checkLogin() throws IOException {
        boolean valide = false;
        authenticate(code.getText().toString());
        return valide;
    }

    public void authenticate(String codeValidation) throws IOException {
        String url = getResources().getString(R.string.base_url) + "/api/validation/serial?code=" + codeValidation + "&serial=" + getSerialNumber() + "&mac=" + Utils.getMACAddress("eth0");
        Log.e("Utils", Utils.getMACAddress("eth0"));
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
                        Log.e("onFailure", "fail");
                        //  Toast.makeText(getApplicationContext(), "Désolé le code n'est pas bon!", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    //Toast.makeText(getApplicationContext(), "Merci ...", Toast.LENGTH_SHORT).show();
                    LoginUtility.saveCode(getBaseContext(), code.getText().toString());
                    LoginUtility.setUserLoggedIn(getBaseContext(), true);
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
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

    public static String getSerialNumber() {
        String serialNumber;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            serialNumber = (String) get.invoke(c, "gsm.sn1");
            Log.e("gsm.sn1: ", serialNumber);
            if (serialNumber.equals("")) {
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
                Log.e("ril.serialnumber: ", serialNumber);
            }
            if (serialNumber.equals("")) {
                serialNumber = (String) get.invoke(c, "ro.serialno");
                Log.e("ro.serialno: ", serialNumber);
            }
            if (serialNumber.equals("")) {
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
                Log.e("sys.serialnumber: ", serialNumber);
            }
            if (serialNumber.equals("")) {
                serialNumber = Build.SERIAL;
                Log.e("Build.SERIAL: ", serialNumber);
            }

            // If none of the methods above worked
            if (serialNumber.equals(""))
                serialNumber = null;
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }

        return serialNumber;
    }
    @Override
    public void onBackPressed() {
    }
}