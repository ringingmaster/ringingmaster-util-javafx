package org.ringingmaster.util.javafx.grid.canvas;

import org.junit.Assert;
import org.junit.Test;
import org.ringingmaster.util.javafx.grid.GridPosition;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Steve Lake
 */
public class CaretPositionMoverTest {

    @Test
    public void allCallsHandleEmptyModel() {
        StubGridEditorModel model = new StubGridEditorModel(new String[][]{}, new GridPosition(0, 0, 0));
        GridPane mockGridPane = mock(GridPane.class);
        when(mockGridPane.getModel()).thenReturn(model);

        CaretPositionMover caretPositionMover = new CaretPositionMover();
        caretPositionMover.setModel(mockGridPane.getModel());

        caretPositionMover.moveLeft();
        Assert.assertEquals(new GridPosition(0, 0, 0), model.getCaretPosition());
        caretPositionMover.moveRight();
        Assert.assertEquals(new GridPosition(0, 0, 0), model.getCaretPosition());
        caretPositionMover.moveUp();
        Assert.assertEquals(new GridPosition(0, 0, 0), model.getCaretPosition());
        caretPositionMover.moveDown();
        Assert.assertEquals(new GridPosition(0, 0, 0), model.getCaretPosition());
        caretPositionMover.moveToStartOfLastCellIfItHasContentsElseLastButOne();
        Assert.assertEquals(new GridPosition(0, 0, 0), model.getCaretPosition());
        caretPositionMover.moveToStartOfRow();
        Assert.assertEquals(new GridPosition(0, 0, 0), model.getCaretPosition());
    }

    @Test
    public void rollsRightToNextPosition() {
        String[][] characters = {{"ab", "cd"},
                {"", "e"}};
        StubGridEditorModel model = new StubGridEditorModel(characters, new GridPosition(0, 0, 0));
        GridPane mockGridPane = mock(GridPane.class);
        when(mockGridPane.getModel()).thenReturn(model);
        CaretPositionMover caretPositionMover = new CaretPositionMover();
        caretPositionMover.setModel(mockGridPane.getModel());

        caretPositionMover.moveRight();
        Assert.assertEquals(new GridPosition(0, 0, 1), model.getCaretPosition());
        caretPositionMover.moveRight();
        Assert.assertEquals(new GridPosition(0, 0, 2), model.getCaretPosition());
        caretPositionMover.moveRight();
        Assert.assertEquals(new GridPosition(1, 0, 0), model.getCaretPosition());
        caretPositionMover.moveRight();
        Assert.assertEquals(new GridPosition(1, 0, 1), model.getCaretPosition());
        caretPositionMover.moveRight();
        Assert.assertEquals(new GridPosition(1, 0, 2), model.getCaretPosition());
        caretPositionMover.moveRight();
        Assert.assertEquals(new GridPosition(0, 1, 0), model.getCaretPosition());
        caretPositionMover.moveRight();
        Assert.assertEquals(new GridPosition(1, 1, 0), model.getCaretPosition());
        caretPositionMover.moveRight();
        Assert.assertEquals(new GridPosition(1, 1, 1), model.getCaretPosition());
        caretPositionMover.moveRight();
        Assert.assertEquals(new GridPosition(1, 1, 1), model.getCaretPosition());
    }

    @Test
    public void rollsLeftToPreviousPosition() {
        String[][] characters = {{"ab", "cd"},
                {"", "e"}};
        StubGridEditorModel model = new StubGridEditorModel(characters, new GridPosition(1, 1, 1));
        GridPane mockGridPane = mock(GridPane.class);
        when(mockGridPane.getModel()).thenReturn(model);
        CaretPositionMover caretPositionMover = new CaretPositionMover();
        caretPositionMover.setModel(mockGridPane.getModel());

        caretPositionMover.moveLeft();
        Assert.assertEquals(new GridPosition(1, 1, 0), model.getCaretPosition());
        caretPositionMover.moveLeft();
        Assert.assertEquals(new GridPosition(0, 1, 0), model.getCaretPosition());
        caretPositionMover.moveLeft();
        Assert.assertEquals(new GridPosition(1, 0, 2), model.getCaretPosition());
        caretPositionMover.moveLeft();
        Assert.assertEquals(new GridPosition(1, 0, 1), model.getCaretPosition());
        caretPositionMover.moveLeft();
        Assert.assertEquals(new GridPosition(1, 0, 0), model.getCaretPosition());
        caretPositionMover.moveLeft();
        Assert.assertEquals(new GridPosition(0, 0, 2), model.getCaretPosition());
        caretPositionMover.moveLeft();
        Assert.assertEquals(new GridPosition(0, 0, 1), model.getCaretPosition());
        caretPositionMover.moveLeft();
        Assert.assertEquals(new GridPosition(0, 0, 0), model.getCaretPosition());
        caretPositionMover.moveLeft();
        Assert.assertEquals(new GridPosition(0, 0, 0), model.getCaretPosition());
    }

