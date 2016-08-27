package com.ncr.medicalconferencetracker.Acitivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.ncr.medicalconferencetracker.Acitivities.AdminActivities.AdminMasterActivity;
import com.ncr.medicalconferencetracker.Acitivities.DoctorActivities.DoctorMasterActivity;
import com.ncr.medicalconferencetracker.CustomObjects.DBHelper;
import com.ncr.medicalconferencetracker.R;

public class LoginActivity extends AppCompatActivity {

    // 0 for admin , 1 for doctor
    private int actorType = 0;

    private LinearLayout loginContainer;
    private LinearLayout registerContainer;
    private ViewAnimator viewAnimator;

    private EditText loginUsernameEt;
    private EditText loginPasswordEt;
    private EditText registerUsernameEt;
    private EditText registerPasswordEt;

    Animation fade_in, fade_out;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DBHelper(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            actorType = extras.getInt("loginType");
        }

        initUIElements();

    }

    private void initUIElements()
    {
        fade_in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

        loginContainer = (LinearLayout) findViewById(R.id.loginact_login_container);
        registerContainer = (LinearLayout) findViewById(R.id.loginact_register_container);
        viewAnimator = (ViewAnimator) findViewById(R.id.loginact_animator);
        viewAnimator.setInAnimation(fade_in);
        viewAnimator.setOutAnimation(fade_out);

        loginUsernameEt = (EditText) findViewById(R.id.loginact_login_username_et);
        loginPasswordEt = (EditText) findViewById(R.id.loginact_login_password_et);
        registerUsernameEt = (EditText) findViewById(R.id.loginact_register_username_et);
        registerPasswordEt = (EditText) findViewById(R.id.loginact_register_password_et);

    }


    public void showLogin(View v)
    {
        registerContainer.setVisibility(View.GONE);
        loginContainer.setVisibility(View.VISIBLE);

        viewAnimator.showNext();
    }

    public void showRegister(View v)
    {
        loginContainer.setVisibility(View.GONE);
        registerContainer.setVisibility(View.VISIBLE);

        viewAnimator.showPrevious();
    }

    public void onLoginClick(View v)
    {
        String username = loginUsernameEt.getText().toString();
        String password = loginPasswordEt.getText().toString();

        boolean checkLogin = db.checkLoginCredentials(username, password, actorType);

        if(checkLogin)
        {
            Toast.makeText(LoginActivity.this, "Login Succesful", Toast.LENGTH_SHORT).show();

            if(actorType == 0)
            {
                // go to admin page
                Intent toAdmin = new Intent(this, AdminMasterActivity.class);
                startActivity(toAdmin);
                finish();
            }
            else if(actorType == 1)
            {
                Intent toDoctor = new Intent(this, DoctorMasterActivity.class);
                toDoctor.putExtra("doctorname", username);
                startActivity(toDoctor);
                finish();
            }

        }
        else
        {
            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

        }

    }

    public void onRegisterClick(View v)
    {
        String username = registerUsernameEt.getText().toString();
        String password = registerPasswordEt.getText().toString();

        db.insertActor(username, password, actorType);

        if(actorType == 0)
        {
            // go to admin page
            Intent toAdmin = new Intent(this, AdminMasterActivity.class);
            startActivity(toAdmin);
            finish();
        }
        else if(actorType == 1)
        {
            Intent toDoctor = new Intent(this, DoctorMasterActivity.class);
            toDoctor.putExtra("doctorname", username);
            startActivity(toDoctor);
            finish();
        }
    }




}
