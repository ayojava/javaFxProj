/*
 * This file can be freely used for educational purposes.
 * However, you must use critical thinking 
 * when using the file as an example.
 */
package lecture5;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 *
 * @author Jaakko Hakulinen <jaakko.hakulinen at tuni.fi>
 */
public class MouseOnCanvas extends Application {

    static final double CANVAS_SIZE = 600;
    static final double PRINT_SCALE = 8;
    static final int SNAPSHOT_SIZE = 100;

    Canvas canvas;
    Stage stage;
    FlowPane snapshots;
    double mouseX;
    double mouseY;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        Menu file = new Menu("_File");
        file.setMnemonicParsing(true);
        MenuItem printItem = new MenuItem("_Print");
        printItem.setMnemonicParsing(true);
        printItem.setAccelerator(new KeyCharacterCombination("P", KeyCombination.CONTROL_DOWN));

        MenuItem exitItem = new MenuItem("E_xit");
        exitItem.setMnemonicParsing(true);
        exitItem.setAccelerator(new KeyCharacterCombination("Q", KeyCombination.CONTROL_DOWN));
        file.getItems().addAll(printItem, exitItem);

        printItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                printGraph();
            }
        });

        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                stage.close();
            }
        });
        canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                GraphicsContext ctx = canvas.getGraphicsContext2D();
                mouseX = evt.getX();
                mouseY = evt.getY();
                drawMousePositionInfo(ctx, mouseX, mouseY, 1.0);
            }
        });
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                WritableImage wi = new WritableImage(SNAPSHOT_SIZE, SNAPSHOT_SIZE);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                snapshotParameters.setTransform(new Scale(SNAPSHOT_SIZE / canvas.getWidth(), SNAPSHOT_SIZE / canvas.getWidth()));
                canvas.snapshot(snapshotParameters, wi);
                ImageView canvasImage = new ImageView(wi);
                snapshots.getChildren().add(canvasImage);
            }
        });

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(file);

        Text heading = new Text("Mouse Position (to hue)");
        heading.setFont(new Font("Arial", 24));

        snapshots = new FlowPane();
        snapshots.setPrefHeight(SNAPSHOT_SIZE);
        snapshots.setPrefWidth(canvas.getWidth());

        canvas.setCursor(Cursor.NONE);

        ScrollPane scrollPane = new ScrollPane(snapshots);
        
        VBox vBox = new VBox(menuBar, new VBox(heading, canvas, scrollPane));
        var scene = new Scene(vBox);

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number oldWidth, Number newWidth) {
                canvas.setWidth(newWidth.doubleValue());
            }
        });

        stage.setScene(scene);

        stage.show();
    }

    public void printGraph() {
        Pane node = (Pane) canvas.getParent();
        Canvas canvasForPrinting = new Canvas(CANVAS_SIZE * PRINT_SCALE, CANVAS_SIZE * PRINT_SCALE);
        drawMousePositionInfo(canvasForPrinting.getGraphicsContext2D(), mouseX * PRINT_SCALE, mouseY * PRINT_SCALE, PRINT_SCALE);

        Text current = (Text) node.getChildren().get(0);
        Text printedText = new Text(current.getText());
        printedText.setFont(current.getFont());
        VBox copyToBePrinted = new VBox(printedText, canvasForPrinting);
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            if (job.showPrintDialog(stage)) {
                double printScale = job.getJobSettings().getPageLayout().getPrintableWidth() / (CANVAS_SIZE * PRINT_SCALE);
                canvasForPrinting.getTransforms().add(new Scale(printScale, printScale));

                boolean success = job.printPage(copyToBePrinted);
                if (success) {
                    job.endJob();
                }
            }
        }
    }

    public void drawMousePositionInfo(GraphicsContext ctx, double mouseX, double mouseY, double printScale) {
        double w = ctx.getCanvas().getWidth();
        double h = ctx.getCanvas().getHeight();

        // calculate the angle from the center to the mouse point, value is in radians as are all values to and from Math trigonometric functions
        double angle = Math.atan2(mouseY - h / 2, mouseX - w / 2);

        Color bg = Color.hsb(Math.toDegrees(angle), 0.5d, 1.0d);
        
        // clear canvas
        // ctx.clearRect(0, 0, w, h);
        ctx.setFill(bg);
        ctx.fillRect(0, 0, w, h);
        ctx.setFill(Color.BLACK);

        ctx.save();
        ctx.setLineWidth(1.0 * printScale);
        ctx.setFont(new Font("Helvetica", 14d * printScale));

        // save the current canvas transform so that we can go back to it
        ctx.save();
        // black cursor with drop shadow
        ctx.setLineWidth(2.0 * printScale);
        ctx.setStroke(Color.BLACK);
        drawCross(ctx, mouseX, mouseY, printScale);
        DropShadow ds = new DropShadow();
        ds.setOffsetX(2.0 * printScale);
        ds.setOffsetY(2.0 * printScale);
        ctx.applyEffect(ds);
        ctx.restore();

        ctx.save();
        ctx.setStroke(Color.GRAY);
        // distance of the mouse from center displayed
        // doing different transform so that text is always upright (or vertical in worst case)
        if (Math.abs(angle) < Math.PI / 2) {
            ctx.translate(w / 2, h / 2);
            ctx.rotate(Math.toDegrees(angle));
        } else {
            ctx.translate(mouseX, mouseY);
            ctx.rotate(Math.toDegrees(angle + Math.PI));
        }
        drawDistance(ctx, Math.round(Math.hypot(mouseY - h / 2, mouseX - w / 2)), printScale);
        ctx.restore();

        // distance in x dimension (horizontal)
        ctx.save();
        ctx.translate(Math.min(w / 2, mouseX), h / 2);
        drawDistance(ctx, Math.abs(w / 2 - mouseX), printScale);
        ctx.restore();
        // distance in y domension (vertical)
        ctx.save();
        ctx.translate(mouseX, Math.max(h / 2, mouseY));
        ctx.rotate(-90);
        drawDistance(ctx, Math.abs(h / 2 - mouseY), printScale);
        ctx.restore();
        // angle
        ctx.save();
        ctx.setLineDashes(2, 2);
        ctx.translate(w / 2, h / 2);
        drawAngle(ctx, angle, printScale);
        ctx.restore();

        ctx.restore();
    }

    void drawDistance(GraphicsContext ctx, double distance, double printScale) {
        ctx.setTextBaseline(VPos.CENTER);
        String distanceString = " " + (distance / printScale) + " ";
        Text text = new Text(distanceString);
        text.setFont(ctx.getFont());
        double width = text.getLayoutBounds().getWidth();
        double textStart = (distance - width) / 2;
        double textEnd = textStart + width;
        ctx.beginPath();
        ctx.moveTo(0, 0);
        ctx.lineTo(textStart, 0);
        ctx.moveTo(textEnd, 0);
        ctx.lineTo(distance, 0);
        ctx.stroke();
        ctx.fillText(distanceString, textStart, 0);
    }

    void drawCross(GraphicsContext ctx, double x, double y, double printScale) {
        ctx.beginPath();
        ctx.moveTo(x - 10 * printScale, y);
        ctx.lineTo(x + 10 * printScale, y);
        ctx.moveTo(x, y - 10);
        ctx.lineTo(x, y + 10);
        ctx.stroke();
    }

    void drawAngle(GraphicsContext ctx, double angle, double printScale) {
        String angleText = Double.toString(Math.round(angle / Math.PI * 180)) + '\u00B0';
        ctx.beginPath();
        ctx.moveTo(0, 0);
        ctx.arc(0, 0, 50 * printScale, 50 * printScale, 0, -Math.toDegrees(angle));
        ctx.stroke();
        ctx.fillText(angleText, 10 * printScale, 10 * printScale);
    }
}
