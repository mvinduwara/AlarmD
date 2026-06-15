package com.alarmclock.ui.controllers;

import com.alarmclock.ui.AppContext;
import com.alarmclock.ui.components.AlarmCard;
import com.alarmclock.ui.models.AlarmViewModel;
import com.alarmclock.ui.theme.ThemeManager;
import com.alarmclock.ui.util.SceneNavigator;
import com.alarmclock.ui.util.TimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

public class DashboardController {

    @FXML private Label clockLabel;
    @FXML private Label dateLabel;
    @FXML private Label nextAlarmLabel;
    @FXML private Label emptyStateLabel;
    @FXML private ListView<AlarmViewModel> alarmListView;
    @FXML private Button addAlarmButton;
    @FXML private Button settingsButton;
    @FXML private Button themeToggleButton;
    @FXML private Button testRingButton;
    @FXML private FontIcon themeIcon;

    @FXML
    public void initialize() {
        startClock();
        setupAlarmList();
        setupButtons();
        refreshNextAlarmBanner();
        updateEmptyState();

        AppContext.getInstance().getAlarmStore().getAlarms()
                .addListener((ListChangeListener<AlarmViewModel>) change -> {
                    refreshNextAlarmBanner();
                    updateEmptyState();
                });

        AppContext.getInstance().use24HourFormatProperty().addListener((obs, oldVal, newVal) -> {
            updateClock();
            refreshNextAlarmBanner();
        });
    }

    private void startClock() {
        updateClock();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateClock() {
        LocalDateTime now = LocalDateTime.now();
        boolean use24h = AppContext.getInstance().isUse24HourFormat();
        clockLabel.setText(TimeFormatter.format(now.toLocalTime().withSecond(0).withNano(0), use24h));
        dateLabel.setText(now.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)));
    }

    private void setupAlarmList() {
        alarmListView.setItems(AppContext.getInstance().getAlarmStore().getAlarms());
        alarmListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(AlarmViewModel alarm, boolean empty) {
                super.updateItem(alarm, empty);
                if (empty || alarm == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    AlarmCard card = new AlarmCard(
                            alarm,
                            () -> SceneNavigator.openAlarmEditor(
                                    AppContext.getInstance().getPrimaryStage(),
                                    alarm,
                                    updated -> alarmListView.refresh()
                            ),
                            toDelete -> AppContext.getInstance().getAlarmStore().removeAlarm(toDelete)
                    );
                    setGraphic(card);
                    setText(null);
                }
            }
        });
    }

    private void setupButtons() {
        addAlarmButton.setOnAction(e -> SceneNavigator.openAlarmEditor(
                AppContext.getInstance().getPrimaryStage(),
                null,
                newAlarm -> AppContext.getInstance().getAlarmStore().addAlarm(newAlarm)
        ));

        settingsButton.setOnAction(e -> SceneNavigator.openSettings(AppContext.getInstance().getPrimaryStage()));

        themeIcon.setIconCode(AppContext.getInstance().getTheme() == ThemeManager.AppTheme.DARK
                ? FontAwesomeSolid.MOON : FontAwesomeSolid.SUN);

        themeToggleButton.setOnAction(e -> {
            ThemeManager.AppTheme current = AppContext.getInstance().getTheme();
            ThemeManager.AppTheme next = ThemeManager.toggle(current);
            AppContext.getInstance().setTheme(next);
            themeIcon.setIconCode(next == ThemeManager.AppTheme.DARK
                    ? FontAwesomeSolid.MOON : FontAwesomeSolid.SUN);
        });

        testRingButton.setOnAction(e -> {
            AlarmViewModel preview = alarmListView.getItems().stream()
                    .findFirst()
                    .orElseGet(AlarmViewModel::new);
            SceneNavigator.showAlarmRing(preview);
        });
    }

    private void updateEmptyState() {
        boolean empty = AppContext.getInstance().getAlarmStore().getAlarms().isEmpty();
        emptyStateLabel.setVisible(empty);
        emptyStateLabel.setManaged(empty);
        alarmListView.setVisible(!empty);
    }

    private void refreshNextAlarmBanner() {
        var alarms = AppContext.getInstance().getAlarmStore().getAlarms();
        boolean use24h = AppContext.getInstance().isUse24HourFormat();

        alarms.stream()
                .filter(AlarmViewModel::isEnabled)
                .min(Comparator.comparingLong(this::minutesUntilNextTrigger))
                .ifPresentOrElse(
                        nextAlarm -> {
                            long minutesUntil = minutesUntilNextTrigger(nextAlarm);
                            String time = TimeFormatter.format(nextAlarm.getTime(), use24h);
                            String countdown = TimeFormatter.formatCountdown(java.time.Duration.ofMinutes(minutesUntil));
                            nextAlarmLabel.setText(
                                    "Next: " + nextAlarm.getLabel() + " at " + time + " (in " + countdown + ")");
                        },
                        () -> nextAlarmLabel.setText("No upcoming alarms")
                );
    }

    private long minutesUntilNextTrigger(AlarmViewModel alarm) {
        LocalDateTime now = LocalDateTime.now();
        LocalTime alarmTime = alarm.getTime();
        var repeatDays = alarm.getRepeatDays();

        if (repeatDays.isEmpty()) {
            LocalDateTime candidate = LocalDateTime.of(now.toLocalDate(), alarmTime);
            if (candidate.isBefore(now)) {
                candidate = candidate.plusDays(1);
            }
            return java.time.Duration.between(now, candidate).toMinutes();
        }

        for (int i = 0; i < 8; i++) {
            LocalDate candidateDate = now.toLocalDate().plusDays(i);
            DayOfWeek dow = candidateDate.getDayOfWeek();
            if (!repeatDays.contains(dow)) {
                continue;
            }
            LocalDateTime candidate = LocalDateTime.of(candidateDate, alarmTime);
            if (candidate.isAfter(now)) {
                return java.time.Duration.between(now, candidate).toMinutes();
            }
        }
        return Long.MAX_VALUE;
    }
}