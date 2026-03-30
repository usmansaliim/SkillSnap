package com.skillsnap.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class AlertUtils {

    public static void showError(String title,
                                 String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleAlert(alert);
        alert.showAndWait();
    }

    public static void showInfo(String title,
                                String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleAlert(alert);
        alert.showAndWait();
    }

    public static boolean showConfirmation(String title,
                                           String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleAlert(alert);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() &&
                result.get() == ButtonType.OK;
    }

    private static void styleAlert(Alert alert) {
        // Apply dark theme to dialogs
        alert.getDialogPane().setStyle(
                "-fx-background-color: #161B22;" +
                        "-fx-border-color: #30363D;");
        alert.getDialogPane().lookup(".content.label")
                .setStyle("-fx-text-fill: #C9D1D9;");
    }
}
