## Generating certificates for postgres, user and root (Certificate Authority)
Generate keys
```sh
openssl genrsa -out root.key
```
```sh
openssl genrsa -out server.key
```
```sh
openssl genrsa -out user.key
```
Create certificate request
```sh
openssl req -new -key root.key -out root.csr
```
```sh
openssl req -new -key server.key -out server.csr

 - Common Name (e.g. server FQDN or YOUR name) []:<ip of database vm>
```
```sh
openssl req -new -key user.key -out user.csr

 - Common Name (e.g. server FQDN or YOUR name) []:postgres
```
Create a database to be able to sign other certificates
```sh
echo 01 > root.srl
```
Self sign the root
```sh
openssl x509 -req -days 365 -in root.csr -signkey root.key -out root.crt
```
Sign the user and server with root certificate and key
```sh
openssl x509 -req -days 365 -in server.csr -CA root.crt -CAkey root.key -out server.crt
```
```sh
openssl x509 -req -days 365 -in user.csr -CA root.crt -CAkey root.key -out user.crt
```
Convert them to .pem format
```sh
openssl x509 -in root.crt -out root.pem
```
```sh
openssl x509 -in server.crt -out server.pem
```
```sh
openssl x509 -in user.crt -out user.pem
```



## Setup of postgres server
```sh
sudo apt update
```
```sh
sudo apt install postgresql postgresql-client
```
```sh
sudo systemctl start postgresql
```
```sh
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'postgres';"
```
```sh
sudo -u postgres psql -c "CREATE DATABASE restaurantsdb;"
```
```sh
sudo systemctl enable postgresql
```
### Enable SSL on PostgreSQL:
Go to postgresql main folder
```sh
sudo cd /etc/postgresql/16/main     # 16 is postgres version 
```
Copy server key, and root and server certificates to this folder
```sh
sudo cp /path/to/server.pem /path/to/server.key /path/to/root.pem .
```
Change file user to db user (postgres)
```sh
sudo chown postgres:postgres root.pem server.key server.pem 
```
Change server.key permissions
```sh
sudo chmod 0600 server.key
```
Open your PostgreSQL configuration file (`postgresql.conf`) and set the following parameters:
```sh
listen_addresses = '*'                  # what IP address(es) to listen on;

ssl = on
ssl_cert_file = '/path/to/server.pem'     # Path to your server certificate
ssl_key_file = '/path/to/server.key'      # Path to your server private key
ssl_ca_file = '/path/to/root.pem'         # Path to your root certificate 
```

Modify the `pg_hba.conf` file to allow SSL connections to server ip
```sh
hostssl	restaurantsdb	postgres	192.168.0.20/24		scram-sha-256	clientcert=verify-full
```
Restart postgres 
```sh
sudo systemctl restart postgresql
```
Verify logs to see if its running properly
```sh
sudo cat /var/log/postgresql/postgresql-16-main.log
```

## Connect to the databse remotely:
Send user key and certificate, and root certificate to the grpc server
```sh
scp /path/to/user.pem /path/to/user.key /path/to/root.pem <server vm user>@<server vm ip>:$HOME/
```
Access postgres shell from the terminal 
```sh
psql "host=<db vm ip> user=postgres dbname=restaurantsdb sslcert=user.pem sslkey=user.key sslrootcert=root.pem sslmode=verify-full"
```
