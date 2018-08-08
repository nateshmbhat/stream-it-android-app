package com.example.natesh.mytestingapplication;

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

    public void getAllSongs(String url) throws IOException {
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

                    Log.d("testing" , " \n\nResponse body : \n" +  responseBody.string());
                }
            }
        }) ;
    }
}
