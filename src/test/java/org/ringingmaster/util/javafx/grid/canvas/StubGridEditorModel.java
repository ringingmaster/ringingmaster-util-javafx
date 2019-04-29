package org.ringingmaster.util.javafx.grid.canvas;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.ringingmaster.util.javafx.grid.GridPosition;
import org.ringingmaster.util.javafx.grid.model.AdditionalStyleType;
import org.ringingmaster.util.javafx.grid.model.GridCellModel;
import org.ringingmaster.util.javafx.grid.model.GridCharacterGroup;
import org.ringingmaster.util.javafx.grid.model.GridCharacterModel;
import org.ringingmaster.util.javafx.grid.model.GridModel;
import org.ringingmaster.util.javafx.grid.model.GridModelListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * TODO comments???
 * User: Stephen
 */
public class StubGridEditorModel implements GridModel {

    GridPosition caretPosition = new GridPosition(0, 0, 0);

    //model= [row],[col]
    final String[][] model;
    final String[] rowHeaders;

    public StubGridEditorModel(String[][] model, GridPosition caretPosition) {
        this(model, null, caretPosition);
    }

    public StubGridEditorModel(String[][] model, String[] rowHeaders, GridPosition caretPosition) {
        if (rowHeaders == null) {
            rowHeaders = new String[model.length];
            Arrays.fill(rowHeaders, "");
        }
        this.rowHeaders = rowHeaders;
        this.model = model;
        this.caretPosition = caretPosition;
    }

    @Override
    public void registerListener(GridModelListener listener) {

    }

    @Override
    public void deRegisterListener(GridModelListener listener) {

    }

    @Override
    public int getColumnSize() {
        int colCount = 0;
        for (String[] row : model) {
            if (row.length > colCount) {
                colCount = row.length;
            }
        }
        return colCount;
    }

    @Override
    public int getRowSize() {
        return model.length;
    }

    @Override
    public Color getGridColor() {
        return null;
    }

    @Override
    public GridCellModel getCellModel(int row, int column) {
        return new StubGridCellModel(model[row][column]);
    }

    @Override
    public GridCharacterModel getCharacterModel(GridPosition gridPosition) {
        return null;
    }

    @Override
    public GridCharacterGroup getRowHeader(int row) {
        return new StubGridCellModel(rowHeaders[row]);
    }

    @Override
    public GridPosition getCaretPosition() {
        return caretPosition;
    }

    @Override
    public GridPosition getSelectionStartPosition() {
        return null;
    }

    @Override
    public void setCaretPosition(GridPosition newPosition) {
        this.caretPosition = newPosition;
    }

    @Override
    public void setSelectionEndPosition(GridPosition gridPosition) {

    }

    @Override
    public boolean isSelection() {
        return false;
    }

    @Override
    public boolean isZeroSized() {
        return (getColumnSize() == 0 || getRowSize() == 0);
    }

    class StubGridCellModel implements GridCellModel {

        private final String characters;

        StubGridCellModel(String characters) {
            this.characters = characters;
        }

        @Override
        public int getLength() {
            return characters.length();
        }

        @Override
        public void insertCharacter(int index, String character) {
        }

        @Override
        public void removeCharacter(int index) {
        }

        @Override
        public GridCharacterModel getGridCharacterModel(int index) {
            return new GridCharacterModel() {
                @Override
                public char getCharacter() {
                    return characters.charAt(index);
                }

                @Override
                public Font getFont() {
                    return new Font("Arial", 24);
                }

                @Override
                public Color getColor() {
                    return Color.AQUA;
                }

                @Override
                public Set<AdditionalStyleType> getAdditionalStyle() {
                    return Collections.emptySet();
                }

                @Override
                public Optional<String> getTooltipText() {
                    return Optional.empty();
                }
            };
        }

        @Override
        public Iterator<GridCharacterModel> iterator() {
            return new Iterator<GridCharacterModel>() {
                int index = 0;

                @Override
                public boolean hasNext() {
                    return index < getLength();
                }

                @Override
                public GridCharacterModel next() {
                    return getGridCharacterModel(index++);
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public String toString() {
            return "[" + characters + "]";
        }
    }

}
