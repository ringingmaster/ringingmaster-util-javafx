package com.concurrentperformance.fxutils.dialog;

import com.concurrentperformance.ringingmaster.fxui.desktop.RingingMasterDesktopApp;
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
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO Comments
 *
 * @author Lake
 */
public abstract class SkeletalDialog<T> {

	private final static Logger log = LoggerFactory.getLogger(CallEditor.class);

	protected EditMode editMode;
	protected Stage stage;
	private Function<T, Boolean> onSuccessHandler;


	protected static class Launcher<T> {
		public void showDialog(EditMode editMode, T model, Window owner, String fxml, Function<T, Boolean> onSuccessHandler) {
			checkNotNull(editMode);
			checkNotNull(owner);
			checkNotNull(fxml);
			checkNotNull(onSuccessHandler);

			FXMLLoader fxmlLoader = new FXMLLoader(NotationEditorDialog.class.getResource(fxml));

			try {
				Scene scene = new Scene(fxmlLoader.load());
				SkeletalDialog controller = fxmlLoader.getController();
				controller.init(editMode, scene, model, owner, onSuccessHandler);

			} catch (IOException e) {
				log.error("Error initialising NotationEditorDialog", e);
			}
		}
	}

	private void init(EditMode editMode, Scene scene, T model, Window owner, Function<T, Boolean> onSuccessHandler) {
		this.editMode = editMode;
		this.onSuccessHandler = onSuccessHandler;

		configureScene(scene);
		buildStage(scene, owner);

		if (model != null) {
			buildDialogDataFromModel(model);
		}
		stage.showAndWait();
	}

	private void configureScene(Scene scene) {
		checkNotNull(scene);
		scene.getStylesheets().add(RingingMasterDesktopApp.STYLESHEET);
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

	@FXML
	private void OnOk() {
		T result = buildModelFromDialogData();

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


	protected abstract void buildDialogDataFromModel(T model);
	protected abstract T buildModelFromDialogData();
}
