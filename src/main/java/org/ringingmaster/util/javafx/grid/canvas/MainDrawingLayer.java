package org.ringingmaster.util.javafx.grid.canvas;

import com.google.common.annotations.VisibleForTesting;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import org.ringingmaster.util.javafx.grid.model.AdditionalStyleType;
import org.ringingmaster.util.javafx.grid.model.CellModel;
import org.ringingmaster.util.javafx.grid.model.CharacterModel;
import org.ringingmaster.util.javafx.grid.model.GridModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.ringingmaster.util.javafx.grid.model.AdditionalStyleType.SUPERSCRIPT;

/**
 * TODO comments ???
 *
 * @author Steve Lake
 */
class MainDrawingLayer extends Canvas {

    public static final int EXTRA_END_LINE_TO_CLOSE_ROW_OR_COL = 1;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private GridModel model;
    private GridDimensions dimensions;

    void setModel(GridModel model) {
        this.model = model;
    }

    public void setDimensions(GridDimensions dimensions) {
        this.dimensions = dimensions;
        setWidth(dimensions.getTableRight());
        setHeight(dimensions.getTableBottom());
    }

    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFontSmoothingType(FontSmoothingType.LCD);
        clearBackground(gc);
        drawGrid(gc);
        drawCellsText(gc);
    }

    private void clearBackground(GraphicsContext gc) {
        gc.clearRect(0, 0, getWidth(), getHeight());
    }

    private void drawGrid(final GraphicsContext gc) {
        final int horzLineCount = model.getRowSize() + EXTRA_END_LINE_TO_CLOSE_ROW_OR_COL;
        int accountForRowHeader = model.hasRowHeader() ? 1 : 0;
        final int vertLineCount = model.getColumnSize() - accountForRowHeader + EXTRA_END_LINE_TO_CLOSE_ROW_OR_COL ;

        gc.setFill(model.getGridColor());
        gc.setStroke(model.getGridColor());
        gc.setLineWidth(1.0);

        // Draw horizontal lines
        final double left = dimensions.getTableLeft(model.hasRowHeader());
        final double right = dimensions.getTableRight();
        for (int horizLineIndex = 0; horizLineIndex < horzLineCount; horizLineIndex++) {
            final double horzLinePosition = dimensions.getTableHorizontalLinePosition(horizLineIndex);
            gc.strokeLine(left, horzLinePosition, right, horzLinePosition);
        }


        // Draw vertical lines
        final double top = dimensions.getTableTop();
        final double bottom = dimensions.getTableBottom();
        for (int vertLineIndex = 0; vertLineIndex < vertLineCount; vertLineIndex++) {
            final double vertLinePosition = dimensions.getTableVerticalLinePosition(vertLineIndex + accountForRowHeader);
            gc.strokeLine(vertLinePosition, top, vertLinePosition, bottom);
        }
    }

    private void drawCellsText(final GraphicsContext gc) {
        final int rowCount = model.getRowSize();
        final int colCount = model.getColumnSize();

        for (int row = 0; row < rowCount; row++) {
            final double textBottom = dimensions.getTextBottom(row);
            for (int col = 0; col < colCount; col++) {
                final CellModel cellModel = model.getCellModel(row, col);
                final CellDimension tableCellDimension = dimensions.getCell(row, col);

                drawCellText(gc, cellModel, tableCellDimension, textBottom);
                drawCellUnderline(gc, cellModel, tableCellDimension, textBottom);
            }
        }
    }

    private void drawCellText(GraphicsContext gc, CellModel cellModel, CellDimension tableCellDimension, final double textBottom) {
        for (int characterIndex = 0; characterIndex < cellModel.getLength(); characterIndex++) {
            double textLeft = tableCellDimension.getVerticalCharacterStartPosition(characterIndex);

            CharacterModel gridEditorCharacterModel = cellModel.getCharacterModel(characterIndex);
            final char cellText = gridEditorCharacterModel.getCharacter();
            final Font font = gridEditorCharacterModel.getFont();

            double bottom = textBottom;
            if (gridEditorCharacterModel.getAdditionalStyle().contains(SUPERSCRIPT)) {
                bottom -= font.getSize();
            }

            gc.setFill(gridEditorCharacterModel.getColor()); //TODO do we ned to set both for the font?
            gc.setStroke(gridEditorCharacterModel.getColor());
            gc.setFont(font);
            gc.fillText(String.valueOf(cellText), textLeft, bottom);//What about the other version of this using and AttributedString
        }
    }

    private void drawCellUnderline(GraphicsContext gc, CellModel cellModel, CellDimension tableCellDimension, double textBottom) {
        gc.setFill(Color.RED);
        gc.setStroke(Color.RED);
        gc.setLineWidth(1.0);

        final int bottomOffset = 2;
        final int pixelPitch = 2;
        final double bottom = textBottom + bottomOffset;

        for (int characterIndex = 0; characterIndex < cellModel.getLength(); characterIndex++) {
            CharacterModel characterModel = cellModel.getCharacterModel(characterIndex);
            if (characterModel.getAdditionalStyle().contains(AdditionalStyleType.WIGGLY_UNDERLINE)) {
                final double textLeft = tableCellDimension.getVerticalCharacterStartPosition(characterIndex);
                final double textRight = tableCellDimension.getVerticalCharacterEndPosition(characterIndex);

                double X = textLeft;
                while (X < textRight) {
                    double leftX = X;
                    double leftY = getVerticalWiggleOffset(X, pixelPitch) + bottom;

                    X = Math.min(alignToNextPixelPitch(X, pixelPitch), textRight);
                    double rightX = X;
                    double rightY = getVerticalWiggleOffset(X, pixelPitch) + bottom;

                    gc.strokeLine(leftX, leftY, rightX, rightY);
                }
            }
        }
    }

    @VisibleForTesting
    static double getVerticalWiggleOffset(double horizontalValue, int pixelPitch) {
        int nextLeftUptickPeakPixelAlignment = (int) Math.floor(horizontalValue / (pixelPitch * 2)) * pixelPitch * 2;
        double distanceFromUptickPeak = horizontalValue - nextLeftUptickPeakPixelAlignment;
        if (distanceFromUptickPeak <= pixelPitch) {
            return distanceFromUptickPeak;
        } else {
            return pixelPitch - (distanceFromUptickPeak - pixelPitch);
        }
    }

    @VisibleForTesting
    static int alignToNextPixelPitch(double value, int pixelPitch) {
        // The hacky 0.0000001 value ensures that a value that aligns exactly to the pixel boundary rolls over to the next
        return ((int) (Math.ceil((value + 0.0000001) / pixelPitch))) * pixelPitch;
    }


}
