import java.util.ArrayList;

public class Tree {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private Node root;
    private String name;
    private RankedAlphabet alphabet;

    public Tree(){

    }

    public Tree(String s, String data, int i, RankedAlphabet a){
        this.root = new Node(data, i, null);
        this.root.setHeight(1);
        this.name = s;
        this.alphabet = a;
    }

    public Tree copyTree(){
        Tree t = new Tree(this.name, this.root.getData(), this.root.getNumChildren(), this.alphabet);
        recursiveCopy(this.root, t.getRoot());
        return t;

}
    public void recursiveCopy(Node currentNode, Node newNode){
        newNode.setState(currentNode.getState());
        for(int i = 0; i < currentNode.getNumChildren(); i++){
            Node nextNode = newNode.addChild(currentNode.getChildren().get(i).getData(), currentNode.getChildren().get(i).getNumChildren());
            recursiveCopy(currentNode.getChildren().get(i), nextNode);
        }
    }

    public String getName(){
        return this.name;
    }

    public int getMaxHeight(){
        int maxHeight = recursiveHeight(this.root, 1);
        return maxHeight;
    }

    public int recursiveHeight(Node n, int maxHeight){
        if(n.getHeight() > maxHeight){
            maxHeight = n.getHeight();
        }
        for(int i = 0; i < n.getNumChildren(); i++){
            int subTreeHeight = recursiveHeight(n.getChildren().get(i), maxHeight);
            if(subTreeHeight > maxHeight){
                maxHeight = subTreeHeight;
            }
        }
        return maxHeight;
    }

    public Node getRoot(){
        return this.root;
    }

    public void setRoot(Node r){
        this.root = r;
    }

    public RankedAlphabet getAlphabet(){
        return this.alphabet;
    }

    public void print(){
        System.out.println(traversePreOrder(this.root));
    }

    public ArrayList<ArrayList<Node>> convert(){
        ArrayList<ArrayList<Node>> levels = new ArrayList<>();
        for(int i = 0; i < this.getMaxHeight(); i++){
            levels.add(new ArrayList<>());
        }

        recursiveConvert(levels, this.root);

        return levels;
    }

    public ArrayList<ArrayList<Node>> recursiveConvert(ArrayList<ArrayList<Node>> levels, Node n){
        levels.get(n.getHeight() - 1).add(n);
        for(int i = 0; i < n.getNumChildren(); i++){
            levels = recursiveConvert(levels, n.getChildren().get(i));
        }
        return levels;
    }

    private String traversePreOrder(Node root){
        if(root == null){
            return "";
        }

        StringBuilder s = new StringBuilder();
        if(root.getState()){
            s.append(ANSI_RED + root.getData() + ANSI_RESET);
        } else{
            s.append(ANSI_CYAN + root.getData() + ANSI_RESET);
        }

        ArrayList<Node> children = root.getChildren();
        String pointerForRightmost = "└──";
        String pointerForOthers = (children.size() > 1) ? "├──" : "└──";

        for(int i = 0; i < children.size(); i++){
            if(i != children.size() - 1){
                traverseNodes(s, "", pointerForOthers, children.get(i), children.size() > 1);
            } else {
                traverseNodes(s, "", pointerForRightmost, children.get(i), false);
            }
        }

        return s.toString();
    }

    private void traverseNodes(StringBuilder s, String padding, String pointer, Node node, boolean hasSibling){
        if (node != null) {
            s.append("\n");
            s.append(padding);
            s.append(pointer);
            if(node.getState()){
                s.append(ANSI_RED + node.getData() + ANSI_RESET);
            } else{
                s.append(ANSI_CYAN + node.getData() + ANSI_RESET);
            }

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if(hasSibling){
                paddingBuilder.append("│  ");
            } else{
                paddingBuilder.append("   ");
            }

            ArrayList<Node> children = node.getChildren();

            String paddingForBoth = paddingBuilder.toString();
            String pointerForRightmost = "└──";
            String pointerForOthers = (children.size() > 1) ? "├──" : "└──";

            for(int i = 0; i < children.size(); i++){
                if(i != children.size() - 1){
                    traverseNodes(s, paddingForBoth, pointerForOthers, children.get(i), children.size() > 1);
                } else {
                    traverseNodes(s, paddingForBoth, pointerForRightmost, children.get(i), false);
                }
            }
        }
    }

    public static class Node{
        private String data;
        private int numChildren;
        private ArrayList<Node> children;
        private Node parent;
        private int height;
        private Boolean state;

        public Node(String s, int i, Node parent){
            this.data = s;
            this.numChildren = i;
            this.parent = parent;
            this.children = new ArrayList<>();
            this.state = false;
        }

        public Node addChild(String child, int arity){
            if(children.size() < numChildren){
                Node childNode = new Node(child, arity, this);
                childNode.setHeight(height + 1);
                this.children.add(childNode);
                return childNode;
            } else {
                System.err.println("Unexpected error; node full");
                return null;
            }
        }

        public void removeChild(){
            if(children.size() > 0){
                children.remove(children.get(children.size() - 1));
            }
        }

        public void addChildNode(Node n){
            if(children.size() < numChildren) {
                children.add(n);
            }
        }

        public String getData(){
            return this.data;
        }

        public int getNumChildren(){
            return this.numChildren;
        }

        public ArrayList<Node> getChildren(){
            return this.children;
        }

        public ArrayList<String> getChildrenAsString(){
            ArrayList<String> s = new ArrayList<>();
            for(int i = 0; i < this.children.size(); i++){
                s.add(this.children.get(i).getData());
            }
            return s;
        }

        public void setData(String s){
            this.data = s;
        }

        public void setArity(int i){
            this.numChildren = i;
        }

        public void setHeight(int i){
            this.height = i;
        }

        public int getHeight(){
            return this.height;
        }

        public Node getParent(){
            return this.parent;
        }

        public void setParent(Node n){
            this.parent = n;
        }

        public Boolean getState(){
            return this.state;
        }

        public void setState(Boolean t){
            this.state = t;
        }
    }

}
