package com.dns;

import javax.xml.crypto.Data;
import java.net.*;
import java.util.ArrayList;

enum QueryType {
    QUERY_A,
    QUERY_NS,
    QUERY_MX
}

public class DnsClient {
    private int timeout;
    private int maxRetries;
    private int port;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private QueryType type;
    private short ID;

    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];

    public DnsClient(String serverAdress, int timeout, int maxRetries, int port, QueryType type) {
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

        this.type = type;
    }

    public InetAddress performDnsRequest(String domainName) {
        initSocket();

        try {
            socket.connect(serverAddress, port);
        } catch (Exception e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            return null;
        }

        var query = new DnsMessage(domainName, type);
        var data = query.buildQuestion((short)0); // TODO: make a random 16-bit ID
        var sendPacket = new DatagramPacket(data, data.length);
        var receivePacket = new DatagramPacket(receiveData, receiveData.length);

        var attemptCtr = 0;
        var success = false;
        while (attemptCtr++ < maxRetries) {
            // send packet
            try {
                socket.send(sendPacket);
            } catch(Exception e){
                System.err.println("Error while sending packet: " + e.getMessage());
                continue;
            }
            // receive packet
            try {
                socket.receive(receivePacket);
                success = true;
            } catch (SocketTimeoutException e) {
                System.err.println("Socket timed out while waiting for response: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error while listening to socket: " + e.getMessage());
            }
        }
        if (success) {
            // TODO: read data
        }
        closeSocket();
        return serverAddress;
    }

    private void initSocket() {
        try {
            socket = new DatagramSocket(); // just to make sure
            socket.setSoTimeout(timeout);
        } catch (SocketException e) {
            System.err.println("Failed to initialize the socket: " + e.getMessage());
            System.exit(1);
        }
    }

    private void closeSocket() {
        socket.close();
        socket = null; // force the garbage collector to dispose of the object
    }
}
