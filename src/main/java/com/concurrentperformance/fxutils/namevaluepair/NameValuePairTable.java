package com.concurrentperformance.fxutils.namevaluepair;

import com.concurrentperformance.fxutils.color.ColorUtil;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
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

	public static final String STYLESHEET = "com/concurrentperformance/fxutils/namevaluepair/namevaluepair.css";

	public NameValuePairTable() {
		getStylesheets().add(STYLESHEET);

		getColumns().addAll(Arrays.asList(

				createTableColumn("Name", NameValuePairModel::nameProperty),
				createTableColumn("Value", NameValuePairModel::valueProperty)
		));

		setFixedCellSize(22);

		hideHeaders();

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

		private <S> TableColumn<S, NameValueColumnDescriptor> createTableColumn(String name, Function<S, ObservableValue<NameValueColumnDescriptor>> propertyMapper) {
		TableColumn<S, NameValueColumnDescriptor> col = new TableColumn<>(name);
		col.setCellValueFactory(cellData -> propertyMapper.apply(cellData.getValue()));
		col.setCellFactory(new Callback<TableColumn<S, NameValueColumnDescriptor>, TableCell<S, NameValueColumnDescriptor>>() {
			@Override
			public TableCell<S, NameValueColumnDescriptor> call(TableColumn<S, NameValueColumnDescriptor> param) {
				return new TableCell<S, NameValueColumnDescriptor>() {
					@Override protected void updateItem(NameValueColumnDescriptor item, boolean empty) {
					if (item == getItem()) return;

						super.updateItem(item, empty);

						if (item != null) {

							if (item.getBackgroundColor() == null) {
								setStyle("");
							}
							else {
								setStyle("-fx-background-color: " + ColorUtil.toWeb(item.getBackgroundColor()));
							}

							if (item.isInvalid()) {
								getStyleClass().add("invalidCell");
							}
							else {
								getStyleClass().remove("invalidCell");
							}

							setPadding(new Insets(0,4,0,4));

							super.setText(item.toString());
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

}
