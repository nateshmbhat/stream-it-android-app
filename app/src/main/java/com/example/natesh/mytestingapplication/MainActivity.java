package com.example.natesh.mytestingapplication;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    Button streamBtn , playButton , pauseButton , getAllSongs ;
    MyMediaPlayer myMediaPlayer  ;
    ListView allSongsListView ;
    MyHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeAllObjects();
        setupAllListeners();
    }


    void initializeAllObjects(){
        myMediaPlayer = new MyMediaPlayer(this) ;
        httpClient = new MyHttpClient(this) ;
        setContentView(R.layout.activity_main);
        streamBtn = findViewById(R.id.audioStreambtn);
        playButton= findViewById(R.id.playMusicButton);
        pauseButton= findViewById(R.id.pauseMusicButton);
        getAllSongs= findViewById(R.id.getAllSongs);
        allSongsListView = findViewById(R.id.allSongsListView) ;
    }




    void setupAllListeners(){

        streamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.startMusicFromURL("http://192.168.0.100:8090/test");
            }
        });


        getAllSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    httpClient.getAllSongs("http://192.168.0.100:8090/getSongs") ;
//                    Log.d("testing" ,"Server response : " +  response) ;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.resumeMusic();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.pauseMusic();
            }
        }) ;

        allSongsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("testing" , adapterView.getItemAtPosition(i).toString()) ;
            }
        });
    }

}
