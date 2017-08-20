package org.ringingmaster.fxutils.dialog;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO Comments
 *
 * @author Lake
 */
public class SceneLauncher {

    private Stage stage;

    public SceneLauncher(Parent root, List<String> stylesheets, Window owner, String title) {
        checkNotNull(root);
        checkNotNull(stylesheets);
        checkNotNull(title);

        Scene scene = new Scene(root);
        configureScene(scene, stylesheets);
        buildStage(scene, owner, title);
        getStage().showAndWait();
    }

    protected void configureScene(Scene scene, List<String> stylesheets) {
        checkNotNull(scene);
        scene.getStylesheets().addAll(stylesheets);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                close();
            }
        });
    }

    protected void buildStage(Scene scene, Window owner, String title) {
        stage = new Stage(StageStyle.DECORATED);
        stage.initOwner(owner);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);

    }

    public Stage getStage() {
        return stage;
    }

    protected void close() {
        stage.close();
    }

}
