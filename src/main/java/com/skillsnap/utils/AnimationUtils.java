package com.skillsnap.utils;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtils {

    // ── Fade in from invisible ────────────────────────────────
    public static void fadeIn(Node node) {
        node.setOpacity(0);
        FadeTransition ft =
                new FadeTransition(Duration.millis(400), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    // ── Slide in from right ───────────────────────────────────
    public static void slideInFromRight(Node node) {
        node.setTranslateX(60);
        node.setOpacity(0);

        TranslateTransition tt =
                new TranslateTransition(
                        Duration.millis(350), node);
        tt.setFromX(60);
        tt.setToX(0);

        FadeTransition ft =
                new FadeTransition(Duration.millis(350), node);
        ft.setFromValue(0);
        ft.setToValue(1);

        ParallelTransition pt =
                new ParallelTransition(tt, ft);
        pt.play();
    }

    // ── Bounce effect (for badge unlock) ─────────────────────
    public static void bounce(Node node) {
        ScaleTransition st =
                new ScaleTransition(Duration.millis(150), node);
        st.setFromX(1.0); st.setToX(1.2);
        st.setFromY(1.0); st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    // ── Shake effect (for wrong answer) ──────────────────────
    public static void shake(Node node) {
        TranslateTransition tt =
                new TranslateTransition(
                        Duration.millis(60), node);
        tt.setFromX(0); tt.setToX(10);
        tt.setAutoReverse(true);
        tt.setCycleCount(4);
        tt.setOnFinished(e -> node.setTranslateX(0));
        tt.play();
    }

    // ── Pulse effect (for timer warning) ─────────────────────
    public static void pulse(Node node) {
        ScaleTransition st =
                new ScaleTransition(Duration.millis(500), node);
        st.setFromX(1.0); st.setToX(1.05);
        st.setFromY(1.0); st.setToY(1.05);
        st.setAutoReverse(true);
        st.setCycleCount(Timeline.INDEFINITE);
        st.play();
    }

    // ── Count up animation (for score reveal) ────────────────
    public static void countUp(javafx.scene.text.Text text,
                               int from, int to,
                               int durationMs) {
        final int[] current = {from};
        int steps = to - from;
        int intervalMs = steps > 0 ?
                durationMs / steps : durationMs;

        Timeline tl = new Timeline(new KeyFrame(
                Duration.millis(Math.max(intervalMs, 16)),
                e -> {
                    current[0] += 1;
                    text.setText(String.valueOf(current[0]));
                    if (current[0] >= to) {
                        ((Timeline)e.getSource()).stop();
                    }
                }));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }
}
