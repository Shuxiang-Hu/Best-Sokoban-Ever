package data;


import model.GameRecorder;
import factory.GameObjectFactory;
import object.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static data.GameGrid.translatePoint;


/**
 * Represents a level in Sokoban, implementing
 * @author COMP2012
 * @author Shuxiang Hu
 */
public final class GameLevel implements Iterable<GameObject> {

    /**
     * Name of current level
     */
    private final String name;

    /**
     * Index of current level
     */
    private final int index;

    /**
     * the object grid of current level
     */
    private final GameGrid objectsGrid;

    /**
     * the initial status of objectGrid
     */
    private final GameGrid initialObjectGrid;

    /**
     * A list of GameGrids to keep track of history game object layouts
     */
    private List<GameGrid> previousObjectGrids;

    /**
     * A game grid used only to keep track of position of diamonds
     */
    private final GameGrid diamondsGrid;


    /**
     * A list of records of current level
     */
    private List<GameRecord> levelRecords;

    /**
     * Numbers of diamonds in this level
     */
    private int numberOfDiamonds = 0;

    /**
     * Initial position of the keeper
     */
    private Point initialKeeperPosition;

    /**
     * The current position of the keeper
     */
    private Point keeperPosition = new Point(0, 0);

    /**
     * A list of Points to keep track of the history position of the keeper
     */
    private List<Point> previousKeeperPositions;

    /**
     * The position of the exit of the portal
     */
    private Point portalExitPosition = new Point(0, 0);

    /**
     * A GameRecorder which helps to write and read records file
     */
    private GameRecorder levelRecorder ;

    /**
     * Constructs a game Level with its name, index and layout
     * @param levelName - the name of current game level
     * @param levelIndex - the index of current game level
     * @param raw_level - the layout of the grid of this game level
     * */
    public GameLevel(String levelName, int levelIndex, List<String> raw_level) {

        //initialize fields
        name = levelName;
        index = levelIndex;
        int rows = raw_level.size();
        int columns = raw_level.get(0).trim().length();
        previousObjectGrids = new ArrayList<>();
        previousKeeperPositions = new ArrayList<>();
        initialObjectGrid = new GameGrid(rows,columns);
        objectsGrid = new GameGrid(rows, columns);
        previousObjectGrids.add(objectsGrid);
        previousKeeperPositions.add(initialKeeperPosition);
        diamondsGrid = new GameGrid(rows, columns);
        levelRecorder = new GameRecorder(levelIndex);
        levelRecords = levelRecorder.loadRecords();
        GameRecorder.sortRecords(levelRecords);

        //initialize the game grid, one GameObject at a time
        for (int row = 0; row < raw_level.size(); row++) {

            // Loop over the string one char at a time because it should be the fastest way:
            // http://stackoverflow.com/questions/8894258/fastest-way-to-iterate-over-all-the-chars-in-a-string
            for (int col = 0; col < raw_level.get(row).length(); col++) {
                // The game object is null when the we're adding a floor or a diamond
                GameObject curTile = GameObjectFactory.fromChar(raw_level.get(row).charAt(col));


                //if curTile is a Diamond, then add it to diamondsGrid and add floor to the same position in objectsGrid
                //if it is a keeper, then set keeperPosition
                //if it the portal exit, then set position of portal exit
                if (curTile.getCharSymbol() == 'D') {
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = new GameFloor();
                } else if (curTile.getCharSymbol() == 'S') {
                    keeperPosition = new Point(row, col);
                    initialKeeperPosition = new Point(row,col);
                    diamondsGrid.putGameObjectAt(new GameDebugObject(), row, col);
                }
                else if (curTile.getCharSymbol() == 'E'){
                    portalExitPosition = new Point(row,col);
                    diamondsGrid.putGameObjectAt(new GameDebugObject(), row, col);
                }
                else {
                    diamondsGrid.putGameObjectAt(new GameDebugObject(), row, col);
                }

                //add the game object to the object grid
                objectsGrid.putGameObjectAt(curTile, row, col);
                initialObjectGrid.putGameObjectAt(curTile,row,col);

                curTile = null;
            }
        }
    }

    /**
     * Gets the initial position of the keeper
     * @return A Point object representing the position of the keeper
     */
    public Point getInitialKeeperPosition() {
        return initialKeeperPosition;
    }

