package com.alarmclock.ui.store;

import com.alarmclock.ui.models.AlarmViewModel;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumSet;

public class AlarmStore {

    private final ObservableList<AlarmViewModel> alarms = FXCollections.observableArrayList(
            (AlarmViewModel alarm) -> new Observable[] {
                    alarm.enabledProperty(),
                    alarm.timeProperty(),
                    alarm.labelProperty(),
                    alarm.repeatDaysProperty()
            }
    );

    public AlarmStore() {
        seedSampleData();
    }

    public ObservableList<AlarmViewModel> getAlarms() {
        return alarms;
    }

    public void addAlarm(AlarmViewModel alarm) {
        alarms.add(alarm);
    }

    public void removeAlarm(AlarmViewModel alarm) {
        alarms.remove(alarm);
    }

    private void seedSampleData() {
        AlarmViewModel wake = new AlarmViewModel(
                LocalTime.of(6, 30),
                "Wake Up",
                EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                "Digital",
                10,
                true
        );

        AlarmViewModel gym = new AlarmViewModel(
                LocalTime.of(17, 0),
                "Gym Session",
                EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                "Chime",
                5,
                true
        );

        AlarmViewModel weekend = new AlarmViewModel(
                LocalTime.of(9, 0),
                "Weekend Relax",
                EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
                "Classic",
                15,
                false
        );

        alarms.addAll(wake, gym, weekend);
    }
}