package object;
/**
 * Represents a object in they game, the concrete type
 * is defined by the symbol character:
 * WALL('W'),
 * FLOOR(' '),
 * CRATE('C'),
 * DIAMOND('D'),
 * KEEPER('S'),
 * CRATE_ON_DIAMOND('O'),
 * PORTAL('P'),
 * PORTAL_EXIT('E'),
 * DEBUG_OBJECT('=');
 * @author Shuxiang Hu
 */
public abstract class GameObject {
//    WALL('W'),
//    FLOOR(' '),
//    CRATE('C'),
//    DIAMOND('D'),
//    KEEPER('S'),
//    CRATE_ON_DIAMOND('O'),
//    PORTAL('P'),
//    PORTAL_EXIT('E'),
//    DEBUG_OBJECT('=');

    /** Represents the symbol character of a game object
     * the default symbol is  ' '
     */

    private char SYMBOL = ' ';

    /**
     * Get the symbol of a GameObject object
     * @return GameObject returns a string object version of the symbol
     */
    public String getStringSymbol() {
        return String.valueOf(SYMBOL);
    }

    /**
     * Get the symbol character of a GameObject object
     * @return returns the symbol of a GameObject object
     */
    public char getCharSymbol() {
        return SYMBOL;
    }

    /**
     * Set the symbol character of a GameObject object to a new value
     * @param symbol new symbol character to assign
     */
    public void setCharSymbol(char symbol){
        this.SYMBOL = symbol;
    }

}