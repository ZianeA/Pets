package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.pets.data.DataProvider;

import java.lang.ref.WeakReference;

import static com.example.android.pets.data.PetContract.*;

public class CatalogActivity extends AppCompatActivity {

    public static final String EXTRA_PET = "EXTRA_PET";

    private static final String TAG = CatalogActivity.class.getSimpleName();
    private static final int EDIT_PET_REQUEST_CODE = 0;
    private static final int ADD_PET_REQUEST_CODE = 1;
    public static final String ORDER_BY = PetsEntry.COLUMN_NAME_ID + " DESC";

    private PetsAdapter mAdapter;
    private int mSelectedPetIndex;
    private RecyclerView mRecyclerView;
    private SQLiteDatabase mDatabase;

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


        mRecyclerView = findViewById(R.id.rv_pets);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        GetPetTask dbGetterTask = new GetPetTask(new WeakReference<CatalogActivity>(this));
        PetDbHelper dbHelper = new PetDbHelper(this);
        dbGetterTask.execute(dbHelper);
    }

    private static class GetPetTask extends AsyncTask<PetDbHelper, Void, Cursor> {

        WeakReference<CatalogActivity> mCatalogActivityWeakReference;

        public GetPetTask(WeakReference<CatalogActivity> reference) {
            mCatalogActivityWeakReference = reference;
        }


        @Override
        protected Cursor doInBackground(PetDbHelper... petDbHelpers) {
            if (petDbHelpers == null || petDbHelpers.length == 0) return null;

            CatalogActivity catalogActivity = mCatalogActivityWeakReference.get();
            if (catalogActivity == null) return null;

            catalogActivity.mDatabase = petDbHelpers[0].getWritableDatabase();
            return catalogActivity.mDatabase.query(PetsEntry.TABLE_NAME, null,
                    null, null, null, null,
                    ORDER_BY);
        }

        @Override
        protected void onPostExecute(final Cursor cursor) {
            super.onPostExecute(cursor);
            final CatalogActivity catalogActivity = mCatalogActivityWeakReference.get();
            if (catalogActivity == null || cursor == null) return;

            catalogActivity.mAdapter = new PetsAdapter(cursor,
                    new PetsAdapter.OnPetListItemClickListener() {
                        @Override
                        public void onClick(View view, int petIndex) {
                            catalogActivity.mSelectedPetIndex = petIndex;
                            cursor.moveToPosition(petIndex);
                            Pet selectedPet = new Pet();

                            int nameCol = cursor.getColumnIndex(PetsEntry.COLUMN_NAME_NAME);
                            int breedCol = cursor.getColumnIndex(PetsEntry.COLUMN_NAME_BREED);
                            int genderCol = cursor.getColumnIndex(PetsEntry.COLUMN_NAME_GENDER);
                            int weightCol = cursor.getColumnIndex(PetsEntry.COLUMN_NAME_WEIGHT);

                            selectedPet.setName(cursor.getString(nameCol));
                            selectedPet.setBreed(cursor.getString(breedCol));
                            selectedPet.setGender(cursor.getInt(genderCol));
                            selectedPet.setWeight(cursor.getInt(weightCol));

                            catalogActivity.editPet(selectedPet);
                        }
                    });
            catalogActivity.mRecyclerView.setAdapter(catalogActivity.mAdapter);
        }
    }

    private static class AddPetTask extends AsyncTask<Pet, Void, Cursor>{

        WeakReference<CatalogActivity> mCatalogActivityWeakReference;

        public AddPetTask(WeakReference<CatalogActivity> reference) {
            mCatalogActivityWeakReference = reference;
        }

        @Override
        protected Cursor doInBackground(Pet... pets) {
            if(pets == null || pets.length == 0) return null;

            ContentValues values = new ContentValues();
            values.put(PetsEntry.COLUMN_NAME_NAME, pets[0].getName());
            values.put(PetsEntry.COLUMN_NAME_BREED, pets[0].getBreed());
            values.put(PetsEntry.COLUMN_NAME_GENDER, pets[0].getGender());
            values.put(PetsEntry.COLUMN_NAME_WEIGHT, pets[0].getWeight());

            CatalogActivity catalogActivity = mCatalogActivityWeakReference.get();
            if(catalogActivity == null) return null;

            catalogActivity.mDatabase.insert(PetsEntry.TABLE_NAME, null, values);
//            SystemClock.sleep(9000);
            return catalogActivity.mDatabase.query(PetsEntry.TABLE_NAME, null,
                    null, null, null, null, ORDER_BY);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            CatalogActivity catalogActivity = mCatalogActivityWeakReference.get();
            if(catalogActivity == null || cursor == null) return;

            catalogActivity.mAdapter.swapData(cursor);
            catalogActivity.mAdapter.notifyItemInserted(0);
            catalogActivity.mRecyclerView.smoothScrollToPosition(0);
        }
    }

    //Add data to the database. This method should be executed only once, since any data added
    //to the database is persistent.
    private void addDummyData(PetDbHelper mDbHelper) {
        SQLiteDatabase writableDb = mDbHelper.getWritableDatabase();

        for (Pet p : DataProvider.pets) {
            ContentValues values = new ContentValues();
            values.put(PetsEntry.COLUMN_NAME_NAME, p.getName());
            values.put(PetsEntry.COLUMN_NAME_BREED, p.getBreed());
            values.put(PetsEntry.COLUMN_NAME_GENDER, p.getGender());
            values.put(PetsEntry.COLUMN_NAME_WEIGHT, p.getWeight());
            writableDb.insert(PetsEntry.TABLE_NAME, null, values);
        }
    }

    private void editPet(Pet selectedPet) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra(EXTRA_PET, selectedPet);
        startActivityForResult(intent, EDIT_PET_REQUEST_CODE);
    }

    private void addPet() {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivityForResult(intent, ADD_PET_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (!data.hasExtra(EXTRA_PET)) return;

            Pet newPet = data.getParcelableExtra(EXTRA_PET);

            if (requestCode == ADD_PET_REQUEST_CODE) {
                AddPetTask addPetTask = new AddPetTask(
                        new WeakReference<>(this));
                addPetTask.execute(newPet);
            } else if (requestCode == EDIT_PET_REQUEST_CODE) {

            }
        } else if (resultCode == EditorActivity.RESULT_DELETE_PET) {

        }
//        if (requestCode == EDIT_PET_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                mAdapter.notifyItemChanged(mSelectedPetIndex);
//            } else if (resultCode == EditorActivity.RESULT_DELETE_PET) {
//                mAdapter.notifyItemRemoved(mSelectedPetIndex);
//            }
//        } else if (requestCode == ADD_PET_REQUEST_CODE && resultCode == RESULT_OK) {
//            int lastItemIndex = mAdapter.getItemCount() - 1;
//            mAdapter.notifyItemInserted(lastItemIndex);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
