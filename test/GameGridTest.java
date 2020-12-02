import data.GameGrid;
import factory.GameObjectFactory;
import object.GameObject;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Iterator;

import static org.junit.Assert.*;

public class GameGridTest {
    private GameGrid gameGrid;
    int column = 3, row = 2;
    @Before
    public void setUp(){
        gameGrid = new GameGrid(column,row);
        for(int i=0;i<column;i++){
            for(int j=0;j<row;j++)
            {
                gameGrid.putGameObjectAt(GameObjectFactory.fromChar('W'),i,j);
            }
        }
    }

    @Test
    public void testIsPointOutOfBounds(){
        assertFalse(gameGrid.isPointOutOfBounds(0,0));
        assertFalse(gameGrid.isPointOutOfBounds(1,1));
        assertTrue(gameGrid.isPointOutOfBounds(column,0));
        assertTrue(gameGrid.isPointOutOfBounds(0,row));
        assertTrue(gameGrid.isPointOutOfBounds(-1,0));
        assertTrue(gameGrid.isPointOutOfBounds(0,-1));
        assertTrue(gameGrid.isPointOutOfBounds(-1,-1));
    }

    @Test
    public void testGetter(){
        assertEquals(column, gameGrid.getCOLUMNS());
        assertEquals(row, gameGrid.getROWS());
    }

    @Test
    public void testGetPutObjectAt(){
        char [] testChars = {'W',' ','C','D','S','P','E','O','='};
        for(char c: testChars){
            assertTrue(gameGrid.putGameObjectAt(GameObjectFactory.fromChar(c),1,1));
            assertEquals(c,gameGrid.getGameObjectAt(1,1).getCharSymbol());
            assertTrue(gameGrid.putGameObjectAt(GameObjectFactory.fromChar(c),new Point(1,1)));
            assertEquals(c,gameGrid.getGameObjectAt(new Point(1,1)).getCharSymbol());
        }
        assertFalse(gameGrid.putGameObjectAt(GameObjectFactory.fromChar(testChars[0]),column,row));
    }

    @Test
    public void testGetTargetFromSourceException1(){
        try{
            gameGrid.getGameObjectAt(column,row);
            fail("Should have throw ArrayIndexOutOfBoundsException");
        }
        catch (ArrayIndexOutOfBoundsException e){
            assertTrue(e.getMessage().contains("The point [" + column + ":" + row + "] is outside the map."));
        }
    }

    @Test
    public void testGetTargetFromSourceException2(){
        try{
            gameGrid.getGameObjectAt(null);
            fail("Should have throw IllegalArgumentException");
        }
        catch (IllegalArgumentException e){
            assertTrue(e.getMessage().contains("Point cannot be null."));
        }
    }
    @Test
    public void testToString(){
        String expectedString = ("W".repeat(row)+"\n").repeat(column);
        assertEquals(expectedString,gameGrid.toString());
    }
}
