package com.SpitTxt.Project_A;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class SyntaxAnalyserSPL {
    private LinkedList<List<Object>> Tokenlist;
    private LinkedList<List<Object>> copyTokenlist;
    private ParseTree myTree;
    private ParseTree myPrunedTree;
    private boolean tokenCount;
    private String foundError;


    //private List<List<String[]>> parserTree = new ArrayList<List<String[]>>();
    private int Index;
    private boolean endBranch;


    SyntaxAnalyserSPL(LinkedList<List<Object>> Tokenlist){
        this.Tokenlist = Tokenlist;
        this.copyTokenlist = copyList(Tokenlist);

        //this.currentNode = new Node("PROG");w
        this.myTree = new ParseTree();
        this.myPrunedTree = new ParseTree();
        //this.endBranch = false;

        this.tokenCount = true;
        foundError = new String();
    }

    public void begin(){
        branchSPLProgr();

        /*Index = 0;
        while((Index <= Tokenlist.size()) && (this.endBranch ==false)) {
            List<Object> next = Tokenlist.get(Index);
            System.out.println("Token new branch : " + (String) next.get(2) +" - " + (String) next.get(1));
            Index = addBranch(Index);
        }*/
    }

    private LinkedList<List<Object>> copyList(LinkedList<List<Object>> list){
        LinkedList<List<Object>> output = new LinkedList<>();
        for (int i =0; i < list.size(); i++){
            output.add(list.get(i));
        }

        return output;
    }

    public ParseTree getParseTree(){
        return myTree;
    }

    public ParseTree getPrunedParseTree(){
        myPrunedTree= new ParseTree(myTree);
        myPrunedTree.prune();

        //myPrunedTree.prune();
        return myPrunedTree;
    }

    /*****************************BRANCHES***********************************/

    private void branchSPLProgr(){ //ROOT NODE
        branchProcDefs(); //CAN BE NULL

        String[] branchCheck = new String[]{"main", "{"};
        happy(branchCheck);

        branchAlgorithm();

        branchCheck = new String[]{"halt", ";"};
        happy(branchCheck);

        branchVarDecl();

        branchCheck = new String[]{"}"};
        happy(branchCheck);
    }

    private boolean branchProcDefs(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        //ADD
        String branchName = "ProcDefs";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES
        if (branchPD() == false){ //this is null
            myTree.rollBack(branchName);
            myTree.removeTreeNode(node);
            return true;
        }


        String[] branchCheck = new String[]{","};
        happy(branchCheck);

        branchProcDefs();


        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchPD(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        if(value.equals("proc") == false){
            return false;
        }

        //ADD
        String branchName = "PD";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE
        String[] branchCheck = new String[]{"proc"};
        happy(branchCheck);

        if(isUserDefinedName() == false) {
            tokenObject = Tokenlist.get(0);
            value= (String) tokenObject.get(2);
            printError(value + " is not a UserDefinedName");
        }

        branchCheck = new String[]{"{"};
        happy(branchCheck);

        branchProcDefs();
        branchAlgorithm();

        branchCheck = new String[]{"return", ";"};
        happy(branchCheck);

        branchVarDecl();

        branchCheck = new String[]{"}"};
        happy(branchCheck);

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchAlgorithm(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        System.out.println("Checking algorithm");
        //ADD
        String branchName = "Algorithm";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE
        if(branchInstr() == false) { // null
            myTree.rollBack(branchName);
            myTree.removeTreeNode(node);
            return true;
        }

        String[] branchCheck = new String[]{";"};
        happy(branchCheck);

        branchAlgorithm();

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchInstr(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        System.out.println("Checking instr");
        //ADD
        String branchName = "Instr";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE
        if(branchAssign() ==true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }
        else if(branchBranch() ==true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }
        else if(branchLoop() ==true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }
        else if(branchPCall() ==true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }


        myTree.rollBack(branchName);
        myTree.removeTreeNode(node);
        return false; //null
    }

    private boolean branchAssign(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(1);
        String value= (String) tokenObject.get(0);
        /*List<Object> tokenObject2 = Tokenlist.get(2);
        String value2= (String) tokenObject2.get(0);*/
        if( (value.equals(":=") == false)){// || (value2.equals("=") == false) ){
            return false;
        }

        System.out.println("checking Assign");
        //ADD
        String branchName = "Assign";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE
        branchLHS();

        String[] branchCheck = new String[]{":="};
        happy(branchCheck);

        branchExpr();

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchBranch(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        if(value.equals("if") == false){
            return false;
        }

        System.out.println("Checking branch");
        //ADD
        String branchName = "Branch";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE

        String[] branchCheck = new String[]{"if" ,"("};
        happy(branchCheck);

        branchExpr();

        branchCheck = new String[]{")" ,"then", "{"};
        happy(branchCheck);

        branchAlgorithm();

        branchCheck = new String[]{"}"};
        happy(branchCheck);

        branchAlternat();

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchAlternat(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        if(value.equals("else") == false){
            return true; //null
        }

        System.out.println("Checking alternat");
        //ADD
        String branchName = "Alternat";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE

        String[] branchCheck = new String[]{"else" ,"{"};
        happy(branchCheck);

        branchAlgorithm();

        branchCheck = new String[]{"}"};
        happy(branchCheck);

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchLoop(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        String loopType = "";

        if(value.equals("do") == true){
            loopType = value;
        }
        else if(value.equals("while") == true){
            loopType = value;
        }
        else{
            return false;
        }

        System.out.println("Checking loop");
        //ADD
        String branchName = "Loop";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE
        if (loopType.equals("do")){
            String[] branchCheck = new String[]{"do" ,"{"};
            happy(branchCheck);

            branchAlgorithm();

            branchCheck = new String[]{"}" ,"until", "("};
            happy(branchCheck);

            branchExpr();

            branchCheck = new String[]{")"};
            happy(branchCheck);
        }else if (loopType.equals("while")){
            String[] branchCheck = new String[]{"while" ,"("};
            happy(branchCheck);

            branchExpr();

            branchCheck = new String[]{")" ,"do", "{"};
            happy(branchCheck);

            branchAlgorithm();

            branchCheck = new String[]{"}"};
            happy(branchCheck);
        }

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchLHS(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        System.out.println("Checking lhs");
        //ADD
        String branchName = "LHS";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE
        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        if(value.equals("output")) {
            String[] branchCheck = new String[]{"output"};
            happy(branchCheck);

            myTree.rollBack(node.getHead().getValue());
            return true;
        }
        else if (branchVar() == true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }
        else if (branchField() == true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }

        myTree.rollBack(node.getHead().getValue());
        myTree.removeTreeNode(node);
        return false;
    }

    private boolean branchExpr(){

        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        System.out.println("Checking expr");
        //ADD
        String branchName = "Expr";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE
        if(branchConst() == true) {
            myTree.rollBack(node.getHead().getValue());
            return true;
        }
         if (branchVar() == true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }
        else if (branchField() == true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }
        else if (branchUnOp() == true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }
        else if (branchBinOp() == true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }

        myTree.rollBack(node.getHead().getValue());
        myTree.removeTreeNode(node);
        return false;
    }

    private boolean branchPCall(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        if( value.equals("call") == false ){
            return false;
        }

        System.out.println("Checking pcall");
        //ADD
        String branchName = "PCall";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        String[] branchCheck = new String[]{"call"};
        happy(branchCheck);

        if (isUserDefinedName() == false)
            printError(value + " is not a UserDefinedName");

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchVar(){

        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        List<Object> tokenObject2 = Tokenlist.get(1);
        String value2= (String) tokenObject2.get(0);
        if( (value.equals("call") == true) || (value2.equals("[") == true) ){
            return false;
        }

        //ADD
        String branchName = "Var";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        if (isUserDefinedName() == false)
            printError(value + " is not a UserDefinedName");


        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchField(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(1);
        String value= (String) tokenObject.get(0);
        if( value.equals("[") == false ){
            return false;
        }

        //ADD
        String branchName = "Field";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        if (isUserDefinedName() == false)
            printError(value + " is not a UserDefinedName");

        String[] branchCheck = new String[]{"["};
        happy(branchCheck);

        if (branchConst() == false) { //weird but works
            branchVar();
        }

        branchCheck = new String[]{"]"};
        happy(branchCheck);

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchConst(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        //ADD
        String branchName = "Const";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE
        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        if(value.equals("true")) {
            String[] branchCheck = new String[]{"true"};
            happy(branchCheck);

            myTree.rollBack(node.getHead().getValue());
            return true;
        }
        else if (value.equals("false")) {
            String[] branchCheck = new String[]{"false"};
            happy(branchCheck);

            myTree.rollBack(node.getHead().getValue());
            return true;
        }
        else if (isShortString() == true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }
        else if (isNumber() == true){
            myTree.rollBack(node.getHead().getValue());
            return true;
        }

        myTree.rollBack(node.getHead().getValue());
        myTree.removeTreeNode(node);
        return false;
    }

    private boolean branchUnOp(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        String opType = "";

        if(value.equals("input") == true){
            opType = value;
        }
        else if(value.equals("not") == true){
            opType = value;
        }
        else{
            return false;
        }

        //ADD
        String branchName = "UnOp";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE
        if (opType.equals("input")){
            String[] branchCheck = new String[]{"input" ,"("};
            happy(branchCheck);

            branchVar();

            branchCheck = new String[]{")"};
            happy(branchCheck);
        }else if (opType.equals("not")){
            String[] branchCheck = new String[]{"not" ,"("};
            happy(branchCheck);

            branchExpr();

            branchCheck = new String[]{")"};
            happy(branchCheck);
        }

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchBinOp(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        String opType = "";

        if(value.equals("and") == true){
            opType = value;
        }
        else if(value.equals("or") == true){
            opType = value;
        }
        else if(value.equals("eq") == true){
            opType = value;
        }
        else if(value.equals("larger") == true){
            opType = value;
        }
        else if(value.equals("add") == true){
            opType = value;
        }
        else if(value.equals("sub") == true){
            opType = value;
        }
        else if(value.equals("mult") == true){
            opType = value;
        }
        else{
            return false;
        }

        //ADD
        String branchName = "BinOp";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES HERE

        String[] branchCheck = new String[]{opType ,"("};
        happy(branchCheck);

        branchExpr();

        branchCheck = new String[]{","};
        happy(branchCheck);

        branchExpr();

        branchCheck = new String[]{")"};
        happy(branchCheck);

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchVarDecl(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        //ADD
        String branchName = "VarDecl";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES
        if(branchDec() == false){
            myTree.rollBack(branchName);
            myTree.removeTreeNode(node);
            return true; //null
        }

        String[] branchCheck = new String[]{";"};
        happy(branchCheck);

        branchVarDecl();

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchDec(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        boolean isArray = false;
        if(value.equals("arr")){
            isArray = true;
        }

        //ADD
        String branchName = "Dec";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES
        if (isArray == false) {
            if (branchTYP() == false) {
                myTree.rollBack(branchName);
                myTree.removeTreeNode(node);
                return false; //null
            }

            branchVar();
        }else{
            String[] branchCheck = new String[]{"arr"};
            happy(branchCheck);

            branchTYP();

            branchCheck = new String[]{"["};
            happy(branchCheck);

            branchConst();

            branchCheck = new String[]{"]"};
            happy(branchCheck);

            branchVar();
        }

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private boolean branchTYP(){
        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(0);
        String value= (String) tokenObject.get(0);
        String addValue = "" ;
        if( value.equals("num") == true ){
            addValue = value;
        }
        else if (value.equals("bool") == true){
            addValue = value;
        }
        else if (value.equals("string") == true){
            addValue = value;
        }
        else{
            return false;
        }

        //ADD
        String branchName = "TYP";
        Node node = new Node(branchName, false);
        myTree.addTreeNode(node);

        //VALUES

        String[] branchCheck = new String[]{addValue};
        happy(branchCheck);

        myTree.rollBack(node.getHead().getValue());
        return true;
    }


    private boolean isUserDefinedName() {
        List<Object> tokenObject = Tokenlist.get(0);
        String token = (String) tokenObject.get(0);

        //VALIDATE
        if(foundError.isEmpty() == false) {
            return true;
        }

        if(token.charAt(0) == '"') {//error found; stop adding to tree (report error)
            //printError();
            return false;
        }

        //ADD
        //VALUES
        Node node = new Node(token, true);
        myTree.addTreeNode(node);
        Tokenlist.remove(0);

        myTree.rollBack(node.getHead().getValue());

        return true;
    }

    private boolean isShortString() {
        List<Object> tokenObject = Tokenlist.get(0);
        String token = (String) tokenObject.get(0);

        //VALIDATE
        if( (token.charAt(0) != '"') || (token.charAt(token.length()-1) != '"') ) {//error found; stop adding to tree (report error)
            //printError();
            return false;
        }

        //ADD
        //VALUES
        Node node = new Node(token, true);
        myTree.addTreeNode(node);
        Tokenlist.remove(0);

        myTree.rollBack(node.getHead().getValue());

        return true;
    }

    private boolean isNumber() {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        List<Object> tokenObject = Tokenlist.get(0);
        String token = (String) tokenObject.get(0);

        //VALIDATE

        if(pattern.matcher(token).matches() == false) {//error found; stop adding to tree (report error)
            //printError();
            return false;
        }

        //ADD
        //VALUES
        Node node = new Node(token, true);
        myTree.addTreeNode(node);
        Tokenlist.remove(0);

        myTree.rollBack(node.getHead().getValue());

        return true;
    }

    /*****************************BRANCHES***********************************/

    private boolean checkTokenList(String value){ //checks and add-removes 1st token
        if(foundError.isEmpty() == false) {//error found; stop adding to tree (report error)
            //printError();
            return true;
        }

        List<Object> tokenObject = Tokenlist.get(0);
        String[] token = new String[]{(String) tokenObject.get(0)};
        //String tokenType = (String) tokenObject.get(1);
        //List<String[]> branch = new ArrayList<String[]>();

        if(value.equals(token[0]) == false){
            return false;
        }

        //add to syntax tree
        Node node = new Node(value, true);
        myTree.addTreeNode(node);
        Tokenlist.remove(0);

        myTree.rollBack(node.getHead().getValue());
        return true;
    }

    private void happy(String[] branchCheck){
        for (int i = 0; i < branchCheck.length; i++) {
            if (checkTokenList(branchCheck[i]) == false) {
                List<Object> tokenObject = Tokenlist.get(0);
                String value = (String) tokenObject.get(0);
                printError(branchCheck[i] + " expected but found '" + value + "' instead");
                break;
            }
        }
    }

    private void printError(String s) {
        foundError = s;
        myTree.addTreeNode(new Node(s,true));
    }
}
