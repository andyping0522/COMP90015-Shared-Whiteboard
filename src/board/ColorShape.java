package board;

import java.awt.*;
import java.io.Serializable;

public class ColorShape implements Serializable {

    private Color color;
    private Shape shape;

    public ColorShape(Color color, Shape shape) {
        this.color = color;
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public Shape getShape() {
        return shape;
    }
}
