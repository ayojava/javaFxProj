package lecture3;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import static javafx.scene.text.Font.font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class RadioButtonDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Radio button selection");

        Text born = new Text("Born in XXXX's");
        born.setFont(new Font("Arial", 32));

        ToggleGroup radioGroup = new ToggleGroup();

        RadioButton radio1 = new RadioButton("20-30");
        radio1.setToggleGroup(radioGroup);
        radio1.setUserData(20);
        RadioButton radio2 = new RadioButton("30-40");
        radio2.setToggleGroup(radioGroup);
        radio2.setUserData(30);
        RadioButton radio3 = new RadioButton("40-50");
        radio3.setToggleGroup(radioGroup);
        radio3.setUserData(40);
        RadioButton radio4 = new RadioButton("50-60");
        radio4.setToggleGroup(radioGroup);
        radio4.setUserData(50);
        RadioButton radio5 = new RadioButton("60+");
        radio5.setToggleGroup(radioGroup);
        radio5.setUserData(60);

        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (radioGroup.getSelectedToggle() != null) {
                    born.setText("Born in " + (2010 - (Integer) radioGroup.getSelectedToggle().getUserData()) + "'s");
                }
            }
        });

        VBox radioPane = new VBox();
        radioPane.getChildren().addAll(radio1, radio2, radio3, radio4, radio5);
        radioPane.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        radioPane.setPadding(new Insets(10, 10, 10, 10));
        radioPane.setSpacing(10);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(born);
        borderPane.setCenter(radioPane);
        borderPane.setPadding(new Insets(20, 20, 20, 20));

        var scene = new Scene(borderPane);
        stage.setScene(scene);

        stage.show();
    }
}
