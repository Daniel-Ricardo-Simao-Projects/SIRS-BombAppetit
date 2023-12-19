package sirs.server;

import io.grpc.Server;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyServerBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import sirs.server.service.BombAppetitImpl;

public class ServerMain {
    public static void main(String[] args) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(Files.newInputStream(Paths.get("src/main/resources/server.keystore")), "changeme".toCharArray());

            java.security.PrivateKey privateKey = (java.security.PrivateKey) keyStore.getKey("server", "changeme".toCharArray());
            PublicKey publicKey = keyStore.getCertificate("server").getPublicKey();
            KeyPair keyPair = new KeyPair(publicKey, privateKey);
            X509Certificate certificate = (X509Certificate) keyStore.getCertificate("server");

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(Files.newInputStream(Paths.get("src/main/resources/server.truststore")), "changeme".toCharArray());
            X509Certificate CACertificate = (X509Certificate) trustStore.getCertificate("ca");


            SslContext sslContext = GrpcSslContexts.configure(SslContextBuilder.forServer(keyPair.getPrivate(), certificate)
                            .trustManager(CACertificate))
                    .build();

            final Server server = NettyServerBuilder.forPort(5000).sslContext(sslContext)
                    .addService(new BombAppetitImpl())
                    .build();
            server.start();
            System.out.println("Server started at " + server.getPort());
            System.out.println("Press [Ctrl + c] to stop the server...");

            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutdown hook initiated. Shutting down server...");
                server.shutdown();
                System.out.println("Server shut down.");
            }));
            server.awaitTermination();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
