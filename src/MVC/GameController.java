package MVC;

import javafx.scene.input.KeyCode;
import data.GameLevel;
import data.GameRecord;
import object.GraphicObject;

import java.awt.*;
import java.io.FileInputStream;
import java.util.List;


public class GameController {
    private GameModel gameModel;
    private static GameController gameController = new GameController();

    public GameController(){
        this.gameModel = GameModel.getInstance();
    }

    public static GameController getUniqueInstance()  {
        if(gameController == null){
            gameController = new GameController();
        }
        return gameController;
    }
    public void requestResetLevel() { gameModel.resetLevel();}

    public void handleKeyInput(KeyCode code){

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
                Toolkit.getDefaultToolkit().beep();
        }

        if (GameModel.isDebugActive()) {
            System.out.println(code);
        }

    }

    public GameModel getGameModel() {
        return gameModel;
    }
    public void setGameModel(GameModel gameModel) {
        this.gameModel =gameModel;
    }
    /**
     * calls toggleDebug
     */
    public void callToggleDebug() {
        gameModel.toggleDebug();
    }

    /**
     * calls toggleMusic();
     */
    public void callToggleMusic() { gameModel.callToggleMusic(); }

    public void requestUndo() {
        gameModel.undoCurrentLevel();
    }

    public void requestSaveGame(){
        gameModel.saveGame();
    }

    /**
     * load a saved game
     */
    public void requestLoadGame() {
        gameModel.loadGameFile();
    }


    public List<GameRecord> requestGetHighScoresString() {
        return gameModel.getGameLevelHandler().getCurrentLevel().getTop10Record();
    }

    public boolean requestCheckIsTop10(){
        return gameModel.checkTop10();
    }

    public boolean requestCheckGameStatus() {
        return gameModel.checkGameStatus();
    }

    public void requestNextLevel() {
        gameModel.gotoNextLevel();
    }

    public void requestSaveRecord(String username) {
        gameModel.saveGameRecord(username);
    }

    public void requestSetStartTime() {
        gameModel.getGameLevelHandler().setStartTime(System.currentTimeMillis());
    }

    public int requestGetMovesCount() {
        return gameModel.getGameLevelHandler().getMovesCount();
    }

    public int requestGetTotalMovesCount() {
        return gameModel.getGameLevelHandler().getTotalMoveCount();
    }

    public boolean requestCheckGameComplete() {
        return gameModel.getGameLevelHandler().isGameComplete();
    }

    public GameLevel requestGetCurrentLevel() {
        return gameModel.getGameLevelHandler().getCurrentLevel();
    }

    public int requestCurrentLevelIndex() {
        return requestGetCurrentLevel().getIndex();
    }

    public long requestGetTimeInterval() {
        return gameModel.getGameLevelHandler().getTimeInterval();
    }

    public void requestReloadGame(FileInputStream in) {
        gameModel.reloadGameFile(in);
    }
}
