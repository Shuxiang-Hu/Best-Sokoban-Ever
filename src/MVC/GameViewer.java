package MVC;

import component.GameLogger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
import data.GameLevel;
import main.Main;
import object.GameObject;
import data.GameRecord;
import object.GraphicObject;


import java.awt.*;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This singleton class controls the GUI and asks the controller for data to be displayed
 * @author Shuxiang Hu
 * @author COMP2013
 */
public class GameViewer {

    /**
     * Provides data to be and updates data according to user input
     */
    private GameController gameController;

    /**
     * The unique instance
     */
    private static GameViewer gameViewer = new GameViewer();

    /**
     * The game grid to be displayed on GUI
     */
    private GridPane gameGrid;

    /**
     * Label showing time (in seconds) spent in current game level, updated real-time
     */
    private final Label TIMECOUNTER = new Label();

    /**
     * Label showing moves in current game level, updated real-time
     */
    private final Label MOVECOUNTER = new Label();

    /**
     * Label showing total move count across all game levels, updated real-time
     */
    private final Label TOTALMOVECOUNTER = new Label();

    /**
     * Responsible for real-time update of time label
     */
    private final Timeline GAMETIMELINE = new Timeline(new KeyFrame(Duration.millis(100), actionEvent -> {
        gameController.getGameModel().updateTimeInterval();
        String timeText = "Time Count: "+gameController.getGameModel().getGameLevelHandler().getTimeInterval()/1000;
        TIMECOUNTER.setText(timeText);
    } ));

    /**
     * Constructs a GameView Object and assigns it a game controller
     */
    private GameViewer(){
        this.gameController = GameController.getUniqueInstance();
    }

    /**
     * Gets the unique instance of GameViewer class
     * @return the unique instance of GameViewer class
     */
    public static GameViewer getUniqueInstance(){
        if(gameViewer == null){
            gameViewer = new GameViewer();
        }
        return gameViewer;
    }

