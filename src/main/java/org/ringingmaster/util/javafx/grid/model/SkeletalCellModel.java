package org.ringingmaster.util.javafx.grid.model;

import java.util.Iterator;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public abstract class SkeletalCellModel implements CellModel {


    @Override
    public void insertCharacter(int index, String character) {
        //TODO
    }

    @Override
    public void removeCharacter(int index) {
        //TODO
    }

    @Override
    public Iterator<CharacterModel> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < getLength();
            }

            @Override
            public CharacterModel next() {
                return getCharacterModel(index++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

}
