package org.ringingmaster.util.javafx.virtualcanvas;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comments???
 *
 * @author Steve Lake
 */
public class VirtualCanvas extends GridPane {

    private final Logger log = LoggerFactory.getLogger(VirtualCanvas.class);

    // Canvas Nodes are not automatically re-sizable by parent as they dont derive from Region
    // Instead, we use a StackPane and lock the canvas size to it.
    private final StackPane canvasHolder = new StackPane();

    private final ScrollBar hScroll;
    private final ScrollBar vScroll;

    private double hVirtualSize;
    private double vVirtualSize;


    public VirtualCanvas() {

        hScroll = createScrollBar(Orientation.HORIZONTAL);
        vScroll = createScrollBar(Orientation.VERTICAL);

        Observable<Number> hScrollValueObservable = JavaFxObservable.valuesOf(hScroll.valueProperty());
        Observable<Number> vScrollValueObservable = JavaFxObservable.valuesOf(vScroll.valueProperty());
        Observable.combineLatest(hScrollValueObservable, vScrollValueObservable, Pair::of)
                .subscribe(pair -> setOrigin(pair.getLeft().doubleValue(), pair.getRight().doubleValue()));

        canvasHolder.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Configure Grid
        add(canvasHolder, 0, 0);
        add(hScroll, 0, 1);
        add(vScroll, 1, 0);
        setHgrow(canvasHolder, Priority.ALWAYS);
        setVgrow(canvasHolder, Priority.ALWAYS);

        // So the square between the two scroll bar is correct colour.
        setBackground(new Background(new BackgroundFill(Color.gray(.9), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void setOrigin(double hOrigin, double vOrigin) {
        for (Node child : canvasHolder.getChildren()) {

            if (child instanceof ResizableCanvas)
                ((ResizableCanvas)child).setOrigin(hOrigin, vOrigin);
        }
    }


    public void setViewportDrawers(ViewportDrawer... viewportDrawers) {

        canvasHolder.getChildren().clear();

        for (ViewportDrawer drawer : viewportDrawers) {

            ResizableCanvas resizableCanvas = new ResizableCanvas();

            resizableCanvas.setViewportDrawer(drawer);

            resizableCanvas.widthProperty().bind(canvasHolder.widthProperty());
            resizableCanvas.heightProperty().bind(canvasHolder.heightProperty());

            resizableCanvas.heightProperty().addListener((observable, oldValue, newValue) -> setScrollBarSizes());
            resizableCanvas.widthProperty().addListener((observable, oldValue, newValue) -> setScrollBarSizes());

            canvasHolder.getChildren().add(resizableCanvas);
        }
    }

    public void setVirtualSize(double hVirtualSize, double vVirtualSize) {
        this.hVirtualSize = hVirtualSize;
        this.vVirtualSize = vVirtualSize;
        setScrollBarSizes();
    }

    public void triggerVirtualCanvasRender() {
        for (Node child : canvasHolder.getChildren()) {

            if (child instanceof ResizableCanvas)
                ((ResizableCanvas)child).render();
        }
    }

    private void setScrollBarSizes() {
        double canvasWidth = canvasHolder.getWidth();
        double canvasHeight = canvasHolder.getHeight();

        double hScrollMax = hVirtualSize - canvasWidth;
        double vScrollMax = vVirtualSize - canvasHeight;

        double hVisibleAmount = hScrollMax * canvasWidth / hVirtualSize;
        double vVisibleAmount = vScrollMax * canvasHeight / vVirtualSize;

        double hUnitIncrement = hScrollMax / 100; //TODO would be good if this was a "row"
        double vUnitIncrement = vScrollMax / 100; //TODO would be good if this was a "row"

        hScroll.setMax(hScrollMax);
        hScroll.setVisibleAmount(hVisibleAmount);
        hScroll.setUnitIncrement(hUnitIncrement);

        vScroll.setMax(vScrollMax);
        vScroll.setVisibleAmount(vVisibleAmount);
        vScroll.setUnitIncrement(vUnitIncrement);
    }


    public ScrollBar createScrollBar(Orientation orientation) {
        ScrollBar sb = new ScrollBar();
        sb.setOrientation(orientation);
        sb.setMin(0.0);
        sb.setMax(0.0);
        sb.setUnitIncrement(1.0);
        sb.setVisibleAmount(10.0);
        return sb;
    }
}