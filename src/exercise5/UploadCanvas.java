package exercise5;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;


public class UploadCanvas extends Application {

    BorderPane root = new BorderPane();
    MenuBar mb = new MenuBar();
    VBox topVBox = new VBox();
    VBox centerBox = new VBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        topVBox.setSpacing(10);
        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(openItem, exitItem);

        ImageView imageView = new ImageView();


        openItem.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            File file = fileChooser.showOpenDialog(stage);
            //imageView.sefile.getAbsolutePath();
        });

        exitItem.setOnAction(ae -> Platform.exit());
    }
}
