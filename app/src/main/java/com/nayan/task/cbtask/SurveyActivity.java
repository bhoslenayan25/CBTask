package com.nayan.task.cbtask;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nayan.task.cbtask.database.DataBaseAdapter;
import com.nayan.task.cbtask.database.SurveyTable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nayan on 18/8/17.
 */

public class SurveyActivity extends AppCompatActivity {

    private String TAG = SurveyActivity.class.getName();

    private LinearLayout layout;
    Button btnSkip, btnNext;

    JSONArray jsonArray = null;
    LinearLayout.LayoutParams layoutParams = null;

    int VIEW_ID = 0;
    int ID_TEXT = 1;
    int ID_RADIO = 2;
    int ID_DATE = 3;
    int ID_CHKBX = 4;
    int ID_NUMBER = 5;

    int COUNT_RADIO = 0;
    int COUNT_CHKBX = 0;

    int count = 0;

    JSONArray dataArray;

    //regex for mob no
    String mobilePattern = "^[789]\\d{9}$";

    DataBaseAdapter dba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        layout = (LinearLayout) findViewById(R.id.layout);
        btnSkip = (Button) findViewById(R.id.btnSkip);
        btnNext = (Button) findViewById(R.id.btnNext);

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 5, 0, 5);

        dba = new DataBaseAdapter(this);

        dataArray = new JSONArray();

        jsonArray = getQuestions();

        try {
            if (count < jsonArray.length() - 1) {
                if (jsonArray.getJSONObject(count).getString("optional").equalsIgnoreCase("yes")) {
                    btnSkip.setVisibility(View.VISIBLE);
                } else {
                    btnSkip.setVisibility(View.GONE);
                }
                nextQuestion();
            } else if (count == jsonArray.length()) {
                layout.removeAllViews();
                TextView textView = new TextView(SurveyActivity.this);
                textView.setLayoutParams(layoutParams);
                textView.setText("Thank You");
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setTypeface(Typeface.SERIF, Typeface.BOLD);
                textView.setTextSize(15);
                layout.addView(textView);
                btnSkip.setVisibility(View.GONE);
                btnNext.setText("Done");
                count++;
            } else if (count == jsonArray.length() + 1) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (count < jsonArray.length() - 1) {
                        if (jsonArray.getJSONObject(count).getString("optional").equalsIgnoreCase("yes")) {
                            btnSkip.setVisibility(View.VISIBLE);
                        } else {
                            btnSkip.setVisibility(View.GONE);
                        }
                        nextQuestion();
                    } else if (count == jsonArray.length()) {
                        layout.removeAllViews();
                        TextView textView = new TextView(SurveyActivity.this);
                        textView.setLayoutParams(layoutParams);
                        textView.setText("Thank You");
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextColor(Color.parseColor("#000000"));
                        textView.setTypeface(Typeface.SERIF, Typeface.BOLD);
                        textView.setTextSize(15);
                        layout.addView(textView);
                        btnSkip.setVisibility(View.GONE);
                        btnNext.setText("Done");
                        count++;
                    } else if (count == jsonArray.length() + 1) {

                        ContentValues cv;
                        dba.open();
                        Cursor cursor = dba.ourDatabase.query(SurveyTable.DATABASE_TABLE, new String [] {"MAX("+ SurveyTable.KEY_ID+")"}, null, null, null, null, null);
                        int id = 0;
                        if(cursor.getCount() > 0){
                            id = cursor.getInt(0);
                        }
                        cursor.close();
                        for (int i = 0; i < dataArray.length(); i++) {
                            id = id + 1;
                            cv = new ContentValues();
                            cv.put(SurveyTable.KEY_ID,id);
                            cv.put(SurveyTable.KEY_DATE, DateFormat.getDateTimeInstance().format(new Date()));
                            cv.put(SurveyTable.KEY_QUESTION,dataArray.getJSONObject(i).getString("question"));
                            cv.put(SurveyTable.KEY_ANSWER,dataArray.getJSONObject(i).getString("answer"));
                            SurveyTable.insertdata(cv);
                        }
                        dba.close();

                        Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext.setText("Next");
                try {

                    if (jsonArray != null) {
                        //   count = count - 1;
                        String value = "";
                        boolean isValid = false;
                        if (count < jsonArray.length()) {
                            if (jsonArray.getJSONObject(count).getString("optional").equalsIgnoreCase("yes")) {
                                btnSkip.setVisibility(View.VISIBLE);
                            } else {
                                btnSkip.setVisibility(View.GONE);
                            }
                            if (count >= 0) {
                                if (VIEW_ID == ID_TEXT) {
                                    EditText editText = (EditText) findViewById(100 + ID_TEXT);
                                    value = editText.getText().toString().trim();
                                    if (jsonArray.getJSONObject(count).getString("optional").equalsIgnoreCase("no")) {
                                        if (value.isEmpty()) {
                                            isValid = false;
                                            editText.setError("Field should not empty");
                                        } else {
                                            isValid = true;
                                        }
                                    } else {
                                        isValid = true;
                                    }

                                } else if (VIEW_ID == ID_RADIO) {
                                    RadioButton radioButton;
                                    for (int i = 0; i < COUNT_RADIO; i++) {
                                        radioButton = (RadioButton) findViewById(200 + i);
                                        if (jsonArray.getJSONObject(count).getString("optional").equalsIgnoreCase("no")) {
                                            if (radioButton.isChecked()) {
                                                isValid = true;
                                                value = radioButton.getText().toString().trim();
                                                break;
                                            }
                                        } else {
                                            isValid = true;
                                            if (radioButton.isChecked()) {
                                                value = radioButton.getText().toString().trim();
                                                break;
                                            }
                                        }
                                    }

                                    if (!isValid) {
                                        Toast.makeText(SurveyActivity.this, "Please select", Toast.LENGTH_LONG).show();
                                    }


                                } else if (VIEW_ID == ID_DATE) {
                                    TextView textView = (TextView) findViewById(100 + ID_DATE);
                                    value = textView.getText().toString().trim();
                                    if (jsonArray.getJSONObject(count).getString("optional").equalsIgnoreCase("no")) {
                                        if (value.isEmpty()) {
                                            isValid = false;
                                        } else {
                                            isValid = true;
                                        }
                                    } else {
                                        isValid = true;
                                    }

                                    if (!isValid) {
                                        Toast.makeText(SurveyActivity.this, "Please select date", Toast.LENGTH_LONG).show();
                                    }


                                } else if (VIEW_ID == ID_NUMBER) {
                                    EditText editText = (EditText) findViewById(100 + ID_NUMBER);
                                    value = editText.getText().toString().trim();
                                    if (jsonArray.getJSONObject(count).getString("optional").equalsIgnoreCase("no")) {
                                        if (value.isEmpty()) {
                                            editText.setError("Field should not empty");
                                            isValid = false;
                                        } else {
                                            if (value.matches(mobilePattern)) {
                                                isValid = true;
                                            } else {
                                                isValid = false;
                                                editText.setError("Enter valid mobile no");
                                            }
                                        }
                                    } else {
                                        isValid = true;
                                    }

                                } else if (VIEW_ID == ID_CHKBX) {
                                    CheckBox checkBox;
                                    for (int i = 0; i < COUNT_CHKBX; i++) {
                                        checkBox = (CheckBox) findViewById(300 + i);
                                        if (jsonArray.getJSONObject(count).getString("optional").equalsIgnoreCase("no")) {
                                            if (checkBox.isChecked()) {
                                                isValid = true;
                                                value = value + checkBox.getText().toString().trim() + ",";
                                            }
                                        } else {
                                            isValid = true;
                                            if (checkBox.isChecked()) {
                                                value = value + checkBox.getText().toString().trim() + ",";
                                            }
                                        }
                                    }

                                    if (!isValid) {
                                        Toast.makeText(SurveyActivity.this, "Please select values", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            if (isValid) {

                                if (value.length() > 0) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("question", jsonArray.getJSONObject(count).getString("question"));
                                    jsonObject.put("answer", value);
                                    dataArray.put(jsonObject);
                                }

                                if (count == jsonArray.length() - 1) {
                                    layout.removeAllViews();
                                    TextView textView = new TextView(SurveyActivity.this);
                                    textView.setLayoutParams(layoutParams);
                                    textView.setText("Thank You");
                                    textView.setGravity(Gravity.CENTER);
                                    textView.setTextColor(Color.parseColor("#000000"));
                                    textView.setTypeface(Typeface.SERIF, Typeface.BOLD);
                                    textView.setTextSize(15);
                                    layout.addView(textView);
                                    btnSkip.setVisibility(View.GONE);
                                    btnNext.setText("Done");
                                    count++;
                                } else {
                                    count++;
                                    nextQuestion();
                                }
                            }

                        } else if (count == jsonArray.length()) {
                            ContentValues cv;
                            dba.open();
                            Cursor cursor = dba.ourDatabase.query(SurveyTable.DATABASE_TABLE, new String [] {"MAX("+ SurveyTable.KEY_ID+")"}, null, null, null, null, null);
                            int id = 0;
                            if(cursor.getCount() > 0){
                                cursor.moveToFirst();
                                id = cursor.getInt(0);
                            }
                            cursor.close();
                            id = id + 1;
                            for (int i = 0; i < dataArray.length(); i++) {
                                cv = new ContentValues();
                                cv.put(SurveyTable.KEY_ID,id);
                                cv.put(SurveyTable.KEY_DATE, DateFormat.getDateTimeInstance().format(new Date()));
                                cv.put(SurveyTable.KEY_QUESTION,dataArray.getJSONObject(i).getString("question"));
                                cv.put(SurveyTable.KEY_ANSWER,dataArray.getJSONObject(i).getString("answer"));
                                SurveyTable.insertdata(cv);
                            }
                            dba.close();

                            Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void nextQuestion() {

        try {
            layout.removeAllViews();
            TextView textView = new TextView(SurveyActivity.this);
            textView.setLayoutParams(layoutParams);
            textView.setText("Q: " + jsonArray.getJSONObject(count).getString("question"));
            textView.setGravity(Gravity.LEFT);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setTypeface(Typeface.SERIF, Typeface.BOLD);
            textView.setTextSize(15);
            layout.addView(textView);

            if (jsonArray.getJSONObject(count).getString("type").equalsIgnoreCase("text") ||
                    jsonArray.getJSONObject(count).getString("type").equalsIgnoreCase("phone")) {
                VIEW_ID = ID_TEXT;
                final EditText editText = new EditText(SurveyActivity.this);
                editText.setId(100 + ID_TEXT);
                editText.setGravity(Gravity.LEFT);
                editText.setLayoutParams(layoutParams);
                editText.setBackgroundResource(R.drawable.edittextstyle);
                editText.setTypeface(Typeface.SERIF);
                editText.setTextColor(Color.parseColor("#000000"));
                editText.setPadding(3, 3, 3, 3);
                editText.setMinLines(1);
                editText.setMaxLines(1);
                editText.setEnabled(true);

                if (jsonArray.getJSONObject(count).getString("type").equalsIgnoreCase("phone")) {
                    VIEW_ID = ID_NUMBER;
                    editText.setId(100 + ID_NUMBER);
                    editText.setInputType(InputType.TYPE_CLASS_PHONE);
                }

                layout.addView(editText);

            } else if (jsonArray.getJSONObject(count).getString("type").equalsIgnoreCase("radio")) {
                VIEW_ID = ID_RADIO;
                COUNT_RADIO = jsonArray.getJSONObject(count).getJSONArray("values").length();
                RadioGroup radioGroup = new RadioGroup(SurveyActivity.this);
                radioGroup.setLayoutParams(layoutParams);
                radioGroup.setOrientation(RadioGroup.VERTICAL);
                layout.addView(radioGroup);
                RadioButton radioButton;
                for (int i = 0; i < COUNT_RADIO; i++) {
                    radioButton = new RadioButton(SurveyActivity.this);
                    radioButton.setId(200 + i);
                    radioButton.setTag(jsonArray.getJSONObject(count).getJSONArray("values").get(i).toString().trim());
                    radioButton.setTextColor(Color.parseColor("#000000"));
                    radioButton.setLayoutParams(layoutParams);
                    radioButton.setBackgroundResource(android.R.color.transparent);
                    radioButton.setText(jsonArray.getJSONObject(count).getJSONArray("values").get(i).toString().trim());
                    radioButton.setTypeface(Typeface.SERIF);
                    radioGroup.addView(radioButton);
                }
            } else if (jsonArray.getJSONObject(count).getString("type").equalsIgnoreCase("checkbox")) {
                VIEW_ID = ID_CHKBX;
                COUNT_CHKBX = jsonArray.getJSONObject(count).getJSONArray("values").length();
                CheckBox checkBox;
                for (int i = 0; i < COUNT_CHKBX; i++) {
                    checkBox = new CheckBox(SurveyActivity.this);
                    checkBox.setId(300 + i);
                    checkBox.setTag(jsonArray.getJSONObject(count).getJSONArray("values").get(i).toString().trim());
                    checkBox.setTextColor(Color.parseColor("#000000"));
                    checkBox.setChecked(false);
                    checkBox.setLayoutParams(layoutParams);
                    checkBox.setBackgroundResource(android.R.color.transparent);
                    checkBox.setText(jsonArray.getJSONObject(count).getJSONArray("values").get(i).toString().trim());
                    checkBox.setTypeface(Typeface.SERIF);
                    layout.addView(checkBox);
                }
            } else if (jsonArray.getJSONObject(count).getString("type").equalsIgnoreCase("date")) {
                VIEW_ID = ID_DATE;
                final TextView textView1 = new TextView(SurveyActivity.this);
                textView1.setId(100 + ID_DATE);
                textView1.setGravity(Gravity.LEFT);
                textView1.setLayoutParams(layoutParams);
                textView1.setBackgroundResource(R.drawable.edittextstyle);
                textView1.setTypeface(Typeface.SERIF);
                textView1.setTextColor(Color.parseColor("#000000"));
                textView1.setPadding(3, 3, 3, 3);
                textView1.setMinLines(1);
                textView1.setMaxLines(1);
                textView1.setEnabled(true);
                textView1.setHint("Tap to select date");

                final Calendar myCalendar = Calendar.getInstance();

                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        textView1.setText(day + "-" + month + "-" + year);
                    }

                };

                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(SurveyActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                layout.addView(textView1);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean validate(int count) {
        boolean flag = false;

        try {
            if (VIEW_ID == ID_TEXT) {
                EditText editText = (EditText) findViewById(100 + ID_TEXT);
                if (jsonArray.getJSONObject(count).getString("optional").equalsIgnoreCase("no")) {

                }
            } else if (VIEW_ID == ID_RADIO) {

            } else if (VIEW_ID == ID_DATE) {

            } else if (VIEW_ID == ID_NUMBER) {

            } else if (VIEW_ID == ID_CHKBX) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }


    private JSONArray getQuestions() {
        JSONArray jsonArray = null;
        try {
            InputStream is = getAssets().open("questions.json");
            int size = is.available();
            byte buffer[] = new byte[size];
            is.read(buffer);
            is.close();
            String bufferString = new String(buffer);
            jsonArray = (new JSONObject(bufferString)).getJSONArray("questions");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
