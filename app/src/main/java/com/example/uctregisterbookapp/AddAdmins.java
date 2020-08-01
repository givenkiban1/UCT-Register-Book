package com.example.uctregisterbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AddAdmins extends AppCompatActivity {


    private static final String TAG = "AddAdmins";
    public Context mContext;

    private static Button btn1;
    public static EditText txtID,txtName,txtSurname,txtCellNo, txtRoomNo;
    public static Spinner spinFloor, spinBlock, spinType;

    private FirebaseAuth mAuth1;



    private static ArrayList<String> availableFloors = new ArrayList<>();
    private static ArrayList<String> noFloors = new ArrayList<>();

    private static ArrayList<String> admins;
    private static ArrayList<ArrayList<String>> floorReps;

/*
    private void initFbase(){

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://uctregisterbookapp.firebaseio.com")
                .setApiKey("-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDjRaEDqksA9qzT\\n6wqsGYHh7xOHwNGUN2Z7oLIokclQigEGR92lZFKtcRfBZjoqzt4O7ucVbrPAhQR0\\nFpdcAolcwEA51WX8i/jtIXNItJGCwgPtEtfjr8QBIdkyVY5fUPzoqeAQXoDPnHAh\\nya23sbXGc4P83rd5fBk9Ai8AYwPc/F/tBXwaHpqRZzYwj/I6xJroAaXDJWJuJq18\\nix66riXdw1KREdNAjET6gVx4GTA2CCnwFMy92Y71BjG2Mj7qC0u0fJb97uT6TB0p\\nBWdP2BjbYIOYb5abnhnvt8sMBS1WYcT0vn64JoaAz/ISlkf1k1sUwzN4deYMBz2d\\nQZCmx2i3AgMBAAECggEAJLgguiNe5+Jy7PBKMqESY4sJeEL2eYBz7eB6OGTJhlot\\nrTKaRHkPhJdDk8ss9bKjnP0LV+KhZyb2n7zwqnx8bgBfwBhS2HlS+BkR87q80cf/\\n82M6fz5sKBSsb130WUEQA5VCrtJd9G0djs/zYbz0FgKe3hGryhR7TWiXaHQW8TKF\\nVwAPEyECGhstOxwfAUDPSnSEVkP9j913l7EHOf1tlKnSOnfBNk8uDOxik/7t6cSX\\nIDntELihV9PUDEKszYcMqpXMU6sMvO+EPAms2vsP7SH66ppldM8VB6+b3EyA9dFP\\nWY4ifpPScrp3DcCcLdMZhkOnUkMzSwM0jaZSA2YdJQKBgQD735WxcbFippos4FiS\\nVDOp3L/kQ0npKkkdI7qWVzNQIcUaYNWifYu/Bn07p1l2Rgb/ZcfZxJNILlDM2e2s\\n9a6dV1boWdSvfEoXlYFhPnQMoohSNThmO4dREX7r28p8ZiJhCCsVHY5f6Pc3V8Te\\nPRmmxmB3tWaniZ0doy+LJwtcWwKBgQDm/tw8jVJvxpnYXBdDuaZDyLCNgv4QNOlE\\nQe2PN9xHf3GVX1IRA/sWN0Q4yjumDAm0Ck4s034yXA8VDKcv8o1kt4jPjAZftLb1\\nqpU6dMLP+ttDIbAY+p3Ra5hGGcvO8lgxKttKQMfT2HZSTaRcR5j4E/5V7gKAX/+W\\nK6L73wOD1QKBgQCynxxcy8E9seMSwkNt+rLs2rVnXuTyq19Qf/8aEVs9j8VlcJjw\\n9FH/tc1oWcUTIJRj2T2CzWqZvObmcAKjL1SXFtJ2UwaHMQCmewtW+GB6eWVtz3pg\\n3gSX1G2LPML7t77Xy2AqSFyo3eE8aU0fQnuHj5XoL0hOtkQrOYhFCfp/kwKBgQDJ\\na44rFqe9Y5WevZ5jpaw/Cu44VvKmuxx0MnCtI25mwdAPCdELmUcF9ib8xgsBHRoQ\\n7bOBDuHJk9Bq8F+QRZXZLtiS8WxVY8TtrlL1Z/6/T+SFQwjEnzOh+ooLBk9bIqKY\\nLOCOfLJCHUv/NEiPVrciCn0J/MIdopfBIEe8DJiKCQKBgQCPS3+FVvK6Bs+wxWDo\\nRCczQBmCmFfPCUK0coV1lIzk+IoKYyAM+CpRbJBS8DsUAD4/REQ+u8o0ncKtNJPq\\nq7N3J9cWN/FODkWobaKElapHa455HGdVtdor8tA6jk2k7+QpxmOkChHQyhc3Mw4e\\n1QjyPy74UcTAQMaoajP5/stGeA==\\n-----END PRIVATE KEY-----\\n")
                .setApplicationId("uctregisterbookapp").build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "AnyAppName");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e){
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("AnyAppName"));
        }

    };

*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_admins);

        mContext = getApplicationContext();

        btn1 = (Button) findViewById(R.id.btnAdd);
        txtID  =(EditText) findViewById(R.id.edtSNo);
        txtCellNo =(EditText) findViewById(R.id.edtCellNo);
        txtName  =(EditText) findViewById(R.id.edtName);
        txtSurname =(EditText) findViewById(R.id.edtSurname);
        txtRoomNo =(EditText) findViewById(R.id.edtRoomNo);

        admins = new ArrayList<>();

        floorReps = new ArrayList<>();

        mAuth1 = FirebaseAuth.getInstance();
  //      initFbase();

        spinBlock = findViewById(R.id.BlockNames);
        spinFloor = findViewById(R.id.FloorNo);
        spinType = findViewById(R.id.UserType);

        noFloors.add("1");
        noFloors.add("2");
        noFloors.add("3");
        noFloors.add("4");


        availableFloors.clear();
        availableFloors.add("Blue");
        availableFloors.add("Brown");
        availableFloors.add("Green");
        availableFloors.add("Grey");
        availableFloors.add("Pink");
        availableFloors.add("Yellow");

        DatabaseReference df = FirebaseDatabase.getInstance().getReference("admins");
        df.keepSynced(true);
        df.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        admins.clear();

                        floorReps.clear();


                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()
                        ) {
                            ArrayList<String> x= new ArrayList<>();

                            //loop thru admins tbl
                            if (postSnapshot.exists() ) {
                                admins.add(postSnapshot.child("StudentNo").getValue(String.class));

                                if (postSnapshot.child("UserType").getValue(String.class)!=null) {
                                    if (postSnapshot.child("UserType").getValue(String.class).equals("Floor Rep")) {
                                        x.add(postSnapshot.child("BlockName").getValue(String.class));
                                        x.add(postSnapshot.child("FloorNo").getValue(String.class));
                                        x.add(postSnapshot.child("StudentNo").getValue(String.class));
                                    }
                                }
                                floorReps.add(x);
                            }


                        }
                        resetViews();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );


        btn1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        if (txtCellNo.getText().toString()!="" && txtID.getText().toString()!="" && txtName.getText().toString()!="" && txtRoomNo.getText().toString()!=""&&
                            txtSurname.getText().toString()!="" && spinBlock.getSelectedItem().toString()!="" && spinFloor.getSelectedItem().toString()!="" &&

                            admins.contains(txtID.getText().toString().toUpperCase())==false
                        ){


                            DatabaseReference newAdmin,newUser , df = FirebaseDatabase.getInstance().getReference("");
                            newUser = df.child("users").child(txtID.getText().toString().toUpperCase());
                            newAdmin = df.child("admins").child(txtID.getText().toString().toUpperCase());

                            DatabaseReference sNo, sName, sCellNo, sSurname, sRoomNo, sFloorNo,sBlock;

                            sNo = newAdmin.child("StudentNo");

                            sName = newUser.child("Name");
                            sCellNo = newUser.child("CellNumber");
                            sSurname = newUser.child("Surname");
                            sRoomNo = newUser.child("RoomNo");
                            sFloorNo = newAdmin.child("FloorNo");
                            sBlock = newAdmin.child("BlockName");

                            String x = "Me";
                            if (!spinType.getSelectedItem().toString().equalsIgnoreCase("Floor rep")){
                                x = getFloorRep( spinBlock.getSelectedItem().toString() ,  spinFloor.getSelectedItem().toString());
                            }

                            if (x ==""){
                                Toast.makeText(AddAdmins.this, "No Existing floor rep for this floor! Add one before continuing!", Toast.LENGTH_SHORT).show();
                                System.exit(1);
                            }

                            newUser.child("FloorRep").setValue(x);

                            sNo.setValue(txtID.getText().toString().toUpperCase());
                            newUser.child("StudentNo").setValue(txtID.getText().toString().toUpperCase());

                            sName.setValue(txtName.getText().toString());
                            sCellNo.setValue(txtCellNo.getText().toString());
                            sSurname.setValue(txtSurname.getText().toString());
                            sRoomNo.setValue(txtRoomNo.getText().toString());
                            sFloorNo.setValue(spinFloor.getSelectedItem().toString());
                            sBlock.setValue(spinBlock.getSelectedItem().toString());

                            newAdmin.child("UserType").setValue(spinType.getSelectedItem().toString());

                            newAdmin.push();
                            newUser.push();



                            mAuth1.createUserWithEmailAndPassword(txtID.getText().toString().toUpperCase()+"@myuct.ac.za", "temppass")
                                    .addOnCompleteListener(AddAdmins.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d(TAG, "createUserWithEmail:success "+ mAuth1.getCurrentUser().toString());
                                                FirebaseUser user = mAuth1.getCurrentUser();
                                                user.sendEmailVerification().addOnCompleteListener(
                                                        AddAdmins.this, new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Log.d(TAG, "verification email sent");
                                                            }
                                                        }
                                                );

                                                mAuth1.signOut();




                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                            }

                                            // ...
                                        }
                                    });




                            Toast.makeText(AddAdmins.this, "New Admin created!", Toast.LENGTH_SHORT).show();
                            //finish();




                        }
                        else
                        {
                            Toast.makeText(AddAdmins.this, "There was a problem trying to create new admin. Check that all fields are filled in and that user doesn't already exist!", Toast.LENGTH_LONG).show();
                        }




                    }
                }
        );



        if (mAuth1.getCurrentUser()==null){
            login.signInAgain();
            finish();
        }



    }

    private String getFloorRep(String s1, String s2) {
        for (ArrayList<String> str : floorReps
        ) {

            if (str.get(0).equalsIgnoreCase( s1 ) && str.get(1).equalsIgnoreCase( s2 ) ){
                return str.get(2);
            }

        }

        return "";
    }


    public void resetViews(){

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, availableFloors);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinBlock.setAdapter(adapter);

        setFloors(noFloors);

    }

    public void setFloors(ArrayList<String> x){

        ArrayAdapter<String> r = new ArrayAdapter<>(getApplicationContext() , android.R.layout.simple_spinner_dropdown_item, x);
        r.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinFloor.setAdapter(r);

    }

}
