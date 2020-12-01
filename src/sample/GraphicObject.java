package sample;


import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.io.File;

class GraphicObject extends Rectangle {
    private static Image m_floor =
            new Image(new File(System.getProperty("user.dir") + "/resource/GameImages/WhiteFloor.png").toURI().toString());
    private static Image m_background =
            new Image(new File(System.getProperty("user.dir") + "/resource/GameImages/BlackWall.png").toURI().toString());
    private static  Image m_keeper =
            new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/downKeeper.png").toURI().toString());
    private static final Image M_DIAMOND =
            new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/diamond.png").toURI().toString());
    private static final Image M_CRATE_ON_DIAMOND = new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/CrateOnDiamond.png").toURI().toString());
    private static final Image M_CRATE =
            new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/Crate.png").toURI().toString());
    private static final Image M_PORTAL =
            new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/Portal.png").toURI().toString());
    private final Image APPEARANCE;


    public static void setM_background(String backgroundImagePath) {
        GraphicObject.m_background = new Image(new File(backgroundImagePath).toURI().toString());
    }

    public static void setM_floor(String floorImagePath) {
        GraphicObject.m_floor = new Image(new File(floorImagePath).toURI().toString());
    }

    public Image getAppearance() {
        return APPEARANCE;
    }

    /**
     * Constructs a GraphicObject object with color, size and style
     * @param obj specifies the type of the GraphicObject object
     */
    GraphicObject(GameObject obj) {
        //specify the appearance of the GraphicObject object
        //add fading animation for DIAMOND
        switch (obj.getCharSymbol()) {
            case 'W':
                APPEARANCE = m_background;
                break;

            case 'C':
                APPEARANCE = M_CRATE;
                break;

            case 'D':
                APPEARANCE = M_DIAMOND;
                break;

            case 'S':
                APPEARANCE = m_keeper;
                break;

            case ' ':

            case 'E':
                APPEARANCE = m_floor;
                break;

            case 'O':
                APPEARANCE = M_CRATE_ON_DIAMOND;
                break;

            case 'P':
                APPEARANCE = M_PORTAL;
                break;

            default:
                String message = "Error in Level constructor. Object not recognized.";
                GameLogger.getUniqueInstance().severe(message);
                throw new AssertionError(message);
        }

    }

    public static void setKeeperPosition(String direction){
        m_keeper =
                new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/"+direction+"Keeper.png").toURI().toString());
    }

}
