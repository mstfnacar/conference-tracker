package com.ncr.medicalconferencetracker.Fragments.AdminFragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ncr.medicalconferencetracker.Adapters.ConferenceAdapter;
import com.ncr.medicalconferencetracker.Adapters.TopicAdapter;
import com.ncr.medicalconferencetracker.CustomObjects.ConferenceObj;
import com.ncr.medicalconferencetracker.CustomObjects.DBHelper;
import com.ncr.medicalconferencetracker.CustomObjects.TopicObj;
import com.ncr.medicalconferencetracker.R;

import java.util.ArrayList;


public class TopicFragment extends Fragment {

    private Context context;
    private DBHelper db;
    private int actorType;
    private String doctorName;

    private FloatingActionButton addTopicFab;
    private TopicAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<TopicObj> topicList = new ArrayList<>();


    public TopicFragment() {
        // Required empty public constructor
    }


    public static TopicFragment newInstance(Context context, int actorType, String doctorName) {
        TopicFragment fragment = new TopicFragment();
        fragment.context = context;
        fragment.actorType = actorType;
        fragment.doctorName = doctorName;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DBHelper(context);
        topicList = db.getTopicList();
        adapter = new TopicAdapter(topicList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addTopicFab = (FloatingActionButton) view.findViewById(R.id.topic_fragment_fab);
        if(actorType == 0)
            addTopicFab.setVisibility(View.GONE);
        else if(actorType == 1)
            addTopicFab.setVisibility(View.VISIBLE);

        addTopicFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTopic();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.topic_fragment_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.topic_fragment_swiperefresh);

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

    }

    private void refreshRecyclerViewOnSwipe()
    {
        topicList.clear();
        topicList.addAll(db.getTopicList());
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void addTopic()
    {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_add_topic);

        Button addButton = (Button) dialog.findViewById(R.id.dialog_addtopic_add_button);
        Button cancelButton = (Button) dialog.findViewById(R.id.dialog_addtopic_cancel_button);

        final EditText nameEt = (EditText) dialog.findViewById(R.id.dialog_addtopic_nameEt);
        final EditText descriptionEt = (EditText) dialog.findViewById(R.id.dialog_addtopic_descriptionEt);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEt.getText().toString();
                String description = descriptionEt.getText().toString();

                if(name.trim().length() == 0)
                {
                    Toast.makeText(context, "Please enter a topic name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    boolean result = db.insertTopic(name, description, doctorName);
                    Log.d("DEBUGGGG", "onClick: " + result);
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
