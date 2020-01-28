package com.dns;

import net.sourceforge.argparse4j.*;
import com.dns.DnsClient;

public class Main {

    public static void main(String[] args) {
        var parser = ArgumentParsers.newFor("DnsClient").build().defaultHelp(true)
                .description("Perform a dns request to given server.");
        parser.addArgument("-t", "--timeout")
                .required(false)
                .setDefault(5)
                .help("Specify the timeout (seconds) before retransmitting an unanswered query.");
        parser.addArgument("-r", "--max-retries")
                .required(false)
                .setDefault(3)
                .help("Set maximum number of times to retransmit before giving up.");
        parser.addArgument("-p", "--port")
                .required(false)
                .setDefault(53)
                .help("UDP port number of the DNS server");
        parser.addArgument("type").nargs(1)
                .choices("-mx", "-ns")
                .required(false)
                .help("Contact a mail/name server");
        parser.addArgument("@server").nargs(1)
                .required(true)
                .help("Specify the IP address of the DNS server.");
        parser.addArgument("name").nargs(1)
                .required(true)
                .help("Specify the name of the domain.");

        try {
            var parsed = parser.parseArgs(args);

            // TODO: parse and send info to client
            var client = new DnsClient(parsed.getString("@server"), parsed.getInt("timeout"), parsed.getInt("max-retries"), parsed.getInt("port"));
            var addr = client.performDnsRequest("google.ca");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
