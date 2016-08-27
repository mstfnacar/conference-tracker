package com.ncr.medicalconferencetracker.Fragments.DoctorFragments;

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
import com.ncr.medicalconferencetracker.Adapters.InviteAdapter;
import com.ncr.medicalconferencetracker.Adapters.TopicAdapter;
import com.ncr.medicalconferencetracker.CustomObjects.ConferenceObj;
import com.ncr.medicalconferencetracker.CustomObjects.DBHelper;
import com.ncr.medicalconferencetracker.CustomObjects.InviteObj;
import com.ncr.medicalconferencetracker.CustomObjects.TopicObj;
import com.ncr.medicalconferencetracker.R;

import java.util.ArrayList;


public class InviteFragment extends Fragment {

    private Context context;
    private DBHelper db;
    private String doctorName;

    private InviteAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<InviteObj> inviteList = new ArrayList<>();


    public InviteFragment() {
        // Required empty public constructor
    }


    public static InviteFragment newInstance(Context context, String doctorName) {
        InviteFragment fragment = new InviteFragment();
        fragment.context = context;
        fragment.doctorName = doctorName;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DBHelper(context);
        inviteList = db.getInviteList(doctorName);
        adapter = new InviteAdapter(inviteList, context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.invite_fragment_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.invite_fragment_swiperefresh);

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
        inviteList.clear();
        inviteList.addAll(db.getInviteList(doctorName));
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


}
