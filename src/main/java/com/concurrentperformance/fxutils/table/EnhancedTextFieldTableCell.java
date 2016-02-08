package com.concurrentperformance.fxutils.table;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class EnhancedTextFieldTableCell<S,T> extends TableCell<S,T> {


	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/***************************************************************************
	 *                                                                         *
	 * Static cell factories                                                   *
	 *                                                                         *
	 **************************************************************************/


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
			final Consumer<T> postCommitHook, final StringConverter<T> converter) {
		return list -> new EnhancedTextFieldTableCell<>(postCommitHook, converter);
	}


	/***************************************************************************
	 *                                                                         *
	 * Fields                                                                  *
	 *                                                                         *
	 **************************************************************************/

	private TextField textField;
	private StringConverter<T> converter;
	private Consumer<T> postCommitHook;


	/***************************************************************************
	 *                                                                         *
	 * Constructors                                                            *
	 *                                                                         *
	 **************************************************************************/

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
	public EnhancedTextFieldTableCell(Consumer<T> postCommitHook, StringConverter<T> converter) {
		this.getStyleClass().add("text-field-table-cell");
		this.postCommitHook = checkNotNull(postCommitHook);
		this.converter = checkNotNull(converter);
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
				textField = createTextField();
			}

			textField.setText(getItemText());
			setText(null);
			setGraphic(textField);

			textField.selectAll();

			// requesting focus so that key input can immediately go into the
			// TextField (see RT-28132)
			textField.requestFocus();
		}
	}

	private TextField createTextField() {
		final TextField textField = new TextField(getItemText());

		// Use onAction here rather than onKeyReleased (with check for Enter),
		// as otherwise we encounter RT-34685
		textField.setOnAction(event -> {
			doCommitEdit();
			event.consume();
		});

		textField.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				doCancelEdit();
				event.consume();
			} else if (event.getCode() == KeyCode.TAB) {
				doCommitEdit();

				Pair<Integer, TableColumn<S, ?>> nextCell = getNextCellIndex(!event.isShiftDown());
				if (nextCell != null) {
					getTableView().edit(nextCell.getKey(), nextCell.getValue());
				}
			}
		});

		textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				doCommitEdit();
			}
		});

		return textField;
	}

	private void doCommitEdit() {
		if (isEditing()) {
			String newValueText = textField.getText();
			T newValueObject = converter.fromString(newValueText);
			commitEdit(newValueObject);

			postCommitHook.accept(newValueObject);
		}
	}

	/** {@inheritDoc} */
	@Override public void cancelEdit() {
		super.cancelEdit();
		doCancelEdit();
	}

	private void doCancelEdit() {
		setText(getItemText());
		setGraphic(null);
	}

	/** {@inheritDoc} */
	@Override public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (isEmpty()) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getItemText());
					textField.selectAll();
				}
				setText(null);

				setGraphic(textField);
			} else {
				setText(getItemText());
				setGraphic(null);
			}
		}
	}

	private String getItemText() {
		return converter == null ?
				getItem() == null ? "" : getItem().toString() :
				converter.toString(getItem());
	}

	/**
	 *
	 * @param forward true gets the column to the right, false the column to the left of the current column
	 * @return
	 */
	private Pair<Integer, TableColumn<S, ?>> getNextCellIndex(boolean forward) {
		List<TableColumn<S, ?>> columns = new ArrayList<>();
		for (TableColumn<S, ?> column : getTableView().getColumns()) {
			columns.addAll(getLeaves(column));
		}
		//There is no other column that supports editing.
		if (columns.size() < 2) {
			return null;
		}

		int rowIndex = getTableRow().getIndex();

		int columnIndex = columns.indexOf(getTableColumn());
		if (forward) {
			columnIndex++;
			if (columnIndex > columns.size() - 1) {
				columnIndex = 0;
				rowIndex++;
				if (rowIndex >= getTableView().getItems().size()) {
					rowIndex= 0;
				}
			}
		} else {
			columnIndex--;
			if (columnIndex < 0) {
				columnIndex = columns.size() - 1;
				rowIndex--;
				if (rowIndex < 0) {
					rowIndex = getTableView().getItems().size()-1;
				}
			}
		}
		return new Pair<>(rowIndex, columns.get(columnIndex));
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