    /**
     * Get the initial object grid
     * @return Reference to initial object grid
     */
    public GameGrid getInitialObjectGrid() {
        return initialObjectGrid;
    }

    /**
     * Gets the position of the keeper before the most recent move
     * @return Reference to the Point representing previous keeper position, null if no previous game status
     */
    public Point getLastPreviousKeeperPosition(){
        int length = previousKeeperPositions.size();
        if(length == 0){
            return null;
        }
        else {
            return previousKeeperPositions.remove(length-1);
        }
    }

    /**
     * Gets the reference to the objectGrid
     * @return Reference to the objectGrid
     */
    public GameGrid getObjectsGrid() {
        return objectsGrid;
    }

    /**
     * Gets the list of GameRecord objects of current level
     * @return List of GameRecord objects of current level
     */
    public List<GameRecord> getLevelRecords() { return levelRecords; }

    /**
     * Gets the position of the portal exit
     * @return the position of the portal exit
     */
    public Point getPortalExitPosition() {
        return portalExitPosition;
    }

    /**
     * Gets the name of current level instance
     * @return the name of current level instance
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the index of current level instance
     * @return the index of current level instance
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the position of the keeper
     * @return the position of the keeper
     */
    public Point getKeeperPosition() {
        return keeperPosition;
    }

    /**
     * Gets the grid before the most recent move
     * @return Reference to the previous grid layout, null if no previous game status
     */
    public GameGrid getLastPreviousObjectGrid() {
        int length = previousObjectGrids.size();
        if(length == 0){
            return null;
        }
        else {
            return previousObjectGrids.remove(length-1);
        }
    }

    /**
     * Sets the keeper position to a new point
     * @param p the new position of the keeper
     */
    public void setKeeperPosition(Point p) {
        this.keeperPosition.setLocation(p);
    }

    /**
     * Sets the number of diamonds in this level
     * @param numberOfDiamonds number of diamonds in this level
     * @deprecated
     */
    public void setNumberOfDiamonds(int numberOfDiamonds) {
        this.numberOfDiamonds = numberOfDiamonds;
    }

    /**
     * Add a history keeper position to the list of previous keeper positions
     * @param p the previous keeper position to be recorded
     */
    public void addPreviousKeeperPosition(Point p) {
        previousKeeperPositions.add(p);
    }

    /**
     * Add a history GameGrid to the list of previous GameGrid objects
     * @param oldGrid the previous grid to be recorded
     */
    public void addPreviousObjectGrid(GameGrid oldGrid) {
        previousObjectGrids.add(oldGrid);
    }

    /**
     * Checks if current game level has been completed
     * @return true if current game level has be completed by the player
     * (i.e all the crates are filled with a diamond)
     */
    public boolean isComplete() {
        int cratedDiamondsCount = 0;
        //count the number of crate on diamonds
        for (int row = 0; row < objectsGrid.getROWS(); row++) {
            for (int col = 0; col < objectsGrid.getCOLUMNS(); col++) {
                if (objectsGrid.getGameObjectAt(col, row).getCharSymbol() == 'C' && diamondsGrid.getGameObjectAt(col, row).getCharSymbol() == 'D') {
                    cratedDiamondsCount++;
                }
            }
        }

        //the game is completed when all the crates are on diamonds
        return cratedDiamondsCount >= numberOfDiamonds;
    }

    /**
     * Gets a GameObject object from a source point by a give delta
     * @param source the source point
     * @param delta the distance and direction of movement
     * @return a GameObject object from a source point by a give delta
     */
    public GameObject getTargetObject(Point source, Point delta) {
        return objectsGrid.getTargetFromSource(source, delta);
    }

    /**
     * Gets a GameObject object at a given point in the objectsGrid
     * @param p the position of the GameObject object
     * @return a GameObject object at the given point
     */
    public GameObject getObjectAt(Point p) {
        return objectsGrid.getGameObjectAt(p);
    }

    /**
     * Moves a GameObject from a point to another
     * @param object the GameObject to be moved
     * @param source the start point
     * @param delta the destination
     */
    public void moveGameObjectBy(GameObject object, Point source, Point delta) {
        moveGameObjectTo(object, source, translatePoint(source, delta));
    }

