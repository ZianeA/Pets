package com.example.android.pets;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetsAdapterViewHolder>{

    private List<Pet> mData;

    public PetsAdapter(List<Pet> data) {
        mData = data;
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

    public class PetsAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView mName;
        private TextView mBreed;


        public PetsAdapterViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_name);
            mBreed = itemView.findViewById(R.id.tv_breed);
        }

        private void bind(int position){
            mName.setText(mData.get(position).getName());
            mBreed.setText(mData.get(position).getBreed());
        }
    }
}
