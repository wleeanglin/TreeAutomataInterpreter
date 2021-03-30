import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GUI extends Application{
    private Stage window;

    private ArrayList<RankedAlphabet> alphabets;
    private ArrayList<Tree> trees;
    private ArrayList<TreeAutomaton> automata;
    //private ArrayList<ArrayList<Tree>> histories;

    private Utility u;

    public static void main(String[] args){
        launch(args);
    }

    public GUI(){

    }

    public void start(Stage primaryStage) throws Exception {
        this.alphabets = new ArrayList<>();
        this.trees = new ArrayList<>();
        this.automata = new ArrayList<>();
        //this.histories = new ArrayList<>();
        this.u = new Utility();
        ArrayList<RankedAlphabet> exampleAlphs = u.getExampleAlphabets();
        this.alphabets.addAll(exampleAlphs);
        ArrayList<Tree> exampleTrees = u.getExampleTrees(this.alphabets);
        this.trees.addAll(exampleTrees);
        ArrayList<TreeAutomaton> exampleAutomata = u.getAutomataExamples(this.alphabets);
        this.automata.addAll(exampleAutomata);
        mainMenu(primaryStage);
    }

    public void mainMenu(Stage primaryStage){
        window = primaryStage;
        Button newButton, modifyButton, operateButton, tutorialButton;

        Label title = new Label("Main Menu");
        newButton = new Button("new");
        newButton.setOnAction(e -> newMenu(window));

        modifyButton = new Button("modify");
        modifyButton.setOnAction(e -> modifyMenu(window));

        operateButton = new Button("operate");
        operateButton.setOnAction(e -> operateMenu(window));

        tutorialButton = new Button("tutorial");
        tutorialButton.setOnAction(e -> tutorialMenu(window));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(title, newButton, modifyButton, operateButton, tutorialButton);
        layout.setAlignment(Pos.CENTER);
        Scene mainMenu = new Scene(layout, 400, 400);

        window.setTitle("Tree Automata");
        window.setMinHeight(300);
        window.setMinWidth(100);
        window.setScene(mainMenu);
        window.show();
    }

    public void newMenu(Stage window){
        Button alphabetButton, treeButton, automatonButton, backButton;

        Label title = new Label("New");
        alphabetButton = new Button("alphabet");
        alphabetButton.setOnAction(e -> {
            RankedAlphabet r = new RankedAlphabet();
            NewAlphabetGUI.display(this.alphabets, r);
            if(r.getComplete()) {
                this.alphabets.add(r);
            }
        });

        treeButton = new Button("tree");
        treeButton.setOnAction(e -> {
            //Check if alphabet defined maybe
            Tree t = new Tree();
            NewTreeGUI.display(this.alphabets, this.trees, t);
            if(t.getComplete()){
                this.trees.add(t);
            }
        });

        automatonButton = new Button("automaton");
        automatonButton.setOnAction(e -> {
            TreeAutomaton t = new TreeAutomaton();
            NewAutomatonGUI.display(this.alphabets, this.automata, t);
            if(t.getComplete()){
                this.automata.add(t);
            }
        });

        backButton = new Button("back");
        backButton.setOnAction(e -> mainMenu(window));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(title, alphabetButton, treeButton, automatonButton, backButton);
        layout.setAlignment(Pos.CENTER);
        Scene newMenu = new Scene(layout, 400, 400);

        window.setScene(newMenu);
        window.show();
    }

    public void modifyMenu(Stage window){
        Button alphabetButton, treeButton, automatonButton, backButton;

        //When modifying do this.setComplete(false);
        Label title = new Label("Modify");
        alphabetButton = new Button("alphabet");
        alphabetButton.setOnAction(e -> {
            ModifyAlphabetGUI.display(this.alphabets);
        });

        automatonButton = new Button("automaton");
        automatonButton.setOnAction(e -> {
            ModifyAutomatonGUI.display(this.automata);
        });

        backButton = new Button("back");
        backButton.setOnAction(e -> mainMenu(window));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(title, alphabetButton, automatonButton, backButton);
        layout.setAlignment(Pos.CENTER);
        Scene modifyMenu = new Scene(layout, 400, 400);

        window.setScene(modifyMenu);
        window.show();
    }

    public void operateMenu(Stage window){
        Button operateButton, backButton, automataInfo, treeInfo;

        ListView<String> automataList = new ListView<>();
        ListView<String> treeList = new ListView<>();
        automataList.setPrefHeight(200); automataList.setPrefWidth(175);
        treeList.setPrefHeight(200); treeList.setPrefWidth(175);

        ObservableList<String> automataListItems = FXCollections.observableArrayList(u.getAutomataNames(this.automata));
        ObservableList<String> treeListItems = FXCollections.observableArrayList(u.getTreeNames(this.trees));
        automataList.setItems(automataListItems);
        treeList.setItems(treeListItems);

        Label automata = new Label("automata -");
        Label trees = new Label("trees -");
        automata.setTextAlignment(TextAlignment.LEFT);
        trees.setTextAlignment(TextAlignment.RIGHT);


        automataInfo = new Button("automaton info");
        automataInfo.setOnAction(e ->{
           if(automataList.getSelectionModel().getSelectedIndex() >= 0){
               objectInformationGUI.displayAutomaton(this.automata.get(automataList.getSelectionModel().getSelectedIndex()));
           }
        });

        treeInfo = new Button("tree info");
        treeInfo.setOnAction(e ->{
            if(treeList.getSelectionModel().getSelectedIndex() >= 0){
                objectInformationGUI.displayTree(this.trees.get(treeList.getSelectionModel().getSelectedIndex()));
            }
        });

        operateButton = new Button("operate");
        operateButton.setOnAction(e ->{
            if(!(automataList.getSelectionModel().getSelectedIndex() >= 0)){
                ErrorGUI.display("No automaton selected.");
            } else if(!(treeList.getSelectionModel().getSelectedIndex() >= 0)){
                ErrorGUI.display("No tree selected.");
            } else{
                Tree t = this.trees.get(treeList.getSelectionModel().getSelectedIndex());
                TreeAutomaton a = this.automata.get(automataList.getSelectionModel().getSelectedIndex());
                if(!t.getAlphabet().compare(a.getAlphabet())){
                    ErrorGUI.display("Automaton and tree defined using different ranked alphabets.");
                } else{
                    StringBuffer errbuf = new StringBuffer();
                    ArrayList<Tree> history = a.operateAutomata(t, errbuf, true);
                    historyDisplayGUI.display(history, errbuf.toString());
                }
            }
        });

        backButton = new Button("back");
        backButton.setOnAction(e -> mainMenu(window));

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

        row1.getChildren().addAll(automata, trees);
        row2.getChildren().addAll(automataList, treeList);
        row3.getChildren().addAll(automataInfo, treeInfo);
        row4.getChildren().addAll(operateButton, backButton);
        vert.getChildren().addAll(row1, row2, row3, row4);

        Scene operateMenu = new Scene(vert, 400, 400);
        window.setScene(operateMenu);
    }

    public void tutorialMenu(Stage window){
        Button information, examples, walkthrough, back;

        information = new Button("tree automata information");
        information.setOnAction(e -> {
            InformationGUI.display(alphabets.get(0), trees.get(0), automata.get(0));
        });

        examples = new Button("provided example work-through");
        examples.setOnAction(e -> {
            ExamplesGUI.display(this.automata);
        });

        walkthrough = new Button("tool walk-through");
        walkthrough.setOnAction(e ->{
            WalkThroughGUI.display();
        });

        back = new Button("back");
        back.setOnAction(e -> mainMenu(window));


        VBox vert = new VBox(10);
        vert.setAlignment(Pos.CENTER);
        vert.getChildren().addAll(information, examples, walkthrough, back);

        Scene tutorialScene = new Scene(vert, 400, 400);
        window.setScene(tutorialScene);
    }

}
