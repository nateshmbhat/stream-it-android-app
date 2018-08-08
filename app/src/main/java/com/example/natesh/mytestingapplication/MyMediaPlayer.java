package com.example.natesh.mytestingapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

public class MyMediaPlayer {

    MediaPlayer  mediaPlayer ;
    private boolean playPause;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    Context context ;

    MyMediaPlayer(Context context ){
        mediaPlayer = new MediaPlayer() ;
        this.context = context ;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(context);

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
        new Player().execute(url) ;
    }

    public void pauseMusic(){
        //Pauses if the music is playing
        mediaPlayer.pause();
    }

    public void resumeMusic(){
        //Resumes if paused previously
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    public void stopMusic(){
        //Stops the music . After this calling start will start music from beginning
        mediaPlayer.stop() ;
    }



    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;
            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("testing", e.getMessage());
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
            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }
    }

}

