package com.example.android.pets;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        EditText mName = findViewById(R.id.et_name);
        EditText mBreed = findViewById(R.id.et_breed);
        EditText mWeight = findViewById(R.id.et_measurement);
        Spinner mGenderSpinner = findViewById(R.id.spinner_gender);

        Pet selectedPet = getIntent().getParcelableExtra(CatalogActivity.SELECTED_PET_KEY);

        mName.setText(selectedPet.getName());
        mBreed.setText(selectedPet.getBreed());
        mWeight.setText(String.valueOf(selectedPet.getWeight()));
        mGenderSpinner.setSelection(selectedPet.getGender());

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
