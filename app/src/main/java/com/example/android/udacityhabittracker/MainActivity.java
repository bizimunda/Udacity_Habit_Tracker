package com.example.android.udacityhabittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
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
    }

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

        } finally {
            cursor.close();
        }
    }

    private void delete(){
        mDbHelper= new HabitDbHelper(this);
        SQLiteDatabase db= mDbHelper.getWritableDatabase();

        String selection= HabitContract.HabitEntry._ID + " LIKE ?";
        String [] selectionArgs ={"1"};
        db.delete(HabitContract.HabitEntry.TABLE_NAME, selection, selectionArgs);
        Toast.makeText(this, "Id is deleted", Toast.LENGTH_SHORT).show();
        db.close();
    }

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

    //region MenuOptions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}
