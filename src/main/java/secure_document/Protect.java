package main.java.secure_document;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Base64;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class Protect {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: protect <inputJsonFile> <restaurantePrivateKey> <userPublicKey> <outputJsonFile>");
            return;
        }

        try {
            String originalJson = new String(Files.readAllBytes(Paths.get(args[0])));
            protect(originalJson, args[1], args[2], args[3]);
            System.out.println("Ciphered JSON written to " + args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void protect(String originalJson, String restaurantPrivKeyPath, String userPubKeyPath, String outputJsonFile) throws Exception {
        // 
        PrivateKey restaurantPrivateKey = readPrivateKey(restaurantPrivKeyPath);
        PublicKey userPublicKey = readPublicKey(userPubKeyPath);

        // Symmetric Key Generation
        SecretKey symmetricKey = generateSymmetricKey();

        // Cipher Voucher (Symmetric Encryption)
        //String voucherInfo = originalJson; // For simplicity, using the entire JSON as voucher information
        String voucherInfo = JsonParser.parseString(originalJson).getAsJsonObject().getAsJsonObject("restaurantInfo").getAsJsonObject("mealVoucher").toString();
        CipherResult cipherResult = cipherVoucher(voucherInfo, symmetricKey);

        // Cipher Symmetric Key (Asymmetric Encryption)
        String encryptedSymmetricKey = cipherSymmetricKey(symmetricKey, userPublicKey);

        // Nonce Integration
        String nonce = generateNonce(cipherResult.iv);

        // Digital Signature
        //String dataToSign = originalJson + "," + cipherResult.encryptedVoucher + "," + Base64.getEncoder().encodeToString(cipherResult.iv) + "," + encryptedSymmetricKey + "," + nonce;
        //System.out.println(dataToSign);
        //String digitalSignature = createDigitalSignature(dataToSign, restaurantPrivateKey);

        // Build the ciphered JSON dynamically
        JsonObject originalJsonObject = JsonParser.parseString(originalJson).getAsJsonObject();
        JsonObject mealVoucherObject = new JsonObject();
        mealVoucherObject.addProperty("encryptedVoucher", cipherResult.encryptedVoucher);
        mealVoucherObject.addProperty("iv", Base64.getEncoder().encodeToString(cipherResult.iv));
        mealVoucherObject.addProperty("encryptedSymmetricKey", encryptedSymmetricKey);
        mealVoucherObject.addProperty("nonce", nonce);
        //mealVoucherObject.addProperty("signature", digitalSignature);
        originalJsonObject.getAsJsonObject("restaurantInfo").add("mealVoucher", mealVoucherObject);
        
        // digital signature
        String digitalSignature = createDigitalSignature(originalJsonObject.toString(), restaurantPrivateKey);
        originalJsonObject.addProperty("signature", digitalSignature);

        // Write JSON object to file using Gson
        try (FileWriter fileWriter = new FileWriter(outputJsonFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(originalJsonObject, fileWriter);
        }

    }

    private static SecretKey generateSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // Key size 256 bits
        return keyGenerator.generateKey();
    }

    private static CipherResult cipherVoucher(String voucherInfo, SecretKey symmetricKey) throws Exception {
        // Generate a random IV
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[12]; // GCM recommended IV size is 12 bytes
        random.nextBytes(iv);

        // Initialize cipher with the generated IV
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey, gcmParameterSpec);

        // Encrypt the voucher information
        byte[] encryptedVoucher = cipher.doFinal(voucherInfo.getBytes());

        // Return the result, including the IV
        return new CipherResult(Base64.getEncoder().encodeToString(encryptedVoucher), iv);
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

    private static String generateNonce(byte[] iv) {
        String timestamp = generateTimestamp(); // Current timestamp
        //int randomPart = new Random().nextInt(Integer.MAX_VALUE); // Random number

        // Combine timestamp, random number, and IV
        return timestamp + "_" + Base64.getEncoder().encodeToString(iv);
    }

    private static String generateTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return dateFormat.format(new Date());
    }

    private static PrivateKey readPrivateKey(String privateKeyPath) throws Exception {
        byte[] privEncoded = Files.readAllBytes(Paths.get(privateKeyPath));
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privEncoded);
        KeyFactory keyFacPriv = KeyFactory.getInstance("RSA");
        return keyFacPriv.generatePrivate(privSpec);
    }

    private static PublicKey readPublicKey(String publicKeyPath) throws Exception {
        byte[] pubEncoded = Files.readAllBytes(Paths.get(publicKeyPath));
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubEncoded);
        KeyFactory keyFacPub = KeyFactory.getInstance("RSA");
        return keyFacPub.generatePublic(pubSpec);
    }

    private static class CipherResult {
        String encryptedVoucher;
        byte[] iv;

        CipherResult(String encryptedVoucher, byte[] iv) {
            this.encryptedVoucher = encryptedVoucher;
            this.iv = iv;
        }
    }
}