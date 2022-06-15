package com.SpitTxt.Project_B;


import java.util.*;

public class Scoper {
    //private ArrayList<String> treeList;
    //private HashMap<String,List<Object>> symbolTable;
    //Arrays.asList(tokenType, newWord , index)

    private ParseTree myTree;
    private HashMap<String,ArrayList<Node>> symbolTable;
    LinkedList<List<Object>> Tokenlist;

    private ArrayList<Integer> usedValues;
    private Stack scopeStack;
    private Stack scopeParentStack;

    private int localScope;
    private int parentScope;
    private ArrayList<Integer> childrenScopes;
    private Node terminalScopeNode;


    private ArrayList<String> foundScopes;
    private ArrayList<String> foundChildScopes;

    Scoper(ParseTree tree, LinkedList<List<Object>> Tokenlist){
        this.myTree = tree;
        this.Tokenlist = Tokenlist;
        this.symbolTable = new HashMap<>();

        this.usedValues = new ArrayList<>();
        this.scopeStack = new Stack();
        this.scopeParentStack = new Stack();

        this.localScope = 0;
        this.parentScope = -1;
        this.childrenScopes = new ArrayList<>();
        this.terminalScopeNode = null;

        this.foundScopes = new ArrayList<>();
        this.foundChildScopes = new ArrayList<>();
    }

    public void begin(){
        myTree.setIDs();
        scanTree(false);
        fixChildren();

        myTree.prune();
        scanTree(true);
    }

    public HashMap<String,ArrayList<Node>> getSymbolTable(){
        return symbolTable;
    }


    private void fixChildren() {
        //ArrayList<String> children = new ArrayList<>();
        for(int i=0; i < foundScopes.size(); i++){
            foundChildScopes.add(getChildrenScopes(i));
        }
    }

    private String getChildrenScopes(int index) {
        String output =  "/[";

        String[] scopeId = foundScopes.get(index).split("/");
        String local = scopeId[0];
        String parent = scopeId[1];

        ArrayList<String> foundList = new ArrayList<>();

        boolean emptyChildren = true;

        for(int i=0; i < foundScopes.size(); i++){
            if( (i != index) ){
                String[] newScopeId = foundScopes.get(i).split("/");
                String newLocal = newScopeId[0];
                String newParent = newScopeId[1];

                if(foundList.contains(newLocal))
                    continue;

                if( (newParent.equals(local)) && (emptyChildren == true)){
                    foundList.add(newLocal);

                    emptyChildren = false;
                    output = output + newLocal;
                }else if((newParent.equals(local)) && (emptyChildren == false)){
                    foundList.add(newLocal);
                    output = output + "," +newLocal;
                }
            }
        }
        output = output + "]";

        return output;
    }

    private void setupSymbolTable(String value, Node node) {

        //if(node.isProcedure()) //todo:  check if is scope
        //   value = value + "_P";
        //else if (node.isVariable())
        //    value = value + "_V";

        if(node.isConstant() == true){
            value = "Constant";
        }

        if(symbolTable.containsKey(value)) {

            ArrayList<Node> oldList = symbolTable.get(value);
            if(oldList.contains(node) == true){
                return;
            }

            ArrayList<Node> valueList = new ArrayList<>();
            for(int i =0; i < oldList.size(); i++)
                valueList.add(oldList.get(i));
            valueList.add(node);
            //symbolTable.put(value,valueList);
            symbolTable.replace(value,oldList, valueList);
        }else{
            ArrayList<Node> list = new ArrayList<>();
            list.add(node);
            symbolTable.put(value, list);
        }
    }






    public void scanTree(boolean isFinalScan) {
        //ArrayList<String> treeList = new ArrayList<>();

        this.myTree.setIDs();

        Node rootNode = myTree.getRootNode();

        String scope = "";

        //if(rootNode.getValue().equals("SPLProgr")){
        scope = getScope(rootNode , isFinalScan);
        rootNode.setScope(scope);
        //}

        //ArrayList<Node> nodeList= new ArrayList<>(); //parralel arrays
        //ArrayList<Boolean> infolist= new ArrayList<>();
        //List<Object> tableData =  Arrays.asList(rootNode, scope,nodeList,infolist);

        if (isFinalScan == true) {
            setupSymbolTable(rootNode.getValue(), rootNode);
            myTree.setScopeList(rootNode.getScope());
        }

        //treeList.add("|" + rootNode.getValue() +" : ID - " + rootNode.getId() + " : SCOPE - " + scope);
        //System.out.println("|" + rootNode.getValue() +" : ID - " + rootNode.getId() + " : SCOPE - " + scope);

        if(rootNode.getChildren() != null){
            int index = 0;
            while (index < rootNode.getChildren().size()){
                scanChildren(isFinalScan,rootNode.getChildren().get(index) , "\t",scope);
                index = index +1;
            }
        }

        //return treeList;
    }

