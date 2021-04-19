/*
 * This file can be freely used for educational purposes.
 * However, you must use critical thinking 
 * when using the file as an example.
 */
package lecture5;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;

/**
 *
 * @author Jaakko Hakulinen <jaakko.hakulinen at tuni.fi>
 */
public class Bezier extends Application {

    public static final int CPW = 4; // control point width (visual)

    Canvas canvas;
    Affine transform;
    double position = 0.0; // interpolation position betwee 0.0 and 1.0
    double zoom = 1;
    Point2D[] controlPoints = new Point2D[]{
        new Point2D(500, 200),
        new Point2D(400, 400),
        new Point2D(100, 300),
        new Point2D(200, 200)
    };
    int draggedPoint = -1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        canvas = new Canvas(600, 600);
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        drawBezier(ctx);
        Slider positionSlider = new Slider();
        positionSlider.setPrefWidth(canvas.getWidth());
        positionSlider.setOrientation(Orientation.HORIZONTAL);
        positionSlider.setMin(0);
        positionSlider.setMax(100);
        positionSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                position = new_val.doubleValue() / 100;
                drawBezier(ctx);
            }
        });

        Slider zoomSlider = new Slider();
        zoomSlider.setPrefWidth(canvas.getWidth());
        zoomSlider.setOrientation(Orientation.HORIZONTAL);
        zoomSlider.setMin(1);
        zoomSlider.setMax(10);
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                zoom = new_val.doubleValue();
                drawBezier(ctx);
            }
        });

        canvas.setOnScroll(new EventHandler<ScrollEvent>() {
            public void handle(ScrollEvent event) {
                zoomSlider.setValue(zoom + event.getDeltaY() / 500);
            }
        });

        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                for (int i = 0; i < controlPoints.length; i++) {
                    try {
                        // transform the mouse point to the coordinates
                        // we have defined control points in
                        Point2D transformed = transform.inverseTransform(event.getX(), event.getY());
                        // check if mouse is near control points (allows 
                        // some distance to make use easier.
                        if (controlPoints[i].distance(transformed) < Math.max(10, CPW/2)) {
                            draggedPoint = i;
                        }
                    } catch (NonInvertibleTransformException ex) {
                        Logger.getLogger(Bezier.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (draggedPoint >= 0) {
                    try {
                        Point2D transformed = transform.inverseTransform(event.getX(), event.getY());
                        controlPoints[draggedPoint] = transformed;
                        drawBezier(canvas.getGraphicsContext2D());
                    } catch (NonInvertibleTransformException ex) {
                        Logger.getLogger(Bezier.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                draggedPoint = -1;
            }
        });

        VBox vBox = new VBox(canvas, new Text("Set control position"), positionSlider, new Text("Zoom in"), zoomSlider);
        var scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    private void drawBezier(GraphicsContext ctx) {
        double h = ctx.getCanvas().getHeight();
        double w = ctx.getCanvas().getWidth();

        ctx.clearRect(0, 0, w, h);

        ctx.save();

        // move origo to the center
        ctx.translate(w / 2, h / 2);
        // scale there
        ctx.scale(zoom, zoom);
        // move origo back
        ctx.translate(-w / 2, -h / 2);

        // store this transform so that it can be used in mouse event handling
        transform = ctx.getTransform().clone();

        // draw the actual Bézier curve
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(2);
        ctx.beginPath();
        ctx.moveTo(controlPoints[0].getX(), controlPoints[0].getY());
        ctx.bezierCurveTo(
                controlPoints[1].getX(), controlPoints[1].getY(),
                controlPoints[2].getX(), controlPoints[2].getY(),
                controlPoints[3].getX(), controlPoints[3].getY());
        ctx.stroke();

        // draw the control points and lines between them
        // visualizing how Bézier curve forms/is defined
        ctx.setLineWidth(1);
        ctx.setStroke(Color.GRAY);
        drawMidControlPoints(ctx, controlPoints);

        ctx.restore();
    }

    void drawMidControlPoints(GraphicsContext ctx, Point2D[] points) {
        double p = position;
        double r = 1.0 - position;
        Point2D[] mcps = new Point2D[points.length - 1]; // mid control points
        for (int i = 0; i < points.length; i++) {
            // draw a control points and line between it and the next one
            ctx.fillRect(points[i].getX() - CPW / 2, points[i].getY() - CPW / 2, CPW, CPW);
            if (i < mcps.length) { // if this is not the last control point, iterpolate point between this and the next one
                ctx.strokeLine(points[i].getX(), points[i].getY(), points[i + 1].getX(), points[i + 1].getY());
                // interpolate the mid control point on the line
                mcps[i] = new Point2D(
                        p * points[i].getX() + r * points[i + 1].getX(),
                        p * points[i].getY() + r * points[i + 1].getY());
                // draw to interpolated control point
                ctx.fillRect(mcps[i].getX() - CPW / 2, mcps[i].getY() - CPW / 2, CPW, CPW);
            }
        }
        ctx.setFill(Color.GRAY);
        // if control points left to interpolate, do so recursively
        // else draw the final point
        if (mcps.length > 1) {
            drawMidControlPoints(ctx, mcps);
        } else {
            ctx.strokeOval(mcps[0].getX() - 4, mcps[0].getY() - 4, 8, 8);
        }
    }
}
