package org.ringingmaster.util.javafx.dialog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO Comments
 *
 * @author Lake
 */
public abstract class SkeletalDialog<MODEL> {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private EditMode editMode;
	private Stage stage;
	private Function<MODEL, Boolean> onSuccessHandler;


	public static class DialogBuilder<S_MODEL, S_CTRL extends SkeletalDialog<S_MODEL>> {
		public S_CTRL buildDialog(EditMode editMode, S_MODEL model, Window owner, URL fxmlResource, List<String> stylesheets, Function<S_MODEL, Boolean> onSuccessHandler) {
			checkNotNull(editMode);
			checkNotNull(owner);
			checkNotNull(fxmlResource);
			checkNotNull(onSuccessHandler);

			FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);

			try {
				Scene scene = new Scene(fxmlLoader.load());
				S_CTRL controller = fxmlLoader.getController();
				controller.init(editMode, scene, model, owner, stylesheets, onSuccessHandler);
				return controller;

			} catch (IOException e) {
				final Logger log = LoggerFactory.getLogger(this.getClass());
				log.error("Error initialising [" + fxmlResource + "]", e);
				throw new RuntimeException(e);
			}
		}
	}

	protected void init(EditMode editMode, Scene scene, MODEL model, Window owner, List<String> stylesheets, Function<MODEL, Boolean> onSuccessHandler) {
		this.editMode = checkNotNull(editMode);
		this.onSuccessHandler = checkNotNull(onSuccessHandler);

		configureScene(scene, stylesheets);
		buildStage(scene, owner);

		initialiseDialog(editMode, model);


		if (model != null) {
			populateDialogFromModel(model);
		}
	}

	protected void showAndWait() {
		stage.showAndWait();
	}

	private void configureScene(Scene scene, List<String> stylesheets) {
		checkNotNull(scene);
		scene.getStylesheets().addAll(stylesheets);
		scene.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ESCAPE)) {
				OnCancel();
			}
		});
	}


	private void buildStage(Scene scene, Window owner) {
		stage = new Stage(StageStyle.DECORATED);
		stage.initOwner(owner);
		stage.setScene(scene);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setTitle(editMode.getEditText() + ": ");
	}

	public Stage getStage() {
		return stage;
	}

	public EditMode getEditMode() {
		return editMode;
	}

	@FXML
	private void OnOk() {
		MODEL result = buildModelFromDialogData();

		try {
			Boolean success = onSuccessHandler.apply(result);
			if (success) {
				close();
			}
		} catch (RuntimeException e) {
			log.error("",e);
		}
	}

	@FXML
	protected void OnCancel() {
		close();
	}

	protected void close() {
		stage.close();
	}

	protected void initialiseDialog(EditMode editMode, MODEL model) {};

	protected abstract void populateDialogFromModel(MODEL model);
	protected abstract MODEL buildModelFromDialogData();
}
