package object;
/**
 * Represents a wall, which is subclass of GameObject
 * with a symbol character 'W'
 * @author Shuxiang Hu
 */
public class GameWall extends GameObject {
    /**
     * Constructs a GameWall object with a
     * character symbol 'W'
     */
    public GameWall() {
        setCharSymbol('W');;
    }
}
