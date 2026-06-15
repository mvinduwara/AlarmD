module com.alarmclock.core {
    requires java.sql;
    requires java.desktop;
    requires com.h2database;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;

    exports com.alarmclock.core.model;
    exports com.alarmclock.core.service;
    exports com.alarmclock.core.events;
    exports com.alarmclock.core.db;
}