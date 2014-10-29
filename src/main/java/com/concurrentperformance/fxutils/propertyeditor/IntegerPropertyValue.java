package com.concurrentperformance.fxutils.propertyeditor;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
public class IntegerPropertyValue extends SkeletalPropertyValue implements PropertyValue {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private IntegerProperty psudoValue = new SimpleIntegerProperty(0);

	public IntegerPropertyValue(String propertyName) {
		super(propertyName);
		setEditor(buildTextField());
	}

	private TextField buildTextField() {
		TextField textField = new TextField();
		textField.setPadding(new Insets(0,2,0,2));
		textField.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		//TODO textField.setPromptText("TEST");

		// Prevent anything other than integer numbers
		textField.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if ((event.getCharacter() != null) &&
						(event.getCharacter().length() > 0) &&
						(!Character.isDigit(event.getCharacter().charAt(0)))) {
					event.consume();
				}
			}
		});

		// update psudo value when valid
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
				try {
					final Integer parsedValue = Integer.parseInt(newValue);
					psudoValue.setValue(parsedValue);
				}
				catch (NumberFormatException e) {
					// Do nothing
				}
			}
		});

		return textField;
	}

	public void setListener(ChangeListener<? super Number> listener, CallbackStyle callbackStyle) {

		TextField textField = (TextField) getEditor();
		if (callbackStyle == CallbackStyle.EVERY_KEYSTROKE) {
			psudoValue.addListener(listener);
		}
		else if (callbackStyle == CallbackStyle.WHEN_FINISHED){
			// just loss of focus.
			textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
					if (newValue == false) {
						notifyListener(listener);
					}
				}
			});

			textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (event.getCode().equals(KeyCode.ENTER)) {
						notifyListener(listener);
					}
				}
			});
		}
		else {
			throw new IllegalArgumentException();
		}

	}

	static Integer previousNotifiedValue = null;

	private void notifyListener(ChangeListener<? super Number> listener) {

		final Integer newValue = psudoValue.getValue();

		if (newValue != null && !newValue.equals(previousNotifiedValue)) {
			listener.changed(psudoValue, previousNotifiedValue, newValue);
			previousNotifiedValue = newValue;
		}
	}

	public void setValue(Integer value) {
		final TextField textField = (TextField) getEditor();
		if (value == null ||
			!textField.getText().equals(value)) {
			textField.setText(value.toString());
		}
	}

	@Override
	public void setFont(Font font) {
		((TextField)getEditor()).setFont(font);
	}
}
