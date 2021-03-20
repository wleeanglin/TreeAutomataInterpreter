import java.util.ArrayList;

public class Utility {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public Boolean alphabetNameTaken(String name, ArrayList<RankedAlphabet> alphabets){
        for(int i = 0; i < alphabets.size(); i++){
            if(alphabets.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public Boolean treeNameTaken(String name, ArrayList<Tree> trees){
        for(int i = 0; i < trees.size(); i++){
            if(trees.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public Boolean automatonNameTaken(String name, ArrayList<TreeAutomaton> automata){
        for(int i = 0; i < automata.size(); i++){
            if(automata.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public String getNameGeneral(Object a){
        if(a.getClass().getName().equals("RankedAlphabet")){
            RankedAlphabet b = new RankedAlphabet();
            try{
                b = (RankedAlphabet) a;
            } catch(ClassCastException e){}
            return b.getName();
        } else if(a.getClass().getName().equals("Tree")){
            Tree b = new Tree();
            try{
                b = (Tree) a;
            } catch(ClassCastException e){}
            return b.getName();
        } else if(a.getClass().getName().equals("TreeAutomaton")){
            TreeAutomaton b = new TreeAutomaton();
            try{
                b = (TreeAutomaton) a;
            } catch(ClassCastException e){}
            return b.getName();
        } else{
            System.out.println(ANSI_RED + "Unexpected erroer getting general class name" + ANSI_RESET);
            return null;
        }
    }

    public Boolean containsGeneral(ArrayList a, String s){
        for(int i = 0; i < a.size(); i++){
            if(getNameGeneral(a.get(i)).equals(s)){
                return true;
            }
        }
        return false;
    }

    public void printCommands(String[] commands){
        System.out.printf(ANSI_GREEN + "Please choose one of the following commands;\n" + ANSI_RESET);
        for(int i = 0; i < commands.length; i++){
            System.out.println(ANSI_CYAN + commands[i] + ANSI_RESET);
        }
    }

    public ArrayList<String> concatAlphArray(ArrayList<String> elements, ArrayList<Integer> Arity){
        ArrayList<String> newArrayList = new ArrayList<>();
        for(int i = 0; i < elements.size(); i++){
            newArrayList.add(elements.get(i) + " " + Arity.get(i));
        }
        return newArrayList;
    }

    public ArrayList<String> getAlphabetNames(ArrayList<RankedAlphabet> alphabets){
        ArrayList<String> newArrayList = new ArrayList<>();
        for(int i = 0; i < alphabets.size(); i++){
            newArrayList.add(alphabets.get(i).getName());
        }
        return newArrayList;
    }

    public ArrayList<String> getTreeNames(ArrayList<Tree> trees){
        ArrayList<String> newArrayList = new ArrayList<>();
        for(int i = 0; i < trees.size(); i++){
            newArrayList.add(trees.get(i).getName());
        }
        return newArrayList;
    }

    public ArrayList<String> getAutomataNames(ArrayList<TreeAutomaton> automata){
        ArrayList<String> newArrayList = new ArrayList<>();
        for(int i = 0; i < automata.size(); i++){
            newArrayList.add(automata.get(i).getName());
        }
        return newArrayList;
    }

    //DEFAULTS
    public ArrayList<RankedAlphabet> getExampleAlphabets(){
        ArrayList<RankedAlphabet> examples = new ArrayList<>();
        RankedAlphabet one = new RankedAlphabet();
        one.setName("ExampleAlphabet1");
        one.add("a", 0);
        one.add("b", 1);
        one.add("c", 2);

        RankedAlphabet two = new RankedAlphabet();
        two.setName("ExampleAlphabet2");
        two.add("nullary", 0);
        two.add("unary", 1);
        two.add("binary", 2);
        two.add("(3)ary", 3);

        examples.add(one);
        examples.add(two);

        return examples;
    }

    public ArrayList<Tree> getExampleTrees(ArrayList<RankedAlphabet> alphabets){
        ArrayList<Tree> examples = new ArrayList<>();
        RankedAlphabet one = alphabets.get(0);
        RankedAlphabet two = alphabets.get(1);

        Tree treeOne = new Tree();
        treeOne.setName("ExampleTree1");
        treeOne.setAlphabet(one);
        treeOne.addNextNode((String) one.getAlph().get(2), 2);
        treeOne.addNextNode((String) one.getAlph().get(2), 2);
        treeOne.addNextNode((String) one.getAlph().get(1), 1);
        treeOne.addNextNode((String) one.getAlph().get(1), 1);
        treeOne.addNextNode((String) one.getAlph().get(0), 0);
        treeOne.addNextNode((String) one.getAlph().get(0), 0);
        treeOne.addNextNode((String) one.getAlph().get(0), 0);

        Tree treeTwo = new Tree();
        treeTwo.setName("ExampleTree2");
        treeTwo.setAlphabet(two);
        treeTwo.addNextNode((String) two.getAlph().get(3), 3);
        treeTwo.addNextNode((String) two.getAlph().get(2), 2);
        treeTwo.addNextNode((String) two.getAlph().get(1), 1);
        treeTwo.addNextNode((String) two.getAlph().get(0), 0);
        treeTwo.addNextNode((String) two.getAlph().get(0), 0);
        treeTwo.addNextNode((String) two.getAlph().get(0), 0);
        treeTwo.addNextNode((String) two.getAlph().get(0), 0);

        examples.add(treeOne);
        examples.add(treeTwo);

        return examples;
    }

    public ArrayList<TreeAutomaton> getAutomataExamples(ArrayList<RankedAlphabet> alphabets){
        ArrayList<TreeAutomaton> examples = new ArrayList<>();
        RankedAlphabet one = alphabets.get(0);
        RankedAlphabet two = alphabets.get(1);

        ArrayList<String> emptyList = new ArrayList<>();

        TreeAutomaton automatonOne = new TreeAutomaton("ExampleAutomaton1", one);
        automatonOne.addRule(getRule((String) one.getAlph().get(0), 0, emptyList, "q1"));
        ArrayList<String> r1 = new ArrayList<>();
        r1.add("q1");
        automatonOne.addRule(getRule((String) one.getAlph().get(1), 1, r1, "q2"));
        ArrayList<String> r2 = new ArrayList<>();
        r1.add("q2");
        automatonOne.addRule(getRule((String) one.getAlph().get(1), 1, r2, "q2"));
        ArrayList<String> r3 = new ArrayList<>();
        r3.add("q1"); r3.add("q2");
        automatonOne.addRule(getRule((String) one.getAlph().get(2), 2, r3, "q3"));
        ArrayList<String> r4 = new ArrayList<>();
        r4.add("q2"); r4.add("q3");
        automatonOne.addRule(getRule((String) one.getAlph().get(2), 2, r4, "q4"));
        automatonOne.addFinalState("q4");

        TreeAutomaton automatonTwo = new TreeAutomaton("ExampleAutomaton2", two);
        automatonTwo.addRule(getRule((String) two.getAlph().get(0), 0, emptyList,"state1"));
        ArrayList<String> r5 = new ArrayList<>();
        r5.add("state1");
        automatonTwo.addRule(getRule((String) two.getAlph().get(1), 1, r5, "state2"));
        ArrayList<String> r6 = new ArrayList<>();
        r6.add("state1"); r6.add("state1");
        automatonTwo.addRule(getRule((String) two.getAlph().get(2), 2, r6, "state3"));
        ArrayList<String> r7 = new ArrayList<>();
        r7.add("state1"); r7.add("state2"); r7.add("state3");
        automatonTwo.addRule(getRule((String) two.getAlph().get(3), 3, r7, "state4"));
        automatonTwo.addFinalState("state4");

        examples.add(automatonOne);
        examples.add(automatonTwo);

        return examples;
    }

    public TransitionRule getRule(String name, int arity, ArrayList<String> current, String next){
        TransitionRule r = new TransitionRule(name, arity);
        r.setCurrentStates(current);
        r.setNewState(next);
        return r;
    }

}
