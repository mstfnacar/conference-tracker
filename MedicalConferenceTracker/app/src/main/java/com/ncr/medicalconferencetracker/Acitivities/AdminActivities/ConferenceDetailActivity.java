package com.ncr.medicalconferencetracker.Acitivities.AdminActivities;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncr.medicalconferencetracker.CustomObjects.DBHelper;
import com.ncr.medicalconferencetracker.R;

import java.util.ArrayList;

public class ConferenceDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton editButton;
    private LinearLayout editContainer;
    private LinearLayout infoContainer;
    private EditText conferenceNameEt;
    private EditText conferenceDescriptionEt;
    private TextView conferenceNameTv;
    private TextView conferenceDescriptionTv;
    private FloatingActionButton inviteDoctorFab;

    private DBHelper db;
    private Context appContext;

    private String conferenceName;
    private String conferenceDescription;
    private int conferenceId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_detail);

        appContext = this.getApplicationContext();
        db = new DBHelper(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null)
        {
            conferenceName = extras.getString("conference_name");
            conferenceDescription = extras.getString("conference_description");
            conferenceId = extras.getInt("conference_id");
        }

        initUIElements();
        hideEditLayout();
    }


    private void initUIElements()
    {
        toolbar = (Toolbar) findViewById(R.id.conference_detail_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        infoContainer = (LinearLayout) findViewById(R.id.conference_detail_info_container);
        editContainer = (LinearLayout) findViewById(R.id.conference_detail_edit_container);
        editButton = (ImageButton) findViewById(R.id.conference_detail_edit_button);

        conferenceNameEt = (EditText) findViewById(R.id.conference_detail_conference_nameEt);
        conferenceDescriptionEt = (EditText) findViewById(R.id.conference_detail_conference_descriptionEt);
        conferenceNameTv = (TextView) findViewById(R.id.conference_detail_conference_nameTv);
        conferenceDescriptionTv = (TextView) findViewById(R.id.conference_detail_conference_descriptionTv);

        conferenceNameTv.setText(conferenceName);
        conferenceDescriptionTv.setText(conferenceDescription);

        inviteDoctorFab = (FloatingActionButton) findViewById(R.id.conference_detail_fab);
        inviteDoctorFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteDoctor();
            }
        });

    }

    public void editButtonOnClick(View v)
    {
        showEditLayout();
    }


    private void showEditLayout()
    {
        editButton.setVisibility(View.GONE);
        infoContainer.setVisibility(View.GONE);
        editContainer.setVisibility(View.VISIBLE);

        conferenceNameEt.setText(conferenceNameTv.getText().toString());
        conferenceDescriptionEt.setText(conferenceDescriptionTv.getText().toString());

    }

    private void hideEditLayout()
    {
        editButton.setVisibility(View.VISIBLE);
        editContainer.setVisibility(View.GONE);
        infoContainer.setVisibility(View.VISIBLE);
    }

    public void saveConference(View v)
    {
        String newConferenceName = conferenceNameEt.getText().toString();
        String newConferenceDescription = conferenceDescriptionEt.getText().toString();

        if(newConferenceName.trim().length() == 0)
        {
            Toast.makeText(ConferenceDetailActivity.this, "Conference name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        db.updateConference(newConferenceName, newConferenceDescription, conferenceId);

        conferenceNameTv.setText(newConferenceName);
        conferenceDescriptionTv.setText(newConferenceDescription);

        hideEditLayout();
    }

    public void cancelEdit(View v)
    {
        hideEditLayout();
    }

    private void inviteDoctor()
    {
        final ArrayList<String> doctorList = db.getDoctorList();

        if(doctorList == null || doctorList.size() == 0) {
            Toast.makeText(ConferenceDetailActivity.this, "There is no doctor in system yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_invite_doctor);

        Button inviteButton = (Button) dialog.findViewById(R.id.dialog_invitedoctor_invite_button);
        Button cancelButton = (Button) dialog.findViewById(R.id.dialog_invitedoctor_cancel_button);

        final AutoCompleteTextView nameEt = (AutoCompleteTextView) dialog.findViewById(R.id.dialog_invitedoctor_doctor_name_actv);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, doctorList );
        nameEt.setAdapter(adapter);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String doctorName = nameEt.getText().toString();
                if(!db.isDoctorAvailable(doctorName))
                {
                    Toast.makeText(ConferenceDetailActivity.this, "There is no available doctor with this name.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                Toast.makeText(ConferenceDetailActivity.this, "Invited: " + nameEt.getText().toString(), Toast.LENGTH_SHORT).show();

                db.insertInvite(doctorName, conferenceName);

                dialog.dismiss();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
