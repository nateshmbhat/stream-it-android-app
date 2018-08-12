package com.example.natesh.mytestingapplication;

public class Song {
    String songName , label  , fullPathName , dirName  , singer , album , lyrics , composer ;

    public Song(String fullPathName) {
        this.fullPathName = fullPathName;
        int indexOfSeperator =  fullPathName.lastIndexOf('/');
        if(indexOfSeperator<0)
            indexOfSeperator = fullPathName.lastIndexOf('\\') ;

        this.songName = (fullPathName.substring(indexOfSeperator+1)) ;
    }

    public  Song(){;}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getSongName() {
        return songName;
    }

    public String getFullPathName() {
        return fullPathName;
    }

    public String getDirName() {
        return dirName;
    }

    public String getSinger() {
        return singer;
    }

    public String getAlbum() {
        return album;
    }

    public String getLyrics() {
        return lyrics;
    }

    public String getComposer() {
        return composer;
    }
}
