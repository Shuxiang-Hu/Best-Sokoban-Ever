package object;


import viewer.GameModel;
import model.GameLogger;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.io.File;
/**
 * Represents a graphic game object, each subclass
 * of GameObject has its own display image
 * @author Shuxiang Hu
 */
public class GraphicObject extends Rectangle {
    /**
     * Image for a floor object, default color is white
     */
    private static Image m_floor =
            new Image(new File(System.getProperty("user.dir") + "/resource/GameImages/WhiteFloor.png").toURI().toString());

    /**
     * Image for a wall object, default color is black
     */
    private static Image m_background =
            new Image(new File(System.getProperty("user.dir") + "/resource/GameImages/BlackWall.png").toURI().toString());

    /**
     * Image for a keeper object, default direction is down
     */
    private static  Image m_keeper =
            new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/downKeeper.png").toURI().toString());

    /**
     * Image for a diamond object, a red circle
     */
    private static final Image M_DIAMOND =
            new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/diamond.png").toURI().toString());

    /**
     * Image for a crate on diamond object, a yellow crate
     */
    private static final Image M_CRATE_ON_DIAMOND = new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/CrateOnDiamond.png").toURI().toString());

    /**
     * Image for a crate, a red crate
     */
    private static final Image M_CRATE =
            new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/Crate.png").toURI().toString());

    /**
     * Image for a portal
     */
    private static final Image M_PORTAL =
            new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/Portal.png").toURI().toString());

    /**
     * Image of a GraphicObject instance
     */
    private final Image APPEARANCE;

    /**
     * Constructs a GraphicObject object by specifying is appearance
     * @param obj specifies the type of the GraphicObject object
     */
    public GraphicObject(GameObject obj) {
        //specify the appearance of the GraphicObject object
        //add fading animation for DIAMOND
        char symbol = obj.getCharSymbol();
        switch (symbol) {
            case 'W':
                APPEARANCE = m_background;
                if(GameModel.isDebugActive()){
                    System.out.println("Wall");
                }
                break;

            case 'C':
                APPEARANCE = M_CRATE;
                if(GameModel.isDebugActive()){
                    System.out.println("Crate");
                }
                break;

            case 'D':
                APPEARANCE = M_DIAMOND;
                if(GameModel.isDebugActive()){
                    System.out.println("Diamond");
                }
                break;

            case 'S':
                APPEARANCE = m_keeper;
                if(GameModel.isDebugActive()){
                    System.out.println("Keeper");
                }
                break;

            case ' ':

            case 'E':
                APPEARANCE = m_floor;
                if(GameModel.isDebugActive()){
                    System.out.println("Floor");
                }
                break;

            case 'O':
                APPEARANCE = M_CRATE_ON_DIAMOND;
                if(GameModel.isDebugActive()){
                    System.out.println("Crate on diamond");
                }
                break;

            case 'P':
                APPEARANCE = M_PORTAL;
                if(GameModel.isDebugActive()){
                    System.out.println("Portal");
                }
                break;

            default:
                String message = "Error in Level constructor. Object not recognized.";
                GameLogger.getUniqueInstance().severe(message);
                throw new AssertionError(message);
        }

    }
    /**
     * Sets the background image to a new one
     * @param backgroundImagePath path to the new image file
     */
    public static void setM_background(String backgroundImagePath) {
        GraphicObject.m_background = new Image(new File(backgroundImagePath).toURI().toString());
    }
    /**
     * Sets the floor image to a new one
     * @param floorImagePath path to the new image file
     */
    public static void setM_floor(String floorImagePath) {
        GraphicObject.m_floor = new Image(new File(floorImagePath).toURI().toString());
    }

    /**
     * Gets the appearance image of a instance
     * @return  image appearance of a GraphicObject instance
     */
    public Image getAppearance() {
        return APPEARANCE;
    }


    /**
     * Sets the direction that the keeper faces
     * @param direction a string specifying the new direction
     */
    public static void setKeeperPosition(String direction){
        m_keeper =
                new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/"+direction+"Keeper.png").toURI().toString());
    }

}
