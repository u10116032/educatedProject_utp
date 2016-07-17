package com.example.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by halley on 2016/6/23.
 */
public class DbUpload {
    private final String SERVER_URL = "http://163.21.245.192/u10116032/UploadToServer.php";
    private final String INSERT_URL = "http://163.21.245.192/u10116032/insert.php";
    private final String HOMEWORK_URL = "http://163.21.245.192/u10116032/getHomework.php";


    private int serverResponseCode = 0;
    private RequestQueue requestQueue;
    private String result;
    private Boolean upLoadSuccess=false;
    private String user;
    private String photo_dir,rec_dir;
    private Geocoder geocoder ;
    private StringRequest homeworkRequest;
    private Context context;
    private HttpURLConnection mUrlConnection;
    private String homework;



    public DbUpload( Context context, String user){
        this.context = context;
        this.requestQueue  = Volley.newRequestQueue(context);
        this.user = user;
        this.geocoder = new Geocoder(context, Locale.TAIWAN);
    }

    private String upLoadFile(final String fileUri){


        Runnable imageThread = new Runnable() {

            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                DataOutputStream dataOutputStream = null;
                File sourceFile = new File(fileUri);
                String fileName = sourceFile.getName();
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";

                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;

                try {
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(SERVER_URL);

                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    httpURLConnection.setRequestProperty("uploaded_file", fileName);

                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                    dataOutputStream.writeBytes(lineEnd);

                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        dataOutputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    serverResponseCode = httpURLConnection.getResponseCode();
                    StringBuilder stringBuilder = new StringBuilder();
                    //String serverResponseMessage = httpURLConnection.getResponseMessage();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    while ((result = bufferedReader.readLine()) != null) {
                        stringBuilder.append(result);
                    }
                    result = stringBuilder.toString();
                    httpURLConnection.disconnect();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(imageThread);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return result;

    }

    public boolean upLoad(final Item item){
        if(item.getPhotodir() != null)
            photo_dir = "http://163.21.245.192/u10116032/" + upLoadFile(item.getPhotodir());
        if(item.getRecorddir() != null)
            rec_dir = "http://163.21.245.192/u10116032/" + upLoadFile(item.getRecorddir());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                List<Address> address = null;
                try {
                    address = geocoder.getFromLocation(item.getLatitude(), item.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String location = address.get(0).getCountryName()+address.get(0).getAdminArea()+address.get(0).getLocality()+address.get(0).getThoroughfare()+address.get(0).getFeatureName();

                Map<String, String> parameters = new HashMap<String,String>();
                parameters.put("date",item.getDate());
                parameters.put("title",item.getTitle());
                parameters.put("content",item.getContent());
                parameters.put("longitude", String.valueOf(item.getLongitude()));
                parameters.put("latitude", String.valueOf(item.getLatitude()));
                parameters.put("photo_dir", photo_dir);
                parameters.put("rec_dir", rec_dir);
                parameters.put("category", item.getCategory());
                parameters.put("user", user);
                parameters.put("address", location);


                return parameters;
            }
        };
        requestQueue.add(stringRequest);

        if(upLoadFile(item.getPhotodir()) != null)
            upLoadSuccess = true;
        return upLoadSuccess;
    }

    public String getHomework() {

        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(HOMEWORK_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((homework = bufferedReader.readLine()) != null) {
                        stringBuilder.append(homework);
                    }
                    homework = stringBuilder.toString();
                    connection.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(run);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return homework;
    }




}
