package main.java.secure_document;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.time.Instant;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
            JsonObject json = protect(originalJson, args[1], args[2]);
            // Write JSON object to file using Gson
            try (FileWriter fileWriter = new FileWriter(args[3])) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(json, fileWriter);
            }
            System.out.println("Ciphered JSON written to " + args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JsonObject protect(String originalJson, String restaurantPrivKeyPath, String userPubKeyPath) throws Exception {
        // Read private key of the restaurant and public key of the user
        //System.out.println(System.getProperty("user.dir"));
        PrivateKey restaurantPrivateKey = readPrivateKey(restaurantPrivKeyPath);
        PublicKey userPublicKey = readPublicKey(userPubKeyPath);

        // Generate symmetric key
        SecretKey symmetricKey = generateSymmetricKey();
        
        // Iterate through each meal voucher and cipher individually
        JsonArray mealVouchersArray = JsonParser.parseString(originalJson)
                .getAsJsonObject().getAsJsonObject("restaurantInfo")
                .getAsJsonArray("mealVouchers");

        String nonce = "";
        JsonObject originalJsonObject = JsonParser.parseString(originalJson).getAsJsonObject();
        
        if (mealVouchersArray.size() == 0) {
            System.out.println("PROTECT: THERE IS NO VOUCHERS");
            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[12]; // GCM recommended IV size is 12 bytes
            random.nextBytes(iv);
            nonce = generateNonce(iv);
        } else {

            JsonArray encryptedVouchersArray = new JsonArray();
    
            for (JsonElement voucherElement : mealVouchersArray) {
                JsonObject voucherObject = voucherElement.getAsJsonObject();
                String voucherInfo = voucherObject.toString();
    
                // Cipher Voucher (Symmetric Encryption)
                CipherResult cipherResult = cipherVoucher(voucherInfo, symmetricKey);
    
                // Build encrypted voucher object
                JsonObject encryptedVoucherObject = new JsonObject();
                encryptedVoucherObject.addProperty("encryptedVoucher", cipherResult.encryptedVoucher);
                encryptedVoucherObject.addProperty("iv", Base64.getEncoder().encodeToString(cipherResult.iv));
    
                // Add encrypted voucher to the array
                encryptedVouchersArray.add(encryptedVoucherObject);
                
                nonce = generateNonce(encryptedVouchersArray.get(0).getAsJsonObject().get("iv").getAsString().getBytes());
                originalJsonObject.getAsJsonObject("restaurantInfo").add("mealVouchers", encryptedVouchersArray);
            }
        }




        // Cipher Symmetric Key (Asymmetric Encryption)
        String encryptedSymmetricKey = cipherSymmetricKey(symmetricKey, userPublicKey);
        originalJsonObject.getAsJsonObject("restaurantInfo").addProperty("encryptedSymmetricKey", encryptedSymmetricKey);
        
        // Nonce Integration
        originalJsonObject.getAsJsonObject("restaurantInfo").addProperty("nonce", nonce);
        
        // Digital Signature
        String dataToSign = originalJsonObject.toString();
        String digitalSignature = createDigitalSignature(dataToSign, restaurantPrivateKey);
        originalJsonObject.addProperty("signature", digitalSignature);
        
        //System.out.println(originalJsonObject);
        return originalJsonObject;
        
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
        //String timestamp = generateTimestamp(); // Current timestamp
        //int randomPart = new Random().nextInt(Integer.MAX_VALUE); // Random number
        long timestamp = generateTimestamp();
        String random = generateRandom();
        // Combine timestamp, random number, and IV
        return timestamp + " " + random;
    }

    private static long generateTimestamp() {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        //return dateFormat.format(new Date());
        return Instant.now().toEpochMilli();
    }

    private static String generateRandom() {
        byte[] randomBytes = new byte[12];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }

    public static PrivateKey readPrivateKey(String privateKeyPath) throws Exception {
        byte[] privEncoded = Files.readAllBytes(Paths.get(privateKeyPath));
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privEncoded);
        KeyFactory keyFacPriv = KeyFactory.getInstance("RSA");
        return keyFacPriv.generatePrivate(privSpec);
    }

    public static PublicKey readPublicKey(String publicKeyPath) throws Exception {
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