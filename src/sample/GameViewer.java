package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameViewer {
    private GameModel gameModel;
    private GameController gameController;
    private GridPane gameGrid;
    GameViewer(GameModel gameModel,GameController gameController){
        this.gameModel = gameModel;
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public GridPane configureGameScreen(){
        //initialize menus
        MenuBar mainMenuBar = new MenuBar();

        MenuItem menuItemSaveGame = new MenuItem("Save Game");
        menuItemSaveGame.setDisable(true);
        menuItemSaveGame.setOnAction(actionEvent -> gameController.requestSaveGame());
        MenuItem menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setOnAction(actionEvent -> {
            gameController.requestLoadGame();
            if (gameController.getGameModel().getSaveFile() != null) {
                if (GameModel.isDebugActive()) {
                    GameModel.logger.info("Loading save file: " + gameController.getGameModel().getSaveFile().getName());
                }
                try{
                    reloadGame(new FileInputStream(gameController.getGameModel().getSaveFile()));
                    reloadGrid();
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }

            }
        });

        MenuItem menuItemExit = new MenuItem("Exit");
        menuItemExit.setOnAction(actionEvent -> closeGame());
        Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(menuItemSaveGame, menuItemLoadGame, new SeparatorMenuItem(), menuItemExit);

        MenuItem menuItemUndo = new MenuItem("Undo");
        menuItemUndo.setDisable(true);
        menuItemUndo.setOnAction(actionEvent -> gameController.requestUndo());
        RadioMenuItem radioMenuItemMusic = new RadioMenuItem("Toggle Music");
        radioMenuItemMusic.setOnAction(actionEvent -> gameController.callToggleMusic());
        RadioMenuItem radioMenuItemDebug = new RadioMenuItem("Toggle Debug");
        radioMenuItemDebug.setOnAction(actionEvent -> {
            gameController.callToggleDebug();
            reloadGrid();
        });
        MenuItem menuItemResetLevel = new MenuItem("Reset Level");
        Menu menuLevel = new Menu("Level");
        menuLevel.setOnAction(actionEvent -> resetLevel());
        menuLevel.getItems().addAll(menuItemUndo, radioMenuItemMusic, radioMenuItemDebug,
                new SeparatorMenuItem(), menuItemResetLevel);

        MenuItem menuItemGame = new MenuItem("About This Game");
        Menu menuAbout = new Menu("About");
        menuAbout.setOnAction(actionEvent -> showAbout());
        menuAbout.getItems().addAll(menuItemGame);
        mainMenuBar.getMenus().addAll(menuFile, menuLevel, menuAbout);

        //set layout of the game screen
        gameGrid = new GridPane();
        GridPane root = new GridPane();
        root.add(mainMenuBar, 0, 0);
        root.add(gameGrid, 0, 1);
        return root;
    }

    private void reloadGame(FileInputStream fileInputStream) {
        GameModel gameModel = new GameModel(fileInputStream, true);
        gameController.setGameModel(gameModel);
        setGameController(gameController);
        setGameModel(gameController.getGameModel());
    }

    /**
     * closes the game
     */
    public void closeGame() {
        System.exit(0);
    }
    public void newDialog(String dialogTitle, String dialogMessage, Effect dialogMessageEffect) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(Main.primaryStage);
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);

        Text text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.CENTER);
        text1.setFont(javafx.scene.text.Font.font(14));

        if (dialogMessageEffect != null) {
            text1.setEffect(dialogMessageEffect);
        }

        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);
        dialogVbox.getChildren().add(text1);

        Scene dialogScene = new Scene(dialogVbox, 350, 150);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    /**
     * shows message in a dialog window
     */
    public void showAbout() {
        String title = "About This Game";
        String message = "Enjoy the Game!\n";

        newDialog(title, message, null);
    }

    /**
     * reloads the grid for the next game level
     */
    public void reloadGrid() {
        if (gameController.getGameModel().isGameComplete()) {
            showVictoryMessage();
            return;
        }

        Level currentLevel = gameController.getGameModel().getCurrentLevel();
        Level.LevelIterator levelGridIterator = (Level.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getCurrentPosition());
        }gameGrid.autosize();
        Main.primaryStage.sizeToScene();
    }

    /**
     * shows victory message
     */
    public void showVictoryMessage() {
        String dialogTitle = "Game Over!";
        String dialogMessage = "You completed " + gameController.getGameModel().getMapSetName() + " in " + gameController.getGameModel().getMovesCount() + " moves!";
        MotionBlur mb = new MotionBlur(2, 3);

        newDialog(dialogTitle, dialogMessage, mb);
    }



    public void addObjectToGrid(GameObject gameObject, Point location) {
        GraphicObject graphicObject = new GraphicObject(gameObject);
        gameGrid.add(graphicObject, location.y, location.x);
    }

    public void resetLevel() {}

    /**
     * enable the game to deal with user inputs
     */
    public void setEventFilter() {
        Main.primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            gameController.handleKeyInput(event.getCode());
            reloadGrid();
        });
    }
}
