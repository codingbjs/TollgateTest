package com.commsens.tollgatetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    TollgateLab tollgateLab;
    ArrayList<Tollgate> tollgates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tollgateLab = TollgateLab.getInstance(getApplicationContext());
        tollgates = tollgateLab.getTollgates();
        Log.e("tollgates", tollgates.size() + "");
        getUnit("100");
    }

    private void getUnit(String routeNo){
        Thread getUnitLocation = new Thread(){
            @Override
            public void run() {
                ExApiHttpConnect exApiHttpConnect = new ExApiHttpConnect();
                exApiHttpConnect.getLocationInfoUnit("100");
            }
        };

        getUnitLocation.setDaemon(true);
        getUnitLocation.start();
    }


    private class TollgateDistanceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Tollgate tollgate = intent.getParcelableExtra(Tollgate.TOLLGATE_ALMOST);
            if(tollgate != null){
                Toast.makeText(getApplicationContext(), String.valueOf(tollgate.getAlmost()),Toast.LENGTH_LONG).show();
            }
        }
    }
}