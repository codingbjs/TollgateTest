package com.commsens.tollgatetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        connectEXApi("001");
//        getUnitLocationInfo("001");  <uses-library android:name="org.apache.http.legacy" android:required="false"/>
//        getJsonUnitLocation("100"); android:usesCleartextTraffic="true"
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


    public void connectEXApi(String routeNo){
        Thread connectThread = new Thread(){
            @Override
            public void run() {
//                getUnitLocationInfo(routeNo);
                getJsonUnitLocation(routeNo);
            }
        };

        connectThread.setDaemon(true);
        connectThread.start();
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
//                        Log.i(TAG, response.toString());
                        getUnitString(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }){
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        volleyManager.addToRequestQueue(stringRequest);
    }


    private void getJsonUnitLocation(String routeNo){
        String url = EX_API_URL +
                "key=" + EX_API_KEY + "&" +
                "type=" + EX_API_TYPE + "&" +
                "routeNo=" + routeNo + "&" +
                "numOfRows=" + EX_API_NUM_OF_ROWS + "&" +
                "pageNo=" + EX_API_PAGE_NO;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.e(TAG,response.toString());
                        getUnitJson(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG,error.toString());
                    }
                });

        volleyManager.addToRequestQueue(jsonObjectRequest);
    }


    private void getUnitString(String unitData){

        ArrayList<Tollgate> arrayList = new ArrayList<>();
        Gson gson = new Gson();
        int count = 0;
        int pageNo = 0;
        int numOfRows = 0;
        int pageSize = 0;
        String message = "";
        String code = "";

        try {
            JSONObject jsonObject = new JSONObject(unitData);
            count = jsonObject.getInt("count");
            pageNo = jsonObject.getInt("pageNo");
            numOfRows = jsonObject.getInt("numOfRows");
            pageSize = jsonObject.getInt("pageSize");
            message = jsonObject.getString("message");
            code = jsonObject.getString("code");

            JSONArray jsonArray = jsonObject.getJSONArray("list");

            for (int i = 0; i < jsonArray.length(); i++) {
                Tollgate tollgate = gson.fromJson(jsonArray.get(i).toString(), Tollgate.class);
                arrayList.add(tollgate);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tollgateInfo = "{" +
                                    "count='" + count + '\'' +
                                    ", pageNo='" + pageNo + '\'' +
                                    ", numOfRows='" + numOfRows + '\'' +
                                    ", pageSize='" + pageSize + '\'' +
                                    ", message='" + message + '\'' +
                                    ", code='" + code +
                                    '}';

        Log.w("Tollgate List Info", tollgateInfo);

        for (Tollgate tollgate : arrayList){
            Log.e(tollgate.getUnitName(), tollgate.toString());
        }
    }



    private void getUnitJson(JSONObject unitData){

        ArrayList<Tollgate> arrayList = new ArrayList<>();
        Gson gson = new Gson();
        int count = 0;
        int pageNo = 0;
        int numOfRows = 0;
        int pageSize = 0;
        String message = "";
        String code = "";

        try {

            count = unitData.getInt("count");
            pageNo = unitData.getInt("pageNo");
            numOfRows = unitData.getInt("numOfRows");
            pageSize = unitData.getInt("pageSize");
            message = unitData.getString("message");
            code = unitData.getString("code");

            JSONArray jsonArray = unitData.getJSONArray("list");

            for (int i = 0; i < jsonArray.length(); i++) {
                Tollgate tollgate = gson.fromJson(jsonArray.get(i).toString(), Tollgate.class);
                arrayList.add(tollgate);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tollgateInfo = "{" +
                "count='" + count + '\'' +
                ", pageNo='" + pageNo + '\'' +
                ", numOfRows='" + numOfRows + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", message='" + message + '\'' +
                ", code='" + code +
                '}';

        Log.w("Tollgate List Info", tollgateInfo);

        for (Tollgate tollgate : arrayList){
            Log.e(tollgate.getUnitName(), tollgate.toString());
        }
    }

}