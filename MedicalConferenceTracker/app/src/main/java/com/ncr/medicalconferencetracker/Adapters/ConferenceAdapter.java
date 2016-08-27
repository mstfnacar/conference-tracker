package com.ncr.medicalconferencetracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ncr.medicalconferencetracker.Acitivities.AdminActivities.ConferenceDetailActivity;
import com.ncr.medicalconferencetracker.CustomObjects.ConferenceObj;
import com.ncr.medicalconferencetracker.CustomObjects.DBHelper;
import com.ncr.medicalconferencetracker.R;

import java.util.ArrayList;

/**
 * Created by Mustafa on 21.08.2016.
 */
public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceAdapter.DataObjectHolder> {

    private static ArrayList<ConferenceObj> dataSet;
    private static Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView conferenceNameTv;
        private TextView conferenceDescriptionTv;
        private ImageButton removeButton;

        public DataObjectHolder(View itemView) {
            super(itemView);

            conferenceNameTv = (TextView) itemView.findViewById(R.id.row_conference_fragment_name);
            conferenceDescriptionTv = (TextView) itemView.findViewById(R.id.row_conference_fragment_description);
            removeButton = (ImageButton) itemView.findViewById(R.id.row_conference_remove_button);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = this.getAdapterPosition();

            Intent toConferenceDetail = new Intent(context, ConferenceDetailActivity.class);
            int id = dataSet.get(pos).getId();
            String name = dataSet.get(pos).getName();
            String description = dataSet.get(pos).getDescription();

            toConferenceDetail.putExtra("conference_id", id);
            toConferenceDetail.putExtra("conference_name", name);
            toConferenceDetail.putExtra("conference_description", description);

            v.getContext().startActivity(toConferenceDetail);




        }
    }

    public ConferenceAdapter(ArrayList<ConferenceObj> dataSet, Context context) {

        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType ) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conference_fragment_list, parent, false);
        DataObjectHolder vh = new DataObjectHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {

        holder.conferenceNameTv.setText(dataSet.get(holder.getAdapterPosition()).getName());
        holder.conferenceDescriptionTv.setText(dataSet.get(holder.getAdapterPosition()).getDescription());
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper db = new DBHelper(context);
                db.deleteConference(dataSet.get(holder.getAdapterPosition()).getId());
                deleteItem(holder.getAdapterPosition());
            }
        });
    }

    public void addItem(ConferenceObj dataObj, int index) {
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
