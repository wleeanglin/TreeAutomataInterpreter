import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class objectInformationGUI {
    private static Stage window;

    public static void displayAlphabet(RankedAlphabet a){
        window = new Stage();

        Label alphabetText = new Label();
        alphabetText.setWrapText(true);
        alphabetText.setTextAlignment(TextAlignment.LEFT);
        alphabetText.setAlignment(Pos.CENTER);

        StringBuilder s = new StringBuilder();

        s.append(a.getName());
        s.append("\n");
        for(int i = 0; i < (a.getAlph().size() - 1); i++){
            s.append(a.getAlph().get(i));
            s.append(" ");
            s.append(a.getArityArray().get(i));
            s.append("\n");
        }
        s.append(a.getAlph().get(a.getAlph().size() - 1));
        s.append(" ");
        s.append(a.getArityArray().get(a.getAlph().size() - 1));
        s.append("\n");

        alphabetText.setText(s.toString());

        Button ok = new Button("OK");
        ok.setOnAction(e -> window.close());

        VBox vert = new VBox(20);
        vert.setAlignment(Pos.CENTER);
        vert.getChildren().addAll(alphabetText, ok);

        Scene alph = new Scene(vert, 300, 400);
        window.setTitle("Alphabet information");
        window.setMinHeight(400);
        window.setMinWidth(400);
        window.setScene(alph);
        window.showAndWait();
    }

    public static void displayTree(Tree t){
        window = new Stage();

        Label treeText = new Label();
        treeText.setWrapText(true);
        treeText.setAlignment(Pos.CENTER);
        treeText.setTextAlignment(TextAlignment.LEFT);

        StringBuilder s = new StringBuilder();
        s.append(t.getName());
        s.append("\n");
        s.append("Defined using alphabet - ");
        s.append(t.getAlphabet().getName());
        s.append("\n");
        s.append(t.guiPrint());
        treeText.setText(s.toString());

        Button ok = new Button("OK");
        ok.setOnAction(e -> window.close());

        VBox vert = new VBox(20);
        vert.setAlignment(Pos.CENTER);
        vert.getChildren().addAll(treeText, ok);

        Scene tree = new Scene(vert, 300, 400);
        window.setTitle("Tree Information");
        window.setMinHeight(400);
        window.setMinWidth(400);
        window.setScene(tree);
        window.showAndWait();
    }

    public static void displayAutomaton(TreeAutomaton a){
        window = new Stage();

        Label automatonText = new Label();
        automatonText.setWrapText(true);
        automatonText.setAlignment(Pos.CENTER);
        automatonText.setTextAlignment(TextAlignment.CENTER);

        StringBuilder s = new StringBuilder();

        s.append(a.getName());
        s.append("\n");
        s.append("Defined using alphabet - ");
        s.append(a.getAlphabet().getName());
        s.append("\n");
        s.append("Rules - ");
        s.append("\n");
        ArrayList<String> b = a.printRulesGUI();
        for(int i = 0; i < b.size(); i++){
            s.append(b.get(i));
            s.append("\n");
        }
        s.append("Final states - ");
        for(int i = 0; i < a.getFinStates().size(); i++) {
            s.append(a.getFinStates().get(i));
            s.append(" ");
        }
        s.append("\n");

        automatonText.setText(s.toString());

        Button ok = new Button("OK");
        ok.setOnAction(e -> window.close());

        VBox vert = new VBox(20);
        vert.setAlignment(Pos.CENTER);
        vert.getChildren().addAll(automatonText, ok);

        Scene error = new Scene(vert, 300, 400);
        window.setTitle("Automaton Information");
        window.setMinHeight(400);
        window.setMinWidth(400);
        window.setScene(error);
        window.showAndWait();
    }
}
