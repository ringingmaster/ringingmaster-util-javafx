package com.concurrentperformance.fxutils.table;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class EnhancedTextFieldTableCell<S,T> extends TableCell<S,T> {

	/***************************************************************************
	 *                                                                         *
	 * Static cell factories                                                   *
	 *                                                                         *
	 **************************************************************************/

	/**
	 * Provides a {@link TextField} that allows editing of the cell content when
	 * the cell is double-clicked, or when
	 * {@link TableView#edit(int, javafx.scene.control.TableColumn)} is called.
	 * This method will only  work on {@link TableColumn} instances which are of
	 * type String.
	 *
	 * @return A {@link Callback} that can be inserted into the
	 *      {@link TableColumn#cellFactoryProperty() cell factory property} of a
	 *      TableColumn, that enables textual editing of the content.
	 */
	public static <S> Callback<TableColumn<S,String>, TableCell<S,String>> forTableColumn() {
		return forTableColumn(new DefaultStringConverter());
	}

	/**
	 * Provides a {@link TextField} that allows editing of the cell content when
	 * the cell is double-clicked, or when
	 * {@link TableView#edit(int, javafx.scene.control.TableColumn) } is called.
	 * This method will work  on any {@link TableColumn} instance, regardless of
	 * its generic type. However, to enable this, a {@link StringConverter} must
	 * be provided that will convert the given String (from what the user typed
	 * in) into an instance of type T. This item will then be passed along to the
	 * {@link TableColumn#onEditCommitProperty()} callback.
	 *
	 * @param converter A {@link StringConverter} that can convert the given String
	 *      (from what the user typed in) into an instance of type T.
	 * @return A {@link Callback} that can be inserted into the
	 *      {@link TableColumn#cellFactoryProperty() cell factory property} of a
	 *      TableColumn, that enables textual editing of the content.
	 */
	public static <S,T> Callback<TableColumn<S,T>, TableCell<S,T>> forTableColumn(
			final StringConverter<T> converter) {
		return list -> new EnhancedTextFieldTableCell<S,T>(converter);
	}


	/***************************************************************************
	 *                                                                         *
	 * Fields                                                                  *
	 *                                                                         *
	 **************************************************************************/

	private TextField textField;



	/***************************************************************************
	 *                                                                         *
	 * Constructors                                                            *
	 *                                                                         *
	 **************************************************************************/

	/**
	 * Creates a default TextFieldTableCell with a null converter. Without a
	 * {@link StringConverter} specified, this cell will not be able to accept
	 * input from the TextField (as it will not know how to convert this back
	 * to the domain object). It is therefore strongly encouraged to not use
	 * this constructor unless you intend to set the converter separately.
	 */
	public EnhancedTextFieldTableCell() {
		this(null);
	}

	/**
	 * Creates a TextFieldTableCell that provides a {@link TextField} when put
	 * into editing mode that allows editing of the cell content. This method
	 * will work on any TableColumn instance, regardless of its generic type.
	 * However, to enable this, a {@link StringConverter} must be provided that
	 * will convert the given String (from what the user typed in) into an
	 * instance of type T. This item will then be passed along to the
	 * {@link TableColumn#onEditCommitProperty()} callback.
	 *
	 * @param converter A {@link StringConverter converter} that can convert
	 *      the given String (from what the user typed in) into an instance of
	 *      type T.
	 */
	public EnhancedTextFieldTableCell(StringConverter<T> converter) {
		this.getStyleClass().add("text-field-table-cell");
		setConverter(converter);
	}



	/***************************************************************************
	 *                                                                         *
	 * Properties                                                              *
	 *                                                                         *
	 **************************************************************************/

	// --- converter
	private ObjectProperty<StringConverter<T>> converter =
			new SimpleObjectProperty<StringConverter<T>>(this, "converter");

	/**
	 * The {@link StringConverter} property.
	 */
	public final ObjectProperty<StringConverter<T>> converterProperty() {
		return converter;
	}

	/**
	 * Sets the {@link StringConverter} to be used in this cell.
	 */
	public final void setConverter(StringConverter<T> value) {
		converterProperty().set(value);
	}

	/**
	 * Returns the {@link StringConverter} used in this cell.
	 */
	public final StringConverter<T> getConverter() {
		return converterProperty().get();
	}



	/***************************************************************************
	 *                                                                         *
	 * Public API                                                              *
	 *                                                                         *
	 **************************************************************************/

	/** {@inheritDoc} */
	@Override public void startEdit() {
		if (! isEditable()
				|| ! getTableView().isEditable()
				|| ! getTableColumn().isEditable()) {
			return;
		}
		super.startEdit();

		if (isEditing()) {
			if (textField == null) {
				textField = createTextField(this, getConverter());
			}

			startEdit(this, getConverter(), null, null, textField);
		}
	}

	/** {@inheritDoc} */
	@Override public void cancelEdit() {
		super.cancelEdit();
		cancelEdit(this, getConverter(), null);
	}

	/** {@inheritDoc} */
	@Override public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		updateItem(this, getConverter(), null, null, textField);
	}

	static <T> void updateItem(final Cell<T> cell,
	                           final StringConverter<T> converter,
	                           final HBox hbox,
	                           final Node graphic,
	                           final TextField textField) {
		if (cell.isEmpty()) {
			cell.setText(null);
			cell.setGraphic(null);
		} else {
			if (cell.isEditing()) {
				if (textField != null) {
					textField.setText(getItemText(cell, converter));
				}
				cell.setText(null);

				if (graphic != null) {
					hbox.getChildren().setAll(graphic, textField);
					cell.setGraphic(hbox);
				} else {
					cell.setGraphic(textField);
				}
			} else {
				cell.setText(getItemText(cell, converter));
				cell.setGraphic(graphic);
			}
		}
	}

	private static <T> String getItemText(Cell<T> cell, StringConverter<T> converter) {
		return converter == null ?
				cell.getItem() == null ? "" : cell.getItem().toString() :
				converter.toString(cell.getItem());
	}

	static <T> void startEdit(final Cell<T> cell,
	                          final StringConverter<T> converter,
	                          final HBox hbox,
	                          final Node graphic,
	                          final TextField textField) {
		if (textField != null) {
			textField.setText(getItemText(cell, converter));
		}
		cell.setText(null);

		if (graphic != null) {
			hbox.getChildren().setAll(graphic, textField);
			cell.setGraphic(hbox);
		} else {
			cell.setGraphic(textField);
		}

		textField.selectAll();

		// requesting focus so that key input can immediately go into the
		// TextField (see RT-28132)
		textField.requestFocus();
	}

	static <T> void cancelEdit(Cell<T> cell, final StringConverter<T> converter, Node graphic) {
		cell.setText(getItemText(cell, converter));
		cell.setGraphic(graphic);
	}

	<T> TextField createTextField(final Cell<T> cell, final StringConverter<T> converter) {
		final TextField textField = new TextField(getItemText(cell, converter));

		// Use onAction here rather than onKeyReleased (with check for Enter),
		// as otherwise we encounter RT-34685
		textField.setOnAction(event -> {
			if (converter == null) {
				throw new IllegalStateException(
						"Attempting to convert text input into Object, but provided "
								+ "StringConverter is null. Be sure to set a StringConverter "
								+ "in your cell factory.");
			}
			cell.commitEdit(converter.fromString(textField.getText()));
			event.consume();
		});

		textField.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				cancelEdit(cell, converter, null);
				event.consume();
			} else if (event.getCode() == KeyCode.TAB) {
				if (converter == null) {
					throw new IllegalStateException(
							"Attempting to convert text input into Object, but provided "
									+ "StringConverter is null. Be sure to set a StringConverter "
									+ "in your cell factory.");
				}
				cell.commitEdit(converter.fromString(textField.getText()));
				TableColumn nextColumn = getNextColumn(!event.isShiftDown());
				if (nextColumn != null) {
					getTableView().edit(getTableRow().getIndex(), nextColumn);
				}
			}
		});


		textField.setOnKeyReleased(t -> {
			if (t.getCode() == KeyCode.ESCAPE) {
				cell.cancelEdit();
				t.consume();
			}
		});
		return textField;
	}

	/**
	 *
	 * @param forward true gets the column to the right, false the column to the left of the current column
	 * @return
	 */
	private TableColumn<S, ?> getNextColumn(boolean forward) {
		List<TableColumn<S, ?>> columns = new ArrayList<>();
		for (TableColumn<S, ?> column : getTableView().getColumns()) {
			columns.addAll(getLeaves(column));
		}
		//There is no other column that supports editing.
		if (columns.size() < 2) {
			return null;
		}
		int currentIndex = columns.indexOf(getTableColumn());
		int nextIndex = currentIndex;
		if (forward) {
			nextIndex++;
			if (nextIndex > columns.size() - 1) {
				nextIndex = 0;
			}
		} else {
			nextIndex--;
			if (nextIndex < 0) {
				nextIndex = columns.size() - 1;
			}
		}
		return columns.get(nextIndex);
	}

	private List<TableColumn<S, ?>> getLeaves(TableColumn<S, ?> root) {
		List<TableColumn<S, ?>> columns = new ArrayList<>();
		if (root.getColumns().isEmpty()) {
			//We only want the leaves that are editable.
			if (root.isEditable()) {
				columns.add(root);
			}
			return columns;
		} else {
			for (TableColumn<S, ?> column : root.getColumns()) {
				columns.addAll(getLeaves(column));
			}
			return columns;
		}
	}
}
