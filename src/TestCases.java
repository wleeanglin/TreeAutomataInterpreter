import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class TestCases{
    private RankedAlphabet r;
    private ArrayList alph;
    private ArrayList arity;

    private ArrayList<Boolean> results;

    private String[] expected = {
            "\u001B[32mTree 'tree1' with final state 'q2'\u001B[36m ACCEPTED \u001B[32mby automaton 'aut1'.\n\u001B[0m",
            "\u001B[32mTree 'tree2' with final state 'q3'\u001B[36m ACCEPTED \u001B[32mby automaton 'aut2'.\n\u001B[0m",
            "\u001B[32mTree 'tree3' with final state 'q3'\u001B[36m ACCEPTED \u001B[32mby automaton 'aut3'.\n\u001B[0m",
            "\u001B[32mTree 'tree4' with final state 'q4'\u001B[36m ACCEPTED \u001B[32mby automaton 'aut4'.\n\u001B[0m",
            "\u001B[32mTree 'tree5' with final state 'q5'\u001B[36m ACCEPTED \u001B[32mby automaton 'aut5'.\n\u001B[0m",
            "\u001B[31mNo rule defined in automaton A for node f with children qa, qa.\n\u001B[0m\u001B[32mTree 'tree6'\u001B[31m REJECTED\u001B[32m by automaton 'A'.\n\u001B[0m",
            "\u001B[32mTree 'tree7' with final state 'qf'\u001B[36m ACCEPTED \u001B[32mby automaton 'A'.\n\u001B[0m",
            "\u001B[32mTree 'tree8' with final state 'qo'\u001B[31m REJECTED \u001B[32mby automaton 'A'.\n\u001B[0m"};

    public static void main(String[] args){
        TestCases t = new TestCases();
        t.runTests();
    }

    public TestCases(){

    }

    private void runTests() {
        this.r = new RankedAlphabet("alph1");
        this.r.add("a", 0);
        this.r.add("b", 1);
        this.r.add("c", 2);
        this.r.add("d", 3);
        this.r.add("e", 4);
        this.alph = r.getAlph();
        this.arity = r.getArityArray();
        this.results = new ArrayList<>();

        testOne();
        testTwo();
        testThree();
        testFour();
        testFive();
        testSix();
        testSeven();
        testEight();

        System.out.println("Test cases results - ");
        System.out.println(Collections.frequency(results, true) + "/8 tests passed");
    }

    //a
    private void testOne(){
        System.out.println("Running test 1 -");

        Tree t = new Tree("tree1", (String) alph.get(0), (int) arity.get(0), r);

        TreeAutomaton a = new TreeAutomaton("aut1", r);
        ArrayList<String> r1 = new ArrayList<>();
        a.addRule(getRule((String) alph.get(0),  (int) arity.get(0), r1, "q2"));

        a.addFinalState("q2");

        StringBuffer s = new StringBuffer();
        ArrayList<Tree> histories = a.operateAutomata(t, s, false);

        System.out.printf(s.toString());
        if(s.toString().equals(expected[0])){
            System.out.println("TEST PASSED");
            results.add(true);
        } else{
            System.out.println("TEST FAILED");
            results.add(false);
        }
    }

    //b
    //|
    //a
    private void testTwo(){
        System.out.println("Running test 2 -");

        Tree t = new Tree("tree2", (String) alph.get(1), (int) arity.get(1), r);
        t.addNextNode((String) alph.get(0), (int) arity.get(0));

        TreeAutomaton a = new TreeAutomaton("aut2", r);
        ArrayList<String> r1 = new ArrayList<>();
        a.addRule(getRule((String) alph.get(0),  (int) arity.get(0), r1, "q2"));
        ArrayList<String> r2 = new ArrayList<>();
        r2.add("q2");
        a.addRule(getRule((String) alph.get(1),  (int) arity.get(1), r2, "q3"));

        a.addFinalState("q3");

        StringBuffer s = new StringBuffer();
        ArrayList<Tree> histories = a.operateAutomata(t, s, false);

        System.out.printf(s.toString());
        if(s.toString().equals(expected[1])){
            System.out.println("TEST PASSED");
            results.add(true);
        } else{
            System.out.println("TEST FAILED");
            results.add(false);
        }
    }

    //  c
    // / \
    //a   a
    private void testThree(){
        System.out.println("Running test 3 -");

        Tree t = new Tree("tree3", (String) alph.get(2), (int) arity.get(2), r);
        t.addNextNode((String) alph.get(0), (int) arity.get(0));
        t.addNextNode((String) alph.get(0), (int) arity.get(0));

        TreeAutomaton a = new TreeAutomaton("aut3", r);
        ArrayList<String> r1 = new ArrayList<>();
        a.addRule(getRule((String) alph.get(0),  (int) arity.get(0), r1, "q2"));
        ArrayList<String> r2 = new ArrayList<>();
        r2.add("q2"); r2.add("q2");
        a.addRule(getRule((String) alph.get(2),  (int) arity.get(2), r2, "q3"));

        a.addFinalState("q3");

        StringBuffer s = new StringBuffer();
        ArrayList<Tree> histories = a.operateAutomata(t, s, false);

        System.out.printf(s.toString());
        if(s.toString().equals(expected[2])){
            System.out.println("TEST PASSED");
            results.add(true);
        } else{
            System.out.println("TEST FAILED");
            results.add(false);
        }
    }

    //      c
    //     / \
    //    b   b
    //    |   |
    //    a   a
    private void testFour(){
        System.out.println("Running test 4 -");

        Tree t = new Tree("tree4", (String) alph.get(2), (int) arity.get(2), r);
        t.addNextNode((String) alph.get(1), (int) arity.get(1));
        t.addNextNode((String) alph.get(1), (int) arity.get(1));
        t.addNextNode((String) alph.get(0), (int) arity.get(0));
        t.addNextNode((String) alph.get(0), (int) arity.get(0));

        TreeAutomaton a = new TreeAutomaton("aut4", r);
        ArrayList<String> r1 = new ArrayList<>();
        a.addRule(getRule((String) alph.get(0),  (int) arity.get(0), r1, "q2"));
        ArrayList<String> r2 = new ArrayList<>();
        r2.add("q2");
        a.addRule(getRule((String) alph.get(1),  (int) arity.get(1), r2, "q3"));
        ArrayList<String> r3 = new ArrayList<>();
        r3.add("q3"); r3.add("q3");
        a.addRule(getRule((String) alph.get(2),  (int) arity.get(2), r3, "q4"));

        a.addFinalState("q4");

        StringBuffer s = new StringBuffer();
        ArrayList<Tree> histories = a.operateAutomata(t, s, false);

        System.out.printf(s.toString());
        if(s.toString().equals(expected[3])){
            System.out.println("TEST PASSED");
            results.add(true);
        } else{
            System.out.println("TEST FAILED");
            results.add(false);
        }
    }

    //      d
    //    / | \
    //   c  a  b
    //  / \    |
    // a   a   a
    private void testFive(){
        System.out.println("Running test 5 -");

        Tree t = new Tree("tree5", (String) alph.get(3), (int) arity.get(3), r);
        t.addNextNode((String) alph.get(2), (int) arity.get(2));
        t.addNextNode((String) alph.get(0), (int) arity.get(0));
        t.addNextNode((String) alph.get(1), (int) arity.get(1));
        t.addNextNode((String) alph.get(0), (int) arity.get(0));
        t.addNextNode((String) alph.get(0), (int) arity.get(0));
        t.addNextNode((String) alph.get(0), (int) arity.get(0));

        TreeAutomaton a = new TreeAutomaton("aut5", r);
        ArrayList<String> r1 = new ArrayList<>();
        a.addRule(getRule((String) alph.get(0),  (int) arity.get(0), r1, "q2"));
        ArrayList<String> r2 = new ArrayList<>();
        r2.add("q2");
        a.addRule(getRule((String) alph.get(1),  (int) arity.get(1), r2, "q3"));
        ArrayList<String> r3 = new ArrayList<>();
        r3.add("q2"); r3.add("q2");
        a.addRule(getRule((String) alph.get(2),  (int) arity.get(2), r3, "q4"));
        ArrayList<String> r4 = new ArrayList<>();
        r4.add("q2"); r4.add("q3"); r4.add("q4");
        a.addRule(getRule((String) alph.get(3),  (int) arity.get(3), r4, "q5"));

        a.addFinalState("q5");

        StringBuffer s = new StringBuffer();
        ArrayList<Tree> histories = a.operateAutomata(t, s, false);

        System.out.printf(s.toString());
        if(s.toString().equals(expected[4])){
            System.out.println("TEST PASSED");
            results.add(true);
        } else{
            System.out.println("TEST FAILED");
            results.add(false);
        }
    }

    //Example 1.1.1 part 1
    private void testSix(){
        System.out.println("Running test 6 -");

        RankedAlphabet f = new RankedAlphabet("F");
        f.add("f", 2);
        f.add("g", 1);
        f.add("a", 0);

        Tree t = new Tree("tree6", (String)f.getAlph().get(0), (int)f.getArityArray().get(0), f);
        t.addNextNode((String)f.getAlph().get(2), (int)f.getArityArray().get(2));
        t.addNextNode((String)f.getAlph().get(2), (int)f.getArityArray().get(2));

        TreeAutomaton a = new TreeAutomaton("A", f);
        ArrayList<String> q1 = new ArrayList<>();
        a.addRule(getRule((String) f.getAlph().get(2), (int) f.getArityArray().get(2), q1, "qa"));
        ArrayList<String> q2 = new ArrayList<>();
        q2.add("qa");
        a.addRule(getRule((String) f.getAlph().get(1), (int) f.getArityArray().get(1), q2, "qg"));
        ArrayList<String> q3 = new ArrayList<>();
        q3.add("qg");
        a.addRule(getRule((String) f.getAlph().get(1), (int) f.getArityArray().get(1), q3, "qg"));
        ArrayList<String> q4 = new ArrayList<>();
        q4.add("qg"); q4.add("qg");
        a.addRule(getRule((String) f.getAlph().get(0), (int) f.getArityArray().get(0), q4, "qf"));

        a.addFinalState("qf");

        StringBuffer s = new StringBuffer();
        ArrayList<Tree> histories = a.operateAutomata(t, s, false);

        System.out.printf(s.toString());
        if(s.toString().equals(expected[5])){
            System.out.println("TEST PASSED");
            results.add(true);
        } else{
            System.out.println("TEST FAILED");
            results.add(false);
        }
    }

    //Example 1.1.1 part 2
    private void testSeven(){
        System.out.println("Running test 7 -");

        RankedAlphabet f = new RankedAlphabet("F");
        f.add("f", 2);
        f.add("g", 1);
        f.add("a", 0);

        Tree t = new Tree("tree7", (String)f.getAlph().get(0), (int)f.getArityArray().get(0), f);
        t.addNextNode((String)f.getAlph().get(1), (int)f.getArityArray().get(1));
        t.addNextNode((String)f.getAlph().get(1), (int)f.getArityArray().get(1));
        t.addNextNode((String)f.getAlph().get(2), (int)f.getArityArray().get(2));
        t.addNextNode((String)f.getAlph().get(2), (int)f.getArityArray().get(2));

        TreeAutomaton a = new TreeAutomaton("A", f);
        ArrayList<String> q1 = new ArrayList<>();
        a.addRule(getRule((String) f.getAlph().get(2), (int) f.getArityArray().get(2), q1, "qa"));
        ArrayList<String> q2 = new ArrayList<>();
        q2.add("qa");
        a.addRule(getRule((String) f.getAlph().get(1), (int) f.getArityArray().get(1), q2, "qg"));
        ArrayList<String> q3 = new ArrayList<>();
        q3.add("qg");
        a.addRule(getRule((String) f.getAlph().get(1), (int) f.getArityArray().get(1), q3, "qg"));
        ArrayList<String> q4 = new ArrayList<>();
        q4.add("qg"); q4.add("qg");
        a.addRule(getRule((String) f.getAlph().get(0), (int) f.getArityArray().get(0), q4, "qf"));

        a.addFinalState("qf");

        StringBuffer s = new StringBuffer();
        ArrayList<Tree> histories = a.operateAutomata(t, s, false);

        System.out.printf(s.toString());
        if(s.toString().equals(expected[6])){
            System.out.println("TEST PASSED");
            results.add(true);
        } else{
            System.out.println("TEST FAILED");
            results.add(false);
        }
    }

    //example from section 2.3.2
    //Even number of a's in tree
    private void testEight(){
        System.out.println("Running test 8 -");

        RankedAlphabet f = new RankedAlphabet("f");
        f.add("a", 1);
        f.add("b", 1);
        f.add("Nil", 0);

        Tree t = new Tree("tree8", (String) f.getAlph().get(0), (int) f.getArityArray().get(0), f);
        t.addNextNode( (String) f.getAlph().get(1), (int) f.getArityArray().get(1));
        t.addNextNode( (String) f.getAlph().get(0), (int) f.getArityArray().get(0));
        t.addNextNode( (String) f.getAlph().get(0), (int) f.getArityArray().get(0));
        t.addNextNode( (String) f.getAlph().get(2), (int) f.getArityArray().get(2));

        TreeAutomaton a = new TreeAutomaton("A", f);
        ArrayList<String> q1 = new ArrayList<>();
        a.addRule(getRule((String) f.getAlph().get(2), (int) f.getArityArray().get(2), q1, "qe"));
        ArrayList<String> q2 = new ArrayList<>();
        ArrayList<String> q3 = new ArrayList<>();
        q2.add("qe");
        q3.add("qo");
        a.addRule(getRule((String) f.getAlph().get(0), (int) f.getArityArray().get(0), q2, "qo"));
        a.addRule(getRule((String) f.getAlph().get(0), (int) f.getArityArray().get(0), q3, "qe"));
        a.addRule(getRule((String) f.getAlph().get(1), (int) f.getArityArray().get(1), q2, "qe"));
        a.addRule(getRule((String) f.getAlph().get(1), (int) f.getArityArray().get(1), q3, "qo"));

        a.addFinalState("qe");

        StringBuffer s = new StringBuffer();
        ArrayList<Tree> histories = a.operateAutomata(t, s, false);

        System.out.printf(s.toString());
        if(s.toString().equals(expected[7])){
            System.out.println("TEST PASSED");
            results.add(true);
        } else{
            System.out.println("TEST FAILED");
            results.add(false);
        }
    }

    public TransitionRule getRule(String ele, int arity, ArrayList<String> current, String next){
        TransitionRule r = new TransitionRule(ele, arity);
        r.setCurrentStates(current);
        r.setNewState(next);
        return r;
    }


}
