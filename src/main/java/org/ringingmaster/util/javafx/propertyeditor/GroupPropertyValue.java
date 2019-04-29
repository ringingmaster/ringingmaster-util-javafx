package org.ringingmaster.util.javafx.propertyeditor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.ringingmaster.util.javafx.draw.Direction;
import org.ringingmaster.util.javafx.draw.ShapeDrawer;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class GroupPropertyValue extends SkeletalPropertyValue implements PropertyValue {

    public static final int TRIANGLE_SPACE = 20;
    private boolean groupIsVisible = true;

    GroupPropertyValue(String name) {
        super(name);
    }

    @Override
    public void positionEditor(double x, double y, double width, double height) {
        // Groups do not have an editor
    }

    @Override
    public void draw(GraphicsContext gc, double top, double bottom, double left, double right,
                     double center, double horzPadding, double vertPadding,
                     Color backgroundColor,
                     Color linesColor,
                     Color textColor, Color textColorDisabled) {
        gc.setFill(textColor);
        gc.fillText(getName(), TRIANGLE_SPACE, bottom - vertPadding, right - TRIANGLE_SPACE - (horzPadding * 2));

        gc.setFill(Color.GRAY); //TODO Set color from system colours
        ShapeDrawer.drawTriangle(gc, 10, top + 10, 5, groupIsVisible ? Direction.RIGHT : Direction.DOWN);
    }

    public void toggleGroupVisible() {
        groupIsVisible = !groupIsVisible;
    }

    public void setGroupVisible(boolean groupIsVisible) {
        this.groupIsVisible = groupIsVisible;
    }

    public boolean isGroupVisible() {
        return groupIsVisible;
    }
}
