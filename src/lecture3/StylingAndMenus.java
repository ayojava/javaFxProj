package lecture3;

import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class StylingAndMenus extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    boolean confirmQuit() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to exit?");
        alert.setHeaderText("Really exit?");
        alert.setTitle("Exit?");
        Optional<ButtonType> confirmation = alert.showAndWait();
        if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Greeter");

        final Menu file = new Menu("_File");
        file.setMnemonicParsing(true);
        MenuItem exitItem = new MenuItem("E_xit");
        exitItem.setMnemonicParsing(true);
        exitItem.setAccelerator(new KeyCharacterCombination("Q", KeyCombination.CONTROL_DOWN));
        file.getItems().add(exitItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file);

        VBox vBox = new VBox(menuBar);

        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(10, 10, 10, 10));
        flow.setHgap(10);
        flow.setVgap(10);

        var heading = new Text("Greetings");
        heading.setId("greetings-title");
        flow.getChildren().add(heading);
        Button button = new Button("Input");
        flow.getChildren().add(button);

        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                TextInputDialog textInput = new TextInputDialog("NN");
                textInput.setHeaderText("What is your name?");
                textInput.setTitle("Who are you?");
                Optional<String> text = textInput.showAndWait();
                if (text.isPresent()) {
                    heading.setText("Greetings " + text.get());
                }
            }
        });

        StackPane stack = new StackPane();

        stack.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        stack.setPrefWidth(400);
        stack.setPrefHeight(300);

        Stop[] stops1 = {new Stop(0, Color.WHITE), new Stop(1, Color.BLACK)};
        Stop[] stops2 = {new Stop(0, Color.RED), new Stop(1, Color.YELLOW)};
        Rectangle r1 = new Rectangle(150, 150, new RadialGradient(0, 150, 150, 15, 1, true, CycleMethod.REFLECT, stops1));
        Rectangle r2 = new Rectangle(150, 150, new RadialGradient(0, 150, 150, 15, 1, true, CycleMethod.REFLECT, stops2));

        StackPane.setAlignment(flow, Pos.TOP_CENTER);
        StackPane.setAlignment(r1, Pos.BOTTOM_LEFT);
        StackPane.setAlignment(r2, Pos.BOTTOM_RIGHT);
        stack.getChildren().addAll(r1, r2, flow);

        vBox.getChildren().add(stack);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent e) {
                if (!confirmQuit())

                    e.consume();
            }
        });

        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (confirmQuit())
                    stage.close();
            }
        });


        var cl = getClass().getClassLoader();
        var css = cl.getResource("lecture3/exampleStyles.css");
        scene.getStylesheets().add(css.toExternalForm());

        stage.show();
    }
}