    private void scanChildren(boolean isFinalScan, Node child, String space, String Scope) {
        //process
        /*String scope = Scope;
        String[] scopeValues;
        if(scope.length() > 1) {
            scopeValues = splitScope(scope);
        }
        else {
            scopeValues = new String[1];
            scopeValues[0] = scope;
        }

        if(child.getValue().equals("COND_LOOP")){
            if(checkForLoop(child)) {
                int index = 0;
                String newScope = String.valueOf(index);

                //System.out.println(checkScope(scopeValues, newScope) + " - " + newScope + ":" + scopeValues[0]);

                while (checkScope(scopeValues, newScope) ==false){
                    index = index +1;
                    newScope = String.valueOf(index);
                }

                scope = scope + "." + newScope;
            }
        }
        else if(child.getValue().equals("PROC") ){
            int index = 0;
            String newScope = String.valueOf(index);

            while (checkScope(scopeValues, newScope) ==false){
                index = index +1;
                newScope = String.valueOf(index);
            }

            scope = scope + "." + newScope;
        }*/

        //ArrayList<Node> nodeList= new ArrayList<>();
        //ArrayList<Boolean> infolist= new ArrayList<>();

        //List<Object> tableData =  Arrays.asList(child, scope,nodeList,infolist);


        //treeList.add( space + "|" + child.getValue() +" : ID - " + child.getId() + " : SCOPE - " + scope);
        //System.out.println( space + "|" + child.getValue() +" : ID - " + child.getId() + " : SCOPE - " + scope);

        //gets next child


        String scope = "";

        scope = getScope(child, isFinalScan);
        child.setScope(scope);

        if (isFinalScan == true)
            setupSymbolTable(child.getValue(),child);


        if(child.getChildren() != null) {
            int index = 0;
            while (index < child.getChildren().size()) {
                scanChildren(isFinalScan, child.getChildren().get(index), space + "\t", scope); //
                index = index +1;
            }
        }
        else
            return;
    }

    private String getScope(Node child, boolean isFinalScan) {
        if(isFinalScan == false) {

            if (usedValues.isEmpty()) {
                usedValues.add(0);
            }

            if(scopeStack.isEmpty())
                scopeStack.push(0);

            if(scopeParentStack.isEmpty())
                scopeParentStack.push(-1);

            if (child.getValue().equals("ProcDefs")){ //(child.isProcedure()) {//push

                //parentScope = localScope;
                parentScope =localScope;
                scopeParentStack.push(parentScope);
                //if(localScope==0)
                    //parentScope = -1;

                while (usedValues.contains(localScope) == true) {
                    localScope = localScope + 1;
                }
                usedValues.add(localScope);
                scopeStack.push(localScope);

                childrenScopes.add(localScope);

            }
            else if (child.getValue() == "return") {//pop
                //Node head =
                ArrayList<Node> siblings = child.getHead().getChildren();

                boolean isEnd = true;
                int foundIndex = -1;
                for(int i=0; i < siblings.size(); i ++){
                    if(siblings.get(i).getValue().equals("VarDecl")){
                        isEnd = false;
                    }
                    if(siblings.get(i).getValue().equals("}")){
                        foundIndex = i;
                    }
                }

                if(isEnd == true){

                    // localScope - 1;
                    //if(scopeStack.isEmpty()== false) {

                    scopeStack.pop();
                    localScope = (int) scopeStack.peek();
                    //}else{
                    //    localScope = 0;
                    //}

                    childrenScopes.remove(childrenScopes.size() - 1);

                    /*if(scopeParentStack.isEmpty()== false) {

                        parentScope = (int) scopeParentStack.pop(); //(int) scopeParentStack.peek();
                    }else{
                        parentScope = -1;
                    }*/

                    scopeParentStack.pop();
                    parentScope =  (int) scopeParentStack.peek();
                    //parentScope = parentScope - 1;

                    System.out.println("HERE BE DRSGOND");
                }else{
                    terminalScopeNode = siblings.get(foundIndex);
                    System.out.println("HERE BE DRSGOND 2");
                }
            }
            else if( (terminalScopeNode != null) && (child == terminalScopeNode) ){
                System.out.println("HERE BE DRSGOND 3");
                //localScope = localScope - 1;
                //if(scopeStack.isEmpty()== false) {
                    scopeStack.pop();
                    localScope = (int) scopeStack.peek();
                //}else{
                    //localScope = 0;
                //}

                childrenScopes.remove(childrenScopes.size() - 1);

                //scopeParentStack.pop();
                //parentScope =(int) scopeParentStack.pop();//scopeParentStack.peek();
                scopeParentStack.pop();
                parentScope =  (int) scopeParentStack.peek();
                //parentScope = parentScope - 1;

                terminalScopeNode = null;
            }

            String output = String.valueOf(localScope);

            if (parentScope > -1) {
                output = output + "/" + parentScope;
            }
            else {
                output = output + "/-";
            }


            foundScopes.add(output);

            return output;
        }else{
            String newScope = new String();
            for(int i=0; i < foundScopes.size(); i++){
                if(foundScopes.get(i).equals(child.getScope())) {
                    newScope = foundScopes.get(i) + foundChildScopes.get(i);
                    //setupSymbolTable(newScope, child);
                }
            }

            return newScope;
        }
    }






}
