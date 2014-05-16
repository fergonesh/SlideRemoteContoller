package remote.controller.client;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.*;

import java.io.IOException;
import java.net.InetAddress;

import remote.controller.shared.Network;
import remote.controller.shared.Network.ChatMessage;
import remote.controller.shared.Network.UpdateNames;

/**
 * Created by Pavlo Shenhofer
 * MainActivity Android Client program
 */
public class MainActivity extends Activity implements View.OnClickListener {

    InetAddress address2;
    Client tcpClient, udpClient;
    ChatMessage chatMessage = new ChatMessage();
    String name;
    //  EventSequence msgToSend;
    private boolean stopping; //flag for connection with server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Making connection..");

        setContentView(R.layout.activity_main);
        /**BEGIN Click Button Listener*/
        //   activate();
        tcpClient = new Client();
        tcpClient.start();

        udpClient = new Client();
        udpClient.start();
        //   InetAddress address = tcpClient.discoverHost(Network.TCP_PORT, 5000);
        //     Log.d("Discovered server address:" + address);
        address2 = udpClient.discoverHost(Network.UDP_PORT, 5000);

        Button fullscreenButton = (Button) findViewById(R.id.fullscreen);
        fullscreenButton.setOnClickListener(this);

        Button fullscreenOutButton = (Button) findViewById(R.id.fullscreenout);
        fullscreenOutButton.setOnClickListener(this);

        Button leftButton = (Button) findViewById(R.id.leftArrow);
        leftButton.setOnClickListener(this);

        Button rightButton = (Button) findViewById(R.id.rightArrow);
        rightButton.setOnClickListener(this);
        /**END Click Button Listener*/

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
        switch (v.getId()) {
            case R.id.fullscreen: {
                chatMessage.text = "pressF5";
                sendMsg();

            }
            break;

            case R.id.fullscreenout: {
                chatMessage.text = "ESC";
                sendMsg();
            }
            break;

            case R.id.rightArrow: {
                chatMessage.text = "rightArrow";
                sendMsg();
            }
            break;

            case R.id.leftArrow: {
                chatMessage.text = "leftArrow";
                sendMsg();
            }
            break;

            default:
                break;
        }
    }

    public void sendMsg() {
        boolean isNetwork = isNetworkAvailable();
        if (tcpClient != null) {
            tcpClient = new Client();
            tcpClient.start();
        }
        if (udpClient != null) {
            udpClient = new Client();
            udpClient.start();
        }

        if (address2 == null) {
            address2 = udpClient.discoverHost(Network.UDP_PORT, 5000);
        }
        Log.d("Discovered server address:" + address2);
        //   Log.info("Client was started...");
        if (address2 != null) {

            Network.register(tcpClient);

            tcpClient.addListener(new Listener() {
                public void connected(Connection connection) {

                    tcpClient.sendTCP(chatMessage);
                    tcpClient.stop();

                }


            });


            String input = "localhost";
            if (input == null || input.trim().length() == 0) System.exit(1);
            final String host = input;

            input = "Test";
            if (input == null || input.trim().length() == 0) System.exit(1);
            name = input.trim();

            new Thread("Connect") {
                public void run() {
                    try {
                        //  tcpClient.connect(5000, "localhost", Network.port);
                        tcpClient.connect(5000, address2, Network.TCP_PORT);
                        // Server communication after connection can go here, or in Listener#connected().
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.exit(1);
                    }
                }
            }.start();

        }
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

    private void startListener(final ChatMessage chm) {
        tcpClient.addListener(new Listener() {
            public void connected(Connection connection) {
                tcpClient.sendTCP(chm);
                //   tcpClient.stop();

            }

            public void received(Connection connection, Object object) {
                if (object instanceof UpdateNames) {
                    UpdateNames updateNames = (UpdateNames) object;
                    return;
                }

                if (object instanceof ChatMessage) {
                    ChatMessage chatMessage = (ChatMessage) object;
                    return;
                }
            }
        });
    }


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