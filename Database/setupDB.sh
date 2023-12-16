#!/bin/zsh

# Create ssl folder
mkdir ~/ssl
cd ~/ssl

# Generate keys
openssl genrsa -out root.key
openssl genrsa -out server.key
openssl genrsa -out user.key

openssl req -new -key root.key -out root.csr
openssl req -new -key server.key -out server.csr -subj "/CN=192.168.0.30"
openssl req -new -key user.key -out user.csr -subj "/CN=postgres"

echo 01 > root.srl

openssl x509 -req -days 365 -in root.csr -signkey root.key -out root.crt
openssl x509 -req -days 365 -in server.csr -CA root.crt -CAkey root.key -out server.crt
openssl x509 -req -days 365 -in user.csr -CA root.crt -CAkey root.key -out user.crt

openssl x509 -in root.crt -out root.pem
openssl x509 -in server.crt -out server.pem
openssl x509 -in user.crt -out user.pem

# Setup PostgreSQL server
sudo apt update
sudo apt install postgresql postgresql-client
sudo systemctl start postgresql
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'postgres';"
sudo -u postgres psql -c "CREATE DATABASE restaurantsdb;"
sudo systemctl enable postgresql

# Enable SSL on PostgreSQL
sudo cp ~/ssl/server.pem ~/ssl/server.key ~/ssl/root.pem /etc/postgresql/16/main
sudo chown postgres:postgres /etc/postgresql/16/main/root.pem /etc/postgresql/16/main/server.key /etc/postgresql/16/main/server.pem
sudo chmod 0600 /etc/postgresql/16/main/server.key

sudo sed -i "s/#listen_addresses = 'localhost'/listen_addresses = '*'/" /etc/postgresql/16/main/postgresql.conf
sudo sed -i "s/#ssl = off/ssl = on/" /etc/postgresql/16/main/postgresql.conf
sudo sed -i "s/#ssl_cert_file = 'server.crt'/ssl_cert_file = 'server.pem'/" /etc/postgresql/16/main/postgresql.conf
sudo sed -i "s/#ssl_key_file = 'server.key'/ssl_key_file = 'server.key'/" /etc/postgresql/16/main/postgresql.conf
sudo sed -i "s/#ssl_ca_file = 'root.crt'/ssl_ca_file = 'root.pem'/" /etc/postgresql/16/main/postgresql.conf

sudo sh -c "echo 'hostssl\trestaurantsdb\tpostgres\t192.168.0.20/24\tscram-sha-256\tclientcert=verify-full' >> /etc/postgresql/16/main/pg_hba.conf"

sudo systemctl restart postgresql

# Verify logs
sudo cat /var/log/postgresql/postgresql-16-main.log

# Send certificates to the grpc server
scp ~/ssl/user.pem ~/ssl/user.key ~/ssl/root.pem grpcserveruser@192.168.0.20:"$HOME"/

# Access PostgreSQL shell remotely
psql "host=192.168.0.30 user=postgres dbname=restaurantsdb sslcert=user.pem sslkey=user.key sslrootcert=root.pem sslmode=verify-full"
