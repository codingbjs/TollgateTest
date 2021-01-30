package com.commsens.tollgatetest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ExApiHttpConnect {

//    key   	string	필수	발급받은 인증키
//    type  	string	필수	검색결과 포맷
//    routeNo	string	선택	노선코드
//    unitCode	string	선택	영업소코드
//    numOfRows	string	선택	한 페이지당 출력건수
//    pageNo	string	선택	출력 페이지번호

    private final String TAG = "ExApiHttpConnect";

    private final String EX_API_URL = "http://data.ex.co.kr/openapi/locationinfo/locationinfoUnit?";
    private final String EX_API_KEY = "9207755269";
    private final String EX_API_TYPE = "json";
    private final String EX_API_NUM_OF_ROWS = "100";
    private final String EX_API_PAGE_NO = "1";

    VolleyManager volleyManager;


    public ExApiHttpConnect(Context context) {
        volleyManager = new VolleyManager(context);
    }


    public String getLocationInfoUnitHttp(String routeNo){
        try {
            String urlString = EX_API_URL +
                    "key=" + EX_API_KEY + "&" +
                    "type=" + EX_API_TYPE + "&" +
                    "routeNo=" + routeNo + "&" +
                    "numOfRows=" + EX_API_NUM_OF_ROWS + "&" +
                    "pageNo=" + EX_API_PAGE_NO;

            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDefaultUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Content-Type","application/json");
            httpURLConnection.setRequestProperty("Accept","application/json");
            httpURLConnection.setRequestProperty("Accept-Charset","UTF-8");
            httpURLConnection.setConnectTimeout(10000);


            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(httpURLConnection.getOutputStream(),
                            StandardCharsets.UTF_8);

//            PrintWriter printWriter = new PrintWriter(outputStreamWriter);
//            printWriter.write(postData.toString());
//            printWriter.flush();
//            Log.i("Http Write", postData.toString());

            InputStreamReader inputStreamReader =
                    new InputStreamReader(httpURLConnection.getInputStream(),
                            StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder getUnitLocation = new StringBuilder();
            String readUnit;

            while ((readUnit = bufferedReader.readLine()) != null){
                getUnitLocation.append(readUnit);
            }

            Log.i("Http Read", getUnitLocation.toString());

            return getUnitLocation.toString();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void getUnitLocationInfoVolley(String routeNo){
        StringBuilder url = new StringBuilder();
        url.append(EX_API_URL);
        url.append("key=").append(EX_API_KEY).append("&");
        url.append("type=").append(EX_API_TYPE).append("&");
        url.append("routeNo=").append(routeNo).append("&");
        url.append("numOfRows=").append(EX_API_NUM_OF_ROWS).append("&");
        url.append("pageNo=").append(EX_API_PAGE_NO);

        StringBuilder getUnitLocation = new StringBuilder();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response.toString());
                        getUnitLocation.append(response);
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
