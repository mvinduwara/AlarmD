package com.alarmclock.ui.theme;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;

public final class ThemeManager {

    public enum AppTheme {
        LIGHT, DARK
    }

    private ThemeManager() {
    }

    public static void apply(AppTheme theme) {
        switch (theme) {
            case LIGHT -> Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            case DARK -> Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        }
    }

    public static AppTheme toggle(AppTheme current) {
        AppTheme next = current == AppTheme.DARK ? AppTheme.LIGHT : AppTheme.DARK;
        apply(next);
        return next;
    }
}