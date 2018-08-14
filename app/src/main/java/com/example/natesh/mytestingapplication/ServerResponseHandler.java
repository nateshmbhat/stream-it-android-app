package com.example.natesh.mytestingapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.internal.Util;

public class ServerResponseHandler {
    Context context ;
    ServerResponseHandler(Context c ){this.context = c ; }

    public void handleSongInfo(String response , Song currentSong)
    {
        //gets an empty currentSong object and populates it with the data from response

        TextView metadata  = ((Activity)this.context).findViewById(R.id.TextView_single_music_metadata) ;
        TextView songTitle = ((Activity)this.context).findViewById(R.id.TextView_song_title_single_music) ;
        TextView songAlbum = ((Activity)this.context).findViewById(R.id.TextView_song_album_single_music) ;
        SeekBar songController = ((Activity)this.context).findViewById(R.id.SeekBar_song_controller_single_song_view) ;

        HashMap<String , String> songInfo  = getSongInfoMapping(response) ;
        Log.d("testing" , songInfo.toString()) ;

        currentSong.setTitle(songInfo.get("title"));
        currentSong.setAlbum(songInfo.get("album"));
        currentSong.setComment(songInfo.get("comment"));
        currentSong.setTrackLength(songInfo.get("trackLength"));
        currentSong.setComposer(songInfo.get("composer"));
        currentSong.setLyricist(songInfo.get("lyricist"));
        currentSong.setLyrics(songInfo.get("lyrics"));
        currentSong.setLyricsSite(songInfo.get("lyricsSite"));
        currentSong.setArtist(songInfo.get("artist"));
        currentSong.setYear(songInfo.get("year"));
        currentSong.setTrack(songInfo.get("track"));
        currentSong.setGenre(songInfo.get("genre"));

        String displayData = "" ;
        for(String songinfokey : songInfo.keySet()){
            if(songInfo.get(songinfokey)!=null)
                displayData+=Character.toUpperCase(songinfokey.charAt(0))+songinfokey.substring(1) + "    =   " + songInfo.get(songinfokey)+"\n" ;
        }

        metadata.setText(displayData);
        if(currentSong.getTitle()!=null) songTitle.setText(currentSong.getTitle());
        else songTitle.setText(currentSong.getFilename());

        if(currentSong.getAlbum()!=null) songAlbum.setText(currentSong.getAlbum());
        else songAlbum.setText("album");

        if(currentSong.getTrackLength()!=null){
            songController.setMax(Integer.parseInt(currentSong.getTrackLength()));
        }
    }



    private HashMap<String , String> getSongInfoMapping(String info){
        HashMap<String , String> map = new HashMap<>() ;
        String[] split = info.trim().split("\n");
        for(String song : split){
            String[] field = song.trim().split("=") ;
            if(field.length==2){
                map.put(field[0].trim() , field[1].trim()) ;
            }
            else{
                map.put(field[0].trim() , null) ;
            }
        }
        return map ;
    }


    public void handleGetAllSongs(String response){
       /*
       Expects response in this format :
       "
       file1path
       file2path
       file3path
       "
       */

        ArrayList<String> filePaths = new ArrayList<>() ;
        filePaths.addAll(Arrays.asList(response.split("\n"))) ;

        ArrayList<Song> songsArray = Utility.getSongsArrayFromFilePaths(filePaths) ;

        ListView allSongsListView =  ((Activity)context).findViewById(R.id.allSongsListView) ;
        SongArrayAdapter allSongsAdapter = new SongArrayAdapter(this.context , android.R.layout.simple_list_item_1 , songsArray);
        allSongsListView.setAdapter(allSongsAdapter);
    }
}


class SongArrayAdapter extends  ArrayAdapter<Song>{

    public SongArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Song> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view , @NonNull ViewGroup parent) {

        if(view==null)
            view = LayoutInflater.from(getContext()).inflate( R.layout.song_item_layout, parent , false) ;

        Song song = getItem(position) ;

        ((TextView)view.findViewById(R.id.songName)).setText(song.getFilename());
        ((TextView)view.findViewById(R.id.songLabel)).setText(song.getAlbum()) ;

        return view;
    }
}
