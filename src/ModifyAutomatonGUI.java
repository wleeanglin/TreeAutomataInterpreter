import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.ArrayList;

public class ModifyAutomatonGUI {
    private Utility u;
    private ArrayList<TreeAutomaton> automata;
    private RankedAlphabet r;
    private TreeAutomaton t;

    public static void display(ArrayList<TreeAutomaton> automata){
        ModifyAutomatonGUI g = new ModifyAutomatonGUI();
        g.launch(automata);
    }

    public ModifyAutomatonGUI(){

    }

    public void launch(ArrayList<TreeAutomaton> automata){
        this.automata = automata;
        this.u = new Utility();

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        getAutomatonName(window);
    }

    public void getAutomatonName(Stage window){
        Button cancelButton, continueButton;

        ListView<String> automatonList;

        Label selectAlphabet = new Label("Select Automaton");

        automatonList = new ListView<>();
        automatonList.setPrefHeight(200);
        automatonList.setPrefWidth(175);
        ObservableList<String> automatonDisplay = FXCollections.observableArrayList(u.getAutomataNames(this.automata));
        automatonList.setItems(automatonDisplay);

        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> window.close());

        continueButton = new Button("continue");
        continueButton.setOnAction(e -> {
            if(automatonList.getSelectionModel().getSelectedIndex() >= 0){
                if(this.automata.get(automatonList.getSelectionModel().getSelectedIndex()).getModifiable()){
                    this.t = this.automata.get(automatonList.getSelectionModel().getSelectedIndex());
                    this.r = this.t.getAlphabet();
                    modifyRules(window);
                } else{
                    ErrorGUI.display("Example alphabets/automata not modifiable");
                }
            }
        });

        VBox buttons = new VBox(10);
        VBox vert = new VBox(10);
        HBox row1 = new HBox(10);
        HBox row2 = new HBox(10);

