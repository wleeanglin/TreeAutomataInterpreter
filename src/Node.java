public class Node {
    String element;
    int numOfChildren;
    Node[] Children;
    int count;

    public Node(String s, int i){
        this.element = s;
        this.numOfChildren = i;
        this.Children = new Node[i];
        count = 0;
    }

    public void addChild(Node n){
        if(count < numOfChildren){
            Children[count] = n;
        } else{
            System.err.println("Children full");
        }
    }
}
