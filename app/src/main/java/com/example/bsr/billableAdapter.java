package com.example.bsr;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import static androidx.core.content.ContextCompat.startActivity;

import java.util.ArrayList;

public class billableAdapter extends RecyclerView.Adapter<billableAdapter.BillableViewHolder> {

    private ArrayList<Billable> billable_list;

    public billableAdapter(ArrayList<Billable> billable_list) {
        this.billable_list = billable_list;
    }

    @NonNull
    @Override
    public BillableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.billable_layout, parent, false);
        return new BillableViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BillableViewHolder holder, int position) {
        String description = billable_list.get(position).getDescription();
        String date = billable_list.get(position).getDate();
        String amount = billable_list.get(position).getAmount();

        holder.description.setText(description);
        holder.date.setText(amount);
        holder.amount.setText(amount);

        holder.itemView.setOnClickListener(view -> {
            Context context = view.getContext();

            Intent intent = new Intent(context, Dashboard.class);
            intent.putExtra("title", description);
            intent.putExtra("date", date);
            intent.putExtra("amount", amount);
            startActivity(context, intent, null);
        });
    }

    @Override
    public int getItemCount() {
        return billable_list.size();
    }

    public class BillableViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        TextView date;
        TextView description;

        public BillableViewHolder(@NonNull View itemView) {
            super(itemView);
            description = (TextView)itemView.findViewById(R.id.billableDescription);
            date = (TextView)itemView.findViewById(R.id.billableDate);
            amount = (TextView)itemView.findViewById(R.id.billableAmount);
        }
    }

}