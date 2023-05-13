package project.abstractClasses;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class test extends Application {

    private static final double MIN_RADIUS = 10;
    private static final double MAX_RADIUS = 100;
    private static final double PANE_SIZE  = 2 * (MIN_RADIUS + MAX_RADIUS);
    private static final double SCALE_FACTOR = MAX_RADIUS / MIN_RADIUS;
    private static final Duration SCALE_DURATION = Duration.seconds(3);

    @Override
    public void start(Stage stage) {
        final Circle circle = new Circle(MIN_RADIUS, Color.CRIMSON);

        final StackPane root = new StackPane(circle);
        root.setPrefSize(PANE_SIZE, PANE_SIZE);

        stage.setScene(new Scene(root, Color.PALETURQUOISE));
        stage.show();

        animateCircle(circle);
    }

    private void animateCircle(Circle circle) {
        ScaleTransition scaler = new ScaleTransition(
                SCALE_DURATION,
                circle
        );

        scaler.setFromX(1);
        scaler.setToX(SCALE_FACTOR);
        scaler.setFromY(1);
        scaler.setToY(SCALE_FACTOR);

        scaler.setAutoReverse(true);
        scaler.setCycleCount(ScaleTransition.INDEFINITE);
        scaler.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
