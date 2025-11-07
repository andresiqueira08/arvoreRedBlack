package arvoreredblack;

class Node {
    private int info;
    private Node left, right, parent;
    private boolean red; // true = vermelho, false = preto

    public Node(int info) {
        this.info = info;
        this.red = true; // novo nó sempre vermelho
    }

    public int getInfo(){
        return info; 
    }
    public void setInfo(int info) {
        this.info = info; 
    }

    public Node getLeft(){
        return left; 
    }
    public void setLeft(Node left){ 
        this.left = left; 
    }

    public Node getRight(){
        return right; 
    }
    public void setRight(Node right){
        this.right = right; 
    }

    public Node getParent(){
        return parent; 
    }
    public void setParent(Node parent){
        this.parent = parent; 
    }

    public boolean isRed(){ 
        return red; 
    }
    public void setRed(boolean red){
        this.red = red; 
    }
}
