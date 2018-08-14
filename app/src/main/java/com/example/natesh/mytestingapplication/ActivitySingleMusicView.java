package com.example.natesh.mytestingapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ActivitySingleMusicView extends AppCompatActivity {

    Button nextSong, prevSong, pauseButton, getAllSongs;
    SeekBar songController;
    MyMediaPlayer myMediaPlayer;
    ListView allSongsListView;
    Song currentSong;
    ProgressDialog progressDialog;
    MyHttpClient httpClient;
    String sHostWithPort;
    ArrayList<String> allSongPaths;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_song_layout);

        String songPath = getIntent().getStringExtra("songPath");
        sHostWithPort = getIntent().getStringExtra("serverip");
        allSongPaths = getIntent().getStringArrayListExtra("allSongs");

        initializeAllObjects();
        setupAllListeners();
        loadNewSong(songPath);
    }

    void loadNewSong(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            songController.setProgress(0, true);
        } else songController.setProgress(0);

        playOrPauseMusic(true);
        currentSong.setFullPathName(path);
        try {
            myMediaPlayer.loadMusicFromRemoteFilePath(sHostWithPort, currentSong);
            httpClient.getSongInfo(currentSong);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initializeAllObjects() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Streaming...");
        myMediaPlayer = new MyMediaPlayer(this, progressDialog);
        myMediaPlayer.setSongPathsList(allSongPaths);
        ;

        httpClient = new MyHttpClient(this);
        httpClient.setServerAddress(sHostWithPort);

        pauseButton = findViewById(R.id.pauseMusicButton);
        nextSong = findViewById(R.id.ButtonNextSong_single_song_view);
        prevSong = findViewById(R.id.ButtonPreviousSong_single_song_view);
        songController = findViewById(R.id.SeekBar_song_controller_single_song_view);
        currentSong = new Song();
    }


    void playOrPauseMusic(boolean play) {
        if (play) {
            myMediaPlayer.resumeMusic();
            pauseButton.setText("❚❚");
        } else {
            myMediaPlayer.pauseMusic();
            pauseButton.setText("▶");
        }
    }

    void setupAllListeners() {

        songController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (fromUser) {
                    myMediaPlayer.setSongPlaytime(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int value = seekBar.getProgress();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    seekBar.setTooltipText("" + value);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myMediaPlayer.isPlaying()) {
                    myMediaPlayer.pauseMusic();
                    ((Button) view).setText("▶");
                } else {
                    myMediaPlayer.resumeMusic();
                    ((Button) view).setText("❚❚");
                }
            }
        });

        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curSongIndex = allSongPaths.indexOf(currentSong.getFullPathName());
                if (curSongIndex + 1 < allSongPaths.size()) {
                    loadNewSong(allSongPaths.get(curSongIndex + 1));
                } else {
                    loadNewSong(allSongPaths.get(0));
                }
            }
        });

        prevSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int curSongIndex = allSongPaths.indexOf(currentSong.getFullPathName());
                if (curSongIndex - 1 >= 0) {
                    loadNewSong(allSongPaths.get(curSongIndex - 1));
                } else {
                    loadNewSong(allSongPaths.get(allSongPaths.size() - 1));
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        myMediaPlayer.release();
    }

}
