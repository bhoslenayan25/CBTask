package com.nayan.task.cbtask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nayan.task.cbtask.adapter.SurveyAdapter;
import com.nayan.task.cbtask.database.DataBaseAdapter;
import com.nayan.task.cbtask.model.SurveyEntity;
import com.nayan.task.cbtask.parser.Parser;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by nayan on 18/8/17.
 */

public class SurveyList extends AppCompatActivity {

    protected RecyclerView.Adapter mAdapter;
    public static RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    LinearLayout.LayoutParams layoutParams = null;

    private TextView noData;

    DataBaseAdapter dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_master);
        noData = (TextView) findViewById(R.id.noData);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 5, 0, 5);

        dba = new DataBaseAdapter(this);

        noData.setVisibility(View.GONE);
        try {
            dba.open();
            ArrayList<ArrayList<SurveyEntity>> survey = Parser.getSurveyData();
            dba.close();
            if(survey != null){
                noData.setVisibility(View.GONE);
                Collections.reverse(survey);
                mAdapter = new SurveyAdapter(SurveyList.this,survey);
                mRecyclerView.setAdapter(mAdapter);
            }else {
                noData.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SurveyList.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
