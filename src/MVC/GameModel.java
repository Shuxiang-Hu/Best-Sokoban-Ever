package MVC;

import component.GameLevelHandler;
import component.GameLogger;
import component.GameSaver;
import component.MusicPlayer;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import data.GameLevel;
import data.GameRecord;
import main.Main;
import object.GraphicObject;


import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GameModel {
    private static GameModel gameModel = new GameModel();
    private static final String M_GAMENAME = "BestSokobanEverV6";
    private GameLogger gameLogger;
    private static boolean m_debug = false;
    private String mapSetName;
    private GameSaver gameSaver;
    private GameLevelHandler gameLevelHandler;
    private MusicPlayer gameMusicPlayer;
    private File saveFile;

    /**
     * create a GameModel Object
     */
    private GameModel() {
        try {
            InputStream input = new FileInputStream("resource/GameLayouts/SampleGame.skb");
            gameLevelHandler = new GameLevelHandler(loadGameFile(input));
            gameLogger = GameLogger.getUniqueInstance();
            gameSaver = GameSaver.getInstance();
            gameMusicPlayer = MusicPlayer.getUniqueInstance();

        } catch (NoSuchElementException | FileNotFoundException e) {
            gameLogger.warning("Cannot load the default save file: " + e.getStackTrace());
        }
    }

    public static GameModel getInstance(){
        if(gameModel == null){
            gameModel = new GameModel();
        }
        return gameModel;
    }
    /**
     * checks if the debug configuration is active
     * @return true if debug is active, otherwise false
     */
    public static boolean isDebugActive() {
        return m_debug;
    }

    public static String getM_GAMENAME() {
        return M_GAMENAME;
    }

    /**
     * gets the mapSetName
     * @return the mapSetName
     */
    public String getMapSetName() {
        return mapSetName;
    }

    public GameLevelHandler getGameLevelHandler() {
        return gameLevelHandler;
    }

    /**
     * read in user input and move accordingly
     * @param delta movement direction and distance
     */
    public void handleKey(Point delta) {
        gameLevelHandler.move(delta);

    }

    /**
     *  toggle debug status
     */
    public void toggleDebug() {
        m_debug = !m_debug;
    }

    public File getSaveFile() {
        return saveFile;
    }

    /**
     * loads the user-chosen game file
     */
    public void loadGameFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        saveFile = fileChooser.showOpenDialog(Main.m_primaryStage);
        gameLevelHandler.setMovesCount(0);
        gameLevelHandler.setTotalMoveCount(0);
    }

    public void saveGame() {
        gameSaver.writeLevels(gameLevelHandler.getLevels(), gameLevelHandler.getCurrentLevel().getIndex(), mapSetName);
    }

    public boolean checkTop10(){
        long timeInterval = gameLevelHandler.getTimeInterval()/1000;
        int movesCount = gameLevelHandler.getMovesCount();
        return GameRecord.isTopN(gameLevelHandler.getCurrentLevel().getLevelRecords(),timeInterval,movesCount,10);
    }

    public boolean checkGameStatus(){
        if (gameLevelHandler.getCurrentLevel().isComplete()) {
            if (isDebugActive()) {
                System.out.println("Level complete!");
            }
            return true;
        }
        return false;
    }


    /**
     * loads the maps for different level from a input
     * @param input the map layout for different levels
     * @return a list of game Levels
     */
    public List<GameLevel> loadGameFile(InputStream input) {
        List<GameLevel> levels = new ArrayList<>(5);
        int levelIndex = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            List<String> rawLevel = new ArrayList<>();
            String levelName = "";

            while (true) {
                String line = reader.readLine();

                // Break the loop if EOF is reached
                if (line == null) {
                    if (rawLevel.size() != 0) {
                        GameLevel parsedLevel = new GameLevel(levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                    }
                    break;
                }

                if (line.contains("MapSetName")) {
                    mapSetName = line.replace("MapSetName: ", "");
                    continue;
                }

                if (line.contains("LevelName")) {
                    if (parsedFirstLevel) {
                        GameLevel parsedLevel = new GameLevel(levelName, ++levelIndex, rawLevel);
                        levels.add(parsedLevel);
                        rawLevel.clear();
                    } else {
                        parsedFirstLevel = true;
                    }

                    levelName = line.replace("LevelName: ", "");
                    continue;
                }

                line = line.trim();
                line = line.toUpperCase();
                // If the line contains at least 2 WALLS, add it to the list
                if (line.matches(".*W.*W.*")) {
                    rawLevel.add(line);
                }
            }

        } catch (IOException e) {
            gameLogger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            gameLogger.severe("Cannot open the requested file: " + e);
        }
        return levels;
    }

    public void undoCurrentLevel() {
        gameLevelHandler.undo();
    }

    public void resetLevel() {
        gameLevelHandler.resetLevel();
    }

    public void gotoNextLevel() {
        gameLevelHandler.gotoNextLevel();
    }

    public void saveGameRecord(String username) {
        gameLevelHandler.saveGameRecord(username);
    }

    public void updateTimeInterval(){
        gameLevelHandler.setTimeInterval(System.currentTimeMillis() - gameLevelHandler.getStartTime());
    }


    public void callToggleMusic() {
        gameMusicPlayer.toggleMusic();
    }

    public void reloadGameFile(InputStream in){
        gameLevelHandler = new GameLevelHandler(loadGameFile(in));
    }
}