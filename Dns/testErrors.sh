#!/usr/bin/bash

#queries that should output errors

echo ""
echo "$(tput setaf 2)Test 1: request timeout (1s)$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -t 1 -r 1 -p 420 $1 www.mcgill.ca

echo ""
echo "$(tput setaf 2)Test 2: request timeout (10s)$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -t 10 -r 1 -p 420 $1 www.mcgill.ca


echo ""
echo "$(tput setaf 2)Test 3: request timeout (1s) + 5 tries$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -t 1 -r 5 -p 420 $1 www.sdfasfgghljoirnmgcil.ca

