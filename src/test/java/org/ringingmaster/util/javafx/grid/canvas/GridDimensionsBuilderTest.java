package org.ringingmaster.util.javafx.grid.canvas;

import org.junit.Test;
import org.ringingmaster.util.javafx.grid.GridPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TODO comments ???
 *
 * @author Steve Lake
 */
public class GridDimensionsBuilderTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void rowHeaderOffsetIsCorrect() {

        String[][] characters = {{"abc"},
                {"d"}};
        String[] rowHeaders = {"12", "34"};


        StubGridEditorModel model = new StubGridEditorModel(characters, rowHeaders, new GridPosition(0, 0, 0));

        GridDimensions d = new GridDimensionBuilder().setModel(model).build();

        assertEquals(model.getRowSize(), d.getRowCount());
        assertEquals(model.getColumnSize(), d.getColumnCount());

        assertEquals(d.getTableLeft(true), d.getTableVerticalLinePosition(0), 0.0);
        assertEquals(d.getTableTop(), d.getTableHorizontalLinePosition(0), 0.0);

        assertEquals(d.getRowHeaderLeft(), 0.0, 0.0);

        double lastVerticalLinePosition = d.getRowHeaderLeft();
        for (int i = 0; i < d.getColumnCount() + 1; i++) {
            final double verticalLinePosition = d.getTableVerticalLinePosition(i);
            log.info("column index[{}], pos[{}]", i, verticalLinePosition);
            assertTrue(verticalLinePosition > (lastVerticalLinePosition + 2));// The +2 is to enforce the minimum padding between columns
            lastVerticalLinePosition = verticalLinePosition;
        }

        double lastHorizontalLinePosition = 0.0;
        for (int i = 1; i < d.getRowCount() + 1; i++) {
            final double horizontalLinePosition = d.getTableHorizontalLinePosition(i);
            log.info("row index[{}], pos[{}]", i, horizontalLinePosition);
            assertTrue(horizontalLinePosition > (lastHorizontalLinePosition + 2));// The +2 is to enforce the minimum padding between columns
            lastHorizontalLinePosition = horizontalLinePosition;
        }

    }

    @Test
    public void calculateCellGetsCorrectValues() {
        CellDimension cellDimension = new GridDimensionBuilder().calculateCell(10.0, 2, new double[]{10, 5, 20});
        assertEquals(3, cellDimension.getCharacterCount());

        assertEquals(12.0, cellDimension.getVerticalCharacterStartPosition(0), 0.000001);
        assertEquals(22.0, cellDimension.getVerticalCharacterStartPosition(1), 0.000001);
        assertEquals(27.0, cellDimension.getVerticalCharacterStartPosition(2), 0.000001);

        assertEquals(22.0, cellDimension.getVerticalCharacterEndPosition(0), 0.000001);
        assertEquals(27.0, cellDimension.getVerticalCharacterEndPosition(1), 0.000001);
        assertEquals(47.0, cellDimension.getVerticalCharacterEndPosition(2), 0.000001);

        assertEquals(17.0, cellDimension.getVerticalCharacterMidPosition(0), 0.000001);
        assertEquals(24.5, cellDimension.getVerticalCharacterMidPosition(1), 0.000001);
        assertEquals(37.0, cellDimension.getVerticalCharacterMidPosition(2), 0.000001);
    }

}