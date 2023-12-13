import io.grpc.Server;
import io.grpc.ServerBuilder;
import service.BombAppetitImpl;

public class ServerMain {
    public static void main(String[] args) {
        try {
            System.out.println("Starting server...");
            Server server = ServerBuilder.forPort(5000)
                    .addService(new BombAppetitImpl())
                    .build();
            server.start();
            System.out.println("Server started at " + server.getPort());
            System.out.println("Press any key to stop the server...");
            int a = System.in.read();
            server.shutdown();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
