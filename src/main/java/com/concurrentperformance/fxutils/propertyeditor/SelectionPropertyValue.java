package com.concurrentperformance.fxutils.propertyeditor;

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

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class SelectionPropertyValue extends SkeletalPropertyValue implements PropertyValue {

	public SelectionPropertyValue(String propertyName, String propertyValue) {
		super(propertyName, buildComboBox());
	}

	private static Region buildComboBox() {
		ComboBox comboBox = new ComboBox();

		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("False", "item 2");
		comboBox.setItems(items);
		comboBox.getSelectionModel().select(0);

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

	@Override
	public void positionEditor(double x, double y, double width, int height) {
		super.positionEditor(x,y-1.0,width,height);
	}

}
