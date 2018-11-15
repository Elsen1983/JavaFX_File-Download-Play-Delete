package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller {

    private final String currentFolderPath = System.getProperty("user.dir");
    private final String folderPath = currentFolderPath + "\\testFolders";
    private final String localFolderPath = folderPath + "\\local";
    //  icons
    //  https://glyphlab.com/stock-icons/previews/preview-24.html
    //  https://www.iconfinder.com/iconsets/32x32-free-design-icons
    private final Image ADDED_TO_LIST = new Image("resources/addtolist.png");
    private final Image MUST_ADD_TO_LIST = new Image("resources/addtolist2.png");
    @FXML // fx:id="serverFolderFilesName_LISTVIEW"
    public ListView serverFolderFilesName_LISTVIEW; // Value injected by FXMLLoader
    //////////////////////////
    //  V.A.R.I.A.B.L.E.S.  //
    //////////////////////////
    //paths
    private String desktopPath = System.getProperty("user.home") + "\\Desktop";
    private final String serverFolderPath = desktopPath + getServerFolderPath();
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML // fx:id="main_Pane"
    private Pane main_Pane; // Value injected by FXMLLoader
    @FXML // fx:id="refreshFiles_BTN"
    private Button refreshFiles_BTN; // Value injected by FXMLLoader
    @FXML // fx:id="delete_BTN"
    private Button delete_BTN; // Value injected by FXMLLoader
    @FXML // fx:id="openFile_BTN"
    private Button openFile_BTN; // Value injected by FXMLLoader
    @FXML // fx:id="actionStatus"
    private Label actionStatus; // Value injected by FXMLLoader
    @FXML // fx:id="localFolderFilesName_LISTVIEW"
    private ListView localFolderFilesName_LISTVIEW; // Value injected by FXMLLoader
    @FXML // fx:id="downloadFile_BTN"
    private Button downloadFile_BTN; // Value injected by FXMLLoader
    @FXML // fx:id="playFile_BTN"
    private Button playFile_BTN; // Value injected by FXMLLoader
    @FXML // fx:id="mediaView"
    private MediaView mediaView; // Value injected by FXMLLoader
    @FXML // fx:id="mp_slider"
    private Slider mp_slider; // Value injected by FXMLLoader
    @FXML // fx:id="buttonRectangle"
    private Rectangle buttonRectangle; // Value injected by FXMLLoader
    @FXML // fx:id="play_BTN"
    private Button play_BTN; // Value injected by FXMLLoader
    @FXML // fx:id="stop_BTN"
    private Button stop_BTN; // Value injected by FXMLLoader
    @FXML // fx:id="pause_BTN"
    private Button pause_BTN; // Value injected by FXMLLoader
    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader
    //openFiles
    private List<File> selectedFiles;
    private ObservableList<String> list = FXCollections.observableArrayList();
    //a second ObservableList for comparison
    private ObservableList<String> newList = FXCollections.observableArrayList();
    //setupLists_From_FileArrays
    private File[] listOfLocalFiles;
    private File[] listOfServerFiles;
    private ObservableList<String> obsListOfLocalFiles_Names = FXCollections.observableArrayList();
    private ObservableList<String> obsListOfServerFiles_Names = FXCollections.observableArrayList();
    private ObservableList<String> locOBS = FXCollections.observableArrayList();
    private ObservableList<String> serOBS = FXCollections.observableArrayList();
    private String selectedElement;
    private CheckFilesChange_FilesObservable cfc;

    ////////////////////////////////
    //  C.O.N.S.T.R.U.C.T.O.R.S.  //
    ////////////////////////////////
    public Controller() {

    }

    ///////////////////////////////////////
    //  G.E.T.T.E.R.S. + S.E.T.T.E.R.S.  //
    ///////////////////////////////////////

    //prints ObservableList
    public static void printElements(List<String> list, String version) {
        System.out.println("Size of " + version + " is :" + list.size());
        for (Object o : list) {
            System.out.println(o.toString());
        }
    }

    private String getServerFolderPath() {
        return getNames();
    }

    //////////////////////
    //  M.E.T.H.O.D.S.  //
    //////////////////////

    //getNAME
    private String getNames() {
        System.out.println("GETNAME called");

        readFolder_SingletonPattern.getInstance().setFile("\\server");

        return readFolder_SingletonPattern.getInstance().getFile().toString();

    }

    @FXML
    void refreshFiles(ActionEvent event) {

        loadFiles_From_Folders();
        setupLists_From_FileArrays();

        serverFolderFilesName_LISTVIEW.setDisable(false);
        refreshFiles_BTN.setDisable(true);
        actionStatus.setText("Any information about the actual processes.");
        actionStatus.setTextFill(Color.web("#000000"));

        //after the server folder was RE-Read start the files-checker THREAD again
        CheckFilesChange_FilesObservable cfc = new CheckFilesChange_FilesObservable(listOfServerFiles,
                serverFolderPath, actionStatus, refreshFiles_BTN, serverFolderFilesName_LISTVIEW);
        ListViewsChanger_FilesObserver lvc = new ListViewsChanger_FilesObserver(cfc, listOfServerFiles,
                serverFolderPath, actionStatus, refreshFiles_BTN, serverFolderFilesName_LISTVIEW);

    }

    @FXML
    void downloadFile(ActionEvent event) {
        String file = selectedElement;
        DownloadController download2 = new DownloadController(serverFolderPath, localFolderPath, file);
        download2.getByte();
        //refresh the lists
        loadFiles_From_Folders();
        setupLists_From_FileArrays();
    }

    @FXML
    void playFile(ActionEvent event) {

        System.out.println("Selected element before play was: " + selectedElement);

        String path = localFolderPath + "\\" + selectedElement;

        System.out.println(path);

        MediaPlayer_Controller mpc = new MediaPlayer_Controller(path, mediaView, mp_slider, play_BTN, pause_BTN, stop_BTN, buttonRectangle, actionStatus, selectedElement);
        mpc.startMediaPlayer();

        localFolderFilesName_LISTVIEW.getSelectionModel().clearSelection();
        selectedElement = null;
        playFile_BTN.setDisable(true);
        delete_BTN.setDisable(true);

        actionStatus.setText("Any information about the actual processes.");
        actionStatus.setTextFill(Color.web("#000000"));


    }

    @FXML
    void removeFile(ActionEvent event) {

        System.out.println("Selected element before delete was: " + selectedElement);

        String path = localFolderPath + "\\" + selectedElement;

        System.out.println(path);

        Delete_File_Controller delete = new Delete_File_Controller(path);
        delete.fileDelete();

        //refresh the lists
        loadFiles_From_Folders();
        setupLists_From_FileArrays();

    }

    //open files
    @FXML
    void openFile(ActionEvent event) {
        System.out.println("--------------------  Open Files");
        try {
            //FileChooser for select multiple files
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select media files");
            fileChooser.setInitialDirectory(new File(folderPath));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Media Files", "*.*"));
            //store the selected files in an List
            selectedFiles = fileChooser.showOpenMultipleDialog(null);
            addFilesToOBSList();
        } catch (Exception e) {
            actionStatus.setText("The selection was canceled");
        }
    }

    public void addFilesToOBSList() {
        System.out.println("1 - start addFilesToOBSList() method");
        System.out.println("1a - check the mainlist.");
        printElements(list, "List");
        //clear the second ObservableList
        newList.clear();
        System.out.println("2 - clear the newList");
        printElements(newList, "newList");
        //clear the ListView
        localFolderFilesName_LISTVIEW.getItems().clear();
        System.out.println("3 - Check the number of the selected-files array: " + selectedFiles.size());
        //populate the original and the new (empty) ObservableList from the file-list by a for loop
        for (int i = 0; i < selectedFiles.size(); i++) {
            System.out.println("4 - " + i + "st/nd/th start the first for-loop");
            newList.add(selectedFiles.get(i).getName());
            //going throughout the 2 observable list and check for any match
            for (String k : newList) {
                if (list.contains(k)) {
                    System.out.println("Match Found " + k);
                }
                //when the element from the newList is not in the original list, then add that into the original
                else {
                    System.out.println("Element is not in the list, so add it into.");
                    list.add(k);
                }
            }//end of nested for-loop
        }//end of for-loop

        printElements(list, "List");
        printElements(newList, "newList");

        //use Collection.sort() on the observableList to ordering that in alphabetical
        FXCollections.sort(list);

        addImageViewToElement(localFolderFilesName_LISTVIEW, ADDED_TO_LIST);


        //clear the file-list and the newList --> make it to ready for the next time
        selectedFiles.clear();
        newList.clear();
        printElements(list, "allinone");
        printElements(newList, "new");
    }


    //give the files list in the folder when the application is start
    private synchronized void loadFiles_From_Folders() {

        //files in server folder
        File folderServer = new File(serverFolderPath);

        if (!folderServer.exists()) {
            if (folderServer.mkdirs()) {
                System.out.println("Server Directory is created!");
            } else {
                System.out.println("Failed to create server directory!");
            }
        }
        listOfServerFiles = folderServer.listFiles();

        File folderLocal = new File(localFolderPath);
        if (!folderLocal.exists()) {
            if (folderLocal.mkdirs()) {
                System.out.println("Local Directory is created!");
            } else {
                System.out.println("Failed to create local directory!");
            }
        }
        listOfLocalFiles = folderLocal.listFiles();

        //PRINT
        //server folder files
//        for (int i = 0; i < listOfServerFiles.length; i++) {
//            if (listOfServerFiles[i].isFile()) {
////                System.out.println("ServerSide File : " + listOfServerFiles[i].getName());
//            } else if (listOfServerFiles[i].isDirectory()) {
////                System.out.println("ServerSide Directory : " + listOfServerFiles[i].getName());
//            }
//        }
//        //local folder files
//        for (int j = 0; j < listOfLocalFiles.length; j++) {
//            if (listOfLocalFiles[j].isFile()) {
////                System.out.println("LocalSide File : " + listOfLocalFiles[j].getName());
//            } else if (listOfLocalFiles[j].isDirectory()) {
////                System.out.println("LocalSide Directory : " + listOfLocalFiles[j].getName());
//            }
//        }
    }

    private synchronized void setupLists_From_FileArrays() {
        obsListOfLocalFiles_Names.clear();
        obsListOfServerFiles_Names.clear();
        serOBS.clear();

        if (listOfLocalFiles.length != 0) {
            for (File listOfLocalFile : listOfLocalFiles) {
                String name = listOfLocalFile.getName();
                obsListOfLocalFiles_Names.add(name);
            }
        } else {
            System.out.println("No files in the local folder");
            actionStatus.setText("Is not any file in the local folder");
        }

        if (listOfServerFiles.length != 0) {
            for (File listOfServerFile : listOfServerFiles) {
                String name = listOfServerFile.getName();
                obsListOfServerFiles_Names.add(name);
            }
        } else {
            System.out.println("No files in the server folder");
            actionStatus.setText("Is not any file in the server folder");
        }

        printElements(obsListOfLocalFiles_Names, "localList");
        printElements(obsListOfServerFiles_Names, "serverList");

        for (String k : obsListOfServerFiles_Names) {

            if (obsListOfLocalFiles_Names.contains(k)) {
                addImageViewToElement(localFolderFilesName_LISTVIEW, ADDED_TO_LIST);
            } else {
                //make a new reduced list (elements which isnt in the local folder
                serOBS.add(k);
//                printElements(serOBS, "copied");
                addImageViewToElement(serverFolderFilesName_LISTVIEW, MUST_ADD_TO_LIST);
            }
            try {

                localFolderFilesName_LISTVIEW.setItems(obsListOfLocalFiles_Names);
                //using the reduced observablelist to populate the server listview
                serverFolderFilesName_LISTVIEW.setItems(serOBS);

                //add mousevent to listviews
                localFolderFilesName_LISTVIEW.setOnMouseClicked((MouseEvent e) -> {
                    try {
                        selectedElement = "";
                        System.out.println("local file Selected");
                        selectedElement = this.localFolderFilesName_LISTVIEW.getSelectionModel().getSelectedItem().toString();
//                    System.out.println(selectedElement);
                        downloadFile_BTN.setDisable(true);
                        playFile_BTN.setDisable(false);
                        delete_BTN.setDisable(false);
                    } catch (Exception ex) {
                        System.out.println(ex);
                        playFile_BTN.setDisable(true);
                        delete_BTN.setDisable(true);
                    }
                });

                serverFolderFilesName_LISTVIEW.setOnMouseClicked((MouseEvent e) -> {
                    System.out.println("server File Selected");
                    try {
                        selectedElement = "";
                        selectedElement = this.serverFolderFilesName_LISTVIEW.getSelectionModel().getSelectedItem().toString();
//                        System.out.println(selectedElement);
                        playFile_BTN.setDisable(true);
                        delete_BTN.setDisable(true);
                        downloadFile_BTN.setDisable(false);
                    } catch (Exception ex) {
                        System.out.println(ex);
                        playFile_BTN.setDisable(false);
                        delete_BTN.setDisable(false);
                    }
                });

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private synchronized void addImageViewToElement(ListView lv, Image img) {
        //https://stackoverflow.com/questions/33592308/javafx-how-to-put-imageview-inside-listview
        lv.setCellFactory(param -> new ListCell<String>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(img);
                    setText(name);
                    setGraphic(imageView);
                }
            }
        });
    }

    //IMPORTANT
    @FXML
    // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert main_Pane != null : "fx:id=\"main_Pane\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert refreshFiles_BTN != null : "fx:id=\"refreshFiles_BTN\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert downloadFile_BTN != null : "fx:id=\"downloadFile_BTN\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert playFile_BTN != null : "fx:id=\"playFile_BTN\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert delete_BTN != null : "fx:id=\"delete_BTN\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert openFile_BTN != null : "fx:id=\"openFile_BTN\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert actionStatus != null : "fx:id=\"actionStatus\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert anchorPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert localFolderFilesName_LISTVIEW != null : "fx:id=\"localFolderFilesName_LISTVIEW\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert serverFolderFilesName_LISTVIEW != null : "fx:id=\"serverFolderFilesName_LISTVIEW\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert mediaView != null : "fx:id=\"mediaView\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert buttonRectangle != null : "fx:id=\"buttonRectangle\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert mp_slider != null : "fx:id=\"mp_slider\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert pause_BTN != null : "fx:id=\"pause_BTN\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert stop_BTN != null : "fx:id=\"stop_BTN\" was not injected: check your FXML file 'Main_View.fxml'.";
        assert play_BTN != null : "fx:id=\"play_BTN\" was not injected: check your FXML file 'Main_View.fxml'.";


        mediaView.setVisible(false);
        buttonRectangle.setVisible(false);
        mp_slider.setVisible(false);
        play_BTN.setVisible(false);
        pause_BTN.setVisible(false);
        stop_BTN.setVisible(false);

        //populate the ListViews throughout these methods
        loadFiles_From_Folders();
        setupLists_From_FileArrays();

        //First time Create and Start the thread which checks the file changing in server folder
        CheckFilesChange_FilesObservable cfc = new CheckFilesChange_FilesObservable(listOfServerFiles, serverFolderPath, actionStatus, refreshFiles_BTN, serverFolderFilesName_LISTVIEW);

        ListViewsChanger_FilesObserver lvc = new ListViewsChanger_FilesObserver(cfc, listOfServerFiles, serverFolderPath, actionStatus, refreshFiles_BTN, serverFolderFilesName_LISTVIEW);

    }

}
