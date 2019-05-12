package org.ringingmaster.util.javafx.grid.model;

import org.ringingmaster.util.javafx.grid.GridPosition;
import org.ringingmaster.util.javafx.grid.canvas.CaretPositionMover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkPositionIndex;

/**
 * TODO comments ???
 *
 * @author Steve Lake
 */
public abstract class SkeletalGridModel implements GridModel {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final List<GridModelListener> listeners = new ArrayList<>();

    private GridPosition caretPosition = new GridPosition(0, 0, 0);
    private GridPosition selectionStartPosition = caretPosition;
    private CaretPositionMover caretPositionMover = new CaretPositionMover();

    public SkeletalGridModel() {
        caretPositionMover.setModel(this);
    }

    @Override
    public void registerListener(final GridModelListener listener) {
        listeners.add(listener);
    }

    protected void fireCellContentsChanged() {
        // The changing of the cell structure means the caret may be outside the grid.
        if (caretPosition.getRow() >= getRowSize()) {
            caretPositionMover.moveUp();
        }
        if (caretPosition.getColumn() >= getColumnSize()) {
            caretPositionMover.moveLeft();
        }


        for (GridModelListener listener : listeners) {
            listener.gridModelListener_cellContentsChanged();
        }
    }


    @Override
    public GridPosition getCaretPosition() {
        return caretPosition;
    }

    @Override
    public GridPosition getSelectionStartPosition() {
        return selectionStartPosition;
    }

    @Override
    public void setCaretPosition(GridPosition newPosition) {
        boolean wasInSelection = isSelection();
        doSetCaretPosition(newPosition);
        this.selectionStartPosition = newPosition;
        if (wasInSelection) {
            fireSelectionChanged();
        }
        //log.info("Caret [" + caretPosition+ "], Selection Start [" + selectionStartPosition + "]");
    }

    @Override
    public boolean isSelection() {
        return !caretPosition.equals(selectionStartPosition);
    }


    private void fireCaretPositionMoved() {
        for (GridModelListener listener : listeners) {
            listener.gridModelListener_caretMoved();
        }
    }

    @Override
    public void setSelectionEndPosition(GridPosition selectionEndPosition) {
        checkNotNull(selectionEndPosition);
        doSetCaretPosition(selectionEndPosition);
        fireSelectionChanged();
        //log.info("Caret [" + caretPosition + "], Selection Start [" + selectionStartPosition + "]");
    }

    private void fireSelectionChanged() {
        for (GridModelListener listener : listeners) {
            listener.gridModelListener_selectionChanged();
        }
    }

    private void doSetCaretPosition(GridPosition newPosition) {
        checkValidGridPosition(newPosition);
        this.caretPosition = newPosition;
        fireCaretPositionMoved();
    }

    private void checkValidGridPosition(GridPosition position) {
        checkPositionIndex(position.getRow(), getRowSize(), "row");
        checkPositionIndex(position.getColumn(), getColumnSize(), "column");
        CellModel cellModel = getCellModel( position.getRow(), position.getColumn());
        checkPositionIndex(position.getCharacterIndex(), cellModel.getLength() + 1, "cell");
    }

    @Override
    public boolean isZeroSized() {
        return (getColumnSize() == 0 || getRowSize() == 0);
    }

    @Override
    public boolean hasRowHeader() {
        return false;
    }

    @Override
    public CharacterModel getCharacterModel(GridPosition gridPosition) {
        checkNotNull(gridPosition);
        CellModel cellModel = getCellModel(gridPosition.getColumn(), gridPosition.getRow());
        if (cellModel != null) {
            return cellModel.getCharacterModel(gridPosition.getCharacterIndex());
        }
        return null;
    }

}
