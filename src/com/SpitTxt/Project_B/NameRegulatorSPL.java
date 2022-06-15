package com.SpitTxt.Project_B;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class NameRegulatorSPL {

    private ParseTree myTree;
    private HashMap<String, ArrayList<Node>> mySymbols;

    private ArrayList<String> procedureNames;
    private ArrayList<String> variableNames;

    private boolean foundError;
    private ArrayList<String> ErrorList;

    NameRegulatorSPL(ParseTree myTree, HashMap<String, ArrayList<Node>> mySymbols,  ArrayList ErrorList){
        this.mySymbols = mySymbols;
        this.myTree = myTree;

        this.procedureNames = new ArrayList<>();
        this.variableNames = new ArrayList<>();

        this.foundError = false;
        this.ErrorList = ErrorList;
    }


    public void begin() {
        /**************************************PHASE 1********************************************/

        //Phase 1
        System.out.println("PHASE 1: main can't exist");
        if( mySymbols.containsKey("main")){
            ArrayList<Node> nodeList = mySymbols.get("main");

            int index = 0;
            while(index < nodeList.size() && nodeList.get(index).isConstant() == false){
                index = index +1;
            }

            if(nodeList.size() != 1){
                printErrorMessage("'main' keyword cant exist more than once",nodeList.get(index) );
            }
        }

        fixNamespace(false);
        fixDeclarations();

        /**************************************PHASE 2********************************************/
        /**************************************PHASE 3********************************************/
        setNames();
        checkProcedure();
        //if (foundError == true)
            //return;
        checkVariable();

        fixNamespace(true);
        System.out.println("Final here regulator");
        fixDeclarations();

        checkDecalration();

        setNodeValues();



        //todo, fix namespace, make sure objects are different
        /*TODO: OR
        System.out.println("PHASE 1: variable - procedure check");
        for(int i = 0; i < procedureNames.size(); i++){
            if(variableNames.contains(procedureNames.get(i)) == true){
                printErrorMessage();
            }
        }


        System.out.println("PHASE 1: variable - procedure check");
        for (int i = 0; i < mySymbols.size(); i++) {
            ArrayList<Node> nodeList = mySymbols.get(String.valueOf(i));
            String variableType = nodeList.get(0).getUserName();
            //checks rest of the nodes
            for(int j=1; j< nodeList.size(); j++){
                if( variableType.equals(nodeList.get(j).getUserName()) == false){
                    printErrorMessage();
                    break;
                }
            }
        }
        */
    }

    private void checkDecalration() {
        System.out.println("PHASE 2: check procedure declarations and calls");
        for(int i = 0; i < procedureNames.size(); i++){
            if(mySymbols.containsKey(procedureNames.get(i)) == true) {
                ArrayList<Node> nodeList = mySymbols.get(procedureNames.get(i));

                if(nodeList.size() < 2){
                    if(nodeList.get(0).isDeclaration())
                        printErrorMessage("No usage of procedure declaration '" ,nodeList.get(0));
                    else
                        printErrorMessage("No declaration of procedure usage '",nodeList.get(0));
                    break;
                }else{
                    int callCount = 0;
                    int declareCount = 0;
                    for(int j = 0; j < nodeList.size(); j++){
                        Node node = nodeList.get(j);
                        if(node.isDeclaration() == true){
                            declareCount = declareCount +1;
                        }else {
                            callCount = callCount +1;
                        }
                    }

                    if( declareCount == 0){
                        printErrorMessage("No declaration of procedure usage '" ,nodeList.get(0));
                        break;
                    }
                    if( callCount == 0 ){
                        printErrorMessage("No declaration of procedure usage '" ,nodeList.get(0));
                        break;
                    }
                }

            }
        }






        //TODO: split semantic objects inside symbol-table

        System.out.println("PHASE 3: check variable declarations and calls");
        for(int i = 0; i < variableNames.size(); i++){
            if(mySymbols.containsKey(variableNames.get(i)) == true) {
                ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));

                if(nodeList.size() < 2){
                    if(nodeList.get(0).isDeclaration())
                        printErrorMessage("No usage of variable declaration '",nodeList.get(0));
                    else
                        printErrorMessage("No declaration of variable usage '", nodeList.get(0));
                    break;
                }else{
                    int callCount = 0;
                    int declareCount = 0;
                    for(int j = 0; j < nodeList.size(); j++){
                        Node node = nodeList.get(j);
                        if(node.isDeclaration() == true){
                            declareCount = declareCount +1;
                        }else {
                            callCount = callCount +1;
                        }
                    }

                    if( declareCount == 0){
                        printErrorMessage("No declaration of variable usage '" , nodeList.get(0));
                        break;
                    }
                    if( callCount == 0 ){
                        printErrorMessage("No declaration of variable usage '", nodeList.get(0));
                        break;
                    }
                }

            }
        }
    }

    private void checkVariable(){
        System.out.println("PHASE 3: check variable declarations not inside offsprings");
        for(int i = 0; i < variableNames.size(); i++){
            if(mySymbols.containsKey(variableNames.get(i)) == true){
                ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));
                ArrayList<Node> declareNodes = new ArrayList<>();

                for(int j =0; j < nodeList.size(); j++) {
                    Node declareNode = nodeList.get(j);

                    if(declareNode.isDeclaration()) {

                        declareNodes.add(declareNode);

                    }
                }


                for(int j =0; j < nodeList.size(); j++){
                    if(declareNodes.contains(nodeList.get(j)) == false) {
                        boolean noOffspring = true;

                        Node checkNode = nodeList.get(j);
                        int checkScopeId = Integer.valueOf((String) checkNode.getSplitScope().get(0));

                        for (int k = 0; k < declareNodes.size(); k++) {
                            Node declareNode = declareNodes.get(k);
                            int declareScopeId = Integer.valueOf((String) declareNode.getSplitScope().get(0));


                            if ( declareScopeId  <= checkScopeId) { //bigger id == smaller scope
                                noOffspring = false;
                            }
                        }

                        if(noOffspring == true){
                            printErrorMessage("variable declarations is out of scope, (is not found inside offspring)", checkNode);
                        }
                    }
                }


            }
        }


        System.out.println("PHASE 3: check variable declaration is it's own scope more than once");
        for(int i = 0; i < variableNames.size(); i++){
            if(mySymbols.containsKey(variableNames.get(i)) == true){
                ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));
                ArrayList<String> declaredNodeScopes =  new ArrayList<>();

                for(int j =0; j < nodeList.size(); j++) {
                    Node node = nodeList.get(j);

                    if(node.isDeclaration()) {
                        if(declaredNodeScopes.contains(node.getScope())){
                            printErrorMessage("variable declarations found 2 or more in one scope",node);
                        }else{
                            declaredNodeScopes.add(node.getScope());
                        }

                    }
                }
            }
        }


        System.out.println("PHASE 3: If there is no declaration for variable, there must exist a matching declaration");
        for(int i = 0; i < variableNames.size(); i++){
            if(mySymbols.containsKey(variableNames.get(i)) == true){
                ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));
                ArrayList<String> declaredNodeScopes =  new ArrayList<>();

                for(int j =0; j < nodeList.size(); j++) {
                    Node node = nodeList.get(j);

                    if(node.isDeclaration()) {
                        declaredNodeScopes.add(node.getScope());
                    }
                }

                if(declaredNodeScopes.isEmpty()){
                    printErrorMessage("No variable declaration found in any scope ",nodeList.get(0));
                }
            }
        }
    }

    private void checkProcedure(){
        System.out.println("PHASE 2: check procedure declarations 2 siblings");
        for(int i = 0; i < procedureNames.size(); i++){
            if(mySymbols.containsKey(procedureNames.get(i)) == true){
                ArrayList<Node> nodeList = mySymbols.get(procedureNames.get(i));
                ArrayList<String> declareNodesIds = new ArrayList<>();

                //String parentId = (String) nodeList.get(0).getSplitScope().get(0);
                for(int j =0; j < nodeList.size(); j++) {
                    Node node = nodeList.get(j);

                    String parentId = (String) node.getSplitScope().get(1);

                    if(node.isDeclaration()){
                        if(declareNodesIds.contains(parentId) == false) {
                            declareNodesIds.add(parentId);
                        }
                        else{
                            printErrorMessage("Two or more procedure declaration cannot be found in same scope ",node);
                        }
                    }
                }
            }
        }

        System.out.println("PHASE 2: check procedure declarations parent-siblings");
        for(int i = 0; i < procedureNames.size(); i++){
            if(mySymbols.containsKey(procedureNames.get(i)) == true){
                ArrayList<Node> nodeList = mySymbols.get(procedureNames.get(i));
                ArrayList<String> declareNodesPIds = new ArrayList<>();
                ArrayList<String> declareNodesLIds = new ArrayList<>();

                //String parentId = (String) nodeList.get(0).getSplitScope().get(0);
                for(int j =0; j < nodeList.size(); j++) {
                    Node node = nodeList.get(j);

                    String localId = (String) node.getSplitScope().get(0);
                    String parentId = (String) node.getSplitScope().get(1);

                    if(node.isDeclaration()){

                        if(declareNodesLIds.isEmpty() || declareNodesPIds.isEmpty()){
                            declareNodesLIds.add(localId);
                            declareNodesPIds.add(parentId);
                        }else {
                            if (declareNodesPIds.contains(localId) == true) {
                                printErrorMessage("Procedure declaration cannot be found withing the same procedure declaration",node);
                            }
                            if (declareNodesLIds.contains(parentId) == true) {
                                printErrorMessage("Procedure declaration cannot be found withing the same procedure declaration",node);
                            }
                            else {
                                declareNodesLIds.add(localId);
                                declareNodesPIds.add(parentId);
                            }
                        }
                    }
                }
            }
        }


    }

    private void fixDeclarations() {
        for(int i = 0; i < procedureNames.size(); i++){
            if(mySymbols.containsKey(procedureNames.get(i)) == true){
                ArrayList<Node> nodeList = mySymbols.get(procedureNames.get(i));
                ArrayList<Node> declaredNodes =  new ArrayList<>();

                for(int j =0; j < nodeList.size(); j++) {
                    Node node = nodeList.get(j);

                    if(node.isDeclaration()) {
                        declaredNodes.add(node);
                    }
                }

                ArrayList<Node> newNodeList = new ArrayList<>();

                for(int j=0; j < declaredNodes.size(); j++){
                    newNodeList.add(declaredNodes.get(j));
                }

                for(int j =0; j < nodeList.size(); j++) {
                    Node node = nodeList.get(j);

                    if(declaredNodes.contains(node)) {
                        continue;
                    }else{
                        newNodeList.add(node);
                    }
                }

                mySymbols.replace(procedureNames.get(i),nodeList, newNodeList);
            }
        }


        for(int i = 0; i < variableNames.size(); i++){
            if(mySymbols.containsKey(variableNames.get(i)) == true){
                ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));
                ArrayList<Node> declaredNodes =  new ArrayList<>();

                for(int j =0; j < nodeList.size(); j++) {
                    Node node = nodeList.get(j);

                    if(node.isDeclaration()) {
                        declaredNodes.add(node);
                    }
                }

                ArrayList<Node> newNodeList = new ArrayList<>();

                for(int j=0; j < declaredNodes.size(); j++){
                    newNodeList.add(declaredNodes.get(j));
                }

                for(int j =0; j < nodeList.size(); j++) {
                    Node node = nodeList.get(j);

                    if(declaredNodes.contains(node)) {
                        continue;
                    }else{
                        newNodeList.add(node);
                    }
                }

                mySymbols.replace(variableNames.get(i),nodeList, newNodeList);
            }
        }

    }

    private void fixNamespace(boolean isFinal) {

        System.out.println("Fix Name Space : " + isFinal);

        if(isFinal == false){
            for (String value: mySymbols.keySet()) {


                /*if(value.charAt(1)=='/'){ // isScope ignore
                    continue;
                }*/

                ArrayList<Node> nodeList = mySymbols.get(value);

                ArrayList<Node> isProcedure = new ArrayList<>();
                ArrayList<Node> isVariable = new ArrayList<>();
                ArrayList<Node> isNothing = new ArrayList<>();

                System.out.println("CALL_BACK " + mySymbols.get(value));
                if(nodeList != null) {
                    for (int j = 0; j < nodeList.size(); j++) {
                        Node node = nodeList.get(j);

                        if (node.isProcedure() == true) {
                            isProcedure.add(node);

                        }
                        else if (node.isVariable() == true) {
                            isVariable.add(node);

                        }else{
                            isNothing.add(node);
                        }
                    }
                }


                if( (isNothing.isEmpty() == false) && (isProcedure.isEmpty() == true) && (isVariable.isEmpty() == true) ){
                    return;
                }
                else if((isNothing.isEmpty() == true) && (isProcedure.isEmpty() == false) && (isVariable.isEmpty() == true)){
                    return;
                }
                else if((isNothing.isEmpty() == true) && (isProcedure.isEmpty() == true) && (isVariable.isEmpty() == false)){
                    return;
                }
                else{
                    if(isProcedure.isEmpty() == false ) {
                        String newValue = value + "_P";
                        mySymbols.put(newValue, isProcedure);
                        mySymbols.replace(value,nodeList, isNothing);
                    }

                    if(isVariable.isEmpty() == false ) {
                        String newValue = value + "_V";
                        mySymbols.put(newValue, isProcedure);
                        mySymbols.replace(value,nodeList, isNothing);
                    }
                }
            }
        }else{
            //fix according to scope

            ArrayList<String> names = new ArrayList<>();

            for(int i = 0; i < procedureNames.size(); i++)
                names.add(procedureNames.get(i));
            for(int i = 0; i < variableNames.size(); i++)
                names.add(variableNames.get(i));

            for(int i = 0; i < names.size(); i++){
                if(mySymbols.containsKey(names.get(i)) == true) {
                    //ArrayList<Node> nodeList = mySymbols.get(procedureNames.get(i));

                    String value = names.get(i);
                    ArrayList<Node> nodeList = mySymbols.get(value);

                    ArrayList<Node> declaredNodes = new ArrayList<>();
                    ArrayList<ArrayList<Node>> callNodes = new ArrayList<>();


                    for (int j = 0; j < nodeList.size(); j++) {
                        Node node = nodeList.get(j);
                        if (node.isDeclaration()) {
                            declaredNodes.add(node);
                        }

                        /*if (node.isProcedure() == true) {
                            isProcedure.add(node);

                        }
                        else if (node.isVariable() == true) {
                            isVariable.add(node);

                        }
                        else {
                            isNothing.add(node);
                        }*/
                    }

                    if (declaredNodes.size() > 1) { //more than 1 declare node else skip everything

                        System.out.println("Scope is checked here babYYY");

                        for (int k = 0; k < declaredNodes.size(); k++) {
                            callNodes.add(new ArrayList<>());
                        }

                        for (int bull = 0; bull < nodeList.size(); bull++) {
                            Node node = nodeList.get(bull);

                            if (declaredNodes.contains(node) == false) {
                                boolean found = false;

                                for (int mac = 0; mac < declaredNodes.size(); mac++) {
                                    if (isScopeRelated(declaredNodes.get(mac), node,declaredNodes ) == true) {

                                        System.out.println("WORK WORKKK");
                                        callNodes.get(mac).add(node);
                                        found = true;
                                    }
                                }

                                if(found == false){
                                    //printErrorMessage(); // highly doubt the error happens but for debugging you never know
                                    printErrorMessage("highly doubt this error happens (var/proc node not found another declaration after checks)",node);
                                }
                            }

                        }

                        for (int k = 0; k < declaredNodes.size(); k++) {
                            String newValue = value + "_" + String.valueOf(i+1);
                            mySymbols.put(newValue, callNodes.get(k));

                        }

                        mySymbols.remove(value);

                    }
                }
            }
        }
    }

    private boolean isScopeRelated(Node declaredNode, Node node, ArrayList<Node> list) {

        if(declaredNode.isProcedure()){
            if(declaredNode.getScope().equals(node.getScope())){
                return true;
            }

            ArrayList<String> children = (ArrayList<String>) declaredNode.getSplitScope().get(2);
            String checkID = (String) node.getSplitScope().get(0);

            if(children.contains(checkID)){
                return true;
            }
        }else if(declaredNode.isVariable()){
            /***********/
            if(declaredNode.getScope().equals(node.getScope())){
                return true;
            }

            //ArrayList<String> declareID = (ArrayList<String>) declaredNode.getSplitScope().get(2);
            String declareID = (String) declaredNode.getSplitScope().get(0);
            String nodeIdParent = (String) node.getSplitScope().get(1);
            boolean isNearest = true;
            int distance = 1 ;

            while(nodeIdParent.equals(declareID) == false ){

                if(nodeIdParent.equals(declareID)){
                    break;
                }

                ArrayList<String> scopeList = myTree.getScopeList();
                int index = 0;
                String parentInnerID = (String) myTree.getSplitScope(scopeList.get(index)).get(0);
                while(index < scopeList.size() ){
                    if(parentInnerID.equals(nodeIdParent))
                        break;

                    index = index + 1;
                    if(index >=scopeList.size())
                        return false;

                    parentInnerID = (String) myTree.getSplitScope(scopeList.get(index)).get(0);
                }

                nodeIdParent = (String) myTree.getSplitScope(scopeList.get(index)).get(1);
                distance = distance +1;
            }

            //validates if it is truly the case

            for(int i=0; i < list.size(); i++){

                String newDeclareID = (String) list.get(i).getSplitScope().get(0);
                String newNodeIdParent = (String) node.getSplitScope().get(1);

                int newDistance = 1 ;

                while(newNodeIdParent.equals(newDeclareID) == false ){

                    if(newNodeIdParent.equals(newDeclareID)){
                        break;
                    }

                    ArrayList<String> scopeList = myTree.getScopeList();
                    int index = 0;
                    String parentInnerID = (String) myTree.getSplitScope(scopeList.get(index)).get(0);
                    while(index < scopeList.size()){
                        if(parentInnerID.equals(newNodeIdParent)) {
                            break;
                        }

                        index = index + 1;
                        if(index >=scopeList.size())
                            break;

                        parentInnerID = (String) myTree.getSplitScope(scopeList.get(index)).get(0);
                    }

                    newNodeIdParent = (String) myTree.getSplitScope(scopeList.get(index)).get(1);
                    newDistance = newDistance +1;

                    if(newDistance > distance){
                        break;
                    }
                }

                if (newDistance < distance)
                    isNearest = false;
            }

            return isNearest;

            /***********/

        }

        return false;
    }

    private void setNames() {
        procedureNames = new ArrayList<>();
        variableNames = new ArrayList<>();

        for (String value: mySymbols.keySet()) {

            ArrayList<Node> nodeList = mySymbols.get(value);
            System.out.println("CALL_BACK " + mySymbols.get(value));
            //ArrayList<Node> nodeList = mySymbols.get(String.valueOf(i));

            //checks rest of the nodes
            if(nodeList != null) {
                for (int j = 0; j < nodeList.size(); j++) {
                    Node node = nodeList.get(j);

                    if (node.isProcedure() == true) {
                        procedureNames.add(node.getValue());
                    }
                    else if (node.isVariable() == true) {
                        variableNames.add(node.getValue());
                    }
                }
            }
        }
    }

    private void printErrorMessage(String s , Node node) {
        if(foundError == true)
            return;

        foundError = true;

        //("'" +node.getValue() +"' @line" +node.getLineCount()))
        String finalError = "Semantic error (regulator): " + "\n" + s +"\n'" + node.getValue() +"' @line " +node.getLineCount();
        this.ErrorList.add(finalError );
    }

    public ArrayList<String> getErrorList() {
        return ErrorList;
    }

    public void setErrorList(ArrayList<String> errorList) {
        ErrorList = errorList;
    }

    private void setNodeValues(){

        setNames();

        for(int i = 0; i < procedureNames.size(); i++){
            if(mySymbols.containsKey(procedureNames.get(i)) == true){
                ArrayList<Node> nodeList = mySymbols.get(procedureNames.get(i));
                for(int j =0; j < nodeList.size(); j++) {
                    if(nodeList.get(j).getValue().equals(procedureNames.get(i)) == false)
                        nodeList.get(j).setRegulatedName(procedureNames.get(i));
                }
            }
        }


        for(int i = 0; i < variableNames.size(); i++){
            if(mySymbols.containsKey(variableNames.get(i)) == true){
                ArrayList<Node> nodeList = mySymbols.get(variableNames.get(i));

                for(int j =0; j < nodeList.size(); j++) {
                    if(nodeList.get(j).getValue().equals(variableNames.get(i)) == false)
                        nodeList.get(j).setRegulatedName(variableNames.get(i));
                }
            }
        }

    }

}
