package com.nayan.task.cbtask.parser;

import android.database.Cursor;

import com.nayan.task.cbtask.database.SurveyTable;
import com.nayan.task.cbtask.model.SurveyEntity;

import java.util.ArrayList;

/**
 * Created by nayan on 18/8/17.
 */

public class Parser {

    public static ArrayList<ArrayList<SurveyEntity>> getSurveyData(){

        ArrayList<SurveyEntity> arraySurvey = new ArrayList<SurveyEntity>();
        ArrayList<ArrayList<SurveyEntity>> survey = new ArrayList<ArrayList<SurveyEntity>>();

        SurveyEntity surveyEntity;
        Cursor cursor = SurveyTable.getData();
        if (cursor.getCount() > 1) {
            int id = 1;
            while (cursor.moveToNext()) {
                if (id == cursor.getInt(0)) {
                    surveyEntity = new SurveyEntity();
                    surveyEntity.setId("" + id);
                    surveyEntity.setDate(cursor.getString(1));
                    surveyEntity.setQuestion(cursor.getString(2));
                    surveyEntity.setAnswer(cursor.getString(3));
                    arraySurvey.add(surveyEntity);

                } else {
                    survey.add(arraySurvey);
                    id = cursor.getInt(0);

                    arraySurvey = new ArrayList<SurveyEntity>();
                    surveyEntity = new SurveyEntity();
                    surveyEntity.setId("" + id);
                    surveyEntity.setDate(cursor.getString(1));
                    surveyEntity.setQuestion(cursor.getString(2));
                    surveyEntity.setAnswer(cursor.getString(3));
                    arraySurvey.add(surveyEntity);
                }
            }
            survey.add(arraySurvey);
            cursor.close();

        } else {
            survey = null;
        }
        return survey;
    }

}
