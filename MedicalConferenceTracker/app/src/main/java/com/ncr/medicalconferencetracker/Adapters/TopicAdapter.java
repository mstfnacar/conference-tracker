package com.ncr.medicalconferencetracker.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ncr.medicalconferencetracker.CustomObjects.TopicObj;
import com.ncr.medicalconferencetracker.R;

import java.util.ArrayList;

/**
 * Created by Mustafa on 21.08.2016.
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.DataObjectHolder>{

    private ArrayList<TopicObj> dataSet;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView topicNameTv;
        private TextView topicDescriptionTv;
        private TextView suggestedbyTv;

        public DataObjectHolder(View itemView) {
            super(itemView);

            topicNameTv = (TextView) itemView.findViewById(R.id.row_topic_fragment_name);
            topicDescriptionTv = (TextView) itemView.findViewById(R.id.row_topic_fragment_description);
            suggestedbyTv = (TextView) itemView.findViewById(R.id.row_topic_fragment_owner_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = this.getAdapterPosition();

        }
    }

    public TopicAdapter(ArrayList<TopicObj> dataSet) {

        this.dataSet = dataSet;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType ) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_topic_fragment_list, parent, false);
        DataObjectHolder vh = new DataObjectHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        holder.topicNameTv.setText(dataSet.get(holder.getAdapterPosition()).getName());
        holder.topicDescriptionTv.setText(dataSet.get(holder.getAdapterPosition()).getDescription());
        holder.suggestedbyTv.setText( "Suggested by " + dataSet.get(holder.getAdapterPosition()).getOwnerName());

    }

    public void addItem(TopicObj dataObj, int index) {
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
