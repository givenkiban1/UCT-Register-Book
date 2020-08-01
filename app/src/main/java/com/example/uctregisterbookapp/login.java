package com.example.uctregisterbookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class login extends AppCompatActivity {

    private static FirebaseAuth mAuth;
    private static final String TAG = "login";
    private Button signIn;
    private EditText uEmail, uPass;

    //
    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";

    private static final String DefaultUnameValue = "";
    private static String UnameValue;

    private static final String DefaultPasswordValue = "";
    private static String PasswordValue;


    private static  Context mContext;
        //


    private static void savePreferences(String email,  String pass) {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = email;
        PasswordValue = pass;
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.commit();
    }


    public static void signInAgain(){
        mAuth.signInWithEmailAndPassword(loadPreferences().get(0) , loadPreferences().get(1));
    }

    public static ArrayList<String> loadPreferences() {

        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);

        ArrayList<String> x = new ArrayList<>();
        x.add(UnameValue);
        x.add(PasswordValue);

        return x;
    }






    public static void logOut(){
        mAuth.signOut();
        savePreferences("", "");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mContext = getApplicationContext();
        signIn = findViewById(R.id.login);
        uEmail = findViewById(R.id.username);
        uPass = findViewById(R.id.password);

        signIn.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!(uPass.getText().toString().isEmpty() & uEmail.getText().toString().isEmpty())){
                            makeSignIn(uEmail.getText().toString(), uPass.getText().toString());
                        }
                        else
                        {
                            Toast.makeText(login.this, "You need to enter your username and password!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

        );



    }

    private void makeSignIn(final String email, final String pass) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            savePreferences(email , pass);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "The Password or Email you've entered is incorrect.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d(TAG, "onStart: "+ currentUser);
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        //
        if (currentUser!=null){
            //Toast.makeText(this, "Already signed in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext() , Main2Activity.class));
            finish();
        }
    }

    public static List<List<String>> getUserDetails(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final List<String> strings, st2;
        st2 = new ArrayList<>();
        strings= new ArrayList<>();

        DatabaseReference xx,u, df = FirebaseDatabase.getInstance().getReference("admins");
        df.keepSynced(true);
        final List<List<String>> x = new ArrayList<>();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            
            //u = df.child( email.substring(0 , email.indexOf("@")).toUpperCase() );

            final String iid= email.substring(0 , email.indexOf("@")).toUpperCase();

            xx = FirebaseDatabase.getInstance().getReference("users");
            xx.keepSynced(true);

            strings.add(email);


            df.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()
                            ) {
                                if (postSnapshot.exists()) {

                                    if (postSnapshot.child("StudentNo").getValue(String.class) != null) {

                                        if (postSnapshot.child("StudentNo").getValue(String.class).equalsIgnoreCase(iid)) {
                                        Log.d(TAG, "onDataChange: " + postSnapshot);

                                            strings.add(postSnapshot.child("BlockName").getValue(String.class));

                                            strings.add(postSnapshot.child("FloorNo").getValue(String.class));
                                            strings.add(postSnapshot.child("StudentNo").getValue(String.class));
                                            strings.add(postSnapshot.child("UserType").getValue(String.class));


                                        }



                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }
            );


            xx.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()
                            ) {

                                if (dataSnapshot.exists()) {

                                    if (postSnapshot.child("StudentNo").getValue(String.class) != null) {
                                        if (postSnapshot.child("StudentNo").getValue(String.class).equalsIgnoreCase(iid)) {

                                            st2.add( postSnapshot.child("RoomNo").getValue(String.class) );
                                            st2.add( postSnapshot.child("FloorRep").getValue(String.class) );
                                            st2.add( postSnapshot.child("CellNumber").getValue(String.class) );
                                            st2.add( postSnapshot.child("Name").getValue(String.class) );
                                            st2.add( postSnapshot.child("Surname").getValue(String.class) );

                                        }


                                    }

                                }

                            }
                        }




                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }
            );


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    Main2Activity.user = x;
                }
            }, 3000);


        }

        x.add(strings);
        x.add(st2);

        return x;
    }


}
