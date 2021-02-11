import java.util.ArrayList;

public class RankedAlphabet {
    private ArrayList alph;
    private ArrayList arity;
    private String name;

    public RankedAlphabet(){

    }

    public RankedAlphabet(String s){
        this.name = s;
        this.alph = new ArrayList();
        this.arity = new ArrayList<>();
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

    public String getName(){
        return this.name;
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
}
