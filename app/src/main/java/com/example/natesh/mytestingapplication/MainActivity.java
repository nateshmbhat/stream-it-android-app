package com.example.natesh.mytestingapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaMetadata;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button streamBtn , playButton , pauseButton , getAllSongs ;
    MyMediaPlayer myMediaPlayer  ;
    ListView allSongsListView ;
    ProgressDialog progressDialog;
    MyHttpClient httpClient;
    String sHostWithPort ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeAllObjects();
        setupAllListeners();
    }


    void initializeAllObjects(){
        sHostWithPort = "http://192.168.0.100:8090" ;

        progressDialog = new ProgressDialog(this) ;
        progressDialog.setMessage("Streaming...");

        myMediaPlayer = new MyMediaPlayer(this , progressDialog) ;
        httpClient = new MyHttpClient(this) ;
        setContentView(R.layout.activity_main);
        getAllSongs= findViewById(R.id.getAllSongs);
        allSongsListView = findViewById(R.id.allSongsListView) ;
    }




    void setupAllListeners(){

        getAllSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    httpClient.makeRequest(sHostWithPort + "/getSongs" , "getSongs"); ;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        allSongsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) adapterView.getItemAtPosition(i);
                Log.d("file" , song.getFullPathName()) ;

                List<String> allSongs = new ArrayList<String>() ;

                for(int pos =0 , n = allSongsListView.getAdapter().getCount(); pos< n  ; pos++)
                {
                   allSongs.add(((Song)adapterView.getItemAtPosition(pos)).getFullPathName());
                }

                startActivity(new Intent( MainActivity.this , ActivitySingleMusicView.class)
                        .putExtra("songPath" , song.getFullPathName() )
                        .putStringArrayListExtra("allSongs" , (ArrayList<String>) allSongs)
                );

            }
        });
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }

}
