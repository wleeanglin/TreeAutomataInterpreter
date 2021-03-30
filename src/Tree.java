import java.util.ArrayList;

public class Tree {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private Node root;
    private String name;
    private RankedAlphabet alphabet;

    //For building tree;
    private Node currentNode;
    private ArrayList<Node> toComplete;
    private Boolean complete = false;

    public Tree(){

    }

    public Tree(String s, String data, int i, RankedAlphabet a){
        this.root = new Node(data, i, null);
        this.root.setHeight(1);
        this.name = s;
        this.alphabet = a;
        this.currentNode = this.root;
        this.toComplete = new ArrayList<>();
        toComplete.add(currentNode);
    }

    public void instansiate(String data, int i, RankedAlphabet a){
        this.root = new Node(data, i, null);
        this.root.setHeight(1);
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

    public void setName(String s){
        this.name = s;
    }

    public void setAlphabet(RankedAlphabet a){
        this.alphabet = a;
    }

    public Boolean getComplete(){
        return this.complete;
    }

    public void setComplete(Boolean b){
        this.complete = b;
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

    public String guiPrint(){return traversePreOrder(this.root, true);}

    public void print(){
        System.out.println(traversePreOrder(this.root, false));
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

    private String traversePreOrder(Node root, Boolean guiFlag){
        if(root == null){
            return "";
        }

        StringBuilder s = new StringBuilder();
        if (guiFlag) {
            s.append(root.getData());
        } else{
            if(root.getState()){
                s.append(ANSI_RED + root.getData() + ANSI_RESET);
            } else{
                s.append(ANSI_CYAN + root.getData() + ANSI_RESET);
            }
        }

        ArrayList<Node> children = root.getChildren();
        String pointerForRightmost = "└──";
        String pointerForOthers = (children.size() > 1) ? "├──" : "└──";

        for(int i = 0; i < children.size(); i++){
            if(i != children.size() - 1){
                traverseNodes(s, "", pointerForOthers, children.get(i), children.size() > 1, guiFlag);
            } else {
                traverseNodes(s, "", pointerForRightmost, children.get(i), false, guiFlag);
            }
        }

        return s.toString();
    }

    private void traverseNodes(StringBuilder s, String padding, String pointer, Node node, boolean hasSibling, boolean guiFlag){
        if (node != null) {
            s.append("\n");
            s.append(padding);
            s.append(pointer);
            if(guiFlag){
                s.append(node.getData());
            } else{
                if(node.getState()){
                    s.append(ANSI_RED + node.getData() + ANSI_RESET);
                } else{
                    s.append(ANSI_CYAN + node.getData() + ANSI_RESET);
                }
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
                    traverseNodes(s, paddingForBoth, pointerForOthers, children.get(i), children.size() > 1, guiFlag);
                } else {
                    traverseNodes(s, paddingForBoth, pointerForRightmost, children.get(i), false, guiFlag);
                }
            }
        }
    }

    public void addNextNode(String s, int arity){
        if(this.root == null){
            this.root = new Node(s, arity, null);
            this.root.setHeight(1);
            this.currentNode = this.root;
            this.toComplete = new ArrayList<>();
            toComplete.add(currentNode);
        } else{
            while(true){
                if(currentNode.getChildren().size() < currentNode.getNumChildren()){
                    Node newNode = currentNode.addChild(s, arity);
                    newNode.setHeight(this.currentNode.getHeight() + 1);
                    toComplete.add(newNode);
                    break;
                } else{
                    toComplete.remove(this.currentNode);
                    if(toComplete.size() > 0){
                        this.currentNode = toComplete.get(0);
                    } else{
                        this.complete = true;
                        break;
                    }
                }
            }
        }

        //maybe could be done more gracefully
        if(toComplete.size() == 0){
            this.complete = true;
        } else{
            this.complete = true;
            for(int i = 0; i < toComplete.size(); i++){
                if(toComplete.get(i).getChildren().size() < toComplete.get(i).getNumChildren()){
                    this.complete = false;
                }
            }
        }
    }

    public String getQuestionTree(){
        String s;
        if(currentNode.getChildren().size() < currentNode.getNumChildren()){
            Node temp = currentNode.addChild("?", 0);
            s = this.guiPrint();
            currentNode.removeChild();
        } else if(questionCheck() != -1){
            Node n = toComplete.get(questionCheck());
            Node temp = n.addChild("?", 0);
            s = this.guiPrint();
            n.removeChild();
        } else{
            s = this.guiPrint();
        }
        return s;
    }

    public int questionCheck(){
        for(int i = 0; i < toComplete.size(); i++){
            if(toComplete.get(i).getChildren().size() < toComplete.get(i).getNumChildren()){
                return i;
            }
        }
        return -1;
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

        //Removes last added child
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
