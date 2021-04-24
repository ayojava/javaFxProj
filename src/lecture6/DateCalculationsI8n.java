/*
 * This file can be freely used for educational purposes.
 * However, you must use critical thinking 
 * when using the file as an example.
 */
package lecture6;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Jaakko Hakulinen <jaakko.hakulinen at tuni.fi>
 */
public class DateCalculationsI8n extends Application {

    Pattern datePattern = Pattern.compile("(?<day>\\d\\d?)\\.(?<month>\\d\\d?)\\.(?<year>\\d\\d(\\d\\d)?)");
    DateFormat df = DateFormat.getDateInstance();
    Date typedDate;
    Integer typedNumber;
    Text dateFormatErrorMessage;
    Text resultDate;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GridPane grid = new GridPane();

        grid.add(new Text(java.util.ResourceBundle.getBundle("res/bundle").getString("DATE CALCULATOR")), 0, 0, 4, 1);
        var dateInput = new TextField();
        Tooltip dateTooltip = new Tooltip();
        dateTooltip.setText(java.util.ResourceBundle.getBundle("res/bundle").getString("ENTER A DATE HERE IN FORMAT LIKE 1.12.2024"));
        dateInput.setTooltip(dateTooltip);
        grid.add(new Text(java.util.ResourceBundle.getBundle("res/bundle").getString("DATE:")), 0, 1);
        grid.add(dateInput, 0, 2);
        grid.add(new Text(java.util.ResourceBundle.getBundle("res/bundle").getString("PLUS")), 1, 2);
        var daysInput = new TextField();
        Tooltip daysTooltip = new Tooltip();
        daysTooltip.setText(java.util.ResourceBundle.getBundle("res/bundle").getString("ENTER A AMOUNT OF DAYS HERE AS A NUMBER"));
        daysInput.setTooltip(daysTooltip);
        grid.add(daysInput, 2, 2);
        grid.add(new Text(java.util.ResourceBundle.getBundle("res/bundle").getString("DAYS")), 2, 1);
        grid.add(new Text(java.util.ResourceBundle.getBundle("res/bundle").getString("IS DATE")), 3, 2);
        resultDate = new Text(java.util.ResourceBundle.getBundle("res/bundle").getString("XXX"));
        grid.add(resultDate, 4, 2);
        dateFormatErrorMessage = new Text(java.util.ResourceBundle.getBundle("res/bundle").getString("BAD DATE FORMAT (DAY.MONTH.YEAR)"));
        grid.add(dateFormatErrorMessage, 0, 3);

        grid.setPadding(new Insets(16, 16, 16, 16));
        grid.setHgap(16);
        grid.setVgap(16);

        dateInput.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent evt) {
                Matcher matcher = datePattern.matcher(dateInput.getText());
                boolean dateOK = false;
                if (matcher.matches() && matcher.group("day") != null && matcher.group("month") != null && matcher.group("year") != null) {
                    int day = Integer.parseInt(matcher.group("day"));
                    int month = Integer.parseInt(matcher.group("month")) - 1;
                    int year = Integer.parseInt(matcher.group("year"));
                    Calendar ci = new GregorianCalendar(year, month, day); 

                    if (ci.get(Calendar.DATE) == day && ci.get(Calendar.MONTH) == month && ci.get(Calendar.YEAR) == year) {
                        typedDate = ci.getTime();
                        dateOK = true;
                    } 
                }
                if (!dateOK) {
                    dateFormatErrorMessage.setVisible(true);
                    typedDate = null;
                } else {
                    dateFormatErrorMessage.setVisible(false);
                    updateResultDate();
                }
            }
        });
        daysInput.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent evt) {
                if (typedDate != null) {
                    try {
                        typedNumber = Integer.parseInt(daysInput.getText());

                        updateResultDate();

                    } catch (NumberFormatException ex) {
                        typedNumber = null;
                    }
                }
            }
        });

        var scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }

    void updateResultDate() {
        if (typedDate == null || typedNumber == null)
            return;
        Calendar ci = Calendar.getInstance();
        ci.setTime(typedDate);
        ci.add(Calendar.DAY_OF_MONTH, typedNumber);
        resultDate.setText(df.format(ci.getTime()));
    }
}
