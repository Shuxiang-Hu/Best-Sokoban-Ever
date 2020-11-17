package sample;

public enum GameObject {
    WALL('W'),
    FLOOR(' '),
    CRATE('C'),
    DIAMOND('D'),
    KEEPER('S'),
    CRATE_ON_DIAMOND('O'),
    DEBUG_OBJECT('=');

    //constant value not in capital letter
    //rule 4 violated
    private final char symbol;

    GameObject(final char symbol) {
        this.symbol = symbol;
    }

    /**
     * This method is used to add two integers. This is
     * a the simplest form of a class method, just to
     * show the usage of various javadoc Tags.
     * @param c is the symbol of the GameObject to be returned
     * @return GameObject returns a game object according to the input character, return WALL in case of invalid symbol
     */
    public static GameObject fromChar(char c) {
        for (GameObject t : GameObject.values()) {
            if (Character.toUpperCase(c) == t.symbol) {    //constant value not in capital letter, rule 5 violated
                return t;
            }
        }
        return WALL;
    }
    //accessor methods not declared at the top of the enumeration, rule 4 violated
    /**
     * This method is used to get the symbol of a GameObject object
     * @return GameObject returns a string object version of the symbol
     */
    public String getStringSymbol() {
        return String.valueOf(symbol);
    }

    /**
     * This method is used to get the symbol of a GameObject object
     * @return GameObject returns the symbol of a GameObject object
     */
    public char getCharSymbol() {
        return symbol;
    }
}