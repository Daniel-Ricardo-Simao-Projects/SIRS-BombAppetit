package main.java.secure_document;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;

public class Check {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: check <inputJsonFile> <publicKey>");
            return;
        }
        try {
            String json = new String(Files.readAllBytes(Paths.get(args[0])));
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonObject restaurantInfo = jsonObject.getAsJsonObject("restaurantInfo");

            // Extract nonce, signature, and remove signature from the JSON
            String nonce = restaurantInfo.get("nonce").getAsString();
            String signature = jsonObject.get("signature").getAsString();
            jsonObject.remove("signature");

            json = jsonObject.toString();

            // Display the retrieved fields
            System.out.println("Nonce: " + nonce);
            System.out.println("Signature: " + signature);

            // Read public key
            PublicKey publicKey = readPublicKey(args[1]);

            // Verify signature and check nonce
            checkSignature(json, publicKey, signature);
            checkNonce(nonce);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkSignature(String json, PublicKey publicKey, String signature) throws Exception {
        // Verify the digital signature
        boolean result = verifyDigitalSignature(json, publicKey, signature);
        if (result) {
            System.out.println("Signature is valid");
        } else {
            System.out.println("Signature is invalid");
        }
    }

    public static void checkNonce(String nonce) {
        // Split nonce into timestamp and random values
        String[] splitStrings = nonce.split(" ");
        long timestamp = Long.parseLong(splitStrings[0]);

        // Check if the timestamp is within the last 10 seconds
        long tenSecAgo = Instant.now().minusMillis(10000).toEpochMilli();
        long currTime = Instant.now().toEpochMilli();
        if (timestamp >= tenSecAgo && timestamp <= currTime) {
            System.out.println("Nonce timestamp is valid");
        } else {
            System.out.println("Nonce timestamp is invalid");
        }
    }

    public static boolean verifyDigitalSignature(String data, PublicKey publicKey, String signature) throws Exception {
        java.security.Signature sig = java.security.Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        return sig.verify(Base64.getDecoder().decode(signature));
    }

    public static PublicKey readPublicKey(String publicKeyPath) throws Exception {
            byte[] pubEncoded = readFile(publicKeyPath);
            X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubEncoded);
            KeyFactory keyFacPub = KeyFactory.getInstance("RSA");
            PublicKey pub = keyFacPub.generatePublic(pubSpec);
            return pub;
        }

     private static byte[] readFile(String path) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] content = new byte[fis.available()];
        fis.read(content);
        fis.close();
        return content;
    }

}
