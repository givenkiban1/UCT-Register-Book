package com.example.uctregisterbookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUsers extends AppCompatActivity {

    private static int controlVar =0;
    private static Button btn2, btn1;
    public static EditText txtID,txtName,txtSurname,txtCellNo, txtRoomNo;
    public static String sID, sName, sSurname, sCellNo, sRoomNo;
    private DBManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);
        btn1 = (Button) findViewById(R.id.btnUpdate);
        btn2 = (Button) findViewById(R.id.btnAdd);
        txtID  =(EditText) findViewById(R.id.edtSNo);
        txtCellNo =(EditText) findViewById(R.id.edtCellNo);
        txtName  =(EditText) findViewById(R.id.edtName);
        txtSurname =(EditText) findViewById(R.id.edtSurname);
        txtRoomNo =(EditText) findViewById(R.id.edtRoomNo);

        btn2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db = new DBManager(txtID.getText().toString());
                        db.AddUser(txtID.getText().toString() , txtName.getText().toString(), txtSurname.getText().toString(), txtCellNo.getText().toString(), txtRoomNo.getText().toString());
// AddUser(String uID, String uName, String uSurname, String uCellNo, String uRoomNo)
                        //db.CloseAll();
                        //db = null;
                        Toast.makeText(getApplicationContext(), "USER HAS BEEN ADDED!", Toast.LENGTH_SHORT).show();
                        goBack();
                    }
                }
        );

        btn1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db = new DBManager(txtID.getText().toString());
                        db.editUser(txtID.getText().toString() , txtName.getText().toString(), txtSurname.getText().toString(), txtCellNo.getText().toString(), txtRoomNo.getText().toString());
                        //db = null;
                        Toast.makeText(getApplicationContext(), "USER HAS BEEN UPDATED!", Toast.LENGTH_SHORT).show();
                        goBack();
                    }
                }
        );


        if (controlVar==1){//meaning add user
            btn2.setVisibility(View.VISIBLE);
            txtID.setText(sID);

        }
        else///////
        if (controlVar == 2){
            //meaning update user
            btn1.setVisibility(View.VISIBLE);
            iniTxts();
        }



    }


    public static void setControlVar(int x){
        controlVar = x;
    }

    public static void iniTxts(){
        txtSurname.setText(sSurname);
        txtID.setText(sID);
        txtRoomNo.setText(sRoomNo);
        txtCellNo.setText(sCellNo);
        txtName.setText(sName);
    }
    public void goBack(){
        //based on control var


        btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);
        controlVar = 0;
        /*
        sID = sCellNo = sRoomNo =sName = sSurname = "";
        txtRoomNo.setText("");
        txtID.setText("");
        txtSurname.setText();
        */
        //startActivity(new Intent(getApplicationContext() , ManageUsers.class));

        finish();

    }
}
