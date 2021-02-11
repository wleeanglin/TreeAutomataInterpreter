import java.util.ArrayList;

public class TransitionRule {
    private ArrayList<String> currentStates;
    private String element;
    private int arity;
    private String newState;

    public TransitionRule(String s, int i){
        this.element = s;
        this.arity = i;
        this.currentStates = new ArrayList<>();
    }

    public ArrayList<String> getCurrentStates(){
        return this.currentStates;
    }

    public String getNewState(){
        return this.newState;
    }

    public void setCurrentStates(ArrayList<String> s){
        for(int i = 0; i < s.size(); i++){
            currentStates.add(s.get(i));
        }
    }

    public void setNewState(String s){
        this.newState = s;
    }

    public String getElement(){
        return this.element;
    }

    public void printRule(){
        System.out.printf("%s", element);
        System.out.printf("(");
        int j = 0;
        //(q1 q2 q3)
        for(; j < currentStates.size() - 1; j++){
            System.out.printf("%s ", currentStates.get(j));
        }
        if(currentStates.size() > 0){
            System.out.printf("%s", currentStates.get(j));
        }
        System.out.printf(") ");
        System.out.printf("-> ");
        System.out.printf("%s\n", newState);
    }
}
