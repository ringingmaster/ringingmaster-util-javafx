package org.ringingmaster.util.javafx.namevaluepair;

import org.ringingmaster.util.javafx.color.ColorUtil;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Function;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class NameValuePairTable extends TableView<NameValuePairModel> {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static final String STYLESHEET = "org/ringingmaster/util/javafx/namevaluepair/namevaluepair.css";
	public static final String INVALID_CELL_STYLE = "invalidCell";

	public NameValuePairTable() {
		getStylesheets().add(STYLESHEET);

		getColumns().addAll(Arrays.asList(

				createTableColumn("Name", NameValuePairModel::nameProperty, 150, 150, 150),
				createTableColumn("Value", NameValuePairModel::valueProperty, 10000, 100, 10000)
		));

		setFixedCellSize(22);
		setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
		setEmptyTableMessage("");
		hideHeaders();
	}

	protected void setEmptyTableMessage(String message) {
		setPlaceholder(new Label(message));
	}

	public void setSize(double size) {
		getColumns().get(0).setMaxWidth(size);
		getColumns().get(0).setMinWidth(size);
		getColumns().get(0).setPrefWidth(size);
	}

	void hideHeaders() {
		// Hide the status headers
		widthProperty().addListener((ov, t, t1) -> {
			// Get the table header
			Pane header = (Pane) lookup("TableHeaderRow");
			if (header != null && header.isVisible()) {
				header.setMaxHeight(0);
				header.setMinHeight(0);
				header.setPrefHeight(0);
				header.setVisible(false);
				header.setManaged(false);
			}
		});
	}

	private <S> TableColumn<S, NameValueCellDescriptor> createTableColumn(String name, Function<S, ObservableValue<NameValueCellDescriptor>> propertyMapper, double maxWidth, double minWidth, double prefWidth) {
		TableColumn<S, NameValueCellDescriptor> col = new TableColumn<>(name);
		col.setCellValueFactory(cellData -> propertyMapper.apply(cellData.getValue()));
		col.setMaxWidth(maxWidth);
		col.setMinWidth(minWidth);
		col.setPrefWidth(prefWidth);

		col.setCellFactory(new Callback<TableColumn<S, NameValueCellDescriptor>, TableCell<S, NameValueCellDescriptor>>() {
			@Override
			public TableCell<S, NameValueCellDescriptor> call(TableColumn<S, NameValueCellDescriptor> column) {
				return new TableCell<S, NameValueCellDescriptor>() {
					@Override
					protected void updateItem(NameValueCellDescriptor item, boolean empty) {
						if (item == getItem()) return;

						super.updateItem(item, empty);

						setMinWidth(200);
						if (item != null) {


							if (item.getBackgroundColor() == null) {
								setStyle("");
							}
							else {
								setStyle("-fx-background-color: " + ColorUtil.toWeb(item.getBackgroundColor()));
							}

							if (item.isInvalid()) {
								if (!getStyleClass().contains(INVALID_CELL_STYLE)) {
									getStyleClass().add(INVALID_CELL_STYLE);
								}
							}
							else {
								getStyleClass().remove(INVALID_CELL_STYLE);
							}

							setPadding(new Insets(0,4,0,4));

							super.setText(item.getText());
						}
						else {
							super.setText(null);
						}
					}

				};
			}
		});

		return col ;
	}

	public void updateDisplayProperty(String propertyName, String value) {
		updateDisplayProperty(propertyName, value, null);
	}

	public void updateDisplayProperty(String propertyName, String value, Color backgroundColor) {
		getItems().stream()
				.filter(columnDescriptor -> columnDescriptor.getName().getText().equals(propertyName))
				.forEach(pair -> pair.setValue(new NameValueCellDescriptor(value, backgroundColor, false)));
	}

	public void updateDisplayProperty(String propertyName, String value, boolean disabled) {
		getItems().stream()
				.filter(columnDescriptor -> columnDescriptor.getName().getText().equals(propertyName))
				.forEach(pair -> {pair.setValue(new NameValueCellDescriptor(value, null, disabled));
					pair.setName(new NameValueCellDescriptor(propertyName, null, disabled));
				});
	}

}
