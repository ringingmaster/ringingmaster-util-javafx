package org.ringingmaster.util.javafx.propertyeditor;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
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

    private CallbackStyle callbackStyle = CallbackStyle.EVERY_KEYSTROKE;
    private Integer previousNotifiedValue = null;
    private Integer currentValue = null;
    private ChangeListener<? super Number> listener;

    public IntegerPropertyValue(String propertyName) {
        super(propertyName);
        setEditor(buildTextField());
    }

    private TextField buildTextField() {
        TextField textField = new TextField();
        textField.setPadding(new Insets(0, 2, 0, 2));
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
                    if (Strings.isNullOrEmpty(newValue)) {
                        currentValue = null;
                    } else {
                        currentValue = Integer.parseInt(newValue);
                    }
                } catch (NumberFormatException e) {
                    // Do nothing
                }
                if (callbackStyle == CallbackStyle.EVERY_KEYSTROKE) {
                    notifyListener();
                }
            }
        });

        return textField;
    }

    public void setListener(ChangeListener<? super Number> listener, CallbackStyle callbackStyle) {

        this.callbackStyle = callbackStyle;
        this.listener = listener;

        TextField textField = (TextField) getEditor();
        if (callbackStyle == CallbackStyle.EVERY_KEYSTROKE) {
            // Do nothing
        } else if (callbackStyle == CallbackStyle.WHEN_FINISHED) {
            // just loss of focus.
            textField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue == false) {
                    notifyListener();
                }
            });

            textField.setOnKeyReleased(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    notifyListener();
                }
            });
        } else {
            throw new IllegalArgumentException();
        }

    }

    public void setValue(Integer value) {
        final TextField textField = (TextField) getEditor();
        String textFieldValue = textField.getText();
        if (value == null) {
            if (!Strings.isNullOrEmpty(textFieldValue)) {
                textField.setText("");
            }
        } else {
            String convertedValue = value.toString();
            if (!Objects.equal(convertedValue, textFieldValue)) {
                textField.setText(convertedValue);
            }
        }
        // Need to do this, otherwise this change never gets notified as no keyboard action takes place.
        if (callbackStyle == CallbackStyle.WHEN_FINISHED) {
            notifyListener();
        }
    }

    private void notifyListener() {
        if (listener != null) {
            if (!Objects.equal(currentValue, previousNotifiedValue)) {
                listener.changed(null, previousNotifiedValue, currentValue);
                previousNotifiedValue = currentValue;
            }
        }
    }

    @Override
    public void setFont(Font font) {
        ((TextField) getEditor()).setFont(font);
    }

}
