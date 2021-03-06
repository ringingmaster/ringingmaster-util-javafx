package org.ringingmaster.util.javafx.propertyeditor;

import javafx.scene.text.Font;
import org.ringingmaster.util.javafx.font.FontMetrics;

/**
 * TODO comments ???
 *
 * @author Steve Lake
 */
public class PropertyGeometry {

    private double vertSeparatorPosition = 100.0;
    private final double vertResizeWidth = 3.0;
    private final double closestVertSeparatorCanBeToEdge = 30.0;

    private final Font font = Font.getDefault();
    private final double height;
    private final double descent;
    private final double padding = 3.0;


    private final double minUnderlyingControlWidth = 200.0;
    private final double minUnderlyingControlHeight = 200.0;

    public PropertyGeometry() {

        FontMetrics fm  = new FontMetrics(font);

		descent = fm.getDescent();
		height = (descent + fm.getAscent()) + (2 * padding);

    }

    public double getVertSeparatorPosition() {
        return vertSeparatorPosition;
    }

    public void setVertSeparatorPosition(double vertSeparatorPosition) {
        this.vertSeparatorPosition = vertSeparatorPosition;
    }

    public double getVertResizeWidth() {
        return vertResizeWidth;
    }

    public double getClosestVertSeparatorCanBeToEdge() {
        return closestVertSeparatorCanBeToEdge;
    }

    public double getHeight() {
        return height;
    }

    public double getDescent() {
        return descent;
    }

    public double getPadding() {
        return padding;
    }

    public Font getFont() {
        return font;
    }

    public double getMinUnderlyingControlHeight() {
        return minUnderlyingControlHeight;
    }

    public double getMinUnderlyingControlWidth() {
        return minUnderlyingControlWidth;
    }

    @Override
    public String toString() {
        return "PropertyGeometry{" +
                "vertSeparatorPosition=" + vertSeparatorPosition +
                ", vertResizeWidth=" + vertResizeWidth +
                ", closestVertSeparatorCanBeToEdge=" + closestVertSeparatorCanBeToEdge +
                ", font=" + font +
                ", height=" + height +
                ", descent=" + descent +
                ", padding=" + padding +
                ", minUnderlyingControlWidth=" + minUnderlyingControlWidth +
                ", minUnderlyingControlHeight=" + minUnderlyingControlHeight +
                '}';
    }
}
