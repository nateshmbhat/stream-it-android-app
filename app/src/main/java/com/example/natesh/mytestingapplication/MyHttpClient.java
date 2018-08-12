package com.example.natesh.mytestingapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MyHttpClient {

    private final OkHttpClient httpclient = new OkHttpClient();
    ServerResponseHandler respHandler  ;
    Context context ;

    MyHttpClient(Context context){
        this.context = context ;
        respHandler = new ServerResponseHandler(context) ;
    }


    public void makeRequest(String url , String requestType) throws IOException {
        Log.d("testing" , "Reqeusting = " + url) ;
        Request request = new Request.Builder()
                .url(url)
                .build();

        httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("testing" , "ERROR OCCURED ! " ) ;
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        Log.d("testing" , responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    String responseString = responseBody.string() ;

                    Log.d("testing" , " \n\nResponse body : \n" +  responseString ) ;

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(requestType.equals("getSongs"))
                            {
                                respHandler.handleGetAllSongs(responseString);
                            }
                            else if(requestType.equals("getSongInfo"))
                            {
                                respHandler.handleSongInfo(responseString);
                            }
                        }
                    });
                }
            }
        }) ;
    }
}
