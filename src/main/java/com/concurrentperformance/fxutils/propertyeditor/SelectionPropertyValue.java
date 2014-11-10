package com.concurrentperformance.fxutils.propertyeditor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class SelectionPropertyValue extends SkeletalPropertyValue implements PropertyValue {

	private static final Logger log = LoggerFactory.getLogger(SelectionPropertyValue.class);

	private boolean suppressNotifications = false;

	public SelectionPropertyValue(String propertyName) {
		super(propertyName);
		setEditor(buildComboBox());
	}

	private Region buildComboBox() {
		ComboBox comboBox = new ComboBox();

		comboBox.setPadding(new Insets(0, 2, 0, 2));
		comboBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

		final Callback<ListView<String>, ListCell<String>> callback = new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> param) {
				final ListCell<String> cell = new ListCell<String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super. updateItem(item, empty);
						setFont(Font.getDefault()); //TODO set the font using the propertyGeometry
						setPadding(new Insets(0, 1, 0, 1));
						setText(item);
					}
				};
				return cell;
			}

		};
		comboBox.setCellFactory(callback);
		comboBox.setButtonCell(callback.call(null));

		return comboBox;
	}

	public void setListener(ChangeListener<Number> listener) {
		((ComboBox) getEditor()).getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				if (!suppressNotifications) {
					listener.changed(observableValue, oldValue, newValue);
				}
			}
		});
	}

	public void setItems(List<String> items) {
		final ComboBox comboBox = (ComboBox) getEditor();
		// When updating the combo box items, we remove suppress our listener, so it does not get the value set to -1
		// and then back to the valid value.
		suppressNotifications = true;
		final ObservableList<String> observableItems = FXCollections.observableArrayList(items);
		comboBox.setItems(observableItems);
		suppressNotifications = false;
	}

	public void setSelectedIndex(int selectedIndex) {
		((ComboBox) getEditor()).getSelectionModel().select(selectedIndex);
	}

	@Override
	public void positionEditor(double x, double y, double width, double height) {
		super.positionEditor(x,y-1.0,width,height);
	}


}
