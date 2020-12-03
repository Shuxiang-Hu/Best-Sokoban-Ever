package object;
/**
 * Represents a debug object, which is subclass of GameObject
 * with a symbol character '='
 * @author Shuxiang Hu
 */
public class GameDebugObject extends GameObject {
    public GameDebugObject() {
        setCharSymbol('=');
    }
}
