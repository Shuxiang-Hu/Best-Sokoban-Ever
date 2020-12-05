import factory.GameObjectFactory;
import object.GameObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Parameterized tests for GameObject class, its subclasses and its Factory
 */
@RunWith(Parameterized.class)
public class GameObjectTest {
    /**
     * Input character symbol
     */
    private char input;

    /**
     * Expected symbol of created game object
     */
    private char expectedOutput;

    public GameObjectTest(char inputChar, char expectedOutput){
        this.input=inputChar;
        this.expectedOutput = expectedOutput;
    }

    /**
     * Initialize parameters
     * @return a collection of input and expected output pairs
     */
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

    /**
     * Tests is a game object is created with correct symbol character
     */
    @Test
    public void testFromChar(){
        GameObject testObject = GameObjectFactory.fromChar(input);
        assertSame(expectedOutput,testObject.getCharSymbol());
        assertEquals(String.valueOf(expectedOutput),testObject.getStringSymbol());
    }
}
