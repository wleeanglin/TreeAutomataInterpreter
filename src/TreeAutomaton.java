import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.StringBuilder;

public class TreeAutomaton {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private ArrayList<String> states;
    private RankedAlphabet alphabet;
    private ArrayList<String> finStates;
    private ArrayList<TransitionRule> rules;
    private String name;

    //for GUI
    private Boolean complete = false;
    private Boolean modifiable = true;

    public TreeAutomaton(){
        this.rules = new ArrayList<>();
        this.states = new ArrayList<>();
        this.finStates = new ArrayList<>();
    }

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

    public void setName(String s){
        this.name = s;
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

    public void setAlphabet(RankedAlphabet a){
        this.alphabet = a;
    }

    public void setModifiable(Boolean b){
        this.modifiable = b;
    }

    public Boolean getModifiable(){
        return this.modifiable;
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

    public void removeRule(TransitionRule r){
        //Maybe do states too?
        rules.remove(r);
    }

    public void addFinalState(String s){
        this.finStates.add(s);
    }

    public void removeFinalState(String s){
        if(this.finStates.contains(s)){
            this.finStates.remove(s);
        }
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

    public int getRuleIndex(String element, ArrayList<String> states){
        Collections.sort(states);
        for(int i = 0; i < rules.size(); i++){
            ArrayList<String> a = (ArrayList<String>) rules.get(i).getCurrentStates().clone();
            Collections.sort(a);
            if(rules.get(i).getElement().equals(element) && a.equals(states)){
                return i;
            }
        }
        return -1;
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

    public ArrayList<TransitionRule> getRules(){
        return this.rules;
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

    public ArrayList<String> printRulesGUI(){
        ArrayList<String> rulesList = new ArrayList<>();
        StringBuilder b = new StringBuilder();
        for(int i = 0; i < rules.size(); i++){
            b.setLength(0);
            b.append(rules.get(i).getElement());
            b.append("(");
            int j = 0;
            //(q1 q2 q3)
            for(; j < rules.get(i).getCurrentStates().size() - 1; j++){
                b.append(rules.get(i).getCurrentStates().get(j));
                b.append(" ");
            }
            if(rules.get(i).getCurrentStates().size() > 0){
                b.append(rules.get(i).getCurrentStates().get(j));
            }
            b.append(") -> ");
            b.append(rules.get(i).getNewState());
            rulesList.add(b.toString());
        }
        return rulesList;
    }

    public ArrayList<Tree> operateAutomata(Tree t, StringBuffer errbuf, Boolean guiFlag){
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
                        if(!guiFlag){
                            errbuf.append(ANSI_GREEN + "Tree '" + b.getName() + "' with final state '" + b.getRoot().getData() + "'" + ANSI_CYAN + " ACCEPTED " + ANSI_GREEN + "by automaton '" + this.getName() + "'.\n" + ANSI_RESET);
                        } else{
                            errbuf.append("Tree '" + b.getName() + "' with final state '" + b.getRoot().getData() + "' ACCEPTED by automaton '" + this.getName() + "'.\n");
                        }
                    } else{
                        if(!guiFlag){
                            errbuf.append(ANSI_GREEN + "Tree '" + b.getName() + "' with final state '" + b.getRoot().getData() + "'" + ANSI_RED + " REJECTED " + ANSI_GREEN + "by automaton '" + this.getName() + "'.\n" + ANSI_RESET);
                        } else{
                            errbuf.append("Tree '" + b.getName() + "' with final state '" + b.getRoot().getData() + "' REJECTED by automaton '" + this.getName() + "'.\n");
                        }
                    }
                    break;
                }
            }
            b = nextStep(b, levels, h, w, errbuf, guiFlag);
            w++;
            if(b != null){
                history.add(b);
            } else{
                break;
            }
        }

        return history;
    }

    public Tree nextStep(Tree t, ArrayList<ArrayList<Tree.Node>> levels, int h, int w, StringBuffer errbuf, Boolean guiFlag){
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
            if(!guiFlag){
                errbuf.append(ANSI_RED);
            }
            errbuf.append("No rule defined in automaton " + name + " for node " + n.getData() + " with children ");
            for (int i = 0; i < n.getChildren().size() - 1; i++) {
                errbuf.append(n.getChildren().get(i).getData() + ", ");
            }
            if(n.getChildren().size() > 0) {
                errbuf.append(n.getChildren().get(n.getChildren().size() - 1).getData() + ".\n");
            }
            if(!guiFlag){
                errbuf.append(ANSI_RESET);
            }
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

    public void modify(BufferedReader reader) throws IOException{
        while(true){
            System.out.println(ANSI_GREEN + "Current Rules of the Tree Automaton " + this.getName() + ANSI_RESET);
            this.printRules();
            System.out.println(ANSI_GREEN + "Current Final States of the Tree Automaton " + this.getName() + ANSI_RESET);
            for(int i = 0; i < this.getFinStates().size(); i++){
                System.out.println(ANSI_CYAN + this.getFinStates().get(i) + ANSI_RESET);
            }
            while(true){
                System.out.println(ANSI_GREEN + "Modify [r]ules/[f]inal states or go [b]ack");
                String input = reader.readLine().toLowerCase();
                if(input.equals("b") || input.equals("back")){
                    return;
                } else if(input.equals("r") || input.equals("rules")){
                    modifyRules(reader);
                    break;
                } else if(input.equals("f") || input.equals("final")){
                    modifyFinalStates(reader);
                    break;
                } else{
                    System.out.println(ANSI_RED + "Command not recognised" + ANSI_RESET);
                }
            }
        }
    }

    public void modifyRules(BufferedReader reader) throws IOException{
        while(true){
            System.out.println(ANSI_GREEN + "[a]dd/[r]emove rule or go [b]ack");
            String input = reader.readLine().toLowerCase();
            if(input.equals("b") || input.equals("back")){
                return;
            } else if(input.equals("a") || input.equals("add")){
                TransitionRule newRule = getRuleFromInput(reader);
                if(newRule == null){
                    continue;
                }
                //Check for existing rule with same element && starting states
                if(this.checkDuplicateRule(newRule.getElement(), newRule.getCurrentStates())){
                    System.out.println(ANSI_RED + "Rule already defined for this combination of element and current states." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "In deterministic finite automaton no two transition rules can have the same left hand side." + ANSI_RESET);
                    continue;
                } else{
                    this.addRule(newRule);
                }
                return;
            } else if(input.equals("r") || input.equals("remove")){
                TransitionRule toRemove = getRuleFromInput(reader);
                if(toRemove == null){
                    continue;
                }
                System.out.println(getRuleIndex(toRemove.getElement(), toRemove.getCurrentStates()));
                if(getRuleIndex(toRemove.getElement(), toRemove.getCurrentStates()) != -1){
                    this.rules.remove(getRuleIndex(toRemove.getElement(), toRemove.getCurrentStates()));
                } else {
                    System.out.println(ANSI_RED + "No rule found with element " + toRemove.getElement() + " and current states; ");
                    for(int i = 0; i < toRemove.getCurrentStates().size(); i++){
                        System.out.printf("%s ", toRemove.getCurrentStates().get(i));
                    }
                    System.out.printf("\n");
                }
                return;
            } else{
                System.out.println(ANSI_RED + "Command not recognised" + ANSI_RESET);
            }
        }
    }

    public void modifyFinalStates(BufferedReader reader) throws IOException{
        while(true){
            ArrayList<String> unrecognizedStates = new ArrayList<>();
            ArrayList<String> recognizedStates = new ArrayList<>();
            System.out.println(ANSI_GREEN + "[a]dd/[r]emove final state or go [b]ack");
            String input = reader.readLine().toLowerCase();
            if(input.equals("b") || input.equals("back")){
                return;
            } else if(input.equals("a") || input.equals("add")){
                System.out.println(ANSI_GREEN + "Enter states from the following to add to final state list;" + ANSI_RESET);
                for(int i = 0; i < states.size(); i++){
                    if(!finStates.contains(states.get(i))){
                        System.out.println(ANSI_CYAN + states.get(i) + ANSI_RESET);
                    }
                }
                String[] inputArray = reader.readLine().split(" ");
                for(int i = 0; i < inputArray.length; i++){
                    if(states.contains(inputArray[i]) && !finStates.contains(inputArray[i])){
                        recognizedStates.add(inputArray[i]);
                        this.finStates.add(inputArray[i]);
                    } else {
                        unrecognizedStates.add(inputArray[i]);
                    }
                }
                if(recognizedStates.size() > 0){
                    System.out.println(ANSI_GREEN + "The following states have been added as a final state;" + ANSI_RESET);
                    for(int i = 0; i < recognizedStates.size(); i++){
                        System.out.println(ANSI_CYAN + recognizedStates.get(i) + ANSI_RESET);
                    }
                }
                if(unrecognizedStates.size() > 0){
                    System.out.println(ANSI_RED + "The following states were not recognised or already recorded as final states;" + ANSI_RESET);
                    for(int i = 0; i < unrecognizedStates.size(); i++){
                        System.out.println(ANSI_RED + unrecognizedStates.get(i) + ANSI_RESET);
                    }
                    System.out.println(ANSI_GREEN + "The rest were stored correctly." + ANSI_RESET);
                }
            } else if(input.equals("r") || input.equals("remove")){
                System.out.println(ANSI_GREEN + "Enter states from the following to remove to final state list;" + ANSI_RESET);
                for(int i = 0; i < finStates.size(); i++){
                    System.out.println(ANSI_CYAN + finStates.get(i) + ANSI_RESET);
                }
                String[] inputArray = reader.readLine().split(" ");

                for(int i = 0; i < inputArray.length; i++){
                    if(finStates.contains(inputArray[i])){
                        recognizedStates.add(inputArray[i]);
                        this.finStates.remove(inputArray[i]);
                    } else {
                        unrecognizedStates.add(inputArray[i]);
                    }
                }
                if(recognizedStates.size() > 0){
                    System.out.println(ANSI_GREEN + "The following states were removed as final states;" + ANSI_RESET);
                    for(int i = 0; i < recognizedStates.size(); i++){
                        System.out.println(ANSI_CYAN + recognizedStates.get(i) + ANSI_RESET);
                    }
                }
                if(unrecognizedStates.size() > 0){
                    System.out.println(ANSI_RED + "The following states were not recognised or are not currently final states;" + ANSI_RESET);
                    for(int i = 0; i < unrecognizedStates.size(); i++){
                        System.out.println(ANSI_RED + unrecognizedStates.get(i) + ANSI_RESET);
                    }
                    System.out.println(ANSI_GREEN + "The rest were removed correctly." + ANSI_RESET);
                }
            }
        }
    }

    public TransitionRule getRuleFromInput(BufferedReader reader) throws IOException{
        ArrayList<String> currentStates = new ArrayList<>();
        System.out.println(ANSI_GREEN + "Enter transition rule in the form " + ANSI_YELLOW + "\"Element | stateBefore1, stateBefore2, ... | stateAfter\"" + ANSI_RESET);
        String input = reader.readLine();
        String[] inputArray = input.split(" ");
        if(!this.alphabet.contains(inputArray[0].toLowerCase())){
            System.out.println(ANSI_RED + "Element " + ANSI_CYAN + inputArray[0] + ANSI_RED + " is not a part of the alphabet " + ANSI_CYAN + alphabet.getName() + ANSI_RED + "." + ANSI_RESET);
            System.out.println(ANSI_GREEN + "Please try again with an element from the following;" + ANSI_RESET);
            for(int i = 0; i < alphabet.getAlph().size(); i++){
                System.out.println(ANSI_CYAN + alphabet.getAlph().get(i) + ANSI_RESET);
            }
            return null;
        }

        TransitionRule current = new TransitionRule(inputArray[0].toLowerCase(), alphabet.getArity(inputArray[0].toLowerCase()));

        if(inputArray.length < 4 + this.alphabet.getArity(inputArray[0].toLowerCase())){
            System.out.println(ANSI_RED + "Not enough inputs. Please try again." + ANSI_RESET);
            return null;
        } else if(inputArray.length > 4 + alphabet.getArity(inputArray[0].toLowerCase())){
            System.out.println(ANSI_RED + "Too many inputs. Please try again." + ANSI_RESET);
            return null;
        }

        if(!inputArray[1].equals("|")){
            System.out.println(ANSI_RED + "First separator \"|\" not found. Please try again." + ANSI_RESET);
            return null;
        }

        int i = 2;
        for(; i < 2 + this.alphabet.getArity(inputArray[0].toLowerCase()); i++){
            if(inputArray[i].equals("|")){
                System.out.println(ANSI_RED + "Second separator \"|\" found too early. Please try again." + ANSI_RESET);
                System.out.println(ANSI_GREEN + "Element " + ANSI_CYAN + inputArray[0] + ANSI_GREEN + " has arity " + ANSI_CYAN + this.alphabet.getArity(inputArray[0].toLowerCase()) + ANSI_GREEN + " in alphabet " + ANSI_CYAN + this.alphabet.getName() + ANSI_GREEN + "." + ANSI_RESET);
                return null;
            } else {
                currentStates.add(inputArray[i]);
            }
        }

        if(!inputArray[i++].equals("|")){
            System.out.println(ANSI_RED + "Second separator \"|\" not found. Please try again." + ANSI_RESET);
            return null;
        }

        String newState = inputArray[i++];

        current.setCurrentStates(currentStates);
        current.setNewState(newState);
        return current;
    }

    public void setComplete(Boolean a){
        this.complete = a;
    }

    public Boolean getComplete(){
        return this.complete;
    }

}
