package sample;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import static sample.GameGrid.translatePoint;


public final class GameLevel implements Iterable<GameObject> {

    private final String name;
    private final GameGrid objectsGrid;
    private final GameGrid initialObjectGrid;
    private final GameGrid previousObjectGrid;
    private final GameGrid diamondsGrid;
    private final int index;
    private List<GameRecord> levelRecords;
    private int numberOfDiamonds = 0;
    private Point initialKeeperPosition;
    private Point keeperPosition = new Point(0, 0);
    private Point previousKeeperPosition = new Point(0, 0);
    private GameRecorder levelRecorder ;
    private boolean undo;

    public Point getInitialKeeperPosition() {
        return initialKeeperPosition;
    }

    public GameGrid getInitialObjectGrid() {
        return initialObjectGrid;
    }

    public Point getPreviousKeeperPosition() {
        return previousKeeperPosition;
    }

    public void setKeeperPosition(Point p) {
        this.keeperPosition.setLocation(p);
    }

    public void setPreviousKeeperPosition(Point p) {
        this.previousKeeperPosition.setLocation(p);
    }

    public GameGrid getObjectsGrid() {
        return objectsGrid;
    }

    public List<GameRecord> getLevelRecords() {
        return levelRecords;
    }

    public void setLevelRecords(List<GameRecord> levelRecords) {
        this.levelRecords = levelRecords;
    }

    /**
 * Constructs a game Level with its name, index and layout
 * @param levelName - the name of current game level
 * @param levelIndex - the index of current game level
 * @param raw_level - the layout of the grid of this game level
 * */
    public GameLevel(String levelName, int levelIndex, List<String> raw_level) {
        if (GameModel.isDebugActive()) {
            System.out.printf("[ADDING LEVEL] LEVEL [%d]: %s\n", levelIndex, levelName);
        }

        name = levelName;
        index = levelIndex;
        undo = false;
        int rows = raw_level.size();
        int columns = raw_level.get(0).trim().length();

        initialObjectGrid = new GameGrid(rows,columns);
        objectsGrid = new GameGrid(rows, columns);
        previousObjectGrid = new GameGrid(rows,columns);
        diamondsGrid = new GameGrid(rows, columns);
        levelRecorder = new GameRecorder(levelIndex);
        levelRecords = levelRecorder.loadRecords();
        GameRecorder.sortRecords(levelRecords);

        for (int row = 0; row < raw_level.size(); row++) {

            // Loop over the string one char at a time because it should be the fastest way:
            // http://stackoverflow.com/questions/8894258/fastest-way-to-iterate-over-all-the-chars-in-a-string
            for (int col = 0; col < raw_level.get(row).length(); col++) {
                // The game object is null when the we're adding a floor or a diamond
                GameObject curTile = GameObject.fromChar(raw_level.get(row).charAt(col));

                //if curTile is a Diamond, then add it to diamondsGrid and add floor to the same position in objectsGrid
                //if it is a keeper, then set keeperPosition
                if (curTile == GameObject.DIAMOND) {
                    numberOfDiamonds++;
                    diamondsGrid.putGameObjectAt(curTile, row, col);
                    curTile = GameObject.FLOOR;
                } else if (curTile == GameObject.KEEPER) {
                    keeperPosition = new Point(row, col);
                    initialKeeperPosition = new Point(row,col);
                }

                objectsGrid.putGameObjectAt(curTile, row, col);
                initialObjectGrid.putGameObjectAt(curTile,row,col);
                curTile = null;
            }
        }
    }

    public String getName() {
        return name;
    }

    int getIndex() {
        return index;
    }

    Point getKeeperPosition() {
        return keeperPosition;
    }

    /**
     * Checks if current game level has been completed
     * @return true if current game level has be completed by the player
     * (i.e all the crates are filled with a diamond)
     */
    boolean isComplete() {
        int cratedDiamondsCount = 0;
        for (int row = 0; row < objectsGrid.getROWS(); row++) {
            for (int col = 0; col < objectsGrid.getCOLUMNS(); col++) {
                if (objectsGrid.getGameObjectAt(col, row) == GameObject.CRATE && diamondsGrid.getGameObjectAt(col, row) == GameObject.DIAMOND) {
                    cratedDiamondsCount++;
                }
            }
        }

        return cratedDiamondsCount >= numberOfDiamonds;
    }

