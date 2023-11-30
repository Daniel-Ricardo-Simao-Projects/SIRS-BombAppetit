package main.java.secure_document;

import com.google.gson.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Unprotect {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: unprotect <inputJsonFile> <userPrivateKey> <outputJsonFile>");
            return;
        }
        try {
            unprotect(args[0], args[1], args[2]);
            System.out.println("Unprotected " + args[0] + " to " + args[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void unprotect(String inputJson, String userPrivateKeyPath, String outputJson) throws Exception {
        // Get keys
        PrivateKey userPrivateKey = readPrivateKey(userPrivateKeyPath);

        try(FileReader fileReader = new FileReader(inputJson)) {
            // Get input file content
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);
            JsonObject mealVoucher = jsonObject.getAsJsonObject("restaurantInfo").getAsJsonObject("mealVoucher");
            String encryptedVoucherString = mealVoucher.remove("encryptedVoucher").getAsString();
            String encryptedSymmetricKeyString = mealVoucher.remove("encryptedSymmetricKey").getAsString();
            String iv = mealVoucher.remove("iv").getAsString();
            mealVoucher.remove("nonce");
            mealVoucher.remove("signature");

            // Decipher Symmetric Key (Asymmetric Decryption)
            SecretKey secretKey = decipherSymmetricKey(encryptedSymmetricKeyString, userPrivateKey);

            // Decipher Voucher (Symmetric Decryption)
            String decipheredVoucher = decipherVoucher(encryptedVoucherString, secretKey, iv);

            // Rebuild Voucher/Menu
            JsonElement voucherElement = JsonParser.parseString(decipheredVoucher);
            mealVoucher.add("code", voucherElement.getAsJsonObject().get("code"));
            mealVoucher.add("description", voucherElement.getAsJsonObject().get("description"));

            // Write to output file
            try(FileWriter fileWriter = new FileWriter(outputJson)) {
                Gson gsonOutput = new GsonBuilder().setPrettyPrinting().create();
                gsonOutput.toJson(jsonObject, fileWriter);
            }
        }
    }

    private static PrivateKey readPrivateKey(String privateKeyPath) throws Exception {
        byte[] privEncoded = Files.readAllBytes(Paths.get(privateKeyPath));
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privEncoded);
        KeyFactory keyFacPriv = KeyFactory.getInstance("RSA");
        return keyFacPriv.generatePrivate(privSpec);
    }

    private static String decipherVoucher(String cipheredVoucherString, SecretKey symmetricKey, String iv) throws Exception {
        byte[] cipheredVoucherBytes = Base64.getDecoder().decode(cipheredVoucherString);
        byte[] ivBytes = Base64.getDecoder().decode(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, ivBytes);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, symmetricKey, gcmParameterSpec);
        byte[] voucherBytes = cipher.doFinal(cipheredVoucherBytes);
        return new String(voucherBytes);
    }

    private static SecretKey decipherSymmetricKey(String encryptedSymmetricKeyString, PrivateKey userPrivateKey) throws Exception {
        byte[] encryptedSymmetricKeyBytes = Base64.getDecoder().decode(encryptedSymmetricKeyString);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, userPrivateKey);
        byte[] SymmetricKeyBytes = cipher.doFinal(encryptedSymmetricKeyBytes);
        return new SecretKeySpec(SymmetricKeyBytes, "AES");
    }
}
