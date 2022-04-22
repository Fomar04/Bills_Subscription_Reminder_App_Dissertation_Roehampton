package com.example.bsr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import static androidx.core.content.ContextCompat.startActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class billableAdapter extends RecyclerView.Adapter<billableAdapter.BillableViewHolder> {

    private ArrayList<Billable> billable_list;
    private String collection_name;
    private Context context;

    public billableAdapter(ArrayList<Billable> billable_list, String collection_name, Context context) {
        this.billable_list = billable_list;
        this.collection_name = collection_name;
        this.context = context;
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
        holder.date.setText(date);
        holder.amount.setText(amount);

        holder.itemView.setOnClickListener(view -> {
            Context context = view.getContext();

            Intent intent = new Intent(context, Dashboard.class);
            intent.putExtra("description", description);
            intent.putExtra("date", date);
            intent.putExtra("amount", amount);

            startActivity(context, intent, null);
        });
    }

    @Override
    public int getItemCount() {
        return billable_list.size();
    }

    public class BillableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView amount;
        TextView date;
        TextView description;

        public BillableViewHolder(@NonNull View itemView) {
            super(itemView);
            description = (TextView)itemView.findViewById(R.id.billableDescription);
            date = (TextView)itemView.findViewById(R.id.billableDate);
            amount = (TextView)itemView.findViewById(R.id.billableAmount);

            itemView.setOnLongClickListener(view -> {
                //Log.d("Tagggg", billable_list.get(getAdapterPosition()).toString());

                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(view.getContext());
                deleteDialog.setTitle("Delete from list");
                deleteDialog.setMessage("Are you sure you want to delete this from the list?.");

                deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                       Session.db.getInstance().collection(collection_name).document(billable_list.get(getAdapterPosition()).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                           public void onSuccess(Void aVoid) {
                               Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

                               billable_list.remove(getAdapterPosition());
                               notifyDataSetChanged();
                            }
                        })
                                .addOnFailureListener(e -> Toast.makeText(context, "Something went wrong, please try again", Toast.LENGTH_LONG).show());;


                    }
                });

                deleteDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // close the dialog



                    }
                });


                deleteDialog.create().show();


                return true;
            });


        }

        @Override
        public void onClick(View view) {

        }




    }


}