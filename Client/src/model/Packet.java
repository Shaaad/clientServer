package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet {
    private String sender;
    private String message;

    public Packet(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public Packet() {

    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public void write(DataOutputStream dos) {
        try {
            dos.writeUTF(sender);
            dos.writeUTF(message);
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

}
