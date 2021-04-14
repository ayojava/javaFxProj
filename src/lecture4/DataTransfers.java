/*
 * This file can be freely used for educational purposes.
 * However, you must use critical thinking 
 * when using the file as an example.
 */
package lecture4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Jaakko Hakulinen <jaakko.hakulinen at tuni.fi>
 */
public class DataTransfers extends Application implements EventHandler {

    static final String[] INITIAL_IMAGES = new String[]{"GuineaFowl.jpg", "getz.jpg"};
    static final String[] INITIAL_CAPTIONS = new String[]{"Some fowls", "An old Getz"};

    MenuItem copyItem;
    MenuItem pasteItem;
    Node selectedElement;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Data transfer");

        Menu file = new Menu("_File");
        MenuItem exitItem = new MenuItem("E_xit");
        exitItem.setMnemonicParsing(true);
        exitItem.setAccelerator(new KeyCharacterCombination("Q", KeyCombination.CONTROL_DOWN));
        Menu edit = new Menu("_Edit");
        copyItem = new MenuItem("_Copy");
        copyItem.setMnemonicParsing(true);
        copyItem.setAccelerator(new KeyCharacterCombination("C", KeyCombination.CONTROL_DOWN));
        copyItem.setDisable(true);
        pasteItem = new MenuItem("_Paste");
        pasteItem.setMnemonicParsing(true);
        pasteItem.setAccelerator(new KeyCharacterCombination("V", KeyCombination.CONTROL_DOWN));
        Clipboard cb = Clipboard.getSystemClipboard();
        pasteItem.setDisable(true);

