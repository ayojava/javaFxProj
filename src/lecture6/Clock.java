/*
 * This file can be freely used for educational purposes.
 * However, you must use critical thinking 
 * when using the file as an example.
 */
package lecture6;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author Jaakko Hakulinen <jaakko.hakulinen at tuni.fi>
 */
public class Clock extends Application {

    Canvas canvas = new Canvas(300, 300);
    Timer timer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        canvas = new Canvas(300, 300);
        drawClock(canvas.getGraphicsContext2D(), new Date());
        VBox vBox = new VBox(canvas);
        var scene = new Scene(vBox);

        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(null);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Bounds contentSize = canvas.getBoundsInParent();
        stage.setX(bounds.getMinX() + bounds.getWidth() - contentSize.getWidth() - 32);
        stage.setY(bounds.getMinY() + 32);

        stage.setScene(scene);
        stage.show();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        drawClock(canvas.getGraphicsContext2D(), new Date());
                    }
                });
            }
        }, 1000, 1000);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent evt) {
                timer.cancel();
            }
        });
    }

    void drawClock(GraphicsContext ctx, Date time) {
        double w = ctx.getCanvas().getWidth();
        double h = ctx.getCanvas().getHeight();
        ctx.clearRect(0, 0, w, h);

        // Hour marks 
        ctx.save();;
        ctx.translate(w / 2, h / 2); // origin to centre of the canvas
        ctx.save();
        for (int i = 0; i < 12; i++) {
            ctx.fillRect(-w / 24, h / 2 - h / 12, w / 12, h / 12);
            ctx.rotate(360 / 12); // rotate (around centre)
        }
        ctx.restore();

        // clock hands
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        ctx.save();
        ctx.rotate(c.get(Calendar.HOUR) * 360 / 12 + Calendar.MINUTE * 360 / 12 / 60 + 180); // hour hand, rotate per time
        drawHand(ctx, w / 12, h / 12 * 4);
        ctx.restore();
        ctx.save();
        ctx.rotate(c.get(Calendar.MINUTE) * 360 / 60 + 180); // minutes hand, rotate per time
        drawHand(ctx, w / 16, h / 12 * 5);
        ctx.restore();
        ctx.save();
        ctx.rotate(c.get(Calendar.SECOND) * 360 / 60 + 180); // seconds hand, rotate per time
        drawHand(ctx, w / 48, h / 12 * 5);
        ctx.restore();

        ctx.restore();
    }

    void drawHand(GraphicsContext ctx, double width, double length) {
        ctx.beginPath();
        ctx.moveTo(width / 2, 0);
        ctx.lineTo(-width / 2, 0);
        ctx.lineTo(0, length);
        ctx.closePath();
        ctx.fill();
    }
}
