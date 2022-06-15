package com.SpitTxt.Project_B;


import java.util.ArrayList;
import java.util.Stack;

public class ParseTree {

    private Node rootNode;
    private Node currentNode;
    private ArrayList<Node> previousNode;
    private boolean pruned;
    private int nodeCount;
    private ArrayList<String> ScopeList;


    private Stack stackedNodes;

    private String names[] = {"PROC", "INSTR", "IO", "CALL", "VAR", "NUMEXPR"};


    ParseTree(){
        this.rootNode = new Node("SPLProgr", false);
        this.currentNode = rootNode;

        this.previousNode = new ArrayList<>();
        this.ScopeList = new ArrayList<>();

        this.pruned = false;
        this.nodeCount =0;
    }

    ParseTree(ParseTree oldTree){
        this.rootNode = new Node(oldTree.getRootNode());
        this.currentNode = this.rootNode;

        this.previousNode = new ArrayList<>();
        this.ScopeList = new ArrayList<>(oldTree.getScopeList());

        this.pruned = new Boolean(oldTree.isPruned());
        this.nodeCount = new Integer(oldTree.getNodeCount());
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


    public ArrayList<String> getScopeList() {
        return ScopeList;
    }

    public void setScopeList(String scope) {
        if(ScopeList.contains(scope) ==false){
            ScopeList.add(scope);
        }
    }

    public ArrayList<Object> getSplitScope(String scope) {

        ArrayList<Object> output = new ArrayList<>();

        String[] listId = scope.split("/");

        String parentID = listId[0];
        output.add(parentID);

        String nodeID = listId[1];
        output.add(nodeID);

        String arrayValues = listId[2].substring(1,listId[2].length() -1); //remove '[' and ']' from string
        String[] arrayID = arrayValues.split(",");
        ArrayList<String> childrenID = new ArrayList<>();
        if(arrayID.length > 1 ){
            for(int i =0; i < arrayID.length; i++){
                childrenID.add(arrayID[i]);
            }
        }else{
            childrenID.add(arrayValues);
        }

        output.add(childrenID);

        return output;
    }

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
        else{
            head = node.getHead();
            head.removeChild(node);
        }

        //currentNode.addChild(node);
        //previousNode.add(currentNode);
        //currentNode =previousNode.get(previousNode.size()-1);
    }


    /*public void addNode(String index, String token){
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
    }*/

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

