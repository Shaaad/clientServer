import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;

public class Packet {
    private String sender;
    private String message;

    public Packet() {

    }

    public void write(DataOutputStream dos) {
        try {
            dos.writeUTF(sender);
            dos.writeUTF(message);
        } catch (SocketException se) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(DataInputStream dis) {
        try {
            sender = dis.readUTF();
            message = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle() {
        Main.handlers.forEach(h -> Main.sendPacket(h.getClientSocket(), this));
    }
}
