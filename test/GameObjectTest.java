//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//import sample.GameObject;
//import java.util.Arrays;
//import java.util.Collection;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertSame;
//
//
//@RunWith(Parameterized.class)
//public class GameObjectTest {
//    private char input;
//    private GameObject expectedOutput;
//    public GameObjectTest(char inputChar, GameObject expectedOutput){
//        this.input=inputChar;
//        this.expectedOutput = expectedOutput;
//    }
//
//    @Parameterized.Parameters
//    public static Collection setParameters(){
//        GameObject []enumerations = GameObject.values();
//        return Arrays.asList(new Object[][]{
//                {'W',enumerations[0]},
//                {' ',enumerations[1]},
//                {'C',enumerations[2]},
//                {'D',enumerations[3]},
//                {'S',enumerations[4]},
//                {'O',enumerations[5]},
//                {'=',enumerations[6]},
//                {'A',enumerations[0]},
//        });
//    }
//
//    @Test
//    public void testFromChar(){
//        System.out.println("Input char is: " + input);
//        GameObject testObject = GameObject.fromChar(input);
//        assertSame(expectedOutput,testObject);
//    }
//}
