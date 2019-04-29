package org.ringingmaster.util.javafx.grid.canvas;

import static com.google.common.base.Preconditions.checkElementIndex;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class GridDimension {

    private final boolean zeroSized;

    private final double[] vertLinePositions;
    private final double[] horzLinePositions;

    private final double[] rowHeights;
    private final double[] columnWidths;
    private final double[] bottomGaps;

    private final CellDimension[][] cellDimensions;


    GridDimension(boolean zeroSized,
                  double[] vertLinePositions, double[] horzLinePositions,
                  double[] rowHeights, double[] columnWidths,
                  double[] bottomGaps,
                  CellDimension[][] cellDimensions) {
        this.zeroSized = zeroSized;
        this.vertLinePositions = vertLinePositions;
        this.horzLinePositions = horzLinePositions;
        this.rowHeights = rowHeights;
        this.columnWidths = columnWidths;
        this.bottomGaps = bottomGaps;
        this.cellDimensions = cellDimensions;
    }

    public double getRowHeaderLeft() {
        return vertLinePositions[0];
    }

    public double getTableLeft(boolean hasRowHeader) {
        return vertLinePositions[hasRowHeader?1:0];
    }

    public double getTableRight() {
        return vertLinePositions[vertLinePositions.length - 1];
    }

    public double getTableTop() {
        return horzLinePositions[0];
    }

    public double getTableBottom() {
        return horzLinePositions[horzLinePositions.length - 1];
    }

    public double getTableVerticalLinePosition(int vertLineIndex) {
        final int offsetVertLineIndex = vertLineIndex;
        checkElementIndex(offsetVertLineIndex, vertLinePositions.length);
        return vertLinePositions[offsetVertLineIndex];
    }

    public double getTableHorizontalLinePosition(int horzLineIndex) {
        checkElementIndex(horzLineIndex, horzLinePositions.length);
        return horzLinePositions[horzLineIndex];
    }


    public CellDimension getCell(int rowIndex, int colIndex) {
        int offsetColIndex = colIndex;
        checkElementIndex(offsetColIndex, cellDimensions.length);
        final CellDimension[] column = cellDimensions[offsetColIndex];
        checkElementIndex(rowIndex, column.length);
        return column[rowIndex];
    }

    public double getTextBottom(int rowIndex) {
        checkElementIndex(rowIndex + 1, horzLinePositions.length);
        checkElementIndex(rowIndex, bottomGaps.length);
        // The +1 is because the horizontal Line positions have a top line that we are not interested in.
        return getTableHorizontalLinePosition(rowIndex + 1) - bottomGaps[rowIndex];
    }

    public double getRowHeight(final int rowIndex) {
        checkElementIndex(rowIndex, rowHeights.length);
        return rowHeights[rowIndex];
    }

    public boolean isZeroSized() {
        return zeroSized;
    }

    public int getRowCount() {
        return rowHeights.length;
    }

    public int getColumnCount() {
        return columnWidths.length;
    }
}
