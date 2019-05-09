package org.ringingmaster.util.javafx.grid.canvas;

import org.ringingmaster.util.javafx.grid.GridPosition;
import org.ringingmaster.util.javafx.grid.model.CellModel;

/**
 * TODO Comments
 *
 * @author Steve Lake
 */
public class CaretPositionMover {

    private final GridPane parent;

    // This allows the character position to be held when navigating up and down.
    private static final int NOT_SET = -1;
    private int stickyCharacterPosition = NOT_SET;

    public CaretPositionMover(GridPane parent) {
        this.parent = parent;
    }

    void moveRight() {
        if (parent.getModel().isZeroSized()) {
            return;
        }

        GridPosition caretPosition = parent.getModel().getCaretPosition();

        // If in selection, the just move to the right end of the selection
        if (parent.getModel().isSelection()) {
            GridPosition selectionStartPosition = parent.getModel().getSelectionStartPosition();
            parent.getModel().setCaretPosition((selectionStartPosition.compareTo(caretPosition) > 0) ? selectionStartPosition : caretPosition);
            return;
        }

        int row = caretPosition.getRow();
        int col = caretPosition.getColumn();
        int character = caretPosition.getCharacterIndex() + 1;
        CellModel cellModel = parent.getModel().getCellModel(row, col);
        if (character > cellModel.getLength()) {
            // roll to start of next cell
            col++;
            character = 0;
            if (col >= parent.getModel().getColumnSize()) {
                // roll to next row
                row++;
                col = (parent.getModel().hasRowHeader()?1:0);
            }
        }
        // if we have blown the row count, then there is nowhere else to go
        // (we are in as far right and down as we can go), ignore the move.
        if (row < parent.getModel().getRowSize()) {
            parent.getModel().setCaretPosition(new GridPosition(row, col, character));
            stickyCharacterPosition = character;
        }
    }

    protected void moveLeft() {
        if (parent.getModel().isZeroSized()) {
            return;
        }

        GridPosition caretPosition = parent.getModel().getCaretPosition();

        // If in selection, the just move to the left end of the selection
        if (parent.getModel().isSelection()) {
            GridPosition selectionStartPosition = parent.getModel().getSelectionStartPosition();
            parent.getModel().setCaretPosition((selectionStartPosition.compareTo(caretPosition) < 0) ? selectionStartPosition : caretPosition);
            return;
        }

        int row = caretPosition.getRow();
        int col = caretPosition.getColumn();
        int character = caretPosition.getCharacterIndex() - 1;
        if (character < 0) {
            // roll to end of previous cell
            col--;
            if (col < (parent.getModel().hasRowHeader()?1:0)) {
                // roll to previous row
                row--;
                col = parent.getModel().getColumnSize() - 1;
            }
            if (row >= 0) {
                character = parent.getModel().getCellModel(row, col).getLength();
            }
        }

        // if we have blown the row count, then there is nowhere else to go
        // (we are in as far right and down as we can go), ignore the move.
        if (row >= 0) {
            parent.getModel().setCaretPosition(new GridPosition(row, col, character));
            stickyCharacterPosition = character;
        }

    }

    protected void moveUp() {
        if (parent.getModel().isZeroSized()) {
            return;
        }

        GridPosition pos = parent.getModel().getCaretPosition();
        int row = pos.getRow() - 1;
        int col = pos.getColumn();
        int character = pos.getCharacterIndex();
        if (row >= 0) {
            int cellLength = parent.getModel().getCellModel(row, col).getLength();
            if (stickyCharacterPosition != NOT_SET) {
                character = stickyCharacterPosition;
            }
            character = Math.min(cellLength, character);
            parent.getModel().setCaretPosition(new GridPosition(row, col, character));
        }
    }

    void moveDown() {
        if (parent.getModel().isZeroSized()) {
            return;
        }

        GridPosition pos = parent.getModel().getCaretPosition();
        int col = pos.getColumn();
        int row = pos.getRow() + 1;
        int character = pos.getCharacterIndex();
        if (row < parent.getModel().getRowSize()) {
            int cellLength = parent.getModel().getCellModel(row, col).getLength();
            if (stickyCharacterPosition != NOT_SET) {
                character = stickyCharacterPosition;
            }
            character = Math.min(cellLength, character);
            parent.getModel().setCaretPosition(new GridPosition(row, col, character));
        }
    }

    void moveToStartOfLastCellIfItHasContentsElseLastButOne() {
        if (parent.getModel().isZeroSized()) {
            return;
        }

        GridPosition pos = parent.getModel().getCaretPosition();
        int col = parent.getModel().getColumnSize() - 1;
        int row = pos.getRow();
        int character = 0;
        if (parent.getModel().getCellModel(row, col).getLength() == 0 &&
                col - 1 >= 0) {
            col--;
        }
        parent.getModel().setCaretPosition(new GridPosition(row, col,  character));
        stickyCharacterPosition = 0;
    }

    void moveToStartOfRow() {
        if (parent.getModel().isZeroSized()) {
            return;
        }

        GridPosition pos = parent.getModel().getCaretPosition();
        int col = (parent.getModel().hasRowHeader()?1:0);
        int row = pos.getRow();
        int character = 0;
        parent.getModel().setCaretPosition(new GridPosition(row, col,  character));
        stickyCharacterPosition = 0;

    }

    void deleteBack() {
        if (parent.getModel().getCaretPosition().getCharacterIndex() > 0) {
            GridPosition caretPosition = parent.getModel().getCaretPosition();
            moveLeft();
            parent.getModel().getCellModel(caretPosition.getRow(), caretPosition.getColumn()).removeCharacter(caretPosition.getCharacterIndex() - 1);
        }
    }

    void deleteForward() {
        GridPosition caretPosition = parent.getModel().getCaretPosition();
        CellModel cellModel = parent.getModel().getCellModel(caretPosition.getRow(), caretPosition.getColumn());
        if (caretPosition.getCharacterIndex() < cellModel.getLength()) {
            cellModel.removeCharacter(caretPosition.getCharacterIndex());
        }
    }
}
