package com.commsens.tollgatetest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ExApiHttpConnect {

//    key   	string	필수	발급받은 인증키
//    type  	string	필수	검색결과 포맷
//    routeNo	string	선택	노선코드
//    unitCode	string	선택	영업소코드
//    numOfRows	string	선택	한 페이지당 출력건수
//    pageNo	string	선택	출력 페이지번호


    private final String EX_API_KEY = "test";
    private final String EX_API_URL = "http://data.ex.co.kr/openapi/locationinfo/locationinfoUnit";
    private final String EX_API_URL_GET = "http://data.ex.co.kr/openapi/locationinfo/locationinfoUnit?key=9207755269&type=json&routeNo=100&numOfRows=10&pageNo=1";



    StringBuilder postData = new StringBuilder();


    public String getLocationInfoUnit(String routeNo){
        try {
            postData.append("key").append("=").append(EX_API_KEY).append("&");
            postData.append("type").append("=").append("json").append("&");
            postData.append("routeNo").append("=").append(routeNo).append("&");
//            postData.append("unitCode").append("=").append(EX_API_KEY).append("&");
            postData.append("numOfRows").append("=").append("100").append("&");
            postData.append("pageNo").append("=").append("1");


            URL url = new URL(EX_API_URL_GET);
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

            PrintWriter printWriter = new PrintWriter(outputStreamWriter);
            printWriter.write(postData.toString());
            printWriter.flush();
            Log.i("Http Write", postData.toString());

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

}
