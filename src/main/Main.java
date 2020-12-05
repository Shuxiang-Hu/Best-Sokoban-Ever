package main;

import model.GameModel;
import viewer.GameViewer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * Main class of sokoban project
 */
public class Main extends Application {

    /**
     * Game window
     */
    public static Stage m_primaryStage;

    /**
     * Viewer to control the layouts in the GUI
     */
    private GameViewer gameViewer;

    /**
     * Start of the game
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println("Done!");
    }

    /**
     * starts the game window
     * @param primaryStage the game window
     */
    @Override
    public void start(Stage primaryStage) {

        loadDefaultSaveFile(primaryStage);

        //open the game window
        primaryStage.setTitle(GameModel.getM_GAMENAME());
        primaryStage.setWidth(400);
        primaryStage.setHeight(400);
        primaryStage.setScene(new Scene(gameViewer.configureStartScreen()));
        primaryStage.show();

        //enable the screen to read in and show user inputs
        gameViewer.setEventFilter();
    }

    /**
     * read the game file and initialize game with it
     * @param stage the game window
     */
    void loadDefaultSaveFile(Stage stage) {
        m_primaryStage = stage;
        InputStream in = null;
        //load the default game file
        try {
            in = new FileInputStream("resource/GameLayouts/SampleGame.skb");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        initializeGame(in);
    }

    /**
     * Gets the main game window
     * @return The main game window
     */
    public static Stage getM_primaryStage() {
        return m_primaryStage;
    }

    /**
     * initialize the game
     * @param input game file
     */
    public void initializeGame(InputStream input) {
        gameViewer = GameViewer.getUniqueInstance();
    }
}
