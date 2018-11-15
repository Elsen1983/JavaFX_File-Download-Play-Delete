package main;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;

public class ListViewsChanger_FilesObserver implements Files_Observer {

    private File [] files;
    private File folderServer;
    private Label actionStatus;
    private Button refreshFiles_BTN;
    private File[] listOfServerFiles;
    private ListView serverFolderFilesName_listview;
    private final String labelText = "SOMETHING CHANGED IN SERVER FOLDER!\nPress REFRESH button please!";

    private static int observerIDTracker = 0;
    private int observerID;

    private Files_Observable filesObservable;

    public ListViewsChanger_FilesObserver(Files_Observable filesObservable, File[] listOfServerFiles, String serverFolderPath, Label actionStatus, Button refreshFiles_BTN, ListView serverFolderFilesName_listview){
        this.filesObservable = filesObservable;
        this.observerID = ++observerIDTracker;
        System.out.println("New Observer " + this.observerID);
        filesObservable.add(this);
        //-----------------------------------------------------------------
        //for update listview
        this.listOfServerFiles = listOfServerFiles;
        this.folderServer = new File(serverFolderPath);
        this.actionStatus = actionStatus;
        this.refreshFiles_BTN = refreshFiles_BTN;
        this.serverFolderFilesName_listview = serverFolderFilesName_listview;
    }

    @Override
    public void update(File[] files) {
        this.files = files;
        printFiles();

        //Platform.runLater for changing the gui in fxml
        Platform.runLater(() -> {
            // Update UI here.
            actionStatus.setText(labelText);
            actionStatus.setFont(Font.font("Verdana", 14));
            actionStatus.setTextFill(Color.web("#dd1111"));
            refreshFiles_BTN.setDisable(false);
            serverFolderFilesName_listview.setDisable(true);
        });

    }





    private void printFiles() {

        System.out.println(observerID + "\n Files:");
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                System.out.println("ServerSide File : " + files[i].getName());
            } else if (files[i].isDirectory()) {
                System.out.println("ServerSide Directory : " + files[i].getName());
            }
        }

    }
}
