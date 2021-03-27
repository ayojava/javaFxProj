package exercise2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ResourceBundle;

public class No2Controller implements Initializable {
    @FXML
    private MenuBar menuBar;

    @FXML
    private Text headingTxt;

    @FXML
    private void handleUpdateAction(final ActionEvent event){
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

    @FXML
    private void handleAboutAction(final ActionEvent event) {
        System.out.println("You clicked on About!");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");

        alert.setHeaderText("Something about this application");

        alert.showAndWait();
    }

//    @FXML
//    private void handleKeyInput(final InputEvent event) {
//        if (event instanceof KeyEvent) {
//            final KeyEvent keyEvent = (KeyEvent) event;
//            if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.A) {
//                provideAboutFunctionality();
//            }
//        }
//    }

    @Override
    public void initialize(java.net.URL arg0, ResourceBundle arg1) {
        menuBar.setFocusTraversable(true);
    }


}
