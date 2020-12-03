package object;
/**
 * Represents a keeper, which is subclass of GameObject
 * with a symbol character 'S'
 * @author Shuxiang Hu
 */
public class GameKeeper extends GameObject {
    /**
     * Constructs a GameKeeper object with a
     * character symbol 'S'
     */
    public GameKeeper() {
        setCharSymbol('S');
    }
}
