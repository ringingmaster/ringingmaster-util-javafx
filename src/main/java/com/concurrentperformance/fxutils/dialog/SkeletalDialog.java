package com.concurrentperformance.fxutils.dialog;

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
public abstract class SkeletalDialog<T> {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private EditMode editMode;
	private Stage stage;
	private Function<T, Boolean> onSuccessHandler;


	public static class Launcher<T> {
		public void showDialog(EditMode editMode, T model, Window owner, URL fxmlResource, List<String> stylesheets, Function<T, Boolean> onSuccessHandler) {
			checkNotNull(editMode);
			checkNotNull(owner);
			checkNotNull(fxmlResource);
			checkNotNull(onSuccessHandler);

			FXMLLoader fxmlLoader = new FXMLLoader(fxmlResource);

			try {
				Scene scene = new Scene(fxmlLoader.load());
				SkeletalDialog controller = fxmlLoader.getController();
				controller.init(editMode, scene, model, owner, stylesheets, onSuccessHandler);

			} catch (IOException e) {
				final Logger log = LoggerFactory.getLogger(this.getClass());
				log.error("Error initialising [" + fxmlResource + "]", e);
			}
		}
	}

	private void init(EditMode editMode, Scene scene, T model, Window owner, List<String> stylesheets, Function<T, Boolean> onSuccessHandler) {
		this.editMode = checkNotNull(editMode);
		this.onSuccessHandler = checkNotNull(onSuccessHandler);

		configureScene(scene, stylesheets);
		buildStage(scene, owner);

		buildDialogStructure(editMode, model);


		if (model != null) {
			populateDialogFromModel(model);
			checkModelFromDialogData();
		}
		stage.showAndWait();
	}

	private void configureScene(Scene scene, List<String> stylesheets) {
		checkNotNull(scene);
		scene.getStylesheets().addAll(stylesheets);
		scene.setOnKeyReleased(event -> {
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
		T result = populateModelFromDialogData();

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

	protected void checkModelFromDialogData() {}
	protected void buildDialogStructure(EditMode editMode, T model) {};
	protected abstract void populateDialogFromModel(T model);
	protected abstract T populateModelFromDialogData();
}
