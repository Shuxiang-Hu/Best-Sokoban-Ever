package component;

import MVC.GameModel;
import data.GameGrid;
import data.GameLevel;
import object.GameObject;
import object.GraphicObject;

import java.awt.*;
import java.util.List;

public class GameLevelHandler {
    private List<GameLevel> levels;
    private boolean gameComplete = false;
    private int movesCount = 0;
    private int totalMoveCount = 0;
    private int previousMovesCount=0;
    private long previousTimeInterval=0;
    private long startTime = 0;
    private long timeInterval = 0;
    private GameLogger levelHandlerLogger;
    private GameLevel currentLevel;

    public GameLevelHandler(List<GameLevel> gameLevels) {
        levels = gameLevels;
        currentLevel = getNextLevel();
        levelHandlerLogger = GameLogger.getUniqueInstance();
    }
    public void setStartTime(long currentTimeMillis) {
        this.startTime = currentTimeMillis;
    }
    /**
     * gets current game level
     * @return current game level
     */
    public GameLevel getCurrentLevel() {
        return currentLevel;
    }
    public long getTimeInterval() {
        return timeInterval;
    }
    /**
     * gets the movesCount
     * @return the number of moves so far
     */
    public int getMovesCount() {
        return movesCount;
    }
    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }
    public int getTotalMoveCount() {
        return totalMoveCount;
    }
    public void gotoNextLevel() {
        currentLevel = getNextLevel();
        GraphicObject.setKeeperPosition("down");
    }

    public void resetLevel() {
        GameLevel.resetGameGrid(currentLevel.getObjectsGrid(),currentLevel.getInitialObjectGrid());
        currentLevel.setKeeperPosition(currentLevel.getInitialKeeperPosition());
        totalMoveCount -= movesCount;
        movesCount = 0;
        startTime = System.currentTimeMillis();
    }
    public void saveGameRecord(String username) {
        levels.get(currentLevel.getIndex()-2).saveRecord(username,previousTimeInterval/1000,previousMovesCount);
    }

    /**
     * gets next game level
     * @return next Level, null if all levels are completed
     */
    public GameLevel getNextLevel() {
        previousMovesCount = movesCount;
        movesCount = 0;
        previousTimeInterval = timeInterval;
        timeInterval = 0;
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
     * undoes a move
     */
    public void undo() {

        GameGrid previousGrid =  currentLevel.getLastPreviousObjectGrid();
        Point previousKeeperPosition = currentLevel.getLastPreviousKeeperPosition();
        if((previousGrid != null) && (previousKeeperPosition != null)){
            GameLevel.resetGameGrid(currentLevel.getObjectsGrid(), previousGrid);
            currentLevel.setKeeperPosition(previousKeeperPosition);
            movesCount++;
            totalMoveCount ++;
        }
    }
    public boolean isGameComplete() {
        return gameComplete;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setMovesCount(int movesCount) {
        this.movesCount = movesCount;
    }

    public List<GameLevel> getLevels() {
        return levels;
    }

    public void setTotalMoveCount(int totalMoveCount) {
        this.totalMoveCount = totalMoveCount;
    }

    public void move(Point delta) {
        if (isGameComplete()) {
            return ;
        }

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

        boolean keeperMoved = false;

        //decide what to do according to target position
        switch (keeperTarget.getCharSymbol()) {

            case 'P':
            case 'W':
                break;

            case 'C':

                GameObject crateTarget = currentLevel.getTargetObject(targetObjectPoint, delta);
                if(crateTarget.getCharSymbol() == 'P'){
                    char objectSymbolAtPortalExit = currentLevel.getObjectAt(currentLevel.getPortalExitPosition()).getCharSymbol();
                    if( objectSymbolAtPortalExit == ' ' || objectSymbolAtPortalExit == 'E'){
                        GameLevel.resetGameGrid(previousGrid,currentLevel.getObjectsGrid());
                        currentLevel.addPreviousObjectGrid(previousGrid);
                        currentLevel.addPreviousKeeperPosition(previousKeeperPosition);
                        currentLevel.teleportCrateTo(keeperTarget,targetObjectPoint);
                        currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                        keeperMoved = true;
                    }
                    else{
                        break;
                    }
                }
                else if (crateTarget.getCharSymbol() != ' ') {
                    break;
                }
                else{
                    GameLevel.resetGameGrid(previousGrid,currentLevel.getObjectsGrid());
                    currentLevel.addPreviousObjectGrid(previousGrid);
                    currentLevel.addPreviousKeeperPosition(previousKeeperPosition);
                    currentLevel.moveGameObjectBy(keeperTarget, targetObjectPoint, delta);
                    currentLevel.moveGameObjectBy(keeper, keeperPosition, delta);
                    keeperMoved = true;
                }

                break;

            case ' ':
                GameLevel.resetGameGrid(previousGrid,currentLevel.getObjectsGrid());
                currentLevel.addPreviousObjectGrid(previousGrid);
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
