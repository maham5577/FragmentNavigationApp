package com.sem.fragmentnavigationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "results.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_RESULTS = "results";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_QUESTION_NUMBER = "question_number";
    public static final String COLUMN_RESULT = "result";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_RESULTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION_NUMBER + " INTEGER, " +
                COLUMN_RESULT + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement if you need to upgrade the database
    }

    public static void insertResult(SQLiteDatabase db, int questionNumber, String result) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_NUMBER, questionNumber);
        values.put(COLUMN_RESULT, result);
        db.insert(TABLE_RESULTS, null, values);
    }

    public Cursor getResult() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                COLUMN_QUESTION_NUMBER,
                COLUMN_RESULT
        };
        String sortOrder = COLUMN_QUESTION_NUMBER + " ASC";
        return db.query(
                TABLE_RESULTS,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }
}
