package org.ringingmaster.util.javafx.grid.canvas;

import javafx.scene.layout.Pane;
import org.ringingmaster.util.javafx.grid.model.GridModel;
import org.ringingmaster.util.javafx.grid.model.GridModelListener;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * TODO comments ???
 *
 * @author Steve Lake
 */
public class GridPane extends Pane implements GridModelListener {

    private final SelectionLayer selectionLayer = new SelectionLayer();
    private final MainDrawingLayer mainDrawingLayer = new MainDrawingLayer();
    private final InteractionLayer interactionLayer = new InteractionLayer();
    private GridDimensions dimensions;
    private GridModel model;
    private final String name;


    public GridPane(String name) {
        // Order is important here to get the drawing in the correct Z Order.
        getChildren().add(selectionLayer);
        getChildren().add(mainDrawingLayer);
        getChildren().add(interactionLayer);
        this.name = name;
    }


    public void setModel(GridModel model) {
        checkState(this.model == null); //At the moment, only a 1 shot
        this.model = checkNotNull(model);
        mainDrawingLayer.setModel(model);
        interactionLayer.setModel(model);
        selectionLayer.setModel(model);
        this.model.registerListener(this);
        gridModelListener_cellContentsChanged();
    }

    @Override
    public void gridModelListener_cellContentsChanged() {
        dimensions = new GridDimensionBuilder().setModel(model).build(); //TODO make stateles and reactive

        setMinWidth(dimensions.getTableRight());
        setMinHeight(dimensions.getTableBottom());

        mainDrawingLayer.setDimensions(dimensions);
        interactionLayer.setDimensions(dimensions);
        selectionLayer.setDimensions(dimensions);

        selectionLayer.draw();
        mainDrawingLayer.draw();
        interactionLayer.draw();
    }

    @Override
    public void gridModelListener_caretMoved() {
        interactionLayer.forceCaretBlinkOnIfVisible();
        interactionLayer.draw();
    }

    @Override
    public void gridModelListener_selectionChanged() {
        selectionLayer.draw();
    }

    public GridDimensions getDimensions() {
        return dimensions;
    }

    GridModel getModel() {
        return model;
    }

    public String getName() {
        return name;
    }
}
