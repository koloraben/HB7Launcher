package com.app.hb7launcher.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hb7launcher.MainActivity;
import com.app.hb7launcher.R;
import com.app.hb7launcher.utils.HttpStatusCodeRange;
import com.app.hb7launcher.utils.HttpStatusCodeRangeUtil;
import com.app.hb7launcher.utils.Utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.app.hb7launcher.utils.Utils.getSerialNumber;


public class LoginActivity extends Activity {
    Button loginButton;
    EditText code;
    TextView maceth0;
    TextView macwlan0;
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
        maceth0 = (TextView) findViewById(R.id.maceth0);
        macwlan0 = (TextView) findViewById(R.id.macwlan0);
        setMac();
        code.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(code, InputMethodManager.SHOW_IMPLICIT);
        code.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    try {
                        //MyToast.createNotif(LoginActivity.this, checkInternet(getApplicationContext()), GREEN);
                        if (TextUtils.isEmpty((code.getText().toString().trim()))) {
                            Toast.makeText(getApplicationContext(), "Attention, vous n'avez rien saisi !", Toast.LENGTH_LONG).show();
                        }
                        authenticate(code.getText().toString().trim());
                        return false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    requestFocus();
                } else {
                    code.clearFocus();
                }
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
        loginButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // TODO Auto-generated method stub
                if (hasFocus) {
                    loginButton.setBackgroundColor(getResources().getColor(R.color.default_background));
                } else {
                    loginButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }

        });

    }

    @Override
    public void onBackPressed() {
    }

    void setMac() {
        String wlan0 = Utils.getMACAddress("wlan0");
        String eth0 = Utils.getMACAddress("eth0");
        if (!TextUtils.isEmpty(wlan0)) {
            String tx = "WIFI MAC ".concat(wlan0);
            macwlan0.setText(tx);
        } else {
            macwlan0.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(eth0)) {
            String tx = "ETHERNET MAC ".concat(eth0);
            maceth0.setText(tx);
        } else {
            maceth0.setVisibility(View.GONE);
        }
    }

    public void authenticate(String codeValidation) throws IOException {
        String url = getResources().getString(R.string.base_url) + "/api/validation/serial?code=" + codeValidation + "&serial=" + getSerialNumber() + "&macwlan0=" + Utils.getMACAddress("wlan0") + "&maceth0=" + Utils.getMACAddress("eth0");
        Log.d(TAG + " used url ", url);
        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .build();
        AsyncTask<Void, Void, Response> asyncTask = new AsyncTask<Void, Void, Response>() {
            ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pdLoading.setMessage("\tChargement...");
                pdLoading.show();
            }
            @Override
            protected void onCancelled(){
                Toast.makeText(getApplicationContext(), "Désolé nous rencontrons des problèmes de serveur temporaire !", Toast.LENGTH_LONG).show();
                pdLoading.dismiss();
                requestFocus();
            }
            @Override
            protected Response doInBackground(Void... params) {
                Response response = null;
                try {
                    response = client.newCall(request).execute();


                } catch (Exception e) {
                    cancel(true);
                    Log.e("onFailure", e.getMessage());
                    e.printStackTrace();
                }
                return response;
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
                if (r.code() == 403 || r.code() == 401) {
                    Log.e("handleClientError", "Erreur Autorisation ::" + r.message());
                    Toast.makeText(getApplicationContext(), "Désolé le code n'est pas bon!", Toast.LENGTH_LONG).show();
                } else if(r.code() == 404){
                    Toast.makeText(getApplicationContext(), "Cet appareil n'est pas enregistré !", Toast.LENGTH_LONG).show();
                }else {
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

            private void handleStatu(Response response) {
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
            }

            @Override
            protected void onPostExecute(Response s) {
                super.onPostExecute(s);
                handleStatu(s);
                pdLoading.dismiss();
                requestFocus();
            }
        };

        asyncTask.execute();
    }

    public void requestFocus() {
        code.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(code, InputMethodManager.SHOW_IMPLICIT);
    }

}