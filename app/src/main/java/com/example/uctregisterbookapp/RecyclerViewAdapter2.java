package com.example.uctregisterbookapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.viewHolder> {
    private static final String TAG = "RecyclerViewAdapter2";

    private ArrayList<String> loanIDs = new ArrayList<>();
    private ArrayList<String> StudentNos = new ArrayList<>();
    private ArrayList<String> loanOuts = new ArrayList<>();
    private ArrayList<String> loanIns = new ArrayList<>();
    private ArrayList<String> loanItems = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter2(Context context, ArrayList<String> loanIDs, ArrayList<String> studentNos, ArrayList<String> loanOuts, ArrayList<String> loanIns, ArrayList<String> loanItems) {
        this.loanIDs = loanIDs;
        this.StudentNos = studentNos;
        this.loanOuts = loanOuts;
        this.loanIns = loanIns;
        this.loanItems = loanItems;
        this.mContext = context;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_items, parent , false);
        viewHolder xViewHolder = new viewHolder(view);
        return xViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: called. "+position);
        //load images if using glide to download, etc...

        holder.tID.setText(StudentNos.get(position));

        holder.tIn.setText(loanIns.get(position));

        holder.tOut.setText(loanOuts.get(position));


        if (loanItems.get(position).equalsIgnoreCase("Broom")){
            holder.circleImageView.setImageResource(R.drawable.broom);
        }
        else
            if (loanItems.get(position).equalsIgnoreCase("Mop")){
                holder.circleImageView.setImageResource(R.drawable.jnjnn);
            }
            else
            if (loanItems.get(position).equalsIgnoreCase("Both Broom + Mop"))
                {
                    holder.circleImageView.setImageResource(R.drawable.mopnbucket);
                }

            /*
        Log.d(TAG, "onBindViewHolder: ITEMS :"+loanItems.get(position));
        Log.d(TAG, "onBindViewHolder: DATE OUT :"+loanOuts.get(position));
        Log.d(TAG, "onBindViewHolder: DATE IN : "+loanIns.get(position));
        Log.d(TAG, "onBindViewHolder: RECORD ID "+loanIDs.get(position));
        Log.d(TAG, "onBindViewHolder: STUDENT NO> : "+StudentNos.get(position));
        Log.d(TAG, "onBindViewHolder: "+ (loanItems.get(position).equalsIgnoreCase("Broom")));
*/



        //if color shandis
        if (loanIns.get(position).equalsIgnoreCase("Not returned yet")){
            holder.relativeLayout.setBackgroundColor(R.color.LightBlue);
        }
        else{

            holder.relativeLayout.setBackgroundColor(R.color.Green);
        }




        holder.relativeLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, String.format("onClick: Clicked on loan No. %s", loanIDs.get(position)));
                        Toast.makeText(mContext, String.format("loan No. %s", loanIDs.get(position)), Toast.LENGTH_SHORT).show();
                    }
                }
        );


        holder.relativeLayout.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        Log.d(TAG, "Wish to confirm returned");
             //           Toast.makeText(mContext, "Wish to confirm returned", Toast.LENGTH_SHORT).show();
                        //ManageUsers.editUser(position);

                        if (loanIns.get(position).equalsIgnoreCase("Not returned yet")){

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                            builder.setMessage(String.format("Do you wish to Confirm %s Return ?", loanItems.get(position)))
                                    .setTitle("Confirmation");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked OK button
                                    ManageLoans.ConfirmReturned(loanIDs.get(position), position);
                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                    //do nothing.
                                }
                            });

                            AlertDialog dialog = builder.create();

                            dialog.show();



                        }

                        return false;
                    }
                }
        );


        //end viewholder bind
    }

    @Override
    public int getItemCount() {
        return loanIDs.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder{

        TextView tID, tOut, tIn;
        CircleImageView circleImageView;
        RelativeLayout relativeLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tID = itemView.findViewById(R.id.student_Name);
            tOut = itemView.findViewById(R.id.txtLoanOut);
            tIn = itemView.findViewById(R.id.txtLoanIn);
            circleImageView = itemView.findViewById(R.id.image2);
            relativeLayout = itemView.findViewById(R.id.RelativeLayout2);



        }
    }

}
