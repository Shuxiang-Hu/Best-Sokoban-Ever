package factory;

import object.*;

public class GameObjectFactory {
    /**
     * This method is used to add two integers. This is
     * a the simplest form of a class method, just to
     * show the usage of various javadoc Tags.
     * @param c is the symbol of the GameObject to be returned
     * @return GameObject returns a game object according to the input character, return WALL in case of invalid symbol
     */
    public static GameObject fromChar(char c) {
        GameObject gameObject = new GameWall();
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
