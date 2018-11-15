package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application{

      //////////////////////////
     //  V.A.R.I.A.B.L.E.S.  //
    //////////////////////////

      ///////////////////////////////////////
     //  G.E.T.T.E.R.S. + S.E.T.T.E.R.S.  //
    ///////////////////////////////////////

      ////////////////////////////////
     //  C.O.N.S.T.R.U.C.T.O.R.S.  //
    ////////////////////////////////

      //////////////////////
     //  M.E.T.H.O.D.S.  //
    //////////////////////
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main_View.fxml"));
        primaryStage.setTitle("Project I.");
        Scene newScene = new Scene(root);
        primaryStage.setScene(newScene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("System and threads are shut down!");
                System.exit(1);
            }
        });
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public void init() {
    }
    public static void main(String[] args) {
        launch(args);
    }

}
