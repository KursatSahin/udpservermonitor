package clientUI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    ObservableList<Request> requests = FXCollections.observableArrayList();

    private TreeItem<Request> clients;
    private TreeView<Request> tree;
    Label clientCountLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tree View Sample");

        // Client container
        clients = new TreeItem<>(new Request("Clients"));

        // initialize tree view into gui
        tree = new TreeView<Request>(clients);
        tree.setShowRoot(false);

        // Client count
        clientCountLabel = new Label("Client count: 0");
        clientCountLabel.setPadding(new Insets(5, 5, 5, 5));
        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(tree, clientCountLabel);
        AnchorPane.setTopAnchor(tree, 0.0);
        AnchorPane.setLeftAnchor(tree, 0.0);
        AnchorPane.setRightAnchor(tree, 0.0);
        AnchorPane.setBottomAnchor(tree, 30.0);
        AnchorPane.setBottomAnchor(clientCountLabel, 0.0);
//        root.setAlignment(clientCountLabel, Pos.TOP_LEFT);

        // Set tree table columns
//        TreeTableColumn<String, String> column = new TreeTableColumn<>("Column");

        // Start reader thread
        SocketThread socketThread = new SocketThread();
        socketThread.clients = clients;
        socketThread.clientCountLabel = clientCountLabel;

        Thread thread = new Thread(socketThread);
        thread.start();

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}