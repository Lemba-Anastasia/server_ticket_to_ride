package model;
public class Edge {
    private Node firstNode;
    private Node secondNode;
    private boolean built = false;


    public Edge(Node node1, Node node2) {
        this.firstNode = node1;
        this.secondNode = node2;
    }

    public boolean isBuilt() {
        return built;
    }

    public void setBuilt(boolean built) {
        this.built = built;
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public Node getSecondNode() {
        return secondNode;
    }

    public boolean containNode(Node node) {
        return firstNode == node || secondNode == node;
    }

    public Node getAnotherNode(Node node){
        if(node==firstNode) {return  secondNode;}
        else if(node==secondNode){ return firstNode;}
        else return null;
    }
}
