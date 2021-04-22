package exercise5;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class DrawText extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        GridPane gridPane = new GridPane();

        Canvas canvas = new Canvas(600, 600);

        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
        graphicsContext2D.setFont(new Font("Arial", 60));

        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        MenuItem addImageMenuItem = new MenuItem("Load Image");
        MenuItem addTopTextMenuItem = new MenuItem("Top Text");
        MenuItem addBottomTextMenuItem = new MenuItem("Bottom Text");

        addImageMenuItem.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            Image image = new Image("file:" + file.getAbsolutePath());
            graphicsContext2D.drawImage(image,0,0);
        });

        addTopTextMenuItem.setOnAction(actionEvent -> {
            addTextToImage("Captioned",100,100,200,graphicsContext2D);
        });
        addBottomTextMenuItem.setOnAction(actionEvent -> {
            addTextToImage("Image",100,300,400,graphicsContext2D);
        });

        fileMenu.getItems().add(addImageMenuItem);
        fileMenu.getItems().add(addTopTextMenuItem);
        fileMenu.getItems().add(addBottomTextMenuItem);

        final MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(editMenu);

        final VBox vBox = new VBox(menuBar);

        gridPane.add(vBox,0,0);
        gridPane.add(canvas,0,1);

        Scene scene = new Scene(gridPane, 600, 600);
        stage.setTitle("Exercise 5.2");
        stage.setScene(scene);
        stage.show();
    }

    private void addTextToImage(String text,int pos1, int pos2,int pos3,GraphicsContext graphicsContext2D){
        graphicsContext2D.strokeText(text,pos1,pos2,pos3);
    }
}
