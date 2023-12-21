#!/bin/zsh

# This script generates credentials for the gRPC server

# Clean up old credentials
rm -rf *.crt *.csr *.key *.srl *.p12 *.keystore
rm -rf ../Server/src/main/resources/*

# Generate CA key and Certificate
openssl genrsa -out ca.key
openssl req -new -x509 -days 365 -key ca.key -out ca.crt -subj "/C=PT/CN=CA"

# Generate Server Certificate and Truststore
openssl genrsa -out server.key
openssl req -new -key server.key -out server.csr -subj "/CN=server/OU=server/O=IST/L=Lisbon/ST=Portugal/C=PT"
openssl x509 -req -days 365 -in server.csr -CA ca.crt -CAkey ca.key -extfile server-domains.ext -set_serial 01 -out server.crt

openssl pkcs12 -password pass:changeme -export -in server.crt -inkey server.key -out server.p12 -name server -CAfile ca.crt -caname root
keytool -importkeystore -deststorepass changeme -destkeypass changeme -destkeystore server.keystore -srckeystore server.p12 -srcstoretype PKCS12 -srcstorepass changeme -alias server
keytool -import -trustcacerts -file ca.crt -alias ca -keystore server.truststore -storepass changeme -noprompt

mv server.truststore ../Server/src/main/resources/
mv server.keystore ../Server/src/main/resources/
cp server-domains.ext ../Server/src/main/resources/

rm server.csr server.p12 server.key ca.key

echo "Server credentials generated successfully."
