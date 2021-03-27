package exercise2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;


public class No3 extends Application {

    private Text headingTxt;
    public static void main(String[] args) {
        launch(args);
    }

    void displayTextInputDialog(){

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Update");

        dialog.setContentText("Type Your Message:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            final String text = headingTxt.getText();
            headingTxt.setText(text + "   " + result.get());

            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetY(3.0f);
            dropShadow.setColor(Color.color(0.4f, 0.4f, 0.4f));
            headingTxt.setEffect(dropShadow);
            headingTxt.setCache(true);
            headingTxt.setX(10.0f);
            headingTxt.setY(270.0f);
            headingTxt.setFill(Color.RED);
            headingTxt.setFont(Font.font(null, FontWeight.BOLD, 32));
        }
    }

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Greeter");

        final Menu fileMenu = new Menu("_File");
        fileMenu.setMnemonicParsing(true);
        MenuItem exitMenuItem = new MenuItem("E_xit");
        exitMenuItem.setMnemonicParsing(true);
        exitMenuItem.setAccelerator(new KeyCharacterCombination("Q", KeyCombination.CONTROL_DOWN));
        fileMenu.getItems().add(exitMenuItem);

        final Menu editMenu = new Menu("_Edit");
        editMenu.setMnemonicParsing(true);
        MenuItem updateMenuItem = new MenuItem("U_pdate");
        updateMenuItem.setMnemonicParsing(true);
        updateMenuItem.setAccelerator(new KeyCharacterCombination("U", KeyCombination.CONTROL_DOWN));
        editMenu.getItems().add(updateMenuItem);

        updateMenuItem.setOnAction(actionEvent -> {
            displayTextInputDialog();
        });

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,editMenu);

        headingTxt = new Text("Heading");
        headingTxt.setId("greetings-title");

        VBox vBox = new VBox(menuBar , headingTxt);


        Scene scene = new Scene(vBox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
