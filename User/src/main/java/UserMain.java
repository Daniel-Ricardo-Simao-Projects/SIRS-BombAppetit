import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import service.BombAppetit;

public class UserMain {
    public static void main(String[] args) {
        try {
            System.out.println("Starting user...");
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5000)
                    .usePlaintext()
                    .build();
            BombAppetit secureCommunication = new BombAppetit(channel);
            char option = '1';
            System.out.println("\n\tWELCOME TO BOMB APPETIT");
           while (option != '0') {
                showMenu();
                option = (char) System.in.read();
                System.in.read();
                switch (option) {
                    case '5':
                        //secureCommunication.doReview();
                        break;
                    case '4':
                        //secureCommunication.sendVoucher();
                        break;
                    case '3':
                        //secureCommunication.useVoucher();
                        break;
                    case '2':
                        //secureCommunication.checkAvailableVouchers();
                        break;
                    case '1':
                        while (option != '0') {
                            showRestaurantsInformationMenu();
                            option = (char) System.in.read();
                            System.in.read();
                            switch (option) {
                                case '4':
                                    //secureCommunication.getRestaurantVouchers();
                                    break;
                                case '3':
                                    //secureCommunication.getRestaurantReviews();
                                    break;
                                case '2':
                                    //secureCommunication.getRestaurantMenu();
                                    break;
                                case '1':
                                    secureCommunication.getRestaurants();
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
        System.out.println("| 1 - Get restaurants\t\t\t|");
        System.out.println("| 2 - Get restaurant menu\t\t|");
        System.out.println("| 3 - Get restaurant reviews\t\t|");
        System.out.println("| 4 - Get restaurant vouchers\t\t|");
        System.out.println("| 0 - Back\t\t\t\t|");
        System.out.println("-----------------------------------------");
        System.out.print("\nChoose an option: ");
    }
}
