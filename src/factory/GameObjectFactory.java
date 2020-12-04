package factory;

import object.*;

/**
 * The only thing this factory class does is to offer a static method to construct
 * a GameObject subclass instance given a symbol char
 */
public class GameObjectFactory {
    /**
     * Gets a GameObject subclass instance according to the input char
     * @param c is the symbol of the GameObject to be returned
     * @return GameObject returns a game object according to the input character, return WALL in case of invalid symbol
     */
    public static GameObject fromChar(char c) {
        //default is wall
        GameObject gameObject = new GameWall();
        //capitalize lower case chars
        if(c>='a'&&c<='z'){
            c -=32;
        }
        switch (c){
            case 'W':
                gameObject = new GameWall();
                break;

            case ' ':
                gameObject = new GameFloor();
                break;

            case 'S':
                gameObject = new GameKeeper();
                break;

            case 'C':
                gameObject =  new GameCrate();
                break;

            case 'O':
                gameObject =  new GameCrateOnDiamond();
                break;

            case 'D':
                gameObject =  new GameDiamond();
                break;

            case 'P':
                gameObject =  new GamePortal();
                break;

            case 'E':
                gameObject =  new GamePortalExit();
                break;

            case '=':
                gameObject =  new GameDebugObject();
                break;

            default:
                break;
        }
        return gameObject;
    }

}
