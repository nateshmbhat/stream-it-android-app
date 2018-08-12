package com.example.natesh.mytestingapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

    public void handleSongInfo(String response)
    {
        TextView metadata  = ((Activity)this.context.getApplicationContext()).findViewById(R.id.single_music_metadata_TextView) ;
        metadata.setText(response);
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

        ((TextView)view.findViewById(R.id.songName)).setText(song.getSongName());
        ((TextView)view.findViewById(R.id.songLabel)).setText(song.getLabel()) ;

        return view;
    }
}
