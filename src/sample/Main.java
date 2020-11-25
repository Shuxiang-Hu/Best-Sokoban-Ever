package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.InputStream;



public class Main extends Application {

    public static Stage primaryStage;

    private GameViewer gameViewer;

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
        primaryStage.setTitle(GameModel.GAME_NAME);
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
        primaryStage = stage;
        InputStream in = getClass().getClassLoader().getResourceAsStream("sample/SampleGame.skb");
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
