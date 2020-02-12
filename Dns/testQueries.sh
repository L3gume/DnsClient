#!/usr/bin/bash

# queries that should work

echo "================ A type queries ==============="
echo ""
echo "$(tput setaf 2)Test 1: www.mcgill.ca [A query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -t 2 -r 10 $1 www.mcgill.ca

echo ""
echo "$(tput setaf 2)Test 2: www.facebook.com [A query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -t 2 -r 10 $1 www.facebook.com

echo ""
echo "$(tput setaf 2)Test 3: www.reddit.com [A query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -t 2 -r 10 $1 www.reddit.com

echo ""
echo "$(tput setaf 2)Test 4: www.amazon.ca [A query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -t 2 -r 10 $1 www.amazon.ca

echo ""
echo "$(tput setaf 2)Test 5: www.instagram.com [A query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -t 2 -r 10 $1 www.instagram.com

echo ""
echo "$(tput setaf 2)Test 6: www.linkedin.com [A query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -t 2 -r 10 $1 www.linkedin.com

echo ""
echo "$(tput setaf 2)Test 7: www.microsoft.com [A query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -t 2 -r 10 $1 www.microsoft.com

echo ""
echo "================ MX type queries ==============="
echo ""
echo "$(tput setaf 2)Test 8: mail.mcgill.ca [MX query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -mx -t 2 -r 10 $1 mail.mcgill.ca 

echo ""
echo "$(tput setaf 2)Test 9: gmail.com [MX query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -mx -t 2 -r 10 $1 gmail.com 

echo ""
echo "$(tput setaf 2)Test 10: hotmail.com [MX query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -mx -t 2 -r 10 $1 hotmail.com 


echo ""
echo "================ NS type queries ==============="
echo ""
echo "$(tput setaf 2)Test 11: mcgill.ca [NS query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -ns -t 2 -r 10 $1 mcgill.ca 

echo ""
echo "$(tput setaf 2)Test 12: google.com [NS query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -ns -t 2 -r 10 $1 google.com 

echo ""
echo "$(tput setaf 2)Test 13: google.ca [NS query]$(tput sgr0)"
echo ""
java -jar out/artifacts/DnsClient_jar/DnsClient.jar -ns -t 2 -r 10 $1 google.ca
