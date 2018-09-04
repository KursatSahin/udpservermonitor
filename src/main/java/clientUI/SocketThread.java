package clientUI;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
                clients.getChildren().forEach((treeItem) -> {
                    Request currRequest = treeItem.getValue();
                    String currClientId = currRequest.getClientId();
                    String reqClientId = requestTreeItem.getValue().getClientId();
                    if (currClientId.equals(reqClientId)) {
                        treeItem.getChildren().add(requestTreeItem);
                        newClient.set(false);
                    }
                });
                if (newClient.get()) {
                    clients.getChildren().add(requestTreeItem);
                }
            });
        }
        return null;
    }


}
