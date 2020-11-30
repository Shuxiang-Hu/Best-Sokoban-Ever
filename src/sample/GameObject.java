package sample;

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

    char SYMBOL = ' ';

    /**
     * This method is used to get the symbol of a GameObject object
     * @return GameObject returns a string object version of the symbol
     */
    public String getStringSymbol() {
        return String.valueOf(SYMBOL);
    }

    /**
     * This method is used to get the symbol of a GameObject object
     * @return GameObject returns the symbol of a GameObject object
     */
    public char getCharSymbol() {
        return SYMBOL;
    }

}