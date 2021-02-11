import java.util.ArrayList;
import java.util.Collections;

public class TreeAutomaton {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    private ArrayList<String> states;
    private RankedAlphabet alphabet;
    private ArrayList<String> finStates;
    private ArrayList<TransitionRule> rules;
    private String name;

    public TreeAutomaton(String s, RankedAlphabet a){
        this.name = s;
        this.alphabet = a;
        this.rules = new ArrayList<>();
        this.states = new ArrayList<>();
        this.finStates = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<String> getStates(){
        return this.states;
    }

    public ArrayList<String> getFinStates(){
        return this.finStates;
    }

    public RankedAlphabet getAlphabet(){
        return this.alphabet;
    }

    public void addRule(TransitionRule r){
        rules.add(r);
        for(int i = 0; i < r.getCurrentStates().size(); i++){
            if(!states.contains(r.getCurrentStates().get(i))){
                states.add(r.getCurrentStates().get(i));
            }
        }
        if(!states.contains(r.getNewState())){
            states.add(r.getNewState());
        }
    }

    public void addFinalState(String s){
        this.finStates.add(s);
    }

    public Boolean checkDuplicateRule(String element, ArrayList<String> states){
        Collections.sort(states);
        for(int i = 0; i < rules.size(); i++){
            ArrayList<String> a = (ArrayList<String>) rules.get(i).getCurrentStates().clone();
            Collections.sort(a);
            if(rules.get(i).getElement().equals(element) && a.equals(states)){
                return true;
            }
        }
        return false;
    }

    public TransitionRule getRule(String data, ArrayList<String> children){
        Collections.sort(children);
        for(int i = 0; i < rules.size(); i++){
            ArrayList<String> a = (ArrayList<String>) rules.get(i).getCurrentStates().clone();
            Collections.sort(a);
            if(rules.get(i).getElement().equals(data) && a.equals(children)){
                return this.rules.get(i);
            }
        }
        return null;
    }

    public void printRules(){

        for(int i = 0; i < rules.size(); i++){
            //Rule 1 = index 0
            System.out.printf(ANSI_YELLOW);
            System.out.printf("Rule %d ", (i+1));
            System.out.printf("%s", rules.get(i).getElement());
            System.out.printf("(");
            int j = 0;
            //(q1 q2 q3)
            for(; j < rules.get(i).getCurrentStates().size() - 1; j++){
                System.out.printf("%s ", rules.get(i).getCurrentStates().get(j));
            }
            if(rules.get(i).getCurrentStates().size() > 0){
                System.out.printf("%s", rules.get(i).getCurrentStates().get(j));
            }
            System.out.printf(") ");
            System.out.printf("-> ");
            System.out.printf("%s\n", rules.get(i).getNewState());
            System.out.printf(ANSI_RESET);
        }

    }

    public ArrayList<Tree> operateAutomata(Tree t, StringBuffer errbuf){
        ArrayList<Tree> history = new ArrayList<>();
        history.add(t);

        int h = t.getMaxHeight() - 1;
        int w = 0;

        while(true) {
            Tree b = history.get(history.size() - 1).copyTree();
            ArrayList<ArrayList<Tree.Node>> levels = b.convert();
            if(w >= levels.get(h).size()){
                w = 0;
                h--;
                if(h < 0) {
                    if(this.getFinStates().contains(b.getRoot().getData())){
                        errbuf.append(ANSI_GREEN + "Tree '" + b.getName() + "' with final state '" + b.getRoot().getData() + "'" + ANSI_CYAN + " ACCEPTED " + ANSI_GREEN + "by automaton '" + this.getName() + "'.\n" + ANSI_RESET);
                    } else{
                        errbuf.append(ANSI_GREEN + "Tree '" + b.getName() + "' with final state '" + b.getRoot().getData() + "'" + ANSI_RED + " REJECTED " + ANSI_GREEN + "by automaton '" + this.getName() + "'.\n" + ANSI_RESET);
                    }
                    break;
                }
            }
            b = nextStep(b, levels, h, w, errbuf);
            w++;
            if(b != null){
                history.add(b);
            } else{
                break;
            }
        }

        return history;
    }

    public Tree nextStep(Tree t, ArrayList<ArrayList<Tree.Node>> levels, int h, int w, StringBuffer errbuf){
        Tree.Node n = levels.get(h).get(w);
        TransitionRule r = this.getRule(n.getData(), n.getChildrenAsString());
        if(r != null){
            //This part is for states so it gets rid of them :)
            for(int i = 0; i < n.getChildren().size(); i++){
                if(n.getChildren().get(i).getState()){
                    n.getChildren().set(i, n.getChildren().get(i).getChildren().get(0));
                    n.getChildren().get(i).setParent(n);
                }
            }
            Tree.Node newNode = new Tree.Node(r.getNewState(), 1, n.getParent());
            newNode.setState(true);
            newNode.addChildNode(n);
            if(n.getParent() != null){
                for(int i = 0; i < n.getParent().getChildren().size(); i++){
                    if(n.getParent().getChildren().get(i).equals(n)){
                        //if(n.getParent().getChildren().get(i).getData().equals(n.getData()) && n.getParent().getChildren().get(i).getChildren().equals(n.getChildren())){
                        n.getParent().getChildren().set(i, newNode);
                    }
                }
            } else {
                //set root of t as newNode
                t.setRoot(newNode);
            }

            n.setParent(newNode);
        } else {
            errbuf.append(ANSI_RED + "No rule defined in automaton " + name + " for node " + n.getData() + " with children ");
            for (int i = 0; i < n.getChildren().size() - 1; i++) {
                errbuf.append(n.getChildren().get(i).getData() + ", ");
            }
            errbuf.append(n.getChildren().get(n.getChildren().size() - 1).getData() + ".\n" + ANSI_RESET);
            return null;
        }

        return t;
    }

    public void printConvert(ArrayList<ArrayList<Tree.Node>> levels){
        for(int i = 0; i < levels.size(); i++){
            for(int j = 0; j < levels.get(i).size(); j++){
                System.out.printf("%s ", levels.get(i).get(j).getData());
            }
            System.out.println();
        }
    }

}
