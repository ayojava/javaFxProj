/*
 * This file can be freely used for educational purposes.
 * However, you must use critical thinking 
 * when using the file as an example.
 */
package lecture3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This demo uses a controller and fxml file to setup
 * an interface.
 * 
 * @author Jaakko Hakulinen <jaakko.hakulinen at tuni.fi>
 */
public class FXMLDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        // TODO add styling to fxml?
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui.fxml"));
        DemoController controller = new DemoController();
        controller.setStage(stage);
        loader.setController(controller);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        
        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();
    }
    
}