    @Test
    public void rollsDownToNextRow() {
        String[][] characters = {{"ab", "cd"},
                {"r", "e"},
                {"", "x"},
                {"kwd", "z"}};
        StubGridEditorModel model = new StubGridEditorModel(characters, new GridPosition(0, 0, 1));
        GridPane mockGridPane = mock(GridPane.class);
        when(mockGridPane.getModel()).thenReturn(model);
        CaretPositionMover caretPositionMover = new CaretPositionMover();
        caretPositionMover.setModel(mockGridPane.getModel());

        caretPositionMover.moveRight();//to set the sticky position
        caretPositionMover.moveDown();
        Assert.assertEquals(new GridPosition(0, 1, 1), model.getCaretPosition());
        caretPositionMover.moveDown();
        Assert.assertEquals(new GridPosition(0, 2, 0), model.getCaretPosition());
        caretPositionMover.moveDown();
        Assert.assertEquals(new GridPosition(0, 3, 2), model.getCaretPosition());
        caretPositionMover.moveDown();
        Assert.assertEquals(new GridPosition(0, 3, 2), model.getCaretPosition());
    }

    @Test
    public void rollsUpToPreviousRow() {
        String[][] characters = {{"ab", "cd"},
                {"", "e"},
                {"w", "x"},
                {"wqs", "z"}};
        StubGridEditorModel model = new StubGridEditorModel(characters, new GridPosition(0, 3, 1));
        GridPane mockGridPane = mock(GridPane.class);
        when(mockGridPane.getModel()).thenReturn(model);
        CaretPositionMover caretPositionMover = new CaretPositionMover();
        caretPositionMover.setModel(mockGridPane.getModel());

        caretPositionMover.moveRight();//to set the sticky position
        caretPositionMover.moveUp();
        Assert.assertEquals(new GridPosition(0, 2, 1), model.getCaretPosition());
        caretPositionMover.moveUp();
        Assert.assertEquals(new GridPosition(0, 1, 0), model.getCaretPosition());
        caretPositionMover.moveUp();
        Assert.assertEquals(new GridPosition(0, 0, 2), model.getCaretPosition());
        caretPositionMover.moveUp();
        Assert.assertEquals(new GridPosition(0, 0, 2), model.getCaretPosition());
    }

    @Test
    public void movesToStartOfLastCellWhenPopulated() {
        String[][] characters = {{"ab", "cd", "test"},
                {"", "efgh", ""}};
        StubGridEditorModel model = new StubGridEditorModel(characters, new GridPosition(0, 0, 1));
        GridPane mockGridPane = mock(GridPane.class);
        when(mockGridPane.getModel()).thenReturn(model);
        CaretPositionMover caretPositionMover = new CaretPositionMover();
        caretPositionMover.setModel(mockGridPane.getModel());

        caretPositionMover.moveToStartOfLastCellIfItHasContentsElseLastButOne();
        Assert.assertEquals(new GridPosition(2, 0, 0), model.getCaretPosition());
        caretPositionMover.moveDown();
        Assert.assertEquals(new GridPosition(2, 1, 0), model.getCaretPosition());
    }

    @Test
    public void movesToStartOfLastButOneCellWhenUnpopulated() {
        String[][] characters = {{"ab", "cd", "test"},
                {"", "efgh", ""}};
        StubGridEditorModel model = new StubGridEditorModel(characters, new GridPosition(1, 1, 0));
        GridPane mockGridPane = mock(GridPane.class);
        when(mockGridPane.getModel()).thenReturn(model);
        CaretPositionMover caretPositionMover = new CaretPositionMover();
        caretPositionMover.setModel(mockGridPane.getModel());

        caretPositionMover.moveToStartOfLastCellIfItHasContentsElseLastButOne();
        Assert.assertEquals(new GridPosition(1, 1, 0), model.getCaretPosition());
        caretPositionMover.moveUp();
        Assert.assertEquals(new GridPosition(1, 0, 0), model.getCaretPosition());
    }

    @Test
    public void movesToStartOnluOneColumn() {
        String[][] characters = {{"ab"},
                {"",}};
        StubGridEditorModel model = new StubGridEditorModel(characters, new GridPosition(0, 0, 2));
        GridPane mockGridPane = mock(GridPane.class);
        when(mockGridPane.getModel()).thenReturn(model);
        CaretPositionMover caretPositionMover = new CaretPositionMover();
        caretPositionMover.setModel(mockGridPane.getModel());

        caretPositionMover.moveToStartOfLastCellIfItHasContentsElseLastButOne();
        Assert.assertEquals(new GridPosition(0, 0, 0), model.getCaretPosition());
        caretPositionMover.moveUp();
        Assert.assertEquals(new GridPosition(0, 0, 0), model.getCaretPosition());
    }

    @Test
    public void movesToStartOfRow() {
        String[][] characters = {{"ab", "cd"},
                {"dl", "efgh"}};
        StubGridEditorModel model = new StubGridEditorModel(characters, new GridPosition(1, 0, 1));
        GridPane mockGridPane = mock(GridPane.class);
        when(mockGridPane.getModel()).thenReturn(model);
        CaretPositionMover caretPositionMover = new CaretPositionMover();
        caretPositionMover.setModel(mockGridPane.getModel());

        caretPositionMover.moveToStartOfRow();
        Assert.assertEquals(new GridPosition(0, 0, 0), model.getCaretPosition());
        caretPositionMover.moveDown();
        Assert.assertEquals(new GridPosition(0, 1, 0), model.getCaretPosition());
    }
}
