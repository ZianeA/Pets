package com.example.android.pets;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditorActivity extends AppCompatActivity {

    private static final String TAG = EditorActivity.class.getSimpleName();
    private static final String ADD_PET_TITLE = "Add Pet";
    private static final String EDIT_PET_TITLE = "Edit Pet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        EditText mNameEditText = findViewById(R.id.et_name);
        EditText mBreedEditText = findViewById(R.id.et_breed);
        EditText mWeightEditText = findViewById(R.id.et_measurement);
        Spinner mGenderSpinner = findViewById(R.id.spinner_gender);

        Intent intent = getIntent();

        if(intent.hasExtra(CatalogActivity.SELECTED_PET_KEY)) {
            setTitle(EDIT_PET_TITLE);
            Pet selectedPet = getIntent().getParcelableExtra(CatalogActivity.SELECTED_PET_KEY);
            mNameEditText.setText(selectedPet.getName());
            mBreedEditText.setText(selectedPet.getBreed());
            mWeightEditText.setText(String.valueOf(selectedPet.getWeight()));
            mGenderSpinner.setSelection(selectedPet.getGender());

        } else {
            setTitle(ADD_PET_TITLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
