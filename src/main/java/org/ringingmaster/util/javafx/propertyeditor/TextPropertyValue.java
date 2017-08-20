package org.ringingmaster.util.javafx.propertyeditor;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class TextPropertyValue extends SkeletalPropertyValue implements PropertyValue {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private StringProperty psudoValue = new SimpleStringProperty("");
	private String previousNotifiedValue = null;

	public TextPropertyValue(String propertyName) {
		super(propertyName);
		setEditor(buildTextField());
	}

	private TextField buildTextField() {
		TextField textField = new TextField();
		textField.setPadding(new Insets(0,2,0,2));
		textField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		//TODO textField.setPromptText("TEST");

		// update psudo value when valid
		textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
			try {
				psudoValue.setValue(newValue);
			}
			catch (NumberFormatException e) {
				// Do nothing
			}
		});

		return textField;
	}

	public void setListener(ChangeListener<String> listener, CallbackStyle callbackStyle) {
		TextField textField = (TextField) getEditor();
		if (callbackStyle == CallbackStyle.EVERY_KEYSTROKE) {
			psudoValue.addListener(listener);
		} else if (callbackStyle == CallbackStyle.WHEN_FINISHED) {
			// just loss of focus.
			textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
				if (newValue == false) {
					notifyListener(listener);
				}
			});

			textField.setOnKeyReleased(event -> {
				if (event.getCode().equals(KeyCode.ENTER)) {
					notifyListener(listener);
				}
			});
		} else {
			throw new IllegalArgumentException();
		}
	}

	private void notifyListener(ChangeListener<String> listener) {

		final String newValue = psudoValue.getValue();

		if (newValue != null && !newValue.equals(previousNotifiedValue)) {
			listener.changed(psudoValue, previousNotifiedValue, newValue);
			previousNotifiedValue = newValue;
		}
	}

	public void setValue(String value) {
		final TextField textField = (TextField) getEditor();
		if (value == null ||
			!textField.getText().equals(value)) {
			textField.setText(value);
		}
	}

	@Override
	public void setFont(Font font) {
		((TextField)getEditor()).setFont(font);
	}

	@Override
	public String toString() {
		return "TextPropertyValue{" +
				"name=" + getName() +
				'}';
	}
}
