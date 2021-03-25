# TreeAutomataInterpreter
Java interpreter for bottom up finite deterministic tree automata


To launch to main menu...
```
javac *.java
java TreeMain
```
At startup select either [g]ui or [c]onsole.


#Tree automata information
Tree automata are an expansion to finite state automata that work on tree structures instead of the strings that are normally dealt with by more conventional finite state automata. 

This tool visualises the acceptance process for bottom up deterministic tree automata. To understand the definition of a bottom up deterministic tree automaton we will introduce the elements required for a simple operation using the tools included examples.

A ranked alphabet is a couple (F, Arity) where F is a finite, nonempty alphabet and Arity is a mapping from F to the natural numbers, that is each element of the alphabet has an associated natural number. The arity of an element in F represents the number of child nodes this element will have in our trees. When defining a ranked alphabet there must be at least one element defined with arity 0, else any tree defined would go on infinitely.

A tree in our context is simply a hierarchical data structure consisting of nodes which have zero or more child nodes. We use elements of a ranked alphabet to define a tree where a node with a label of an element of f in F has Arity(f) child nodes.

A bottom-up deterministic finite tree automaton is defined as a tuple containing the following;

*Q, a set of a unary states (i.e. arity=1) 

*F, a ranked alphabet

*Qf, a set of final states

*Δ, a set of transition rules

As we are working with bottom-up  finite tree automata the transition rules are of the form f(q1(x1),...,qn(xn)) → q(f(x1,...,xn)) where f is an element of our ranked alphabet with arity n, qi are elements of our set of states and xi are subtrees. That is, the state of a node is determined by the state of its children, i.e. ‘bottom-up’.

This tool visualises the acceptance process for a tree passed into a tree automaton. This process consists of the automaton working on a tree using its transition rules until either there are no more transition rules that can be applied or then root node is a state. This state is then checked against the set of final states to see whether the tree was ‘accepted’ or ‘rejected’. To see an example of the operation process in action click ‘example walk through’ in the tutorial menu. 

#Included examples

To witness the acceptance process of a tree with a bottom-up deterministic tree automata we can use the tool with one of the provided examples. Go back to the main menu and click operate.

Now, select ExampleAutomaton1 and ExampleTree1 from the left and right columns respectively before clicking operate. 

This will bring us to the operation process display. The rules and final states of ExampleAutomaton1 will now be displayed. We’ll use these to work through the operation process together using the tool. 
The first step of the process is always just the tree before any transition rules have been applied. We work from the bottom of the tree to the top. The lowest node in this tree is the ‘a’ on the far right. This node does not have any children so we will apply the rule from the automaton which sees element ‘a’ with no child states, which there can only be one of because this automaton is deterministic. We can see that this rule results in q1, so we give the node we are looking at parent ‘q1’, representing the state q1. Click ‘>’ and you should see this happen.

There are 3 nodes on the same level now. It doesn’t matter which order we look at these nodes in, however this tool will always look at the left-most node of a level, which is represented graphically by this tool as the node closest to the top of the window on a given level. In this case this happens to be a node labelled by the unary element ‘b’. This element has child state ‘q1’. We will look or a rule in the tree automaton which has element ‘b’ with child state ‘q1’ which we can see results in state ‘q2’. Similarly to the previous step when you click ‘>’ the node b is given parent state ‘q2’.

Click ‘>’ twice more, as the next two nodes are in the same situation as the first ‘a’ node that was operated on. Next we come to the rightmost lowest ‘c’ node. This node has child states q1 and q2. The rule in the automaton with element ‘c’ and states ‘q1’ and ‘q2’ results in state ‘q3’. Click ‘>’ to continue. 

Keep pressing ‘>’ until the acceptance test is complete. As the root node is state ‘q4’ after all possible transition rules have been applied and the state ‘q4’ is a member of the final states set of the automaton, this tree is accepted by this automaton. 

#GUI Tutorial

When inputting your own trees and automata to test for acceptance there are 3 steps in the definition process. The first step is to input a new ranked alphabet. To do this go to the main menu and click new and then click alphabet.
Firstly you will need to choose a name for your new alphabet. After the name has been selected you can continue to the main alphabet input page. To add new elements to the alphabet write an element in the ‘element box’ and its associated arity in the ‘arity’ box before pressing add. You can remove elements from the alphabet by selecting them from the list on the right hand side and clicking remove. When you are finished defining an alphabet click finish to go back to the menu. You must have at least one element with arity 0 to finish defining a new alphabet. Without this and tree defined would be infinite. 

Next you can define a tree that you would like to operate on using you’re new ranked alphabet. To do this go back to the main menu, click new and then tree. Then, similarly to when defining a new alphabet, you can select a name for your new tree. Once this has been done the next step is to select an alphabet to use for the definition of the tree. To do this select whichever alphabet from the list before clicking continue.
Now to define the tree you must first select which element you would like at the root of the tree. This can be any element from the list on the left hand side of the window but remember the number of children of a node is determined by arity of the element selected. The tree is then defined level by level with the position of the next node to be added to the tree represented by the ‘?’ in the graphical representation of the tree on the right side of the window. 
When the tree has been fully defined, i.e. each leaf node is a node labelled by an element of arity 0, the definition process is complete. 

Finally you have to define a bottom-up tree automaton. To do this, go back to the ‘new’ menu and select automaton. First thing to do is to select a name and alphabet to use for this automaton. If you’d like to operate this automaton on the tree just defined you should select the same alphabet used previously. 
Now you need to go through and define the transition rules. As we are working with bottom up finite tree automaton you will need 3 components for each transition rule. An element f, which is what the node is labelled, arity(f) current states, which are the child states of the node, a new state, which is what the node will transition to. The states are actively stored as you define rules, so can be any string. When you are finished defining rules click continue.
Finally you should select any number of final states. Once you click finish the tree automaton has been fully defined.

To test an automatons acceptance of a tree go back to the main menu, click operate, select an automaton and a tree defined using the same alphabet and click operate. Step back and forth the acceptance process until no more rules can be applied. For a more detailed explanation of this process follow the example walk thro