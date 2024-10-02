package com.vdsl.thithu_and103;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MotorcycleAdapter extends RecyclerView.Adapter<MotorcycleAdapter.MotorcycleViewHolder> {
    private List<XeMay> motorcycles;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnUpdateClickListener onUpdateClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(XeMay motorcycle);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(XeMay motorcycle);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(XeMay motorcycle);
    }

    public MotorcycleAdapter(Context context, List<XeMay> motorcycles, OnItemClickListener onItemClickListener, OnUpdateClickListener onUpdateClickListener, OnDeleteClickListener onDeleteClickListener) {
        this.context = context;
        this.motorcycles = motorcycles;
        this.onItemClickListener = onItemClickListener;
        this.onUpdateClickListener = onUpdateClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public MotorcycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_motorcycle, parent, false);
        return new MotorcycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MotorcycleViewHolder holder, int position) {
        XeMay motorcycle = motorcycles.get(position);
        holder.textViewName.setText(motorcycle.getTen_xe_PH46164());
        holder.textViewPrice.setText(String.format("%.2f VNĐ", motorcycle.getGia_ban_PH46164()));
        holder.textViewColor.setText(motorcycle.getMau_sac_PH46164());
        Glide.with(context).load(motorcycle.getHinh_anh_PH46164()).into(holder.imageViewMotorcycle);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(motorcycle));

        holder.buttonUpdate.setOnClickListener(v -> onUpdateClickListener.onUpdateClick(motorcycle));
        holder.buttonDelete.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(motorcycle));
    }

    @Override
    public int getItemCount() {
        return motorcycles.size();
    }

    public static class MotorcycleViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice, textViewColor;
        ImageView imageViewMotorcycle;
        Button buttonUpdate, buttonDelete;


        public MotorcycleViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewColor = itemView.findViewById(R.id.textViewColor);
            imageViewMotorcycle = itemView.findViewById(R.id.imageViewMotorcycle);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

