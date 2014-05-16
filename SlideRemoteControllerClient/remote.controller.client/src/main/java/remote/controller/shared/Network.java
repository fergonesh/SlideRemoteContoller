package remote.controller.shared;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
    static public final int port = 54555;
    static public final int TCP_PORT = 54555;
    static public final int UDP_PORT = 54777;

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(RegisterName.class);
        kryo.register(String[].class);
        kryo.register(UpdateNames.class);
        kryo.register(ChatMessage.class);
    }

    static public class RegisterName {
        public String name;
    }

    static public class UpdateNames {
        public String[] names;
    }

    static public class ChatMessage {
        public String text;
    }
}












/*
package shen.android.shared; */
/**
 * ver 0.3
 *//*


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
    static public final int TCP_PORT = 54555;
    static public final int UDP_PORT = 54777;
    static public final int USB_PORT = 54888;

    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(String[].class);
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);

        kryo.register(KeyEvent.class);
*/
/*        kryo.register(EventSequence.class);
        kryo.register(DelayEvent.class);*//*

        kryo.register(Event.class);
        kryo.register(ArrayList.class);
  */
/*      kryo.register(MouseEvent.class);
        kryo.register(MouseEvent.Type.class);
        kryo.register(ExecutableCommand.class);
        *//*

        kryo.register(ServerGreeting.class);
        //  kryo.register(ServerWindow.class);
        //kryo.register(Handshake.class);
    }

    static public class SomeRequest {
        public String text;
    }

    static public class SomeResponse {
        public String text;
    }

}*/


