package com.ncr.medicalconferencetracker.Fragments.AdminFragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ncr.medicalconferencetracker.Adapters.ConferenceAdapter;
import com.ncr.medicalconferencetracker.CustomObjects.ConferenceObj;
import com.ncr.medicalconferencetracker.CustomObjects.DBHelper;
import com.ncr.medicalconferencetracker.R;

import java.util.ArrayList;


public class ConferenceFragment extends Fragment {

    private Context context;
    private DBHelper db;

    private ConferenceAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton addConferenceFab;

    private ArrayList<ConferenceObj> conferenceObjList = new ArrayList<>();

    public ConferenceFragment() {
        // Required empty public constructor
    }

    public static ConferenceFragment newInstance(Context context) {
        ConferenceFragment fragment = new ConferenceFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DBHelper(context);
        conferenceObjList = db.getConferenceList();
        adapter = new ConferenceAdapter(conferenceObjList, context);


    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRecyclerViewOnSwipe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conference, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addConferenceFab = (FloatingActionButton) view.findViewById(R.id.conference_fragment_fab);

        recyclerView = (RecyclerView) view.findViewById(R.id.conference_fragment_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.conference_fragment_swiperefresh);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshRecyclerViewOnSwipe();
            }
        });

        addConferenceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addConf();
            }
        });

    }

    private void refreshRecyclerViewOnSwipe()
    {
        //adapter = new ConferenceAdapter(db.getConferenceList());
        conferenceObjList.clear();
        conferenceObjList.addAll(db.getConferenceList());
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void addConf()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_add_conference);

        Button addButton = (Button) dialog.findViewById(R.id.dialog_addconference_add_button);
        Button cancelButton = (Button) dialog.findViewById(R.id.dialog_addconference_cancel_button);

        final EditText nameEt = (EditText) dialog.findViewById(R.id.dialog_addconference_nameEt);
        final EditText descriptionEt = (EditText) dialog.findViewById(R.id.dialog_addconference_descriptionEt);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEt.getText().toString();
                String description = descriptionEt.getText().toString();

                if(name.trim().length() == 0)
                {
                    Toast.makeText(context, "Please enter a conference name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    db.insertConference(name, description);
                    refreshRecyclerViewOnSwipe();
                    dialog.dismiss();
                }

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
