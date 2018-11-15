package main;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckFilesChange_FilesObservable implements Files_Observable {

    //////////////////////////
    //  V.A.R.I.A.B.L.E.S.  //
    //////////////////////////
    private final ArrayList<Files_Observer> listObserver;
    private File[] newFileList;


    private Controller c;
    private File folderServer;
    private Label actionStatus;
    private Button refreshFiles_BTN;
    private File[] listOfServerFiles;
    private ListView serverFolderFilesName_listview;


    private final String labelText = "SOMETHING CHANGED IN SERVER FOLDER!\nPress REFRESH button please!";

    //add it for make sure the thread will be stopped if we close the application
    private boolean stopWasRequested = false;
    //https://www.youtube.com/watch?v=a2aB9n472U0
    private final ExecutorService service = Executors.newCachedThreadPool();

    ///////////////////////////////////////
    //  G.E.T.T.E.R.S. + S.E.T.T.E.R.S.  //
    ///////////////////////////////////////

    ////////////////////////////////
    //  C.O.N.S.T.R.U.C.T.O.R.S.  //
    ////////////////////////////////
    CheckFilesChange_FilesObservable(File[] listOfServerFiles, String serverFolderPath, Label actionStatus, Button refreshFiles_BTN, ListView serverFolderFilesName_listview) {
        this.listObserver = new ArrayList<>();

        this.c = new Controller();
        this.listOfServerFiles = listOfServerFiles;
        this.folderServer = new File(serverFolderPath);
        this.actionStatus = actionStatus;
        this.refreshFiles_BTN = refreshFiles_BTN;
        this.serverFolderFilesName_listview = serverFolderFilesName_listview;


        service.submit(() -> {
            try {
                while (!stopWasRequested) {

                    System.out.println("CHECKER THREAD STARTED");

                    //the Thread runs in every 2 second
                    Thread.sleep(5000);

                    //create a new list of files from server folder
                    this.newFileList = folderServer.listFiles();

                    //call the method which used for comparing the numbers of files in server folder
                    checkFilesDifferent(newFileList);

                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    //////////////////////
    //  M.E.T.H.O.D.S.  //
    //////////////////////
    //a method for comparing the starting and the current numbers of the files in server folder
    public synchronized void checkFilesDifferent(File [] newFileList) {

        this.newFileList = newFileList;

        System.out.println(newFileList.length);
        System.out.println(listOfServerFiles.length);

        if (newFileList.length == listOfServerFiles.length) {
            //no changes in the server folder, so the thread will running without stop
        } else {
            //some changes happened in the server folder

            // stop the thread when a changing was detected -> refresh button will start it again.
            stopWasRequested = true;

            System.out.println("Different number of files in the folder!");

            //notify observers about the changes
            notifyObservers();

        }
    }


    @Override
    public void add(Files_Observer newObserver) {
        listObserver.add(newObserver);
    }

    @Override
    public void remove(Files_Observer deleteObserver) {
        int observerIndex = listObserver.indexOf(deleteObserver);
        System.out.println("Observer " + (observerIndex + 1) + " deleted");
        listObserver.remove(observerIndex);
    }

    @Override
    public void notifyObservers() {

        for (Files_Observer observer : listObserver) {
            observer.update(newFileList);
        }
    }

    //stop was requested, and service shut down
    public void shutdown() {
        service.shutdown();
    }

}
