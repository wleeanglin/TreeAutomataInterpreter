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

        Label status = new Label();
        status.setWrapText(true);
        //maybe have max width as a function of the current width?
        status.setMaxWidth(350);

        //see how far along in the history we are
        //this is ugly hack
        int[] counter = {0};

        tree.setText(history.get(counter[0]).guiPrint());
        if(history.size() > 1){
            status.setText("Step " + 1 + "/" + history.size());
        } else{
            status.setText("Step 1/1\nOperation finished with message " + errbuf);
        }

        stepForward = new Button(">");
        stepForward.setOnAction(e -> {
            if(counter[0] < (history.size() - 1)){
                tree.setText(history.get(++counter[0]).guiPrint());
                if(counter[0] == history.size() - 1){
                    status.setText("Step " + (counter[0] + 1)+ "/" + history.size() + "\n" + "Operation finished with message; " + errbuf);
                } else {
                    status.setText("Step " + (counter[0] + 1)+ "/" + history.size());
                }
            } else{
                ErrorGUI.display("Already at the end of operation!");
            }
        });

        stepBackward = new Button("<");
        stepBackward.setOnAction(e -> {
            if(counter[0] > 0){
                tree.setText(history.get(--counter[0]).guiPrint());
                status.setText("Step " + (counter[0] + 1) + "/" + history.size());
            } else{
                ErrorGUI.display("Can't go back further!");
            }
        });

        fullForward = new Button(">>");
        fullForward.setOnAction(e -> {
            counter[0] = (history.size() - 1);
            tree.setText(history.get(counter[0]).guiPrint());
        });

        fullBackward = new Button("<<");
        fullBackward.setOnAction(e -> {
            counter[0] = 0;
            tree.setText(history.get(counter[0]).guiPrint());
        });

        quit = new Button("quit");
        quit.setOnAction(e -> window.close());

        VBox vert = new VBox(10);
        HBox row2 = new HBox(10);
        HBox row3 = new HBox(10);
        HBox row4 = new HBox(10);
        vert.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);
        row3.setAlignment(Pos.CENTER);
        row4.setAlignment(Pos.CENTER);

        row2.getChildren().addAll(fullBackward, stepBackward, stepForward, fullForward);
        row3.getChildren().addAll(status);
        row4.getChildren().addAll(quit);
        vert.getChildren().addAll(tree, row2, row3, row4);

        Scene historyGUI = new Scene(vert, 400, 400);
        window.setTitle("Operation history");
        window.setMinWidth(400);
        window.setMinHeight(400);
        window.setScene(historyGUI);
        window.showAndWait();
    }
}
