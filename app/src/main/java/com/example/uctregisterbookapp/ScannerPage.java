package com.example.uctregisterbookapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission_group.CAMERA;

public class ScannerPage extends AppCompatActivity implements ZXingScannerView.ResultHandler
{

    private ZXingScannerView sView;
    private static int controlVar =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sView = new ZXingScannerView(this);
        setContentView(sView);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){

            if (checkPermission()){
                Toast.makeText(getApplicationContext() , "permission is granted", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }


        sView.startCamera();
    }

    /*
  */

    public static void setControlVar(int x){
        controlVar = x;
    }

    private boolean checkPermission(){
        return (
                ContextCompat.checkSelfPermission(Main2Activity.appMain , Manifest.permission.CAMERA)
                )==(
                PackageManager.PERMISSION_GRANTED
                );
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this , new String[]{CAMERA}, 1);
    }

    public void onRequestPermissionsResult(int requestCode , String permission[], int grantResults[]){
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted) {
                        Toast.makeText(getApplicationContext(), "Camera permission granted!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "Camera Permission denied!", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if (shouldShowRequestPermissionRationale(CAMERA)){

                                //allow user to give permission...
                                displayADiag("Do you allow us to use your camera ?",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{CAMERA} , 1);
                                            }
                                        });
                                return ;

                            }
                        }
                    }
                }
                break;
        }
    }

    public void displayADiag(String message, DialogInterface.OnClickListener Listener){

        new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("OK", Listener)
                .setNegativeButton("NO", null)
                .create()
                .show();


    }

    @Override
    public void handleResult(Result result) {
        goBack(result.getText());
    }

    @Override
    public void onResume() {
        super.onResume();

/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission()){
                if (sView ==null){
                    sView = new ZXingScannerView(this);
                    setContentView(sView);
                }

                sView.setResultHandler(this);
                sView.startCamera();
            }

            else
            {
                requestPermission();
            }
        }
*/
        sView.setResultHandler(this);
        sView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        sView.stopCamera();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        sView.stopCamera();
    }

    public void goBack(String result){
        switch (controlVar){
            case 1:
                Main2Activity.BarcodeVal.setText(result);
                //onBackPressed();
                finish();
                break;
            case 2:
            {
                startActivity(new Intent(getApplicationContext() , AddUsers.class));
                AddUsers.setControlVar(1);
                AddUsers.sID =  (result);
                finish();

            }
            break;

            case 3:
            {
                //onBackPressed();
                finish();
                ManageLoans.setStudentID(result);

            }
            break;
        }

        controlVar = 0;
    }
}
