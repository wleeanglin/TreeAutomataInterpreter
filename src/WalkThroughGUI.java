import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;

public class WalkThroughGUI {

    public static void display(){
        WalkThroughGUI g = new WalkThroughGUI();
        g.launch();
    }

    public void launch(){
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
        window.setTitle("Tool Walk Through");
        window.setMinWidth(400);
        window.setMinHeight(400);
        window.setScene(infoScene);
        window.show();

    }

    private static String[] text = {
            "When inputting your own trees and automata to test for acceptance there are 3 steps in the definition process. The first step is to input a new ranked alphabet. To do this go to the main menu and click new and then click alphabet.\n" + "Firstly you will need to choose a name for your new alphabet. After the name has been selected you can continue to the main alphabet input page. To add new elements to the alphabet write an element in the ‘element box’ and its associated arity in the ‘arity’ box before pressing add. You can remove elements from the alphabet by selecting them from the list on the right hand side and clicking remove. When you are finished defining an alphabet click finish to go back to the menu. You must have at least one element with arity 0 to finish defining a new alphabet. Without this and tree defined would be infinite. \n",
            "Next you can define a tree that you would like to operate on using you’re new ranked alphabet. To do this go back to the main menu, click new and then tree. Then, similarly to when defining a new alphabet, you can select a name for your new tree. Once this has been done the next step is to select an alphabet to use for the definition of the tree. To do this select whichever alphabet from the list before clicking continue.\n" + "Now to define the tree you must first select which element you would like at the root of the tree. This can be any element from the list on the left hand side of the window but remember the number of children of a node is determined by arity of the element selected. The tree is then defined level by level with the position of the next node to be added to the tree represented by the ‘?’ in the graphical representation of the tree on the right side of the window. \n" + "When the tree has been fully defined, i.e. each leaf node is a node labelled by an element of arity 0, the definition process is complete. \n",
            "Finally you have to define a bottom-up tree automaton. To do this, go back to the ‘new’ menu and select automaton. First thing to do is to select a name and alphabet to use for this automaton. If you’d like to operate this automaton on the tree just defined you should select the same alphabet used previously. \n" + "Now you need to go through and define the transition rules. As we are working with bottom up finite tree automaton you will need 3 components for each transition rule. An element f, which is what the node is labelled, arity(f) current states, which are the child states of the node, a new state, which is what the node will transition to. The states are actively stored as you define rules, so can be any string. When you are finished defining rules click continue.\n" + "Finally you should select any number of final states. Once you click finish the tree automaton has been fully defined.\n",
            "Finally you have to define a bottom-up tree automaton. To do this, go back to the ‘new’ menu and select automaton. First thing to do is to select a name and alphabet to use for this automaton. If you’d like to operate this automaton on the tree just defined you should select the same alphabet used previously. \n" + "Now you need to go through and define the transition rules. As we are working with bottom up finite tree automaton you will need 3 components for each transition rule. An element f, which is what the node is labelled, arity(f) current states, which are the child states of the node, a new state, which is what the node will transition to. The states are actively stored as you define rules, so can be any string. When you are finished defining rules click continue.\n" + "Finally you should select any number of final states. Once you click finish the tree automaton has been fully defined.\n"
           };
}
