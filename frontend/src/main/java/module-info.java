module com.alarmclock.ui {
    requires com.alarmclock.core;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;

    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome6;
    requires org.kordamp.ikonli.material2;
    requires org.controlsfx.controls;

    opens com.alarmclock.ui to javafx.fxml;
    opens com.alarmclock.ui.controllers to javafx.fxml;
    opens com.alarmclock.ui.components to javafx.fxml;
    opens com.alarmclock.ui.models to javafx.base;

    exports com.alarmclock.ui;
}