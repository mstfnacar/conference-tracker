package com.ncr.medicalconferencetracker.Acitivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ncr.medicalconferencetracker.CustomObjects.DBHelper;
import com.ncr.medicalconferencetracker.R;

public class StartupActivity extends AppCompatActivity {

    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        db = new DBHelper(this);
    }




    public void loginOnDoctor(View v)
    {
        Intent toLogin = new Intent(this, LoginActivity.class);
        toLogin.putExtra("loginType", 1);
        startActivity(toLogin);
        finish();
    }

    public void loginOnAdmin(View v)
    {
        Intent toSignup = new Intent(this, LoginActivity.class);
        toSignup.putExtra("loginType", 0);
        startActivity(toSignup);
        finish();
    }


}
