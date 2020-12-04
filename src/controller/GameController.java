package controller;

import javafx.scene.input.KeyCode;
import data.GameLevel;
import data.GameRecord;
import object.GraphicObject;
import viewer.GameModel;

import java.awt.*;
import java.io.FileInputStream;
import java.util.List;

/**
 * This singleton class is responsible for communicating with model and handle keyboard input.
 * Its asks game model for certain or notify the game model to update game data
 * @author Shuxiang Hu
 */
public class GameController {
    /**
     * Holds all data related to the game
     */
    private GameModel gameModel;

    /**
     * The unique instance of the singleton class
     */
    private static GameController gameController = new GameController();

    /**
     * Constructs a GameController object and assigns it a GameModel object
     */
    public GameController(){
        this.gameModel = GameModel.getInstance();
    }

    /**
     * Gets the unique instance of this singleton class
     * @return the unique instance of this singleton class
     */
    public static GameController getUniqueInstance()  {
        if(gameController == null){
            gameController = new GameController();
        }
        return gameController;
    }

    /**
     * Asks the game model to reset current game level
     */
    public void requestResetLevel() { gameModel.resetLevel();}

    /**
     * Transform user input key into a movement delta and ask the game model to update the grid accordingly.
     * Beeps in case of invalid key board input
     * @param code keyboard input
     */
    public void handleKeyInput(KeyCode code){

        //Transform user input key into a movement delta and ask the game model to update the grid accordingly
        Point delta;
        switch (code) {
            case UP:
                GraphicObject.setKeeperPosition("up");
                delta = new Point(-1, 0);
                gameModel.handleKey(delta);
                break;

            case RIGHT:
                GraphicObject.setKeeperPosition("right");
                delta = new Point(0, 1);
                gameModel.handleKey(delta);
                break;

            case DOWN:
                GraphicObject.setKeeperPosition("down");
                delta = new Point(1, 0);
                gameModel.handleKey(delta);
                break;

            case LEFT:
                GraphicObject.setKeeperPosition("left");
                delta = new Point(0,-1);
                gameModel.handleKey(delta);
                break;

            default:
                //Beeps in case of invalid key board input
                Toolkit.getDefaultToolkit().beep();
        }

        if (GameModel.isDebugActive()) {
            System.out.println(code);
        }

    }

    /**
     * Gets the game model
     * @return game model of the game
     */
    public GameModel getGameModel() {
        return gameModel;
    }

    /**
     * Asks the game model to toggle debug mode
     */
    public void callToggleDebug() {
        gameModel.toggleDebug();
    }

    /**
     * Asks the game model to toggle music
     */
    public void callToggleMusic() { gameModel.callToggleMusic(); }


    /**
     * Asks the game model to undo a step
     */
    public void requestUndo() {
        gameModel.undoCurrentLevel();
    }

    /**
     * Asks the game model to save uncompleted game
     */
    public void requestSaveGame(){
        gameModel.saveGame();
    }

    /**
     * Asks the game model to load a saved game
     */
    public void requestLoadGame() {
        gameModel.loadGameFile();
    }


    /**
     * Gets a list a high scores
     * @return a list of top 10 game records
     */
    public List<GameRecord> requestGetHighScores() {
        return gameModel.getGameLevelHandler().getCurrentLevel().getTop10Record();
    }

    /**
     * Asks the game model to check if current player reached top 10
     * @return true if within top 10, false otherwise
     */
    public boolean requestCheckIsTop10(){
        return gameModel.checkTop10();
    }

    /**
     * Asks game model to check if current game is completed
     * @return true if current game is completed, false otherwise
     */
    public boolean requestCheckGameStatus() {
        return gameModel.checkGameStatus();
    }

    /**
     * Asks the game model to move to the next level
     */
    public void requestNextLevel() {
        gameModel.gotoNextLevel();
    }

    /**
     * Request the game model to save a record, given a user name
     * @param username the user name in the record
     */
    public void requestSaveRecord(String username) {
        gameModel.saveGameRecord(username);
    }

    /**
     * Sets the start time to current time (in milliseconds)
     */
    public void requestSetStartTime() {
        gameModel.getGameLevelHandler().setStartTime(System.currentTimeMillis());
    }

    /**
     * Gets the moves count of current level
     * @return moves count of current level
     */
    public int requestGetMovesCount() {
        return gameModel.getGameLevelHandler().getMovesCount();
    }

    /**
     * Gets the total moves count in the game so far
     * @return the total moves count in the game so far
     */
    public int requestGetTotalMovesCount() {
        return gameModel.getGameLevelHandler().getTotalMoveCount();
    }

    /**
     * Checks if current level is completed
     * @return true if current level is completed
     */
    public boolean requestCheckGameComplete() {
        return gameModel.getGameLevelHandler().isGameComplete();
    }

    /**
     * Gets current game level
     * @return current game level
     */
    public GameLevel requestGetCurrentLevel() {
        return gameModel.getGameLevelHandler().getCurrentLevel();
    }

    /**
     * Gets the index of current game level
     * @return the index of current game level
     */
    public int requestCurrentLevelIndex() {
        return requestGetCurrentLevel().getIndex();
    }

    /**
     * Gets the time spent in current game level so far
     * @return the time spent in current game level so far
     */
    public long requestGetTimeInterval() {
        return gameModel.getGameLevelHandler().getTimeInterval();
    }

    /**
     * Reloads a game in a file
     * @param in the game file to be loaded
     */
    public void requestReloadGame(FileInputStream in) {
        gameModel.reloadGameFile(in);
    }
}
