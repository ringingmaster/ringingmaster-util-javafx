package org.ringingmaster.util.javafx.grid.model;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public interface GridCellModel extends GridCharacterGroup {

    void insertCharacter(int index, String character);

    void removeCharacter(int index);

}