        buttons.setAlignment(Pos.CENTER);
        vert.setAlignment(Pos.CENTER);
        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);

        row1.getChildren().addAll(selectAlphabet);
        buttons.getChildren().addAll(continueButton, cancelButton);
        row2.getChildren().addAll(buttons, automatonList);
        vert.getChildren().addAll(row1, row2);

        Scene getAlphabetScene = new Scene(vert, 400, 400);
        window.setTitle("Modify Automaton");
        window.setScene(getAlphabetScene);
        window.showAndWait();
    }

    public void modifyRules(Stage window){
        Button addRule, removeRule, continueButton, cancelButton;
        Label element, currentStates, newState;
        TextField elementBox, currentStatesBox, newStateBox;

        ListView<String> ruleList = new ListView<>();
        ruleList.setPrefHeight(300);
        ruleList.setPrefWidth(200);
        ObservableList<String> rl = FXCollections.observableArrayList(t.printRulesGUI());
        ruleList.setItems(rl);

        element = new Label("Element -");
        currentStates = new Label("Current States -");
        newState = new Label("New State -");
        elementBox = new TextField();
        currentStatesBox = new TextField();
        newStateBox = new TextField();

        addRule = new Button("add");
        addRule.setOnAction(e ->{
            if(elementBox.getText().equals("")) {
                ErrorGUI.display("Element box empty. Please give an element string before adding rule.");
            } else if(newStateBox.getText().equals("")){
                ErrorGUI.display("New state box empty. Please give an arity for element before adding rule.");
            } else if(elementBox.getText().contains(" ")) {
                ErrorGUI.display("No spaces allowed in element strings.");
            } else if(newStateBox.getText().contains(" ")){
                ErrorGUI.display("No spaces allowed in new state strings.");
            } else if(!this.r.getAlph().contains(elementBox.getText().toLowerCase())) {
                ErrorGUI.display("Element " + elementBox.getText().toLowerCase() + " not found in alphabet " + r.getName());
            }
            else{
                ArrayList<String> currentList = new ArrayList<>();
                if(!currentStatesBox.getText().equals("")){
                    String[] current = currentStatesBox.getText().split("\\s+");
                    for(int i = 0; i < current.length; i++){
                        currentList.add(current[i]);
                    }
                }
                if(currentList.size() != r.getArity(elementBox.getText().toLowerCase())){
                    ErrorGUI.display("Incorrect number current states entered for element " + elementBox.getText().toLowerCase() + ". This element needs " + r.getArity(elementBox.getText().toLowerCase()) + " current states.");
                } else {
                    TransitionRule rule = new TransitionRule(elementBox.getText().toLowerCase(), r.getArity( elementBox.getText().toLowerCase()));
                    rule.setCurrentStates(currentList);
                    rule.setNewState(newStateBox.getText().toLowerCase());
                    if(t.checkDuplicateRule(elementBox.getText().toLowerCase(), currentList)){
                        ErrorGUI.display("Rule already defined for this element + curent state combination. DFTA can't have two rules with the same left hand side.");
                    } else {
                        t.addRule(rule);
                        elementBox.clear(); currentStatesBox.clear(); newStateBox.clear();
                        ObservableList<String> rules = FXCollections.observableArrayList(t.printRulesGUI());
                        ruleList.setItems(rules);
                    }
                }
            }
        });

        removeRule = new Button("remove");
        removeRule.setOnAction(e -> {
            if(ruleList.getSelectionModel().getSelectedIndex() >= 0) {
                t.removeRule(this.t.getRules().get(ruleList.getSelectionModel().getSelectedIndex()));
                ObservableList<String> rules = FXCollections.observableArrayList(t.printRulesGUI());
                ruleList.setItems(rules);
            }
        });

        continueButton = new Button("continue");
        continueButton.setOnAction(e -> {
            modifyFinalStates(window);
        });


        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> {
            window.close();
        });

        VBox vert = new VBox(10);
        HBox row1 = new HBox(10);
        HBox row2 = new HBox(10);
        HBox row3 = new HBox(10);
        vert.setAlignment(Pos.CENTER);
        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);
        row3.setAlignment(Pos.CENTER);

        HBox inputLayout = new HBox(10);
        inputLayout.setAlignment(Pos.CENTER);
        VBox v1 = new VBox(15);
        VBox v2 = new VBox(10);
        v1.setAlignment(Pos.CENTER_LEFT);
        v2.setAlignment(Pos.CENTER_RIGHT);
        HBox v1r1 = new HBox(10);
        HBox v1r2 = new HBox(10);
        HBox v1r3 = new HBox(10);
        HBox v2r1 = new HBox(10);
        HBox v2r2 = new HBox(10);
        HBox v2r3 = new HBox(10);

        v1r1.getChildren().add(element);
        v1r2.getChildren().add(currentStates);
        v1r3.getChildren().add(newState);
        v2r1.getChildren().add(elementBox);
        v2r2.getChildren().add(currentStatesBox);
        v2r3.getChildren().add(newStateBox);

        v1.getChildren().addAll(v1r1, v1r2, v1r3);
        v2.getChildren().addAll(v2r1, v2r2, v2r3);

        inputLayout.getChildren().addAll(v1, v2);

        row1.getChildren().addAll(inputLayout, ruleList);
        row2.getChildren().addAll(addRule, removeRule);
        row3.getChildren().addAll(continueButton, cancelButton);
        vert.getChildren().addAll(row1, row2, row3);

        Scene buildAutomatonScene = new Scene(vert, 600, 400);
        window.setScene(buildAutomatonScene);

    }

    public void modifyFinalStates(Stage window){
        Button addButton, removeButton, finishButton, cancelButton;
        Label existingStates, finalStates;

        existingStates = new Label("Existing States -");
        finalStates = new Label("Final States -");

        ListView<String> existingStateList = new ListView<>();
        ListView<String> finalStateList = new ListView<>();
        existingStateList.setPrefHeight(200); existingStateList.setPrefWidth(100);
        finalStateList.setPrefHeight(200); finalStateList.setPrefWidth(100);

        ObservableList<String> existingStateListItems = FXCollections.observableArrayList(t.getStates());
        existingStateList.setItems(existingStateListItems);

        ObservableList<String> fs = FXCollections.observableArrayList(t.getFinStates());
        finalStateList.setItems(fs);

        addButton = new Button("add");
        addButton.setOnAction(e -> {
            if(existingStateList.getSelectionModel().getSelectedIndex() >= 0){
                if(!t.getFinStates().contains(t.getStates().get(existingStateList.getSelectionModel().getSelectedIndex()))){
                    t.addFinalState(t.getStates().get(existingStateList.getSelectionModel().getSelectedIndex()));
                    ObservableList<String> existingFinalStateItems = FXCollections.observableArrayList(t.getFinStates());
                    finalStateList.setItems(existingFinalStateItems);
                } else{
                    ErrorGUI.display("Final state already recorded.");
                }
            }
        });

        removeButton = new Button("remove");
        removeButton.setOnAction(e -> {
            if(finalStateList.getSelectionModel().getSelectedIndex() >= 0){
                t.removeFinalState(t.getFinStates().get(finalStateList.getSelectionModel().getSelectedIndex()));
                ObservableList<String> existingFinalStateItems = FXCollections.observableArrayList(t.getFinStates());
                finalStateList.setItems(existingFinalStateItems);
            }
        });

        finishButton = new Button("finish");
        finishButton.setOnAction(e -> {
            t.setComplete(true);
            window.close();
        });

        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> window.close());

        VBox vert = new VBox(10);
        HBox row1 = new HBox(10);
        HBox row2 = new HBox(10);
        HBox row3 = new HBox(10);
        HBox row4 = new HBox(10);
        vert.setAlignment(Pos.CENTER);
        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);
        row3.setAlignment(Pos.CENTER);
        row4.setAlignment(Pos.CENTER);

        row1.getChildren().addAll(existingStates, finalStates);
        row2.getChildren().addAll(existingStateList, finalStateList);
        row3.getChildren().addAll(addButton, removeButton);
        row4.getChildren().addAll(finishButton, cancelButton);
        vert.getChildren().addAll(row1, row2, row3, row4);

        Scene getFinalStatesScene = new Scene(vert, 400, 400);
        window.setScene(getFinalStatesScene);
    }

}
