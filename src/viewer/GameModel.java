package viewer;

import model.GameLevelHandler;
import model.GameLogger;
import model.GameSaver;
import model.MusicPlayer;
import javafx.stage.FileChooser;
import data.GameLevel;
import data.GameRecord;
import main.Main;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This singleton class is responsible for all data in the game
 * @author COMP2013
 * @author Shuxiang Hu
 */
public class GameModel {

    /**
     * The unique instance of the class
     */
    private static GameModel gameModel = new GameModel();

    /**
     * Game name
     */
    private static final String M_GAMENAME = "BestSokobanEverV6";

    /**
     * A logger for the game
     */
    private GameLogger gameLogger;

    /**
     * Indicates whether debug mode is activated
     */
    private static boolean m_debug = false;

    /**
     * Game map name
     */
    private String mapSetName;

    /**
     * Responsible for saving current uncompleted game in to file
     */
    private GameSaver gameSaver;

    /**
     * Responsible for handle all game levels
     */
    private GameLevelHandler gameLevelHandler;

    /**
     * Responsible for BGM
     */
    private MusicPlayer gameMusicPlayer;

    /**
     * The file storing current game
     */
    private File saveFile;

    /**
     * Constructs a GameModel object
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

    /**
     * Gets the unique instance of GameModel class
     * @return the unique instance of GameModel class
     */
    public static GameModel getInstance(){
        if(gameModel == null){
            gameModel = new GameModel();
        }
        return gameModel;
    }

    /**
     * Checks if the debug configuration is active
     * @return true if debug is active, otherwise false
     */
    public static boolean isDebugActive() {
        return m_debug;
    }

    /**
     * Gets the game name
     * @return The game name
     */
    public static String getM_GAMENAME() {
        return M_GAMENAME;
    }

    /**
     * Gets the mapSetName
     * @return the mapSetName
     */
    public String getMapSetName() {
        return mapSetName;
    }

    /**
     * Get the GameLevelHandler instance
     * @return the game level handler of current GameModel instance
     */
    public GameLevelHandler getGameLevelHandler() {
        return gameLevelHandler;
    }

    /**
     * Asks game level handler to move the keeper, give a movement delta
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

    /**
     * Gets the save file of current game
     * @return the save file of current game
     */
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * loads the user-chosen game file
     */
    public void loadGameFile(){
        //ask user to choose file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sokoban save file", "*.skb"));
        saveFile = fileChooser.showOpenDialog(Main.m_primaryStage);

        //reset game data
        gameLevelHandler.setMovesCount(0);
        gameLevelHandler.setTotalMoveCount(0);
    }

    /**
     * Ask the game saver to save uncompleted game levels
     */
    public void saveGame() {
        gameSaver.writeLevels(gameLevelHandler.getLevels(), gameLevelHandler.getCurrentLevel().getIndex(), mapSetName);
    }

    /**
     * Checks if current player is within the top 10 player of current level
     * using time and move count
     * @return true if the palyer is within top 10, false otherwise
     */
    public boolean checkTop10(){
        long timeInterval = gameLevelHandler.getTimeInterval()/1000;
        int movesCount = gameLevelHandler.getMovesCount();
        return GameRecord.isTopN(gameLevelHandler.getCurrentLevel().getLevelRecords(),timeInterval,movesCount,10);
    }

    /**
     * Checks if current game level has been completed
     * @return True if current game has been completed, false otherwise
     */
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
     * Loads the maps for different level from a input
     * @param input the map layout for different levels
     * @return a list of game Levels
     */
    public List<GameLevel> loadGameFile(InputStream input) {
        //a list of game levels to load
        List<GameLevel> levels = new ArrayList<>();
        int levelIndex = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            boolean parsedFirstLevel = false;
            //a list of game object layout
            List<String> rawLevel = new ArrayList<>();
            String levelName = "";
            //read the file one line at a time
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

                //a map name if found
                if (line.contains("MapSetName")) {
                    mapSetName = line.replace("MapSetName: ", "");
                    continue;
                }

                //a level name if found
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

                // If the line contains at least 2 WALLS, add it to the map list
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

    /**
     * Undoes a step
     */
    public void undoCurrentLevel() {
        gameLevelHandler.undo();
    }

    /**
     * Resets current level to initial status
     */
    public void resetLevel() {
        gameLevelHandler.resetLevel();
    }

    /**
     * Go to the next game level
     */
    public void gotoNextLevel() {
        gameLevelHandler.gotoNextLevel();
    }

    /**
     * Asks game level to save a game record by providing the user name
     * @param username user name of player
     */
    public void saveGameRecord(String username) {
        gameLevelHandler.saveGameRecord(username);
    }

    /**
     * Updates time spent in current level
     */
    public void updateTimeInterval(){
        gameLevelHandler.setTimeInterval(System.currentTimeMillis() - gameLevelHandler.getStartTime());
    }


    /**
     * Asks music player to toggle music
     */
    public void callToggleMusic() {
        gameMusicPlayer.toggleMusic();
    }

    /**
     * reload a game file from a InputStream
     * @param in the file to be reloaded
     */
    public void reloadGameFile(InputStream in){
        gameLevelHandler = new GameLevelHandler(loadGameFile(in));
    }
}