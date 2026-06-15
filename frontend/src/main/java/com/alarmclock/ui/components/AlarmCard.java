package com.alarmclock.ui.components;

import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.theme.Styles;
import com.alarmclock.ui.AppContext;
import com.alarmclock.ui.models.AlarmViewModel;
import com.alarmclock.ui.util.TimeFormatter;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

public class AlarmCard extends HBox {

    private final AlarmViewModel alarm;
    private final Label timeLabel = new Label();

    public AlarmCard(AlarmViewModel alarm, Runnable onEdit, Consumer<AlarmViewModel> onDelete) {
        this.alarm = alarm;

        getStyleClass().add("alarm-card");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(16);
        setPadding(new Insets(16, 20, 16, 20));

        VBox infoBox = new VBox(4);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        timeLabel.getStyleClass().addAll(Styles.TITLE_1, "alarm-time");
        updateTimeLabel();

        Label labelLabel = new Label(alarm.getLabel());
        labelLabel.getStyleClass().addAll(Styles.TEXT_BOLD, "alarm-label");

        Label repeatLabel = new Label(alarm.getRepeatSummary());
        repeatLabel.getStyleClass().addAll(Styles.TEXT_MUTED, Styles.TEXT_SMALL, "alarm-repeat");

        infoBox.getChildren().addAll(timeLabel, labelLabel, repeatLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ToggleSwitch toggleSwitch = new ToggleSwitch();
        toggleSwitch.setSelected(alarm.isEnabled());
        toggleSwitch.selectedProperty().bindBidirectional(alarm.enabledProperty());

        Button editButton = new Button();
        editButton.setGraphic(new FontIcon(FontAwesomeSolid.PEN));
        editButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, "alarm-action-button");
        editButton.setOnAction(e -> onEdit.run());

        Button deleteButton = new Button();
        deleteButton.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
        deleteButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.DANGER, "alarm-action-button");
        deleteButton.setOnAction(e -> onDelete.accept(alarm));

        VBox actionsBox = new VBox(8, editButton, deleteButton);
        actionsBox.setAlignment(Pos.CENTER);

        getChildren().addAll(infoBox, spacer, toggleSwitch, actionsBox);

        alarm.timeProperty().addListener((obs, oldVal, newVal) -> updateTimeLabel());
        alarm.labelProperty().addListener((obs, oldVal, newVal) -> labelLabel.setText(newVal));
        alarm.repeatDaysProperty().addListener((obs, oldVal, newVal) -> repeatLabel.setText(alarm.getRepeatSummary()));
        AppContext.getInstance().use24HourFormatProperty().addListener((obs, oldVal, newVal) -> updateTimeLabel());

        opacityProperty().bind(
                Bindings.when(alarm.enabledProperty())
                        .then(1.0)
                        .otherwise(0.45)
        );
    }

    private void updateTimeLabel() {
        timeLabel.setText(TimeFormatter.format(alarm.getTime(), AppContext.getInstance().isUse24HourFormat()));
    }
}