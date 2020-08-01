package com.example.uctregisterbookapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBManager {

    private DatabaseReference database;

    private DatabaseReference sRoot,sNo, sName, sCellNo, sSurname, sRoomNo, ran, byWhom;
    private String key = "";

    private DatabaseReference dateOut, dateIn, lsNo, lItems;

    public DBManager(String sID){
        database = FirebaseDatabase.getInstance().getReference("users");
        //database.goOnline();

        //Acquiring the fields
        sRoot = database.child(sID.toUpperCase());

        //key = sRoot.push().getKey();


        sNo = sRoot.child("StudentNo");
        sName = sRoot.child("Name");
        sCellNo = sRoot.child("CellNumber");
        sSurname = sRoot.child("Surname");
        sRoomNo = sRoot.child("RoomNo");
    }

    public DBManager(boolean x , String rec){

        database = FirebaseDatabase.getInstance().getReference("loans");

        if (!x){
            key = database.push().getKey();
        }
        else
        {
            key = rec;
        }


        sRoot = database.child(key);

        dateIn = sRoot.child("DateReturned");
        dateOut = sRoot.child("DateLoaned");
        lsNo = sRoot.child("StudentNo");
        lItems = sRoot.child("ItemsTaken");
        ran = sRoot.child("RecordId");
        byWhom = sRoot.child("ByWhom");
    }


    public void AddUser(String uID, String uName, String uSurname, String uCellNo, String uRoomNo){
        //sNo.setValue(uID.toUpperCase());
        sName.setValue(uName);
        sCellNo.setValue(uCellNo);
        sSurname.setValue(uSurname);
        sRoomNo.setValue(uRoomNo);
        if (Main2Activity.user.get(0).get(4).equalsIgnoreCase("assistant")){
            sRoot.child("FloorRep").setValue(Main2Activity.user.get(1).get(1));
        }
        else {
            sRoot.child("FloorRep").setValue(Main2Activity.user.get(0).get(3));
        }
        sRoot.push();
    }

    public void editUser(String uID, String uName, String uSurname, String uCellNo, String uRoomNo){
        //sNo.setValue(uID);
        sName.setValue(uName);
        sCellNo.setValue(uCellNo);
        sSurname.setValue(uSurname);
        sRoomNo.setValue(uRoomNo);

        sRoot.push();
    }

    public void CloseAll(){
        //database.goOffline();
    }


    public void makeBooking(String sdateIn, String sdateOut, String ssNo, String sItems){

        dateIn.setValue(sdateIn);
        dateOut.setValue(sdateOut);
        lsNo.setValue(ssNo.toUpperCase());
        lItems.setValue(sItems);
        ran.setValue(key);
        int i = 0;
        if (Main2Activity.getUser().size()>=4){
            i = 3;
        }

        byWhom.setValue(Main2Activity.user.get(0).get(0));
        sRoot.push();
    }
//all of this is redundant. i will either make only one methos called booking that does both edit and add.. replacing existing
//or i will keep it as is, but make tweeks like user already exists, etc...
//also remember to add that are you sure u wish to delete or edit record,,, onnly for user details tho... not records

    public void editBooking(String sdateIn, String sdateOut, String ssNo, String sItems){

        dateIn.setValue(sdateIn);
        dateOut.setValue(sdateOut);
        lsNo.setValue(ssNo);
        lItems.setValue(sItems);

        sRoot.push();
    }

    /*
    public void fetchAll(){
        //
        database.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        /*for (DataSnapshot:
                             ) {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }*/
}
