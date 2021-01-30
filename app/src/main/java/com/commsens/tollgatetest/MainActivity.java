package com.commsens.tollgatetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private final String EX_API_URL = "http://data.ex.co.kr/openapi/locationinfo/locationinfoUnit?";
    private final String EX_API_KEY = "9207755269";
    private final String EX_API_TYPE = "json";
    private final String EX_API_NUM_OF_ROWS = "100";
    private final String EX_API_PAGE_NO = "1";

    TollgateLab tollgateLab;
    ArrayList<Tollgate> tollgates;
    VolleyManager volleyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tollgateLab = TollgateLab.getInstance(getApplicationContext());
        tollgates = tollgateLab.getTollgates();
        volleyManager = new VolleyManager(getApplicationContext());
        getUnitLocationInfo("100");
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

    public void getUnitLocationInfo(String routeNo){

        String url = EX_API_URL +
                "key=" + EX_API_KEY + "&" +
                "type=" + EX_API_TYPE + "&" +
                "routeNo=" + routeNo + "&" +
                "numOfRows=" + EX_API_NUM_OF_ROWS + "&" +
                "pageNo=" + EX_API_PAGE_NO;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }){
        };
        volleyManager.addToRequestQueue(stringRequest);
    }


}