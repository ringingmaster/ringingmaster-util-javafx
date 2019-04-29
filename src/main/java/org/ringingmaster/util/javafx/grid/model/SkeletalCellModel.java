package org.ringingmaster.util.javafx.grid.model;

import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public abstract class SkeletalCellModel implements CellModel {

    private List<GridModelListener> listeners;

    public SkeletalCellModel(final List<GridModelListener> listeners) {
        this.listeners = checkNotNull(listeners);
    }

    protected void fireCellStructureChanged() { //TODO rename
        for (GridModelListener listener : listeners) {
            listener.gridModelListener_contentsChanged();
        }
    }


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
