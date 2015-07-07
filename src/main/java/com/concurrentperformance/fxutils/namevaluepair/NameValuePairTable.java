package com.concurrentperformance.fxutils.namevaluepair;

import com.concurrentperformance.fxutils.color.ColorUtil;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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

	public NameValuePairTable() {
		getColumns().addAll(Arrays.asList(
				createTableColumn("Name", NameValuePairModel::nameProperty),
				createTableColumn("Value", NameValuePairModel::valueProperty)
		));

		setRowFactory(tv -> {
			TableRow<NameValuePairModel> row = new TableRow<>();

			// use EasyBind to access the nameProperty of the itemProperty of the cell:
//			row.disableProperty().bind(
//					EasyBind.select(row.itemProperty()) // start at itemProperty of row
//							.selectObject(NameValuePairModel::nameProperty)  // map to nameProperty of item, if item non-null
//							.map(x -> x.intValue() < 5) // map to BooleanBinding via intValue of value < 5
//							.orElse(false)); // value to use if item was null

			return row;
		});
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

	private <S> TableColumn<S, ColumnDescriptor> createTableColumn(String name, Function<S, ObservableValue<ColumnDescriptor>> propertyMapper) {
		TableColumn<S, ColumnDescriptor> col = new TableColumn<>(name);
		col.setCellValueFactory(cellData -> propertyMapper.apply(cellData.getValue()));
		col.setCellFactory(new Callback<TableColumn<S, ColumnDescriptor>, TableCell<S, ColumnDescriptor>>() {
			@Override
			public TableCell<S, ColumnDescriptor> call(TableColumn<S, ColumnDescriptor> param) {
				return new TableCell<S, ColumnDescriptor>() {
					@Override protected void updateItem(ColumnDescriptor item, boolean empty) {
						if (item == getItem()) return;

						super.updateItem(item, empty);

						if (item != null) {

							//setTextFill(Color.CHOCOLATE);
							if (item.getBackgroundColor() == null) {
								setStyle("");
							}
							else {
								setStyle("-fx-background-color: " + ColorUtil.toWeb(item.getBackgroundColor()));
							}

							setPadding(new Insets(0,4,0,4));

							super.setText(item.toString());
						}
					}

				};
			}
		});
		return col ;
	}

}
