package com.SpitTxt.Project_C;

import java.util.ArrayList;
import java.util.HashMap;

public class TypeChecker {

    private ParseTree myTree;
    private HashMap<String, ArrayList<Node>> mySymbols;

    private ArrayList<String> procedureNames;
    private ArrayList<String> variableNames;
    private ArrayList<String> fieldNames;

    private boolean foundError;
    private ArrayList<String> ErrorList;

    TypeChecker(ParseTree myTree, HashMap<String, ArrayList<Node>> mySymbols,  ArrayList ErrorList){
        this.mySymbols = mySymbols;
        this.myTree = myTree;

        this.foundError = false;
        this.ErrorList = ErrorList;

    }


    public void begin() {
        setNames();

        //type check
        initialiseVariableTypes();
        checkDeclaredVariableTypes();
        checkConditions();
        String[] values = new String[]{"not", "and", "or", "add", "sub", "mult"};
        checkOperators(values);
        checkIfNodeSet(false);

        System.out.println("check done heree");

        //type flow
        setAnchors();
        checkFlow();
        //checkIfNodeSet(true);

        System.out.println("check done heree 2");


    }

    /**************type****************/

    private void initialiseVariableTypes() {
        /*String checks = "N,NN,B T, F ,S, U,M";


        for(int i = 0; i < procedureNames.size(); i++){
            //if(mySymbols.containsKey(procedureNames.get(i)) == true){
            ArrayList<Node> nodeList = mySymbols.get(procedureNames.get(i));

            for(int j =0; j < nodeList.size(); j++) {
                nodeList.get(j).setCastType("U");
            }
            //}
        }*/

        /***************VARIABLES******************/

        for(int i = 0; i < variableNames.size(); i++){
            ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));
            if(nodeList == null)
                continue;
            for(int j =0; j < nodeList.size(); j++) {
                nodeList.get(j).setTypeSymbol(new TypeSymbol());
            }
        }

        for(int i = 0; i < fieldNames.size(); i++){
            ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));
            if(nodeList == null)
                continue;
            for(int j =0; j < nodeList.size(); j++) {
                nodeList.get(j).setTypeSymbol(new TypeSymbol());
            }
        }


        /***************OPERATORS******************/
        //booleans
        if(mySymbols.containsKey("not")){
            ArrayList<Node> nodeList = mySymbols.get("not");

            for(int i =0; i < nodeList.size(); i++){
                nodeList.get(i).setTypeSymbol(new TypeSymbol("B"));
            }
        }
        if(mySymbols.containsKey("and")){
            ArrayList<Node> nodeList = mySymbols.get("and");

            for(int i =0; i < nodeList.size(); i++){
                nodeList.get(i).setTypeSymbol(new TypeSymbol("B"));
            }
        }
        if(mySymbols.containsKey("or")){
            ArrayList<Node> nodeList = mySymbols.get("or");

            for(int i =0; i < nodeList.size(); i++){
                nodeList.get(i).setTypeSymbol(new TypeSymbol("B"));
            }
        }
        //numbers
        if(mySymbols.containsKey("add")){
            ArrayList<Node> nodeList = mySymbols.get("add");

            for(int i =0; i < nodeList.size(); i++){
                nodeList.get(i).setTypeSymbol(new TypeSymbol("N"));
            }
        }
        if(mySymbols.containsKey("sub")){
            ArrayList<Node> nodeList = mySymbols.get("sub");

            for(int i =0; i < nodeList.size(); i++){
                nodeList.get(i).setTypeSymbol(new TypeSymbol("N"));
            }
        }
        if(mySymbols.containsKey("mult")){
            ArrayList<Node> nodeList = mySymbols.get("mult");

            for(int i =0; i < nodeList.size(); i++){
                nodeList.get(i).setTypeSymbol(new TypeSymbol("N"));
            }
        }
        //unknown
        if(mySymbols.containsKey("eq")){
            ArrayList<Node> nodeList = mySymbols.get("eq");

            for(int i =0; i < nodeList.size(); i++){
                nodeList.get(i).setTypeSymbol(new TypeSymbol());
            }
        }
        if(mySymbols.containsKey("larger")){
            ArrayList<Node> nodeList = mySymbols.get("larger");

            for(int i =0; i < nodeList.size(); i++){
                nodeList.get(i).setTypeSymbol(new TypeSymbol());
            }
        }

        /***************CONSTANTS******************/
        if(mySymbols.containsKey("true")){
            ArrayList<Node> nodeList = mySymbols.get("true");

            for(int i =0; i < nodeList.size(); i++){
                nodeList.get(i).setTypeSymbol(new TypeSymbol("T"));
            }
        }

        if(mySymbols.containsKey("false")){
            ArrayList<Node> nodeList = mySymbols.get("false");

            for(int i =0; i < nodeList.size(); i++){
                nodeList.get(i).setTypeSymbol(new TypeSymbol("F"));
            }
        }

        //NOTE: numbers-string already done in analyser,
    }

    private void checkDeclaredVariableTypes() {
        for(int i = 0; i < variableNames.size(); i++){
            ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));
            String type = "";
            if(nodeList == null)
                continue;

            if(nodeList.get(0).isDeclaration()){
                String id = (String.valueOf (Integer.valueOf(nodeList.get(0).getId()) -1));
                Node typeNode = myTree.getNodeByID(id);

                System.out.println(typeNode.getValue() + " MAHUUGA");

                if (typeNode.getValue().equals("bool") ==true) {
                    type = "B";
                }
                else if (typeNode.getValue().equals("num") ==true) {
                    type = "N";
                }
                else if (typeNode.getValue().equals("string") ==true) {
                    type = "S";
                }

                System.out.println(type);

            }

            for(int j =0; j < nodeList.size(); j++) {
                nodeList.get(j).setTypeSymbol(new TypeSymbol(type));
            }
        }



        for(int i = 0; i < fieldNames.size(); i++){
            ArrayList<Node> nodeList = mySymbols.get(fieldNames.get(i));
            if(nodeList == null)
                continue;
            String type = "";
            if(nodeList.get(0).isDeclaration()){
                String id = (String.valueOf (Integer.valueOf(nodeList.get(0).getId()) -1));
                Node typeNode = myTree.getNodeByID(id);

                do {
                    id = (String.valueOf(Integer.valueOf(typeNode.getId()) - 1));
                    typeNode = myTree.getNodeByID(id);

                    if (typeNode.getValue().equals("bool") == true) {
                        type = "B";
                        break;
                    }
                    else if (typeNode.getValue().equals("num") == true) {
                        type = "N";
                        break;
                    }
                    else if (typeNode.getValue().equals("string") == true) {
                        type = "S";
                        break;
                    }
                }
                while (typeNode.getValue().equals("arr") == false);
            }

            for(int j =0; j < nodeList.size(); j++) {
                nodeList.get(j).setTypeSymbol(new TypeSymbol(type));
            }
        }

    }

    private void checkConditions() {

        /*****************Field Check, variable and constant*****************/
        for(int i = 0; i < fieldNames.size(); i++){
            ArrayList<Node> nodeList = mySymbols.get(fieldNames.get(i));
            if(nodeList.equals(null))
                continue;

            for(int j =0; j < nodeList.size(); j++) {

                Node node = nodeList.get(j);
                String id = node.getId();

                if(node.isDeclaration() == false){
                    Node testNode = myTree.getNodeByID(id);

                    while (testNode.getTypeSymbol() == null){ //( (testNode != null) && (testNode.getTypeSymbol() == null || testNode.isVariable()) ){
                        id = String.valueOf(Integer.valueOf(id) +1);
                        testNode = myTree.getNodeByID(id);
                    }


                    if(testNode.equals(null) ==false) {
                        TypeSymbol nodeType = testNode.getTypeSymbol();
                        if (testNode.isVariable()) {// is var
                            if (nodeType.isNumber() == false)
                                printErrorMessage("field " + fieldNames.get(i) + " expects variable of type number instead got type " + nodeType.getType(), nodeList.get(j));
                        }
                        else {
                            if (nodeType.isNotNegative() == false)
                                printErrorMessage("field " + fieldNames.get(i) + " expects variable of type positive number instead got type " + nodeType.getType(), nodeList.get(j));
                        }
                    //}else{
                        //printErrorMessage("field " + fieldNames.get(i) + " using variable with not type/invalid constant", nodeList.get(j));
                    }

                }
            }
        }


        /***********************CHECKING FOR OPERATOR "OUTPUT"*******************/
        if(mySymbols.containsKey("output")){
            ArrayList<Node> nodeList = mySymbols.get("output");

            for(int i =0; i < nodeList.size(); i++){
                Node node = nodeList.get(i);
                String id = node.getId();

                do {
                    id = String.valueOf(Integer.valueOf(id) +1);
                    node = myTree.getNodeByID(id);
                }
                while(node.getTypeSymbol() == null);

                if (node.equals(null) ==false) {

                    TypeSymbol nodeType = node.getTypeSymbol();
                    if (nodeType.isMixed() == false)
                        printErrorMessage("output symbol expects variable of mixed type instead got type " + nodeType.getType(), node);
                }
            }
        }

        /***********************CHECKING FOR OPERATOR "INPUT"*******************/
        if(mySymbols.containsKey("input")){
            ArrayList<Node> nodeList = mySymbols.get("input");

            for(int i =0; i < nodeList.size(); i++){
                Node node = nodeList.get(i);
                String id = node.getId();

                do {
                    id = String.valueOf(Integer.valueOf(id) +1);
                    node = myTree.getNodeByID(id);
                }
                while(node.getTypeSymbol() == null);

                if (node.equals(null) ==false) {
                    TypeSymbol nodeType = node.getTypeSymbol();
                    if (nodeType.isNumber() == false)
                        printErrorMessage("input symbol expects variable of mixed type instead got type " + nodeType.getType(), node);
                }
            }
        }



        /***********************CHECKING FOR OPERATOR "EQ"*******************/
        if(mySymbols.containsKey("eq")){
            ArrayList<Node> nodeList = mySymbols.get("eq");

            for(int i =0; i < nodeList.size(); i++){
                Node node1 = nodeList.get(i);
                String id = node1.getId();
                do {
                    id = String.valueOf(Integer.valueOf(id) +1);
                    node1 = myTree.getNodeByID(id);
                }
                while(node1.getTypeSymbol() == null);
                TypeSymbol nodeType1 = node1.getTypeSymbol();

                if (node1.equals(null) ==false) {
                    Node node2 = node1;
                    id = node2.getId();
                    do {
                        id = String.valueOf(Integer.valueOf(id) + 1);
                        node2 = myTree.getNodeByID(id);
                    }
                    while (node2.getTypeSymbol() == null);
                    TypeSymbol nodeType2 = node2.getTypeSymbol();

                    if (node2.equals(null) ==false) {
                        if (nodeType1.getType().equals(nodeType2.getType()) == true) { //todo: checkSubType()
                            nodeList.get(i).getTypeSymbol().setSymbolType("B");
                        }
                        else {
                            nodeList.get(i).getTypeSymbol().setSymbolType("F");
                        }
                    }
                }

            }
        }


        /***********************CHECKING FOR OPERATOR "LARGER"*******************/
        if(mySymbols.containsKey("larger")){
            ArrayList<Node> nodeList = mySymbols.get("larger");

            for(int i =0; i < nodeList.size(); i++){
                Node node1 = nodeList.get(i);
                String id = node1.getId();
                do {
                    id = String.valueOf(Integer.valueOf(id) +1);
                    node1 = myTree.getNodeByID(id);
                }
                while(node1.getTypeSymbol() == null);
                TypeSymbol nodeType1 = node1.getTypeSymbol();

                if (node1.equals(null) ==false) {
                    Node node2 = node1;
                    id = node2.getId();
                    do {
                        id = String.valueOf(Integer.valueOf(id) + 1);
                        node2 = myTree.getNodeByID(id);
                    }
                    while (node2.getTypeSymbol() == null);

                    if (node2.equals(null) ==false) {
                        TypeSymbol nodeType2 = node2.getTypeSymbol();

                        if (nodeType1.isNumber() && nodeType2.isNumber()) {
                            nodeList.get(i).getTypeSymbol().setSymbolType("B");
                        }
                    }
                }

                /*else{ TODO: not sure rules say not to
                    nodeList.get(i).getTypeSymbol().setSymbolType("F");
                }*/
            }
        }

    }

    private void checkOperators(String[] values) {
        for(int a=0; a < values.length; a++) {
            if (mySymbols.containsKey(values[a])) {
                if(a < 3) { //bool

                    ArrayList<Node> nodeList = mySymbols.get(values[a]); //output

                    for (int i = 0; i < nodeList.size(); i++) {
                        Node node = nodeList.get(i);
                        String id = node.getId();

                        do {
                            id = String.valueOf(Integer.valueOf(id) + 1);
                            node = myTree.getNodeByID(id);
                        }
                        while (node.getTypeSymbol() == null);


                        TypeSymbol nodeType = node.getTypeSymbol();
                        if (nodeType.isBoolean() == false)
                            printErrorMessage(values[a] + " symbol expects variable of boolean type instead got type " + nodeType.getType(), node);
                    }
                }else{ //num
                    ArrayList<Node> nodeList = mySymbols.get(values[a]); //output

                    for (int i = 0; i < nodeList.size(); i++) {
                        Node node = nodeList.get(i);
                        String id = node.getId();

                        do {
                            id = String.valueOf(Integer.valueOf(id) + 1);
                            node = myTree.getNodeByID(id);
                        }
                        while (node.getTypeSymbol() == null);

                        if (node.equals(null) ==false) {
                            TypeSymbol nodeType = node.getTypeSymbol();
                            if (nodeType.isNumber() == false)
                                printErrorMessage(values[a] + " symbol expects variable of number type instead got type " + nodeType.getType(), node);
                        }
                    }
                }
            }
        }
    }

    /*private void checkVariables(){

    }*/

    /**************flow****************/

    private void setAnchors() {

        Node currentNode = mySymbols.get("main").get(0);

        ArrayList<Node> constantsList = mySymbols.get("Constant");
        ArrayList<Node> trueList = mySymbols.get("true");
        ArrayList<Node> falseList = mySymbols.get("false");

        if(trueList != null)
            for (int i = 0; i < trueList.size(); i++)
                constantsList.add(trueList.get(i));
        if(falseList != null)
            for (int i = 0; i < falseList.size(); i++)
                constantsList.add(falseList.get(i));


        if(constantsList != null) {
            String id = currentNode.getId();

            while (currentNode != null) {

                //if(currentNode.getValue().equals("Const") == false){}

                if (constantsList.contains(currentNode)) {
                    String nextID = String.valueOf(Integer.valueOf(id) - 1);
                    Node nextNode = myTree.getNodeByID(nextID);
                    while (nextNode.isVariable() == false) {
                        nextID = String.valueOf(Integer.valueOf(nextID) - 1);
                        nextNode = myTree.getNodeByID(nextID);
                    }

                    if( nextNode.equals(null) ==false && nextNode.getTypeSymbol().checkEquals(currentNode.getTypeSymbol())) {
                        nextNode.setVerified(true);
                        ArrayList<Node> nodeList = mySymbols.get(nextNode.getValue());
                        verifyNodes(nodeList, nextNode);
                    }else{
                        printErrorMessage("Variable found to not have incompatible types to " + currentNode.getValue(), nextNode);
                    }

                }
                else if (currentNode.isProcedure()) {

                    Node declaration = mySymbols.get(currentNode.getValue()).get(0);
                    String newIDDec = String.valueOf(Integer.valueOf(declaration.getId()) +1);
                    declaration = myTree.getNodeByID(newIDDec);

                    setAnchorsChilden(declaration);
                }

                id = String.valueOf((Integer.valueOf(id) + 1));
                currentNode = myTree.getNodeByID(id);
            }
        }
    }

    private void setAnchorsChilden(Node declaration) {
        Node currentNode = declaration;

        ArrayList<Node> constantsList = mySymbols.get("Constant");
        ArrayList<Node> trueList = mySymbols.get("true");
        ArrayList<Node> falseList = mySymbols.get("false");

        if(trueList != null)
            for (int i = 0; i < trueList.size(); i++)
                constantsList.add(trueList.get(i));
        if(falseList != null)
            for (int i = 0; i < falseList.size(); i++)
                constantsList.add(falseList.get(i));

        if(constantsList != null) {

            String id = currentNode.getId();

            while (currentNode.getValue().equals("return")) {
            //while (currentNode != null) {

                if (constantsList.contains(currentNode)) {
                    String nextID = String.valueOf((Integer.valueOf(id) - 1));
                    Node nextNode = myTree.getNodeByID(nextID);
                    while (nextNode.isVariable() == false) {
                        nextID = String.valueOf((Integer.valueOf(nextID) - 1));
                        nextNode = myTree.getNodeByID(nextID);
                    }

                    if(nextNode.equals(null) ==false && nextNode.getTypeSymbol().checkEquals(currentNode.getTypeSymbol())) {
                        nextNode.setVerified(true);
                        ArrayList<Node> nodeList = mySymbols.get(nextNode.getValue());
                        verifyNodes(nodeList, nextNode);
                    }else{
                        printErrorMessage("Variable found to not have incompatible types to " + currentNode.getValue(), nextNode);
                    }

                }
                else if (currentNode.isProcedure()) {

                    Node newDeclaration = mySymbols.get(currentNode.getValue()).get(0);
                    String newIDDec = String.valueOf(Integer.valueOf(newDeclaration.getId()) +1);
                    newDeclaration = myTree.getNodeByID(newIDDec);
                    setAnchorsChilden(newDeclaration);
                }

                id = String.valueOf((Integer.valueOf(id) + 1));
                currentNode = myTree.getNodeByID(id);
            }
        }

    }

    private void verifyNodes(ArrayList<Node> nodeList, Node nextNode) {

        //todo, check for while, for, if statements here

        Node currentNode = nextNode;
        String id = currentNode.getId();

        while (currentNode != null) {

            //if(currentNode.getValue().equals("Const") == false){}

            if (nodeList.contains(currentNode)) {

                int nodeID = Integer.parseInt(currentNode.getId());
                Node testNode = myTree.getNodeByID(String.valueOf(nodeID + 1));

                int maxScan = 0;
                while (testNode!= null && maxScan < 10) {
                    if (testNode.getValue().equals(":=")) {
                        return;
                    }

                    maxScan = maxScan +1;
                    testNode = myTree.getNodeByID(String.valueOf(nodeID + (maxScan +1)));
                }


                testNode = myTree.getNodeByID(String.valueOf(nodeID - 1));

                if (testNode.getValue().equals(":=")) {
                    currentNode.setVerified(true);
                    continue;
                }

                //todo find/add more conditions

                maxScan = 0;
                while (testNode!= null && maxScan < 10) { //scan backawards

                    nodeID = nodeID - 1;
                    testNode = myTree.getNodeByID(String.valueOf(nodeID - 1));

                    if (testNode.getValue().equals(":=")) {
                        currentNode.setVerified(true);
                        break;
                    }

                    if(currentNode.getTypeSymbol().isBoolean()){
                        if (testNode.getValue().equals("if")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("while")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("until")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("not")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("and")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("or")) {
                            currentNode.setVerified(true);
                            break;
                        }

                    }else if(currentNode.getTypeSymbol().isNumber()){
                        if (testNode.getValue().equals("eq")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("larger")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("add")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("sub")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("mult")) {
                            currentNode.setVerified(true);
                            break;
                        }
                    }

                    maxScan = maxScan +1;
                }
            }
            else if (currentNode.isProcedure()) {
                //scan until end or new const found
                Node newDeclaration = mySymbols.get(currentNode.getValue()).get(0);
                String newIDDec = String.valueOf(Integer.valueOf(newDeclaration.getId()) +1);
                newDeclaration = myTree.getNodeByID(newIDDec);
                if (verifyNodesChildren(nodeList, newDeclaration) == false){
                    return;
                }
            }

            id = String.valueOf((Integer.valueOf(id) + 1));
            currentNode = myTree.getNodeByID(id);
        }
    }

    private boolean verifyNodesChildren(ArrayList<Node> nodeList, Node nextNode)  {

        //todo, check for while, for, if statements here
        Node currentNode = nextNode;

        String id = currentNode.getId();

        while (currentNode != null) {

            //if(currentNode.getValue().equals("Const") == false){}

            if (nodeList.contains(currentNode)) {

                int nodeID = Integer.parseInt(currentNode.getId());
                Node testNode = myTree.getNodeByID(String.valueOf(nodeID + 1));

                int maxScan = 0;
                while (testNode!= null && maxScan < 10) {
                    if (testNode.getValue().equals(":=")) {
                        return false;
                    }

                    maxScan = maxScan +1;
                    testNode = myTree.getNodeByID(String.valueOf(nodeID + (maxScan +1)));
                }


                testNode = myTree.getNodeByID(String.valueOf(nodeID - 1));

                if (testNode.getValue().equals(":=")) {
                    currentNode.setVerified(true);
                    continue;
                }

                //todo find/add more conditions

                maxScan = 0;
                while (testNode!= null && maxScan < 10) { //scan backawards

                    nodeID = nodeID - 1;
                    testNode = myTree.getNodeByID(String.valueOf(nodeID - 1));

                    if (testNode.getValue().equals(":=")) {
                        currentNode.setVerified(true);
                        break;
                    }

                    if(currentNode.getTypeSymbol().isBoolean()){
                        if (testNode.getValue().equals("if")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("while")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("until")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("not")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("and")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("or")) {
                            currentNode.setVerified(true);
                            break;
                        }

                    }else if(currentNode.getTypeSymbol().isNumber()){
                        if (testNode.getValue().equals("eq")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("larger")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("add")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("sub")) {
                            currentNode.setVerified(true);
                            break;
                        }

                        if (testNode.getValue().equals("mult")) {
                            currentNode.setVerified(true);
                            break;
                        }
                    }

                    maxScan = maxScan +1;
                }
            }
            else if (currentNode.isProcedure()) {
                //scan until end or new const found
                Node newDeclaration = mySymbols.get(currentNode.getValue()).get(0);

                String newIDDec = String.valueOf(Integer.valueOf(newDeclaration.getId()) +1);
                newDeclaration = myTree.getNodeByID(newIDDec);
                if (verifyNodesChildren(nodeList, newDeclaration) == false){
                    return false;
                }


            }

            id = String.valueOf((Integer.valueOf(id) + 1));
            currentNode = myTree.getNodeByID(id);
        }


        /*for(int i=0; i < nodeList.size();i++ ){
            Node node = nodeList.get(i);
            if(node.getScope().equals(nextNode.getScope()) == true){
                int nodeID = Integer.parseInt(node.getId());
                Node testNode = myTree.getNodeByID(String.valueOf(nodeID-1));

                if(testNode.getValue().equals(":=") ){
                    node.setVerified(true);
                    continue;
                }

                //todo find/add more conditions
            }
        }*/

        return true;

    }

    private void checkFlow() {
        Node currentNode = mySymbols.get("main").get(0);
        String id = currentNode.getId();

        while (currentNode != null && foundError == false) {

            //if(currentNode.getValue().equals("Const") == false){}

            /*if (currentNode.isField() && currentNode.isVerified() == false ) {
                String nextID = String.valueOf((Integer.valueOf(id) + 1));
                Node nextNode = myTree.getNodeByID(nextID);

                //skip until on the LHS
                /*while (nextNode.getValue().equals(":=") == false) { // until ";"
                    nextID = String.valueOf((Integer.valueOf(nextID) + 1));
                    nextNode = myTree.getNodeByID(nextID);
                }
                nextID = String.valueOf((Integer.valueOf(nextID) + 1));
                nextNode = myTree.getNodeByID(nextID);*

                while (nextNode.getValue().equals(";")) { // until ";"
                    if(nextNode.isVariable() == true){
                        if(nextNode.isVerified() == false){
                            printErrorMessage("Variable found to not have a value set", nextNode);
                            return;
                        }
                    }
                    nextID = String.valueOf((Integer.valueOf(nextID) + 1));
                    nextNode = myTree.getNodeByID(nextID);
                }

                currentNode.setVerified(true);
                ArrayList<Node> nodeList = mySymbols.get(currentNode.getValue());
                verifyNodes(nodeList, currentNode);
            }
            else*/
            if (currentNode.isVariable() && currentNode.isVerified() == false ) {
                String nextID = String.valueOf((Integer.valueOf(id) + 1));
                Node nextNode = myTree.getNodeByID(nextID);

                while (nextNode.getValue().equals(";") ==false) { // until ";"
                    boolean foundLHS = false;

                    if(nextNode.isVariable() == true){
                        if( nextNode.getTypeSymbol().checkEquals(currentNode.getTypeSymbol()) == false && foundLHS == true){
                            printErrorMessage("Variable found to not have incompatible types to " + currentNode.getValue(), nextNode);
                        }

                        if(nextNode.isVerified() == false){
                            printErrorMessage("Variable found to not have a value set", nextNode);
                            return;
                        }
                    }

                    if(nextNode.getValue().equals(":="))
                        foundLHS = true;
                    nextID = String.valueOf((Integer.valueOf(nextID) + 1));
                    nextNode = myTree.getNodeByID(nextID);
                }

                currentNode.setVerified(true);
                ArrayList<Node> nodeList = mySymbols.get(currentNode.getValue());
                verifyNodes(nodeList, currentNode);
            }
            else if (currentNode.isProcedure()) {

                if(mySymbols.containsKey(currentNode.getValue())) {
                    Node declaration = mySymbols.get(currentNode.getValue()).get(0);
                    String newIDDec = String.valueOf(Integer.valueOf(declaration.getId()) +1);
                    declaration = myTree.getNodeByID(newIDDec);
                    checkFlowChildren(declaration);
                }else if(mySymbols.containsKey(currentNode.getRegulatedName())){
                    Node declaration = mySymbols.get(currentNode.getRegulatedName()).get(0);
                    String newIDDec = String.valueOf(Integer.valueOf(declaration.getId()) +1);
                    declaration = myTree.getNodeByID(newIDDec);
                    checkFlowChildren(declaration);
                }
            }

            id = String.valueOf((Integer.valueOf(id) + 1));
            currentNode = myTree.getNodeByID(id);
        }

    }

    private void checkFlowChildren(Node startNode) {
        Node currentNode = startNode;
        String id = currentNode.getId();

        while (currentNode.getValue().equals("return")) {

            //if(currentNode.getValue().equals("Const") == false){}

            if (currentNode.isVariable() && currentNode.isVerified() == false ) {
                String nextID = String.valueOf((Integer.valueOf(id) + 1));
                Node nextNode = myTree.getNodeByID(nextID);

                while (nextNode.getValue().equals(";") ==false) { // until ";"
                    boolean foundLHS = false;

                    if(nextNode.isVariable() == true){

                        if( nextNode.getTypeSymbol().checkEquals(currentNode.getTypeSymbol()) == false && foundLHS == true){
                            printErrorMessage("Variable found to not have incompatible types to " + currentNode.getValue(), nextNode);
                        }

                        if(nextNode.isVerified() == false){
                            printErrorMessage("Variable found to not have a value set", nextNode);
                            return;
                        }

                    }

                    if(nextNode.getValue().equals(":="))
                        foundLHS = true;
                    nextID = String.valueOf((Integer.valueOf(nextID) + 1));
                    nextNode = myTree.getNodeByID(nextID);
                }

                currentNode.setVerified(true);
                ArrayList<Node> nodeList = mySymbols.get(currentNode.getValue());
                verifyNodes(nodeList, currentNode);
            }
            else if (currentNode.isProcedure()) {

                if(mySymbols.containsKey(currentNode.getValue())) {
                    Node declaration = mySymbols.get(currentNode.getValue()).get(0);
                    String newIDDec = String.valueOf(Integer.valueOf(declaration.getId()) +1);
                    declaration = myTree.getNodeByID(newIDDec);
                    checkFlowChildren(declaration);
                }else if(mySymbols.containsKey(currentNode.getRegulatedName())){
                    Node declaration = mySymbols.get(currentNode.getRegulatedName()).get(0);
                    String newIDDec = String.valueOf(Integer.valueOf(declaration.getId()) +1);
                    declaration = myTree.getNodeByID(newIDDec);
                    checkFlowChildren(declaration);
                }
            }

            id = String.valueOf((Integer.valueOf(id) + 1));
            currentNode = myTree.getNodeByID(id);
        }
    }

    /*****************HELPER*********************/

    private void checkIfNodeSet(boolean isFinal) {
        if(isFinal == false){
            for(int i = 0; i < variableNames.size(); i++){
                ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));
                if(nodeList == null)
                    continue;

                for(int j =0; j < nodeList.size(); j++) {
                    if (nodeList.get(j).getTypeSymbol().isUnknown()){
                        printErrorMessage("Variable type is unknown" ,nodeList.get(j));
                    }
                }
            }

            for(int i = 0; i < fieldNames.size(); i++){
                ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));
                if(nodeList == null)
                    continue;

                for(int j =0; j < nodeList.size(); j++) {
                    if (nodeList.get(j).getTypeSymbol().isUnknown()){
                        printErrorMessage("Variable field type is unknown" ,nodeList.get(j));
                    }
                }
            }


        }else{
            for(int i = 0; i < variableNames.size(); i++){
                ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));
                if(nodeList == null)
                    continue;

                for(int j =0; j < nodeList.size(); j++) {
                    if (nodeList.get(j).isVerified() == false){
                        printErrorMessage("Variable has no value set" ,nodeList.get(j));
                    }
                }
            }

            for(int i = 0; i < fieldNames.size(); i++){
                ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));
                if(nodeList == null)
                    continue;

                for(int j =0; j < nodeList.size(); j++) {
                    if (nodeList.get(j).isVerified() == false){
                        printErrorMessage("Variable field has no value set" ,nodeList.get(j));
                    }
                }
            }
        }
    }

    private void setNames() {
        procedureNames = new ArrayList<>();
        variableNames = new ArrayList<>();
        fieldNames = new ArrayList<>();


        for (String value: mySymbols.keySet()) {

            ArrayList<Node> nodeList = mySymbols.get(value);

            //if(nodeList != null) {


       // for (int i = 0; i < mySymbols.size(); i++) {
            //ArrayList<Node> nodeList = mySymbols.get(String.valueOf(i));

            //checks rest of the nodes
            if ( nodeList == null)
                continue;

            for(int j=0; j< nodeList.size(); j++){
                Node node = nodeList.get(j);

                if( node.isProcedure() == true){
                    procedureNames.add(node.getValue());
                }
                else if(node.isField() == true){
                    fieldNames.add(node.getValue());
                }
                else if (node.isVariable() == true){
                    variableNames.add(node.getValue());
                }
            }
        }
    }



    private void printErrorMessage(String s,Node node ) {
        if(foundError == true)
            return;

        foundError = true;
        //foundError = s;
        String finalError = "Semantic error (Checker): " + "\n" + s +"\n'" + node.getValue() +"' @line " +node.getLineCount() + " -" +  node.getTypeSymbol().getSymbolType();
        this.ErrorList.add(finalError );
        //myTree.addTreeNode(new Node(s,true));
    }

    public ArrayList<String> getErrorList() {
        return ErrorList;
    }

    public void setErrorList(ArrayList<String> errorList) {
        ErrorList = errorList;
    }
}


