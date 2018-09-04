package com.example.android.pets;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

import java.util.List;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetsAdapterViewHolder> {

    private Cursor mData;
    private OnPetListItemClickListener mClickListener;

    public PetsAdapter(Cursor data, OnPetListItemClickListener clickListener) {
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

        public PetsAdapterViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_name);
            mBreed = itemView.findViewById(R.id.tv_breed);
            itemView.setOnClickListener(this);
        }

        private void bind(int position) {
            if (!mData.moveToPosition(position)) return;

            int nameCol = mData.getColumnIndex(PetContract.PetsEntry.COLUMN_NAME_NAME);
            int breedCol = mData.getColumnIndex(PetContract.PetsEntry.COLUMN_NAME_BREED);
            mName.setText(mData.getString(nameCol));
            mBreed.setText(mData.getString(breedCol));
            int idCol = mData.getColumnIndex(PetContract.PetsEntry.COLUMN_NAME_ID);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClick(view, getLayoutPosition());
        }
    }

    public interface OnPetListItemClickListener {
        public void onClick(View view, int petIndex);
    }

    public void swapData(Cursor newData) {
        if (mData != null) {
            mData.close();
        }

        mData = newData;
    }
}
