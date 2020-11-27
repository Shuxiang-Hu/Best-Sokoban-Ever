package sample;

import javafx.scene.input.KeyCode;


public class GameController {
    private GameModel gameModel;

    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void requestResetLevel() { gameModel.resetLevel();}

    public boolean handleKeyInput(KeyCode code){
        return gameModel.handleKey(code);
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
        gameModel.undo();
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


    public String requestGetHighScoresString() {
        return gameModel.getCurrentLevelHighScoresString();
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
        System.out.println("requesting save " + username +"'s game record");
        gameModel.saveGameRecord(username);
    }
}
