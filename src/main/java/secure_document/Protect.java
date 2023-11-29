package main.java.secure_document;

import java.security.*;
import javax.crypto.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Base64;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Protect {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: protect <inputJsonFile> <outputJsonFile>");
            return;
        }

        try {
            String originalJson = new String(Files.readAllBytes(Paths.get(args[0])));
            protect(originalJson, args[1]);
            System.out.println("Ciphered JSON written to " + args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void protect(String originalJson, String outputJsonFile) throws Exception {
        // Generate keys for testing purposes
        KeyPair restaurantKeyPair = generateKeyPair();
        KeyPair userKeyPair = generateKeyPair();

        PrivateKey restaurantPrivateKey = restaurantKeyPair.getPrivate();
        PublicKey userPublicKey = userKeyPair.getPublic();

        // Symmetric Key Generation
        SecretKey symmetricKey = generateSymmetricKey();

        // Cipher Voucher (Symmetric Encryption)
        String voucherInfo = originalJson; // For simplicity, using the entire JSON as voucher information
        String encryptedVoucher = cipherVoucher(voucherInfo, symmetricKey);

        // Cipher Symmetric Key (Asymmetric Encryption)
        String encryptedSymmetricKey = cipherSymmetricKey(symmetricKey, userPublicKey);

        // Nonce Integration
        String nonce = generateNonce();

        // Digital Signature
        String dataToSign = originalJson + encryptedVoucher + encryptedSymmetricKey + nonce;
        String digitalSignature = createDigitalSignature(dataToSign, restaurantPrivateKey);

        // Build the ciphered JSON dynamically
        JsonObject originalJsonObject = JsonParser.parseString(originalJson).getAsJsonObject();
        JsonObject mealVoucherObject = new JsonObject();
        mealVoucherObject.addProperty("encryptedVoucher", encryptedVoucher);
        mealVoucherObject.addProperty("encryptedSymmetricKey", encryptedSymmetricKey);
        mealVoucherObject.addProperty("nonce", nonce);
        mealVoucherObject.addProperty("signature", digitalSignature);
        originalJsonObject.getAsJsonObject("restaurantInfo").add("mealVoucher", mealVoucherObject);

        // Write JSON object to file using Gson
        try (FileWriter fileWriter = new FileWriter(outputJsonFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(originalJsonObject, fileWriter);
        }

    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Key size 2048 bits
        return keyPairGenerator.generateKeyPair();
    }

    private static SecretKey generateSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // Key size 256 bits
        return keyGenerator.generateKey();
    }

    private static String cipherVoucher(String voucherInfo, SecretKey symmetricKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
        byte[] encryptedVoucher = cipher.doFinal(voucherInfo.getBytes());
        return Base64.getEncoder().encodeToString(encryptedVoucher);
    }

    private static String cipherSymmetricKey(SecretKey symmetricKey, PublicKey userPublicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, userPublicKey);
        byte[] encryptedSymmetricKey = cipher.doFinal(symmetricKey.getEncoded());
        return Base64.getEncoder().encodeToString(encryptedSymmetricKey);
    }

    private static String createDigitalSignature(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    private static String generateNonce() {
        // Implement your logic to generate a nonce (timestamp + random number)
        // Return the generated nonce as a string
        return "<timestamp_random>";
    }
}