    /**
     * Reset a GameGrid to an exact copy of another grid
     * @param oldGrid the grid to be reset
     * @param newGrid the grid to be copied
     */
    public static void resetGameGrid (GameGrid oldGrid, GameGrid newGrid){
        for (int row = 0; row < oldGrid.getROWS(); row++) {
            for (int col = 0; col < oldGrid.getCOLUMNS(); col++) {
                char tempChar = newGrid.getGameObjectAt(row,col).getCharSymbol();
                oldGrid.putGameObjectAt(GameObjectFactory.fromChar(tempChar),row,col);
            }
        }
    }

    /**
     * Moves a GameObject from one position to another in object grid
     * @param object the object to move
     * @param source the start point
     * @param destination the destination
     */
    public void moveGameObjectTo(GameObject object, Point source, Point destination) {
        objectsGrid.putGameObjectAt(getObjectAt(destination), source);
        objectsGrid.putGameObjectAt(object, destination);
    }
    /**
     * Teleports a crate to the position of the exit of the portal
     * @param object the object to move
     * @param source the start point
     */
    public void teleportCrateTo(GameObject object, Point source) {
        objectsGrid.putGameObjectAt(new GameFloor(), source);
        objectsGrid.putGameObjectAt(object, portalExitPosition);
    }

    /**
     * Converts a game level to a String
     * @return a string representing the current status of the game level
     */
    @Override
    public String toString() {
        GameGrid gameGrid = new GameGrid(objectsGrid.getROWS(), objectsGrid.getCOLUMNS());
        for (int row = 0; row < gameGrid.getROWS(); row++) {
            for (int col = 0; col < gameGrid.getCOLUMNS(); col++) {
                char tempChar = objectsGrid.getGameObjectAt(row,col).getCharSymbol();
                if(diamondsGrid.getGameObjectAt(row,col).getCharSymbol() == 'D'){
                    tempChar = 'D';
                }
                gameGrid.putGameObjectAt(GameObjectFactory.fromChar(tempChar),row,col);
            }
        }

        return gameGrid.toString();
    }

    /**
     * Gets the top 10 records of current level
     * @return list of top 10 records, all records in the list if there are less than 10 records
     */
    public List<GameRecord> getTop10Record() {
        List<GameRecord> top10 = levelRecords.subList(0,Math.min(10,levelRecords.size()));
        GameRecorder.sortRecords(top10);
        return top10;
    }

    /**
     * Returns a new LevelIterator
     * @return a new levelIterator
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new LevelIterator();
    }

    /**
     * Saves a new game record into the record file of current level
     * @param username the user name of the player who created the record
     * @param timeInterval the time for the player to complete this level
     * @param movesCount the moves count for the player to complete this level
     */
    public void saveRecord(String username, long timeInterval, int movesCount) {
        levelRecords.add(new GameRecord(username,timeInterval,movesCount,index));
        GameRecorder.sortRecords(levelRecords);
        levelRecorder.saveRecord(levelRecords);
    }

    /**
     * Represents an iterator for a game level
     */
    public class LevelIterator implements Iterator<GameObject> {

        /**
         * the pointer to column of current GameObject
         */
        int column = 0;

        /**
         * the pointer to row of current GameObject
         */
        int row = 0;

        /**
         * Checks if the whole game grid has been iterated
         * @return true if the whole game grid has been iterated, no GameObject instance left
         */
        @Override
        public boolean hasNext() { return !(row == objectsGrid.getROWS() - 1 && column == objectsGrid.getCOLUMNS()); }

        /**
         * Gets the next GameObject in current iterator
         * @return the next GameObject in current iterator
         */
        @Override
        public GameObject next() {
            if (column >= objectsGrid.getCOLUMNS()) {
                column = 0;
                row++;
            }

            GameObject object = objectsGrid.getGameObjectAt(column, row);
            GameObject diamond = diamondsGrid.getGameObjectAt(column, row);
            GameObject retObj = object;

            column++;

            if (diamond.getCharSymbol() == 'D') {
                if (object.getCharSymbol() == 'C') {
                    retObj = new GameCrateOnDiamond();
                } else if (object.getCharSymbol() == ' ') {
                    retObj = diamond;
                }
            }

            return retObj;
        }

        /**
         * Gets the position of the GameObject that the iterator points to
         * @return a Point representing the position of the GameObject that the iterator points to
         */
        public Point getCurrentPosition() {
            return new Point(column, row);
        }
    }

}