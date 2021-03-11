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
}
