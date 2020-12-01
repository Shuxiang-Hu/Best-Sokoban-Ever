import factory.GameObjectFactory;
import object.GameObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

//    WALL('W'),
//    FLOOR(' '),
//    CRATE('C'),
//    DIAMOND('D'),
//    KEEPER('S'),
//    CRATE_ON_DIAMOND('O'),
//    PORTAL('P'),
//    PORTAL_EXIT('E'),
//    DEBUG_OBJECT('=');
@RunWith(Parameterized.class)
public class GameObjectTest {
    private char input;
    private char expectedOutput;
    public GameObjectTest(char inputChar, char expectedOutput){
        this.input=inputChar;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters
    public static Collection setParameters(){
        char [] enumerations = {'W',' ','C','D','S','P','E','O','='};
        return Arrays.asList(new Object[][]{
                {'W',enumerations[0]},
                {'w',enumerations[0]},
                {' ',enumerations[1]},
                {'C',enumerations[2]},
                {'c',enumerations[2]},
                {'D',enumerations[3]},
                {'d',enumerations[3]},
                {'S',enumerations[4]},
                {'s',enumerations[4]},
                {'P',enumerations[5]},
                {'p',enumerations[5]},
                {'E',enumerations[6]},
                {'e',enumerations[6]},
                {'O',enumerations[7]},
                {'o',enumerations[7]},
                {'=',enumerations[8]},
                {'A',enumerations[0]},
        });
    }

    @Test
    public void testFromChar(){
        GameObject testObject = GameObjectFactory.fromChar(input);
        assertSame(expectedOutput,testObject.getCharSymbol());
        assertEquals(String.valueOf(expectedOutput),testObject.getStringSymbol());
    }
}
