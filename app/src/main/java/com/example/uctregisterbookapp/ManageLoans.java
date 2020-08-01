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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManageLoans extends AppCompatActivity {

    private static EditText editTextID;

    private static final String TAG = "ManageLoans";
    private static Context mContext;
    private static Button makeLoans;


    private static ArrayList<String> loanIDs = new ArrayList<>();
    private static ArrayList<String> StudentNos = new ArrayList<>();
    private static ArrayList<String> loanOuts = new ArrayList<>();
    private static ArrayList<String> loanIns = new ArrayList<>();
    private static ArrayList<String> loanItems = new ArrayList<>();
    private static ArrayList<String> ExistingIDs = new ArrayList<>();

    private RecyclerViewAdapter2 adapter;
    private RecyclerView recyclerView;

    private static ArrayList<String> availableEquip = new ArrayList<>();

    private DatabaseReference database;


    public static void setStudentID(String x){
        editTextID.setText(x);
    }

    private static int i;
    private static LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_loans);
        mContext = getApplicationContext();

        Log.d(TAG, "onCreate: "+ Main2Activity.user);


        linearLayout = findViewById(R.id.addUserGroup);
        final ImageButton toggler = findViewById(R.id.toggler);

        i = 0;

        toggler.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (i){
                            case 0:{
                                i=1;
                                toggler.setImageResource(R.drawable.down);
                                linearLayout.setVisibility(View.GONE);
                            }break;
                            case 1:{
                                i=0;
                                toggler.setImageResource(R.drawable.up);
                                linearLayout.setVisibility(View.VISIBLE);
                            }break;
                        }

                    }
                }
        );


        recyclerView = findViewById(R.id.recycler_view_layout2);

        adapter = new RecyclerViewAdapter2(this ,loanIDs  ,StudentNos, loanOuts, loanIns, loanItems);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this ));

        DatabaseReference usersTbl = FirebaseDatabase.getInstance().getReference("users");
        usersTbl.keepSynced(true);

        usersTbl.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ExistingIDs.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()
                        ) {
                            ExistingIDs.add(postSnapshot.child("StudentNo").getValue(String.class));
                        }

                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //
                    }
                }
        );


        database = FirebaseDatabase.getInstance().getReference("loans");
        database.keepSynced(true);

        database.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                        //clear existing data...
                        loanIDs.clear();
                        StudentNos.clear();
                        loanIns.clear();
                        loanItems.clear();
                        loanOuts.clear();

                        availableEquip.clear();
                        availableEquip.add("Broom");
                        availableEquip.add("Mop");
                        availableEquip.add("Both Broom + Mop");

                        makeLoans.setEnabled(true);

                            List<List<String>> user = Main2Activity.user;

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()
                        ) {
                            if (postSnapshot.exists()) {

                                //code here, iterate thru all the children.
                                if (postSnapshot.child("StudentNo").getValue(String.class)!=null && postSnapshot.child("ByWhom").getValue(String.class)!=null) {

                                    if (user.get(0).get(0).equalsIgnoreCase(postSnapshot.child("ByWhom").getValue(String.class)) || postSnapshot.child("ByWhom").getValue(String.class).contains(user.get(1).get(1))) {
                                        StudentNos.add(postSnapshot.child("StudentNo").getValue(String.class));
                                        loanOuts.add(postSnapshot.child("DateLoaned").getValue(String.class));
                                        loanIns.add(postSnapshot.child("DateReturned").getValue(String.class));
                                        loanIDs.add(postSnapshot.child("RecordId").getValue(String.class));//postSnapshot.getValue(String.class));
                                        loanItems.add(postSnapshot.child("ItemsTaken").getValue(String.class));


                                if (postSnapshot.child("DateReturned").getValue(String.class).equalsIgnoreCase("Not returned yet")) {
                                    String x = postSnapshot.child("ItemsTaken").getValue(String.class);
                                    if (availableEquip.contains(x)) {
                                        //meaning, if you've taken both mop and broom, then there's nothing left.
                                        if (x.equalsIgnoreCase("Both Broom + Mop")) {
                                            availableEquip.clear();
                                        } else {
                                            //but if you've taken either one, then option for mop and broom must be removed and that item that's taken.
                                            availableEquip.remove(x);
                                            availableEquip.remove("Both Broom + Mop");
                                        }
                                    }

                                }
                                    }
                                }

                            }
                        }


                        Log.d(TAG , "user ids :"+ StudentNos);
                        Log.d(TAG, "onDataChange: Calling reycler view bro");
                        resetViews();
                    }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );


        //end db


        TextView lDate = findViewById(R.id.loanOutDate);

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        final String dateString = format.format( new Date() );

        /*Date date = format.parse ( "2009-12-31" );*/

        editTextID = findViewById(R.id.loanStudentNo);

