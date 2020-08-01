package com.example.uctregisterbookapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.uctregisterbookapp.NetworkUtil;

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            boolean IsConnected = NetworkUtil.getConnectivityStatusString(context);
            // Toast in here, you can retrieve other value like String from NetworkUtil
            // but you need some change in NetworkUtil Class

            if (IsConnected){
                Toast.makeText(context, "There's internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
