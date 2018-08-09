package com.example.natesh.mytestingapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MyMediaPlayer {

    MediaPlayer  mediaPlayer ;
    private boolean playPause;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    Context context ;
    Player currentPlayer  ;

    MyMediaPlayer(Context context  , ProgressDialog progressBar){
        mediaPlayer = new MediaPlayer() ;
        this.context = context ;
        this.progressDialog = progressBar ;
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        currentPlayer = new Player() ;

        setAllListeners();
    }


    public void setAllListeners(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                initialStage = true;
                playPause = false;
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        });
    }



    public void startMusicFromURL(String url){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop() ;
        }
        currentPlayer = new Player();
        currentPlayer.execute(url) ;
    }

    public void loadMusicFromRemoteFilePath(String host , String path)
    {
        String encodedUrl = new String("");
        try {
            encodedUrl = host+"/getFile?"+ URLEncoder.encode( path , "utf-8").replace("+" , "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        startMusicFromURL( encodedUrl ); ;
    }

    public void pauseMusic(){
        //Pauses if the music is playing
        mediaPlayer.pause();
    }

    public void resumeMusic(){
        //Resumes if paused previously
        if(mediaPlayer==null)return ;
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    public void stopMusic(){
        //Stops the music . After this, calling start will start music from beginning
        mediaPlayer.stop() ;
    }



    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                e.printStackTrace();
                prepared = false;
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }
    }

}

