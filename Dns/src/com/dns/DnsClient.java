package com.dns;

import java.net.*;

public class DnsClient {
    private int timeout;
    private int maxRetries;
    private int port;
    private DatagramSocket socket;
    private InetAddress serverAddress;

    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];

    public DnsClient(String serverAdress, int timeout, int maxRetries, int port) {
        try {
            this.serverAddress = InetAddress.getByName(serverAdress);
        }
        catch (Exception e){
            // TODO: actually handle error
            e.printStackTrace();
        }

        this.timeout = timeout;
        this.maxRetries = maxRetries;
        this.port = port;

        try {
            this.socket = new DatagramSocket();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InetAddress performDnsRequest(String domainName) {
        // TODO
        return serverAddress;
    }

    private void initSocket() {
        // TODO
    }
}