    public void rollBack(Node value){

        /*if(value == rootNode){
            System.out.println("BACK TO MAIN 1 -> " + value.getValue());
            for(int i =0; i < previousNode.size();i++){
                System.out.println(previousNode.get(i).getValue());
            }
        }else if(previousNode.isEmpty()){
            System.out.println("BACK TO MAIN 2 -> " + value.getValue());
        }*/


        if(previousNode != null) {
            for (int i = previousNode.size() - 1; i >= 0; i--) {
                if (previousNode.get(i) == value ){
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

        treeList.add("|" + rootNode.printNode() );
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


        treeList.add(space + "|" + child.printNode());

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
        /*if (prog != NULL){
            if (prog->childHead != NULL ){
                if(prog->childHead->nextchild != NULL){
                    node::children* tmp = prog->childHead;
                    while (tmp != NULL) {
                        pruneRec(tmp->child);
                        tmp = tmp->nextchild;
                    }
                }else {
                    pruneRec(prog->childHead->child);
                }
                //pruneRec(prog);
            }
        }*/

        if (rootNode != null) {
            if (rootNode.getChildren().isEmpty() == false) {
                if (rootNode.getChildren().size() >= 2) {//if(rootNode.getChildren().get(0).getChildren().isEmpty() == false){
                    ArrayList<Node> tmp = rootNode.getChildren();
                    for(int i =0; i< tmp.size(); i++){
                        pruneRec(tmp.get(i));
                    }
                }else{
                    pruneRec(rootNode.getChild(0));
                }
            }
        }

        this.setIDs();

    }

    public void pruneRec(Node ptr){
        /*if (ptr->childHead != NULL){
            if(ptr->childHead->nextchild != NULL){
                node::children* tmp = ptr->childHead;
                while (tmp != NULL) {
                    pruneRec(tmp->child);
                    tmp = tmp->nextchild;
                }
            }else {

                node* par = ptr->parnt;
                if (par == NULL)
                    cout<<"oi its null"+ ptr->text<<endl;

                node::children* tmp = par->childHead;
                while (tmp != NULL) {
                    if (tmp->child == ptr) {
                        break;
                    }
                    tmp = tmp->nextchild;
                }
                //cout<<"hear"<<endl;
                tmp->child = ptr->childHead->child;
                tmp->child->parnt = ptr->parnt;
                pruneRec(tmp->child);
            }
        }*/


        if(ptr.getChildren().isEmpty() == false){
            if (ptr.getChildren().size() >= 2) {//if(rootNode.getChildren().get(0).getChildren().isEmpty() == false){
                ArrayList<Node> tmp = ptr.getChildren();
                for(int i =0; i< tmp.size(); i++){
                    pruneRec(tmp.get(i));
                }
            }else{
                //pruneRec(rootNode.getChild(0));
                Node parent = ptr.getHead();
                ArrayList<Node> newChilren = new ArrayList<>();

                int index =0;
                for (int i =0; i< parent.getChildren().size(); i++){
                    Node tmp = parent.getChild(i);
                    newChilren.add(tmp);
                    if(tmp == ptr){
                        index = i;
                    }
                }

                //parent.getChild(index).setChildren(ptr.getChildren());

                Node newChild = ptr.getChild(0);
                newChilren.set(index,newChild );

                parent.setChildren(newChilren);

                pruneRec(newChild);

            }
        }
    }




    public void prunet(){
        //this.reset();

        boolean unchanged = false;

        while(unchanged == false) {

            if (rootNode != null) {
                if (rootNode.getChildren().isEmpty() == false) {
                    if (rootNode.getChildren().size() > 1) { // more than one child
                        ArrayList<Node> children = rootNode.getChildren();
                    /*int index =0;
                    while ((index < children.size()) && (children.get(index) != null)) {
                        pruneRec(children.get(index));
                        //tmp = tmp->nextchild;
                        index = index +1;
                    }*/
                        boolean allTrueCheck = true;

                        for (int i = 0; i < children.size(); i++) {
                            if (pruneRect(children.get(i)) == false){
                                allTrueCheck = false;
                            }
                        }

                        if(allTrueCheck == true){
                            unchanged = true;
                        }
                    }
                    else { //single child then prune
                        //pruneRec(rootNode.getChild(0));
                        Node child = rootNode.getChild(0);
                    /*while(child.getChildren().size() == 1){
                        child = child.getChild(0);
                    }

                    if(child.isTerminal()){
                        ArrayList<Node> newChildren = new ArrayList();
                        newChildren.add(child);
                        rootNode.setChildren(newChildren);
                    }else {*/
                        if (child.isTerminal() == false) {
                            rootNode.setChildren(child.getChildren()); //new children


                        /*ArrayList<Node> children = rootNode.getChildren();
                        for (int i =0; i <  children.size(); i++ ){
                            pruneRec(children.get(i));
                        }*/
                            unchanged = false;
                            //prune();
                            return;
                        }


                    }
                }


                //this.pruned = true;
            }
        }
    }

    public boolean pruneRect(Node ptr){


        if (ptr.getChildren().isEmpty() == false || ptr.isTerminal() == false){
            //System.out.println("Number of child : " + ptr.getChildren().length);
            if(ptr.getChildren().size() > 1){ // more than one child
                ArrayList<Node> children = ptr.getChildren();
                    /*int index =0;
                    while ((index < children.size()) && (children.get(index) != null)) {
                        pruneRec(children.get(index));
                        //tmp = tmp->nextchild;
                        index = index +1;
                    }*/
                for (int i =0; i < children.size(); i++ ){
                    pruneRec(children.get(i));
                }
            }else { //single child then prune
                //pruneRec(rootNode.getChild(0));
                Node child = ptr.getChild(0);
                //Node child = rootNode.getChild(0);
                /*while(child.getChildren().size() == 1){
                    child = child.getChild(0);
                }*/

                //prune()
                /*if(child.isTerminal()){
                    ArrayList<Node> newChildren = new ArrayList();
                    newChildren.add(child);
                    ptr.setChildren(newChildren);
                }else {*/
                if(child.isTerminal() == false) {
                    ptr.setChildren(child.getChildren()); //new children

                    /*ArrayList<Node> children = ptr.getChildren();
                    for (int i =0; i <  children.size(); i++ ){
                        pruneRec(children.get(i));
                    }*/

                    //prune();
                    return false;
                }
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

        return true;
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

    public Node getNodeByID(String id){

        System.out.println("small id : " + rootNode.getId() + " - " + id);

        if(rootNode.getId().equals(id)){
            return rootNode;
        }

        if(rootNode.getChildren() != null && rootNode.getChildren().isEmpty() == false){
            int index = 0;
            Node child = rootNode.getChild(index);
            /*while ((child != null) && (child.getId().compareTo(id) < 0)){ // equal/same = 0
                //(index < rootNode.getChildren().size()){
                index = index +1;
                child = rootNode.getChild(index);
            }*/

            for( int i = 0; i <rootNode.getChildren().size(); i++ ) {
                child = rootNode.getChild(i);
                Node output = getNodeByIDChildren(child, id);
                if (output != null) {
                    return output;
                }
            }
        }

        return null;
    }

    public Node getNodeByIDChildren(Node node, String id){

        System.out.println("small id : " + node.getId() + " - " + id);

        if(node == null){
            return null;
        }

        if(node.getId().equals(id)){
            return node;
        }

        if(node.getChildren() != null && node.getChildren().isEmpty() == false ){

            int index = 0;
            Node child = node.getChild(index);
            /*while ((child != null) && (child.getId().compareTo(id) < 0)){ // equal/same = 0
                index = index +1;
                child = node.getChild(index);
            }*/


            for( int i = 0; i <node.getChildren().size(); i++ ) {
                child = node.getChild(i);
                Node output = getNodeByIDChildren(child, id);
                if (output != null) {
                    return output;
                }

                //child = node.getChild(i);
            }

            //Node output = getNodeByIDChildren(child, id);
            //if (output != null){
            //    return output;
            //}


            /*int index = 0;
            while (index < child.getChildren().size()){

                Node output = getNode(child.getChildren().get(index), id);
                if (output != null){
                    return output;
                }

                index = index +1;
            }*/
        }

        return null;
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
}
