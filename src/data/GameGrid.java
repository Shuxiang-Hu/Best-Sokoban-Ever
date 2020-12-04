package data;


import object.GameDebugObject;
import object.GameObject;

import java.awt.*;
import java.util.Iterator;

/**
 * Represents a grid in the game by a 2-D array
 * @author COMP2013
 * @author hushuxiang
 */
public class GameGrid implements Iterable {

    /**
     * The number of columns in the grid
     */
    private final int COLUMNS;

    /**
     * The number of rows in the grid
     */
    private final int ROWS;

    /**
     * The 2-D array (grid) of GameObject objects
     */
    private GameObject[][] gameObjects;

    /**
     * Constructs a GameGrid Object by specifying numbers of columns and rows
     * @param columns column of the game object
     * @param rows row of the game object
     */
    public GameGrid(int columns, int rows) {
        COLUMNS = columns;
        ROWS = rows;
        gameObjects = new GameObject[COLUMNS][ROWS];
    }

    /**
     * Gets the number of rows of current GameGrid
     * @return the number of rows of current GameGrid
     */
    public int getROWS() {
        return ROWS;
    }

    /**
     * Gets the number of columns of current GameGrid
     * @return the number of columns of current GameGrid
     */
    public int getCOLUMNS() {
        return COLUMNS;
    }
    /**
     * Check if a point is
     * out of the bound of current GameGrid
     * @param x the x coordinate
     * @param y the y coordinate
     * @return returns true if the x or y coordinate is out of the bound, false otherwise
     */
    public boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= COLUMNS || y >= ROWS);
    }

    /**
     * Checks if a point is
     * out of the bound of current GameGrid
     * @param p is the point to be checked
     * @return returns true if the point is out of the bound, false otherwise
     * @deprecated
     */
    private boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);//fields accessed not through getters
    }

    /**
     * Gets the dimension of current GameGrid
     * @return returns a dimension Object representing the length and width of current GameGrid
     * @deprecated
     */
    public Dimension getDimension() {
        return new Dimension(COLUMNS, ROWS);
    }

    /**
     * Gets the target GameObject object from a source Point by a given delta
     * @param source the source Point
     * @param delta the direction and distance of movement
     * @return the GameObject from the source Point by the given delta
     */
    public GameObject getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * Translates a point to a new point
     * Object, given certain delta x and delta y
     * @param sourceLocation specifies the origin point
     * @param delta specifies the translation by x-axis and y-axis
     * @return returns a new translated Point object
     */
    public static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }


    /**
     * Gets the GameObject at a given position
     * @param col specifies the column of the GameObject
     * @param row specifies the row of the GameObject
     * @throws ArrayIndexOutOfBoundsException if the input column or row is out of bound
     * @return returns the GameObject at given position
     */
    public GameObject getGameObjectAt(int col, int row) throws ArrayIndexOutOfBoundsException {
        if (isPointOutOfBounds(col, row)) {
            throw new ArrayIndexOutOfBoundsException("The point [" + col + ":" + row + "] is outside the map.");
        }
        return gameObjects[col][row];
    }

    /**
     * Gets the GameObject at a given position
     * Object, given certain delta x and delta y
     * @param p specifies the position of the GameObject
     * @throws IllegalArgumentException if input point is null
     * @throws ArrayIndexOutOfBoundsException if the input point is out of bound
     * @return returns the GameObject at given position
     */
    public GameObject getGameObjectAt(Point p) {
        if (p == null) {
            throw new IllegalArgumentException("Point cannot be null.");
        }

        return getGameObjectAt((int) p.getX(), (int) p.getY());
    }

    /**
     * Adds a GameObject object to a given position in grid
     * @param gameObject specifies the GameObject to be add to the grid
     * @param x specifies the column of the GameObject
     * @param y specifies the row of the GameObject
     * @return returns false if can not add the object to the given position (i.e. out of bound),
     *         otherwise return the added object.
     */
    public boolean putGameObjectAt(GameObject gameObject, int x, int y) {
        if (isPointOutOfBounds(x, y)) {
            return false;
        }

        gameObjects[x][y] = gameObject;
        return true;
    }

    /**
     * Removes a GameObject object at a given position in grid
     * Object, given certain delta x and delta y
     * @param position specifies the position of the object to be removed
     * @return returns true if successfully removed
     * @deprecated
     */
    public boolean removeGameObjectAt(Point position) {
        return putGameObjectAt(null, position);
    }

    /**
     * This This method removes a GameObject object at a given position in grid
     * object, given certain delta x and delta y
     * @param gameObject specifies the GameObject object to be added
     * @param p specifies the point to add the GameObject object to
     * @return returns true if the object is successfully added, otherwise false
     */
    public boolean putGameObjectAt(GameObject gameObject, Point p) {
        return p != null && putGameObjectAt(gameObject, (int) p.getX(), (int) p.getY());
    }


    /**
     * Converts current game grid to a string
     * @return A String indication the layout of Game Objects in the grid
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(gameObjects.length);

        for (GameObject[] gameObject : gameObjects) {
            for (GameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    aGameObject = new GameDebugObject();
                }
                sb.append(aGameObject.getCharSymbol());
            }

            sb.append('\n');
        }

        return sb.toString();
    }


    /**
     * Returns a new GridIterator
     * @return a new GridIterator
     */
    @Override
    public Iterator<GameObject> iterator() {
        return new GridIterator();
    }

    /**
     * Represents an iterator for a GameGrid object
     */
    public class GridIterator implements Iterator<GameObject> {

        /**
         * the row of the GameObject current iterator points to
         */
        int row = 0;

        /**
         * the column of the GameObject current iterator points to
         */
        int column = 0;

        /**
         * Checks if the whole game grid has been iterated
         * @return true if the whole game grid has been iterated, no GameObject instance left
         */
        @Override
        public boolean hasNext() {
            return !(row >= ROWS && column == COLUMNS);
        }

        /**
         * Gets the next GameObject in current iterator
         * @return the next GameObject in current iterator
         */
        @Override
        public GameObject next() {
            if (column >= COLUMNS) {
                column = 0;
                row++;
            }
            return getGameObjectAt(column++, row);
        }
    }
}