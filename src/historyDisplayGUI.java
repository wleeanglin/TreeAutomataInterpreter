import com.sun.javafx.binding.Logging;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.ArrayList;
import java.util.Arrays;

public class historyDisplayGUI {
    private ArrayList<Tree> history;
    private String errbuf;

    public static void display(ArrayList<Tree> history, String errbuf){
        historyDisplayGUI g = new historyDisplayGUI();
        g.launch(history, errbuf);
    }

    public void launch(ArrayList<Tree> history, String errbuf){
        this.history = history;
        this.errbuf = errbuf;

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        Button stepForward, stepBackward, fullForward, fullBackward, quit;
        Text tree = new Text();

        tree.setText(history.get(0).guiPrint());

        //see how far along in the history we are
        //this is ugly hack
        int[] counter = {0};

        stepForward = new Button(">");
        stepForward.setOnAction(e -> {
            if(counter[0] < history.size()){
                counter[0]++;
                tree.setText(history.get(counter[0]).guiPrint());
            } else{
                ErrorGUI.display("Already at the end of operation!");
            }
        });

        stepBackward = new Button("<");
        stepBackward.setOnAction(e -> {
            if(counter[0] > 0){
                counter[0]--;
                tree.setText(history.get(counter[0]).guiPrint());
            } else{
                ErrorGUI.display("Can't go back further!");
            }
        });

        fullForward = new Button(">>");

        fullBackward = new Button("<<");

        quit = new Button("quit");
        quit.setOnAction(e -> window.close());

        VBox vert = new VBox(10);
        HBox row2 = new HBox(10);
        HBox row3 = new HBox(10);
        vert.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);
        row3.setAlignment(Pos.CENTER);

        row2.getChildren().addAll(fullBackward, stepBackward, stepForward, fullForward);
        row3.getChildren().addAll(quit);
        vert.getChildren().addAll(tree, row2, row3);

        Scene historyGUI = new Scene(vert, 400, 400);
        window.setTitle("Operation history");
        window.setMinWidth(400);
        window.setMinHeight(400);
        window.setScene(historyGUI);
        window.showAndWait();
    }
}