        file.getItems().add(exitItem);
        edit.getItems().addAll(copyItem, pasteItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file, edit);

        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                stage.close();
            }
        });

        copyItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Clipboard cb = Clipboard.getSystemClipboard();
                Map<DataFormat, Object> data = new HashMap<>();
                if (selectedElement instanceof ImageView) {
                    data.put(DataFormat.IMAGE, ((ImageView) selectedElement).getImage());
                }
                if (selectedElement instanceof Text) {
                    data.put(DataFormat.PLAIN_TEXT, ((Text) selectedElement).getText());
                }
                cb.setContent(data);
            }
        });

        pasteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Clipboard cb = Clipboard.getSystemClipboard();
                for (DataFormat f : cb.getContentTypes()) {
                    if (f == DataFormat.IMAGE && selectedElement instanceof ImageView) {
                        ((ImageView) selectedElement).setImage((Image) cb.getContent(DataFormat.IMAGE));
                    }
                    if (f == DataFormat.PLAIN_TEXT && selectedElement instanceof Text) {
                        ((Text) selectedElement).setText((String) cb.getContent(DataFormat.PLAIN_TEXT));
                    }
                }
            }
        });

        VBox vBox = new VBox();

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setPadding(new Insets(10, 10, 10, 10));

        var cl = getClass().getClassLoader();
        for (int i = 0; i < INITIAL_IMAGES.length; i++) {
            var imageStream = cl.getResourceAsStream("res/" + INITIAL_IMAGES[i]);
            Image image = new Image(imageStream);
            ImageView imageView = new ImageView(image);
            imageView.setOnMouseClicked(this);

            imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Dragboard db = imageView.startDragAndDrop(TransferMode.COPY);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(imageView.getImage());
                    db.setContent(content);
                    event.consume();
                }
            });

            imageView.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    Dragboard db = event.getDragboard();
                    if (db.hasImage() || (db.hasFiles() && suitableImageFile(db.getFiles()) != null)) {
                        // Thge following shows you can check the potential 
                        // transfer mdes, usually this is unnecessary and you
                        // can just return the one you support
                        Set<TransferMode> transferModes = db.getTransferModes();
                        TransferMode suitable = null;
                        for (TransferMode tm : transferModes) {
                            if (tm == TransferMode.COPY) {
                                suitable = tm;
                            }
                        }
                        event.acceptTransferModes(suitable);
                    }
                    event.consume();
                }
            });

            imageView.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasImage()) {
                        imageView.setImage(db.getImage());
                        success = true;
                    }
                    if (db.hasFiles()) {
                        File suitable = suitableImageFile(db.getFiles());
                        if (suitable != null) {
                            try {
                                Image i = new Image(new FileInputStream(suitable));
                                imageView.setImage(i);
                                success = true;
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(DataTransfers.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    event.setDropCompleted(success);
                    event.consume();
                }
            });

            Text caption = new Text(INITIAL_CAPTIONS[i]);
            caption.setFont(new Font("Arial", 32));
            caption.setOnMouseClicked(this);

            caption.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Dragboard db = caption.startDragAndDrop(TransferMode.COPY);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(caption.getText());
                    db.setContent(content);
                    event.consume();
                }
            });

            caption.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    Dragboard db = event.getDragboard();
                    if (db.hasString()) {
                        event.acceptTransferModes(TransferMode.COPY);
                    }
                    event.consume();
                }
            });

            caption.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    Dragboard db = event.getDragboard();
                    if (db.hasString()) {
                        caption.setText(db.getString());
                        event.setDropCompleted(true);
                    } else {
                        event.setDropCompleted(false);
                    }
                    event.consume();
                }
            });
            // JavaFX has incompatibility with image pasting from clip board with some
            // programs but not all. Images appear with inforrect/faded colours and
            // image sizes seem to be incorrect as well.
            // Adding background alleviates the issue somewhat (colour are clorer to the truth)
            // but doesn't fix it. The bug has been open in OpenJDK since summer 2019.
            Pane bgPane = new Pane();
            bgPane.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), Insets.EMPTY)));
            bgPane.getChildren().add(imageView);
            // put both image and text inside scroll panes as any size content
            // can appear per drag-n-drop or copy-n-paste
            ScrollPane imageScrollPane = new ScrollPane(bgPane);
            ScrollPane textScrollPane = new ScrollPane(caption);
            grid.add(imageScrollPane, i, 0);
            grid.add(textScrollPane, i, 1);
        }

        // listening to window receiveing focus, i.e., user switching back to this 
        // application from some other program. As clip board content may have changed
        // we should update paste item status.
        stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown) {
                if (onShown) {
                    System.out.println("window focus changed");
                    Clipboard cb = Clipboard.getSystemClipboard();
                    if (selectedElement instanceof ImageView) {
                        pasteItem.setDisable(!cb.hasContent(DataFormat.IMAGE));
                    } else if (selectedElement instanceof Text) {
                        pasteItem.setDisable(!cb.hasContent(DataFormat.PLAIN_TEXT));
                    }
                }
            }
        });

        vBox.getChildren().addAll(menuBar, grid);
        var scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Tells if dragged image files contain at least one that is of suitable
     * type, i.e., if extensions tells it is one of the image types JavaFX
     * documentation states is supported. Returns the first suitable one.
     *
     * @param files
     * @return
     */
    File suitableImageFile(List<File> files) {
        for (File f : files) {
            String fname = f.getName();
            if (fname.toLowerCase().endsWith(".bmp")
                    || fname.toLowerCase().endsWith(".jpg")
                    || fname.toLowerCase().endsWith(".jpeg")
                    || fname.toLowerCase().endsWith(".gif")
                    || fname.toLowerCase().endsWith(".png")) {
                return f;
            }
        }
        return null;
    }

    /**
     * Registered to listen to user clicking on ImageView and Text nodes.
     *
     * @param evt
     */
    @Override
    public void handle(Event evt) {
        // remove hightlight from previously selected node, if any
        if (selectedElement != null) {
            selectedElement.setEffect(null);
        }
        // remember the selected node
        selectedElement = (Node) evt.getTarget();
        // add a small visual effect to indicate the selection, could be better to make this more clear.
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0);
        ds.setOffsetX(3.0);
        ds.setColor(Color.GRAY);
        selectedElement.setEffect(ds);
        // enable/disable copy and paste menu items
        copyItem.setDisable(false);
        Clipboard cb = Clipboard.getSystemClipboard();
        if (selectedElement instanceof ImageView) {
            pasteItem.setDisable(!cb.hasContent(DataFormat.IMAGE));
        } else if (selectedElement instanceof Text) {
            pasteItem.setDisable(!cb.hasContent(DataFormat.PLAIN_TEXT));
        }
    }

}
