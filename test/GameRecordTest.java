import data.GameRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameRecordTest {
    GameRecord record1,record2,record3;
    String username1 = "user1";
    String username2 = "user2";
    String username3 = "user3";
    int time1,time2,time3;
    int movesCount1,movesCount2,movesCount3;
    int levelIndex;
    @Before
    public void setUp(){
        time1 = 12;
        time2 = 12;
        time3 = 44;
        movesCount1 = 55;
        movesCount2 = 67;
        movesCount3 = 46;
        levelIndex =1;
        record1 = new GameRecord(username1,time1,movesCount1,levelIndex);
        record2 = new GameRecord(username2,time2,movesCount2,levelIndex);
        record3 = new GameRecord(username3,time3,movesCount3,levelIndex);
    }

    @Test
    public void testGetters(){
        assertEquals(time1,record1.getTime());
        assertEquals(movesCount1,record1.getNumberOfMoves());
    }

    @Test
    public void testSetter(){
        int newTime = 31;
        record1.setTime(newTime);
        assertEquals(newTime,record1.getTime());
    }

    @Test
    public void testIsBetterThan(){
        assertTrue(record1.isBetterThan(record2));
        assertTrue(record1.isBetterThan(record3));
        assertFalse(record1.isBetterThan(record1));
        assertFalse(record2.isBetterThan(record1));
        assertTrue(record2.isBetterThan(record3));
        assertFalse(record2.isBetterThan(record2));
        assertFalse(record3.isBetterThan(record1));
        assertFalse(record3.isBetterThan(record2));
        assertFalse(record3.isBetterThan(record3));
    }
    @Test
    public void testToString(){
        String expectedStr = "Level "+levelIndex+" GameRecord:\n" +
                "user name=" + username1 + "\n"+
                "time=" + time1 + "\n"+
                "number of moves=" + movesCount1 + "\n";
        assertEquals(expectedStr,record1.toString());
    }

    @Test
    public void testIsTopN(){
        List<GameRecord> recordList = new ArrayList<GameRecord>();
        recordList.add(record1);
        recordList.add(record2);
        recordList.add(record3);
        assertFalse(GameRecord.isTopN(recordList,time1,movesCount1,0));
        assertFalse(GameRecord.isTopN(recordList,time2,movesCount2,0));
        assertFalse(GameRecord.isTopN(recordList,time3,movesCount3,0));
        assertFalse(GameRecord.isTopN(recordList,time1,movesCount1,1));
        assertFalse(GameRecord.isTopN(recordList,time2,movesCount2,1));
        assertFalse(GameRecord.isTopN(recordList,time3,movesCount3,1));
        assertTrue(GameRecord.isTopN(recordList,time1,movesCount1,2));
        assertFalse(GameRecord.isTopN(recordList,time2,movesCount2,2));
        assertFalse(GameRecord.isTopN(recordList,time3,movesCount3,2));
        assertTrue(GameRecord.isTopN(recordList,time1,movesCount1,3));
        assertTrue(GameRecord.isTopN(recordList,time2,movesCount2,3));
        assertFalse(GameRecord.isTopN(recordList,time3,movesCount3,3));
        assertTrue(GameRecord.isTopN(recordList,time1,movesCount1,4));
        assertTrue(GameRecord.isTopN(recordList,time2,movesCount2,4));
        assertTrue(GameRecord.isTopN(recordList,time3,movesCount3,4));
    }
}
