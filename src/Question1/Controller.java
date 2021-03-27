package Question1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;




public class Controller {
    @FXML
    public Label middleNamefld;

    @FXML
    public Button autoFillBtn;

    @FXML
    private TextField firstNameTxt;

    @FXML
    private TextField middleNameTxt;

    @FXML
    private TextField lastNameTxt;

    @FXML
    private RadioButton radioBtnLbl;

    @FXML
    protected void handleRadioButtonAction(ActionEvent event) {
        if(radioBtnLbl.isSelected()){
            middleNameTxt.setDisable(false);
            middleNamefld.setVisible(true);
        }else{
            middleNameTxt.setDisable(true);
            middleNamefld.setVisible(false);
        }
    }

    @FXML
    public void handleAutoFillAction(ActionEvent actionEvent) {
        middleNameTxt.setDisable(false);
        middleNamefld.setVisible(true);
        firstNameTxt.setText("Ayodeji");
        middleNameTxt.setText("Oluwaseun");
        lastNameTxt.setText("Ilori");
    }
}
