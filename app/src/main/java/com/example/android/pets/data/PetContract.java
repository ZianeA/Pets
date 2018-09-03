package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class PetContract {

    public PetContract() {
    }

    public static class PetsEntry implements BaseColumns {

        public static final String TABLE_NAME = "pets";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_BREED = "breed";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_WEIGHT = "weight";

        public static final int GENDER_MALE = 0;
        public static final int GENDER_FEMALE = 1;
        public static final int GENDER_UNKNOWN = 2;
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PetsEntry.TABLE_NAME + " (" +
                    PetsEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    PetsEntry.COLUMN_NAME_NAME + " TEXT," +
                    PetsEntry.COLUMN_NAME_BREED + " TEXT," +
                    PetsEntry.COLUMN_NAME_GENDER + " INTEGER," +
                    PetsEntry.COLUMN_NAME_WEIGHT + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PetsEntry.TABLE_NAME;

    public static class PetDbHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "shelter.db";

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
}
