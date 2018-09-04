package clientUI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    ObservableList<Request> requests = FXCollections.observableArrayList();

    private TreeItem<Request> clients;
    private TreeView<Request> tree;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tree View Sample");

        // Client container
        clients = new TreeItem<>(new Request());

        // initialize tree view into gui
        tree = new TreeView<Request>(clients);
        tree.setShowRoot(false);
        StackPane root = new StackPane();
        root.getChildren().add(tree);

        // Start reader thread
        SocketThread socketThread = new SocketThread();
        socketThread.clients = clients;
        Thread thread = new Thread(socketThread);
        thread.start();

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    public void addClient(Request request) {

        TreeItem<String> rootItem = new TreeItem<String>();
        rootItem.setExpanded(true);
        TreeItem<String> item = new TreeItem<String>();
        rootItem.setValue("Client");
    }
}