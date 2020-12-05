package model;

import data.GameGrid;
import data.GameLevel;
import object.GameObject;
import object.GraphicObject;

import java.awt.*;
import java.util.List;

/**
 * Helps a GameModel object to handle data related to GameLevel
 * @author Shuxiang Hu
 */
public class GameLevelHandler {
    /**
     * List of game levels of current game
     */
    private List<GameLevel> levels;

    /**
     * Represents whether current game level is completed
     */
    private boolean gameComplete = false;

    /**
     * The number of moves in current game level
     */
    private int movesCount = 0;

    /**
     * The number of moves in all game levels so far
     */
    private int totalMoveCount = 0;

    /**
     * The number of moves in previous game level
     */
    private int previousMovesCount=0;

    /**
     * The time used to complete previous game level (in millisecond)
     */
    private long previousTimeInterval=0;

    /**
     * The time that the game starts (in millisecond)
     */
    private long startTime = 0;

    /**
     * The time spent in current game level (in millisecond)
     */
    private long timeInterval = 0;

    /**
     * The logger to log messages in this class
     */
    private GameLogger levelHandlerLogger;

    /**
     * The GameLevel object that the player is playing
     */
    private GameLevel currentLevel;


    /**
     * Constructs a GameLevelHandler by assigning it a list of game levels
     * @param gameLevels A list of game levels that the GameLevelHandler is responsible for
     */
    public GameLevelHandler(List<GameLevel> gameLevels) {
        levels = gameLevels;
        currentLevel = getNextLevel();
        levelHandlerLogger = GameLogger.getUniqueInstance();
    }

    /**
     * Sets the start time to current time (in millisecond）
     * @param currentTimeMillis current time in milliseconds
     */
    public void setStartTime(long currentTimeMillis) {
        this.startTime = currentTimeMillis;
    }

    /**
     * Gets current game level
     * @return current game level
     */
    public GameLevel getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Gets the time spent in current level so fat (in milliseconds)
     * @return the time spent in current level so fat (in milliseconds)
     */
    public long getTimeInterval() {
        return timeInterval;
    }
    /**
     * Gets the number of moves in current level so far
     * @return the number of moves in current level so far
     */
    public int getMovesCount() {
        return movesCount;
    }

    /**
     * Gets the total number of moves across all levels so far
     * @return the total number of moves across all levels so far
     */
    public int getTotalMoveCount() {
        return totalMoveCount;
    }
    /**
     * Checks if current level has been completed
     * @return true if current level has been completed, false otherwise
     */
    public boolean isGameComplete() {
        return gameComplete;
    }

