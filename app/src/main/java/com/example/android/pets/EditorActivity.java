package com.example.android.pets;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.pets.data.DataProvider;

public class EditorActivity extends AppCompatActivity {

    public static final int RESULT_DELETE_PET = 405;

    private static final String TAG = EditorActivity.class.getSimpleName();
    private static final String ADD_PET_TITLE = "Add Pet";
    private static final String EDIT_PET_TITLE = "Edit Pet";

    private boolean isAddPet;
    private int mSelectedPetIndex;
    private EditText mNameEditText;
    private EditText mBreedEditText;
    private EditText mWeightEditText;
    private Spinner mGenderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mNameEditText = findViewById(R.id.et_name);
        mBreedEditText = findViewById(R.id.et_breed);
        mWeightEditText = findViewById(R.id.et_measurement);
        mGenderSpinner = findViewById(R.id.spinner_gender);

        Intent intent = getIntent();

        if (intent.hasExtra(CatalogActivity.EXTRA_PET)) {
            editPet();
        } else {
            addPet();
        }
    }

    private void addPet() {
        isAddPet = true;
        setTitle(ADD_PET_TITLE);
    }

    private void editPet() {
        setTitle(EDIT_PET_TITLE);
        Pet selectedPet = getIntent().
                getParcelableExtra(CatalogActivity.EXTRA_PET);

        mNameEditText.setText(selectedPet.getName());
        mBreedEditText.setText(selectedPet.getBreed());
        mWeightEditText.setText(String.valueOf(selectedPet.getWeight()));
        mGenderSpinner.setSelection(selectedPet.getGender());
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
                return true;
            case R.id.action_delete:
                deletePet();
                setResult(RESULT_DELETE_PET);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePet() {
        DataProvider.pets.remove(mSelectedPetIndex);
    }

    private void savePet() {
        Pet newPet = new Pet();
        newPet.setName(mNameEditText.getText().toString());
        newPet.setBreed(mBreedEditText.getText().toString());
        newPet.setGender(mGenderSpinner.getSelectedItemPosition());
        newPet.setWeight(Integer.decode(mWeightEditText.getText().toString()));

        Intent returnIntent = new Intent();
        returnIntent.putExtra(CatalogActivity.EXTRA_PET, newPet);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
