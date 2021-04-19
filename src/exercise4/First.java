package exercise4;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class First extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Drag and Drop");

        TextField text1 = new TextField("Some Text");
        text1.setFont(Font.font("Arial"));

        TextField text2 = new TextField("Another");
        text2.setFont(Font.font("Arial"));

        TextField text3 = new TextField("Third");
        text3.setFont(Font.font("Arial"));

        VBox vBox = new VBox();

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(10, 10, 10, 10));


        grid.add(text1,0,0);
        grid.add(text2,1,0);
        grid.add(text3,2,0);

        vBox.getChildren().add(grid);
        Scene scene = new Scene(vBox, 200, 100);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
