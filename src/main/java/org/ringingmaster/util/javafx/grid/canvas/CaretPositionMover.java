package org.ringingmaster.util.javafx.grid.canvas;

import org.ringingmaster.util.javafx.grid.GridPosition;
import org.ringingmaster.util.javafx.grid.model.CellModel;
import org.ringingmaster.util.javafx.grid.model.GridModel;

/**
 * TODO Comments
 *
 * @author Steve Lake
 */
public class CaretPositionMover {

    private GridModel model;

    // This allows the character position to be held when navigating up and down.
    private static final int NOT_SET = -1;
    private int stickyCharacterPosition = NOT_SET;

    public void setModel(GridModel model) {
        this.model = model;
    }

    public void moveRight() {
        if (model.isZeroSized()) {
            return;
        }

        GridPosition caretPosition = model.getCaretPosition();

        // If in selection, the just move to the right end of the selection
        if (model.isSelection()) {
            GridPosition selectionStartPosition = model.getSelectionStartPosition();
            model.setCaretPosition((selectionStartPosition.compareTo(caretPosition) > 0) ? selectionStartPosition : caretPosition);
            return;
        }

        int row = caretPosition.getRow();
        int col = caretPosition.getColumn();
        int character = caretPosition.getCharacterIndex() + 1;
        CellModel cellModel = model.getCellModel(row, col);
        if (character > cellModel.getLength()) {
            // roll to start of next cell
            col++;
            character = 0;
            if (col >= model.getColumnSize()) {
                // roll to next row
                row++;
                col = (model.hasRowHeader()?1:0);
            }
        }
        // if we have blown the row count, then there is nowhere else to go
        // (we are in as far right and down as we can go), ignore the move.
        if (row < model.getRowSize()) {
            model.setCaretPosition(new GridPosition(row, col, character));
            stickyCharacterPosition = character;
        }
    }

    public  void moveLeft() {
        if (model.isZeroSized()) {
            return;
        }

        GridPosition caretPosition = model.getCaretPosition();

        // If in selection, the just move to the left end of the selection
        if (model.isSelection()) {
            GridPosition selectionStartPosition = model.getSelectionStartPosition();
            model.setCaretPosition((selectionStartPosition.compareTo(caretPosition) < 0) ? selectionStartPosition : caretPosition);
            return;
        }

        int row = caretPosition.getRow();
        int col = caretPosition.getColumn();
        int character = caretPosition.getCharacterIndex() - 1;
        if (character < 0) {
            // roll to end of previous cell
            col--;
            if (col < (model.hasRowHeader()?1:0)) {
                // roll to previous row
                row--;
                col = model.getColumnSize() - 1;
            }
            if (row >= 0) {
                character = model.getCellModel(row, col).getLength();
            }
        }

        // if we have blown the row count, then there is nowhere else to go
        // (we are in as far right and down as we can go), ignore the move.
        if (row >= 0) {
            model.setCaretPosition(new GridPosition(row, col, character));
            stickyCharacterPosition = character;
        }

    }

    public  void moveUp() {
        if (model.isZeroSized()) {
            return;
        }

        GridPosition pos = model.getCaretPosition();
        int row = pos.getRow() - 1;
        int col = pos.getColumn();
        int character = pos.getCharacterIndex();
        if (row >= 0) {
            int cellLength = model.getCellModel(row, col).getLength();
            if (stickyCharacterPosition != NOT_SET) {
                character = stickyCharacterPosition;
            }
            character = Math.min(cellLength, character);
            model.setCaretPosition(new GridPosition(row, col, character));
        }
    }

    public void moveDown() {
        if (model.isZeroSized()) {
            return;
        }

        GridPosition pos = model.getCaretPosition();
        int col = pos.getColumn();
        int row = pos.getRow() + 1;
        int character = pos.getCharacterIndex();
        if (row < model.getRowSize()) {
            int cellLength = model.getCellModel(row, col).getLength();
            if (stickyCharacterPosition != NOT_SET) {
                character = stickyCharacterPosition;
            }
            character = Math.min(cellLength, character);
            model.setCaretPosition(new GridPosition(row, col, character));
        }
    }

    public void moveToStartOfLastCellIfItHasContentsElseLastButOne() {
        if (model.isZeroSized()) {
            return;
        }

        GridPosition pos = model.getCaretPosition();
        int col = model.getColumnSize() - 1;
        int row = pos.getRow();
        int character = 0;
        if (model.getCellModel(row, col).getLength() == 0 &&
                col - 1 >= 0) {
            col--;
        }
        model.setCaretPosition(new GridPosition(row, col,  character));
        stickyCharacterPosition = 0;
    }

    public void moveToStartOfRow() {
        if (model.isZeroSized()) {
            return;
        }

        GridPosition pos = model.getCaretPosition();
        int col = (model.hasRowHeader()?1:0);
        int row = pos.getRow();
        int character = 0;
        model.setCaretPosition(new GridPosition(row, col,  character));
        stickyCharacterPosition = 0;

    }

    public void deleteBack() {
        if (model.getCaretPosition().getCharacterIndex() > 0) {
            GridPosition caretPosition = model.getCaretPosition();
            moveLeft();
            model.getCellModel(caretPosition.getRow(), caretPosition.getColumn()).removeCharacter(caretPosition.getCharacterIndex() - 1);
        }
        else {
            moveLeft();
        }
    }

    public void deleteForward() {
        GridPosition caretPosition = model.getCaretPosition();
        CellModel cellModel = model.getCellModel(caretPosition.getRow(), caretPosition.getColumn());
        if (caretPosition.getCharacterIndex() < cellModel.getLength()) {
            cellModel.removeCharacter(caretPosition.getCharacterIndex());
        }
    }
}
