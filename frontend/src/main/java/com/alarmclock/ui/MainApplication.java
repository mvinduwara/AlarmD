package com.alarmclock.ui;

import com.alarmclock.ui.theme.ThemeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        ThemeManager.apply(ThemeManager.AppTheme.DARK);
        AppContext.getInstance().setTheme(ThemeManager.AppTheme.DARK);
        AppContext.getInstance().setPrimaryStage(stage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/alarmclock/ui/fxml/dashboard.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 420, 720);
        scene.getStylesheets().add(getClass().getResource("/com/alarmclock/ui/css/app.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/com/alarmclock/ui/css/dashboard.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/com/alarmclock/ui/css/alarm-card.css").toExternalForm());

        stage.setTitle("Alarm Clock");
        stage.setMinWidth(380);
        stage.setMinHeight(640);
        stage.setScene(scene);

        try {
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/alarmclock/ui/images/app-icon.png")));
        } catch (Exception ignored) {
            // App icon is optional; the application still runs without it.
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}