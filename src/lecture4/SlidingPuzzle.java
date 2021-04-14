/*
 * This file can be freely used for educational purposes.
 * However, you must use critical thinking 
 * when using the file as an example.
 */
package lecture4;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Jaakko Hakulinen <jaakko.hakulinen at tuni.fi>
 */
public class SlidingPuzzle extends Application implements EventHandler {

    UndoManager undos = new UndoManager();
    MenuItem undoItem;
    MenuItem redoItem;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Sliding puzzle");

        VBox vBox = new VBox();

        final Menu file = new Menu("_File");
        undoItem = new MenuItem("_Undo");
        undoItem.setMnemonicParsing(true);
        undoItem.setAccelerator(new KeyCharacterCombination("Z", KeyCombination.CONTROL_DOWN));
        undoItem.setDisable(true);
        redoItem = new MenuItem("_Redo");
        redoItem.setMnemonicParsing(true);
        redoItem.setAccelerator(new KeyCharacterCombination("Y", KeyCombination.CONTROL_DOWN));
        redoItem.setDisable(true);
        MenuItem exitItem = new MenuItem("E_xit");
        exitItem.setMnemonicParsing(true);
        exitItem.setAccelerator(new KeyCharacterCombination("Q", KeyCombination.CONTROL_DOWN));

        file.getItems().addAll(undoItem, redoItem, new SeparatorMenuItem(), exitItem);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(file);

        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                stage.close();
            }
        });
        undoItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (undos.canUndo()) {
                    undos.undo();
                    updateUndoRedoMenuItems();
                }
            }
        });
        redoItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (undos.canRedo()) {
                    undos.redo();
                    updateUndoRedoMenuItems();
                }
            }
        });

        var cl = getClass().getClassLoader();
        var imageStream = cl.getResourceAsStream("res/GuineaFowl.jpg");
        Image image = new Image(imageStream);

        GridPane grid = new GridPane();

        fillGrid(grid, image);

        grid.setHgap(5);
        grid.setVgap(5);

        Text attribution = new Text("\"Guinea Fowl on Lace\" by RobW_ is licensed under CC BY-NC-ND 2.0");
        
        vBox.getChildren().addAll(menuBar, grid, attribution);
        
        var scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    void updateUndoRedoMenuItems() {
        undoItem.setDisable(!undos.canUndo());
        undoItem.setText(undos.canUndo() ? undos.getUndoPresentationName() : "Undo");
        redoItem.setDisable(!undos.canRedo());
        redoItem.setText(undos.canRedo() ? undos.getRedoPresentationName() : "Redo");
    }

    void fillGrid(GridPane grid, Image image) {
        double size = Math.min(image.getWidth(), image.getHeight());
        double blockSize = (size / 3);
        double xOffset = (image.getWidth() - size) / 2;
        double yOffset = (image.getHeight() - size) / 2;
        List<ImageView> blocks = new LinkedList<>();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                ImageView v = new ImageView(image);
                Rectangle2D viewportRect = new Rectangle2D(
                        x * blockSize + xOffset,
                        y * blockSize + yOffset,
                        blockSize,
                        blockSize);
                v.setViewport(viewportRect);
                v.setOnMouseClicked(this);
                blocks.add(v);
            }
        }

        Collections.shuffle(blocks);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                boolean emptyBlock = x == 0 && y == 2;
                ImageView v = blocks.get(x * 3 + y);
                v.setUserData(new BlockData(x, y, emptyBlock));
                if (emptyBlock) {
                    v.setOpacity(0.1);
                }
                grid.add(v, x, y);
            }
        }
    }

    @Override
    public void handle(Event evt) {
        ImageView clickedBlock = (ImageView) evt.getTarget();
        ImageView empty = (ImageView) clickedBlock.getParent().getChildrenUnmodifiable().filtered(p -> ((BlockData) p.getUserData()).isEmpty()).get(0);
        BlockData bdA = (BlockData) empty.getUserData();
        BlockData bdB = (BlockData) clickedBlock.getUserData();

        if (bdA.nextTo(bdB)) {
            undos.addEdit(new Swap(empty, clickedBlock));
            updateUndoRedoMenuItems();
        }
    }

    private static class Swap extends AbstractUndoableEdit {

        ImageView blockA;
        ImageView blockB;

        public Swap(ImageView a, ImageView b) {
            blockA = a;
            blockB = b;
            doSwap();
        }

        void doSwap() {
            GridPane grid = (GridPane) blockA.getParent();
            BlockData aPos = (BlockData) blockA.getUserData();
            BlockData bPos = (BlockData) blockB.getUserData();
            // remove ImageItems from the grid
            grid.getChildren().remove(blockA);
            grid.getChildren().remove(blockB);
            // swap the positions in the userDatas
            aPos.swapPlaces(bPos);
            // put to grid into the new positions
            grid.add(blockA, aPos.getX(), aPos.getY());
            grid.add(blockB, bPos.getX(), bPos.getY());
        }

        @Override
        public void undo() {
            doSwap();
        }

        public void redo() {
            doSwap();
        }

        @Override
        public boolean canRedo() {
            return true;
        }

        @Override
        public String getPresentationName() {
            return "move";
        }
    }

    private static class BlockData {
        
        int x;
        int y;
        boolean empty;
        
        public BlockData(int x, int y, boolean empty) {
            this.x = x;
            this.y = y;
            this.empty = empty;
        }
        
        public void swapPlaces(BlockData other) {
            int tmpX = x;
            int tmpY = y;
            x = other.getX();
            y = other.getY();
            other.setX(tmpX);
            other.setY(tmpY);
        }
        
        private boolean nextTo(BlockData b) {
            return Math.abs(x - b.getX()) + Math.abs(y - b.getY()) == 1;
        }
        
        public boolean isEmpty() {
            return empty;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
