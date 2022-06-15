package com.SpitTxt.Project_C;

import java.util.ArrayList;

public class Node {
    private TypeSymbol typeSymbol;

    private String value;
    private boolean terminal;
    private String ID;

    private Node head;
    private ArrayList<Node> children;

    private String scope;
    private String regulatedName;

    private boolean isProcedure;
    private boolean isVariable;
    private boolean isField;
    private boolean isDeclaration;
    private boolean isVerified;
    private boolean isConstant;
    private String lineCount;


    //private String castType;



    //private String checks = "N,NN,B T, F ,S, U,M,";

    //private String token;
    //private String type;
    //private String NodeType;
    //private String NodeTypeV;



    Node(String value, boolean terminal) {
        this.typeSymbol = null;
        this.value = value;
        this.terminal = terminal;
        this.ID = "";
        this.scope = "";
        this.regulatedName = "";

        /**************************PROP**************************/
        this.isProcedure = false;
        this.isVariable = false;
        this.isField = false;
        this.isDeclaration = false;
        this.isVerified = false;
        this.isConstant = false;
        this.lineCount = "";
        //this.castType = "";


        /**************************PROP**************************/

        this.head = null;
        this.children = new ArrayList<>();

        //this.changed = false;
        //this.token = "";
        //this.type = "";
        //this.NodeType = "";
        //this.NodeTypeV = "";
    }

    Node(Node oldNode){
        if (oldNode.getTypeSymbol() != null){
            this.typeSymbol = new TypeSymbol(oldNode.getTypeSymbol());
        }else{
            this.typeSymbol = null;
        }

        this.value = new String (oldNode.getValue());
        this.terminal = oldNode.isTerminal();
        this.ID = new String(oldNode.getId());
        this.scope = new String(oldNode.getScope());
        this.regulatedName = new String(oldNode.getRegulatedName());

        /**************************PROP**************************/
        this.isProcedure = new Boolean(oldNode.isProcedure());
        this.isVariable = new Boolean(oldNode.isVariable());
        this.isField = new Boolean(oldNode.isField());
        this.isDeclaration = new Boolean(oldNode.isDeclaration());
        this.isVerified = new Boolean(oldNode.isVerified());
        this.isConstant = new Boolean(oldNode.isConstant());
        //this.castType = new String(oldNode.getCastType());
        this.lineCount = new String(oldNode.getLineCount());


        /**************************PROP**************************/

        this.head = null;
        this.children = new ArrayList<>();
        for(int i =0 ; i < oldNode.getChildren().size(); i++){
            this.addChild(new Node(oldNode.getChildren().get(i)));
        }

        //this.changed = oldNode.isChanged();
        ///this.token = new String(oldNode.getToken());
        //this.type = new String(oldNode.getType());
        //this.ID = new String(oldNode.getId());
        //this.NodeType = new String(oldNode.getNodeType());
        //this.NodeTypeV = new String(oldNode.getNodeTypeV());
    }


    public boolean isTerminal(){
        return terminal;
    }

    public void addChild(Node cNode){
        if(children == null)
            children = new ArrayList<>();

        cNode.setHead(this);
        children.add(cNode);
    }

    public void removeChild(Node child){
        getChildren().remove(child);

        /*for(int i =0 ; i < getChildren().size(); i++){
            if(child == getChildren().get(i)){
                getChildren().remove(child);
            }
        }*/
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

    public String printNode(){

        String output = "";

        if (value.isEmpty() == false){ output = output + value; }
        //if (terminal == false){  }else{  }
        if(ID.isEmpty() == false){ output =  output + " - ID: " + ID; }
        if (scope.isEmpty() == false){ output = output + " - Scope: (" + scope + ")"; }
        if (regulatedName.isEmpty() == false){ output = output + " - Regulated_Name: [" + regulatedName + "]"; }

        return output;
    }


    public ArrayList<Object> getSplitScope() {

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


    /**************************************************GETTERS*****************************************************/


    public String getValue() {
        return this.value;
    }

    public String getId() {
        return this.ID;
    }

    public Node getHead() {
        return this.head;
    }

    public ArrayList<Node> getChildren(){
        return this.children;
    }

    public String getScope() {
        return scope;
    }

    public String getLineCount() {
        return lineCount;
    }

    public boolean isProcedure() {
        return isProcedure;
    }

    public boolean isVariable() {
        return isVariable;
    }

    public boolean isDeclaration() {
        return isDeclaration;
    }

    public TypeSymbol getTypeSymbol() {
        return typeSymbol;
    }

    public boolean isField() {
        return isField;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public String getRegulatedName() {
        return regulatedName;
    }

/* public String getValueType() { return valueType; }

    public String getToken() { return this.token;  }

    public String getType() { return this.type; }

    public String getNodeType() { return this.NodeType; }

    public String getNodeTypeV() { return this.NodeTypeV;

    public String getCastType() {  return castType; }
    }*/



    /**************************************************SETTERS*****************************************************/

    public void setValue(String value){
        this.value = value;
    }

    public void setId(String ID){
        this.ID = ID;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public void setChildren(ArrayList<Node> children){
        this.children = children;

        for(int i =0; i < this.children.size(); i++){
            this.children.get(i).setHead(this);
        }
    }

    public void setScope(String scope) {
        this.scope = scope;
    }



    public void setLineCount(String lineCount) {
        this.lineCount = lineCount;
    }

    public void setProcedure(boolean isProcedure) {
        this.isProcedure = isProcedure;
    }

    public void setVariable(boolean variable) {
        isVariable = variable;
    }

    public void setDeclaration(boolean declaration) {
        this.isDeclaration = declaration;
    }

    public void setTypeSymbol(TypeSymbol typeSymbol) {
        this.typeSymbol = typeSymbol;
    }

    public void setField(boolean field) {
        isField = field;
    }

    public void setVerified(boolean verified) {
        if(isVariable) {
            isVerified = verified;
        }
    }

    public void setConstant(boolean constant) {
        isConstant = constant;
    }

    public void setRegulatedName(String regulatedName) {
        if(this.isVariable || this.isProcedure || this.isField)
            this.regulatedName = regulatedName;
    }

    /*public void setValueType(String valueType) { this.valueType = valueType; }

    public void setToken(String token){ this.token = token; }

    public void setType(String type){ this.type = type; }

    public void setNodeType(String NodeType){ this.NodeType = NodeType; }

    public void setNodeTypeV(String NodeTypeV){ this.NodeTypeV = NodeTypeV; }

    public void setCastType(String castType) { this.castType = castType; }*/



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

    /*public void changeValue(String value){
        this.changed = true;
        this.value = value;
    }

    public boolean isChanged(){
        return changed;
    }*/
}
