package com.alarmclock.ui.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class AlarmViewModel {

    private final StringProperty id = new SimpleStringProperty(UUID.randomUUID().toString());
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>(LocalTime.of(7, 0));
    private final StringProperty label = new SimpleStringProperty("Alarm");
    private final BooleanProperty enabled = new SimpleBooleanProperty(true);
    private final ObjectProperty<Set<DayOfWeek>> repeatDays =
            new SimpleObjectProperty<>(EnumSet.noneOf(DayOfWeek.class));
    private final StringProperty soundName = new SimpleStringProperty("Classic");
    private final IntegerProperty snoozeMinutes = new SimpleIntegerProperty(10);

    public AlarmViewModel() {
    }

    public AlarmViewModel(LocalTime time, String label, Set<DayOfWeek> repeatDays,
                          String soundName, int snoozeMinutes, boolean enabled) {
        this.time.set(time);
        this.label.set(label);
        this.repeatDays.set(repeatDays.isEmpty() ? EnumSet.noneOf(DayOfWeek.class) : EnumSet.copyOf(repeatDays));
        this.soundName.set(soundName);
        this.snoozeMinutes.set(snoozeMinutes);
        this.enabled.set(enabled);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String value) {
        id.set(value);
    }

    public LocalTime getTime() {
        return time.get();
    }

    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public void setTime(LocalTime value) {
        time.set(value);
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public void setLabel(String value) {
        label.set(value);
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }

    public void setEnabled(boolean value) {
        enabled.set(value);
    }

    public Set<DayOfWeek> getRepeatDays() {
        return repeatDays.get();
    }

    public ObjectProperty<Set<DayOfWeek>> repeatDaysProperty() {
        return repeatDays;
    }

    public void setRepeatDays(Set<DayOfWeek> value) {
        repeatDays.set(value.isEmpty() ? EnumSet.noneOf(DayOfWeek.class) : EnumSet.copyOf(value));
    }

    public String getSoundName() {
        return soundName.get();
    }

    public StringProperty soundNameProperty() {
        return soundName;
    }

    public void setSoundName(String value) {
        soundName.set(value);
    }

    public int getSnoozeMinutes() {
        return snoozeMinutes.get();
    }

    public IntegerProperty snoozeMinutesProperty() {
        return snoozeMinutes;
    }

    public void setSnoozeMinutes(int value) {
        snoozeMinutes.set(value);
    }

    public String getRepeatSummary() {
        Set<DayOfWeek> days = getRepeatDays();

        if (days.isEmpty()) {
            return "Once";
        }
        if (days.size() == 7) {
            return "Every day";
        }

        Set<DayOfWeek> weekdays = EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
        Set<DayOfWeek> weekend = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

        if (days.equals(weekdays)) {
            return "Weekdays";
        }
        if (days.equals(weekend)) {
            return "Weekends";
        }

        return days.stream()
                .sorted()
                .map(d -> d.getDisplayName(TextStyle.SHORT, Locale.ENGLISH))
                .collect(Collectors.joining(", "));
    }
}