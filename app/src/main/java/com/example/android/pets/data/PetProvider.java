package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.pets.Pet;
import com.example.android.pets.data.PetContract.*;


public final class PetProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int PETS = 100;
    private static final int PETS_ID = 101;

    private PetDbHelper mDbHelper;

    static {
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PETS_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String orderBy) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                cursor = db.query(PetsEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, orderBy);
                break;
            case PETS_ID:
                selection = PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PetsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, orderBy);
                break;
            default:
                throw new IllegalArgumentException("invalid URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                id = db.insert(PetsEntry.TABLE_NAME, null, values);
                break;
            default:
                return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int id;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                id = db.delete(PetsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PETS_ID:
                selection = PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                id = db.delete(PetsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("invalid URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int id;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                id = db.update(PetsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case PETS_ID:
                selection = PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                id = db.update(PetsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                id = -1;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }
}
