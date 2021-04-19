/*
 * This file can be freely used for educational purposes.
 * However, you must use critical thinking 
 * when using the file as an example.
 */
package lecture5;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Jaakko Hakulinen <jaakko.hakulinen at tuni.fi>
 */
public class CanvasImage extends Application {

    Canvas canvas;
    Image brushImg;
    
    double pan = 0;
    double tilt = 0;
    double zoom = 1.0;
    double rotate = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        var cl = getClass().getClassLoader();
        var imageStream = cl.getResourceAsStream("res/brush.png");
        brushImg = new Image(imageStream);

        canvas = new Canvas(600, 600);
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        drawImage(ctx);

        Slider tiltSlider = new Slider();
        tiltSlider.setOrientation(Orientation.VERTICAL);
        tiltSlider.setMin(-300);
        tiltSlider.setMax(300);
        tiltSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                tilt = new_val.doubleValue();
                drawImage(ctx);
            }
        });
        
        tiltSlider.setPrefHeight(canvas.getHeight());
        HBox topBox = new HBox();
        VBox left = new VBox();
        left.getChildren().addAll(tiltSlider, new Text("tilt"));
        topBox.getChildren().addAll(left, canvas);
        
        Slider panSlider = new Slider();
        panSlider.setMin(-300);
        panSlider.setMax(300);
        panSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                pan = new_val.doubleValue();
                drawImage(ctx);
            }
        });
        panSlider.setPrefWidth(600);
        Slider zoomSlider = new Slider();
        zoomSlider.setMax(10);
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                zoom = 1.0 + new_val.doubleValue()/3d;
                drawImage(ctx);
            }
        });
        zoomSlider.setPrefWidth(600);
        Slider rotateSlider = new Slider();
        rotateSlider.setMax(360);
        rotateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                rotate = new_val.doubleValue();
                drawImage(ctx);
            }
        });
        rotateSlider.setPrefWidth(600);
        
        GridPane bottomGrid = new GridPane();
        bottomGrid.add(new Text("pan"), 0, 0);
        bottomGrid.add(panSlider, 1, 0);
        bottomGrid.add(new Text("zoom"), 0, 1);
        bottomGrid.add(zoomSlider, 1, 1);
        bottomGrid.add(new Text("rotate"), 0, 2);
        bottomGrid.add(rotateSlider, 1, 2);
        
        VBox vBox = new VBox(topBox, bottomGrid);
        var scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    void drawImage(GraphicsContext ctx) {
        double w = ctx.getCanvas().getWidth();
        double h = ctx.getCanvas().getHeight();
        ctx.clearRect(0, 0, w, h);
        ctx.save();
        ctx.translate(pan, -tilt);
        ctx.translate(w/2, h/2);
        ctx.rotate(rotate);
        ctx.scale(zoom, zoom);
        ctx.translate(-ctx.getCanvas().getWidth()/2, -ctx.getCanvas().getHeight()/2);
        ctx.drawImage(brushImg, w/2-brushImg.getWidth()/2, h/2-brushImg.getHeight()/2);
        ctx.applyEffect(new GaussianBlur());
        DropShadow ds = new DropShadow();
        ds.setOffsetX(4.0);
        ds.setOffsetY(4.0);
        ctx.applyEffect(ds);
        ctx.drawImage(brushImg, w/2-brushImg.getWidth()/2, h/2-brushImg.getHeight()/2);
        ctx.restore();
    }
}
