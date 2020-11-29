package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameViewer {
    private GameController gameController;
    private GridPane gameGrid;
    private final Label TIMECOUNTER = new Label();
    private final Label MOVECOUNTER = new Label();
    private final Label TOTALMOVECOUNTER = new Label();
    private final Timeline GAMETIMELINE = new Timeline(new KeyFrame(Duration.millis(100), actionEvent -> {
        gameController.getGameModel().setTimeInterval(System.currentTimeMillis() - gameController.getGameModel().getStartTime());
        TIMECOUNTER.setText("Time Count: "+gameController.getGameModel().getTimeInterval()/1000);

    } ));
    GameViewer(GameController gameController){
        this.gameController = gameController;
    }


    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public VBox configureStartScreen(){
        //set up buttons for various colors
        Label prompt1 = new Label("Background color");
        Button blackWallButton = new Button("Black");
        blackWallButton.setOnAction(event ->
                GraphicObject.setM_background(System.getProperty("user.dir")+"/resource/GameImages/BlackWall.png"));

        Button grayWallButton = new Button("Gray");
        grayWallButton.setOnAction(event ->
                GraphicObject.setM_background(System.getProperty("user.dir")+"/resource/GameImages/GrayWall.png"));

        Button whiteWallButton = new Button("White");
        whiteWallButton.setOnAction(event ->
                GraphicObject.setM_background(System.getProperty("user.dir")+"/resource/GameImages/WhiteWall.png"));

        Button brownWallButton = new Button("Brown");
        brownWallButton.setOnAction(event ->
                GraphicObject.setM_background(System.getProperty("user.dir")+"/resource/GameImages/BrownWall.png"));


        //initialize buttons for floor color selection
        Label prompt2 = new Label("Floor color");
        Button whiteFloorButton = new Button("White");
        whiteFloorButton.setOnAction(event ->
                GraphicObject.setM_floor(System.getProperty("user.dir")+"/resource/GameImages/WhiteFloor.png"));

        Button brownFloorButton = new Button("Brown");
        brownFloorButton.setOnAction(event ->
                GraphicObject.setM_floor(System.getProperty("user.dir")+"/resource/GameImages/BrownFloor.png"));

        Button greenFloorButton = new Button("Green");
        greenFloorButton.setOnAction(event ->
                GraphicObject.setM_floor(System.getProperty("user.dir")+"/resource/GameImages/GreenFloor.png"));

        Button grayFloorButton = new Button("Gray");
        grayFloorButton.setOnAction(event ->
                GraphicObject.setM_floor(System.getProperty("user.dir")+"/resource/GameImages/GrayFloor.png"));
        //initialize start button
        Button startButton = new Button("START");
        startButton.setOnAction(actionEvent -> {
            Main.getM_primaryStage().setScene(new Scene(configureGameScreen()));
            gameController.getGameModel().setStartTime(System.currentTimeMillis());
            reloadGrid();
        });

        //set layout of elements in start screen
        VBox backgroundColorButtonLayout = new VBox();
        backgroundColorButtonLayout.setAlignment(Pos.TOP_LEFT);
        VBox.setMargin(backgroundColorButtonLayout, new Insets(0,200,0,0));
        VBox floorButtonLayout = new VBox();
        floorButtonLayout.setAlignment(Pos.TOP_RIGHT);
        VBox.setMargin(floorButtonLayout, new Insets(0,0,0,200));
        HBox buttonLayout = new HBox();
        VBox startScreenLayout = new VBox();
        backgroundColorButtonLayout.getChildren().
                addAll(prompt1,blackWallButton,grayWallButton,brownWallButton,whiteWallButton);
        floorButtonLayout.getChildren().addAll(prompt2, whiteFloorButton,grayFloorButton,brownFloorButton,greenFloorButton);
        buttonLayout.getChildren().addAll(backgroundColorButtonLayout,floorButtonLayout);
        buttonLayout.setAlignment(Pos.CENTER);
        startScreenLayout.getChildren().addAll(buttonLayout,startButton);
        startScreenLayout.setAlignment(Pos.CENTER);
        startScreenLayout.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE,null,null)));
        return startScreenLayout;

    }
    public GridPane configureGameScreen(){
        //initialize menus
        MenuBar mainMenuBar = new MenuBar();
        MenuItem menuItemSaveGame = new MenuItem("Save Game");
        menuItemSaveGame.setOnAction(actionEvent -> gameController.requestSaveGame());
        MenuItem menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setOnAction(actionEvent -> {
            gameController.requestLoadGame();
            if (gameController.getGameModel().getSaveFile() != null) {
                if (GameModel.isDebugActive()) {
                    GameModel.m_gameLogger.info("Loading save file: " + gameController.getGameModel().getSaveFile().getName());
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

        menuItemUndo.setOnAction(actionEvent -> {gameController.requestUndo();reloadGrid();});
        RadioMenuItem radioMenuItemMusic = new RadioMenuItem("Toggle Music");
        radioMenuItemMusic.setOnAction(actionEvent -> gameController.callToggleMusic());
        RadioMenuItem radioMenuItemDebug = new RadioMenuItem("Toggle Debug");
        radioMenuItemDebug.setOnAction(actionEvent -> {
            gameController.callToggleDebug();
            reloadGrid();
        });
        MenuItem menuItemResetLevel = new MenuItem("Reset Level");
        Menu menuLevel = new Menu("Level");
        menuItemResetLevel.setOnAction(actionEvent -> {gameController.requestResetLevel();reloadGrid();});
        MenuItem menuItemShowHighScores = new MenuItem("Show High Scores");
        menuItemShowHighScores.setOnAction(actionEvent -> newDialog("Top 10", gameController.requestGetHighScoresString(),null));
        menuLevel.getItems().addAll(menuItemUndo, radioMenuItemMusic, radioMenuItemDebug,
                new SeparatorMenuItem(), menuItemResetLevel,menuItemShowHighScores);

        MenuItem menuItemGame = new MenuItem("About This Game");
        Menu menuAbout = new Menu("About");
        menuAbout.setOnAction(actionEvent -> showAbout());
        menuAbout.getItems().addAll(menuItemGame);
        mainMenuBar.getMenus().addAll(menuFile, menuLevel, menuAbout);

        //initialize counters
        MOVECOUNTER.setText(" Move Count: " + gameController.getGameModel().getMovesCount());
        Label counterSeparator1 = new Label("      ");
        TOTALMOVECOUNTER.setText("Total Move Count: "+gameController.getGameModel().getTotalMoveCount());
        Label counterSeparator2 = new Label("      ");
        TIMECOUNTER.setText("Time Count: 0");
        HBox counterLayout = new HBox();
        counterLayout.getChildren().addAll(MOVECOUNTER,counterSeparator1, TOTALMOVECOUNTER,counterSeparator2, TIMECOUNTER);
        counterLayout.setAlignment(Pos.CENTER);
        HBox topLayout = new HBox();
        topLayout.getChildren().addAll(mainMenuBar,counterLayout);

        //start the time counter
        GAMETIMELINE.setCycleCount(Animation.INDEFINITE);
        GAMETIMELINE.play();
        //set layout of the game screen
        gameGrid = new GridPane();
        GridPane root = new GridPane();
        root.add(topLayout, 0, 0);
        root.add(gameGrid, 0, 1);
        return root;
    }

    private void reloadGame(FileInputStream fileInputStream) {
        GameModel gameModel = new GameModel(fileInputStream, true);
        gameController.setGameModel(gameModel);
        setGameController(gameController);
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
        dialog.initOwner(Main.getM_primaryStage());
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);

        Text text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.LEFT);
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

        GameLevel currentLevel = gameController.getGameModel().getCurrentLevel();
        GameLevel.LevelIterator levelGridIterator = (GameLevel.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();
        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getCurrentPosition());
        }gameGrid.autosize();
        Main.getM_primaryStage().sizeToScene();
        MOVECOUNTER.setText(" Move Count: "+gameController.getGameModel().getMovesCount());
        TOTALMOVECOUNTER.setText("Total Move Count: "+gameController.getGameModel().getTotalMoveCount());
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
        ImageView imageView = new ImageView(graphicObject.getAppearance());
        imageView.setFitHeight(30.0);
        imageView.setFitWidth(30.0);
        gameGrid.add(imageView, location.y, location.x);
    }



    /**
     * enable the game to deal with user inputs
     */
    public void setEventFilter() {
        Main.getM_primaryStage().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            gameController.handleKeyInput(event.getCode());
            if(gameController.requestCheckGameStatus()) {
                int levelIndex = gameController.getGameModel().getCurrentLevel().getIndex();
                int numberOfMoves = gameController.getGameModel().getMovesCount();
                long timeInterval = gameController.getGameModel().getTimeInterval();
                int totalNumberOfMoves = gameController.getGameModel().getTotalMoveCount();
                String statistics = "You completed Level " + levelIndex + " with " + numberOfMoves + " moves and " + timeInterval/1000 + "seconds.";
                statistics += "\nTotal number of moves: " + totalNumberOfMoves;
                afterGamePopup(gameController.requestCheckIsTop10(),statistics);
                gameController.requestNextLevel();
                System.out.println(gameController.getGameModel().getMovesCount());

            }
            reloadGrid();
        });
    }

    public void afterGamePopup(boolean isTop10,String statisticsString) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(Main.getM_primaryStage());
        dialog.setResizable(false);
        dialog.setTitle("Level completed");

        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);


        Text gameStatistics = new Text(statisticsString);
        gameStatistics.setTextAlignment(TextAlignment.LEFT);
        gameStatistics.setFont(javafx.scene.text.Font.font(14));
        dialogVbox.getChildren().addAll(gameStatistics);

        if(isTop10){ //ask for user name if player scored top 10
            Text prompt = new Text("Enter your user name to be entitled to the Top 10");
            prompt.setTextAlignment(TextAlignment.LEFT);
            prompt.setFont(javafx.scene.text.Font.font(14));
            TextField inputBox = new TextField();
            inputBox.setPrefSize(50, 30); //
            inputBox.setEditable(true);

            Button submitBtn = new Button("OK");
            submitBtn.setOnAction(e-> {
                System.out.println(inputBox.getText());
                gameController.requestSaveRecord(inputBox.getText());
                dialog.close();
            });

            dialogVbox.getChildren().addAll(prompt,inputBox,submitBtn);
        }
        else {
            Text notHighScoreMsg = new Text("You did not score Top 10 for this level");
            gameStatistics.setTextAlignment(TextAlignment.LEFT);
            gameStatistics.setFont(javafx.scene.text.Font.font(14));
            dialogVbox.getChildren().add(notHighScoreMsg);
        }


        Scene dialogScene = new Scene(dialogVbox, 350, 210);
        dialog.setScene(dialogScene);
        dialog.show();
    }


}
