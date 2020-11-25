package sample;

import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class GameModel {

    public static final String GAME_NAME = "BestSokobanEverV6";
    public static GameLogger logger;
    private static boolean debug = false;
    private Level currentLevel;
    private String mapSetName;
    private List<Level> levels;
    private boolean gameComplete = false;
    private int movesCount = 0;
    private int totalMoveCount = 0;
    private long startTime = 0;
    private long timeInterval = 0;
    private GameSaver gameSaver;

    public GameSaver getGameSaver() {
        return gameSaver;
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    private MusicPlayer gameMusicPlayer;


    public int getTotalMoveCount() {
        return totalMoveCount;
    }

    private File saveFile;

    public MusicPlayer getGameMusicPlayer() {
        return gameMusicPlayer;
    }

    /**
     * create a StartMeUp Object
     * @param input the game file
     * @param production whether to create media player
     */
    public GameModel(InputStream input, boolean production) {
        try {
            logger = new GameLogger();
            levels = loadGameFile(input);
            currentLevel = getNextLevel();
            gameSaver = new GameSaver();

            if (production) {
                gameMusicPlayer = new MusicPlayer();
            }
        } catch (IOException x) {
            System.out.println("Cannot create logger.");
        } catch (NoSuchElementException e) {
            logger.warning("Cannot load the default save file: " + e.getStackTrace());
        }
    }
    /**
     * checks if the debug configuration is active
     * @return true if debug is active, otherwise false
     */
    public static boolean isDebugActive() {
        return debug;
    }

    /**
     * gets the movesCount
     * @return the number of moves so far
     */
    public int getMovesCount() {
        return movesCount;
    }

    /**
     * gets the mapSetName
     * @return the mapSetName
     */
    public String getMapSetName() {
        return mapSetName;
    }

    /**
     * read in user input and move accordingly
     * @param code movement direction
     */
    public void handleKey(KeyCode code) {

        switch (code) {
            case UP:
                move(new Point(-1, 0));
                break;

            case RIGHT:
                move(new Point(0, 1));
                break;

            case DOWN:
                move(new Point(1, 0));
                break;

            case LEFT:
                move(new Point(0, -1));
                break;

            default:
                // TODO: implement something funny.
        }

        if (isDebugActive()) {
            System.out.println(code);
        }
    }

    /**
     * moves the keeper by a given delta if possible and updates movesCount
     * @param delta movement delta
     */

    public void move(Point delta) {
        if (isGameComplete()) {
            return;
        }

        Point keeperPosition = currentLevel.getKeeperPosition();
        GameObject keeper = currentLevel.getObjectAt(keeperPosition);
        Point targetObjectPoint = GameGrid.translatePoint(keeperPosition, delta);
        GameObject keeperTarget = currentLevel.getObjectAt(targetObjectPoint);

        if (GameModel.isDebugActive()) {
            System.out.println("Current level state:");
            System.out.println(currentLevel.toString());
            System.out.println("Keeper pos: " + keeperPosition);
            System.out.println("Movement source obj: " + keeper);
            System.out.printf("Target object: %s at [%s]", keeperTarget, targetObjectPoint);
        }

        boolean keeperMoved = false;

        //decide what to do according to target position
        switch (keeperTarget) {

            case WALL:
                break;

            case CRATE:

                GameObject crateTarget = currentLevel.getTargetObject(targetObjectPoint, delta);
                if (crateTarget != GameObject.FLOOR) {
                    break;
                }
                Level.resetGameGrid(currentLevel.getPreviousObjectGrid(), currentLevel.getObjectsGrid());
                currentLevel.setPreviousKeeperPosition(keeperPosition);
                currentLevel.moveGameObjectBy(keeperTarget, targetObjectPoint, delta);
                currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            case FLOOR:
                Level.resetGameGrid(currentLevel.getPreviousObjectGrid(), currentLevel.getObjectsGrid());
                currentLevel.setPreviousKeeperPosition(keeperPosition);
                currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            default:
                logger.severe("The object to be moved was not a recognised GameObject."+keeperTarget.toString());
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
        }

        //if keeper made a move, then update movesCount
        //checks if game is complete and go to next level if so
        if (keeperMoved) {
            keeperPosition.translate((int) delta.getX(), (int) delta.getY());
            movesCount++;
            totalMoveCount ++;
            //record previous game status and enable undo
            currentLevel.setUndo(true);

            if (currentLevel.isComplete()) {
                if (isDebugActive()) {
                    System.out.println("Level complete!");
                }
                currentLevel = getNextLevel();
            }
        }
    }

    /**
     * loads the maps for different level from a input
     * @param input the map layout for different levels
     * @return a list of game Levels
     */
    public List<Level> loadGameFile(InputStream input) {
        List<Level> levels = new ArrayList<>(5);
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
                        Level parsedLevel = new Level(levelName, ++levelIndex, rawLevel);
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
                        Level parsedLevel = new Level(levelName, ++levelIndex, rawLevel);
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
            logger.severe("Error trying to load the game file: " + e);
        } catch (NullPointerException e) {
            logger.severe("Cannot open the requested file: " + e);
        }

        return levels;
    }

    /**
     * checks if current game has been completed
     * @return true if the current game has been completed
     */
    public boolean isGameComplete() {
        return gameComplete;
    }

    public long getStartTime() {
        return startTime;
    }

    /**
     * gets next game level
     * @return next Level, null if all levels are completed
     */
    public Level getNextLevel() {
        movesCount = 0;
        startTime = System.currentTimeMillis();
        if (currentLevel == null) {
            return levels.get(0);
        }

        int currentLevelIndex = currentLevel.getIndex();
        if (currentLevelIndex -1 < levels.size()) {
            return levels.get(currentLevelIndex );
        }

        gameComplete = true;
        return null;
    }

    /**
     * gets current game level
     * @return current game level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     *  toggle debug status
     */
    public void toggleDebug() {
        debug = !debug;
    }

    /**
     * undoes a move
     */
    public void undo() {
        if(currentLevel.isUndoActive()){
            Level.resetGameGrid(currentLevel.getObjectsGrid(), currentLevel.getPreviousObjectGrid());//undo a step
            currentLevel.setKeeperPosition(currentLevel.getPreviousKeeperPosition());
            currentLevel.setUndo(false);//disable undo
        }
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
        saveFile = fileChooser.showOpenDialog(Main.primaryStage);
        movesCount = 0;
        totalMoveCount = 0;
    }

    public void setStartTime(long currentTimeMillis) {
        this.startTime = currentTimeMillis;
    }

    public void saveGame() {
        gameSaver.writeLevels(levels, currentLevel.getIndex(), mapSetName);
    }

    public void resetLevel() {
        Level.resetGameGrid(currentLevel.getObjectsGrid(),currentLevel.getInitialObjectGrid());
        currentLevel.setKeeperPosition(currentLevel.getInitialKeeperPosition());
        totalMoveCount -= movesCount;
        movesCount = 0;
        startTime = System.currentTimeMillis();
    }
}