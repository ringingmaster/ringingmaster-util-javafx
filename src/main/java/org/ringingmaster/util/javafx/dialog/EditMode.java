package org.ringingmaster.util.javafx.dialog;

/**
 * TODO Comments
 *
 * @author Steve Lake
 */
public enum EditMode {

    ADD("Add"),
    EDIT("Edit");

    private final String editText;

    EditMode(String editText) {
        this.editText = editText;
    }

    public String getEditText() {
        return editText;
    }
}
