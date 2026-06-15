package com.alarmclock.ui.util;

import com.alarmclock.ui.controllers.AlarmEditorController;
import com.alarmclock.ui.controllers.AlarmRingController;
import com.alarmclock.ui.controllers.SettingsController;
import com.alarmclock.ui.models.AlarmViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.function.Consumer;

public final class SceneNavigator {

    private SceneNavigator() {
    }

    public static void openAlarmEditor(Stage owner, AlarmViewModel existing, Consumer<AlarmViewModel> onSave) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneNavigator.class.getResource("/com/alarmclock/ui/fxml/alarm-editor.fxml"));
            Parent root = loader.load();

            AlarmEditorController controller = loader.getController();
            controller.init(existing, onSave);

            Stage stage = new Stage();
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle(existing == null ? "New Alarm" : "Edit Alarm");

            Scene scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            scene.getStylesheets().add(
                    SceneNavigator.class.getResource("/com/alarmclock/ui/css/app.css").toExternalForm());
            scene.getStylesheets().add(
                    SceneNavigator.class.getResource("/com/alarmclock/ui/css/alarm-editor.css").toExternalForm());

            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to open alarm editor", e);
        }
    }

    public static void openSettings(Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneNavigator.class.getResource("/com/alarmclock/ui/fxml/settings.fxml"));
            Parent root = loader.load();

            SettingsController controller = loader.getController();

            Stage stage = new Stage();
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Settings");

            Scene scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            scene.getStylesheets().add(
                    SceneNavigator.class.getResource("/com/alarmclock/ui/css/app.css").toExternalForm());
            scene.getStylesheets().add(
                    SceneNavigator.class.getResource("/com/alarmclock/ui/css/settings.css").toExternalForm());

            stage.setScene(scene);
            controller.setStage(stage);
            stage.showAndWait();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to open settings", e);
        }
    }

    public static void showAlarmRing(AlarmViewModel alarm) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneNavigator.class.getResource("/com/alarmclock/ui/fxml/alarm-ring.fxml"));
            Parent root = loader.load();

            AlarmRingController controller = loader.getController();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setAlwaysOnTop(true);
            stage.setTitle("Alarm");

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    SceneNavigator.class.getResource("/com/alarmclock/ui/css/app.css").toExternalForm());
            scene.getStylesheets().add(
                    SceneNavigator.class.getResource("/com/alarmclock/ui/css/alarm-ring.css").toExternalForm());

            stage.setScene(scene);
            controller.setStage(stage);
            controller.setAlarm(alarm);

            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);

            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to show alarm ring screen", e);
        }
    }
}