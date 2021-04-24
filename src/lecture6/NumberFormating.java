/*
 * This file can be freely used for educational purposes.
 * However, you must use critical thinking 
 * when using the file as an example.
 */
package lecture6;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Jaakko Hakulinen <jaakko.hakulinen at tuni.fi>
 */
public class NumberFormating extends Application {

    Text dateText;
    Text numberText;
    Text currencyText;
    Text percentageText;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox box = new VBox();

        box.setPadding(new Insets(15, 15, 15, 15));
        box.setSpacing(10);

        ComboBox<Locale> locales = new ComboBox<>();
        Locale[] availableLocales = Locale.getAvailableLocales();
        Arrays.sort(availableLocales, new Comparator<Locale>() {
            public int compare(Locale a, Locale b) {
                return a.getDisplayName().compareTo(b.getDisplayName());
            }
        });
        for (Locale l : availableLocales) {
            locales.getItems().add(l);
        }

        locales.setCellFactory(new Callback<ListView<Locale>, ListCell<Locale>>() {
            public ListCell<Locale> call(ListView<Locale> lv) {
                return new ListCell<Locale>() {
                    {
                        setContentDisplay(ContentDisplay.TEXT_ONLY);
                    }

                    @Override
                    protected void updateItem(Locale item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item != null) {
                            setText(item.getDisplayName());
                        }
                    }
                };
            }
        });
        locales.setButtonCell(new ListCell<Locale>() {
            {
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }

            @Override
            protected void updateItem(Locale item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getDisplayName());
                }
            }
        });

        locales.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent evt) {
                updateTexts(locales.getSelectionModel().getSelectedItem());
            }
        });
        locales.getSelectionModel().select(Locale.getDefault());
        
        box.getChildren().add(locales);

        dateText = new Text();
        numberText = new Text();
        currencyText = new Text();
        percentageText = new Text();
        box.getChildren().add(dateText);
        box.getChildren().add(numberText);
        box.getChildren().add(currencyText);
        box.getChildren().add(percentageText);

        updateTexts(Locale.getDefault());

        var scene = new Scene(box);
        stage.setScene(scene);
        stage.show();
    }

    void updateTexts(Locale locale) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        NumberFormat nf = NumberFormat.getInstance(locale);
        NumberFormat pf = NumberFormat.getPercentInstance(locale);
        NumberFormat cf = NumberFormat.getCurrencyInstance(locale);
        cf.setCurrency(Currency.getInstance(new Locale("en", "IN")));

        dateText.setText("Date: " + df.format(new Date()));
        numberText.setText("Number: " + nf.format(1.32576));
        currencyText.setText("Balance: " + cf.format(1.32576));
        percentageText.setText("Percentage: " + pf.format(1.32576));

    }
}
