package com.nayan.task.cbtask.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

/**
 * Created by nayan on 18/8/17.
 */

public class SurveyTable {

    static String TAG = SurveyTable.class.getName();

    public static final String DATABASE_TABLE = "surveyList";

    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "sdate";
    public static final String KEY_QUESTION = "que";
    public static final String KEY_ANSWER = "ans";

    static final String CREATE_TABLE = " CREATE TABLE " + DATABASE_TABLE + "("
            + KEY_ID + " INTEGER NOT NULL," + KEY_DATE + " TEXT," + KEY_QUESTION + " TEXT," + KEY_ANSWER + " TEXT);";

    public static Cursor getData() {
        try {
            String sql = "select * from " + DATABASE_TABLE;
            Cursor mCur = DataBaseAdapter.ourDatabase.rawQuery(sql, null);
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public static void clearTable() {

        String sql = "delete from " + DATABASE_TABLE;
        try {
            DataBaseAdapter.ourDatabase.execSQL(sql);
        } catch (Exception e) {
            Log.w(TAG,
                    "Error in Clear table" + e.getStackTrace());
        }
    }

    public static long insertdata(ContentValues contentValues) {
        long result = DataBaseAdapter.ourDatabase.insert(DATABASE_TABLE, null,
                contentValues);
        return result;

    }

}
