package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.pets.data.PetAsyncQueryHandler;
import com.example.android.pets.databinding.ActivityEditorBinding;

import static com.example.android.pets.data.PetContract.*;

public class EditorActivity extends AppCompatActivity {

    private static final String TAG = EditorActivity.class.getSimpleName();
    private static final String ADD_PET_TITLE = "Add Pet";
    private static final String EDIT_PET_TITLE = "Edit Pet";

    private boolean isAddPet;
    private Pet mSelectedPet;
    private Spinner mGenderSpinner;
    ActivityEditorBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mGenderSpinner = findViewById(R.id.spinner_gender);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_editor);
        Intent intent = getIntent();

        if (intent.hasExtra(CatalogActivity.EXTRA_PET)) {
            setupEditPet();
        } else {
            setupAddPet();
        }
    }

    private void setupAddPet() {
        isAddPet = true;
        setTitle(ADD_PET_TITLE);
        mBinding.setPet(new Pet());
    }

    private void setupEditPet() {
        setTitle(EDIT_PET_TITLE);
        mSelectedPet = getIntent().
                getParcelableExtra(CatalogActivity.EXTRA_PET);

        mBinding.setPet(mSelectedPet);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        if (isAddPet) {
            hideDeleteMenuItem(menu);
        }
        return true;
    }

    private void hideDeleteMenuItem(Menu menu) {
        MenuItem delete = menu.findItem(R.id.action_delete);
        delete.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_save:
                savePet();
                finish();
                return true;
            case R.id.action_delete:
                deletePet();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePet() {
        PetAsyncQueryHandler deleteHandler = new PetAsyncQueryHandler(getContentResolver());
        deleteHandler.startDelete(0, null, ContentUris.withAppendedId(
                PetsEntry.CONTENT_URI, mSelectedPet.getId()),
                null, null);
    }

    private void savePet() {
        PetAsyncQueryHandler queryHandler = new PetAsyncQueryHandler(getContentResolver());

        ContentValues values = new ContentValues();
        Pet newPet = mBinding.getPet();
        values.put(PetsEntry.COLUMN_NAME, newPet.getName());
        values.put(PetsEntry.COLUMN_BREED, newPet.getBreed());
        values.put(PetsEntry.COLUMN_GENDER, newPet.getGender());
        values.put(PetsEntry.COLUMN_WEIGHT, newPet.getWeight());
        Log.d(TAG, "savePet: " + mGenderSpinner.getSelectedItemPosition());

        if (isAddPet) {
            queryHandler.startInsert(0, null, PetsEntry.CONTENT_URI, values);
        } else {
            queryHandler.startUpdate(0, null, ContentUris.withAppendedId(
                    PetsEntry.CONTENT_URI, mSelectedPet.getId()),
                    values, null, null);
        }
    }
}
