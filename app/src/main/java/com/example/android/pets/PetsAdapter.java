package com.example.android.pets;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetsAdapterViewHolder> {

    private List<Pet> mData;
    private OnPetListItemClickListener mClickListener;

    public PetsAdapter(List<Pet> data, OnPetListItemClickListener clickListener) {
        mData = data;
        mClickListener = clickListener;
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
        return mData != null ? mData.size() : 0;
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
            mName.setText(mData.get(position).getName());
            mBreed.setText(mData.get(position).getBreed());
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClick(view, getLayoutPosition());
        }
    }

    public interface OnPetListItemClickListener {
        public void onClick(View view, int petIndex);
    }
}
