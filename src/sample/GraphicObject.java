package sample;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class GraphicObject extends Rectangle {
    private static Paint backgroundColor = Color.BLACK;
    private static Paint diamondColor = Color.DEEPSKYBLUE;

    public static Paint getDiamondColor() {
        return diamondColor;
    }

    public static void setDiamondColor(Paint diamondColor) {
        GraphicObject.diamondColor = diamondColor;
    }

    /**
     * Constructs a GraphicObject object with color, size and style
     * @param obj specifies the type of the GraphicObject object
     */
    GraphicObject(GameObject obj) {
        Paint color;
        //specify the color of the GraphicObject object
        //add fading animation for DIAMOND
        switch (obj) {
            case WALL:
                color = backgroundColor;
                break;

            case CRATE:
                color = Color.ORANGE;
                break;

            case DIAMOND:
                color = diamondColor;

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
                color = Color.RED;
                break;

            case FLOOR:
                color = Color.WHITE;
                break;

            case CRATE_ON_DIAMOND:
                color = Color.DARKCYAN;
                break;

            default:
                String message = "Error in Level constructor. Object not recognized.";
                GameModel.logger.severe(message);
                throw new AssertionError(message);
        }

        this.setFill(color);
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
    public static Paint getBackgroundColor() {
        return backgroundColor;
    }

    public static void setBackgroundColor(Paint newBackgroundColor) {
        GraphicObject.backgroundColor = newBackgroundColor;
    }
}
