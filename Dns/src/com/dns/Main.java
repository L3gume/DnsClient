package com.dns;

import net.sourceforge.argparse4j.*;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

import java.net.InetAddress;

public class Main {

    public static void main(String[] args) {
        var parser = ArgumentParsers.newFor("DnsClient").build().defaultHelp(true)
                .description("Perform a dns request to given server.");
        parser.addArgument("-t", "--timeout")
                .type(Integer.class)
                .required(false)
                .setDefault(5)
                .help("Specify the timeout (seconds) before retransmitting an unanswered query.");
        parser.addArgument("-r", "--max-retries")
                .type(Integer.class)
                .required(false)
                .setDefault(3)
                .help("Set maximum number of times to retransmit before giving up.");
        parser.addArgument("-p", "--port")
                .type(Integer.class)
                .required(false)
                .setDefault(53)
                .help("UDP port number of the DNS server");
        parser.addArgument("-ns", "--ns")
                .type(Boolean.class)
                .action(Arguments.storeConst()).setConst(true)
                .setDefault(false)
                .required(false)
                .help("Contact a name server, can't be used with -mx.");
        parser.addArgument("-mx", "--mx")
                .type(Boolean.class)
                .action(Arguments.storeConst()).setConst(true)
                .setDefault(false)
                .required(false)
                .help("Contact a mail server, can't be used with -ns.");
        parser.addArgument("@server").nargs(1)
                .type(String.class)
                .required(true)
                .help("Specify the IP address of the DNS server.");
        parser.addArgument("name").nargs(1)
                .type(String.class)
                .required(true)
                .help("Specify the name of the domain.");

        try {
            var parsed = parser.parseArgs(args);
            var type = QueryType.QUERY_A;
            if (parsed.getBoolean("ns") && !parsed.getBoolean("mx")) {
                type = QueryType.QUERY_NS;
            } else if (!parsed.getBoolean("ns") && parsed.getBoolean("mx")) {
                type = QueryType.QUERY_MX;
            } else if (parsed.getBoolean("ns") && parsed.getBoolean("mx")) {
                System.err.println("Cannot have both -ns and -mx switches at the same time. Exiting...");
                System.exit(127);
            }
            // TODO: parse and send info to client
            var server = parsed.getString("@server")
                    .replace("[", "")
                    .replace("]", "")
                    .replace("@", "");
            var timeout = parsed.getInt("timeout");
            var retries = parsed.getInt("max_retries");
            var port = parsed.getInt("port");
            var client = new DnsClient(server, timeout, retries, port, type);
            var response = client.performDnsRequest("google.ca");
            System.out.println(response.getResponseString());
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            parser.printHelp();
            System.exit(127);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(127);
        }

    }
}
