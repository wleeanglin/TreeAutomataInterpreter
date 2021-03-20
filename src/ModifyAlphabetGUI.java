import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.ArrayList;

public class ModifyAlphabetGUI {
    private Utility u;
    private ArrayList<RankedAlphabet> alphabets;
    private RankedAlphabet r;

    public static void display(ArrayList<RankedAlphabet> alphabets){
        ModifyAlphabetGUI g = new ModifyAlphabetGUI();
        g.launch(alphabets);
    }

    public ModifyAlphabetGUI(){

    }

    public void launch(ArrayList<RankedAlphabet> alphabets){
        this.alphabets = alphabets;
        this.u = new Utility();

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        getAlphabetName(window);
    }

    public void getAlphabetName(Stage window){
        Button cancelButton, continueButton;

        ListView<String> alphabetList;

        Label selectAlphabet = new Label("Select Alphabet");

        alphabetList = new ListView<String>();
        alphabetList.setPrefHeight(200);
        alphabetList.setPrefWidth(175);
        ObservableList<String> alphabetsDisplay = FXCollections.observableArrayList(u.getAlphabetNames(this.alphabets));
        alphabetList.setItems(alphabetsDisplay);

        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> window.close());

        continueButton = new Button("continue");
        continueButton.setOnAction(e -> {
            if(alphabetList.getSelectionModel().getSelectedIndex() >= 0){
                this.r = this.alphabets.get(alphabetList.getSelectionModel().getSelectedIndex());
                modifyAlphabet(window);
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
        row2.getChildren().addAll(buttons, alphabetList);
        vert.getChildren().addAll(row1, row2);

        Scene getAlphabetScene = new Scene(vert, 400, 400);
        window.setTitle("Modify Alphabet");
        window.setScene(getAlphabetScene);
        window.showAndWait();
    }

    public void modifyAlphabet(Stage window){
        StringBuffer errbuff = new StringBuffer();

        Button cancelButton, finishButton, addButton, removeButton;
        final TextField elementBox, arityBox;
        ListView<String> elementList;

        Label elementLabel = new Label("Element;");
        Label arityLabel = new Label("Arity;");
        elementBox = new TextField();
        arityBox = new TextField();

        elementList = new ListView<String>();
        elementList.setPrefHeight(200);
        elementList.setPrefWidth(100);
        ObservableList<String> el = FXCollections.observableArrayList(u.concatAlphArray(r.getAlph(), r.getArityArray()));
        elementList.setItems(el);

        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> window.close());

        finishButton = new Button("finish");
        finishButton.setOnAction(e -> {
            if(r.containsNullary()){
                r.setComplete(true);
                window.close();
            } else{
                ErrorGUI.display("No nullary element defined. Please add a nullary element before continuing.");
            }
        });

        addButton = new Button("add");
        addButton.setOnAction(e -> {
            if(elementBox.getText().equals("")){
                ErrorGUI.display("Element box empty. Please give an element string before adding.");
            } else if(arityBox.getText().equals("")){
                ErrorGUI.display("Arity box empty. Please give an arity for element before adding.");
            } else if(!r.checkInput(elementBox.getText().toLowerCase(), arityBox.getText().toLowerCase(), errbuff)){
                ErrorGUI.display(errbuff.toString());
                errbuff.setLength(0);
            } else{
                r.add(elementBox.getText().toLowerCase().replaceAll("\\s+", ""), Integer.parseInt(arityBox.getText().toLowerCase()));
                ObservableList<String> newElements = FXCollections.observableArrayList(u.concatAlphArray(r.getAlph(), r.getArityArray()));
                elementList.setItems(newElements);
                elementBox.clear();
                arityBox.clear();
            }
        });

        removeButton = new Button("remove");
        removeButton.setOnAction(e -> {
            if(elementList.getSelectionModel().getSelectedIndex() >= 0){
                String s = elementList.getSelectionModel().getSelectedItem().split(" ")[0];
                if(r.contains(s)){
                    r.remove(r.getIndex(s));
                }
            }
            ObservableList<String> newElements = FXCollections.observableArrayList(u.concatAlphArray(r.getAlph(), r.getArityArray()));
            elementList.setItems(newElements);
        });

        VBox inputVert = new VBox(10);
        HBox inputRow1 = new HBox(10);
        HBox inputRow2 = new HBox(10);
        inputVert.setAlignment(Pos.CENTER);
        inputRow1.setAlignment(Pos.CENTER);
        inputRow2.setAlignment(Pos.CENTER);

        VBox vert = new VBox(10);
        HBox row1 = new HBox(10);
        HBox row2 = new HBox(10);
        HBox row3 = new HBox(10);

        vert.setAlignment(Pos.CENTER);
        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);
        row3.setAlignment(Pos.CENTER);

        inputRow1.getChildren().addAll(elementLabel, elementBox);
        inputRow2.getChildren().addAll(arityLabel, arityBox);
        inputVert.getChildren().addAll(inputRow1, inputRow2);

        row1.getChildren().addAll(inputVert, elementList);

        row2.getChildren().addAll(addButton, removeButton);
        row3.getChildren().addAll(finishButton, cancelButton);

        vert.getChildren().addAll(row1, row2, row3);

        Scene getElementsScene = new Scene(vert, 400, 400);
        window.setScene(getElementsScene);
    }
}
