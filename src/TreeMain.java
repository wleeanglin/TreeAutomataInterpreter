import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.lang.StringBuffer;

public class TreeMain {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private String[] baseOptions = {"new", "modify", "operate", "save", "load", "help"};
    private String[] newOptions = {"alphabet", "tree", "automaton", "back"};
    private String[] modifyOptions = {"alphabet", "automaton", "back"};
    private String[] saveOptions = {"alphabet", "tree", "automaton", "back"};
    private String[] commands = {"alphabet", "tree", "help", "automaton", "operate"};
    private ArrayList<RankedAlphabet> alphabets;
    private ArrayList<Tree> trees;
    private ArrayList<TreeAutomaton> automata;
    private ArrayList<ArrayList<Tree>> histories;

    public static void main(String[] args) {
        TreeMain m = new TreeMain();
        m.start();
    }

    public TreeMain(){

    }

    public void start(){
        //in case needed
        String[] args = {};
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                System.out.println(ANSI_GREEN + "[g]ui or [c]onsole" + ANSI_RESET);
                String input = reader.readLine();
                if(input.toLowerCase().equals("g") || input.toLowerCase().equals("gui")){
                    GUI.main(args);
                    System.exit(0);
                } else if(input.toLowerCase().equals("c") || input.toLowerCase().equals("console")){
                    this.alphabets = new ArrayList<>();
                    this.trees = new ArrayList<>();
                    this.automata = new ArrayList<>();
                    this.histories = new ArrayList<>();
                    mainMenu(reader);
                    System.exit(0);
                } else{
                    System.out.println(ANSI_RED + "Command not recognized" + ANSI_RESET);
                    continue;
                }
            }
        } catch(IOException e){}
        //waitForCommand();
    }

    public void mainMenu(BufferedReader reader) throws IOException{
        while(true){
            printHeader("MAIN MENU");
            printOptions(baseOptions);
            String input = reader.readLine();
            switch(input.toLowerCase()){
                case "new":
                    newMenu(reader);
                    break;
                case "modify":
                    modifyMenu(reader);
                    break;
                case "operate":
                    System.out.println(ANSI_WHITE + "|                OPERATE                |" + ANSI_RESET);
                    readOperation(reader);
                    break;
                case "save":
                    saveMenu(reader);
                    break;
                case "load":
                    System.out.println(ANSI_WHITE + "|                 LOAD                  |" + ANSI_RESET);
                    break;
                case "help":
                    printHelp();
                    break;
                default:
                    System.out.println(ANSI_RED + "Command not recognised" + ANSI_RESET);
            }
        }
    }

    private void printHeader(String t){
        //Fancy header :D
        int i = ((30 - t.length()) / 2) - 1;
        int j = (30 - (i + t.length())) - 2;
        System.out.println(ANSI_WHITE + "*============================*");
        System.out.printf("|");
        for(int k = 0; k < i; k++){
            System.out.printf(" ");
        }
        System.out.printf(t);
        for(int k = 0; k < j; k++){
            System.out.printf(" ");
        }
        System.out.printf("|\n");
        System.out.println("*============================*" + ANSI_RESET);
    }

    private void printOptions(String[] a){
        for(int i = 0; i < a.length; i++){
            System.out.println(ANSI_CYAN + a[i] + ANSI_RESET);
        }
    }

    private void newMenu(BufferedReader reader) throws IOException{
        while(true){
            printHeader("NEW");
            printOptions(newOptions);
            String input = reader.readLine();
            switch(input.toLowerCase()){
                case "alphabet":
                    RankedAlphabet A = readAlph(reader);
                    if(A != null){
                        alphabets.add(A);
                        return;
                    }
                    break;
                case "tree":
                    Tree B = readTree(reader);
                    if(B != null) {
                        trees.add(B);
                        return;
                    }
                    break;
                case "automaton":
                    TreeAutomaton C = readAutomaton(reader);
                    if(C != null){
                        automata.add(C);
                        return;
                    }
                case "back":
                    return;
                default:
                    System.out.println(ANSI_RED + "Command not recognised" + ANSI_RESET);
                    break;
            }
        }
    }

    private void modifyMenu(BufferedReader reader) throws IOException{
        while(true){
            printHeader("MODIFY");
            printOptions(modifyOptions);
            String input = reader.readLine();
            switch(input.toLowerCase()){
                case "alphabet":
                    try{
                        RankedAlphabet A = (RankedAlphabet) selectToModify("alphabet", this.alphabets, reader);
                        if(A != null){
                            A.modify(reader);
                        }
                    }
                    catch(ClassCastException e){}
                    return;
                case "automaton":
                    try{
                        TreeAutomaton A = (TreeAutomaton) selectToModify("automaton", this.automata, reader);
                        if(A != null){
                            A.modify(reader);
                        }
                    }
                    catch(ClassCastException e){}
                    return;
                case "back":
                    return;
                default:
                    System.out.println(ANSI_RED + "Command not recognised" + ANSI_RESET);
                    break;
            }
        }
    }

    private void saveMenu(BufferedReader reader) throws IOException{
        while(true){
            System.out.println(ANSI_WHITE + "|                 SAVE                  |" + ANSI_RESET);
            printOptions(saveOptions);
            String input = reader.readLine();
            switch(input.toLowerCase()){
                case "alphabet":

                    break;
                case "tree":

                    break;
                case "automaton":

                case "back":
                    return;
                default:
                    System.out.println(ANSI_RED + "Command not recognised" + ANSI_RESET);
                    break;
            }
        }
    }

    public Object selectToModify(String type, ArrayList typeList, BufferedReader reader) throws IOException{
        if(typeList.size() == 0){
            System.out.println(ANSI_RED + "No objects of type " + type + " defined yet" + ANSI_RESET);
            return null;
        } else{
            while(true){
                System.out.println(ANSI_GREEN + "Please select object to modify from the following" + ANSI_RESET);
                System.out.println(ANSI_GREEN + "Type \"back\" to return to the main menu" + ANSI_RESET);
                for(int i = 0; i < typeList.size(); i++){
                    System.out.println(ANSI_CYAN + getNameGeneral(typeList.get(i)) + ANSI_RESET);
                }

                String input = reader.readLine().toLowerCase();
                if(input.equals("back") || input.equals("b")){
                    return null;
                } else if(containsGeneral(typeList, input)) {
                    for (int i = 0; i < typeList.size(); i++) {
                        if (getNameGeneral(typeList.get(i)).equals(input)) {
                            return typeList.get(i);
                        }
                    }
                } else{
                    System.out.println(ANSI_RED + "No object of type " + type + " named \"" + input + "\" created yet" + ANSI_RESET);
                }
            }
        }
    }


    public void waitForCommand(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try{
            while(true){
                System.out.println(ANSI_WHITE + "##### Main Menu #####" + ANSI_RESET);
                printCommands();
                System.out.println(ANSI_GREEN + "Waiting for command..." + ANSI_RESET);

                String comm = reader.readLine();

                if((comm.toLowerCase()).equals("alphabet")) {
                    RankedAlphabet A = readAlph(reader);
                    alphabets.add(A);
                } else if((comm.toLowerCase()).equals("tree")) {
                    Tree A = readTree(reader);
                    if (A != null) {
                        trees.add(A);
                    }
                } else if((comm.toLowerCase()).equals("automaton")) {
                    TreeAutomaton A = readAutomaton(reader);
                    if (A != null) {
                        automata.add(A);
                    }
                } else if((comm.toLowerCase()).equals("operate")){
                    readOperation(reader);
                } else if((comm.toLowerCase()).equals("help")) {
                    printHelp();
                } else if((comm.toLowerCase()).equals("printtrees")){
                    for(int i = 0; i < this.trees.size(); i++){
                        trees.get(i).print();
                        System.out.println("Height = " + trees.get(i).getMaxHeight());
                    }
                } else{
                    System.out.println(ANSI_RED + "Command not recognised" + ANSI_RESET);
                }
            }
        } catch (IOException e){

        }

    }

    public RankedAlphabet readAlph(BufferedReader reader){
        try{
            String name;
            System.out.println(ANSI_WHITE + "##### Defining new Alphabet #####" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "Please enter alphabet name;" + ANSI_RESET);
            while(true){
                name = reader.readLine().toLowerCase();
                if(alphabetNameTaken(name)){
                    System.out.println(ANSI_RED + "There already exists an alphabet with the name " + name + "." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please choose another name;" + ANSI_RESET);
                    continue;
                } else{
                    break;
                }
            }

            RankedAlphabet r = new RankedAlphabet(name);
            System.out.println(ANSI_GREEN + "New Ranked Alphabet '" + name + "' created." + ANSI_RESET);
            System.out.println(ANSI_GREEN + "All ranked alphabets must contain a nullary element. That is an element with arity 0." + ANSI_RESET);
            System.out.println(ANSI_GREEN + "Please enter elements of the alphabet in the form \"element arity\"." + ANSI_RESET);
            System.out.println(ANSI_GREEN + "For example, \"a 0\" would correspond to defining the character 'a' with arity 0 in the alphabet." + ANSI_RESET);

            while(true){
                System.out.println(ANSI_GREEN + "Waiting on input..." + ANSI_RESET);
                System.out.println(ANSI_GREEN + "Enter \"continue\" to finish definition." + ANSI_RESET);
                String input = reader.readLine().toLowerCase();
                if(input.equals("continue")){
                    //dont accept alphabet unless it has a nullary element
                    if(r.containsNullary()){
                        System.out.println(ANSI_GREEN + "Alphabet input completed." + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "Returning to main menu..." + ANSI_RESET);
                        return r;
                    } else{
                        System.out.println(ANSI_RED + "No nullary element added yet so cannot complete alphabet." + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "Please enter a nullary element of the form \"element 0\" before finishing the definition." + ANSI_RESET);
                        continue;
                    }
                }


                String[] arr = input.split(" ", 0);
                if(arr.length <= 1){
                    System.out.println(ANSI_RED + "Not enough inputs." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please enter elements of the form \"element arity\" to define the alphabet." + ANSI_RESET);
                    continue;
                }
                if(arr.length >= 3){
                    System.out.println(ANSI_RED + "Too many inputs." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please enter elements of the form \"element arity\" to define the alphabet." + ANSI_RESET);
                    continue;
                }

                try{
                    int i = Integer.parseInt(arr[1]);
                } catch (NumberFormatException e){
                    System.out.println(ANSI_RED + "Arity not an integer." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please enter elements of the form \"element arity\" to define the alphabet." + ANSI_RESET);
                    continue;
                }

                if(Integer.parseInt(arr[1]) < 0){
                    System.out.println(ANSI_RED + "Negative arity." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please enter elements of the form \"element arity\" to define the alphabet." + ANSI_RESET);
                    continue;
                }

                if(r.contains(arr[0])){
                    System.out.println(ANSI_RED + "Element already recorded - please use different string." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please enter elements of the form \"element arity\" to define the alphabet." + ANSI_RESET);
                    continue;
                }

                r.add(arr[0], Integer.parseInt(arr[1]));
            }
        } catch (IOException e){

        }

        //Shouldn't reach here
        return null;
    }

    public Tree readTree(BufferedReader reader){
        System.out.println(ANSI_WHITE + "##### Defining a new tree #####");
        try {
            //NO RANKED ALPHABET
            if(alphabets.size() == 0){
                System.out.println(ANSI_RED + "No ranked alphabet created yet. Please create one before creating a tree." + ANSI_RESET);
                System.out.println(ANSI_GREEN + "Returning to main menu..." + ANSI_RESET);
                return null;
            }

            Tree t;
            //TREE NAME
            String name;
            while (true) {
                System.out.println(ANSI_GREEN + "Please enter tree name;" + ANSI_RESET);
                name = reader.readLine();
                if (treeNameTaken(name)) {
                    System.out.println(ANSI_RED + "There already exists a tree with the name " + name + "." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please choose another name;" + ANSI_RESET);
                    continue;
                } else {
                    break;
                }
            }

            //RANKED ALPHABET
            String alphabetName;
            RankedAlphabet alphabet = new RankedAlphabet();
            while(true) {
                System.out.println(ANSI_GREEN + "Please select a ranked alphabet from the following;" + ANSI_RESET);
                for(int i = 0; i < alphabets.size(); i++){
                    System.out.println(ANSI_CYAN + alphabets.get(i).getName() + ANSI_RESET);
                }
                alphabetName = reader.readLine().toLowerCase();
                if(!alphabetNameTaken(alphabetName)){
                    System.out.println(ANSI_RED + "Alphabet " + alphabetName + " not defined." + ANSI_RESET);
                    continue;
                } else {
                    break;
                }
            }

            for(int i = 0; i < alphabets.size(); i++) {
                if (alphabets.get(i).getName().equals(alphabetName)) {
                    alphabet = alphabets.get(i);
                    break;
                }
            }

            //INITIATE TREE
            while(true){
                System.out.println(ANSI_GREEN + "Please select an element from the following to be the root of the tree " + ANSI_RED + name+ ANSI_GREEN + ";" + ANSI_RESET);
                for(int i = 0; i < alphabet.getAlph().size(); i++){
                    System.out.println(ANSI_CYAN + alphabet.getAlph().get(i) + ANSI_RESET);
                }
                String input = reader.readLine();
                if(alphabet.contains(input.toLowerCase())) {
                    System.out.println(ANSI_GREEN + "New tree " + ANSI_RED + name + ANSI_GREEN + " created. With root node " + ANSI_CYAN + input + ANSI_GREEN + ", arity = " +  ANSI_CYAN + alphabet.getArity(input) + ANSI_RESET);
                    t = new Tree(name, input, alphabet.getArity(input), alphabet);
                    break;
                } else {
                    System.out.println(ANSI_RED + "Element not recognised." + ANSI_RESET);
                    continue;
                }
            }

            //REST OF TREE
            Tree.Node currentNode = t.getRoot();
            ArrayList<Tree.Node> toComplete = new ArrayList<>();
            toComplete.add(currentNode);
            while(true){
                while(currentNode.getChildren().size() < currentNode.getNumChildren()){
                    //do this purely to make the definition process clearer
                    //Could add arity * ? nodes
                    Tree.Node newNode = currentNode.addChild("?", 0);
                    System.out.println(ANSI_GREEN + "Please enter next element;" + ANSI_RESET);
                    t.print();
                    String input = reader.readLine();
                    if(alphabet.contains(input.toLowerCase())) {
                        newNode.setData(input.toLowerCase()); newNode.setArity(alphabet.getArity(input.toLowerCase()));
                        newNode.setHeight(currentNode.getHeight() + 1);
                        toComplete.add(newNode);
                    } else {
                        System.out.println(ANSI_RED + "Element not recognised." + ANSI_RESET);
                        //Remove the temp node if element not recognised
                        currentNode.removeChild();
                        continue;
                    }
                }
                toComplete.remove(currentNode);
                if(toComplete.size() > 0){
                    currentNode = toComplete.get(0);
                } else {
                    System.out.println(ANSI_GREEN + "Tree successfully defined." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Returning to main menu..." + ANSI_RESET);
                    return t;
                }
            }

        }   catch (IOException e){

        }

        //Shouldn't reach here
        return null;

    }

    public TreeAutomaton readAutomaton(BufferedReader reader){
        System.out.println(ANSI_WHITE + "##### Defining a new tree automaton #####" + ANSI_RESET);
        try{
            if(alphabets.size() == 0){
                System.out.println(ANSI_RED + "No alphabet created yet. Please define a ranked alphabet before attempting to define a tree automaton." + ANSI_RESET);
                System.out.println(ANSI_GREEN + "Returning to main menu..." + ANSI_RESET);
                return null;
            }

            TreeAutomaton t;
            String name;
            while (true) {
                System.out.println(ANSI_GREEN + "Please enter automaton name;" + ANSI_RESET);
                name = reader.readLine().toLowerCase();
                if (automatonNameTaken(name)) {
                    System.out.println(ANSI_RED + "There already exists an automaton with the name " + name + "." + ANSI_RESET);
                    continue;
                } else {
                    break;
                }
            }

            //RANKED ALPHABET
            String alphabetName;
            RankedAlphabet alphabet = new RankedAlphabet();
            while(true) {
                System.out.println(ANSI_GREEN + "Please select a ranked alphabet to use for the automaton from the following;");
                for(int i = 0; i < alphabets.size(); i++){
                    System.out.println(ANSI_CYAN + alphabets.get(i).getName() + ANSI_RESET);
                }
                alphabetName = reader.readLine().toLowerCase();
                if(!alphabetNameTaken(alphabetName)){
                    System.out.println(ANSI_RED + "Alphabet " + alphabetName + " not defined." + ANSI_RESET);
                    continue;
                } else {
                    break;
                }
            }

            for(int i = 0; i < alphabets.size(); i++) {
                if (alphabets.get(i).getName().equals(alphabetName)) {
                    alphabet = alphabets.get(i);
                    break;
                }
            }

            t = new TreeAutomaton(name, alphabet);

            //Define transition rules
            String input;
            ArrayList<String> currentStates = new ArrayList<>();
            System.out.println(ANSI_GREEN + "Please define transition rules in the form " + ANSI_YELLOW + "\"Element | stateBefore1, stateBefore2, ... | stateAfter\"" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "The following is an example input for the transition" + ANSI_YELLOW + " a(q1(x1),q2(x2)) -> q3(a(x1,x2))" + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "\"a | q1 q2 | q3\"" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "States are automatically recorded as transition rules are recorded." + ANSI_RESET);
            while(true){
                currentStates.clear();
                System.out.println(ANSI_GREEN + "Please enter the next transition rule." + ANSI_RESET);
                System.out.println(ANSI_GREEN + "Enter \"continue\" to continue." + ANSI_RESET);
                input = reader.readLine();
                if(input.toLowerCase().equals("continue")){
                    break;
                }

                String[] inputArray = input.split(" ");
                if(!alphabet.contains(inputArray[0].toLowerCase())){
                    System.out.println(ANSI_RED + "Element " + ANSI_CYAN + inputArray[0] + ANSI_RED + " is not a part of the alphabet " + ANSI_CYAN + alphabet.getName() + ANSI_RED + "." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please re-enter input with an element from the following;" + ANSI_RESET);
                    for(int i = 0; i < alphabet.getAlph().size(); i++){
                        System.out.println(ANSI_CYAN + alphabet.getAlph().get(i) + ANSI_RESET);
                    }
                    continue;
                }

                TransitionRule current = new TransitionRule(inputArray[0].toLowerCase(), alphabet.getArity(inputArray[0].toLowerCase()));

                if(inputArray.length < 4 + alphabet.getArity(inputArray[0].toLowerCase())){
                    System.out.println(ANSI_RED + "Not enough inputs." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please re-enter input of the form " + ANSI_YELLOW + "\"Element | stateBefore1, stateBefore2, ... | stateAfter\"" + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Remember to use the separators \"|\"!" + ANSI_RESET);
                    continue;
                } else if(inputArray.length > 4 + alphabet.getArity(inputArray[0].toLowerCase())){
                    System.out.println(ANSI_RED + "Too many inputs." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please re-enter input of the form " + ANSI_YELLOW + "\"Element | stateBefore1, stateBefore2, ... | stateAfter\"" + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Remember to use the separators \"|\"!" + ANSI_RESET);
                    continue;
                }

                if(!inputArray[1].equals("|")){
                    System.out.println(ANSI_RED + "First separator \"|\" not found." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please re-enter input of the form " + ANSI_YELLOW + "\"Element | stateBefore1, stateBefore2, ... | stateAfter\"" + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Remember to use the separators \"|\"!" + ANSI_RESET);
                    continue;
                }

                int i = 2;
                for(; i < 2 + alphabet.getArity(inputArray[0].toLowerCase()); i++){
                    if(inputArray[i].equals("|")){
                        System.out.println(ANSI_RED + "Second separator \"|\" found too early." + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "Element " + ANSI_CYAN + inputArray[0] + ANSI_GREEN + " has arity " + ANSI_CYAN + alphabet.getArity(inputArray[0].toLowerCase()) + ANSI_GREEN + " in alphabet " + ANSI_CYAN + alphabet.getName() + ANSI_GREEN + "." + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "Therefore this rule definition must have " + ANSI_CYAN + alphabet.getArity(inputArray[0].toLowerCase()) + ANSI_GREEN + " elements between the two separators." + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "Please re-enter input of the form " + ANSI_YELLOW + "\"Element | stateBefore1, stateBefore2, ... | stateAfter\"" + ANSI_RESET);
                        continue;
                    } else {
                        currentStates.add(inputArray[i]);
                    }
                }

                if(!inputArray[i++].equals("|")){
                    System.out.println(ANSI_RED + "Second separator \"|\" not found" + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "Please re-enter input of the form " + ANSI_YELLOW + "\"Element | stateBefore1, stateBefore2, ... | stateAfter\"" + ANSI_RESET);
                    continue;
                }

                String newState = inputArray[i++];

                //Check for existing rule with same element && starting states
                if(t.checkDuplicateRule(inputArray[0], currentStates)){
                    System.out.println(ANSI_RED + "Rule already defined for this combination of element and current states." + ANSI_RESET);
                    System.out.println(ANSI_GREEN + "In deterministic finite automaton no two transition rules can have the same left hand side." + ANSI_RESET);
                    continue;
                }

                current.setCurrentStates(currentStates);
                current.setNewState(newState);
                t.addRule(current);
            }

            System.out.println(ANSI_GREEN + "Rules defined so far;" + ANSI_RESET);
            t.printRules();

            while(true){
                System.out.println(ANSI_GREEN + "Please enter finishing states from the following list;" + ANSI_RESET);
                for(int i = 0; i < t.getStates().size(); i++) {
                    System.out.println(ANSI_CYAN + t.getStates().get(i) + ANSI_RESET);
                }
                System.out.println(ANSI_GREEN + "Enter \"continue\" to continue" + ANSI_RESET);
                input = reader.readLine();
                String[] inputArray = input.split(" ");

                if(inputArray[0].toLowerCase().equals("continue")){
                    break;
                }

                ArrayList<String> unrecognizedStates = new ArrayList<>();
                ArrayList<String> recognizedStates = new ArrayList<>();
                for(int i = 0; i < inputArray.length; i++){
                    if(t.getStates().contains(inputArray[i].toLowerCase()) && !t.getFinStates().contains(inputArray[i].toLowerCase())) {
                        t.addFinalState(inputArray[i].toLowerCase());
                        recognizedStates.add(inputArray[i].toLowerCase());
                    } else{
                        unrecognizedStates.add(inputArray[i].toLowerCase());
                    }
                }
                if(recognizedStates.size() > 0){
                    System.out.println(ANSI_GREEN + "The following states have been added as a final state;" + ANSI_RESET);
                    for(int i = 0; i < recognizedStates.size(); i++){
                        System.out.println(ANSI_CYAN + recognizedStates.get(i) + ANSI_RESET);
                    }
                }
                if(unrecognizedStates.size() > 0){
                    System.out.println(ANSI_RED + "The following states were not recognised or already recorded;" + ANSI_RESET);
                    for(int i = 0; i < unrecognizedStates.size(); i++){
                        System.out.println(ANSI_RED + unrecognizedStates.get(i) + ANSI_RESET);
                    }
                    System.out.println(ANSI_GREEN + "The rest were stored correctly." + ANSI_RESET);
                }

            }

            return t;

        } catch (IOException e){

        }

        //unreachable
        return null;
    }

    public void readOperation(BufferedReader reader){
        try{
            if(trees.size() == 0){
                System.out.println(ANSI_RED + "No tree created yet. Please define a tree before operating an automaton." + ANSI_RESET);
                return;
            }
            if(automata.size() == 0){
                System.out.println(ANSI_RED + "No automaton created yet. Please define an automaton before operating it." + ANSI_RESET);
                return;
            }

            TreeAutomaton a;
            String input;
            while (true) {
                System.out.println(ANSI_GREEN + "Please select automaton from the following;" + ANSI_RESET);
                for(int i = 0; i < automata.size(); i++){
                    System.out.println(ANSI_CYAN + automata.get(i).getName() + ANSI_RESET);
                }
                System.out.println(ANSI_GREEN + "Enter \"exit\" to return to the main menu." + ANSI_RESET);
                input = reader.readLine();
                if(input.toLowerCase().equals("exit")){
                    return;
                }

                if(!automatonNameTaken(input.toLowerCase())){
                    System.out.println(ANSI_RED + "Automaton " + input + " not defined." + ANSI_RESET);
                    continue;
                } else{
                    a = null;
                    for(int i = 0; i < automata.size(); i++){
                        if(automata.get(i).getName().equals(input.toLowerCase())){
                            a = automata.get(i);
                        }
                    }
                    break;
                }
            }

            Tree t;
            while(true){
                System.out.println(ANSI_GREEN + "Please select tree from the following." + ANSI_RESET);
                for(int i = 0; i < trees.size(); i++){
                    System.out.println(ANSI_CYAN + trees.get(i).getName() + ANSI_RESET);
                }
                System.out.println(ANSI_GREEN + "Enter \"exit\" to return to the main menu." + ANSI_RESET);
                input = reader.readLine();
                if(input.toLowerCase().equals("exit")){
                    return;
                }

                if(!treeNameTaken(input.toLowerCase())){
                    System.out.println(ANSI_RED + "Tree " + input + " not defined." + ANSI_RESET);
                    continue;
                } else{
                    t = null;
                    for(int i = 0; i < trees.size(); i++){
                        if(trees.get(i).getName().equals(input.toLowerCase())){
                            t = trees.get(i);
                        }
                    }
                    if(!(t.getAlphabet().getName().equals(a.getAlphabet().getName()))){
                        System.out.println(ANSI_RED + "Tree " + input + " defined using a different ranked alphabet to automaton " + a + "." + ANSI_RESET);
                        System.out.println(ANSI_GREEN + "Both the tree and the automaton need to be defined using the same ranked alphabet for the transition rules to work." + ANSI_RESET);
                        continue;
                    }
                    break;
                }
            }

            StringBuffer errbuf = new StringBuffer();
            ArrayList<Tree> history = a.operateAutomata(t, errbuf);
            //Deal with history

            int counter = 0;
            while(true){
                System.out.println(ANSI_GREEN + "Step " + (counter + 1) + ";" + ANSI_RESET);
                history.get(counter).print();
                if(counter == history.size() - 1){
                    System.out.printf("%s", errbuf.toString());
                }
                while(true) {
                    System.out.println(ANSI_GREEN + "Step [f]orward/[b]ackward or [q]uit?" + ANSI_RESET);
                    input = reader.readLine();
                    if(input.toLowerCase().equals("f") || input.toLowerCase().equals("forward")){
                        if(counter < history.size() - 1){
                            counter++;
                            break;
                        } else{
                            System.out.println(ANSI_RED + "No more steps!" + ANSI_RESET);
                        }
                        break;
                    } else if(input.toLowerCase().equals("b") || input.toLowerCase().equals("backward")){
                        if(counter > 0){
                            counter--;
                            break;
                        } else{
                            System.out.println(ANSI_RED + "Can't go backwards from step 1!" + ANSI_RESET);
                        }
                    } else if(input.toLowerCase().equals("q") || input.toLowerCase().equals("quit")){
                        return;
                    } else{
                        System.out.println(ANSI_RED + "Command not recognised" + ANSI_RESET);
                    }
                }

            }

        } catch(IOException e){

        }
    }

    public void printHelp(){
        System.out.println("********************");
        System.out.println("Placeholder help Information");
        System.out.println("********************");
    }

    public Boolean alphabetNameTaken(String name){
        for(int i = 0; i < alphabets.size(); i++){
            if(alphabets.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public Boolean treeNameTaken(String name){
        for(int i = 0; i < trees.size(); i++){
            if(trees.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public Boolean automatonNameTaken(String name){
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

    public void printCommands(){
        System.out.printf(ANSI_GREEN + "Please choose one of the following commands;\n" + ANSI_RESET);
        for(int i = 0; i < commands.length; i++){
            System.out.println(ANSI_CYAN + commands[i] + ANSI_RESET);
        }
    }
}
