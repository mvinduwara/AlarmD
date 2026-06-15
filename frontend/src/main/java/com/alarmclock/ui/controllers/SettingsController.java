package com.alarmclock.ui.controllers;

import atlantafx.base.controls.ToggleSwitch;
import com.alarmclock.ui.AppContext;
import com.alarmclock.ui.theme.ThemeManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class SettingsController {

    @FXML private ToggleSwitch timeFormatSwitch;
    @FXML private ToggleSwitch darkThemeSwitch;
    @FXML private ComboBox<String> defaultSoundComboBox;
    @FXML private Spinner<Integer> defaultSnoozeSpinner;
    @FXML private Button closeButton;

    private Stage stage;

    @FXML
    public void initialize() {
        AppContext context = AppContext.getInstance();

        timeFormatSwitch.setSelected(context.isUse24HourFormat());
        timeFormatSwitch.selectedProperty().addListener((obs, oldVal, newVal) ->
                context.setUse24HourFormat(newVal));

        darkThemeSwitch.setSelected(context.getTheme() == ThemeManager.AppTheme.DARK);
        darkThemeSwitch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            ThemeManager.AppTheme theme = newVal ? ThemeManager.AppTheme.DARK : ThemeManager.AppTheme.LIGHT;
            ThemeManager.apply(theme);
            context.setTheme(theme);
        });

        defaultSoundComboBox.getItems().addAll("Classic", "Digital", "Chime");
        defaultSoundComboBox.setValue(context.getDefaultSound());
        defaultSoundComboBox.valueProperty().addListener((obs, oldVal, newVal) ->
                context.setDefaultSound(newVal));

        defaultSnoozeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, 60, context.getDefaultSnoozeMinutes()));
        defaultSnoozeSpinner.valueProperty().addListener((obs, oldVal, newVal) ->
                context.setDefaultSnoozeMinutes(newVal));

        closeButton.setOnAction(e -> close());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void close() {
        if (stage != null) {
            stage.close();
        }
    }
}