    /**
     * Configure the layout of start screen
     * @return
     */
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
            gameController.requestSetStartTime();
            reloadGrid();
        });

        //VBox for background color buttons
        VBox backgroundColorButtonLayout = new VBox();
        backgroundColorButtonLayout.setAlignment(Pos.TOP_LEFT);
        VBox.setMargin(backgroundColorButtonLayout, new Insets(0,200,0,0));

        //VBox for floor color buttons
        VBox floorButtonLayout = new VBox();
        floorButtonLayout.setAlignment(Pos.TOP_RIGHT);
        VBox.setMargin(floorButtonLayout, new Insets(0,0,0,200));

        //HBox for two VBoxs above
        HBox buttonLayout = new HBox();
        VBox startScreenLayout = new VBox();
        backgroundColorButtonLayout.getChildren().
                addAll(prompt1,blackWallButton,grayWallButton,brownWallButton,whiteWallButton);
        floorButtonLayout.getChildren().addAll(prompt2, whiteFloorButton,grayFloorButton,brownFloorButton,greenFloorButton);
        buttonLayout.getChildren().addAll(backgroundColorButtonLayout,new Text("         "),floorButtonLayout);
        buttonLayout.setAlignment(Pos.CENTER);

        //add color buttons and start button to the layout
        startScreenLayout.getChildren().addAll(buttonLayout,startButton);
        startScreenLayout.setAlignment(Pos.CENTER);
        startScreenLayout.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE,null,null)));

        return startScreenLayout;
    }

    /**
     * Configures the layout of
     * @return
     */
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
                    GameLogger.getUniqueInstance().info("Loading save file: " + gameController.getGameModel().getSaveFile().getName());
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
        menuItemShowHighScores.setOnAction(actionEvent -> showHighScore());
        menuLevel.getItems().addAll(menuItemUndo, radioMenuItemMusic, radioMenuItemDebug,
                new SeparatorMenuItem(), menuItemResetLevel,menuItemShowHighScores);

        MenuItem menuItemGame = new MenuItem("About This Game");
        Menu menuAbout = new Menu("About");
        menuAbout.setOnAction(actionEvent -> showAbout());
        menuAbout.getItems().addAll(menuItemGame);
        mainMenuBar.getMenus().addAll(menuFile, menuLevel, menuAbout);

        //initialize counters
        MOVECOUNTER.setText(" Move Count: " + gameController.requestGetMovesCount());
        Label counterSeparator1 = new Label("      ");
        TOTALMOVECOUNTER.setText("Total Move Count: "+gameController.requestGetTotalMovesCount());
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

    /**
     * Reloads game from a given file input
     * @param fileInputStream game file to be reloaded
     */
    private void reloadGame(FileInputStream fileInputStream) {
        gameController.requestReloadGame(fileInputStream);
    }

    /**
     * Closes the game
     */
    public void closeGame() {
        System.exit(0);
    }

    /**
     * Shows a new dialog win
     * @param dialogTitle title of the dialog window
     * @param dialogMessage context in the dialog window
     * @param dialogMessageEffect effect of the dialog window
     */
    public void newDialog(String dialogTitle, String dialogMessage, Effect dialogMessageEffect) {
        //initialize dialog window
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(Main.getM_primaryStage());
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);

        //initialize the text in the window
        Text text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.LEFT);
        text1.setFont(javafx.scene.text.Font.font(14));

        //set effect of the text
        if (dialogMessageEffect != null) {
            text1.setEffect(dialogMessageEffect);
        }

        //initialize the layout of the window
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);
        dialogVbox.getChildren().add(text1);

        //show the window
        Scene dialogScene = new Scene(dialogVbox);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Shows default message in a dialog window
     */
    public void showAbout() {
        String title = "About This Game";
        String message = "Enjoy the Game!\n";
        newDialog(title, message, null);
    }

    /**
     * Reloads the game grid
     */
    public void reloadGrid() {
        //do nothing if all games levels are completed
        if (gameController.requestCheckGameComplete()) {
            showVictoryMessage();
            return;
        }

        //get the current game level
        GameLevel currentLevel = gameController.requestGetCurrentLevel();
        GameLevel.LevelIterator levelGridIterator = (GameLevel.LevelIterator) currentLevel.iterator();
        gameGrid.getChildren().clear();

        //all all the objects in the game level to the GUI
        while (levelGridIterator.hasNext()) {
            addObjectToGrid(levelGridIterator.next(), levelGridIterator.getCurrentPosition());
        }gameGrid.autosize();
        Main.getM_primaryStage().sizeToScene();

        //update counters
        MOVECOUNTER.setText(" Move Count: "+gameController.requestGetMovesCount());
        TOTALMOVECOUNTER.setText("Total Move Count: "+gameController.requestGetTotalMovesCount());
    }

    /**
     * Shows victory message
     */
    public void showVictoryMessage() {
        String dialogTitle = "Game Over!";
        String dialogMessage = "You completed " + gameController.getGameModel().getMapSetName() + " in " + gameController.requestGetTotalMovesCount() + " moves!";
        MotionBlur mb = new MotionBlur(2, 3);
        newDialog(dialogTitle, dialogMessage, mb);
    }

    /**
     * Add a game object to a given point in the grid
     * @param gameObject the game object to be added to grid
     * @param location the position of the game object in the grid
     */
    public void addObjectToGrid(GameObject gameObject, Point location) {
        GraphicObject graphicObject = new GraphicObject(gameObject);
        ImageView imageView = new ImageView(graphicObject.getAppearance());
        imageView.setFitHeight(30.0);
        imageView.setFitWidth(30.0);
        gameGrid.add(imageView, location.y, location.x);
    }



    /**
     * Enable the game to deal with user inputs
     */
    public void setEventFilter() {
        Main.getM_primaryStage().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            //ask the controller to handle input code
            gameController.handleKeyInput(event.getCode());
            if(gameController.requestCheckGameStatus()) { //if the current level is completed by this keyboard input
                //get statistics of the completed level
                int levelIndex = gameController.requestCurrentLevelIndex();
                int numberOfMoves = gameController.requestGetMovesCount();
                long timeInterval = gameController.requestGetTimeInterval();
                int totalNumberOfMoves = gameController.requestGetTotalMovesCount();

                //Pop up a window showing statistics and ask for user name if it is a top 10
                String statistics = "You completed Level " + levelIndex + " with " + numberOfMoves + " moves and " + timeInterval/1000 + "seconds.";
                statistics += "\nTotal number of moves: " + totalNumberOfMoves;
                afterGamePopup(gameController.requestCheckIsTop10(),statistics);
                gameController.requestNextLevel();
                System.out.println(gameController.requestGetMovesCount());
            }
            reloadGrid();
        });
    }

    /**
     * Shows a popup  window, should be called every time a level is done
     * @param isTop10 if the player scored top 10 for the level
     * @param statisticsString a string containing statistics of the competed level
     */
    public void afterGamePopup(boolean isTop10,String statisticsString) {
        //initialize the dialog window
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(Main.getM_primaryStage());
        dialog.setResizable(false);
        dialog.setTitle("Level completed");

        //use VBox for the layout
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);

        //show game statistics in a Text
        Text gameStatistics = new Text(statisticsString);
        gameStatistics.setTextAlignment(TextAlignment.LEFT);
        gameStatistics.setFont(javafx.scene.text.Font.font(14));
        dialogVbox.getChildren().addAll(gameStatistics);

        if(isTop10){ //ask for user name if player scored top 10
            //prompt user to enter his/her name
            Text prompt = new Text("Enter your user name to be entitled to the Top 10");
            prompt.setTextAlignment(TextAlignment.LEFT);
            prompt.setFont(javafx.scene.text.Font.font(14));
            TextField inputBox = new TextField();
            inputBox.setPrefSize(50, 30); //
            inputBox.setEditable(true);

            //button to submit user name
            Button submitBtn = new Button("OK");
            submitBtn.setOnAction(e-> {
                System.out.println(inputBox.getText());
                gameController.requestSaveRecord(inputBox.getText());
                dialog.close();
            });

            dialogVbox.getChildren().addAll(prompt,inputBox,submitBtn);
        }
        else {
            //just state this is not a top 10 score if it is not
            Text notHighScoreMsg = new Text("You did not score Top 10 for this level！");
            notHighScoreMsg.setTextAlignment(TextAlignment.LEFT);
            notHighScoreMsg.setFont(javafx.scene.text.Font.font(14));
            dialogVbox.getChildren().add(notHighScoreMsg);
        }

        //show window
        Scene dialogScene = new Scene(dialogVbox, 350, 210);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Shows top 10 high scores in a new window
     */
    public void showHighScore(){
        Stage highScoreWindow = new Stage();
        Scene scene = new Scene(new Group());
        int levelIndex = gameController.requestCurrentLevelIndex();
        highScoreWindow.setTitle("Top 10 Players");
        Label tableTitle = new Label("Top 10 Players for level" + levelIndex);//title of top 10 table

        //initialize the table for high scores
        TableView scoreTable = new TableView();
        scoreTable.setEditable(false);

        TableColumn userNameColumn = new TableColumn("User Name");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        TableColumn timeColumn = new TableColumn("Time(s)");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn moveColumn = new TableColumn("Moves");
        moveColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfMoves"));

        ObservableList<GameRecord> data = FXCollections.observableArrayList(gameController.requestGetHighScores());

        scoreTable.setItems(data);
        scoreTable.getColumns().addAll(userNameColumn,timeColumn,moveColumn);

        //add the table title and table to a VBox
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(tableTitle, scoreTable);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        //show the window
        highScoreWindow.setScene(scene);
        highScoreWindow.show();
    }
}
