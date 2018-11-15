package main;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.File;

class MediaPlayer_Controller {

    //////////////////////////
    //  V.A.R.I.A.B.L.E.S.  //
    //////////////////////////
    private MediaView mediaView;
    private MediaPlayer mp;
    private Media me;
    private Duration duration;
    private Slider mp_slider;
    private Button play_BTN;
    private Button stop_BTN;
    private Button pause_BTN;
    private Rectangle buttonRectangle;
    private Label actionStatus;
    private String path;
    private String selectedElement;
    private File file;


    ///////////////////////////////////////
    //  G.E.T.T.E.R.S. + S.E.T.T.E.R.S.  //
    ///////////////////////////////////////

    ////////////////////////////////
    //  C.O.N.S.T.R.U.C.T.O.R.S.  //
    ////////////////////////////////
    public MediaPlayer_Controller(String path, MediaView mediaView, Slider mp_slider, Button play_BTN, Button pause_BTN,
                                  Button stop_BTN, Rectangle buttonRectangle, Label actionStatus, String selectedElement) {
        this.path = path;
        this.mediaView = mediaView;
        this.mp_slider = mp_slider;
        this.play_BTN = play_BTN;
        this.pause_BTN = pause_BTN;
        this.stop_BTN = stop_BTN;
        this.buttonRectangle = buttonRectangle;
        this.actionStatus = actionStatus;
        this.selectedElement = selectedElement;

        this.mp_slider.setVisible(true);
        this.buttonRectangle.setVisible(true);
        this.play_BTN.setVisible(true);
        this.pause_BTN.setVisible(true);
        this.stop_BTN.setVisible(true);

        this.file = new File(path);


//        startMediaPlayer();
    }

    //////////////////////
    //  M.E.T.H.O.D.S.  //
    //////////////////////
    void startMediaPlayer() {

        Platform.runLater(() -> {


            // check if the file exists
            boolean exists = file.exists();
            if (exists == true) {
                // printing the permissions associated with the file
            } else {
                System.out.println("File not found.");
            }

            actionStatus.setText("Any information about the actual processes.");
            actionStatus.setTextFill(Color.web("#000000"));


            System.out.println("MEDIAPLAYER START");

            try {
                //https://www.youtube.com/watch?v=sjiS4mhb0gQ
                //https://www.youtube.com/watch?v=8v7zvNvBcQ4
                me = new Media(file.toURI().toString());
                mp = new MediaPlayer(me);
                mediaView.setMediaPlayer(mp);

                mp.setAutoPlay(true);
                mp.play();
                mediaView.setVisible(true);

                mediaView.setSmooth(true);

                mediaView.setPreserveRatio(false);
                mediaView.setScaleX(0.75);
                mediaView.setScaleY(0.7);

                mediaView.setTranslateX(-80);
                mediaView.setTranslateY(-80);

                // Create the DropShadow effect
                DropShadow dropshadow = new DropShadow();
                dropshadow.setOffsetY(5.0);
                dropshadow.setOffsetX(5.0);
                dropshadow.setColor(Color.BLACK);
                mediaView.setEffect(dropshadow);

                DoubleProperty width = mediaView.fitWidthProperty();
                DoubleProperty height = mediaView.fitHeightProperty();

                width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

                //for slider
                mp_slider.setMinHeight(30);
                mp_slider.setValue(0);
//                mp_slider.setValue(mp.getTotalDuration().toSeconds());
                //listener for video jump using slider
                mp_slider.valueProperty().addListener(arg0 -> {
                    if (mp_slider.isPressed()) {
                        mp.seek(mp.getMedia().getDuration().multiply(mp_slider.getValue() / 100));
                    }
                });
//                mp.currentTimeProperty().addListener((observable, oldValue, newValue) -> mp_slider.setValue(newValue.toSeconds()));


                play_BTN.setOnAction(event -> {
                    System.out.println("Play pressed!");
                    mp.play();
                });

                pause_BTN.setOnAction(event -> {
                    System.out.println("Pause pressed!");
                    mp.pause();
                });

                stop_BTN.setOnAction(event -> {
                    System.out.println("Stop pressed!");
                    mp.stop();

//                    mediaView.setMediaPlayer(null);
                    mediaView.setVisible(false);

                    mp_slider.setVisible(false);
                    buttonRectangle.setVisible(false);
                    play_BTN.setVisible(false);
                    pause_BTN.setVisible(false);
                    stop_BTN.setVisible(false);

                    actionStatus.setText("MediaPlayer stopped");
                    //https://community.oracle.com/thread/2492481
                    //for close the file and drop that out from garbage collection we need next 2 line (not guaranty its works in all situation)
//                    me = null;
//                    mp= null;
                    System.gc();
                });


            } catch (Exception e) {

                System.out.println("Cannot play media because " + e);
                actionStatus.setText("The selected Media is not supported!");
                actionStatus.setFont(Font.font("Verdana", 14));
                actionStatus.setTextFill(Color.web("#dd1111"));

                mediaView.setMediaPlayer(null);
                mediaView.setVisible(false);

                mp_slider.setVisible(false);
                buttonRectangle.setVisible(false);
                play_BTN.setVisible(false);
                pause_BTN.setVisible(false);
                stop_BTN.setVisible(false);

                //https://community.oracle.com/thread/2492481
                //for close the file and drop that out from garbage collection we need next 2 line (not guaranty its works in all situation
                me = null;
                System.gc();
            }
        });
    }
}
