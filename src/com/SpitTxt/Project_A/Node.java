package com.SpitTxt.Project_A;

import java.util.ArrayList;

public class Node {
    private String value;
    private boolean terminal;


    private Node head;
    private ArrayList<Node> children;


    private String token;
    private String type;
    private String ID;
    private String NodeType;
    private String NodeTypeV;
    private boolean changed;



    Node(String value, boolean terminal) {
        this.value = value;
        this.terminal = terminal;
        this.changed = false;

        this.head = null;
        this.children = new ArrayList<>();


        this.token = "";
        this.type = "";
        this.ID = "";
        this.NodeType = "";
        this.NodeTypeV = "";
    }

    Node(Node oldNode){
        this.value = new String (oldNode.value);
        this.terminal = oldNode.isTerminal();


        this.head = null;
        this.children = new ArrayList<>();
        for(int i =0 ; i < oldNode.getChildren().size(); i++){
            this.addChild(new Node(oldNode.getChildren().get(i)));
        }


        this.changed = oldNode.isChanged();
        this.token = new String(oldNode.getToken());
        this.type = new String(oldNode.getType());
        this.ID = new String(oldNode.getId());
        this.NodeType = new String(oldNode.getNodeType());
        this.NodeTypeV = new String(oldNode.getNodeTypeV());

    }

    public void changeValue(String value){
        this.changed = true;
        this.value = value;
    }

    public boolean isTerminal(){
        return terminal;
    }

    public boolean isChanged(){
        return changed;
    }

    public void removeChild(Node child){
        getChildren().remove(child);

        /*for(int i =0 ; i < getChildren().size(); i++){
            if(child == getChildren().get(i)){
                getChildren().remove(child);
            }
        }*/
    }




    public String getValue() {
        return this.value;
    }

    public Node getHead() {
        return this.head;
    }

    public ArrayList<Node> getChildren(){
        return this.children;
    }

    public String getToken() {
        return this.token;
    }

    public String getType() {
        return this.type;
    }


    public String getId() {
        return this.ID;
    }

    public String getNodeType() {
        return this.NodeType;
    }

    public String getNodeTypeV() {
        return this.NodeTypeV;
    }




    public void setValue(String value){
        this.value = value;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public void setChildren(ArrayList<Node> children){
        this.children = children;
    }

    public void setToken(String token){
        this.token = token;
    }


    public void setType(String type){
        this.type = type;
    }

    public void setId(String ID){
        this.ID = ID;
    }

    public void setNodeType(String NodeType){
        this.NodeType = NodeType;
    }

    public void setNodeTypeV(String NodeTypeV){
        this.NodeTypeV = NodeTypeV;
    }







    public void addChild(Node cNode){
        if(children == null)
            children = new ArrayList<>();

        cNode.setHead(this);
        children.add(cNode);
    }

    public Node getChild(int index){
        if(children == null)
            return null;

        if((index <0) || (index >= children.size()))
            return null;

        return children.get(index);
    }

    public void removeLastChild(){
        children.remove(children.size()-1);
    }


    public void setChilds(int index, Node cNode){
        //System.out.println("Pruning here >>>>");

        /*if(index < children.length){
            children[index] = cNode;
        }*/
    }


    public void fixChildrens() {
        /*int newLength = 0;
        for (int i = 0; i < children.length; i++){
            if (children[i] != null)
                newLength = newLength + 1;
        }


        if(newLength == 0){
            children = null;
            return;
        }


        Node[] array = new Node[newLength];
        int index = 0;
        for(int i=0;i<array.length; i++) {
            if (children[index] != null) {
                array[i] = children[index];
            } else {
                index = index + 1;
                while ((index) < children.length) {
                    if (children[index] != null) {
                        array[i] = children[index];
                        break;
                    }
                    index = index + 1;
                }
            }

            index = index + 1;
        }

        children = array;

        System.out.print("Fixed Array : [");
        for(int i=0; i < children.length; i++){
            System.out.print( children[i].getValue() + "-");
        }
        System.out.print("]");
        System.out.println();*/
    }
}
