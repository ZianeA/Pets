package com.example.android.pets;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.pets.data.DataProvider;

public class CatalogActivity extends AppCompatActivity {

    public static final String EXTRA_PET_INDEX = "SELECTED_PET_INDEX";

    private static final String TAG = CatalogActivity.class.getSimpleName();
    private static final int EDIT_PET_REQUEST_CODE = 0;
    private static final int ADD_PET_REQUEST_CODE = 1;

    private PetsAdapter mAdapter;
    private int mSelectedPetIndex;

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

        RecyclerView mRecyclerView = findViewById(R.id.rv_pets);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PetsAdapter(DataProvider.pets, new PetsAdapter.OnPetListItemClickListener() {
            @Override
            public void onClick(View view, int petIndex) {
                mSelectedPetIndex = petIndex;
                editPet();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void editPet() {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra(EXTRA_PET_INDEX, mSelectedPetIndex);
        startActivityForResult(intent, EDIT_PET_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == EDIT_PET_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                mAdapter.notifyItemChanged(mSelectedPetIndex);
            } else if(resultCode == EditorActivity.RESULT_DELETE_PET){
                mAdapter.notifyItemRemoved(mSelectedPetIndex);
            }
        } else if (requestCode == ADD_PET_REQUEST_CODE && resultCode == RESULT_OK) {
            int lastItemIndex = mAdapter.getItemCount() - 1;
            mAdapter.notifyItemInserted(lastItemIndex);
        }
    }

    private void addPet() {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivityForResult(intent, ADD_PET_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
