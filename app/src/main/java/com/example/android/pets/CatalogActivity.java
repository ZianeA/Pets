package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.android.pets.data.PetAsyncQueryHandler;
import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetProvider;

import java.lang.ref.WeakReference;

import static com.example.android.pets.data.PetContract.*;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_PET = "EXTRA_PET";

    private static final String TAG = CatalogActivity.class.getSimpleName();
    private static final int LOADER_PET_ID = 101;

    private PetsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPet();
            }
        });

        getSupportLoaderManager().initLoader(LOADER_PET_ID, null, this);
        RecyclerView mRecyclerView = findViewById(R.id.rv_pets);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PetsAdapter(null, new PetsAdapter.OnPetListItemClickListener() {
            @Override
            public void onClick(View view, Pet selectedPet, int petIndex) {
                editPet(selectedPet);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void editPet(Pet selectedPet) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra(EXTRA_PET, selectedPet);
        startActivity(intent);
    }

    private void addPet() {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_dummy_data:
                addDummyData();
                return true;
            case R.id.action_delete_all_pets:
                deleteAllPets();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAllPets() {
        PetAsyncQueryHandler deleteHandler = new PetAsyncQueryHandler(getContentResolver());
        deleteHandler.startDelete(0, null, PetsEntry.CONTENT_URI, null,
                null);
    }

    private void addDummyData() {
        PetAsyncQueryHandler insertHandler = new PetAsyncQueryHandler(getContentResolver());

        for (int i = 0; i < 200; i++) {
            ContentValues values = new ContentValues();
            values.put(PetsEntry.COLUMN_NAME, "Pet " + "#" + i);
            values.put(PetsEntry.COLUMN_BREED, "Breed " + "#" + i);
            values.put(PetsEntry.COLUMN_GENDER, Pet.GENDER_MALE);
            values.put(PetsEntry.COLUMN_WEIGHT, i);
            insertHandler.startInsert(0, null, PetsEntry.CONTENT_URI, values);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader loader;

        switch (id) {
            case LOADER_PET_ID:
                loader = new CursorLoader(this, PetsEntry.CONTENT_URI, null,
                        null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Invalid loader id: " + id);
        }

        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();

        switch (id) {
            case LOADER_PET_ID:
                mAdapter.swapData(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        int id = loader.getId();

        switch (id) {
            case LOADER_PET_ID:
                mAdapter.swapData(null);
                break;
        }
    }
}
