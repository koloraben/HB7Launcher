package com.app.hb7launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.app.hb7launcher.login.LoginActivity;
import com.app.hb7launcher.login.LoginUtility;

public class MainActivity  extends Activity {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(!LoginUtility.isUserLoggedIn(this)){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
