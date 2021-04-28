import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {

    private static ServerSocket server;
    private static ServerHandler serverHandler;
    public static List<ClientHandler> handlers = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        start();
        handle();
        end();
    }

    private static void start()  {
        try {
            server = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  static void  handle() throws InterruptedException {
        serverHandler = new ServerHandler(server);
        serverHandler.start();
        readChat();
    }

    private static void readChat() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("#end"))
                    end();
                else
                    System.out.println("Unknown command");
            } else {
                Thread.sleep(10);
            }
        }
    }

    public static void sendPacket(Socket receiver, Packet packet) {
        try {
            DataOutputStream dos = new DataOutputStream(receiver.getOutputStream());
            packet.write(dos);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void end() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
