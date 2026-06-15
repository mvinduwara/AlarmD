package com.alarmclock.ui;

import com.alarmclock.ui.store.AlarmStore;
import com.alarmclock.ui.theme.ThemeManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public final class AppContext {

    private static final AppContext INSTANCE = new AppContext();

    private Stage primaryStage;
    private final AlarmStore alarmStore = new AlarmStore();

    private final ObjectProperty<ThemeManager.AppTheme> theme =
            new SimpleObjectProperty<>(ThemeManager.AppTheme.DARK);

    private final BooleanProperty use24HourFormat = new SimpleBooleanProperty(false);
    private final StringProperty defaultSound = new SimpleStringProperty("Classic");
    private final IntegerProperty defaultSnoozeMinutes = new SimpleIntegerProperty(10);

    private AppContext() {
    }

    public static AppContext getInstance() {
        return INSTANCE;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public AlarmStore getAlarmStore() {
        return alarmStore;
    }

    public ObjectProperty<ThemeManager.AppTheme> themeProperty() {
        return theme;
    }

    public ThemeManager.AppTheme getTheme() {
        return theme.get();
    }

    public void setTheme(ThemeManager.AppTheme value) {
        theme.set(value);
    }

    public BooleanProperty use24HourFormatProperty() {
        return use24HourFormat;
    }

    public boolean isUse24HourFormat() {
        return use24HourFormat.get();
    }

    public void setUse24HourFormat(boolean value) {
        use24HourFormat.set(value);
    }

    public StringProperty defaultSoundProperty() {
        return defaultSound;
    }

    public String getDefaultSound() {
        return defaultSound.get();
    }

    public void setDefaultSound(String value) {
        defaultSound.set(value);
    }

    public IntegerProperty defaultSnoozeMinutesProperty() {
        return defaultSnoozeMinutes;
    }

    public int getDefaultSnoozeMinutes() {
        return defaultSnoozeMinutes.get();
    }

    public void setDefaultSnoozeMinutes(int value) {
        defaultSnoozeMinutes.set(value);
    }
}