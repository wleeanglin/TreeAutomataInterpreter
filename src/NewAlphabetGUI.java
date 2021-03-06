import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.ArrayList;

public class NewAlphabetGUI {

    private Utility u;
    private ArrayList<RankedAlphabet> alphabets;
    private RankedAlphabet r;


    public static void display(ArrayList<RankedAlphabet> alphabets, RankedAlphabet r){
        NewAlphabetGUI g = new NewAlphabetGUI();
        g.launch(alphabets, r);
    }

    public void launch(ArrayList<RankedAlphabet> alphabets, RankedAlphabet r){
        u = new Utility();
        this.alphabets = alphabets;
        this.r = r;

        Stage window = new Stage();

        getName(window);
    }

    public NewAlphabetGUI(){

    }

    public void getName(Stage window){
        Button cancelButton, continueButton;
        final TextField nameBox;

        Label newAlphabet = new Label("New Alphabet");
        Label enterName = new Label("Enter name -");
        enterName.setPrefHeight(20);

        nameBox = new TextField();
        nameBox.setPrefHeight(20);

        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> window.close());

        continueButton = new Button("continue");
        continueButton.setOnAction(e -> {
            if(!nameBox.getText().equals("") && !u.alphabetNameTaken(nameBox.getText(), this.alphabets)){
                r.setName(nameBox.getText());
                getAlphabets(window);
            } else if(nameBox.getText().equals("")){
                ErrorGUI.display("No input string entered for name. Please enter a valid string before continuing.");
                nameBox.clear();
            } else{
                ErrorGUI.display("Alphabet already exists of this name. Please enter a new alphabet name.");
                nameBox.clear();
            }
        });

        VBox vert = new VBox(10);
        HBox row2 = new HBox(10);
        HBox row3 = new HBox(10);

        row2.getChildren().addAll(enterName, nameBox);
        row3.getChildren().addAll(continueButton, cancelButton);
        vert.getChildren().addAll(newAlphabet, row2, row3);

        vert.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);
        row3.setAlignment(Pos.CENTER);
        Scene getNameScene = new Scene(vert, 400, 400);
        window.setTitle("New Alphabet");
        window.setMinWidth(400);
        window.setMinHeight(400);
        window.setScene(getNameScene);
        window.showAndWait();

    }

    //Errbuf handles errors from other classes
    public void getAlphabets(Stage window){
        StringBuffer errbuff = new StringBuffer();

        Button cancelButton, finishButton, addButton, removeButton;
        final TextField elementBox, arityBox;
        ListView<String> elementList;

        Label elementLabel = new Label("Element -");
        Label arityLabel = new Label("Arity -");
        elementLabel.setPrefHeight(20);
        arityLabel.setPrefHeight(20);
        elementBox = new TextField();
        arityBox = new TextField();
        elementBox.setPrefHeight(20);
        arityBox.setPrefHeight(20);

        elementList = new ListView<>();
        elementList.setPrefHeight(200);
        elementList.setPrefWidth(100);

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
