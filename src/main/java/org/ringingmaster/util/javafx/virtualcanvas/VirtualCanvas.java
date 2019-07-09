package org.ringingmaster.util.javafx.virtualcanvas;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
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

    private final ResizableCanvas resizableCanvas = new ResizableCanvas();

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
                .subscribe(pair -> resizableCanvas.setOrigin(pair.getLeft().doubleValue(), pair.getRight().doubleValue()));

        // Canvas Nodes are not automatically re-sizable by parent as they dont derive from Region
        // Instead, we use a BorderPane and lock the canvas size to it.
        BorderPane borderPane = new BorderPane();
        resizableCanvas.widthProperty().bind(borderPane.widthProperty());
        resizableCanvas.heightProperty().bind(borderPane.heightProperty());
        borderPane.setCenter(resizableCanvas);
        borderPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Configure Grid
        add(borderPane, 0, 0);
        add(hScroll, 0, 1);
        add(vScroll, 1, 0);
        setHgrow(borderPane, Priority.ALWAYS);
        setVgrow(borderPane, Priority.ALWAYS);

        // So the square between the two scroll basr is correct colour.
        setBackground(new Background(new BackgroundFill(Color.gray(.9), CornerRadii.EMPTY, Insets.EMPTY)));

        resizableCanvas.heightProperty().addListener((observable, oldValue, newValue) -> setScrollBarSizes());
        resizableCanvas.widthProperty().addListener((observable, oldValue, newValue) -> setScrollBarSizes());
    }


    public void setViewportDrawer(ViewportDrawer viewportDrawer) {
        resizableCanvas.setViewportDrawer(viewportDrawer);
    }

    public void setVirtualSize(double hVirtualSize, double vVirtualSize) {
        this.hVirtualSize = hVirtualSize;
        this.vVirtualSize = vVirtualSize;
        setScrollBarSizes();
    }

    public void render() {
        resizableCanvas.render();
    }

    private void setScrollBarSizes() {
        double canvasWidth = resizableCanvas.getWidth();
        double canvasHeight = resizableCanvas.getHeight();

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