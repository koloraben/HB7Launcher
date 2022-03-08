package com.app.hb7launcher.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.hb7launcher.MainActivity;
import com.app.hb7launcher.R;
import com.app.hb7launcher.utils.ColorToast.*;
import com.app.hb7launcher.utils.HttpStatusCodeRange;
import com.app.hb7launcher.utils.HttpStatusCodeRangeUtil;
import com.app.hb7launcher.utils.MyToast;
import com.app.hb7launcher.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Method;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.app.hb7launcher.login.LoginUtility.getIdBox;
import static com.app.hb7launcher.utils.ColorToast.GREEN;
import static com.app.hb7launcher.utils.Utils.getSerialNumber;


public class LoginActivity extends Activity {
    Button loginButton;
    EditText code;
    private static final String TAG = "LoginActivity";
    int counter = 3;

    String checkInternet(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr.getActiveNetworkInfo().isConnected()) {
            Toast.makeText(getApplicationContext(), "connn !", Toast.LENGTH_SHORT).show();
            return "connecté";
        } else {
            Toast.makeText(getApplicationContext(), "noooot !", Toast.LENGTH_SHORT).show();
            return "pas de connexion !";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        code = (EditText) findViewById(R.id.username);
        code.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    try {
                        //MyToast.createNotif(LoginActivity.this, checkInternet(getApplicationContext()), GREEN);
                        authenticate(code.getText().toString());
                        return false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        loginButton = (Button) findViewById(R.id.btn_submit);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    authenticate(code.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
    }

    public void authenticate(String codeValidation) throws IOException {
        String url = getResources().getString(R.string.base_url) + "/api/validation/serial?code=" + codeValidation + "&serial=" + getSerialNumber() + "&macwlan0=" + Utils.getMACAddress("wlan0") + "&maceth0=" + Utils.getMACAddress("eth0");
        Log.d(TAG + " used url ", url);
        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .build();
        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
            ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //this method will be running on UI thread
                pdLoading.setMessage("\tChargement...");
                pdLoading.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    Response response = client.newCall(request).execute();
                    HttpStatusCodeRange range = HttpStatusCodeRangeUtil.getRange(response.code());
                    switch (range) {
                        case SUCCESS_RANGE:
                            handleSuccess();
                            break;
                        case CLIENT_ERROR_RANGE:
                            handleClientError(response);
                            break;
                        case SERVER_ERROR_RANGE:
                            handleServerError();
                            break;
                        case UNKNOWN:
                            handleUnexpectedError(response);
                            break;
                        default:
                            handleUnknownError(response);
                            break;
                    }
                } catch (Exception e) {
                    Log.e("onFailure", e.getMessage());
                }
                return "";
            }

            private void handleUnknownError(Response r) {
                Toast.makeText(getApplicationContext(), "Une erreur inconnue s'est produite ! " + " code erreur =B7-" + r.code(), Toast.LENGTH_LONG).show();

            }

            private void handleUnexpectedError(Response r) {
                Toast.makeText(getApplicationContext(), "Erreur inattendue, désolé nous avons rencontré un problème ! " + " code erreur =B7-" + r.code(), Toast.LENGTH_SHORT).show();
            }

            private void handleServerError() {
                Toast.makeText(getApplicationContext(), "Désolé nous rencontrons des problèmes de serveur temporaire !", Toast.LENGTH_LONG).show();
            }

            private void handleClientError(Response r) {
                if (r.code() == 403) {
                    Log.e("handleClientError", "errur d'authentification::" + r.message());
                    Toast.makeText(getApplicationContext(), "Désolé le code n'est pas bon!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Vérifier votre connexion internet puis réessayez !", Toast.LENGTH_LONG).show();
                }
            }

            private void handleSuccess() {
                Toast.makeText(getApplicationContext(), "Bienvenu !", Toast.LENGTH_LONG).show();
                LoginUtility.saveCode(getBaseContext(), codeValidation);
                LoginUtility.setUserLoggedIn(getBaseContext(), true);
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
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