    /**
     * Gets the time when the current level starts (in milliseconds)
     * @return the time when the current level starts (in milliseconds)
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Sets the moves count to a new value
     * @param movesCount The value to be updated to the moves count
     */
    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }

    /**
     * Gets all the game levels in current game
     * @return all the game levels in current game
     */
    public List<GameLevel> getLevels() {
        return levels;
    }

    /**
     * Sets the total moves count to a new value
     * @param totalMoveCount The value to be updated to the total moves count
     */
    public void setTotalMoveCount(int totalMoveCount) {
        this.totalMoveCount = totalMoveCount;
    }

    /**
     * Sets the time spent in current level
     * @param timeInterval the time to be assigned to timeInterval
     */
    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    /**
     * Change current level to the next level
     */
    public void gotoNextLevel() {
        currentLevel = getNextLevel();
        GraphicObject.setKeeperPosition("down");
    }

    /**
     * Resets current game level to its initial status
     */
    public void resetLevel() {
        //reset grid and keeper position
        GameLevel.resetGameGrid(currentLevel.getObjectsGrid(),currentLevel.getInitialObjectGrid());
        currentLevel.setKeeperPosition(currentLevel.getInitialKeeperPosition());

        //reset game level and move count
        totalMoveCount -= movesCount;
        movesCount = 0;
        startTime = System.currentTimeMillis();
    }

    /**
     * Saves a game record when a high score player enters his/her user name
     * @param username user name of the player who created this record
     */
    public void saveGameRecord(String username) {
        if(currentLevel == null){//save the for the last game level if there is no next level
            levels.get(levels.size()-1).saveRecord(username,previousTimeInterval/1000,previousMovesCount);
        }
        else {
            levels.get(currentLevel.getIndex()-2).saveRecord(username,previousTimeInterval/1000,previousMovesCount);
        }

    }

    /**
     * Gets next game level
     * @return next Level, null if all levels are completed
     */
    public GameLevel getNextLevel() {
        //reset time and move counters
        previousMovesCount = movesCount;
        movesCount = 0;
        previousTimeInterval = timeInterval;
        timeInterval = 0;
        startTime = System.currentTimeMillis();

        //if no current level, then get the first level
        if (currentLevel == null) {
            return levels.get(0);
        }

        //get the next level
        int currentLevelIndex = currentLevel.getIndex();
        if (currentLevelIndex  < levels.size()) {
            return levels.get(currentLevelIndex );
        }

        //return null if no more levels
        gameComplete = true;
        return null;
    }

    /**
     * Undoes a move
     */
    public void undo() {
        //get previous grid and keeper position
        GameGrid previousGrid =  currentLevel.getLastPreviousObjectGrid();
        Point previousKeeperPosition = currentLevel.getLastPreviousKeeperPosition();

        //load previous grid and keeper position is there exist a previous game status
        if((previousGrid != null) && (previousKeeperPosition != null)){
            GameLevel.resetGameGrid(currentLevel.getObjectsGrid(), previousGrid);
            currentLevel.setKeeperPosition(previousKeeperPosition);
            movesCount++;
            totalMoveCount ++;
        }
    }


    /**
     * Moves the keeper by a give delta
     * @param delta movement delta
     */
    public void move(Point delta) {
        //do nothing if the game is already completed
        if (isGameComplete()) {
            return ;
        }

        //record current status to history status list (grid layout and keeper position)
        GameGrid previousGrid = new GameGrid(currentLevel.getObjectsGrid().getCOLUMNS(),currentLevel.getObjectsGrid().getROWS());
        Point keeperPosition = currentLevel.getKeeperPosition();
        Point previousKeeperPosition = new Point((int)keeperPosition.getX(),(int)keeperPosition.getY());
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

        //indicating whether the player made a move
        boolean keeperMoved = false;

        //decide what to do according to target position
        switch (keeperTarget.getCharSymbol()) {
            //keeper can not move to position of a wall or a portal
            case 'P':
            case 'W':
                break;

            //if target is a crate, check if the crate can be moved
            case 'C':

                GameObject crateTarget = currentLevel.getTargetObject(targetObjectPoint, delta);
                if(crateTarget.getCharSymbol() == 'P'){//if crate target is portal
                    char objectSymbolAtPortalExit = currentLevel.getObjectAt(currentLevel.getPortalExitPosition()).getCharSymbol();
                    //if no object at the portal exit
                    //then teleport the crate to the portal exit
                    if( objectSymbolAtPortalExit == ' ' || objectSymbolAtPortalExit == 'E'){
                        //save game status
                        GameLevel.resetGameGrid(previousGrid,currentLevel.getObjectsGrid());
                        currentLevel.addPreviousObjectGrid(previousGrid);
                        currentLevel.addPreviousKeeperPosition(previousKeeperPosition);
                        //teleport crate and keeper moves
                        currentLevel.teleportCrateTo(keeperTarget,targetObjectPoint);
                        currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                        keeperMoved = true;
                    }
                    else{
                        break;
                    }
                }
                //if crate target is not floor
                //then can not move
                else if (crateTarget.getCharSymbol() != ' ') {
                    break;
                }
                //else the crate can be moved
                else{
                    //save game status
                    GameLevel.resetGameGrid(previousGrid,currentLevel.getObjectsGrid());
                    currentLevel.addPreviousObjectGrid(previousGrid);
                    currentLevel.addPreviousKeeperPosition(previousKeeperPosition);

                    //move the crate and the keeper moves
                    currentLevel.moveGameObjectBy(keeperTarget, targetObjectPoint, delta);
                    currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                    keeperMoved = true;
                }

                break;

            case ' ':
                //save game status
                GameLevel.resetGameGrid(previousGrid,currentLevel.getObjectsGrid());
                currentLevel.addPreviousObjectGrid(previousGrid);
                //keeper moves
                currentLevel.addPreviousKeeperPosition(previousKeeperPosition);
                currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                keeperMoved = true;
                break;

            default:
                levelHandlerLogger.severe("The object to be moved was not a recognised GameObject."+keeperTarget.toString());
                throw new AssertionError("This should not have happened. Report this problem to the developer.");
        }

        //if keeper made a move, then update movesCount
        //checks if game is complete and go to next level if so
        if (keeperMoved) {
            keeperPosition.translate((int) delta.getX(), (int) delta.getY());
            movesCount++;
            totalMoveCount ++;
            //record previous game status and enable undo
        }

    }

}
