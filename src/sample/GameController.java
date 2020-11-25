package sample;

import javafx.scene.input.KeyCode;


public class GameController {
    private GameModel gameModel;

    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

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


}
