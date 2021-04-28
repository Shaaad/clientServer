package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import model.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Button setUsername;
    public TextField message;
    public ListView chat;
    private Socket socket;
    private String name = null;
    private List<String>  chatMessages = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatMessages = chat.getItems();
        connect();
        handle();
    }

    private void connect() {
        try {
            socket = new Socket("localhost", 8888);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server disabled");
        }
    }

    private void handle() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    if (dis.available() <= 0) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    else {
                        Packet packet = new Packet();
                        packet.read(dis);
                        try {
                            String newLine = String.format( "[%s] %s", packet.getSender(), packet.getMessage())/*packet.getSender() + ": " + packet.getMessage()*/;
                            chatMessages.add(newLine);
                        } catch (IllegalStateException ise) {ise.printStackTrace();}
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    private void end() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setName() {
        Pane pane = new Pane();

        TextField textField = new TextField();
        textField.setText("");
        if(name == null)
            textField.setPromptText("Введите ваше имя");
        else
            textField.setText(name);

        Button button = new Button();
        button.setText("Выбрать");
        button.setPrefWidth(100);
        button.setLayoutX(200);


        pane.getChildren().addAll(button, textField);

        Stage newWindow = new Stage();
        button.setOnAction(event -> {
            if (!textField.getText().isEmpty()) {
                name = textField.getText();
                newWindow.close();
                setUsername.setVisible(false);
            }
        });
        newWindow.setTitle("Вы больше не сможете сменить ваше имя");
        newWindow.setScene(new Scene(pane));
        newWindow.setResizable(false);
        newWindow.setAlwaysOnTop(true);
        newWindow.setResizable(false);
        newWindow.setWidth(600);
        newWindow.show();
    }

    public void enterName(ActionEvent actionEvent) {
        setName();
    }

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        if (name != null && !message.getText().isEmpty()) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            Packet packet = new Packet(name, message.getText());
            packet.write(dos);
            dos.flush();
            message.setText("");
        }
    }

}
