#!/bin/zsh

# This script generates credentials for the gRPC client.

# Clean up old credentials
rm -rf *.crt *.csr *.key *.srl *.p12 *.keystore
rm -rf ../User/src/main/resources/*

# Generate Client Truststore
keytool -import -trustcacerts -file ca.crt -alias ca -keystore user.truststore -storepass changeme -noprompt
keytool -import -trustcacerts -file server.crt -alias server -keystore user.truststore -storepass changeme -noprompt

mv user.truststore ../User/src/main/resources/

rm *.crt

echo "Credentials generated successfully."
