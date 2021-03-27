/*
 * This file can be freely used for educational purposes.
 * However, you must use critical thinking 
 * when using the file as an example.
 */
package lecture3;

import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is a controller for the interface defined in res/ui.fxml.
 * Key elements in handler are method names which match the
 * # marked event handler references in fxml file,
 * @FXML marking and variable names which match fx:id 
 * values in fxml file. This controller was created to
 * be created in code and has extra method (setStage)
 * to allow program to set variables in the controller.
 * 
 * @author Jaakko Hakulinen <jaakko.hakulinen at tuni.fi>
 */
public class DemoController {
    Stage stage;
    
    public Text actionTarget = null;

    public DemoController() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    protected void handleUpdateAction(ActionEvent event) {
        // this method was added after the lecture to illustrage the direct access to components
        actionTarget.setText("Welcome to FXML");
    }
    
    @FXML
    protected void handleExitAction(ActionEvent event) {
        stage.close();
    }
    
    /**
     * This event handler illustrates how you can access
     * components without receiving direct references to them.
     * The code is not generic but greatly utilises the 
     * knowledge of the structure and content of the fxml
     * content.
     * 
     * @param event 
     */
    @FXML
    protected void handleShuffle(ActionEvent event) {
        Parent root = stage.getScene().getRoot();
        ObservableList<Node> children = root.getChildrenUnmodifiable();
        for (var child : children) {
            if (child.getId().equals("layoutgrid")) {
                GridPane gp = (GridPane)child;
                ObservableList<Node> rects = gp.getChildrenUnmodifiable();
                Paint previous = ((Rectangle)rects.get(rects.size() -1)).getFill();
                for (var child2 : rects) {
                    if (child2.getId() != null && child2.getId().startsWith("rect")) {
                        Rectangle rect = (Rectangle)child2;
                        Paint current = rect.getFill();
                        if (previous != null)
                            rect.setFill(previous);
                        previous = current;
                    }
                }
            }
        }
    }

    @FXML
    protected void handleAboutAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "This application aims to demo the use of FXML UI specification markup.");
        alert.setHeaderText("This is an FXML Demo");
        alert.setTitle("Demo");
        Optional<ButtonType> confirmation = alert.showAndWait();
    }
}
