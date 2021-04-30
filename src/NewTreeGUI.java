import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.ArrayList;

public class NewTreeGUI {
    private ArrayList<RankedAlphabet> alphabets;
    private ArrayList<Tree> trees;

    private Tree t;
    private RankedAlphabet r;
    private Utility u;

    public static void display(ArrayList<RankedAlphabet> alphabets, ArrayList<Tree> trees, Tree t){
        NewTreeGUI g = new NewTreeGUI();
        g.launch(alphabets, trees, t);
    }

    public void launch(ArrayList<RankedAlphabet> alphabets, ArrayList<Tree> trees, Tree t){
        u = new Utility();
        this.alphabets = alphabets;
        this.trees = trees;
        this.t = t;

        Stage window = new Stage();

        getName(window);
    }

    public void getName(Stage window){
        Button cancelButton, continueButton;
        final TextField nameBox;

        Label newAlphabet = new Label("New Tree");
        Label enterName = new Label("Enter name -");

        nameBox = new TextField();

        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> window.close());

        continueButton = new Button("continue");
        continueButton.setOnAction(e -> {
            if(!nameBox.getText().equals("") && !u.treeNameTaken(nameBox.getText(), this.trees)){
                t.setName(nameBox.getText());
                getAlphabetName(window);
            } else if(nameBox.getText().equals("")){
                ErrorGUI.display("No input string entered for name. Please enter a valid string before continuing.");
                nameBox.clear();
            } else{
                ErrorGUI.display("Tree already exists of this name. Please enter a new tree name.");
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
        window.setTitle("New Tree");
        window.setMinWidth(400);
        window.setMinHeight(400);
        window.setScene(getNameScene);
        window.showAndWait();
    }

    public void getAlphabetName(Stage window){
        Button cancelButton, continueButton, showInfo;

        ListView<String> alphabetList;

        Label selectAlphabet = new Label("Select Alphabet");

        alphabetList = new ListView<>();
        alphabetList.setPrefHeight(200);
        alphabetList.setPrefWidth(175);
        ObservableList<String> alphabetsDisplay = FXCollections.observableArrayList(u.getAlphabetNames(this.alphabets));
        alphabetList.setItems(alphabetsDisplay);

        showInfo = new Button("Show info");
        showInfo.setOnAction(e -> {
            if(alphabetList.getSelectionModel().getSelectedIndex() >= 0){
                objectInformationGUI.displayAlphabet(this.alphabets.get(alphabetList.getSelectionModel().getSelectedIndex()));
            }
        });

        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> window.close());

        continueButton = new Button("continue");
        continueButton.setOnAction(e -> {
            if(alphabetList.getSelectionModel().getSelectedIndex() >= 0){
                this.r = this.alphabets.get(alphabetList.getSelectionModel().getSelectedIndex());
                this.t.setAlphabet(r);
                buildTree(window);
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
        buttons.getChildren().addAll(showInfo, continueButton, cancelButton);
        row2.getChildren().addAll(buttons, alphabetList);
        vert.getChildren().addAll(row1, row2);

        Scene getAlphabetScene = new Scene(vert, 400, 400);
        window.setScene(getAlphabetScene);
    }

    public void buildTree(Stage window){
        Button addButton, cancelButton;

        ListView<String> elementList = new ListView<>();
        elementList.setPrefHeight(200);
        elementList.setPrefWidth(100);
        ObservableList<String> newElements = FXCollections.observableArrayList(u.concatAlphArray(r.getAlph(), r.getArityArray()));
        elementList.setItems(newElements);

        Label tree = new Label();
        tree.setMinHeight(200);
        tree.setMinWidth(200);

        addButton = new Button("add");
        addButton.setOnAction(e -> {
            if(!t.getComplete()){
                t.addNextNode((String)r.getAlph().get(elementList.getSelectionModel().getSelectedIndex()), (int)r.getArityArray().get(elementList.getSelectionModel().getSelectedIndex()));
                tree.setText(t.getQuestionTree());
                if(t.getComplete()){
                    addButton.setText("finish");
                }
            } else{
                t.setComplete(true);
                window.close();
            }
        });

        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e ->{
            window.close();
        });

        VBox vert = new VBox(10);
        HBox row1 = new HBox(10);
        HBox row2 = new HBox(10);
        vert.setAlignment(Pos.CENTER);
        row1.setAlignment(Pos.CENTER);
        row2.setAlignment(Pos.CENTER);

        row1.getChildren().addAll(elementList, tree);
        row2.getChildren().addAll(addButton, cancelButton);
        vert.getChildren().addAll(row1, row2);

        Scene buildTreeScene = new Scene(vert, 400, 400);
        window.setScene(buildTreeScene);
    }
}
