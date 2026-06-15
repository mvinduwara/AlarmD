package com.alarmclock.ui.controllers;

import com.alarmclock.ui.AppContext;
import com.alarmclock.ui.models.AlarmViewModel;
import com.alarmclock.ui.util.TimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalTime;

public class AlarmRingController {

    @FXML private Label timeLabel;
    @FXML private Label labelLabel;
    @FXML private Button snoozeButton;
    @FXML private Button dismissButton;

    private Stage stage;
    private AlarmViewModel alarm;
    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        snoozeButton.setOnAction(e -> snooze());
        dismissButton.setOnAction(e -> dismiss());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAlarm(AlarmViewModel alarm) {
        this.alarm = alarm;
        boolean use24h = AppContext.getInstance().isUse24HourFormat();
        timeLabel.setText(TimeFormatter.format(LocalTime.now().withSecond(0), use24h));
        labelLabel.setText(alarm.getLabel());
        playSound(alarm.getSoundName());
    }

    private void playSound(String soundName) {
        try {
            String fileName = "/com/alarmclock/ui/sounds/" + soundName.toLowerCase() + ".mp3";
            URL resource = getClass().getResource(fileName);
            if (resource == null) {
                return;
            }
            Media media = new Media(resource.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } catch (Exception ignored) {
            // Audio is optional; ring screen remains functional without a sound file.
        }
    }

    private void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    private void snooze() {
        stopSound();
        if (stage != null) {
            stage.close();
        }
    }

    private void dismiss() {
        stopSound();
        if (alarm != null && alarm.getRepeatDays().isEmpty()) {
            alarm.setEnabled(false);
        }
        if (stage != null) {
            stage.close();
        }
    }
}