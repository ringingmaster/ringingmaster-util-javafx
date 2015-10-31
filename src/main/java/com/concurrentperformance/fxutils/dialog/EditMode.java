package com.concurrentperformance.fxutils.dialog;

/**
 * TODO Comments
 *
 * @author Lake
 */
public enum EditMode {

	ADD("Add"),
	EDIT("Edit")
	;

	private final String editText;

	EditMode(String editText) {
		this.editText = editText;
	}

	public String getEditText() {
		return editText;
	}
}
