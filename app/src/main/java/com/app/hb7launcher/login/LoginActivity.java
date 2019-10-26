package com.app.hb7launcher.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.hb7launcher.MainActivity;
import com.app.hb7launcher.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;


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
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.ACTION_DOWN:
                            code.clearFocus();
                            loginButton.requestFocus();
                            break;
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            return checkLogin();

                        default:
                            break;
                    }
                }
                return false;
            }
        });
         loginButton =(RelativeLayout)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkLogin();
            }
            });

    }

    public class CheckLoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            HttpsURLConnection urlConnection = null;
            BufferedReader reader = null;
            String responseJsonStr = null;

            try {
                // Construct the URL for the get User query
                final String GET_PROFILE_BASE_URL ="https://api.domain.com/user?";
                Uri builtUri = Uri.parse(GET_PROFILE_BASE_URL).buildUpon().build();
                URL url = new URL(builtUri.toString());
                // Create the request to server and open the connection
                urlConnection = (HttpsURLConnection) url.openConnection();
                // Create the SSL connection
                SSLContext sc;
                sc = SSLContext.getInstance("TLS");
                sc.init(null, null, new SecureRandom());
                urlConnection.setSSLSocketFactory(sc.getSocketFactory());
                // Add API credentials
                String user = params[0];
                String password = params[1];
                String userpass = user + ":" + password;
                // Create the Authentication Token
                String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
                // Add the required Headers.
                urlConnection.addRequestProperty("Authorization", basicAuth);
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("accept", "application/json");
                // Method
                urlConnection.setRequestMethod("GET");
                // Connect
                urlConnection.connect();

                int status = urlConnection.getResponseCode();
                String reason = urlConnection.getResponseMessage();

                Log.v("LOGIN", status + reason);

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do here
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                responseJsonStr = buffer.toString();
                //getNameDataFromJson(responseJsonStr);

            } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
                Log.e("LOGIN", "Error", e);

                return false;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            // if we reach here it means we successfully logged in
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            //login(result);
        }
    }
    public boolean checkLogin(){
        boolean valide = false;
        if (code.getText().toString().toUpperCase().equals("ADMIN")) {
            Toast.makeText(getApplicationContext(),
                    "Merci ...",Toast.LENGTH_SHORT).show();
            LoginUtility.saveCode(getBaseContext(), code.getText().toString());
            LoginUtility.setUserLoggedIn(getBaseContext(), true);
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            valide = true;
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Désolé le code n'est pas bon!",Toast.LENGTH_SHORT).show();

        }
        return valide;
    }
}