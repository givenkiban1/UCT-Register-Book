package com.example.uctregisterbookapp;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.uctregisterbookapp.NetworkUtil;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    public static TextView BarcodeVal;
    public static Application appMain;
    private int noPressed;
    private static boolean doubleBackToExitPressedOnce = false;
    public static List<List<String>> user;

    public static List<String> uDetails = new ArrayList<>();
    private FirebaseAuth mAuth;

    public static List<List<String>> getUser(){
        return user;
    }

    private static final String TAG = "Main2Activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();

        /*
        NetworkUtil k = new NetworkUtil();
        if (k.getConnectivityStatusString(getApplicationContext())){
            Toast.makeText(Main2Activity.this, "there's connnection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(Main2Activity.this, "there's no connnection", Toast.LENGTH_SHORT).show();
        }

        */
        //db stuff

        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            /**
             * Internet is available, Toast It!
             */
            Toast.makeText(getApplicationContext(), "WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
        } else {
            /**
             * Internet is NOT available, Toast It!
             */
            Toast.makeText(getApplicationContext(), "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
        }

        BarcodeVal = (TextView) findViewById(R.id.textView1);

        appMain = getApplication();

        Button btn = (Button) findViewById(R.id.button); //button for scanner just for fun
        Button btnManageStudents = (Button) findViewById(R.id.btnManageStudents); //button for viewing records and editing/adding them

        Button btnManageLoans = (Button) findViewById(R.id.btnManageLoans); //button for viewing records and editing/adding them of loaned items

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getApplicationContext() , ScannerPage.class));
                        ScannerPage.setControlVar(1);

                    }
                }
        );

        btnManageStudents.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getApplicationContext() , ManageUsers.class));
                        //ScannerPage.setControlVar(2);

                    }
                }
        );


        btnManageLoans.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getApplicationContext() , ManageLoans.class));

                    }
                }
        );


        Button btnLogOut = findViewById(R.id.btnLogout);

        btnLogOut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //confirm...
                        //make a class for alert dialog and notifications ...



                        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);

                        builder.setMessage("Are you sure you want to log out ?")
                                .setTitle("Log Out Confirmation");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                login.logOut();
                                startActivity(new Intent(getApplicationContext() , login.class));
                                finish();

                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                //do nothing.
                            }
                        });

                        //have the option to reset password... when i add actionbar

                        AlertDialog dialog = builder.create();

                        dialog.show();


                    }
                }
        );


        user =  login.getUserDetails();

        if (user.size()!=0){

            Log.d(TAG, "onCreate: "+user);

            TextView tv = navigationView.getHeaderView(0).findViewById(R.id.loggedInAdmin);

            int i = 0;
            tv.setText(user.get(i).get(i));

            if (!user.get(i).get(i).toUpperCase().equals("KBNGIV001@MYUCT.AC.ZA")){
                navigationView.getMenu().getItem(1).setVisible(false);
            }
            else
            {
                navigationView.getMenu().getItem(1).setVisible(true);
            }

        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else

            {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setChecked(false);

        if (id == R.id.nav_home) {
            // Handle the camera action
            startActivity(new Intent(getApplicationContext() , ScannerPage.class));
            ScannerPage.setControlVar(1);


        }

        else if (id == R.id.nav_add) {

            //open add admin activity
            startActivity(new Intent(getApplicationContext() , AddAdmins.class));
        }

        else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

            //reset password!

            mAuth.sendPasswordResetEmail(user.get(0).get(0)).addOnSuccessListener(
                    Main2Activity.this,
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(Main2Activity.this, "Password reset Email sent to "+ user.get(0).get(0), Toast.LENGTH_SHORT).show();

                        }
                    }
            );


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
