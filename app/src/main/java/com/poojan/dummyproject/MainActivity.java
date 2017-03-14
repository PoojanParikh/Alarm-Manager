package com.poojan.dummyproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button addReminder;
    ListView listViewReminder;
    ArrayList<ReminderModel> reminderModelArrayList;
    AlertDialog.Builder builder;
    DbHelper reminderDatabase;
    SQLiteDatabase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        listViewReminder = (ListView) findViewById(R.id.reminder_list_view);
        reminderDatabase = new DbHelper(this);

        addReminder = (Button) findViewById(R.id.add_reminder_button);
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGoToSetReminder = new Intent(MainActivity.this,SetReminderActivity.class);
                intentGoToSetReminder.putExtra("update",false);
                startActivity(intentGoToSetReminder);
            }
        });

        listViewReminder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentListView = new Intent(getApplicationContext(), MainActivity.class);
                intentListView.putExtra("name", reminderModelArrayList.get(i).getReminder());

                intentListView.putExtra("ID", reminderModelArrayList.get(i).getId());
                intentListView.putExtra("update", true);
                startActivity(intentListView);
            }
        });

        listViewReminder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete " + reminderModelArrayList.get(i).getReminder());
                builder.setMessage("Do you want to delete ?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText( getApplicationContext(),
                                reminderModelArrayList.get(i).getReminder()
                                        + " is deleted.", Toast.LENGTH_SHORT).show();

                        dataBase.delete(
                                DbHelper.TABLE_NAME,
                                DbHelper.KEY_ID + "="
                                        + reminderModelArrayList.get(i).getId(), null);
                        displayData();
                        dialog.cancel();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }

    private void displayData() {
        dataBase = reminderDatabase.getWritableDatabase();
        Cursor mCursor = dataBase.rawQuery("SELECT * FROM " + reminderDatabase.TABLE_NAME, null);


        reminderModelArrayList.clear();
        if (mCursor.moveToFirst()) {
            do {
                reminderModelArrayList.get(mCursor.getCount()).getId();
                reminderModelArrayList.get(mCursor.getCount()).getReminder();
//                userId.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_ID)));
//                user_name.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_FNAME)));
//                user_datetime.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_LNAME)));

            } while (mCursor.moveToNext());
        }
        CustomAdapter disadpt = new CustomAdapter(MainActivity.this,reminderModelArrayList);
        listViewReminder.setAdapter(disadpt);
        mCursor.close();
    }
}
