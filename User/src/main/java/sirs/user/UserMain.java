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
        if (args.length != 1) {
            System.out.println("Username needed (-Dexec.args=\"username\")");
            return;
        }
        try {
            System.out.printf("Starting user: %s ...\n", args[0]);
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(Files.newInputStream(Paths.get("src/main/resources/user.truststore")),
                    "changeme".toCharArray());
            X509Certificate CACertificate = (X509Certificate) trustStore.getCertificate("ca");
            X509Certificate serverCertificate = (X509Certificate) trustStore.getCertificate("server");

            SslContext sslContext = GrpcSslContexts
                    .configure(SslContextBuilder.forClient().trustManager(serverCertificate, CACertificate)).build();

            String target = "localhost" + ":" + 5000;
            ManagedChannel channel = NettyChannelBuilder.forTarget(target).sslContext(sslContext).build();
            String user = args[0];
            BombAppetit bombAppetit = new BombAppetit(channel, user);

            char option = '1';
            System.out.println("\n\tWELCOME TO BOMB APPETIT");
            while (option != '0') {
                showMenu();
                option = (char) System.in.read();
                System.in.read();
                switch (option) {
                    case '1':
                        bombAppetit.listAllRestaurants();
                        System.out.print("\nChoose a restaurant. If you want to go back, type 0: ");
                        String restaurantName = System.console().readLine();
                        if (restaurantName.equals("0")) {
                            break;
                        }
                        while (option != '0') {
                            System.out.print("\t\t" + restaurantName);
                            showRestaurantsInformationMenu();
                            option = (char) System.in.read();
                            System.in.read();
                            switch (option) {
                                case '6':
                                    // bombAppetit.sendVoucherToOtherUser(restaurantName);
                                    break;
                                case '5':
                                    bombAppetit.useVoucher(restaurantName);
                                    break;
                                case '4':
                                    bombAppetit.getRestaurantVouchers(restaurantName);
                                    break;
                                case '3':
                                    bombAppetit.doReviewOfRestaurant(restaurantName);
                                    break;
                                case '2':
                                    bombAppetit.getRestaurantReviews(restaurantName);
                                    break;
                                case '1':
                                    bombAppetit.getRestaurantMenu(restaurantName);
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
        System.out.println("\n--------------------------------");
        System.out.println("| 1 - Select Restaurant\t\t|");
        System.out.println("| 0 - Exit\t\t\t|");
        System.out.println(  "--------------------------------");
        System.out.print("\nChoose an option: ");
    }


    private static void showRestaurantsInformationMenu() {
        System.out.println("\n------------------ Info -----------------");
        System.out.println("| 1 - Get restaurant menu\t\t|");
        System.out.println("| 2 - Get restaurant reviews\t\t|");
        System.out.println("| 3 - Do review of restaurant\t\t|");
        System.out.println("| 4 - Get restaurant vouchers\t\t|");
        System.out.println("| 5 - Use voucher\t\t\t|");
        System.out.println("| 6 - Send voucher to other user\t|");
        System.out.println("| 0 - Back\t\t\t\t|");
        System.out.println("-----------------------------------------");
        System.out.print("\nChoose an option: ");
    }
}
