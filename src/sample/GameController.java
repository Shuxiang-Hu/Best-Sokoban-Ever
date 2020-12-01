package sample;

import javafx.scene.input.KeyCode;

import java.util.List;


public class GameController {
    private GameModel gameModel;

    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void requestResetLevel() { gameModel.resetLevel();}

    public void handleKeyInput(KeyCode code){
        gameModel.handleKey(code);
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
    public void callToggleMusic() { gameModel.getGameMusicPlayer().toggleMusic(); }

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
}
