import model.GameRecorder;
import data.GameLevel;
import data.GameRecord;
import factory.GameObjectFactory;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameLevelTest {
    GameLevel level;
    String levelName;
    int levelIndex;
    List<String> raw_level;
    Point initialKeeperPosition, portalExitPosition;
    @Before
    public void setUp(){
        levelName = "Simple Start";
        levelIndex = 1;
        raw_level = new ArrayList<>();
        raw_level.add("WWWWWWWWWWWWWWWWWWWW\n");
        raw_level.add("W    W             W\n");
        raw_level.add("W C  W D     E     W\n");
        raw_level.add("w    w      WWWWWWWW\n");
        raw_level.add("w    WWWW  WWWWWWWWW\n");
        raw_level.add("w            WWWWWWW\n");
        raw_level.add("w    WWWWW   WWWWWWW\n");
        raw_level.add("w    WWWWWWWWWWWWWWW\n");
        raw_level.add("w    WWWWWWWWWWWWWWW\n");
        raw_level.add("W    WWWWWWWWWWWWWWW\n");
        raw_level.add("w    WWWWWWWWWWWWWWW\n");
        raw_level.add("w           WWWWWWWW\n");
        raw_level.add("w  P    W WWWWWWWWWW\n");
        raw_level.add("wWWWWWW W WWWWWWWWWW\n");
        raw_level.add("wWWWWWW W WWWWWWWWWW\n");
        raw_level.add("wWWWWWW W WWWWWWWWWW\n");
        raw_level.add("wWWWWWW W WWWWWWWWWW\n");
        raw_level.add("wWWWWWW W WWWWWWWWWW\n");
        raw_level.add("wWWWWWW   SWWWWWWWWW\n");
        raw_level.add("wwwwwwwwWwwwwwwwwwww\n");
        initialKeeperPosition = new Point(18,10);
        portalExitPosition = new Point(2,13);
        level = new GameLevel(levelName,levelIndex,raw_level);
    }


    @Test
    public void testGetters(){
        //test getKeeperPosition()
        assertEquals(initialKeeperPosition.getX(), level.getInitialKeeperPosition().getX(), 0.0);
        assertEquals(initialKeeperPosition.getY(), level.getInitialKeeperPosition().getY(), 0.0);

        //test getKeeperPosition()
        assertEquals(initialKeeperPosition.getX(), level.getKeeperPosition().getX(), 0.0);
        assertEquals(initialKeeperPosition.getY(), level.getKeeperPosition().getY(), 0.0);



        //test getName()
        assertEquals(levelName,level.getName());

        //test getPortalExitPosition()
        assertEquals(portalExitPosition.getX(), level.getPortalExitPosition().getX(), 0.0);
        assertEquals(portalExitPosition.getY(), level.getPortalExitPosition().getY(), 0.0);

        //test getLevelIndex()
        assertEquals(levelIndex,level.getIndex());
    }

    @Test
    public void testSetters(){
        //test setKeeperPosition
        Point newKeeperPos = new Point(17,9);
        level.setKeeperPosition(newKeeperPos);
        assertEquals(newKeeperPos.getX(), level.getKeeperPosition().getX(), 0.0);
        assertEquals(newKeeperPos.getY(), level.getKeeperPosition().getY(), 0.0);

        //test setPreviousKeeperPosition
        Point previousKeeperPos = new Point(18,9);
    }

    @Test
    public void testIsComplete(){
        assertFalse(level.isComplete());
        level.moveGameObjectTo(GameObjectFactory.fromChar('C'),new Point(2,2),new Point(2,7));
        assertTrue(level.isComplete());
    }

    @Test
    public void testGetTargetObject(){
        Point delta = new Point(0,-1);
        assertEquals(' ', level.getTargetObject(initialKeeperPosition,delta).getCharSymbol());
    }

    @Test
    public void testGetObjectAt(){
        assertEquals('S',level.getObjectAt(level.getKeeperPosition()).getCharSymbol());
        assertEquals('E',level.getObjectAt(level.getPortalExitPosition()).getCharSymbol());
    }

    @Test
    public void testMoveObjectBy(){
        Point delta = new Point(0,-1);
        level.moveGameObjectBy(GameObjectFactory.fromChar('S'),level.getKeeperPosition(),delta);
        assertEquals('S', level.getObjectAt(new Point(18,9)).getCharSymbol());
    }

    @Test
    public void testResetGameGrid(){
        Point delta = new Point(0,-1);
        level.moveGameObjectBy(GameObjectFactory.fromChar('S'),level.getKeeperPosition(),delta);
        GameLevel.resetGameGrid(level.getObjectsGrid(), level.getInitialObjectGrid());
        assertEquals('S', level.getObjectAt(level.getInitialKeeperPosition()).getCharSymbol());
    }

    @Test
    public void testMoveObjectTo(){
        Point destination = new Point(3,3);
        level.moveGameObjectTo(GameObjectFactory.fromChar('S'),level.getKeeperPosition(),destination);
        assertEquals('S', level.getObjectAt(destination).getCharSymbol());
    }

    @Test
    public void testGetTop10Record(){
        List<GameRecord> expectedRecordList = level.getTop10Record();//top 10 of current level
        List<GameRecord> actualRecordList = new GameRecorder(level.getIndex()).loadRecords();//top 10 in the record file
        //compare the records in the record lists one by one
        for(int i=0;i<expectedRecordList.size();i++){
            GameRecord expected = expectedRecordList.get(i);
            GameRecord actual = actualRecordList.get(i);
            assertEquals(expected.getTime(),actual.getTime());
            assertEquals(expected.getNumberOfMoves(),actual.getNumberOfMoves());
            assertEquals(expected.toString(),actual.toString());
        }

    }


}
