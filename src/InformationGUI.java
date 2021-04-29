import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class InformationGUI {
    private Tree t;
    private RankedAlphabet a;
    private TreeAutomaton b;

    public static void display(RankedAlphabet a, Tree t, TreeAutomaton b){
        InformationGUI g = new InformationGUI();
        g.launch(a, t, b);
    }

    public void launch(RankedAlphabet a, Tree t, TreeAutomaton b){
        this.t = t;
        this.a = a;
        this.b = b;

        Stage window = new Stage();
        showInfo(window);
    }

    public void showInfo(Stage window) {
        Button forward, backward, exit;

        //ugly hack again
        int counter[] = {0};

        Label infoText = new Label();
        infoText.setText(getString(counter[0]));
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
                infoText.setText(getString(counter[0]));
                statusText.setText("Step " + (counter[0] + 1) + "/" + text.length);
            } else {
                ErrorGUI.display("No more steps");
            }
        });

        backward = new Button("<");
        backward.setOnAction(e -> {
            if (counter[0] > 0) {
                counter[0]--;
                infoText.setText(getString(counter[0]));
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

        Scene infoScene = new Scene(vert, 400, 500);
        window.setTitle("Tree Automata Information");
        window.setMinWidth(400);
        window.setMinHeight(500);
        window.setScene(infoScene);
        window.showAndWait();
    }

    public String getString(int i){
        StringBuilder s = new StringBuilder();
        s.append(text[i]);
        if(i == 1){
            s.append(this.a.getName());
            s.append(" - \n");
            s.append("Element: \t Arity:\n");
            for(int j = 0; j < a.getAlph().size(); j++){
                s.append(a.getAlph().get(j));
                s.append("\t");
                s.append(a.getArityArray().get(j));
                s.append("\n");
            }
        } else if(i == 2){
            s.append(t.getName());
            s.append(" - \n");
            s.append(t.guiPrint());
            s.append("\n");
        } else if(i == 3){
            s.append(b.getName());
            s.append(" - \n");
            s.append("Rules -\n");
            ArrayList<String> c = b.printRulesGUI();
            for(int j = 0; j < c.size(); j++){
                s.append(c.get(j));
                s.append("\n");
            }
            s.append("Final states - ");
            for(int j = 0; j < b.getFinStates().size(); j++){
                s.append(b.getFinStates().get(j));
                s.append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    //Information for printing
    private static String[] text = {
            "Tree automata are an expansion to finite state automata that work on tree structures instead of the strings that are normally dealt with by more conventional finite state automata. \n" + "This tool visualises the acceptance process for bottom up deterministic tree automata. To understand the definition of a bottom up deterministic tree automaton we will introduce the elements required for a simple operation using the tools included examples.\n",
            "A ranked alphabet is a couple (F, Arity) where F is a finite, nonempty alphabet and Arity is a mapping from F to the natural numbers, that is each element of the alphabet has an associated natural number. The arity of an element in F represents the number of child nodes this element will have in our trees. When defining a ranked alphabet there must be at least one element defined with arity 0, else any tree defined would go on infinitely. Here is one of this tool's example alphabets:\n",
            "A tree in our context is simply a hierarchical data structure consisting of nodes which have zero or more child nodes. We use elements of a ranked alphabet to define a tree where a node with a label of an element of f in F has Arity(f) child nodes. Here is an example tree defined using ExampleAlphabet1. Notice all the leaf nodes are ‘a’ as ‘a’ is the only element of our ranked alphabet with arity 0.\n",
    "A bottom-up deterministic finite tree automaton is defined as a tuple containing the following;\n" +
            "-Q, a set of a unary states (i.e. arity=1) \n" +
            "-F, a ranked alphabet\n" +
            "-Qf, a set of final states\n" +
            "-Δ, a set of transition rules\n" +
            "As we are working with bottom-up finite tree automata the transition rules are of the form f(q1(x1),...,qn(xn)) → q(f(x1,...,xn)) where f is an element of our ranked alphabet with arity n, qi are elements of our set of states and xi are subtrees. That is, the state of a node is determined by the state of its children, i.e. ‘bottom-up’. Here is an example of an example bottom-up tree automaton included with the tool defined using ranked alphabet ExampleAlphabet1. \n",
    "This tool visualises the acceptance process for a tree passed into a tree automaton. This process consists of the automaton working on a tree using its transition rules until either there are no more transition rules that can be applied or then root node is a state. This state is then checked against the set of final states to see whether the tree was ‘accepted’ or ‘rejected’. To see an example of the operation process in action click ‘provided example work-through’ in the tutorial menu."};
}
