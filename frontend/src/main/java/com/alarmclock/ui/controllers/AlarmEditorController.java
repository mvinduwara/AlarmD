package com.alarmclock.ui.controllers;

import com.alarmclock.ui.AppContext;
import com.alarmclock.ui.models.AlarmViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class AlarmEditorController {

    @FXML private Label titleLabel;
    @FXML private Spinner<Integer> hourSpinner;
    @FXML private Spinner<Integer> minuteSpinner;
    @FXML private TextField labelField;
    @FXML private ToggleButton mondayButton;
    @FXML private ToggleButton tuesdayButton;
    @FXML private ToggleButton wednesdayButton;
    @FXML private ToggleButton thursdayButton;
    @FXML private ToggleButton fridayButton;
    @FXML private ToggleButton saturdayButton;
    @FXML private ToggleButton sundayButton;
    @FXML private ComboBox<String> soundComboBox;
    @FXML private Spinner<Integer> snoozeSpinner;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button deleteButton;

    private Stage stage;
    private AlarmViewModel existingAlarm;
    private Consumer<AlarmViewModel> onSave;
    private Map<DayOfWeek, ToggleButton> dayButtons;

    @FXML
    public void initialize() {
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 7));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 5));
        snoozeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, 60, AppContext.getInstance().getDefaultSnoozeMinutes()));

        soundComboBox.getItems().addAll("Classic", "Digital", "Chime");
        soundComboBox.setValue(AppContext.getInstance().getDefaultSound());

        dayButtons = new EnumMap<>(DayOfWeek.class);
        dayButtons.put(DayOfWeek.MONDAY, mondayButton);
        dayButtons.put(DayOfWeek.TUESDAY, tuesdayButton);
        dayButtons.put(DayOfWeek.WEDNESDAY, wednesdayButton);
        dayButtons.put(DayOfWeek.THURSDAY, thursdayButton);
        dayButtons.put(DayOfWeek.FRIDAY, fridayButton);
        dayButtons.put(DayOfWeek.SATURDAY, saturdayButton);
        dayButtons.put(DayOfWeek.SUNDAY, sundayButton);

        saveButton.setOnAction(e -> save());
        cancelButton.setOnAction(e -> close());
        deleteButton.setOnAction(e -> {
            if (existingAlarm != null) {
                AppContext.getInstance().getAlarmStore().removeAlarm(existingAlarm);
            }
            close();
        });
    }

    public void init(AlarmViewModel existing, Consumer<AlarmViewModel> onSave) {
        this.existingAlarm = existing;
        this.onSave = onSave;

        if (existing != null) {
            titleLabel.setText("Edit Alarm");
            deleteButton.setVisible(true);
            deleteButton.setManaged(true);

            LocalTime time = existing.getTime();
            hourSpinner.getValueFactory().setValue(time.getHour());
            minuteSpinner.getValueFactory().setValue(time.getMinute());
            labelField.setText(existing.getLabel());
            soundComboBox.setValue(existing.getSoundName());
            snoozeSpinner.getValueFactory().setValue(existing.getSnoozeMinutes());

            Set<DayOfWeek> days = existing.getRepeatDays();
            dayButtons.forEach((day, button) -> button.setSelected(days.contains(day)));
        } else {
            titleLabel.setText("New Alarm");
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void save() {
        String label = (labelField.getText() == null || labelField.getText().isBlank())
                ? "Alarm" : labelField.getText().trim();

        LocalTime time = LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue());

        Set<DayOfWeek> selectedDays = EnumSet.noneOf(DayOfWeek.class);
        dayButtons.forEach((day, button) -> {
            if (button.isSelected()) {
                selectedDays.add(day);
            }
        });

        if (existingAlarm == null) {
            AlarmViewModel alarm = new AlarmViewModel(
                    time, label, selectedDays, soundComboBox.getValue(), snoozeSpinner.getValue(), true);
            if (onSave != null) {
                onSave.accept(alarm);
            }
        } else {
            existingAlarm.setTime(time);
            existingAlarm.setLabel(label);
            existingAlarm.setRepeatDays(selectedDays);
            existingAlarm.setSoundName(soundComboBox.getValue());
            existingAlarm.setSnoozeMinutes(snoozeSpinner.getValue());
            if (onSave != null) {
                onSave.accept(existingAlarm);
            }
        }

        close();
    }

    private void close() {
        if (stage != null) {
            stage.close();
        }
    }
}