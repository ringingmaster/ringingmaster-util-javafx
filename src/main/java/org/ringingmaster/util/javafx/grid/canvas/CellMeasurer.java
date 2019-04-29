package org.ringingmaster.util.javafx.grid.canvas;

import javafx.scene.text.Font;
import org.ringingmaster.util.javafx.font.FontMetrics;
import org.ringingmaster.util.javafx.grid.model.CellModel;
import org.ringingmaster.util.javafx.grid.model.CharacterModel;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * TODO
 *
 * @author Stephen
 */
public class CellMeasurer {

    public static final float MINIMUM_HEIGHT = 6;

    CellMeasurement measureCell(final CellModel cellModel) {
        checkNotNull(cellModel);
        double height = measureCellHeight(cellModel);
        double bottomGap = measureBottomGap(cellModel);
        double[] characterWidths = measureCellTextWidth(cellModel);
        int widthPadding = measureCellHorizontalPadding(cellModel);

        CellMeasurement dimension = new CellMeasurement(height, bottomGap, characterWidths, widthPadding);
        return dimension;
    }


    private float measureCellHeight(final CellModel cellModel) {
        float maxHeight = MINIMUM_HEIGHT;
        for (CharacterModel characterModel : cellModel) {
            final Font font = characterModel.getFont();
            FontMetrics fm = new FontMetrics(font);


            final float height = fm.getDescent() + fm.getAscent();
            if (height > maxHeight) {
                maxHeight = height;
            }
        }
        return maxHeight;
    }

    private double[] measureCellTextWidth(final CellModel cellModel) {
        int characterCount = cellModel.getLength();
        double[] characterWidths = new double[characterCount];
	    for (int index=0;index<cellModel.getLength();index++) {
		    CharacterModel CharacterModel = cellModel.getCharacterModel(index);
		    final Font font = CharacterModel.getFont();
		    final char cellText = CharacterModel.getCharacter();
            FontMetrics fm = new FontMetrics(font);
		    characterWidths[index] = fm.computeStringWidth(String.valueOf(cellText));
	    }
        return characterWidths;
    }

    private int measureCellHorizontalPadding(final CellModel cellModel) {
        return 3; //TODO get from a percentage of the largest character - although it should be consistent in a column.????
    }

    private float measureBottomGap(final CellModel cellModel) {
        float bottomGap = 0;
        for (CharacterModel CharacterModel : cellModel) {
            final Font font = CharacterModel.getFont();
//  TODO remove if suitable replacement - Note MaxDescent vs Descent
//   FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
//            final float descent = fm.getMaxDescent();

            FontMetrics fm = new FontMetrics(font);
            final float descent = fm.getDescent();

            if (bottomGap < descent) {
                bottomGap = descent;
            }
        }
        return bottomGap;
    }
}