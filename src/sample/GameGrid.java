package sample;

import java.awt.*;
import java.util.Iterator;

public class GameGrid implements Iterable {

    private final int COLUMNS;
    private final int ROWS;
    private GameObject[][] gameObjects;

    public GameGrid(int columns, int rows) {
        COLUMNS = columns;
        ROWS = rows;
        gameObjects = new GameObject[COLUMNS][ROWS];
    }

    /**
     * This method is used to check if a point is
     * out of the bound of current GameGrid
     * @param x the x coordinate
     * @param y the y coordinate
     * @return returns true if the x or y coordinate is out of the bound, false otherwise
     */
    private boolean isPointOutOfBounds(int x, int y) {
        return (x < 0 || y < 0 || x >= COLUMNS || y >= ROWS);
    }

    /**
     * This method is used to check if a point is
     * out of the bound of current GameGrid
     * @param p is the point to be checked
     * @return returns true if the point is out of the bound, false otherwise
     */
    private boolean isPointOutOfBounds(Point p) {
        return isPointOutOfBounds(p.x, p.y);//fields accessed not through getters
    }
    /**
     * This method is used to get the dimension of
     * current GameGrid
     * @return returns a dimension Object representing the length and width of current GameGrid
     */
    public Dimension getDimension() {
        return new Dimension(COLUMNS, ROWS);
    }

    GameObject getTargetFromSource(Point source, Point delta) {
        return getGameObjectAt(translatePoint(source, delta));
    }

    /**
     * This method is used to translate a point to a new point
     * Object, given certain delta x and delta y
     * @param sourceLocation specifies the origin point
     * @param delta specifies the translation by x-axis and y-axis
     * @return returns a new translated Point object
     */
    static Point translatePoint(Point sourceLocation, Point delta) {
        Point translatedPoint = new Point(sourceLocation);
        translatedPoint.translate((int) delta.getX(), (int) delta.getY());
        return translatedPoint;
    }


    /**
     * This method is get the GameObject at a given position
     * @param col specifies the column of the GameObject
     * @param row specifies the row of the GameObject
     * @throws ArrayIndexOutOfBoundsException if the input column or row is out of bound
     * @return returns the GameObject at given position
     */
    public GameObject getGameObjectAt(int col, int row) throws ArrayIndexOutOfBoundsException {
        if (isPointOutOfBounds(col, row)) {
            if (GameModel.isDebugActive()) {
                System.out.printf("Trying to get null GameObject from COL: %d  ROW: %d", col, row);
            }
            throw new ArrayIndexOutOfBoundsException("The point [" + col + ":" + row + "] is outside the map.");
        }

        return gameObjects[col][row];
    }

    public int getROWS() {
        return ROWS;
    }

    public int getCOLUMNS() {
        return COLUMNS;
    }

    /**
     * This method is get the GameObject at a given position
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
     * This method is used to add a GameObject object to a given position in grid
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
        return gameObjects[x][y] == gameObject;
    }

    /**
     * This This method removes a GameObject object at a given position in grid
     * Object, given certain delta x and delta y
     * @param position specifies the position of the object to be removed
     * @return returns the removed GameObject object
     */
    public boolean removeGameObjectAt(Point position) {
        return putGameObjectAt(null, position);
    }


    /**
     * This This method removes a GameObject object at a given position in grid
     * Object, given certain delta x and delta y
     * @param gameObject specifies the GameObject object to be added
     * @param p specifies the point to add the GameObject object to
     * @return returns true if the object is successfully added, otherwise false
     */
    public boolean putGameObjectAt(GameObject gameObject, Point p) {
        return p != null && putGameObjectAt(gameObject, (int) p.getX(), (int) p.getY());
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(gameObjects.length);

        for (GameObject[] gameObject : gameObjects) {
            for (GameObject aGameObject : gameObject) {
                if (aGameObject == null) {
                    aGameObject = GameObject.DEBUG_OBJECT;
                }
                sb.append(aGameObject.getCharSymbol());
            }

            sb.append('\n');
        }

        return sb.toString();
    }

    @Override
    public Iterator<GameObject> iterator() {
        return new GridIterator();
    }

    public class GridIterator implements Iterator<GameObject> {
        int row = 0;
        int column = 0;

        @Override
        public boolean hasNext() {
            return !(row == ROWS && column == COLUMNS);
        }

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