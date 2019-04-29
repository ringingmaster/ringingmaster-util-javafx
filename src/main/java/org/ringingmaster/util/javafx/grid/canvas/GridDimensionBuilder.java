package org.ringingmaster.util.javafx.grid.canvas;

import com.google.common.annotations.VisibleForTesting;
import org.ringingmaster.util.javafx.grid.model.CellModel;
import org.ringingmaster.util.javafx.grid.model.GridModel;

/**
 * Contains all the calculations to turn a model, first into a list of width and height measurements
 * and from that into final absolute dimensions.
 *
 * @author Lake
 */
public class GridDimensionBuilder {

    public static final int MIN_ROW_HEIGHT = 15;
    public static final int MIN_COL_WIDTH = 15;
    private GridModel model;

    private static GridDimension ZERO_SIZED_GRID_DIMENSION = new GridDimension(true, null, null, null, null, null, null);

    GridDimensionBuilder setModel(GridModel model) {
        this.model = model;
        return this;
    }

    GridDimension build() {
        if (model.isZeroSized()) {
            return ZERO_SIZED_GRID_DIMENSION;
        } else {
            return calculateFromModel();
        }
    }

    private GridDimension calculateFromModel() {
        CellMeasurer measurer = new CellMeasurer();

        final int rowCount = model.getRowSize();
        final int colCount = model.getColumnSize();
        final int colCountInclRowHeader = colCount + GridDimension.ROW_HEADER_OFFSET;

        final CellMeasurement[][] cellMeasurements = new CellMeasurement[colCountInclRowHeader][rowCount];

        // Measure all row headers
        for (int row = 0; row < rowCount; row++) {
            CellModel rowHeaderModel = model.getRowHeader(row);
            CellMeasurement cellDimension = measurer.measureCell(rowHeaderModel);
            cellMeasurements[GridDimension.ROW_HEADER_POSITION][row] = cellDimension;
        }

        // measure all the cells.
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                final CellModel cellModel = model.getCellModel(row, col);
                CellMeasurement cellDimension = measurer.measureCell(cellModel);
                cellMeasurements[col + GridDimension.ROW_HEADER_OFFSET][row] = cellDimension;
            }
        }


        double[] tableColumnWidths = calculateColumnWidths(cellMeasurements, colCountInclRowHeader, rowCount);
        double[] tableRowHeights = calculateRowHeights(cellMeasurements, colCountInclRowHeader, rowCount);

        double[] vertLinePositions = calculateLinePositionsFromGaps(tableColumnWidths);
        double[] horzLinePositions = calculateLinePositionsFromGaps(tableRowHeights);

        double[] tableBottomGaps = calculateBottomGaps(cellMeasurements, colCountInclRowHeader, rowCount);

        CellDimension[][] cells = calculateCells(cellMeasurements, vertLinePositions, colCountInclRowHeader, rowCount);

        return new GridDimension(false, vertLinePositions, horzLinePositions,
                tableRowHeights, tableColumnWidths, tableBottomGaps, cells);
    }

    private double[] calculateColumnWidths(final CellMeasurement[][] cellMeasurements, int colCount, int rowCount) {
        final double[] columnWidths = new double[colCount];

        for (int col = 0; col < colCount; col++) {
            columnWidths[col] = measureColumnWidth(cellMeasurements, rowCount, col);
        }
        return columnWidths;
    }

    private double measureColumnWidth(final CellMeasurement[][] cellMeasurements, int rowCount, final int col) {
        double maxColWidth = 0;
        for (int row = 0; row < rowCount; row++) {
            final double cellWidth = cellMeasurements[col][row].getTotalWidth();
            if (cellWidth > maxColWidth) {
                maxColWidth = cellWidth;
            }
        }
        return Math.max(maxColWidth, MIN_COL_WIDTH);
    }

    private double[] calculateRowHeights(final CellMeasurement[][] cellMeasurements, int colCount, int rowCount) {
        final double[] rowHeights = new double[rowCount];

        for (int row = 0; row < rowCount; row++) {
            rowHeights[row] = measureRowHeight(cellMeasurements, colCount, row);
        }
        return rowHeights;
    }

    private double measureRowHeight(final CellMeasurement[][] cellMeasurements, int colCount, final int row) {
        double maxRowHeight = 0;
        for (int col = 0; col < colCount; col++) {
            final double cellHeight = cellMeasurements[col][row].getMaxHeight();
            if (cellHeight > maxRowHeight) {
                maxRowHeight = cellHeight;
            }
        }
        return Math.max(maxRowHeight, MIN_ROW_HEIGHT);
    }

    private double[] calculateLinePositionsFromGaps(final double[] gaps) {
        final double[] positions = new double[gaps.length + 1];
        positions[0] = 0.0;

        for (int i = 0; i < gaps.length; i++) {
            positions[i + 1] = positions[i] + gaps[i];
        }
        return positions;
    }

    private double[] calculateBottomGaps(final CellMeasurement[][] cellMeasurements, int colCount, int rowCount) {
        final double[] bottomGaps = new double[rowCount];

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                double cellBottomGap = cellMeasurements[col][row].getBottomGap();
                bottomGaps[row] = Math.max(bottomGaps[row], cellBottomGap);
            }
        }

        return bottomGaps;
    }

    private CellDimension[][] calculateCells(final CellMeasurement[][] cellMeasurements, double[] vertLinePositions,
                                             int colCount, int rowCount) {

        CellDimension[][] cells = new CellDimension[colCount][rowCount];
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            for (int colIndex = 0; colIndex < colCount; colIndex++) {
                double vertLinePosition = vertLinePositions[colIndex];
                final double horizontalPadding = cellMeasurements[colIndex][rowIndex].getHorizontalPadding();
                double[] characterWidths = cellMeasurements[colIndex][rowIndex].getCharacterWidths();
                cells[colIndex][rowIndex] = calculateCell(vertLinePosition, horizontalPadding, characterWidths);
            }
        }


        return cells;
    }

    @VisibleForTesting
    protected CellDimension calculateCell(double vertLinePosition, double horizontalPadding, double[] characterWidths) {
        double[] characterStarts = new double[characterWidths.length + 1];
        double[] characterMids = new double[characterWidths.length];
        double[] characterEnds = new double[characterWidths.length];

        double nextStart = vertLinePosition + horizontalPadding;
        int characterIndex = 0;
        for (; characterIndex < characterWidths.length; characterIndex++) {
            characterStarts[characterIndex] = nextStart;
            characterMids[characterIndex] = nextStart + (characterWidths[characterIndex] / 2);
            characterEnds[characterIndex] = nextStart + characterWidths[characterIndex];
            nextStart = characterEnds[characterIndex];
        }
        // This is to allow the caret to positioned at position n+1 where n is the number of characters.
        characterStarts[characterIndex] = nextStart;

        return new CellDimension(characterWidths.length, characterStarts, characterMids, characterEnds);
    }
}
