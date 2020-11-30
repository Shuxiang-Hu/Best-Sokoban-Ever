package sample;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;

class GraphicObject extends Rectangle {
    private static Image m_floor = new Image(new File(System.getProperty("user.dir") + "/resource/GameImages/BlackWall.png").toURI().toString());
    private static Image m_background = new Image(new File(System.getProperty("user.dir") + "/resource/GameImages/BlackWall.png").toURI().toString());
    private static final Image M_KEEPER = new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/keeper.png").toURI().toString());
    private static final Image M_DIAMOND = new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/diamond.png").toURI().toString());
    private static final Image M_CRATE_ON_DIAMOND = new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/CrateOnDiamond.png").toURI().toString());
    private static final Image M_CRATE = new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/Crate.png").toURI().toString());
    private static final Image M_PORTAL = new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/Portal.png").toURI().toString());
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
        File file;
        //specify the appearance of the GraphicObject object
        //add fading animation for DIAMOND
        switch (obj) {
            case WALL:
                APPEARANCE = m_background;
                break;

            case CRATE:
                APPEARANCE = M_CRATE;
                break;

            case DIAMOND:
                APPEARANCE = M_DIAMOND;

                // TODO: fix memory leak.
                //add fading effect to current GraphicObject object
                if (GameModel.isDebugActive()) {
                    FadeTransition ft = new FadeTransition(Duration.millis(1000), this);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.2);
                    ft.setCycleCount(Timeline.INDEFINITE);
                    ft.setAutoReverse(true);
                    ft.play();
                }

                break;

            case KEEPER:
                APPEARANCE = M_KEEPER;
                break;

            case FLOOR:

            case PORTAL_EXIT:
                APPEARANCE = m_floor;
                break;

            case CRATE_ON_DIAMOND:
                APPEARANCE = M_CRATE_ON_DIAMOND;
                break;

            case PORTAL:
                APPEARANCE = M_PORTAL;
                break;
            default:
                String message = "Error in Level constructor. Object not recognized.";
                GameModel.m_gameLogger.severe(message);
                throw new AssertionError(message);
        }

        this.setHeight(30);
        this.setWidth(30);

        if (obj != GameObject.WALL) {
            this.setArcHeight(50);
            this.setArcWidth(50);
        }

        if (GameModel.isDebugActive()) {
            this.setStroke(Color.RED);
            this.setStrokeWidth(0.25);
        }
    }

}
