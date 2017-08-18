package com.nayan.task.cbtask.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nayan.task.cbtask.R;
import com.nayan.task.cbtask.model.SurveyEntity;

import java.util.ArrayList;

/**
 * Created by nayan on 18/8/17.
 */

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.DataObjectHolder> {

    ArrayList<ArrayList<SurveyEntity>> mDataset;
    Activity context;
    LinearLayout.LayoutParams layoutParams;

    public SurveyAdapter(Activity context,ArrayList<ArrayList<SurveyEntity>> survey) {
        this.mDataset = survey;
        this.context = context;
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 5, 0, 5);
    }

    @Override
    public SurveyAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.survey_list_row, parent, false);
        SurveyAdapter.DataObjectHolder dataObjectHolder = new SurveyAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(SurveyAdapter.DataObjectHolder holder, final int position) {

        TextView textView;
        try {

            textView = new TextView(context);
            textView.setLayoutParams(layoutParams);
            textView.setText("Date: " + mDataset.get(position).get(0).getDate());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setTypeface(Typeface.SERIF, Typeface.BOLD);
            textView.setTextSize(15);
            holder.layout.addView(textView);

            for (int i = 0; i < mDataset.get(position).size(); i++) {

                textView = new TextView(context);
                textView.setLayoutParams(layoutParams);
                textView.setText("Q: " + mDataset.get(position).get(i).getQuestion());
                textView.setGravity(Gravity.LEFT);
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setTypeface(Typeface.SERIF, Typeface.BOLD);
                textView.setTextSize(15);
                holder.layout.addView(textView);

                textView = new TextView(context);
                textView.setLayoutParams(layoutParams);
                textView.setText("A: " + mDataset.get(position).get(i).getAnswer());
                textView.setGravity(Gravity.LEFT);
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setTypeface(Typeface.SERIF, Typeface.ITALIC);
                textView.setTextSize(15);
                holder.layout.addView(textView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;

        public DataObjectHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);

        }

    }
}
