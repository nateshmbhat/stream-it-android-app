package com.example.natesh.mytestingapplication;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    private Button streamBtn;
    MyMediaPlayer myMediaPlayer  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myMediaPlayer = new MyMediaPlayer(this) ;
        setContentView(R.layout.activity_main);
        streamBtn = (Button) findViewById(R.id.audioStreambtn);
        Button playButton= (Button) findViewById(R.id.playMusicButton);
        Button pauseButton= (Button) findViewById(R.id.pauseMusicButton);

        streamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMediaPlayer.startMusicFromURL("http://192.168.0.100:8090/test");
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
    }


}
