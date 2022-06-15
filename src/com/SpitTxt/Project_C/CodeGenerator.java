package com.SpitTxt.Project_C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CodeGenerator {

    private ParseTree myTree;
    private HashMap<String, ArrayList<Node>> mySymbols;

    private boolean foundError;
    private ArrayList<String> ErrorList;
    private ArrayList<String> generatedCode;

    private String procedureLine;

    private ArrayList<Node> procedureCalls;

    private ArrayList<ArrayList<String>> codeBlocks;

    private ArrayList<Integer> blockLine;

    private List<String> keyWords =  Arrays.asList(
            "mult",
            "sub",
            "add",
            "else",
            "call",
            "larger",
            "eq",
            "and",
            "or",
            "not",
            "output"
    );

    ArrayList<String> usedVariableNames;

    CodeGenerator(ParseTree myTree, HashMap<String, ArrayList<Node>> mySymbols, ArrayList ErrorList){
        this.mySymbols = mySymbols;
        this.myTree = myTree;

        this.foundError = false;
        this.ErrorList = ErrorList;
        this.generatedCode = new ArrayList<>();
        this.procedureLine = "";

        this.procedureCalls = new ArrayList<>();
        this.codeBlocks = new ArrayList<>();
        this.blockLine = new ArrayList<>();
        this.usedVariableNames = new ArrayList<>();
    }

    public void begin() {
        checkFlow(null);


        while(procedureCalls.isEmpty() == false){
            checkFlow(procedureCalls.get(0));
        }


        for(int i =0; i < codeBlocks.size(); i++){
            String line = "";
            for(int j =0; j < codeBlocks.get(i).size(); j++){
                line = line + " " + codeBlocks.get(i).get(j);
            }
            generatedCode.add(line);
        }

        boolean Changed = false;
        do {
            Changed = false;

            for (int i = 0; i < generatedCode.size(); i++) {
                String line = generatedCode.get(i);
                if (line.contains("input")) {
                    Changed = true;

                    String[] lineValues = line.split(" ");
                    String newLineOne = lineValues[0];
                    String newLineTwo = new String();

                    for (int j = 0; j < lineValues.length - 1; j++) {
                        if (lineValues[j].equals("=")) {
                            newLineOne = newLineTwo + " " + lineValues[j];
                            j = j + 1;
                            newLineTwo = lineValues[j];
                            continue;
                        }

                        if (newLineTwo.isEmpty() == true) {
                            newLineOne = newLineOne + " " + lineValues[j];
                        }
                        else {
                            newLineTwo = newLineTwo + " " + lineValues[j];
                        }
                    }

                    newLineOne = newLineOne + " " + lineValues[lineValues.length - 1];
                    newLineTwo = newLineTwo + " " + lineValues[lineValues.length - 1];


                    ArrayList<String> temp = new ArrayList<>();
                    for (int k =0; k < generatedCode.size();k++){
                        if(k != i) {
                            temp.add(generatedCode.get(k));
                        }else{
                            temp.add(newLineOne);
                            temp.add(newLineTwo);
                        }
                    }

                    generatedCode = temp;
                    break;
                }
            }
        }while(Changed == true);
    }


    private void checkFlow(Node procedure) {
        Node currentNode = mySymbols.get("main").get(0);
        String id = currentNode.getId();
        ArrayList<Node> nodes = new ArrayList();
        boolean isBranchEnd = true;
        boolean isCodeSnippet = false;


        String endNode = "";
        if(procedure == null){
            endNode = "halt";

            while (currentNode.getValue().equals("{") == false) {
                id = String.valueOf((Integer.valueOf(id) + 1));
                currentNode = myTree.getNodeByID(id);
            }
            id = String.valueOf((Integer.valueOf(id) + 1));
            currentNode = myTree.getNodeByID(id);
        }else {
            endNode = "return";
            currentNode = procedure;
            id = currentNode.getId();

            if(blockLine.isEmpty() == true){
                int newLine = 10;
                blockLine.add(newLine);
            }
            else{
                int newLine = blockLine.get(blockLine.size()-1) + 10;
                blockLine.add(newLine);
            }

            ArrayList<String> procBlock = new ArrayList<>();
            procBlock.add(String.valueOf(blockLine.get(blockLine.size()-1)));
            procBlock.add("[" + procedure.getValue() + "]");
            codeBlocks.add(procBlock);

            while (currentNode.getValue().equals("{") == false) {
                id = String.valueOf((Integer.valueOf(id) + 1));
                currentNode = myTree.getNodeByID(id);
            }
            id = String.valueOf((Integer.valueOf(id) + 1));
            currentNode = myTree.getNodeByID(id);
        }

        int stackNumber = 0;

        while (currentNode.getValue().equals(endNode) == false ) {



            if (isCodeSnippet == true) {
                nodes.add(currentNode);

            }

            if(currentNode.getValue().equals("Algorithm")){
                isCodeSnippet = true;

            }

            if(currentNode.getValue().equals("{")){
                //isBranchEnd = false;
                stackNumber = stackNumber +1;
            }else if(currentNode.getValue().equals("}")){
                //isBranchEnd = true;
                stackNumber = stackNumber - 1;
            }


            if(currentNode.getValue().equals(";") && stackNumber == 0){
                System.out.println("lulululululu - " );

                if(procedure == null)
                    checkNodeList(nodes, false);
                else
                    checkNodeList(nodes, true);
                nodes = new ArrayList();
                isCodeSnippet = false;
            }

            /*if (currentNode.isProcedure()) {

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
            }*/

            //checkNode(currentNode, false);

            id = String.valueOf((Integer.valueOf(id) + 1));
            currentNode = myTree.getNodeByID(id);
        }

        if(blockLine.isEmpty() == true){
            int newLine = 10;
            blockLine.add(newLine);
        }
        else{
            int newLine = blockLine.get(blockLine.size()-1) + 10;
            blockLine.add(newLine);
        }

        if(endNode.equals("halt")){
            ArrayList<String> procBlock = new ArrayList<>();
            procBlock.add(String.valueOf(blockLine.get(blockLine.size()-1)));
            procBlock.add("STOP");
            codeBlocks.add(procBlock);
        }else if(endNode.equals("return")){
            ArrayList<String> procBlock = new ArrayList<>();
            procBlock.add(String.valueOf(blockLine.get(blockLine.size()-1)));
            procBlock.add("RETURN");
            codeBlocks.add(procBlock);

            procedureCalls.remove(procedure);
        }

    }

    private void checkFlowChildren(Node startNode,Node endNode) {
        Node currentNode = startNode;
        String id = currentNode.getId();



        /*if(currentNode.getValue().equals("{")){
            isBranchEnd = false;
        }else if(currentNode.getValue().equals("}")){
            isBranchEnd = true;
        }

        if(currentNode.getValue().equals(";") && isBranchEnd == true){
            checkNodeList(nodes, false);
            nodes = new ArrayList();
        }*/


        ArrayList<Node> nodes = new ArrayList();
        boolean isBranchEnd = true;
        boolean isCodeSnippet = false;

        int stackNumber = 0;
        while (currentNode != endNode) {

            if (isCodeSnippet == true) {
                nodes.add(currentNode);
            }

            if(currentNode.getValue().equals("Algorithm")){
                isCodeSnippet = true;
            }

            if(currentNode.getValue().equals("{")){
                //isBranchEnd = false;
                stackNumber = stackNumber +1;
            }else if(currentNode.getValue().equals("}")){
                //isBranchEnd = true;
                stackNumber = stackNumber - 1;
            }

            if(currentNode.getValue().equals(";") && stackNumber == 0){
                checkNodeList(nodes, false);
                nodes = new ArrayList();
                isCodeSnippet = false;
            }


            id = String.valueOf((Integer.valueOf(id) + 1));
            currentNode = myTree.getNodeByID(id);
        }
    }

    private void checkNodeList(ArrayList<Node> nodes, boolean isProc) {

        System.out.println("MAWAHAHA HAA");

        int newLine = -1;

        ArrayList<String> block = new ArrayList<>();
        if(blockLine.isEmpty() == true){
            newLine = 10;
            blockLine.add(newLine);
        }
        else{
            newLine = blockLine.get(blockLine.size()-1) + 10;
            blockLine.add(newLine);
        }

        String codeType = new String();

        if(nodes.get(0).getValue().equals("Branch")){
            codeType = "if_con";
        }
        else if(nodes.get(0).getValue().equals("Branch")){
            codeType = "if_con";
        }

        if(codeType.isEmpty()) {
            for (int i = 1; i < nodes.size(); i++) {
                if (nodes.get(i).getValue().equals(":=")) {
                    codeType = "assign_con";
                    break;
                }

                if (nodes.get(i).getValue().equals("if")) { //do;  if;  while
                    codeType = "if_con";
                    break;
                }

                if (nodes.get(i).getValue().equals("do") || nodes.get(i).getValue().equals("while")) {
                    codeType = "loop_con";
                    break;
                }

                if (nodes.get(i).getValue().equals("call")) {
                    codeType = "proc_con";
                    break;
                }
            }
        }

        block.add(String.valueOf(newLine));
        if(codeType.equals("assign_con")) {
            ArrayList<Node> lhs = new ArrayList<>();
            lhs.add(null); //conditions very stupid way
            ArrayList<Node> rhs = new ArrayList<>();
            rhs.add(null);
            rhs.add(null);
            Node equal = null;
            boolean flipped = false;

            for (int i = 0; i < nodes.size(); i++) {
                if(nodes.get(i).getValue().equals(":=")){
                    equal = nodes.get(i);
                    flipped = true;
                    continue;
                }

                if(flipped==false){
                    lhs.add(nodes.get(i));
                }else{
                    rhs.add(nodes.get(i));
                }
            }

            ArrayList<String> suggest = translateNode(lhs.get(0), lhs, codeType);
            for (int j = 0; j < suggest.size(); j++) {
                block.add(suggest.get(j));
            }

            //block.add(translateNode(equal, null, codeType).get(0)); //adding equal sign

            suggest = translateNode(rhs.get(0), rhs, codeType);
            for (int j = 0; j < suggest.size(); j++) {
                block.add(suggest.get(j));
            }
        }
        else if(codeType.equals("if_con")){
            Node startNode = null;
            Node endNode = null;
            boolean isInnerNode = false;

            ArrayList<Node> ifList = new ArrayList<>();
            ArrayList<Node> elseList = new ArrayList<>();

            for (int i = 0; i < nodes.size(); i++) {

                if(nodes.get(i).getValue().equals("{")) {
                    if(isInnerNode == false)
                        startNode = nodes.get(i);
                    isInnerNode = true;
                }


                if(nodes.get(i).getValue().equals("}")) {
                    endNode = nodes.get(i);
                }

                if(isInnerNode == false)
                    ifList.add(nodes.get(i));


                if(nodes.get(i).getValue().equals("else")) {
                    elseList.add(nodes.get(i));
                    break;
                }
            }

            //ifList.add(nodes.get(nodes.size()-1)); //add + ";" symbol

            ArrayList<String> suggest = translateNode(ifList.get(0), ifList, codeType);
            for (int j = 0; j < suggest.size(); j++) {
                block.add(suggest.get(j));
            }


            checkFlowChildren(startNode,endNode);

            /************************ELSE BRANCH*************************/

            startNode = null;
            endNode = null;
            isInnerNode = false;

            if(elseList.isEmpty() ==false){

                int index =0 ;
                for (index = 0; index < nodes.size(); index++) {
                    if(nodes.get(index).getValue().equals("else"))
                        break;
                }


                for (; index < nodes.size(); index++) {
                    if(nodes.get(index).getValue().equals("{")) {
                        if(isInnerNode == false)
                            startNode = nodes.get(index);
                        isInnerNode = true;
                    }

                    if(nodes.get(index).getValue().equals("}")) {
                        endNode = nodes.get(index);
                    }
                }

                for(int i =0; i < ifList.size(); i++)
                    elseList.add(ifList.get(i));


                suggest = translateNode(elseList.get(0), elseList, codeType);
                for (int j = 0; j < suggest.size(); j++) {
                    block.add(suggest.get(j));
                }

                checkFlowChildren(startNode,endNode);
            }

        }else if(codeType.equals("loop_con")){
            Node startNode = null;
            Node endNode = null;
            boolean isInnerNode = false;

            block.add("[LOOP_" +  String.valueOf(newLine) + "]");

            ArrayList<Node> loopList = new ArrayList<>();

            for (int i = 0; i < nodes.size(); i++) {
                if(nodes.get(i).getValue().equals("{")) {
                    if(isInnerNode == false)
                        startNode = nodes.get(i);
                    isInnerNode = true;
                }

                if(nodes.get(i).getValue().equals("}")) {
                    endNode = nodes.get(i);
                }

                if(isInnerNode == false)
                    loopList.add(nodes.get(i));
            }

            for (int i = 0; i < nodes.size(); i++) {

                if (nodes.get(i) ==endNode) {
                    isInnerNode = false;
                    continue;
                }

                if(isInnerNode == false)
                    loopList.add(nodes.get(i));
            }


            ArrayList<String> suggest = translateNode(loopList.get(0), loopList, codeType);
            for (int j = 0; j < suggest.size(); j++) {
                block.add(suggest.get(j));
            }

            checkFlowChildren(startNode,endNode);

            block.add("GOTO [LOOP_" +  String.valueOf(newLine) + "]");
        }else if(codeType.equals("proc_con")){
            for (int i = 0; i < nodes.size(); i++) {
                if(nodes.get(i).isProcedure()) {
                    ArrayList<Node> declareList = mySymbols.get(nodes.get(i).getValue());
                    Node declaceNode = declareList.get(0);
                    procedureCalls.add(declaceNode); // newLine

                    block.add("GOSUB [" + nodes.get(i).getValue() + "]");
                    break;
                    //translateNode(nodes.get(i), nodes, codeType);
                }
            }
        }

        codeBlocks.add(block);
    }

    private ArrayList<String> translateNode(Node currentNode, ArrayList<Node> nodeList, String codeType) {

        boolean isRhs = false;
        if( nodeList.get(1) == null){
            nodeList.remove(0);
            nodeList.remove(0);
            isRhs = true;
        }else if(nodeList.get(0) == null){
            nodeList.remove(0);
            isRhs = false;
        }
        currentNode = nodeList.get(0);
        String value = currentNode.getValue();
        //String regulatedValue = currentNode.getRegulatedName();
        String replacement = value;
        ArrayList<String> output =new ArrayList<>();

        /* mult
            – sub
            + add
            “ ”
            () arithmetic
            GOTO n else
            GOSUB p call

            <, > larger
            = eq
            LET .=.
            IF
            INPUT =  input
            END halt
            STOP return*/
        //do;  if;  while
        /* mult
            – sub
            + add
            “ ”
            () arithmetic
            GOTO n else
            GOSUB p call

            <, > larger
            = eq
            LET .=.
            IF
            INPUT =  input
            END halt
            STOP return*/
        /*"assign_con";
        "if_con";
        "loop_con";
        "proc_con";*/


        if(codeType.equals("assign_con")){




            if(isRhs ==false){
                String id = String.valueOf(Integer.valueOf(currentNode.getId()) +1);
                while(currentNode.isConstant() == false && currentNode.isVariable() == false && keyWords.contains(currentNode.getValue()) ==false){
                    currentNode = myTree.getNodeByID(id);
                    id = String.valueOf(Integer.valueOf(id) +1);
                }
                value = currentNode.getValue();

                if (value.equals("output")) {
                    replacement = "PRINT";
                    output.add(replacement);
                    return output;
                }
                else if(currentNode.isField()){
                    replacement = currentNode.getValue();

                    if(usedVariableNames.contains(replacement) == false) {
                        usedVariableNames.add(replacement);
                        output.add("LET");
                    }

                    if(currentNode.getTypeSymbol().isString())
                        replacement = replacement + "$";
                    output.add(replacement);
                    output.add("(");
                    for(int i =0 ; i < nodeList.size();i++){
                        if(nodeList.get(i).isConstant() || nodeList.get(i).isVariable()) {
                            output.add(nodeList.get(i).getValue());
                            break;
                        }
                    }
                    output.add(")");
                    output.add("=");
                    return output;
                }
                else if (currentNode.isVariable()){
                    replacement = currentNode.getValue();

                    if(usedVariableNames.contains(replacement) == false) {
                        usedVariableNames.add(replacement);
                        output.add("LET");
                    }

                    if(currentNode.getTypeSymbol().isString())
                        replacement = replacement + "$";
                    output.add(replacement);
                    output.add("=");
                    return output;
                }


            }else{
                String id = String.valueOf(Integer.valueOf(currentNode.getId()) +1);
                while(currentNode.isConstant() == false && currentNode.isVariable() == false && keyWords.contains(currentNode.getValue()) ==false){
                    currentNode = myTree.getNodeByID(id);
                    id = String.valueOf(Integer.valueOf(id) +1);
                }
                value = currentNode.getValue();



                /******EXPR********/
                if (value.equals("mult")) {
                    replacement = "*";
                    ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList,true, false);
                    for(int i =0; i < temp.size(); i++){
                        output.add(temp.get(i));
                    }
                    //translateNodeChildren(lhsExpr.get(0),lhsExpr,codeType) + "*" + translateNodeChildren(rhsExpr.get(0),rhsExpr,codeType);
                }
                else if (value.equals("sub")) {
                    replacement = "-";
                    ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList,true, false);
                    for(int i =0; i < temp.size(); i++){
                        output.add(temp.get(i));
                    }
                }
                else if (value.equals("add")) {
                    replacement = "+";
                    ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                    for(int i =0; i < temp.size(); i++){
                        output.add(temp.get(i));
                    }
                }

                else if (value.equals("input")) {
                    replacement = "INPUT";
                    output.add(replacement);

                    for(int i =0 ; i < nodeList.size();i++){
                        if(nodeList.get(i).isVariable()) {
                            output.add(nodeList.get(i).getValue());
                            break;
                        }
                    }
                    return output;
                }
                else if(currentNode.isConstant() || currentNode.isVariable() ){
                    replacement = currentNode.getValue();
                    if(currentNode.isVariable() && currentNode.getTypeSymbol().isString())
                        replacement = replacement + "$";
                    output.add(replacement);
                    return output;
                }
            }
        }
        else if(codeType.equals("if_con")){

            boolean isBranch = false;
            if (value.equals("if")) {
                replacement = "if";
                output.add(replacement);

            }
            else if (value.equals("else")) {
                replacement = "if";
                output.add(replacement);
                output.add("not");
                output.add("(");
                isBranch = true;
            }

            String id = String.valueOf(Integer.valueOf(currentNode.getId()) +1);
            while(currentNode.isConstant() == false && currentNode.isVariable() == false && keyWords.contains(currentNode.getValue()) ==false){
                currentNode = myTree.getNodeByID(id);
                id = String.valueOf(Integer.valueOf(id) +1);
            }
            value = currentNode.getValue();

            /******EXPR********/
            //boolean
            if (value.equals("larger")) {
                replacement = ">";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("eq")) {
                replacement = "=";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if(value.equals("and")){
                replacement = "AND";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("or"))
            {
                replacement = "OR";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("not"))
            {
                replacement = "NOT";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, false, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }

            //arithmetic
            else if (value.equals("mult")) {
                replacement = "*";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
                //translateNodeChildren(lhsExpr.get(0),lhsExpr,codeType) + "*" + translateNodeChildren(rhsExpr.get(0),rhsExpr,codeType);
            }
            else if (value.equals("sub")) {
                replacement = "-";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("add")) {
                replacement = "+";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if(currentNode.isConstant() || currentNode.isVariable() ){
                replacement = currentNode.getValue();
                if(currentNode.isVariable() && currentNode.getTypeSymbol().isString())
                    replacement = replacement + "$";
                output.add(replacement);
                return output;
            }


            /******EXPR********/
            if(isBranch == true)
                output.add(")");
        }
        else if(codeType.equals("loop_con")){
            boolean isBranch = false;

            replacement = "if";
            output.add(replacement);


            String id = String.valueOf(Integer.valueOf(currentNode.getId()) +1);
            while(currentNode.isConstant() == false && currentNode.isVariable() == false && keyWords.contains(currentNode.getValue()) ==false){
                currentNode = myTree.getNodeByID(id);
                id = String.valueOf(Integer.valueOf(id) +1);
            }
            value = currentNode.getValue();

            /******EXPR********/
            //boolean
            if (value.equals("larger")) {
                replacement = ">";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("eq")) {
                replacement = "=";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if(value.equals("and")){
                replacement = "AND";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList,true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("or"))
            {
                replacement = "OR";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("not"))
            {
                replacement = "NOT";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, false, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }

            //arithmetic
            else if (value.equals("mult")) {
                replacement = "*";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("sub")) {
                replacement = "-";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList,true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("add")) {
                replacement = "+";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList,true, false);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if(currentNode.isConstant() || currentNode.isVariable() ){
                replacement = currentNode.getValue();
                if(currentNode.isVariable() && currentNode.getTypeSymbol().isString())
                    replacement = replacement + "$";
                output.add(replacement);
                return output;
            }

        }


        output.add(replacement);
        return output;
    }

    private ArrayList<String> translateNodeChildren(Node currentNode, ArrayList<Node> nodeList, String codeType) {

        String value = currentNode.getValue();
        String replacement = value;
        ArrayList<String> output =new ArrayList<>();


        if(codeType.equals("assign_con")){

            String id = String.valueOf(Integer.valueOf(currentNode.getId()) +1);
            while(currentNode.isConstant() == false && currentNode.isVariable() == false && keyWords.contains(currentNode.getValue()) ==false){
                currentNode = myTree.getNodeByID(id);
                id = String.valueOf(Integer.valueOf(id) +1);
            }
            value = currentNode.getValue();

            /******EXPR********/
            if (value.equals("mult")) {
                replacement = "*";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList,true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
                //translateNodeChildren(lhsExpr.get(0),lhsExpr,codeType) + "*" + translateNodeChildren(rhsExpr.get(0),rhsExpr,codeType);
            }
            else if (value.equals("sub")) {
                replacement = "-";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList,true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("add")) {
                replacement = "+";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }

            else if (value.equals("input")) {
                replacement = "INPUT";
                output.add(replacement);

                for(int i =0 ; i < nodeList.size();i++){
                    if(nodeList.get(i).isVariable()) {
                        output.add(nodeList.get(i).getValue());
                        break;
                    }
                }
                return output;
            }
            else if(currentNode.isConstant() || currentNode.isVariable() ){
                replacement = currentNode.getValue();
                if(currentNode.isVariable() && currentNode.getTypeSymbol().isString())
                    replacement = replacement + "$";
                output.add(replacement);
                return output;
            }
        }
        else if(codeType.equals("if_con")){


            String id = String.valueOf(Integer.valueOf(currentNode.getId()) +1);
            while(currentNode.isConstant() == false && currentNode.isVariable() == false && keyWords.contains(currentNode.getValue()) ==false){
                currentNode = myTree.getNodeByID(id);
                id = String.valueOf(Integer.valueOf(id) +1);
            }
            value = currentNode.getValue();

            /******EXPR********/
            //boolean
            if (value.equals("larger")) {
                replacement = ">";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("eq")) {
                replacement = "=";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if(value.equals("and")){
                replacement = "AND";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("or"))
            {
                replacement = "OR";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("not"))
            {
                replacement = "NOT";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, false, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }

            //arithmetic
            else if (value.equals("mult")) {
                replacement = "*";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
                //translateNodeChildren(lhsExpr.get(0),lhsExpr,codeType) + "*" + translateNodeChildren(rhsExpr.get(0),rhsExpr,codeType);
            }
            else if (value.equals("sub")) {
                replacement = "-";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("add")) {
                replacement = "+";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if(currentNode.isConstant() || currentNode.isVariable() ){
                replacement = currentNode.getValue();
                if(currentNode.isVariable() && currentNode.getTypeSymbol().isString())
                    replacement = replacement + "$";
                output.add(replacement);
                return output;
            }

        }
        else if(codeType.equals("loop_con")){


            String id = String.valueOf(Integer.valueOf(currentNode.getId()) +1);
            while(currentNode.isConstant() == false && currentNode.isVariable() == false && keyWords.contains(currentNode.getValue()) ==false){
                currentNode = myTree.getNodeByID(id);
                id = String.valueOf(Integer.valueOf(id) +1);
            }
            value = currentNode.getValue();

            /******EXPR********/
            //boolean
            if (value.equals("larger")) {
                replacement = ">";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("eq")) {
                replacement = "=";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if(value.equals("and")){
                replacement = "AND";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList,true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("or"))
            {
                replacement = "OR";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("not"))
            {
                replacement = "NOT";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, false, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }

            //arithmetic
            else if (value.equals("mult")) {
                replacement = "*";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList, true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("sub")) {
                replacement = "-";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList,true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if (value.equals("add")) {
                replacement = "+";
                ArrayList<String> temp = splitNodeBranch(replacement,codeType,nodeList,true, true);
                for(int i =0; i < temp.size(); i++){
                    output.add(temp.get(i));
                }
            }
            else if(currentNode.isConstant() || currentNode.isVariable() ){
                replacement = currentNode.getValue();
                if(currentNode.isVariable() && currentNode.getTypeSymbol().isString())
                    replacement = replacement + "$";
                output.add(replacement);
                return output;
            }

        }

        output.add(replacement);
        return output;
    }


    public ArrayList<String> getErrorList() {
        return ErrorList;
    }

    public void setErrorList(ArrayList<String> errorList) {
        ErrorList = errorList;
    }

    public ArrayList<String> getGeneratedCode() {
        return generatedCode;
    }

    public void setGeneratedCode(ArrayList<String> generatedCode) {
        this.generatedCode = generatedCode;
    }


    private ArrayList<String> splitNodeBranch(String symbol, String codeType, ArrayList<Node> nodeList, boolean isDuel, boolean isInnerExpr){
        ArrayList<String> output = new ArrayList<>();

        ArrayList<Node> lhsExpr = new ArrayList<>();
        ArrayList<Node> rhsExpr = new ArrayList<>();


        boolean flipped = false;
        int StackNumber =0;

        if (isDuel == false){
            lhsExpr = null;
            flipped = true;
        }

        for(int i=0; i< nodeList.size(); i++){
            if(nodeList.get(i).getValue().equals("("))
                StackNumber = StackNumber +1;

            if(nodeList.get(i).getValue().equals(")"))
                StackNumber = StackNumber -1;

            if(nodeList.get(i).getValue().equals(",") && StackNumber == 1){
                flipped = true;
                if (isDuel == true){
                    continue;
                }

            }

            if(flipped==false){
                lhsExpr.add(nodeList.get(i));
            }else{
                rhsExpr.add(nodeList.get(i));
            }
        }

        if(isInnerExpr == true)
            output.add("(");

        if (isDuel == true) {
            ArrayList<String> leftValues = translateNodeChildren(lhsExpr.get(0), lhsExpr, codeType);
            for (int i =0; i <leftValues.size();i++)
                output.add(leftValues.get(i));
        }

        output.add(symbol);

        ArrayList<String> rightValues = translateNodeChildren(rhsExpr.get(0),rhsExpr,codeType);
        for (int i =0; i <rightValues.size();i++)
            output.add(rightValues.get(i));

        if(isInnerExpr == true)
            output.add(")");

        return output;
    }
}
