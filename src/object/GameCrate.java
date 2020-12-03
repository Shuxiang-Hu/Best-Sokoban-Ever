package object;

/**
 * Represents a crate, which is subclass of GameObject
 * with a symbol character 'C'
 * @author Shuxiang Hu
 */
public class GameCrate extends GameObject {
    /**
     * Constructs a GameCrate object with a
     * character symbol 'C'
     */
    public GameCrate() {
        setCharSymbol('C');
    }
}
