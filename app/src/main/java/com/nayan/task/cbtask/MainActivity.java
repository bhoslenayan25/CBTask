package com.nayan.task.cbtask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nayan.task.cbtask.database.DataBaseAdapter;
import com.nayan.task.cbtask.model.SurveyEntity;
import com.nayan.task.cbtask.parser.Parser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button addSurvey, btnSurveyList,btnExport;
    TextView tvJson;

    DataBaseAdapter dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addSurvey = (Button) findViewById(R.id.btnAddSurvey);
        btnSurveyList = (Button) findViewById(R.id.btnSurveyList);
        btnExport = (Button) findViewById(R.id.btnExport);
        tvJson = (TextView) findViewById(R.id.json);

        dba = new DataBaseAdapter(this);

        addSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tvJson.setText("");
                    Intent intent = new Intent(MainActivity.this,SurveyActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnSurveyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tvJson.setText("");
                    Intent intent = new Intent(MainActivity.this,SurveyList.class);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dba.open();
                    ArrayList<ArrayList<SurveyEntity>> arrayList = Parser.getSurveyData();
                    dba.close();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<ArrayList<SurveyEntity>>>() {
                    }.getType();
                    String json = gson.toJson(arrayList, type);
                    tvJson.setText(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




    }
}
