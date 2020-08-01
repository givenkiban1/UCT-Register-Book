package com.example.uctregisterbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageUsers extends AppCompatActivity {

    private static final String TAG = "ManageUsers";
    private DatabaseReference database;

    private static Context mContext;
    private static ArrayList<String> StudentNos = new ArrayList<>();
    private static ArrayList<String> studentNames = new ArrayList<>();
    private static ArrayList<String> cellNumbers = new ArrayList<>();
    private static ArrayList<String> Surnames = new ArrayList<>();
    private static ArrayList<String> RoomNos = new ArrayList<>();
    private static ArrayList<String> NamePlusSurname = new ArrayList<>();
    private static ArrayList<String> imgNames = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        mContext = getApplicationContext();



        Button btnAdd = (Button) findViewById(R.id.btnAdd); //button for adding users


        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getApplicationContext() , ScannerPage.class));
                        ScannerPage.setControlVar(2); //2 meaning creating a user , 3 meaning editing a user student no...

                    }
                }
        );




        recyclerView = findViewById(R.id.recycler_view_layout);

        adapter = new RecyclerViewAdapter(this ,  imgNames, NamePlusSurname);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this ));

        database = FirebaseDatabase.getInstance().getReference("users");
        database.keepSynced(true);

        database.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                        //clear existing data...
                        studentNames.clear();
                        StudentNos.clear();
                        cellNumbers.clear();
                        Surnames.clear();
                        RoomNos.clear();
                        NamePlusSurname.clear();
                        imgNames.clear();

                        List<List<String>> user = Main2Activity.user;

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()
                        ) {

                            if (postSnapshot.exists()) {
                                //code here, iterate thru all the children.


                                if (postSnapshot.child("StudentNo").getValue(String.class) != null) {

                                    String fr = user.get(0).get(3);

                                    if (user.get(0).get(4).equalsIgnoreCase("assistant")){
                                        fr = user.get(1).get(1);
                                    }

                                    if (postSnapshot.child("FloorRep").getValue(String.class).equalsIgnoreCase(fr)) {

                                        StudentNos.add(postSnapshot.child("StudentNo").getValue(String.class));
                                        studentNames.add(postSnapshot.child("Name").getValue(String.class));
                                        cellNumbers.add(postSnapshot.child("CellNumber").getValue(String.class));
                                        Surnames.add(postSnapshot.child("Surname").getValue(String.class));
                                        RoomNos.add(postSnapshot.child("RoomNo").getValue(String.class));
                                        NamePlusSurname.add(postSnapshot.child("Name").getValue(String.class) + " " + postSnapshot.child("Surname").getValue(String.class));


                                    }

                                }


                            }
                        }



                        Log.d(TAG, "onDataChange: Calling reycler view bro");
                        resetViews();
                    }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );


    }

    public static void editUser(int id){
        mContext.startActivity(new Intent(mContext, AddUsers.class));
        AddUsers.sName = studentNames.get(id);
        AddUsers.sRoomNo = RoomNos.get(id);
        AddUsers.sCellNo = cellNumbers.get(id);
        AddUsers.sID = StudentNos.get(id);
        AddUsers.sSurname = Surnames.get(id);
        AddUsers.setControlVar(2);
    }

    
    private void resetViews(){

        adapter = new RecyclerViewAdapter(this , imgNames, NamePlusSurname);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this ));

    }

    /*
    @Override
    public void onBackPressed() {
        finish();
    }
    */
}
