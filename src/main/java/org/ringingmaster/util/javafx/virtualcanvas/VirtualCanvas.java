package org.ringingmaster.util.javafx.virtualcanvas;

import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * TODO comments???
 *
 * @author Steve Lake
 */
public class VirtualCanvas extends GridPane {

    private final Logger log = LoggerFactory.getLogger(VirtualCanvas.class);

    //    private final BlueLinePane blueLinePane = new BlueLinePane();
    Canvas canvas = new Canvas();

    public VirtualCanvas(ViewportDrawer viewportDrawer) {
        checkNotNull(viewportDrawer);

        ScrollBar vScroll = createScrollBar(Orientation.VERTICAL, 1000, 300);

        ScrollBar hScroll = createScrollBar(Orientation.HORIZONTAL, 1000, 300);

        hScroll.valueProperty().addListener((observable, oldValue, newValue) -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.RED);
            gc.fillRect(-100,-100,100,100);
//            viewportDrawer.draw(gc, getWidth(), getHeight(), newValue.doubleValue(), 300);
        });

//
        addColumn(0, canvas, hScroll);
        add(vScroll, 1, 0);
    }


    public ScrollBar createScrollBar(Orientation orientation, double fullSize, double canvasSize) {
        ScrollBar sb = new ScrollBar();
        sb.setOrientation(orientation);
        sb.setMin(0.0);
        sb.setMax(fullSize - canvasSize);
        sb.setUnitIncrement(1.0);
        sb.setVisibleAmount(canvasSize);
        return sb;
    }
}