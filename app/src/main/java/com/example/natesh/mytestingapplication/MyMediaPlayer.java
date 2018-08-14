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
import java.util.ArrayList;

public class MyMediaPlayer {

    MediaPlayer  mediaPlayer ;
    ArrayList<String> songPathsList ;
    private boolean playPause;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    Context context ;
    SongPathIndex  currentSongPath ;
    Player currentPlayer  ;

    MyMediaPlayer(Context context  , ProgressDialog progressBar){
        mediaPlayer = new MediaPlayer() ;
        this.context = context ;
        this.progressDialog = progressBar ;
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        currentPlayer = new Player() ;

        setAllListeners();
    }

    public void setCurrentSongPath(SongPathIndex currentSongPath) {
        this.currentSongPath = currentSongPath ;
    }

    public SongPathIndex getCurrentSongPath() {
        return currentSongPath;
    }

    public void setSongPlaytime(int value){
        mediaPlayer.seekTo(value*1000);
    }

    public void getSongPlaytime(int value){
        mediaPlayer.getCurrentPosition() ;
    }

    public void setSongPathsList(ArrayList<String> songPathsList){this.songPathsList= songPathsList ; }

    public ArrayList<String> getSongPathsList() {
        return songPathsList;
    }

    public boolean isPlaying(){return mediaPlayer.isPlaying() ; }

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

    public void loadMusicFromRemoteFilePath(String host , Song song)
    {
        String path = song.getFullPathName() ;
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
    public void release(){
        mediaPlayer.release();
    }



    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                setCurrentSongPath(new SongPathIndex(strings[0] , songPathsList.indexOf(strings[0])));
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                MediaPlayer.TrackInfo[] trackInfo = mediaPlayer.getTrackInfo() ;
                for(MediaPlayer.TrackInfo info : trackInfo)
                {
                    Log.d("metadata" , info.toString()) ;
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            progressDialog.setMessage("Streaming...");
            progressDialog.show();
        }
    }

}


class SongPathIndex{
    String songPath ;int index  ;

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSongPath() {
        return songPath;
    }

    public int getIndex() {
        return index;
    }

    public SongPathIndex(String songPath, int index) {
        this.songPath = songPath;
        this.index = index;
    }
}
