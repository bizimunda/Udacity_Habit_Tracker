package com.example.android.udacityhabittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.udacityhabittracker.data.HabitContract;
import com.example.android.udacityhabittracker.data.HabitDbHelper;


public class MainActivity extends ActionBarActivity {

    private HabitDbHelper mDbHelper;
    private TextView displayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //displayView= (TextView) findViewById(R.id.tvDisplay);


        //delete();

        //insert();

        //read();

        //update();

        Cursor cursor=read(3);
        Toast.makeText(this, " "+ cursor.getCount() , Toast.LENGTH_LONG).show();


    }
    //this method let us to insert data into the db
    private void insert(){
        String nameString="swimming";
        int count=3;

        mDbHelper= new HabitDbHelper(this);
        SQLiteDatabase db= mDbHelper.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_NAME, nameString);
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_COUNT, count);

        long newRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);

        if(newRowId==-1){
            Toast.makeText(this, "Error with saving habit ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    //this method let us to read data from the db
    private void read(){
        mDbHelper= new HabitDbHelper(this);
        SQLiteDatabase db=mDbHelper.getReadableDatabase();

        String[] projection ={
                HabitContract.HabitEntry._ID,
                HabitContract.HabitEntry.COLUMN_HABIT_NAME,
                HabitContract.HabitEntry.COLUMN_HABIT_COUNT
        };

        Cursor cursor=db.query(
                HabitContract.HabitEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        try{
            //displayView.setText("Number of rows in this Habit Table are: " + cursor.getCount());

            int idColumnIndex= cursor.getColumnIndex(HabitContract.HabitEntry._ID);
            int nameColumnIndex= cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_HABIT_NAME);
            int countColumnIndex= cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_HABIT_COUNT);

            while (cursor.moveToNext()){
                int currentID=cursor.getInt(idColumnIndex);
                String currentName=cursor.getString(nameColumnIndex);
                int currentCount=cursor.getInt(countColumnIndex);
                Toast.makeText(this, "  " + currentID + currentName + currentCount, Toast.LENGTH_SHORT).show();
            }

        } finally {
            cursor.close();
        }
    }

    //this method let us to delete rows from the db
    private void delete(){
        mDbHelper= new HabitDbHelper(this);
        SQLiteDatabase db= mDbHelper.getWritableDatabase();

        String selection= HabitContract.HabitEntry._ID + " LIKE ?";
        String [] selectionArgs ={"1"};
        db.delete(HabitContract.HabitEntry.TABLE_NAME, selection, selectionArgs);
        Toast.makeText(this, "Id is deleted", Toast.LENGTH_SHORT).show();
        db.close();
    }


    //this method let us to modify data from the db
    private void update(){
        mDbHelper= new HabitDbHelper(this);
        SQLiteDatabase db= mDbHelper.getWritableDatabase();

        String replacedValue="Replace value";

        ContentValues values= new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_NAME, replacedValue);

        String selection= HabitContract.HabitEntry.COLUMN_HABIT_NAME + " LIKE ?";
        String[] selectionArgs={"tracking"};

        int count= db.update(
                HabitContract.HabitEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        Toast.makeText(this, count + ": updated", Toast.LENGTH_SHORT).show();

        db.close();


    }
    //this method let us to read data from db and returns a cursor
    private Cursor read(int recordId){

        mDbHelper= new HabitDbHelper(this);
        SQLiteDatabase db= mDbHelper.getWritableDatabase();
        Cursor record;
        String table = HabitContract.HabitEntry.TABLE_NAME;
        String selection = HabitContract.HabitEntry._ID + " = ? ";
        String[] selectionArgs = new String[]{Integer.toString(recordId)};
        db = mDbHelper.getReadableDatabase();
        try {
            record = db.query(true, table, null, selection, selectionArgs, null, null, null, null);
            record.moveToFirst();
            record.close();
            db.close();
            return record;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return null;
        }
    }


}
