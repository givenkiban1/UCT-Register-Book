package com.example.uctregisterbookapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> imgNames = new ArrayList<>();
    private ArrayList<String> studentNames = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter( Context mContext, ArrayList<String> imgNames, ArrayList<String> studentNames) {
        this.imgNames = imgNames;
        this.studentNames = studentNames;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_layout , parent , false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called. "+position);
        //load images if using glide to download, etc...

        holder.textView.setText(studentNames.get(position));

        holder.relativeLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: Clicked on: "+ studentNames.get(position));
                        Toast.makeText(mContext, studentNames.get(position), Toast.LENGTH_SHORT).show();
                    }
                }
        );


        holder.relativeLayout.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        ManageUsers.editUser(position);

                        return false;
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return studentNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        CircleImageView circleImageView;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.student_Name);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.RelativeLayout);

            circleImageView = (CircleImageView) itemView.findViewById(R.id.image);
        }
    }

}
