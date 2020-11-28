package sample;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;

class GraphicObject extends Rectangle {
    private static Image floor = new Image(new File(System.getProperty("user.dir") + "/resource/GameImages/BlackWall.png").toURI().toString());
    private static Image backGround = new Image(new File(System.getProperty("user.dir") + "/resource/GameImages/BlackWall.png").toURI().toString());
    private static final Image KEEPER = new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/keeper.png").toURI().toString());
    private static final Image DIAMOND = new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/diamond.png").toURI().toString());
    private static final Image CRATE_ON_DIAMOND = new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/CrateOnDiamond.png").toURI().toString());
    private static final Image CRATE = new Image(new File(System.getProperty("user.dir")+"/resource/GameImages/Crate.png").toURI().toString());
    private final Image APPEARANCE;


    public static void setBackground(String backgroundImagePath) {
        GraphicObject.backGround = new Image(new File(backgroundImagePath).toURI().toString());
    }

    public static void setFloor(String floorImagePath) {
        GraphicObject.floor = new Image(new File(floorImagePath).toURI().toString());
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
                APPEARANCE = backGround;
                break;

            case CRATE:
                APPEARANCE = CRATE;
                break;

            case DIAMOND:
                APPEARANCE = DIAMOND;

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
                APPEARANCE = KEEPER;
                break;

            case FLOOR:
                APPEARANCE = floor;
                break;

            case CRATE_ON_DIAMOND:
                APPEARANCE = CRATE_ON_DIAMOND;
                break;

            default:
                String message = "Error in Level constructor. Object not recognized.";
                GameModel.logger.severe(message);
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
