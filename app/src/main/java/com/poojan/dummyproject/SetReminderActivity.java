package com.poojan.dummyproject;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by VNurtureTechnologies on 14/03/17.
 */

public class SetReminderActivity extends AppCompatActivity {

    DatePicker datePicker;
    TimePicker timePicker;
    EditText editTextDescription;
    Button setReminderButton;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    String date_time="";

    int month,year,day,hour,mMinute;

    boolean isUpdate;
    DbHelper mHelper;
    SQLiteDatabase dataBase;

    String id,name,dateTime;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        datePicker = (DatePicker) findViewById(R.id.date_picker);
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        editTextDescription = (EditText) findViewById(R.id.edit_text_description);
        setReminderButton = (Button) findViewById(R.id.set_reminder_button);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(SetReminderActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(SetReminderActivity.this, 0, myIntent, 0);

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, mMinute);

        isUpdate=getIntent().getExtras().getBoolean("update");
        if(isUpdate)
        {
            id=getIntent().getExtras().getString("ID");
            name=getIntent().getExtras().getString("name");
            dateTime=getIntent().getExtras().getString("datetime");
            editTextDescription.setText(name);
            datePicker.setSelected(true);

        }

        final SQLiteDatabase db = openOrCreateDatabase("myDB",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS myTable (FirstName VARCHAR,Datetime VARCHAR)");

        datePicker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                    datePicker();
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.VISIBLE);
            }
        });

        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=editTextDescription.getText().toString().trim();
                datePicker.getDisplay();
                if(name.length()>0 && dateTime.length()>0)
                {
                    saveData();
                }else
                {
                    AlertDialog.Builder alertBuilder=new AlertDialog.Builder(SetReminderActivity.this);
                    alertBuilder.setTitle("Invalid Data");
                    alertBuilder.setMessage("Please, Enter valid data");
                    alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    alertBuilder.create().show();
                }
                startAlert();
            }
//                String fname,lname;
//
//                fname = name.getText().toString();
//                lname = date.getText().toString();
//
//                db.execSQL("INSERT INTO myTable VALUES ('"+fname+"','"+lname+"')");
//                name.setText("");
//                date.setText("");
//                saveData();
        });

        mHelper=new DbHelper(this);
    }

    private void startAlert() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Alarm set in " + calendar.getTimeInMillis() + " milliseconds",Toast.LENGTH_LONG).show();
    }

    private void saveData() {
        dataBase=mHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(DbHelper.KEY_FNAME,name);
        values.put(DbHelper.KEY_LNAME,dateTime );

        System.out.println("");
        if(isUpdate)
        {
            //update database with new data
            dataBase.update(DbHelper.TABLE_NAME, values, DbHelper.KEY_ID+"="+id, null);
        }
        else
        {
            //insert data into database
            dataBase.insert(DbHelper.TABLE_NAME, null, values);
        }
        //close database
        dataBase.close();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void datePicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //*************Call Time Picker Here ********************
                        timePicker();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void timePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        hour = hourOfDay;
                        mMinute = minute;

                        datePicker.updateDate(year,hourOfDay,minute);
//                        date = maindate;
//                        setAlarm();
                    }
                }, hour, mMinute, false);
        timePickerDialog.show();
    }
}
