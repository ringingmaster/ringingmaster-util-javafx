package org.ringingmaster.util.javafx.grid.model;

/**
 * TODO comments ???
 *
 * @author Steve Lake
 */
public interface CellModel extends Iterable<CharacterModel> {

    int getLength();

    CharacterModel getCharacterModel(int index);

    void insertCharacter(int index, String character);

    void removeCharacter(int index);

}
