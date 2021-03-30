import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class RankedAlphabet {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    private ArrayList alph;
    private ArrayList arity;
    private String name;
    private Boolean complete;
    private Boolean modifiable = true;

    public RankedAlphabet(){
        this.alph = new ArrayList();
        this.arity = new ArrayList<>();
        this.complete = false;
    }

    public RankedAlphabet(String name){
        this.name = name;
        this.alph = new ArrayList();
        this.arity = new ArrayList<>();
        this.complete = false;
    }

    public void add(String s, int arity){
        this.alph.add(s);
        this.arity.add(arity);
    }

    public Boolean contains(String s){
        return this.alph.contains(s);
    }

    public int getArity(String s){
        int i = this.alph.indexOf(s);
        return (int) this.arity.get(i);
    }

    public ArrayList getArityArray(){
        return this.arity;
    }

    public void setName(String s){
        this.name = s;
    }

    public String getName(){
        return this.name;
    }

    public void setComplete(Boolean complete){
        this.complete = complete;
    }

    public Boolean getComplete(){
        return this.complete;
    }

    public void setModifiable(Boolean b){
        this.modifiable = b;
    }

    public Boolean getModifiable(){
        return this.modifiable;
    }

    public ArrayList getAlph() {
        return this.alph;
    }

    public boolean containsNullary(){
        for(int i = 0; i < this.arity.size(); i++){
            if((int)this.arity.get(i) == 0){
                return true;
            }
        }
        return false;
    }

    public void remove(int i){
        this.alph.remove(i);
        this.arity.remove(i);
    }

    public void modify(BufferedReader reader) throws IOException {
        while(true){
            System.out.println(ANSI_GREEN + "Current elements of Ranked Alphabet " + this.getName() + ANSI_RESET);
            for(int i = 0; i < alph.size(); i++){
                System.out.println(ANSI_CYAN + "Element: " + alph.get(i) + ", Arity: " + ANSI_YELLOW + arity.get(i) + ANSI_RESET);
            }
            while(true){
                System.out.println(ANSI_GREEN + "[a]dd/[r]emove/[b]ack" + ANSI_RESET);
                String input = reader.readLine().toLowerCase();
                if(input.equals("b") || input.equals("back")){
                    if(this.containsNullary()){
                        return;
                    } else{
                        System.out.println(ANSI_RED + "No nullary element in this alphabet. Please define one before finishing modification." + ANSI_RESET);
                    }
                } else if(input.equals("a") || input.equals("add")){
                    System.out.println(ANSI_GREEN + "Please enter new element in the form \"element arity\"." + ANSI_RESET);
                    input = reader.readLine().toLowerCase();
                    String[] arr = input.split(" ", 0);
                    if(arr.length <= 1){
                        System.out.println(ANSI_RED + "Not enough inputs." + ANSI_RESET);
                        continue;
                    }
                    if(arr.length >= 3){
                        System.out.println(ANSI_RED + "Too many inputs." + ANSI_RESET);
                        continue;
                    }
                    try{
                        int i = Integer.parseInt(arr[1]);
                    } catch (NumberFormatException e){
                        System.out.println(ANSI_RED + "Arity not an integer." + ANSI_RESET);
                        continue;
                    }
                    if(Integer.parseInt(arr[1]) < 0){
                        System.out.println(ANSI_RED + "Negative arity." + ANSI_RESET);
                        continue;
                    }
                    if(this.getAlph().contains(arr[0])){
                        System.out.println(ANSI_RED + "Element already recorded - please use different string." + ANSI_RESET);
                        continue;
                    }
                    this.add(arr[0], Integer.parseInt(arr[1]));
                    break;
                } else if(input.equals("r") || input.equals("remove")){
                    System.out.println(ANSI_GREEN + "Please enter element to remove." + ANSI_RESET);
                    input = reader.readLine().toLowerCase();
                    if(this.contains(input)){
                        for(int i = 0; i < this.alph.size(); i++){
                            if(this.alph.get(i).equals(input)){
                                this.remove(i);
                            }
                        }
                    } else{
                        System.out.println(ANSI_RED + "Element " + input + " not defined in this alphabet." + ANSI_RESET);
                    }
                    break;
                } else{
                    System.out.println(ANSI_RED + "Command not recognised" + ANSI_RESET);
                }
            }
        }
    }

    public Boolean checkInput(String element, String arity, StringBuffer errbuff){
        String[] elementArr = element.split(" ", 0);
        String[] arityArr = element.split(" ", 0);

        if(elementArr.length > 1){
            errbuff.append("Too many arguments for element.");
            return false;
        } else if(arityArr.length > 1){
            errbuff.append("Too many arguments for arity.");
            return false;
        }

        if(getAlph().contains(element.replaceAll("\\s+", ""))){
            errbuff.append("Element already defined.");
            return false;
        }

        //parse int
        int i;
        try{
            i = Integer.parseInt(arity);
        } catch (NumberFormatException e){
            errbuff.append("Arity not an integer");
            return false;
        }
        //Check int
        if(i < 0){
            errbuff.append("Negative Arity");
            return false;
        }
        return true;
    }

    public void printAlphabet(){
        for(int i = 0; i < this.getAlph().size(); i++){
            System.out.println("Element " + (i + 1)  + "; " + this.getAlph().get(i) + " " + this.getArityArray().get(i));
        }
    }

    //Only call when object is known :)
    public int getIndex(String s){
        for(int i = 0; i < this.getAlph().size(); i++){
            if(this.getAlph().get(i).equals(s)){
                return i;
            }
        }
        return -1;
    }

    public Boolean compare(RankedAlphabet a){
        if(!a.getName().equals(this.name)){
            return false;
        } else if(a.getAlph().size() != this.alph.size()){
            return false;
        } else if(a.getArityArray().size() != this.arity.size()){
            return false;
        } else{
            for(int i = 0; i < a.getAlph().size(); i++){
                if(!a.getAlph().get(i).equals(this.alph.get(i))){
                    return false;
                } else if(!a.getArityArray().get(i).equals(this.arity.get(i))){
                    return false;
                }
            }
        }
        return true;
    }

    public RankedAlphabet copy(){
        RankedAlphabet b = new RankedAlphabet();
        String s = this.getName();
        b.setName(s);

        for(int i = 0; i < this.getAlph().size(); i++){
            b.add((String) this.getAlph().get(i), (int) this.getArityArray().get(i));
        }

        return b;
    }


}
