package main.java.secure_document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Date;
import java.time.Instant;
import java.util.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
            JsonObject mealVoucher = restaurantInfo.getAsJsonObject("mealVouchers");
            String nonce = mealVoucher.get("nonce").getAsString();
            String signature = jsonObject.get("signature").getAsString();
            jsonObject.remove("signature");

            json = jsonObject.toString();

            // Display the retrieved fields
            System.out.println("Nonce: " + nonce);
            System.out.println("Signature: " + signature);

            PublicKey publicKey = readPublicKey(args[1]);

            checkSignature(json, publicKey, signature);
            checkNonce(nonce);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkSignature(String json, PublicKey publicKey, String signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(json.getBytes());
        boolean result = sig.verify(Base64.getDecoder().decode(signature));
        if (result) {
            System.out.println("Signature is valid");
        } else {
            System.out.println("Signature is invalid");
            return;
        }
    }

    public static void checkNonce(String nonce) {
        long tenSecAgo = Instant.now().minusMillis(10000).toEpochMilli();
        long currTime = Instant.now().toEpochMilli();
        String[] splitStrings = nonce.split(" ");
        long timestamp = Long.parseLong(splitStrings[0]);
        String random = splitStrings[1]; // TODO

        if (timestamp >= tenSecAgo && timestamp <= currTime) {
            System.out.println("nonce timestamp is valid");
        } else {
            System.out.println("nonce timestamp is invalid");
        }

        System.out.println(currTime - timestamp);
        
    }


    public static PublicKey readPublicKey(String publicKeyPath) throws Exception {
        byte[] pubEncoded = readFile(publicKeyPath);
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubEncoded);
        KeyFactory keyFacPub = KeyFactory.getInstance("RSA");
        PublicKey pub = keyFacPub.generatePublic(pubSpec);
        return pub;
    }

    public static PrivateKey readPrivateKey(String privateKeyPath) throws Exception {
        byte[] privEncoded = readFile(privateKeyPath);
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privEncoded);
        KeyFactory keyFacPriv = KeyFactory.getInstance("RSA");
        PrivateKey priv = keyFacPriv.generatePrivate(privSpec);
        return priv;
    }

    private static byte[] readFile(String path) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] content = new byte[fis.available()];
        fis.read(content);
        fis.close();
        return content;
    }
}