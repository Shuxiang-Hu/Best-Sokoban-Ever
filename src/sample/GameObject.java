package sample;

public enum GameObject {
    WALL('W'),
    FLOOR(' '),
    CRATE('C'),
    DIAMOND('D'),
    KEEPER('S'),
    CRATE_ON_DIAMOND('O'),
    PORTAL('P'),
    PORTAL_EXIT('E'),
    DEBUG_OBJECT('=');

    private final char SYMBOL;

    GameObject(final char SYMBOL) {
        char charToAssign = 'W';
        char[] validChars = {'W',' ','C','D','S','O','P','E','='};
        for(char c : validChars ){
            if(SYMBOL == c){
                charToAssign = c;
                break;
            }
        }
        this.SYMBOL=charToAssign;
    }

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

    /**
     * This method is used to add two integers. This is
     * a the simplest form of a class method, just to
     * show the usage of various javadoc Tags.
     * @param c is the symbol of the GameObject to be returned
     * @return GameObject returns a game object according to the input character, return WALL in case of invalid symbol
     */
    public static GameObject fromChar(char c) {
        for (GameObject t : GameObject.values()) {
            if (Character.toUpperCase(c) == t.SYMBOL) {
                return t;
            }
        }

        return WALL;
    }


}