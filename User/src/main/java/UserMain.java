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
            boolean result = secureCommunication.sendMessage();
            System.out.println("Result: " + result);
            System.out.println("Press any key to stop the user...");
            int a = System.in.read();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
