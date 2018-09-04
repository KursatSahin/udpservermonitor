package clientUI;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketThread extends Task {

    private static int PORT = 8082;
    private boolean running = true;
    public TreeView<Request> tree;
    public TreeItem<Request> clients;
    Label clientCountLabel;
    private int clientCount = 0;

    @Override
    public Object call() {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            System.out.println("Can't open UDP socket");
            e.printStackTrace();
        }
        byte[] receiveData = new byte[1024];
        while (running) {
            if (isCancelled()) {
                System.out.println("Task is cancelled");
                break;
            }
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                System.out.println("Can't receive datagram packet");
                e.printStackTrace();
            }
            String packetContent = new String(receivePacket.getData()).split("\0")[0];
            System.out.println("RECEIVED: " + receivePacket.getLength() + " -- " + packetContent);

            String[] packetComponents = packetContent.split(",");

            String clientId = packetComponents[0];
            String sequenceId = packetComponents[1];
            String protocol = packetComponents[2];
            String serviceName = packetComponents[3];

            TreeItem<Request> requestTreeItem = new TreeItem<>(new Request(clientId, sequenceId, protocol, serviceName));
            Platform.runLater(() -> {
                AtomicBoolean newClient = new AtomicBoolean(true);
                // If we got same client id before add it as a child.
                String newClientId = requestTreeItem.getValue().getClientId();
                clients.getChildren().forEach((treeItem) -> {
                    Request currRequest = treeItem.getValue();
                    String currClientId = currRequest.getClientId();
                    if (currClientId.equals(newClientId)) {
                        treeItem.getChildren().add(requestTreeItem);
                        newClient.set(false);
                    }
                });

                // If we got new client, add a category for it and insert this client request under this category
                // And increment client count by 1
                if (newClient.get()) {
                    TreeItem<Request> newCategory = new TreeItem<>(new Request(newClientId));
                    newCategory.getChildren().add(requestTreeItem);
                    clients.getChildren().add(newCategory);
                    clientCountLabel.setText("Client count: " + (++clientCount));
                }
            });
        }
        return null;
    }


}
