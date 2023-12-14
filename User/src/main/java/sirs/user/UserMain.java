package sirs.user;

import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import sirs.user.service.BombAppetit;

public class UserMain {
    public static void main(String[] args) {
        try {
            System.out.println("Starting user...");
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(Files.newInputStream(Paths.get("src/main/resources/user.truststore")), "changeme".toCharArray());
            X509Certificate CACertificate = (X509Certificate) trustStore.getCertificate("ca");
            X509Certificate serverCertificate = (X509Certificate) trustStore.getCertificate("server");

            SslContext sslContext = GrpcSslContexts.configure(SslContextBuilder.forClient().trustManager(serverCertificate, CACertificate)).build();

            String target = "localhost" + ":" + 5000;
            ManagedChannel channel = NettyChannelBuilder.forTarget(target).sslContext(sslContext).build();
            BombAppetit bombAppetit = new BombAppetit(channel);

            char option = '1';
            System.out.println("\n\tWELCOME TO BOMB APPETIT");
           while (option != '0') {
                showMenu();
                option = (char) System.in.read();
                System.in.read();
                switch (option) {
                    case '5':
                        //bombAppetit.doReview();
                        break;
                    case '4':
                        //bombAppetit.sendVoucher();
                        break;
                    case '3':
                        //bombAppetit.useVoucher();
                        break;
                    case '2':
                        //bombAppetit.checkAvailableVouchers();
                        break;
                    case '1':
                        bombAppetit.listAllRestaurants();
                        System.out.print("\nChoose a restaurant. If you want to go back, type 0: ");
                        String restaurantName = System.console().readLine();
                        if (restaurantName.equals("0")) {
                            break;
                        }
                        while (option != '0') {
                            showRestaurantsInformationMenu();
                            option = (char) System.in.read();
                            System.in.read();
                            switch (option) {
                                case '4':
                                    //change restaurant name
                                    System.out.print("\nChoose a restaurant: ");
                                    restaurantName = System.console().readLine();
                                    break;
                                case '3':
                                    //bombAppetit.getRestaurantVouchers(restaurantName);
                                    break;
                                case '2':
                                    //bombAppetit.getRestaurantReviews(restauranteName);
                                    break;
                                case '1':
                                    //bombAppetit.getRestaurantMenu(restauranteName);
                                    break;
                                case '0':
                                    break;
                                default:
                                    System.out.println("Invalid option");
                                    break;
                            }
                        }
                        option = '1';
                        break;
                    case '0':
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            }
            // close connections
            channel.shutdown();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showMenu() {
        System.out.println("\n------------------ Menu -----------------");
        System.out.println("| 1 - Restaurants information\t\t|");
        System.out.println("| 2 - Check available vouchers\t\t|");
        System.out.println("| 3 - Use voucher\t\t\t|");
        System.out.println("| 4 - Send voucher to other user\t|");
        System.out.println("| 5 - Do review of restaurant\t\t|");
        System.out.println("| 0 - Exit\t\t\t\t|");
        System.out.println("-----------------------------------------");
        System.out.print("\nChoose an option: ");
    }

    private static void showRestaurantsInformationMenu() {
        System.out.println("\n------------------ Info -----------------");
        System.out.println("| 1 - Get restaurant menu\t\t|");
        System.out.println("| 2 - Get restaurant reviews\t\t|");
        System.out.println("| 3 - Get restaurant vouchers\t\t|");
        System.out.println("| 4 - Change restaurant\t\t\t|");
        System.out.println("| 0 - Back\t\t\t\t|");
        System.out.println("-----------------------------------------");
        System.out.print("\nChoose an option: ");
    }
}
