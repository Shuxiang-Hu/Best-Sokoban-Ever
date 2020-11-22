package sample;

import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;

import java.io.FileNotFoundException;

public class GameController {
    private final GameModel gameModel;

    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void handleKeyInput(KeyCode code){
        gameModel.handleKey(code);
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
    public void callToggleMusic() {
        gameModel.toggleMusic();
    }

    public void requestUndo() {
        gameModel.undo();
    }

    public void requestSaveGame(){
        gameModel.saveGame();
    }

    /**
     * load a saved game
     */
    public FileChooser requestLoadGame() {
        return gameModel.loadGameFile();
    }
}
