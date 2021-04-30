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

        alphabetList = new ListView<>();
        alphabetList.setPrefHeight(200);
        alphabetList.setPrefWidth(175);
        ObservableList<String> alphabetsDisplay = FXCollections.observableArrayList(u.getAlphabetNames(this.alphabets));
        alphabetList.setItems(alphabetsDisplay);

        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> window.close());

        continueButton = new Button("continue");
        continueButton.setOnAction(e -> {
            if(alphabetList.getSelectionModel().getSelectedIndex() >= 0){
                if(this.alphabets.get(alphabetList.getSelectionModel().getSelectedIndex()).getModifiable()){
                    this.r = this.alphabets.get(alphabetList.getSelectionModel().getSelectedIndex());
                    modifyAlphabet(window);
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

        Label elementLabel = new Label("Element -");
        Label arityLabel = new Label("Arity -");
        elementBox = new TextField();
        arityBox = new TextField();

        elementList = new ListView<>();
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

        HBox inputHor = new HBox(10);
        VBox inputVertl = new VBox(10);
        VBox inputVertr = new VBox(10);
        HBox ivlr1 = new HBox(10);
        HBox ivlr2 = new HBox(10);
        HBox ivrr1 = new HBox(10);
        HBox ivrr2 = new HBox(10);

        inputHor.setAlignment(Pos.CENTER);
        inputVertl.setAlignment(Pos.CENTER_RIGHT);
        inputVertr.setAlignment(Pos.CENTER_LEFT);

        ivlr1.getChildren().add(elementLabel);
        ivlr2.getChildren().add(arityLabel);
        ivrr1.getChildren().add(elementBox);
        ivrr2.getChildren().add(arityBox);

        inputVertl.getChildren().addAll(ivlr1, ivlr2);
        inputVertr.getChildren().addAll(ivrr1, ivrr2);

        inputHor.getChildren().addAll(inputVertl, inputVertr);

        VBox vert = new VBox(10);
        HBox row1 = new HBox(10);
        HBox row2 = new HBox(10);
        HBox row3 = new HBox(10);

        vert.setAlignment(Pos.CENTER);
        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);
        row3.setAlignment(Pos.CENTER);

        row1.getChildren().addAll(inputHor, elementList);

        row2.getChildren().addAll(addButton, removeButton);
        row3.getChildren().addAll(finishButton, cancelButton);

        vert.getChildren().addAll(row1, row2, row3);

        Scene getElementsScene = new Scene(vert, 400, 400);
        window.setScene(getElementsScene);
    }
}
