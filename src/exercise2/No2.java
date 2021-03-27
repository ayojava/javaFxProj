package exercise2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class No2 extends Application {

    public static void main(String[] arguments) {
        Application.launch(No2.class, arguments);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final Parent fxmlRoot = FXMLLoader.load(getClass().getResource("No2.fxml"));
        stage.setScene(new Scene(fxmlRoot));
        stage.show();
    }
}
