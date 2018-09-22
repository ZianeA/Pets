package com.example.android.pets;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

import static com.example.android.pets.data.PetContract.*;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetsAdapterViewHolder> {

    private Cursor mData;
    private OnPetListItemClickListener mClickListener;

    PetsAdapter(Cursor data, OnPetListItemClickListener clickListener) {
        this.mData = data;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public PetsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_list_item, parent, false);

        return new PetsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetsAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.getCount() : 0;
    }

    public class PetsAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mName;
        private TextView mBreed;

        PetsAdapterViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_name);
            mBreed = itemView.findViewById(R.id.tv_breed);
            itemView.setOnClickListener(this);
        }

        private void bind(int position) {
            if (!mData.moveToPosition(position)) return;

            int nameCol = mData.getColumnIndex(PetsEntry.COLUMN_NAME);
            int breedCol = mData.getColumnIndex(PetsEntry.COLUMN_BREED);
            mName.setText(mData.getString(nameCol));
            mBreed.setText(mData.getString(breedCol));
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Pet selectedPet = CursorToPet(mData, position);

            if(selectedPet == null) return;

            mClickListener.onClick(view, selectedPet, position);
        }
    }

    private Pet CursorToPet(Cursor cursor, int position) {
        if (cursor == null || !cursor.moveToPosition(position)) return null;

        int id = cursor.getInt(cursor.getColumnIndex(PetsEntry._ID));
        String name = cursor.getString(cursor.getColumnIndex(PetsEntry.COLUMN_NAME));
        String breed = cursor.getString(cursor.getColumnIndex(PetsEntry.COLUMN_BREED));
        int gender = cursor.getInt(cursor.getColumnIndex(PetsEntry.COLUMN_GENDER));
        int weight = cursor.getInt(cursor.getColumnIndex(PetsEntry.COLUMN_WEIGHT));

        return new Pet(id, name, breed, gender, weight);
    }

    public interface OnPetListItemClickListener {
        void onClick(View view, Pet selectedPet, int petIndex);
    }

    public void swapData(Cursor newData) {
        if (mData == newData) return;

        if (mData != null) {
            mData.close();
        }

        mData = newData;
        notifyDataSetChanged();
    }
}
