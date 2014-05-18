package remote.controller.client;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
/*import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;*/
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.*;

import java.io.IOException;
import java.net.InetAddress;

import remote.controller.fragments.ButtonFragment;
import remote.controller.fragments.ConnectionFragment;
import remote.controller.shared.Network;
import remote.controller.shared.Network.ChatMessage;
import remote.controller.shared.Network.UpdateNames;
import com.bugsense.trace.BugSenseHandler;

/**
 * Created by Pavlo Shenhofer
 * MainActivity Android Client program
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //@SuppressLint("NewApi")
    private FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction fTrans;
    private InetAddress address2;
    Client tcpClient, udpClient;
    ChatMessage chatMessage = new ChatMessage();
    String name;
    private ConnectionFragment conFragment;
    private ButtonFragment buttonFragment;
    //  EventSequence msgToSend;
    private boolean stopping; //flag for connection with server

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BugSenseHandler.initAndStartSession(this, "c6dbac2d");
        Log.d("Making connection..");

        setContentView(R.layout.activity_main);
        /**BEGIN Click Button Listener*/
        //   activate();
        tcpClient = new Client();
        tcpClient.start();

        udpClient = new Client();
        udpClient.start();

        // Init needed fragments
        conFragment = new ConnectionFragment();
        buttonFragment = new ButtonFragment();

        manager.beginTransaction().add(R.id.pad, conFragment).commitAllowingStateLoss();

        changeFragment();




    }

    @SuppressLint("NewApi")
    public void changeFragment() {

        new Thread("TryConnect") {
            public void run() {

                    address2 = udpClient.discoverHost(Network.UDP_PORT, 5000);
                    if (isNetworkAvailable() && address2 != null) {
                        manager.beginTransaction().replace(R.id.pad, buttonFragment ).commit();
                    }

            }
        }.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Button handling
    public void onClick(View v) {

    }



    public void activate() {
        tcpClient = new Client();
        tcpClient.start();
        udpClient = new Client();
        udpClient.start();
        new ConnectionThread().start();
        Log.d("activated");
    }

    private class ConnectionThread extends Thread {
        @Override
        public void run() {
            Network.register(tcpClient);
            while (!false) {
                /**
                 * Discover server via WiFi
                 */
                InetAddress address = udpClient.discoverHost(Network.UDP_PORT, 5000);
                Log.d("Discovered server address:" + address);

                /**
                 * Server discovered via WiFi? Try to use it.
                 */
                if (address != null) {
                    try {
                        // tcpClient.start();
                        tcpClient.connect(5000, address, Network.TCP_PORT);
                        // tcpClient.sendTCP(new Handshake().phone_TrustMe(accountName, accountType));
                        //currConnType = ConnType.WIFI;
                        Log.d("Connect via WiFI..");
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

/*    public void sendMessage(final Object msg) {
        if (isConnected() && msgToSend != null) {
            new Thread() {
                public void run() {
                    //trustedConnection.sendTCP(msg);
                    tcpClient.sendTCP(msg);
                }
            }.start();

        } else {
            Log.e("Message is null and wasn't sent");
        }
    }*/




    @Override
    protected void onPause() {
        super.onPause();
        new Runnable() {
            public void run() {
                tcpClient.stop();
            }
        };
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}