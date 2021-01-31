package com.commsens.tollgatetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.commsens.tollgatetest.databinding.ActivityMainBinding;
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

    ActivityMainBinding activityMainBinding;

    TollgateLab tollgateLab;
    ArrayList<Tollgate> tollgates;
    VolleyManager volleyManager;
    int routeCount = 0;
    RouteCodeList routeCodeList;
    ArrayList<RouteCodeItem> routeCodeItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.tollgateRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        activityMainBinding.tollgateRecyclerView.setLayoutManager(layoutManager);

        tollgateLab = TollgateLab.getInstance(getApplicationContext());
        tollgates = tollgateLab.getTollgates();

        if(tollgates.size() < 377){
            volleyManager = new VolleyManager(getApplicationContext());
            routeCodeList = new RouteCodeList();
            routeCodeItems = routeCodeList.getRouteCodeItemArrayList();
            getTollgateUnit();
        }else {
            TollgateListAdapter tollgateListAdapter = new TollgateListAdapter(tollgates);
            activityMainBinding.tollgateRecyclerView.setAdapter(tollgateListAdapter);
            activityMainBinding.progressBar.setVisibility(View.GONE);
            activityMainBinding.loadingInfo.setVisibility(View.GONE);
        }
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


    private void getTollgateUnit() {
        RouteCodeItem routeCodeItem = routeCodeItems.get(routeCount);
        connectEXApi(routeCodeItem.getRouteCode());
        Log.i("getTollgateUnit", routeCodeItem.getRouteCode());
    }


    private void connectEXApi(String routeNo){
        Thread connectEXApiThread = new Thread(){
            @Override
            public void run() {
                getJsonUnitLocation(routeNo);
            }
        };
        connectEXApiThread.setDaemon(true);
        connectEXApiThread.start();
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


    private void getUnitJson(JSONObject unitData){
        Gson gson = new Gson();
        ArrayList<Tollgate> getTollgates = new ArrayList<>();
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
                getTollgates.add(tollgate);
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
        Log.e("getTollgates", getTollgates.size() + "");

        for (Tollgate tollgate : getTollgates){
            if(tollgate != null) {
                tollgateLab.jsonTollgateInsert(tollgate);
                Log.e("jsonTollgateInsert", tollgate.toString());
            }
        }

        if(routeCount < routeCodeItems.size()-1){
            routeCount++;
            getTollgateUnit();
            tollgates = tollgateLab.getTollgates();
            activityMainBinding.progressBar.setProgress(tollgates.size());
        } else {
            tollgates = tollgateLab.getTollgates();
            TollgateListAdapter tollgateListAdapter = new TollgateListAdapter(tollgates);
            activityMainBinding.tollgateRecyclerView.setAdapter(tollgateListAdapter);
            activityMainBinding.progressBar.setVisibility(View.GONE);
            activityMainBinding.loadingInfo.setVisibility(View.GONE);
        }


    }





//    public void getUnitLocationInfo(String routeNo){
//
//        String url = EX_API_URL +
//                "key=" + EX_API_KEY + "&" +
//                "type=" + EX_API_TYPE + "&" +
//                "routeNo=" + routeNo + "&" +
//                "numOfRows=" + EX_API_NUM_OF_ROWS + "&" +
//                "pageNo=" + EX_API_PAGE_NO;
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
////                        Log.i(TAG, response.toString());
//                        getUnitString(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG, error.toString());
//                    }
//                }){
//        };
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                6000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
//        );
//
//        volleyManager.addToRequestQueue(stringRequest);
//    }



//        private void getUnitString(String unitData){
//
//        ArrayList<Tollgate> arrayList = new ArrayList<>();
//        Gson gson = new Gson();
//        int count = 0;
//        int pageNo = 0;
//        int numOfRows = 0;
//        int pageSize = 0;
//        String message = "";
//        String code = "";
//
//        try {
//            JSONObject jsonObject = new JSONObject(unitData);
//            count = jsonObject.getInt("count");
//            pageNo = jsonObject.getInt("pageNo");
//            numOfRows = jsonObject.getInt("numOfRows");
//            pageSize = jsonObject.getInt("pageSize");
//            message = jsonObject.getString("message");
//            code = jsonObject.getString("code");
//
//            JSONArray jsonArray = jsonObject.getJSONArray("list");
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                Tollgate tollgate = gson.fromJson(jsonArray.get(i).toString(), Tollgate.class);
//                arrayList.add(tollgate);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        String tollgateInfo = "{" +
//                                    "count='" + count + '\'' +
//                                    ", pageNo='" + pageNo + '\'' +
//                                    ", numOfRows='" + numOfRows + '\'' +
//                                    ", pageSize='" + pageSize + '\'' +
//                                    ", message='" + message + '\'' +
//                                    ", code='" + code +
//                                    '}';
//
//        Log.w("Tollgate List Info", tollgateInfo);
//
//        for (Tollgate tollgate : arrayList){
//            tollgateLab.jsonTollgateInsert(tollgate);
//            Log.e(tollgate.getUnitName(), tollgate.toString());
//        }
//
//        if(routeCount > routeCodeItems.size()){
//            routeCount++;
//            getTollgateUnit(routeCount);
//        }
//    }


}