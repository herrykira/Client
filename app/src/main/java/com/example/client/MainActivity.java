package com.example.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketListener;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.handshake.ServerHandshakeBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ClientActivity";
    public static final int DEFAULT_BUFFER_SIZE = 10240;
    Thread Thread1 = null;
    EditText etIP, etPort;
    TextView tvMessages;
    EditText etMessage;
    Button btnSend;
    String SERVER_IP;
    int SERVER_PORT;
    SocketChannel socketChannel;
    Selector selector;
    Client client;

    BroadcastReceiver mBroadcastReceiver;

    private final ByteBuffer mReadBuffer = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        etIP = findViewById(R.id.etIP);
        etPort = findViewById(R.id.etPort);
        tvMessages = findViewById(R.id.tvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        Button btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMessages.setText("");
                SERVER_IP = etIP.getText().toString().trim();
                SERVER_PORT = Integer.parseInt(etPort.getText().toString().trim());
                Thread1 = new Thread(new Thread1());
                Thread1.start();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    new Thread(new Thread3(message)).start();
                }
            }
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
                    Log.d(TAG, "screen is on");
                }
                else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
                    Log.d(TAG, "screen is off");
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    class Thread1 implements Runnable {
        public void run() {
            try {
//                selector = Selector.open();
//                socketChannel = SocketChannel.open(new InetSocketAddress(SERVER_IP, SERVER_PORT));
//                socketChannel.configureBlocking(false);
//                socketChannel.register(selector, SelectionKey.OP_READ);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG, "start connect...");
//                        tvMessages.setText("Connected\n");
//                    }
//                });
//                new Thread(new Thread2()).start();
                String location = "ws://"+SERVER_IP+":" + SERVER_PORT;
                client = new Client(new URI(location), new WebSocketListener() {
                    @Override
                    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
                        Log.d(TAG, "received as a server");
                        return null;
                    }

                    @Override
                    public void onWebsocketHandshakeReceivedAsClient(WebSocket conn, ClientHandshake request, ServerHandshake response) throws InvalidDataException {
                        Log.d(TAG, "received as a client");
                    }

                    @Override
                    public void onWebsocketHandshakeSentAsClient(WebSocket conn, ClientHandshake request) throws InvalidDataException {
                        Log.d(TAG, "send as a client");
                    }

                    @Override
                    public void onWebsocketMessage(WebSocket conn, String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvMessages.append("Server: " + message + "\n");
                            }
                        });
                    }

                    @Override
                    public void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {

                    }

                    @Override
                    public void onWebsocketMessageFragment(WebSocket conn, Framedata frame) {

                    }

                    @Override
                    public void onWebsocketOpen(WebSocket conn, Handshakedata d) {
                        Log.d(TAG, "onWebsocketOpen");
                    }

                    @Override
                    public void onWebsocketClose(WebSocket ws, int code, String reason, boolean remote) {
                        Log.d(TAG, "onWebsocketClose");
                    }

                    @Override
                    public void onWebsocketClosing(WebSocket ws, int code, String reason, boolean remote) {
                        Log.d(TAG, "onWebsocketClosing");
                    }

                    @Override
                    public void onWebsocketCloseInitiated(WebSocket ws, int code, String reason) {
                        Log.d(TAG, "onWebsocketCloseInitiated");
                    }

                    @Override
                    public void onWebsocketError(WebSocket conn, Exception ex) {
                        Log.d(TAG, "onWebsocketError");

                    }

                    @Override
                    public void onWebsocketPing(WebSocket conn, Framedata f) {
                        Log.d(TAG, "onWebsocketPing");

                    }

                    @Override
                    public void onWebsocketPong(WebSocket conn, Framedata f) {
                        Log.d(TAG, "onWebsocketPong");

                    }

                    @Override
                    public void onWriteDemand(WebSocket conn) {
                        Log.d(TAG, "onWriteDemand");

                    }

                    @Override
                    public InetSocketAddress getLocalSocketAddress(WebSocket conn) {
                        return null;
                    }

                    @Override
                    public InetSocketAddress getRemoteSocketAddress(WebSocket conn) {
                        return null;
                    }
                });
                client.connect();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "start connect...");
                        tvMessages.setText("Connected\n");
                    }
                });
            } catch (URISyntaxException e) {
                Log.e(TAG, "connect error: " + e.getMessage());
            }
        }
    }


    class Thread2 implements Runnable {
        @Override
        public void run() {
            while(true){
                Log.d(TAG, "Waiting for Server connection request");
                try {
                    selector.select();
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                    while(keys.hasNext()) {
                        SelectionKey key = keys.next();
                        keys.remove();

                        if (key.isReadable()) {
                            Log.d(TAG, "Key is read");
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            channel.read(buffer);
                            String msg = new String(buffer.array());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvMessages.append("Server: "  + msg + "\n");
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class Thread3 implements Runnable {
        private String message;
        Thread3(String message) {
            this.message = message;
        }
        @Override
        public void run() {
//            try {
//                ByteBuffer bytes = ByteBuffer.wrap(message.getBytes());
////                int reqSize = bytes.remaining();
////                int wroteSize = 0;
//                int remainSize = bytes.remaining();
//                while (remainSize > 0) {
//                    int len = socketChannel.write(bytes);
//                    if (len < 0) {
//                        Log.d(TAG, "ClientConnection: write failure close connection");
//                        break;
//                    }
////                    wroteSize += len;
//                    remainSize = bytes.remaining();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tvMessages.append("client: " + message + "\n");
//                    etMessage.setText("");
//                }
//            });
            if (client != null){
                client.send(message);
                Log.d(TAG, message);
                runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvMessages.append("client: " + message + "\n");
                    etMessage.setText("");
                }
            });
            }
        }
    }
}