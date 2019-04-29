package org.ringingmaster.util.javafx.grid.model;


import javafx.scene.paint.Color;
import org.ringingmaster.util.javafx.grid.GridPosition;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public interface GridModel {

    boolean hasRowHeader();

    /**
     * Get the number of rows (not lines)
     *
     * @return int number of rows.
     */
    int getRowSize();

    /**
     * Get the number of columns (not lines)
     *
     * @return int number of columns.
     */
    int getColumnSize();


    boolean isZeroSized();

    /**
     * Get the color of the lines that form the grid.
     *
     * @return Color
     */
    Color getGridColor();

    CellModel getCellModel(int row, int column);

    CharacterModel getCharacterModel(GridPosition gridPosition);

    GridPosition getCaretPosition();

    void setCaretPosition(GridPosition newPosition);

    GridPosition getSelectionStartPosition();

    void setSelectionEndPosition(GridPosition gridPosition);

    boolean isSelection();


    void registerListener(GridModelListener listener);

    void deRegisterListener(GridModelListener listener);

}
