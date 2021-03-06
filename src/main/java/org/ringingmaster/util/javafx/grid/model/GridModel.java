package org.ringingmaster.util.javafx.grid.model;


import javafx.scene.paint.Color;
import org.ringingmaster.util.javafx.grid.GridPosition;

/**
 * TODO comments ???
 *
 * @author Steve Lake
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
    Color getGridRowColor(int row);
    Color getGridColColor(int col);

    /**
     * Get the thickness of the lines that form the grid.
     *
     * @return Color
     */
    double getGridRowLineWidth(int row);
    double getGridColLineWidth(int col);

    CellModel getCellModel(int row, int column);

    CharacterModel getCharacterModel(GridPosition gridPosition);

    GridPosition getCaretPosition();

    void setCaretPosition(GridPosition newPosition);

    GridPosition getSelectionStartPosition();

    void setSelectionEndPosition(GridPosition gridPosition);

    boolean isSelection();


    void registerListener(GridModelListener listener);
}
