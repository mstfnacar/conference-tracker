package com.ncr.medicalconferencetracker.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ncr.medicalconferencetracker.CustomObjects.DBHelper;
import com.ncr.medicalconferencetracker.CustomObjects.InviteObj;
import com.ncr.medicalconferencetracker.CustomObjects.TopicObj;
import com.ncr.medicalconferencetracker.R;

import java.util.ArrayList;

/**
 * Created by Mustafa on 22.08.2016.
 */
public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.DataObjectHolder>{

    private ArrayList<InviteObj> dataSet;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView conferenceNameTv;
        private ImageButton acceptButton;
        private ImageButton rejectButton;

        public DataObjectHolder(View itemView) {
            super(itemView);

            conferenceNameTv = (TextView) itemView.findViewById(R.id.row_invite_fragment_conferencename);
            acceptButton = (ImageButton) itemView.findViewById(R.id.row_invite_accept_button);
            rejectButton = (ImageButton) itemView.findViewById(R.id.row_invite_decline_button);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = this.getAdapterPosition();

        }
    }

    public InviteAdapter(ArrayList<InviteObj> dataSet, Context context) {

        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType ) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_invite_fragment_list, parent, false);
        DataObjectHolder vh = new DataObjectHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {

        holder.conferenceNameTv.setText(dataSet.get(holder.getAdapterPosition()).getConferenceName());

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(context);
                db.deleteInvite(dataSet.get(holder.getAdapterPosition()).getId());
                deleteItem(holder.getAdapterPosition());
            }
        });

    }

    public void addItem(InviteObj dataObj, int index) {
        dataSet.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        dataSet.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {

        return dataSet.size();
    }
}
