package com.SpitTxt.Project_A;



import java.util.ArrayList;
import java.util.Stack;

public class ParseTree {

    private Node rootNode;
    private Node currentNode;
    private ArrayList<Node> previousNode;
    private boolean pruned;
    private int nodeCount;


    private Stack stackedNodes;

    private String names[] = {"PROC", "INSTR", "IO", "CALL", "VAR", "NUMEXPR"};


    ParseTree(){
        this.rootNode = new Node("SPLProgr", false);
        this.currentNode = rootNode;
        this.previousNode = new ArrayList<>();

        this.pruned = false;
        nodeCount =0;
    }

    ParseTree(ParseTree oldTree){
        this.rootNode = new Node(oldTree.getRootNode());
        this.currentNode = this.rootNode;

        this.previousNode = new ArrayList<>();

        this.pruned = new Boolean(oldTree.isPruned());
        nodeCount = new Integer(oldTree.getNodeCount());
    }





    public Node getRootNode(){
        return this.rootNode;
    }

    public Node getCurrentNode(){
        if(currentNode != rootNode)
            return this.currentNode;
        return null;
    }

    public ArrayList<Node> getPreviousNode(){
        return this.previousNode;
    }


    public Boolean isPruned(){
        return this.pruned;
    }

    public int getNodeCount(){
        count();
        return this.nodeCount;
    }



    /*private void printChildrens(Node child, String space) {
        //System.out.println(child);
        //System.out.println(space + "|" + child.getValue());
        treeList.add(space + "|" + child.getValue());

        if(child.getChildren() != null) {
            int index = 0;
            while (index < child.getChildren().size()) {
                printChildren(child.getChildren().get(index), space + "\t"); //
                index = index +1;
            }
        }
        else
            return;
    }*/


    public void addTreeNode(Node node){
        currentNode.addChild(node);

        previousNode.add(currentNode);
        currentNode = node;
    }

    public void removeTreeNode(Node node){
        Node head = null;

        if(currentNode == node){
            head = currentNode.getHead();
            head.removeChild(node);
            currentNode =previousNode.get(previousNode.size()-1);
        }
        //else{
        //    head = rootNode;
        //}

        //currentNode.addChild(node);
        //previousNode.add(currentNode);
        //currentNode =previousNode.get(previousNode.size()-1);
    }


    public void addNode(String index, String token){
        Node newNode = new Node(index, false);
        newNode.setToken(token);
        currentNode.addChild(newNode);

        //if(previousNode == null) {
        //    this.previousNode = new ArrayList<>();
        //}

        previousNode.add(currentNode);
        currentNode = newNode;

        System.out.println("here 1 : " + index);
    }

    public void addTermNode(String payload, String token){
        Node newNode = new Node(payload, true);
        newNode.setToken(token);

        String type = "";
        if(payload.charAt(0) == '"')
            type = "string";
        else
            type = "number";

        if(token.equals("User-defined name")){
            if (currentNode.getValue().equals("VAR")){
                newNode.setType("VAR");
                //tempNode.setNodeTypeV(type);
            }
            else if (currentNode.getValue().equals("CALL")){
                newNode.setType("CALL");
                //tempNode.setNodeTypeV(type);
            }
        }

        if(token.equals("Short String")){
            newNode.setNodeTypeV("string");
        }

        if(token.equals("Integer")){
            newNode.setNodeTypeV("number");
        }

        currentNode.addChild(newNode);

        System.out.println("here 2 : " + payload);
    }

    public void reset(){
        currentNode = rootNode;
        previousNode = new ArrayList<>();

        //this.treeList = new ArrayList<>();
    }

    public void removeLast(){
        /*Node[] tmp = previousNode.get(previousNode.size()-1).getChildren();
        System.out.println("Remove parent : " + previousNode.get(previousNode.size()-1).getValue());

        int index =0;
        while ( (index < tmp.length) && (tmp[index] != null)) {
            if(tmp[index].equals(currentNode)){
                break;
            }
            index = index +1;
        }

        System.out.println("Remove child : " + tmp[index].getValue() + " : " + currentNode.getValue());

        previousNode.get(previousNode.size()-1).setChild(index,null);
        previousNode.get(previousNode.size()-1).fixChildren();*/

        /*******************WORL***********************/

        System.out.println("Remove parent : " + currentNode.getValue());
        if(currentNode.getChildren() != null){
            System.out.println("Remove child : " + currentNode.getChild(currentNode.getChildren().size()-1).getValue()+ " : " + currentNode.getValue());
            //currentNode.setChild(currentNode.getChildren().length-1,null);
            //currentNode.fixChildren();

            currentNode.removeLastChild();
        }
    }

    public void rollBack(String value){
        if(previousNode != null) {
            for (int i = previousNode.size() - 1; i > 0; i--) {
                if (previousNode.get(i).getValue().equals(value)) {
                    currentNode = previousNode.get(i);
                    int index = i;
                    while (index < previousNode.size()) {
                        previousNode.remove(index);
                        index = index + 1;
                    }
                    break;
                }
            }
        }
    }