    /**
     * Finds a GameObject object from a source point by a give delta
     * @param source the source point
     * @param delta the amount of movement
     * @return a GameObject object from a source point by a give delta
     */
    GameObject getTargetObject(Point source, Point delta) {
        return objectsGrid.getTargetFromSource(source, delta);
    }

    /**
     * Finds a GameObject object at a given point in the objectsGrid
     * @param p the position of the GameObject object
     * @return a GameObject object at a given point
     */
    GameObject getObjectAt(Point p) {
        return objectsGrid.getGameObjectAt(p);
    }

    void moveGameObjectBy(GameObject object, Point source, Point delta) {
        moveGameObjectTo(object, source, translatePoint(source, delta));
    }

    public GameGrid getPreviousObjectGrid() {
        return previousObjectGrid;
    }

    public static void resetGameGrid (GameGrid oldGrid, GameGrid newGrid){
        for (int row = 0; row < oldGrid.getROWS(); row++) {
            for (int col = 0; col < oldGrid.getCOLUMNS(); col++) {
                char tempChar = newGrid.getGameObjectAt(row,col).getCharSymbol();
                oldGrid.putGameObjectAt(GameObject.fromChar(tempChar),row,col);
            }
        }
    }



    public void setNumberOfDiamonds(int numberOfDiamonds) {
        this.numberOfDiamonds = numberOfDiamonds;
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

    @Override
    public String toString() {
        GameGrid gameGrid = new GameGrid(objectsGrid.getROWS(), objectsGrid.getCOLUMNS());
        for (int row = 0; row < gameGrid.getROWS(); row++) {
            for (int col = 0; col < gameGrid.getCOLUMNS(); col++) {
                char tempChar = objectsGrid.getGameObjectAt(row,col).getCharSymbol();
                if(diamondsGrid.getGameObjectAt(row,col) == GameObject.DIAMOND){
                    tempChar = 'D';
                }
                gameGrid.putGameObjectAt(GameObject.fromChar(tempChar),row,col);
            }
        }

        return gameGrid.toString();
    }

    @Override
    public Iterator<GameObject> iterator() {
        return new LevelIterator();
    }

    public String getHighScoresString() {
        int numberOfRecords = levelRecords.size();
        int topN = 10;
        String returnVal = "";
        if(numberOfRecords==0){
            return "No records for this level";
        }
        else
        {
            int numberOfRecordsToShow = Math.min(topN,numberOfRecords);
            for(int i=0;i<numberOfRecordsToShow;i++)
            {
                String recordString = "";
                recordString += levelRecords.get(i).toString();
                recordString = recordString.replace("\n","     ").replace("Level "+index+" GameRecord:","").replace('=',':').trim();
                recordString += "\n";
                returnVal +=recordString;
            }

            return returnVal;
        }
    }

    public void saveRecord(String username, long timeInterval, int movesCount) {
        levelRecords.add(new GameRecord(username,timeInterval,movesCount,index));
        GameRecorder.sortRecords(levelRecords);
        System.out.println(levelRecords.get(levelRecords.size()-1).toString());
        levelRecorder.saveRecord(levelRecords);
    }

    public class LevelIterator implements Iterator<GameObject> {

        int column = 0;
        int row = 0;

        @Override
        public boolean hasNext() {
            return !(row == objectsGrid.getROWS() - 1 && column == objectsGrid.getCOLUMNS());
        }

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

            if (diamond == GameObject.DIAMOND) {
                if (object == GameObject.CRATE) {
                    retObj = GameObject.CRATE_ON_DIAMOND;
                } else if (object == GameObject.FLOOR) {
                    retObj = diamond;
                } else {
                    retObj = object;
                }
            }

            return retObj;
        }

        public Point getCurrentPosition() {
            return new Point(column, row);
        }
    }

    public boolean isUndoActive() {
        return undo;
    }

    public void setUndo(boolean newUndo) {
        this.undo = newUndo;
    }
}