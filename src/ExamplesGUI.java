import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class ExamplesGUI {
    ArrayList<TreeAutomaton> automata;

    public static void display(ArrayList<TreeAutomaton> a){
        ExamplesGUI g = new ExamplesGUI();
        g.launch(a);
    }

    public void launch(ArrayList<TreeAutomaton> a){
        this.automata = a;
        Stage window = new Stage();
        showInfo(window);
    }

    public void showInfo(Stage window){
        Button forward, backward, exit;

        int counter[] = {0};

        Label infoText = new Label();
        infoText.setText(text[counter[0]]);
        infoText.setWrapText(true);
        infoText.setMaxWidth(380);

        Label statusText = new Label();
        statusText.setText("Step " + (counter[0] + 1) + "/" + text.length);
        statusText.setWrapText(true);
        statusText.setTextAlignment(TextAlignment.CENTER);

        forward = new Button(">");
        forward.setOnAction(e -> {
            if (counter[0] < text.length - 1) {
                counter[0]++;
                infoText.setText(text[counter[0]]);
                statusText.setText("Step " + (counter[0] + 1) + "/" + text.length);
                if(counter[0] == 2){
                    objectInformationGUI.displayAutomaton(automata.get(0));
                }
            } else {
                ErrorGUI.display("No more steps");
            }
        });

        backward = new Button("<");
        backward.setOnAction(e -> {
            if (counter[0] > 0) {
                counter[0]--;
                infoText.setText(text[counter[0]]);
                statusText.setText("Step " + (counter[0] + 1) + "/" + text.length);
            } else {
                ErrorGUI.display("Can't go back further");
            }
        });

        exit = new Button("exit");
        exit.setOnAction(e -> {
            window.close();
        });

        HBox navRow = new HBox(10);
        navRow.setAlignment(Pos.CENTER);
        navRow.getChildren().addAll(backward, forward);

        VBox vert = new VBox(10);
        vert.setAlignment(Pos.CENTER);
        vert.getChildren().addAll(infoText, statusText, navRow, exit);

        Scene infoScene = new Scene(vert, 400, 400);
        window.setTitle("Example Work Through");
        window.setMinWidth(400);
        window.setMinHeight(400);
        window.setScene(infoScene);
        window.show();

    }

    private static String[] text = {
            "To witness the acceptance process of a tree with a bottom-up deterministic tree automata we can use the tool with one of the provided examples. Go back to the main menu and click operate.\n",
            "Now, select ExampleAutomaton1 and ExampleTree1 from the left and right columns respectively before clicking operate. \n",
            "This will bring us to the operation process display. The rules and final states of ExampleAutomaton1 will now be displayed. We’ll use these to work through the operation process together using the tool. \n" + "The first step of the process is always just the tree before any transition rules have been applied. We work from the bottom of the tree to the top. The lowest node in this tree is the ‘a’ on the far right. This node does not have any children so we will apply the rule from the automaton which sees element ‘a’ with no child states, which there can only be one of because this automaton is deterministic. We can see that this rule results in q1, so we give the node we are looking at parent ‘q1’, representing the state q1. Click ‘>’ and you should see this happen.\n",
            "There are 3 nodes on the same level now. It doesn’t matter which order we look at these nodes in, however this tool will always look at the left-most node of a level, which is represented graphically by this tool as the node closest to the top of the window on a given level. In this case this happens to be a node labelled by the unary element ‘b’. This element has child state ‘q1’. We will look or a rule in the tree automaton which has element ‘b’ with child state ‘q1’ which we can see results in state ‘q2’. Similarly to the previous step when you click ‘>’ the node b is given parent state ‘q2’.\n",
            "Click ‘>’ twice more, as the next two nodes are in the same situation as the first ‘a’ node that was operated on. Next we come to the rightmost lowest ‘c’ node. This node has child states q1 and q2. The rule in the automaton with element ‘c’ and states ‘q1’ and ‘q2’ results in state ‘q3’. Click ‘>’ to continue. \n",
            "Keep pressing ‘>’ until the acceptance test is complete. As the root node is state ‘q4’ after all possible transition rules have been applied and the state ‘q4’ is a member of the final states set of the automaton, this tree is accepted by this automaton. "
    };
}
