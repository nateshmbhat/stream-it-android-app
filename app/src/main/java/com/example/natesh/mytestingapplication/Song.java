package com.example.natesh.mytestingapplication;

public class Song {
    String filename, fullPathName,trackLength , artist, album, year, track, title, composer, comment, genre, lyricist, lyrics, lyricsSite;

    public Song(String fullPathName) {
        setFullPathName(fullPathName);
    }

    public Song() {
        ;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(String trackLength) {
        this.trackLength = trackLength;
    }

    public void setFullPathName(String fullPathName) {
        int indexOfSeperator = fullPathName.lastIndexOf('/');
        if (indexOfSeperator < 0)
            indexOfSeperator = fullPathName.lastIndexOf('\\');

        this.fullPathName = fullPathName;
        setFilename(fullPathName.substring(indexOfSeperator + 1));
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setLyricist(String lyricist) {
        this.lyricist = lyricist;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public void setLyricsSite(String lyricsSite) {
        this.lyricsSite = lyricsSite;
    }

    public String getFilename() {
        return filename;
    }

    public String getFullPathName() {
        return fullPathName;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getYear() {
        return year;
    }

    public String getTrack() {
        return track;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public String getGenre() {
        return genre;
    }

    public String getLyricist() {
        return lyricist;
    }

    public String getLyrics() {
        return lyrics;
    }

    public String getLyricsSite() {
        return lyricsSite;
    }

    public String getComposer() {
        return composer;
    }
}
