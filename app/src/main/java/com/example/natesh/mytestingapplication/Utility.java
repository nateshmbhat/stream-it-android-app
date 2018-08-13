package com.example.natesh.mytestingapplication;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;


public class Utility {

    public static String getLocalIp() {
        String ip = "";
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static ArrayList<Song> getSongsArrayFromFilePaths(ArrayList<String> filepaths){
        ArrayList<Song> songsList = new ArrayList<>() ;
        for(String filepath : filepaths){

            Song song = new Song(filepath) ;
            Log.d("file" , File.separator) ;
            Log.d("file" , filepath) ;

            songsList.add(song) ;
        }
        return songsList ;
    }

    public static HashMap<String , String> parsePostRequest(String postbody){
        //Input string is a url encoded post body and hence has to be split right
        System.out.println(postbody);
        HashMap<String , String> map  = new HashMap<>();

        for(String keyValue :  postbody.split("&"))
        {
            String[] keyval = keyValue.split("=") ;
            map.put(URLDecoder.decode(keyval[0]) , URLDecoder.decode(keyval[1])) ;
        }
        return map ;
    }


    ///Takes encoded URL and returns a decoded one
    public static String decodeUrl(String url)
    {
        try {
            String prevURL="";
            String decodeURL=url;
            while(!prevURL.equals(decodeURL))
            {
                prevURL=decodeURL;
                decodeURL= URLDecoder.decode( decodeURL, "UTF-8" );
            }
            return decodeURL;
        } catch (UnsupportedEncodingException e) {
            return "Issue while decoding" +e.getMessage();
        }
    }



    public static String getPostRequestBody(InputStream inputStream) {
        String s = new String();

        byte[] bytes = new byte[1024];
        while (true) {
            try {
                if (inputStream.read(bytes) == -1) break;
                s += new String(bytes);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        return s ;
    }
}