    public ArrayList<String> printTree(){
        this.reset();

        /*if(previousNode == null) {
            this.treeList = new ArrayList<>();
        }*/

        ArrayList<String> treeList = new ArrayList<>();

        treeList.add("|" + rootNode.getValue());
        //System.out.println("|" + currentNode.getValue());
        if(rootNode.getChildren() != null){
            int index = 0;
            while (index < rootNode.getChildren().size()){
                printChildren(rootNode.getChildren().get(index) , "\t", treeList);
                index = index +1;
            }
        }
        return treeList;
    }




    private void printChildren(Node child, String space, ArrayList<String> treeList) {
        //System.out.println(child);
        //System.out.println(space + "|" + child.getValue());


        treeList.add(space + "|" + child.getValue());

        if(child.getChildren() != null) {
            int index = 0;
            while (index < child.getChildren().size()) {
                printChildren(child.getChildren().get(index), space + "\t", treeList); //
                index = index +1;
            }
        }
        else
            return;
    }



    public void prune(){
        this.reset();

        if (rootNode != null){
            if (rootNode.getChildren().isEmpty() == false){
                if(rootNode.getChildren().size() > 1){ // more than one child
                    ArrayList<Node> children = rootNode.getChildren();
                    /*int index =0;
                    while ((index < children.size()) && (children.get(index) != null)) {
                        pruneRec(children.get(index));
                        //tmp = tmp->nextchild;
                        index = index +1;
                    }*/
                    for (int i =0; i <  children.size(); i++ ){
                        pruneRec(children.get(i));
                    }
                }else { //single child then prune
                    //pruneRec(rootNode.getChild(0));
                    Node child = rootNode.getChild(0);
                    rootNode.setChildren(child.getChildren()); //new children

                    ArrayList<Node> children = rootNode.getChildren();
                    for (int i =0; i <  children.size(); i++ ){
                        pruneRec(children.get(i));
                    }
                }
            }

            this.pruned = true;
        }
    }

    public void pruneRec(Node ptr){

        if (ptr.getChildren().isEmpty() == false){
            //System.out.println("Number of child : " + ptr.getChildren().length);
            if(ptr.getChildren().size() > 1){ // more than one child
                ArrayList<Node> children = ptr.getChildren();
                    /*int index =0;
                    while ((index < children.size()) && (children.get(index) != null)) {
                        pruneRec(children.get(index));
                        //tmp = tmp->nextchild;
                        index = index +1;
                    }*/
                for (int i =0; i <  children.size(); i++ ){
                    pruneRec(children.get(i));
                }
            }else { //single child then prune
                //pruneRec(rootNode.getChild(0));
                Node child = ptr.getChild(0);
                ptr.setChildren(child.getChildren()); //new children

                ArrayList<Node> children = ptr.getChildren();
                for (int i =0; i <  children.size(); i++ ){
                    pruneRec(children.get(i));
                }


                /*Node parent = ptr.getHead();
                Node child[] = ptr.getChildren();


                Node tmp[] = parent.getChildren();
                int index =0;
                while ( (index < tmp.length) && (tmp[index] != null)) {
                    if(tmp[index].equals(ptr)){
                        break;
                    }
                    index = index +1;
                }

                parent.setChild(index,child[0]);
                pruneRec(child[0]);*/
            }
        }
    }

    public void setIDs() {
        //if (this.pruned == true) {

            nodeCount = 0;
            String sId = String.valueOf(nodeCount);
            rootNode.setId(sId);

            if (rootNode.getChildren() != null) {
                int index = 0;
                while (index < rootNode.getChildren().size()) {
                    nodeCount = nodeCount+1;
                    sId = String.valueOf(nodeCount);

                    setChildID(rootNode.getChildren().get(index), sId);
                    index = index + 1;
                }
            }

        //}
    }

    private void setChildID(Node child, String Id) {

        child.setId(Id);
        String sId = "";

        if(child.getChildren() != null) {
            int index = 0;
            while (index < child.getChildren().size()) {
                nodeCount = nodeCount+1;
                sId = String.valueOf(nodeCount);

                setChildID(child.getChildren().get(index), sId); //
                index = index +1;
            }
        }
        else
            return;
    }

    public void count(){
        nodeCount = 1;
        if(rootNode.getChildren() != null){
            int index = 0;
            while (index < rootNode.getChildren().size()){
                childCount(rootNode.getChildren().get(index));
                index = index +1;
            }
        }

    }

    private void childCount(Node child) {
        nodeCount = nodeCount +1;
        if(child.getChildren() != null) {
            int index = 0;
            while (index < child.getChildren().size()) {
                childCount(child.getChildren().get(index)); //
                index = index +1;
            }
        }
        else
            return;
    }
}
