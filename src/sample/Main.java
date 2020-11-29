package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;



public class Main extends Application {

    public static Stage m_primaryStage;

    private GameViewer gameViewer;

    public static Stage getM_primaryStage() {
        return m_primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
        System.out.println("Done!");
    }

    /**
     * starts the game window
     * @param primaryStage the game window
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        loadDefaultSaveFile(primaryStage);

        //open the game window
        primaryStage.setTitle(GameModel.M_GAMENAME);
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
        try {
            in = new FileInputStream("resource/GameLayouts/SampleGame.skb");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        initializeGame(in);
    }

    /**
     * initialize the game
     * @param input game file
     */
    public void initializeGame(InputStream input) {
        GameModel gameModel = new GameModel(input, true);
        GameController gameController = new GameController(gameModel);
        gameViewer = new GameViewer(gameController);

    }
}
