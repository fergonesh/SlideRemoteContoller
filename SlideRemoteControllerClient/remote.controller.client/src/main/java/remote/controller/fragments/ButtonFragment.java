package remote.controller.fragments;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;

import remote.controller.client.Log;
import remote.controller.shared.Network;
import remote.controller.shared.Network.ChatMessage;
import remote.controller.shared.Network.UpdateNames;
import remote.controller.client.MainActivity;
import remote.controller.client.R;
import remote.controller.shared.Network;

@SuppressLint("NewApi")
public class ButtonFragment extends Fragment {
    private TextView lblHelp;
    ChatMessage chatMessage = new ChatMessage();
    private InetAddress address2;
    Client tcpClient, udpClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.button_fragment, container, false);
        //lblHelp = (TextView) v.findViewById(R.id.lbl_connection_help);
        tcpClient = new Client();
        tcpClient.start();

        udpClient = new Client();
        udpClient.start();
        Button fullscreenButton = (Button) v.findViewById(R.id.fullscreen);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMessage.text = "pressF5";
                sendMsg();
            }
        });

        Button fullscreenOutButton = (Button) v.findViewById(R.id.fullscreenout);
        fullscreenOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMessage.text = "ESC";
                sendMsg();
            }
        });

        Button leftButton = (Button) v.findViewById(R.id.leftArrow);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMessage.text = "leftArrow";
                sendMsg();
            }
        });

        Button rightButton = (Button) v.findViewById(R.id.rightArrow);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMessage.text = "rightArrow";
                sendMsg();
            }
        });
        //END Click Button Listener
        return v;
    }
/*    public void onClick(View v) {

    }*/

    private void sendMsg() {
        //boolean isNetwork = isNetworkAvailable();
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
        //name = input.trim();

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
}
