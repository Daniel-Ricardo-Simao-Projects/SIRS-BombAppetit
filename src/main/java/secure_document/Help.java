package main.java.secure_document;

public class Help {
    public static void displayHelp() {
        System.out.println("Usage:");
        System.out.println("  protect <inputJsonFile> <restaurantPrivateKey> <userPublicKey> <outputJsonFile>");
        System.out.println("  unprotect <inputJsonFile> <userPrivateKey> <outputJsonFile>");
        System.out.println("  check <inputJsonFile> <restaurantPublicKey>");
    }

    public static void main(String[] args) {
        displayHelp();
    }
}
