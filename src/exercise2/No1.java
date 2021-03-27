package exercise2;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import static javafx.scene.text.Font.font;

import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class No1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Colours Selection");

        Text coloursTxt = new Text();
        coloursTxt.setText("Colours");

        defaultEffect(coloursTxt);

        ToggleGroup radioGroup = new ToggleGroup();

        RadioButton firstRadioBtn = new RadioButton("First Colour");
        firstRadioBtn.setToggleGroup(radioGroup);
        firstRadioBtn.setUserData("First");

        RadioButton secondRadioBtn = new RadioButton("Second Colour");
        secondRadioBtn.setToggleGroup(radioGroup);
        secondRadioBtn.setUserData("Second");

        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (radioGroup.getSelectedToggle() != null) {
                    String label = (String)radioGroup.getSelectedToggle().getUserData();
                    if(label.equalsIgnoreCase("First")){
                        defaultEffect(coloursTxt);
                    }else {
                        anotherEffect(coloursTxt);
                    }
                }
            }
        });


        VBox radioPane = new VBox();
        radioPane.getChildren().addAll(firstRadioBtn, secondRadioBtn);
        radioPane.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        radioPane.setPadding(new Insets(10, 10, 10, 10));
        radioPane.setSpacing(10);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(coloursTxt);
        borderPane.setCenter(radioPane);
        borderPane.setPadding(new Insets(20, 20, 20, 20));

        var scene = new Scene(borderPane);
        stage.setScene(scene);

        stage.show();

    }

    public void defaultEffect(Text coloursTxt ){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetY(3.0f);
        dropShadow.setColor(Color.color(0.4f, 0.4f, 0.4f));

        coloursTxt.setEffect(dropShadow);
        coloursTxt.setCache(true);
        coloursTxt.setX(10.0f);
        coloursTxt.setY(270.0f);
        coloursTxt.setFill(Color.RED);
        coloursTxt.setFont(Font.font(null, FontWeight.BOLD, 32));
    }

    public void anotherEffect(Text coloursTxt){
        InnerShadow is = new InnerShadow();
        is.setOffsetX(4.0f);

        coloursTxt.setEffect(is);
        coloursTxt.setX(10.0f);
        coloursTxt.setY(270.0f);
        coloursTxt.setFill(Color.YELLOW);
        coloursTxt.setFont(Font.font(null, FontWeight.BOLD, 80));

    }
}
