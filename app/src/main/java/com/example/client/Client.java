package com.example.client;

import android.util.Log;

import org.java_websocket.WebSocketListener;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class Client extends WebSocketClient {
    private static final String TAG = "Client";
    private WebSocketListener webSocketListener;
    public Client(URI serverUri, WebSocketListener webSocketListener) {
        super(serverUri);
        this.webSocketListener = webSocketListener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d(TAG, "You are connected to Server: " + getURI());
    }

    @Override
    public void onMessage(String message) {
        Log.d(TAG, "Got: " + message);
        webSocketListener.onWebsocketMessage(this, message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d(TAG, "You have been disconnected from: " + getURI() + "; Code: " + code + ", " + reason);
    }

    @Override
    public void onError(Exception ex) {
        Log.e(TAG, "Exception occurred : " + ex.getMessage());
    }
}
