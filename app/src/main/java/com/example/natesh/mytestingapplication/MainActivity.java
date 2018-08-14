package com.example.natesh.mytestingapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button streamBtn, playButton, pauseButton, getAllSongs;
    MyMediaPlayer myMediaPlayer;
    ListView allSongsListView;
    ProgressDialog progressDialog;
    MyHttpClient httpClient;
    String sHostWithPort;
    final int port = 45672;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        discoverHosts();
        initializeAllObjects();
        setupAllListeners();
    }


    void initializeAllObjects() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Streaming...");

        myMediaPlayer = new MyMediaPlayer(this, progressDialog);
        httpClient = new MyHttpClient(this);
        setContentView(R.layout.activity_main);
        getAllSongs = findViewById(R.id.getAllSongs);
        allSongsListView = findViewById(R.id.allSongsListView);
    }


    void discoverHosts() {
        HostDiscovery hostDiscovery = new HostDiscovery() ;
        hostDiscovery.execute() ;
    }

    void setupAllListeners() {

        getAllSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    httpClient.makeRequest(sHostWithPort + "/getSongs", "getSongs");
                    ;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        allSongsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) adapterView.getItemAtPosition(i);
                Log.d("file", song.getFullPathName());

                List<String> allSongs = new ArrayList<String>();

                for (int pos = 0, n = allSongsListView.getAdapter().getCount(); pos < n; pos++) {
                    allSongs.add(((Song) adapterView.getItemAtPosition(pos)).getFullPathName());
                }

                startActivity(new Intent(MainActivity.this, ActivitySingleMusicView.class)
                        .putExtra("songPath", song.getFullPathName())
                        .putExtra("serverip", sHostWithPort)
                        .putStringArrayListExtra("allSongs", (ArrayList<String>) allSongs)
                );

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }


    class HostDiscovery extends AsyncTask<Void, Void, String> {

        boolean checkConnection(String hostIp) {
            boolean res = false;
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(hostIp, port), 10);
                res = true ;
                Log.d("server", "SUCCESS !  : "+ hostIp);

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("server", "FAILED !  : " + hostIp );
            }

            return res;
        }


        @Override
        protected String doInBackground(Void... voids) {
            String myip = Utility.getLocalIp();

            Log.d("server", "My ip = " + myip);

            String netIp = myip.substring(0, myip.lastIndexOf('.'));
            netIp = netIp.substring(0, netIp.lastIndexOf('.'));
            Log.d("server", "Subnet ip = " + netIp);

            boolean doneFlag = false ;
            for (int i = 0; i < 256; i++) {
                for(int j = 0 ; j<256 ;j++ ) {
                    if (checkConnection(netIp + "." + i + "." + j )) {
                        myip = "http://" + netIp +"."+i+"."+j+":"+port ;
                        doneFlag = true ;
                        break;
                    }
                }
                if(doneFlag) break ;
            }
            return myip;
        }

        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            Log.d("server" , "SERVER ADDRESS IS " + address) ;
            sHostWithPort = address ;
        }
    }

}

