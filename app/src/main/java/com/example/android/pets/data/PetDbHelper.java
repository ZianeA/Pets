package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PetDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "shelter.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PetContract.PetsEntry.TABLE_NAME + " (" +
                    PetContract.PetsEntry._ID + " INTEGER PRIMARY KEY," +
                    PetContract.PetsEntry.COLUMN_NAME + " TEXT," +
                    PetContract.PetsEntry.COLUMN_BREED + " TEXT," +
                    PetContract.PetsEntry.COLUMN_GENDER + " INTEGER," +
                    PetContract.PetsEntry.COLUMN_WEIGHT + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PetContract.PetsEntry.TABLE_NAME;

    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