//spinner entry android:entries="@array/loanItems"



        lDate.setText("LOAN OUT DATE: "+ dateString);

        final Spinner lstItems = findViewById(R.id.loanItems);




        ImageButton scan = findViewById(R.id.scanID);
        scan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getApplicationContext() , ScannerPage.class));
                        ScannerPage.setControlVar(3);


                    }
                }
        );


        makeLoans = findViewById(R.id.btnMakeLoan);
        makeLoans.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String sNum = editTextID.getText().toString().toUpperCase().trim();
                        String date = dateString;
                        String items = lstItems.getSelectedItem().toString();


                        //Log.d(TAG, "onClick: "+);

                        //problem is bellow
                        //2019-07-26 11:02:51.925 18731-18731/com.example.uctregisterbookapp D/ManageLoans: user ids :[KBNGIV001, KBNGIV001, KBNGIV001, KBNGIV001, KBNGIV001, kshsh08806291, KBNGIV001, KBNGIV001, KBNGIV001, Khgfdf006, KBNGIV007, 12345, rrrerreee, gdsdfgg, KBNGIV001, KBNGIV001, MVNSIY006, MVNSIY006, tvrveccwcec, rj5jnrnrnr, rh4h4gg3g3, gbtbtbtnt, efg3g3g3, efg3g3g3, gthth5h, uuuuuu, KBNGIV001, KBNGIV001, KBNGIV001, KBNGIV001, MVNSIY006]

                        //making it work for specific floor reps on each floor.
                        //so i user 1 can add other admins, add button to or on my home screen
                        //add the nav bar thingy
                        //only users from certain floors can get items from certain floors
                        //if offline, app must return to home screen and have a loading shandis waiting for connection.


                        if (sNum!=""){
                            //create loan
                            //also make a code that fills spinner with only available items... so when recycler view detects changes

                            if (ExistingIDs.contains(sNum) ){

                                DBManager db = new DBManager(false , "");

                                db.makeBooking("Not returned yet" , dateString , sNum , items);

                                Toast.makeText(getApplicationContext() , String.format("(%s) has been loaned to %s", items , sNum), Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(ManageLoans.this, "Student doesn't exist in our database. You can't loan items to this student.", Toast.LENGTH_SHORT).show();
                            }


                            //make notifications option...

                        }
                        else
                        {
                            //toast that you gotta fill in errthang
                            Toast.makeText(ManageLoans.this, "Please enter or Scan student number!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );

    }

    private void resetViews(){
        adapter = new RecyclerViewAdapter2(this ,loanIDs  ,StudentNos, loanOuts, loanIns, loanItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this ));

        final Spinner lstItems = findViewById(R.id.loanItems);


        ArrayAdapter<String> adapter2 =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, availableEquip);
        adapter2.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        lstItems.setAdapter(adapter2);

        if (availableEquip.size()==0){
            makeLoans.setEnabled(false);
        }

    }

    public static void ConfirmReturned(String id, int id2){
        DBManager db = new DBManager(true , id);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        final String dateString = format.format( new Date() );
        db.editBooking(dateString , loanOuts.get(id2) , StudentNos.get(id2), loanItems.get(id2));

        Toast.makeText(mContext, "Loan return has been confirmed!", Toast.LENGTH_SHORT).show();
        //Notifications noty = new Notifications(mContext);
        //noty.show("Loan return has been confirmed!");

        //add options like
        //if item aint returned, send email to student, etc...

    }

}
