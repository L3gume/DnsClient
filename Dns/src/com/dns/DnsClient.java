package com.dns;

import javax.xml.crypto.Data;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

enum QueryType {
    QUERY_A,
    QUERY_NS,
    QUERY_MX,
    QUERY_CNAME
}

public class DnsClient {
    private int timeout;
    private int maxRetries;
    private int port;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private QueryType type;
    private short ID;

    public DnsClient(String serverAdress, int timeout, int maxRetries, int port, QueryType type) {
        try {
            // big hardcoded 4, ArrayList is just too annoying to use
            var ipBytes = new byte[4];
            var splitAddr = serverAdress.split("\\.");
            for (var i = 0; i < 4; ++i) {
                ipBytes[i] = ((byte)Integer.parseInt(splitAddr[i]));
            }
            this.serverAddress = InetAddress.getByAddress(ipBytes);
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

    public DnsMessage performDnsRequest(String domainName) {
        DnsMessage response = null;
        var rand = new Random();
        initSocket();

        try {
            System.out.format("Connecting to: %s [port %d]\n", serverAddress.toString(), port);
            socket.connect(serverAddress, port);
        } catch (Exception e) {
            System.err.println("ERROR - Failed to connect to server: " + e.getMessage());
            return null;
        }

        this.ID = (short)rand.nextInt(65535);
        var query = new DnsMessage(domainName, type);

        var sendData = ByteBuffer.allocate(512);
        var queryLen = query.buildQuestion(sendData, this.ID);
        var sendPacket = new DatagramPacket(sendData.array(), sendData.array().length);

        var receiveData = ByteBuffer.allocate(512);
        var receivePacket = new DatagramPacket(receiveData.array(), receiveData.array().length);

        var attemptCtr = 0;
        var success = false;
        while (attemptCtr++ < maxRetries) {
            // send packet
            try {
                System.out.format("Sending query to: %s [port %d] (attempt #%d)\n", serverAddress.toString(), port, attemptCtr);
                socket.send(sendPacket);
            } catch(Exception e){
                System.err.println("ERROR - Error while sending packet: " + e.getMessage());
                continue;
            }
            // receive packet
            try {
                socket.receive(receivePacket);
                success = true;
                break;
            } catch (SocketTimeoutException e) {
                System.err.println("ERROR - Socket timed out while waiting for response: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("ERROR - Error while listening to socket: " + e.getMessage());
            }
            // no need for a continue
        }
        if (success) {
            // rebuild into a DnsMessage to access info
            response = new DnsMessage(receiveData, this.ID, queryLen);
        } else {
            System.err.println("ERROR - Failed to contact name server. Exiting...");
            System.exit(127);
        }
        socket.disconnect();
        closeSocket();
        return response;
    }

    private void initSocket() {
        try {
            socket = new DatagramSocket(); // just to make sure
            socket.setSoTimeout(timeout * 1000);
        } catch (SocketException e) {
            System.err.println("ERROR - Failed to initialize the socket: " + e.getMessage());
            System.exit(127);
        }
    }

    private void closeSocket() {
        socket.close(); // DON'T LEAVE SOCKETS OPEN
        socket = null; // force the garbage collector to dispose of the object
    }
}
