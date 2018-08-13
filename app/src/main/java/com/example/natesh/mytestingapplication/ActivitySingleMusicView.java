package com.example.natesh.mytestingapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.net.URLEncoder;

public class ActivitySingleMusicView extends AppCompatActivity {

    Button streamBtn , playButton , pauseButton , getAllSongs ;
    MyMediaPlayer myMediaPlayer  ;
    ListView allSongsListView ;
    Song currentSong ;
    ProgressDialog progressDialog;
    MyHttpClient httpClient;
    String sHostWithPort ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_song_layout);
        initializeAllObjects();
        setupAllListeners();

        String songPath  = getIntent().getStringExtra("songPath") ;
        currentSong.setFullPathName(songPath);

        try {
            myMediaPlayer.loadMusicFromRemoteFilePath( sHostWithPort , currentSong);
            httpClient.getSongInfo(currentSong); ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void initializeAllObjects(){
        sHostWithPort = "http://192.168.0.100:8090" ;
        progressDialog = new ProgressDialog(this) ;
        progressDialog.setMessage("Streaming...");
        myMediaPlayer = new MyMediaPlayer(this , progressDialog) ;

        httpClient = new MyHttpClient(this) ;
        httpClient.setServerAddress(sHostWithPort);

        playButton= findViewById(R.id.playMusicButton);
        pauseButton= findViewById(R.id.pauseMusicButton);
        currentSong = new Song() ;
    }


    void setupAllListeners(){

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
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
        myMediaPlayer.release();
    }

}
