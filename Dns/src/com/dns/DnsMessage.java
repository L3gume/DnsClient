package com.dns;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

// TODO: build ways to extract info from data when the DnsMessage is a response
public class DnsMessage {
    private String domainName;
    private QueryType type;
    private ByteBuffer data;

    // Answer header
    private short ansID;
    private short ansQDCount;
    private short ansANCount;
    private short ansNSCount;
    private short ansARCount;
    private boolean ansAuthority;
    private boolean ansRecSupport;
    private byte ansRCode;
    // Answer body
    private ArrayList<String> ansName; // There could be more than 1
    private ArrayList<String> ansRData; // There could be more than 1
    private short ansType;
    private short ansClass;
    private int ansTTL;
    private int ansRDLength;
    private int ansPreference;
    private String ansExchange;

    public DnsMessage(String domainName, QueryType type) {
        // domain name is too long, everything is going to break, exit now
        if (domainName.getBytes().length > 63) {
            System.err.println(String.format(
                    "Domain name: \"%s\" is longer that the allowed 63 octets (RFC 1034). Exiting...",
                    domainName));
            System.exit(1);
        }
        this.domainName = domainName;
        this.type = type;
        this.data = ByteBuffer.allocate(512);
    }

    public DnsMessage(ByteBuffer data) {
        this.data = data;
        // TODO: populate other fields and make util functions to fetch info
    }

    /**
     * builds the DNS query
     * @return a byte array ready to be sent
     */
    public ByteBuffer buildQuestion(short ID) {
        data.clear();
        data.put(buildHeader(ID));
        data.put(buildHeader(ID));
        data.put(buildBody());
        data.compact();
        return data;
    }

    /**
     * checks if the message is a question
     * @return true if it's a question
     */
    public boolean isQuestion() {
        return data.get(2) >> 7 == 0;
    }

    /**
     * checks if the message is a response
     * @return true if it's a response
     */
    public boolean isResponse() {
        return !isQuestion();
    }

    /**
     * returns a 16-byte DNS header
     * @param id the id of the sender, 16-bit integer
     * @return a 16-byte array representing the header
     */
    private ByteBuffer buildHeader(short id) {
        // Header is the same for all types of DNS queries
        // Only thing that changes is the id, which will be generated by the derived class
        // we need 12 bytes to make the header
        var bytes = ByteBuffer.allocate(12);
        // ID
        bytes.putShort(id);
        //bytes.add((byte)(id));
        // |QR(1)| Opcode(4) |AA(1)|TC(1)|RD(1)|
        bytes.put((byte)(0b10000001));
        // |RA(1)|   Z(3)   | RCODE(4) |
        bytes.put((byte)(0b10000000));
        // QDCOUNT, always equal to 1
        bytes.put((byte)(0b00000000));
        bytes.put((byte)(0b00000001));
        // ANCOUNT, only matters in response
        bytes.put((byte)(0b00000000));
        bytes.put((byte)(0b00000000));
        // NSCOUNT, can be ignored
        bytes.put((byte)(0b00000000));
        bytes.put((byte)(0b00000000));
        // ARCOUNT, ????
        bytes.put((byte)(0b00000000));
        bytes.put((byte)(0b00000000));
        return bytes;
    }

    private void parseHeader() {

    }

    private ByteBuffer buildBody() {
        var bytes = ByteBuffer.allocate(512);
        // the domain name was already checked
        bytes.put(parseDomainName());
        // type of query QTYPE
        bytes.put((byte)0x00);
        byte b = 0x00;
        switch (type) {
            case QUERY_A: b = (byte)(0x01); break;
            case QUERY_NS: b = (byte)(0x02); break;
            case QUERY_MX: b = (byte)(0x0f); break;
            default:
                System.err.println("Invalid query type. Exiting...");
                System.exit(1);
        }
        bytes.put(b);
        // QCLASS, internet address
        bytes.put((byte)0x00);
        bytes.put((byte)0x01);
        bytes.compact();
        // done building the body
        return bytes;
    }

    private void parseBody() {

    }

    private ByteBuffer parseDomainName() {
        var parsed = ByteBuffer.allocate(512);
        var split = domainName.split(".");
        for (var label : split) {
            parsed.put((byte)label.length());
            for (var ch : label.getBytes()) {
                parsed.put(ch);
            }
        }
        parsed.put((byte)0x00);
        parsed.compact();
        return parsed;
    }

}
