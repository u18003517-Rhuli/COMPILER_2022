package com.SpitTxt.Project_A;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SyntaxAnalyser {
    private LinkedList<List<Object>> Tokenlist;
    private ParseTree myTree;
    private ParseTree myPrunedTree;


    //private List<List<String[]>> parserTree = new ArrayList<List<String[]>>();
    private int Index;
    private boolean endBranch;


    SyntaxAnalyser(LinkedList<List<Object>> Tokenlist){
        this.Tokenlist = Tokenlist;
        //this.currentNode = new Node("PROG");
        this.myTree = new ParseTree();
        this.myPrunedTree = new ParseTree();
        //this.endBranch = false;
    }

    public void begin(){
        Index = 0;
        while((Index <= Tokenlist.size()) && (this.endBranch ==false)) {
            List<Object> next = Tokenlist.get(Index);
            System.out.println("Token new branch : " + (String) next.get(2) +" - " + (String) next.get(1));
            Index = addBranch(Index);
        }
    }

    private int addBranch(int index) {
        if(index < Tokenlist.size() && this.endBranch == false ) {
            if( index >= (Tokenlist.size()-1)) {
                this.endBranch = true;
                return Tokenlist.size();
            }

            myTree.reset();

            List<Object> next = Tokenlist.get(index);
            String[] token = new String[]{(String) next.get(2)};
            String tokenType = (String) next.get(1);
            List<String[]> branch = new ArrayList<String[]>();


            //System.out.println("Next Token : " + index + " : " + token[0] + " - " + tokenType );

            /*if (tokenType.equals("Procedure definition")) {
                branchProc_Defs(branch, token, tokenType, true);
            }*/

            branchProg(branch, token, tokenType, true);

        }

        return  Index;
    }

    /*public ArrayList<String> printTree(){
        //myTree.prune();
        myPrunedTree = myTree;
        myPrunedTree.prune();
        return myPrunedTree.printTree();
    }*/

    public ParseTree getParseTree(){
        return myTree;
    }

    public ParseTree getPrunedParseTree(){
        myPrunedTree= new ParseTree(myTree);
        myPrunedTree.prune();

        //myPrunedTree.prune();
        return myPrunedTree;
    }


    private int branchProg(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"PROG"};
        boolean isTerminal = false;

        if(endValue == false)
            addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            int getBack = branchCode(branch,token,tokenType,endValue );
            myTree.rollBack(branchName[0]);

            //if(endValue ==false)
                //incrementIndex(); TODO: PROG excpetion (for how CODE sends back ...?)

            if( Index >= Tokenlist.size()) {
                this.endBranch = true;
                return 1;
            }

            List<Object> next = Tokenlist.get(Index);
            String[] tokenT = new String[]{(String) next.get(2)};
            String tokenTypeT = (String) next.get(1);

            if(tokenT[0].equals(";")) {
                addNode(tokenT[0],tokenTypeT);

                incrementIndex();
                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                if(tokenTypeT.equals("Procedure definition") ) {
                    getBack = branchProc_Defs(branch, tokenT, tokenTypeT, endValue);
                    myTree.rollBack(branchName[0]);

                    if (endValue == true) {
                        //endBranch(branch, tokenT, endValue);
                    } else
                        return 1;
                }
                else{
                    /*
                    if(endValue ==false)
                        decrementIndex();
                    decrementIndex();
                    myTree.removeLast();*/
                    showError("Can't End on ';' in this case");
                    return 1;
                }
            }
            else{
                //if(endValue ==false)
                    //decrementIndex();
                return 1;
            }
        }
        else{
            //endBranch(branch,token, endValue);
        }
        return 0;
    }

    private int branchProc_Defs(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        /********************COME BACK HERE****************/
        String[] branchName = new String[]{"PROC_DEFS"};
        boolean isTerminal = false;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            int getBack = branchProc(branch,token,tokenType, endValue);
            myTree.rollBack(branchName[0]);

            //if(endValue ==false)
                incrementIndex();

            if( Index >= Tokenlist.size()) {
                this.endBranch = true;
                return 1;
            }

            List<Object> next = Tokenlist.get(Index);
            String[] tokenT = new String[]{(String) next.get(2)};
            String tokenTypeT = (String) next.get(1);

            if(tokenTypeT.equals("Procedure definition")) {
                getBack = branchProc_Defs(branch,tokenT,tokenTypeT,endValue);
                myTree.rollBack(branchName[0]);

                if (endValue == true)
                    return 1;
                    //endBranch(branch, tokenT, endValue);
                 else
                    return 1;
            }
            else{
                decrementIndex();
                return 1;
            }
        }
        else{
            //endBranch(branch,token, endValue);
        }
        return 0;
    }


    private int branchProc(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"PROC"};
        boolean isTerminal = true;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            return 0;
        }
        else{
            String[] mainToken = new String[5];
            //mainToken[1] = token[0];
            addNode(token[0],tokenType);

            incrementIndex();
            List<Object> next = Tokenlist.get(Index);
            String[] tokenT = new String[]{(String) next.get(2)};
            String tokenTypeT = (String) next.get(1);

            if(tokenTypeT.equals("User-defined name")){
                addNode(tokenT[0],tokenTypeT);
                incrementIndex();
                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                if((tokenT[0].equals("{")) && (tokenTypeT.equals("Grouping symbols"))){
                    addNode(tokenT[0],tokenTypeT);
                    incrementIndex();
                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);


                    /*****************SET OUT BRANCHes****************/
                    List<String[]> branchT = new ArrayList<String[]>();
                    int getBack = branchProg(branchT,tokenT,tokenTypeT,false);
                    //mainToken[3] = setBranchToString(branchT);
                    /*****************SET OUT BRANCHes****************/
                    myTree.rollBack(branchName[0]);
                    incrementIndex();

                    if( Index >= Tokenlist.size()) {
                        this.endBranch = true;
                        return 1;
                    }

                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    if((tokenT[0].equals("}")) && (tokenTypeT.equals("Grouping symbols"))){
                        //System.out.println(getBack);
                        addNode(tokenT[0],tokenTypeT);
                        if(endValue == true) {
                            //endBranch(branch,mainToken,endValue);
                        }
                        else
                            return 1;
                    }
                    else{
                        showError("Expected : Grouping symbol '}' : " + tokenT[0] + " found instead");
                    }
                }
                else{
                    showError("Expected : Grouping symbol '{' : " + tokenT[0] + " found instead");
                }
            }
            else {
                showError("Expected : User-defined name : " + tokenTypeT + " found instead");
            }

        }
        return 0;
    }

    private int branchCode(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"CODE"};
        boolean isTerminal = false;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            if((tokenType.equals("Special command")) || (tokenType.equals("I/O commands")) || (tokenType.equals("User-defined name"))
                    || (tokenType.equals("Assignment operator")) || (tokenType.equals("Control structure"))) {

                int getBack = branchInstr(branch,token,tokenType,endValue);
                myTree.rollBack(branchName[0]);

                //if(endValue ==false)
                    incrementIndex();

                if( Index >= Tokenlist.size()) {
                    this.endBranch = true;
                    return 1;
                }

                List<Object> next = Tokenlist.get(Index);
                String[] tokenT = new String[]{(String) next.get(2)};
                String tokenTypeT = (String) next.get(1);

                //System.out.println("CODE INTO : " + tokenT[0]);

                if(tokenT[0].equals(";")){
                    addNode(tokenT[0],tokenTypeT);

                    incrementIndex();
                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    //System.out.println("CODE INTO");
                    if(!tokenTypeT.equals("Procedure definition")) {
                        //System.out.println("CODE INTO");
                        getBack = branchCode(branch,tokenT,tokenTypeT,endValue);
                        myTree.rollBack(branchName[0]);

                        if (endValue == true) {
                            //endBranch(branch, tokenT, endValue);
                            return 1;
                        } else
                            return 1;
                    }
                    else{
                        //if(endValue ==false)
                            //decrementIndex();
                        decrementIndex();
                        myTree.removeLast();
                        return 1;
                    }
                }
                else if(endValue ==false){ //will rollout
                    //if(endValue ==false)
                        decrementIndex();
                    return 1;
                }
                else{
                    return 1;
                }
            }else {
                showError("Expected : CodeType (probably missing ';'):  " + token[0] +" : " + tokenType + " found instead");
            }
        }
        else{
            //endBranch(branch,token, endValue);
        }
        return 0;
    }

    private int branchInstr(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"INSTR"};
        boolean isTerminal = false;

        addDefaults(branchName[0], tokenType);

        List<Object> next;
        String tokenType2 = "";

        if((Index+1) < Tokenlist.size()) {
            next = Tokenlist.get(Index + 1);
            tokenType2 = (String) next.get(1);
        }


        if(isTerminal == false){
            if(tokenType.equals("Special command")) {

                addNode(token[0],tokenType);
                if(endValue==true)
                    return 1;
                    //endBranch(branch,token,endValue);
                else
                    return 1;
            }
            else if (tokenType.equals("I/O commands")) {
                int getBack = branchIo(branch,token,tokenType,endValue);
            }
            else if ((tokenType.equals("Assignment operator"))){
                int getBack = branchAssign(branch,token,tokenType,endValue);
            }
            else if ((tokenType.equals("User-defined name")) && (tokenType2.equals("Assignment operator"))){
                int getBack = branchAssign(branch,token,tokenType,endValue);
            }
            else if ((tokenType.equals("Assignment operator")) && (tokenType2.equals("User-defined name"))){
                int getBack = branchAssign(branch,token,tokenType,endValue);
            }
            else if(tokenType.equals("User-defined name")){
                int getBack = branchCall(branch,token,tokenType,endValue);
            }
            else if (tokenType.equals("Control structure")){
                if((token[0].equals("if")) ||(token[0].equals("then")) || (token[0].equals("else")) ){
                    int getBack = branchCond_Branch(branch,token,tokenType,endValue);
                }
                else if ((token[0].equals("while")) ||(token[0].equals("for"))){
                    int getBack = branchCond_Loop(branch,token,tokenType,endValue);
                }
            }
            else {
                showError("Expected : InstrType: " + token[0] +" : " + tokenType + " found instead");
            }
        }
        else{
            //endBranch(branch,token, endValue);
        }
        return 0;
    }



    private int branchIo(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"IO"};
        boolean isTerminal = true;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            return 0;
        }
        else{
            String[] mainToken = new String[4];
            addNode(token[0],tokenType);
            incrementIndex();
            List<Object> next = Tokenlist.get(Index);
            String[] tokenT = new String[]{(String) next.get(2)};
            String tokenTypeT = (String) next.get(1);


            if((tokenT[0].equals("(")) && (tokenTypeT.equals("Grouping symbols"))){
                addNode(tokenT[0],tokenTypeT);
                incrementIndex();
                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                /*****************SET OUT BRANCHes****************/
                List<String[]> branchT = new ArrayList<String[]>();
                int getBack = branchVar(branchT,tokenT,tokenTypeT,false);
                mainToken[2] =  setBranchToString(branchT);
                /*****************SET OUT BRANCHes****************/
                myTree.rollBack(branchName[0]);
                incrementIndex();

                if( Index >= Tokenlist.size()) {
                    this.endBranch = true;
                    return 1;
                }

                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                if((tokenT[0].equals(")")) && (tokenTypeT.equals("Grouping symbols"))){
                    //System.out.println(getBack);
                    addNode(tokenT[0],tokenTypeT);

                    if(endValue==true) {
                        //endBranch(branch,mainToken,endValue);
                    }
                    else
                        return 1;
                }
                else{
                    showError("Expected : Grouping symbol ')' : " + tokenT[0] + " found instead");
                }
            }
            else{
                showError("Expected : Grouping symbol '(' herer : " + tokenT[0] + " found instead");
            }
        }
        return 0;
    }

    private int branchCall(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"CALL"};
        boolean isTerminal = true;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            return 0;
        }
        else{
            addNode(token[0],tokenType);
            if(endValue ==true)
                return 1;
                //endBranch(branch, token, endValue);
            else {
                return 1;
            }
        }

    }

    private int branchVar(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"VAR"};
        boolean isTerminal = true;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            return 0;
        }
        else{
            addNode(token[0],tokenType);
            if(endValue==true)
                return 1;
                //endBranch(branch,token, endValue);
            else{
                return 1;
            }
        }

    }

    private int branchAssign(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"ASSIGN"};
        boolean isTerminal = true;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            return 0;
        }
        else{

            String[] mainToken = new String[3];
            /*****************SET OUT BRANCHes****************/
            List<String[]> branchT = new ArrayList<String[]>();
            int getBack = branchVar(branchT,token,tokenType, false);
            mainToken[0] =  setBranchToString(branchT);
            /*****************SET OUT BRANCHes****************/
            myTree.rollBack(branchName[0]);
            incrementIndex();

            if( Index >= Tokenlist.size()) {
                this.endBranch = true;
                return 1;
            }

            List<Object> next = Tokenlist.get(Index);
            String[] tokenT = new String[]{(String) next.get(2)};
            String tokenTypeT = (String) next.get(1);

            if((tokenT[0].equals("=")) && (tokenTypeT.equals("Assignment operator"))){
                addNode(tokenT[0],tokenTypeT);

                incrementIndex();
                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                if(tokenTypeT.equals("Short String")){
                    //System.out.println(getBack);
                    addNode(tokenT[0],tokenTypeT);

                    if(endValue==true)
                        return 1;
                        //endBranch(branch,mainToken,endValue);
                    else
                        return 1;

                }else if(tokenTypeT.equals("User-defined name")){
                    /*****************SET OUT BRANCHes****************/
                    branchT = new ArrayList<String[]>();
                    getBack = branchVar(branchT,tokenT,tokenTypeT,false);
                    mainToken[2] =  setBranchToString(branchT);
                    /*****************SET OUT BRANCHes****************/
                    myTree.rollBack(branchName[0]);

                    //System.out.println(getBack);
                    if(endValue==true)
                        return 1;
                        //endBranch(branch,mainToken,endValue);
                    else
                        return 1;
                }
                else if((tokenTypeT.equals("Integer")) || (tokenTypeT.equals("Number operators"))){
                    /*****************SET OUT BRANCHes****************/
                    branchT = new ArrayList<String[]>();
                    getBack = branchNumexpr(branchT,tokenT,tokenTypeT,false);
                    mainToken[2] =  setBranchToString(branchT);
                    /*****************SET OUT BRANCHes****************/
                    myTree.rollBack(branchName[0]);

                    //System.out.println(getBack);
                    if(endValue==true)
                        return 1;
                        //endBranch(branch,mainToken,endValue);
                    else
                        return 1;
                }
            }
            else{
                showError("Expected : Assignment operator '=' : " + tokenT[0] + " found instead");
            }
        }
        return 0;
    }

    private int branchNumexpr(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"NUMEXPR"};
        boolean isTerminal = false;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            if(tokenType.equals("User-defined name")){
                int getBack = branchVar(branch,token,tokenType, endValue);
            }
            else if(tokenType.equals("Number operators")){
                int getBack = branchCalc(branch,token,tokenType, endValue);
            }
            else if(tokenType.equals("Integer")){
                addNode(token[0],tokenType);
                if(endValue==true)
                    return 1;
                    //endBranch(branch,token,endValue);
                else
                    return 1;
            }
            else {
                showError("Expected : Type Numexpr: " + token[0] + " found instead");
            }
        }
        else{
            //endBranch(branch,token,endValue);
        }
        return 0;
    }



    private int branchCalc(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"CALC"};
        boolean isTerminal = true;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            return 0;
        }
        else{
            String[] mainToken = new String[6];
            addNode(token[0],tokenType);
            incrementIndex();
            List<Object> next = Tokenlist.get(Index);
            String[] tokenT = new String[]{(String) next.get(2)};
            String tokenTypeT = (String) next.get(1);

            if((tokenT[0].equals("(")) && (tokenTypeT.equals("Grouping symbols"))){
                addNode(tokenT[0],tokenTypeT);
                incrementIndex();
                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                /*****************SET OUT BRANCHes****************/
                List<String[]> branchT = new ArrayList<String[]>();
                int getBack = branchNumexpr(branchT,tokenT,tokenTypeT,false);
                mainToken[2] = setBranchToString(branchT);
                /*****************SET OUT BRANCHes****************/
                myTree.rollBack(branchName[0]);
                incrementIndex();

                if( Index >= Tokenlist.size()) {
                    this.endBranch = true;
                    return 1;
                }

                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                if((tokenT[0].equals(",")) && (tokenTypeT.equals("Grouping symbols"))){
                    addNode(tokenT[0],tokenTypeT);

                    incrementIndex();
                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    /*****************SET OUT BRANCHes****************/
                    branchT = new ArrayList<String[]>();
                    getBack = branchNumexpr(branchT,tokenT,tokenTypeT,false);
                    mainToken[4] = setBranchToString(branchT);
                    /*****************SET OUT BRANCHes****************/
                    myTree.rollBack(branchName[0]);

                    if( Index >= Tokenlist.size()) {
                        this.endBranch = true;
                        return 1;
                    }

                    incrementIndex();
                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    if((tokenT[0].equals(")")) && (tokenTypeT.equals("Grouping symbols"))) {
                        //System.out.println(getBack);
                        addNode(tokenT[0],tokenTypeT);

                        if(endValue==true)
                            return 1;
                            //endBranch(branch,mainToken,endValue);
                        else
                            return 1;
                    }
                    else{
                        showError("Expected : Grouping symbol ')' : " + tokenT[0] + " found instead");
                    }
                }
                else{
                    showError("Expected : Grouping symbol ',' : " + tokenT[0] + " found instead");
                }
            }
            else{
                showError("Expected : Grouping symbol '(' : " + tokenT[0] + " found instead");
            }
        }
        return 0;
    }

    private int branchCond_Branch(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"COND_BRANCH"};
        boolean isTerminal = true;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            return 0;
        }
        else{
            String[] mainToken = new String[12];
            addNode(token[0],tokenType);
            incrementIndex();
            List<Object> next = Tokenlist.get(Index);
            String[] tokenT = new String[]{(String) next.get(2)};
            String tokenTypeT = (String) next.get(1);

            if((tokenT[0].equals("(")) && (tokenTypeT.equals("Grouping symbols"))){
                addNode(tokenT[0],tokenTypeT);
                incrementIndex();
                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                /*****************SET OUT BRANCHes****************/
                List<String[]> branchT = new ArrayList<String[]>();
                int getBack = branchCond_Bool(branchT,tokenT,tokenTypeT,false);
                //mainToken[2] = setBranchToString(branchT);
                /*****************SET OUT BRANCHes****************/
                myTree.rollBack(branchName[0]);
                incrementIndex();

                if( Index >= Tokenlist.size()) {
                    this.endBranch = true;
                    return 1;
                }

                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                //System.out.println(tokenT[0] + " : " + tokenTypeT);
                if((tokenT[0].equals(")")) && (tokenTypeT.equals("Grouping symbols"))){
                    //System.out.println("Great Stuff");
                    addNode(tokenT[0],tokenTypeT);
                    incrementIndex();
                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    //System.out.println(tokenT[0] + " : " + tokenTypeT);
                    if((tokenT[0].equals("then")) && (tokenTypeT.equals("Control structure"))) {
                        //System.out.println("Great Stuff 2");
                        addNode(tokenT[0],tokenTypeT);
                        incrementIndex();
                        next = Tokenlist.get(Index);
                        tokenT = new String[]{(String) next.get(2)};
                        tokenTypeT = (String) next.get(1);

                        if((tokenT[0].equals("{")) && (tokenTypeT.equals("Grouping symbols"))) {
                            addNode(tokenT[0],tokenTypeT);
                            incrementIndex();
                            next = Tokenlist.get(Index);
                            tokenT = new String[]{(String) next.get(2)};
                            tokenTypeT = (String) next.get(1);

                            /*****************SET OUT BRANCHes****************/
                            branchT = new ArrayList<String[]>();
                            getBack = branchCode(branchT,tokenT,tokenTypeT,false);
                            mainToken[8] = setBranchToString(branchT);
                            /*****************SET OUT BRANCHes****************/
                            myTree.rollBack(branchName[0]);
                            incrementIndex();

                            if( Index >= Tokenlist.size()) {
                                this.endBranch = true;
                                return 1;
                            }

                            next = Tokenlist.get(Index);
                            tokenT = new String[]{(String) next.get(2)};
                            tokenTypeT = (String) next.get(1);


                            if((tokenT[0].equals("}")) && (tokenTypeT.equals("Grouping symbols"))) {

                                addNode(tokenT[0],tokenTypeT);
                                incrementIndex();

                                if( Index >= Tokenlist.size()) {
                                    Index = Index -1;
                                }

                                next = Tokenlist.get(Index);
                                tokenT = new String[]{(String) next.get(2)};
                                tokenTypeT = (String) next.get(1);

                                if((tokenT[0].equals("else")) && (tokenTypeT.equals("Control structure"))) {
                                    addNode(tokenT[0],tokenTypeT);
                                    incrementIndex();
                                    next = Tokenlist.get(Index);
                                    tokenT = new String[]{(String) next.get(2)};
                                    tokenTypeT = (String) next.get(1);

                                    if((tokenT[0].equals("{")) && (tokenTypeT.equals("Grouping symbols"))) {
                                        addNode(tokenT[0],tokenTypeT);
                                        incrementIndex();
                                        next = Tokenlist.get(Index);
                                        tokenT = new String[]{(String) next.get(2)};
                                        tokenTypeT = (String) next.get(1);

                                        /*****************SET OUT BRANCHes****************/
                                        branchT = new ArrayList<String[]>();
                                        getBack = branchCode(branchT,tokenT,tokenTypeT,false);
                                        //mainToken[12] = setBranchToString(branchT);
                                        /*****************SET OUT BRANCHes****************/
                                        myTree.rollBack(branchName[0]);
                                        incrementIndex();

                                        if( Index >= Tokenlist.size()) {
                                            this.endBranch = true;
                                            return 1;
                                        }

                                        next = Tokenlist.get(Index);
                                        tokenT = new String[]{(String) next.get(2)};
                                        tokenTypeT = (String) next.get(1);

                                        if((tokenT[0].equals("}")) && (tokenTypeT.equals("Grouping symbols"))) {
                                            //System.out.println(getBack);
                                            addNode(tokenT[0],tokenTypeT);
                                            if(endValue==true)
                                                return 1;
                                                //endBranch(branch,mainToken,endValue);
                                            else
                                                return 1;

                                        }
                                        else{
                                            showError("Expected : Grouping symbol '}' : " + tokenT[0] + " found instead");
                                        }
                                    }
                                    else{
                                        showError("Expected : Grouping symbol '{' : " + tokenT[0] + " found instead");
                                    }
                                }
                                else {
                                    //System.out.println(getBack);
                                    decrementIndex();
                                    if(endValue==true)
                                        return 1;
                                        //endBranch(branch, mainToken, endValue);
                                    else
                                        return 1;
                                }
                            }
                            else{
                                showError("Expected : Grouping symbol '}' : " + tokenT[0] + " found instead");
                            }
                        }
                        else{
                            showError("Expected : Grouping symbol '{' : " + tokenT[0] + " found instead");
                        }
                    }
                    else{
                        showError("Expected : 'then' : " + tokenT[0] + " found instead");
                    }
                }
                else{
                    showError("Expected : Grouping symbol ')' : " + tokenT[0] + " found instead");
                }
            }
            else{
                showError("Expected : Grouping symbol '(' : " + tokenT[0] + " found instead");
            }


        }
        return 0;
    }

    private int branchCond_Bool(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"BOOL"};
        boolean isTerminal = true;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            return 0;
        }
        else{
            String[] mainToken;
            String[] tokenT;
            String tokenTypeT;
            List<Object> next;

            if((token[0].equals("(")) && (tokenType.equals("Grouping symbols"))){ //(VAR < VAR)
                mainToken = new String[5];
                addNode(token[0],tokenType);
                incrementIndex();
                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                /*****************SET OUT BRANCHes****************/
                List<String[]> branchT = new ArrayList<String[]>();
                int  getBack = branchVar(branchT,tokenT,tokenTypeT,false);
                mainToken[1] =  setBranchToString(branchT);
                /*****************SET OUT BRANCHes****************/
                myTree.rollBack(branchName[0]);
                incrementIndex();

                if( Index >= Tokenlist.size()) {
                    this.endBranch = true;
                    return 1;
                }

                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                if( ((tokenT[0].equals("<")) || (tokenT[0].equals(">"))) && (tokenTypeT.equals("Comparison symbols"))) {
                    //endBranch(branch,tokenT);
                    addNode(tokenT[0],tokenTypeT);
                    incrementIndex();
                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    /*****************SET OUT BRANCHes****************/
                    branchT = new ArrayList<String[]>();
                    getBack = branchVar(branchT,tokenT,tokenTypeT,false);
                    mainToken[3] =  setBranchToString(branchT);
                    /*****************SET OUT BRANCHes****************/
                    myTree.rollBack(branchName[0]);
                    incrementIndex();

                    if( Index >= Tokenlist.size()) {
                        this.endBranch = true;
                        return 1;
                    }

                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    if((tokenT[0].equals(")")) && (tokenTypeT.equals("Grouping symbols"))) {
                        //System.out.println(getBack);
                        addNode(tokenT[0],tokenTypeT);
                        if(endValue==true)
                            return 1;
                            //endBranch(branch,mainToken,endValue);
                        else
                            return 1;
                    }
                    else {
                        showError("Expected : Grouping symbol ')' : " + tokenT[0] + " found instead");
                    }
                }
                else {
                    showError("Expected : Comparison symbol '<' OR '>' : " + tokenT[0] + " found instead");
                }
            }
            else if ((token[0].equals("eq")) && (tokenType.equals("Comparison symbols"))) { //EQ(VAR , VAR)
                mainToken = new String[6];
                int value = -1;

                addNode(token[0],tokenType);
                incrementIndex();
                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                int getBack;

                if((tokenT[0].equals("(")) && (tokenTypeT.equals("Grouping symbols"))) {
                    //endBranch(branch,tokenT);
                    addNode(tokenT[0],tokenTypeT);
                    incrementIndex();
                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);


                    if(tokenTypeT.equals("User-defined name")){
                        /*****************SET OUT BRANCHes****************/
                        List<String[]> branchT = new ArrayList<String[]>();
                        getBack = branchVar(branchT,tokenT,tokenTypeT,false);
                        mainToken[2] =  setBranchToString(branchT);
                        /*****************SET OUT BRANCHes****************/
                        myTree.rollBack(branchName[0]);

                        value = 0;
                    }
                    else if((tokenTypeT.equals("Integer")) || (tokenTypeT.equals("Number operators"))){
                        /*****************SET OUT BRANCHes****************/
                        List<String[]> branchT = new ArrayList<String[]>();
                        getBack = branchNumexpr(branchT,tokenT,tokenTypeT,false);
                        mainToken[2] =  setBranchToString(branchT);
                        /*****************SET OUT BRANCHes****************/
                        myTree.rollBack(branchName[0]);

                        value = 1;
                    }
                    else{
                        /*****************SET OUT BRANCHes****************/
                        List<String[]> branchT = new ArrayList<String[]>();
                        getBack = branchCond_Bool(branchT,tokenT,tokenTypeT,false);
                        mainToken[2] =  setBranchToString(branchT);
                        /*****************SET OUT BRANCHes****************/
                        myTree.rollBack(branchName[0]);

                        value = 2;
                    }

                    incrementIndex();

                    if( Index >= Tokenlist.size()) {
                        this.endBranch = true;
                        return 1;
                    }

                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    if((tokenT[0].equals(",")) && (tokenTypeT.equals("Grouping symbols"))) {
                        //endBranch(branch,tokenT);
                        addNode(tokenT[0],tokenTypeT);
                        incrementIndex();
                        next = Tokenlist.get(Index);
                        tokenT = new String[]{(String) next.get(2)};
                        tokenTypeT = (String) next.get(1);

                        if((tokenTypeT.equals("User-defined name")) && (value == 0)){
                            /*****************SET OUT BRANCHes****************/
                            List<String[]> branchT = new ArrayList<String[]>();
                            getBack = branchVar(branchT,tokenT,tokenTypeT,false);
                            mainToken[4] =  setBranchToString(branchT);
                            /*****************SET OUT BRANCHes****************/
                            myTree.rollBack(branchName[0]);

                        }
                        else if((tokenTypeT.equals("Integer") || (tokenTypeT.equals("Number operators"))) && (value == 1)){
                            /*****************SET OUT BRANCHes****************/
                            List<String[]> branchT = new ArrayList<String[]>();
                            getBack = branchNumexpr(branchT,tokenT,tokenTypeT,false);
                            mainToken[4] =  setBranchToString(branchT);
                            /*****************SET OUT BRANCHes****************/
                            myTree.rollBack(branchName[0]);
                        }
                        else{
                            /*****************SET OUT BRANCHes****************/
                            List<String[]> branchT = new ArrayList<String[]>();
                            getBack = branchCond_Bool(branchT,tokenT,tokenTypeT,false);
                            mainToken[4] =  setBranchToString(branchT);
                            /*****************SET OUT BRANCHes****************/
                            myTree.rollBack(branchName[0]);

                            if( Index >= Tokenlist.size()) {
                                this.endBranch = true;
                                return 1;
                            }
                        }

                        incrementIndex();

                        if( Index >= Tokenlist.size()) {
                            this.endBranch = true;
                            return 1;
                        }

                        next = Tokenlist.get(Index);
                        tokenT = new String[]{(String) next.get(2)};
                        tokenTypeT = (String) next.get(1);

                        if((tokenT[0].equals(")")) && (tokenTypeT.equals("Grouping symbols"))) {
                            addNode(tokenT[0],tokenTypeT);
                            //System.out.println(getBack);
                            if(endValue==true)
                                return 1;
                                //endBranch(branch,mainToken,endValue);
                            else
                                return 1;

                        }
                        else {
                            showError("Expected : Grouping symbol ')' : " + tokenT[0] + " found instead");
                        }
                    }else {
                        showError("Expected : Grouping symbol ',' : " + tokenT[0] + " found instead");
                    }
                }
                else {
                    showError("Expected : Grouping symbol '(' : " + tokenT[0] + " found instead");
                }
            }
            else if (((token[0].equals("and")) || (token[0].equals("or"))) && (tokenType.equals("Boolean operators"))) {
                mainToken = new String[6];
                int value = -1;

                addNode(token[0],tokenType);
                incrementIndex();
                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);

                if((tokenT[0].equals("(")) && (tokenTypeT.equals("Grouping symbols"))) {
                    addNode(tokenT[0],tokenTypeT);
                    incrementIndex();
                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);


                    /*****************SET OUT BRANCHes****************/
                    List<String[]> branchT = new ArrayList<String[]>();
                    int getBack = branchCond_Bool(branchT,tokenT,tokenTypeT,false);
                    mainToken[2] =  setBranchToString(branchT);
                    /*****************SET OUT BRANCHes****************/
                    myTree.rollBack(branchName[0]);
                    incrementIndex();

                    if( Index >= Tokenlist.size()) {
                        this.endBranch = true;
                        return 1;
                    }

                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    if((tokenT[0].equals(",")) && (tokenTypeT.equals("Grouping symbols"))) {
                        addNode(tokenT[0],tokenTypeT);
                        incrementIndex();
                        next = Tokenlist.get(Index);
                        tokenT = new String[]{(String) next.get(2)};
                        tokenTypeT = (String) next.get(1);


                        /*****************SET OUT BRANCHes****************/
                        branchT = new ArrayList<String[]>();
                        getBack = branchCond_Bool(branchT,tokenT,tokenTypeT,false);
                        mainToken[4] =  setBranchToString(branchT);
                        /*****************SET OUT BRANCHes****************/
                        myTree.rollBack(branchName[0]);
                        incrementIndex();

                        if( Index >= Tokenlist.size()) {
                            this.endBranch = true;
                            return 1;
                        }

                        next = Tokenlist.get(Index);
                        tokenT = new String[]{(String) next.get(2)};
                        tokenTypeT = (String) next.get(1);

                        if((tokenT[0].equals(")")) && (tokenTypeT.equals("Grouping symbols"))) {
                            addNode(tokenT[0],tokenTypeT);

                            //System.out.println(getBack);
                            if(endValue==true)
                                return 1;
                                //endBranch(branch,mainToken,endValue);
                            else
                                return 1;

                        }
                        else {
                            showError("Expected : Grouping symbol ')' : " + tokenT[0] + " found instead");
                        }
                    }else {
                        showError("Expected : Grouping symbol ',' : " + tokenT[0] + " found instead");
                    }
                }
                else {
                    showError("Expected : Grouping symbol '(' : " + tokenT[0] + " found instead");
                }
            }
            else if ((token[0].equals("not")) && (tokenType.equals("Boolean operators"))) {
                mainToken = new String[2];

                addNode(token[0],tokenType);
                incrementIndex();
                next = Tokenlist.get(Index);
                tokenT = new String[]{(String) next.get(2)};
                tokenTypeT = (String) next.get(1);


                /*****************SET OUT BRANCHes****************/
                List<String[]> branchT = new ArrayList<String[]>();
                int getBack = branchCond_Bool(branchT,tokenT,tokenTypeT,false);
                mainToken[1] =  setBranchToString(branchT);
                /*****************SET OUT BRANCHes****************/
                myTree.rollBack(branchName[0]);

                //System.out.println(getBack);
                if(endValue==true)
                    return 1;
                    //endBranch(branch,mainToken,endValue);
                else
                    return 1;

            }else{
                showError("Expected : Boolean Expresion");
            }
        }
        return 0;
    }

    private int branchCond_Loop(List<String[]> branch, String[] token, String tokenType, boolean endValue){
        String[] branchName = new String[]{"COND_LOOP"};
        boolean isTerminal = true;

        addDefaults(branchName[0], tokenType);

        if(isTerminal == false){
            return 0;
        }
        else{
            if(token[0].equals("while")) {
                String[] mainToken = new String[7];
                addNode(token[0],tokenType);

                incrementIndex();
                List<Object> next = Tokenlist.get(Index);
                String[] tokenT = new String[]{(String) next.get(2)};
                String tokenTypeT = (String) next.get(1);

                if ((tokenT[0].equals("(")) && (tokenTypeT.equals("Grouping symbols"))) {
                    //endBranch(branch,tokenT);
                    addNode(tokenT[0],tokenTypeT);
                    incrementIndex();
                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    /*****************SET OUT BRANCHes****************/
                    List<String[]> branchT = new ArrayList<String[]>();
                    int getBack = branchCond_Bool(branchT,tokenT,tokenTypeT,false);
                    mainToken[2] = setBranchToString(branchT);
                    /*****************SET OUT BRANCHes****************/
                    myTree.rollBack(branchName[0]);
                    incrementIndex();

                    if( Index >= Tokenlist.size()) {
                        this.endBranch = true;
                        return 1;
                    }

                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    if ((tokenT[0].equals(")")) && (tokenTypeT.equals("Grouping symbols"))) {
                        addNode(tokenT[0],tokenTypeT);
                        incrementIndex();
                        next = Tokenlist.get(Index);
                        tokenT = new String[]{(String) next.get(2)};
                        tokenTypeT = (String) next.get(1);

                        if ((tokenT[0].equals("{")) && (tokenTypeT.equals("Grouping symbols"))) {
                            addNode(tokenT[0],tokenTypeT);
                            incrementIndex();
                            next = Tokenlist.get(Index);
                            tokenT = new String[]{(String) next.get(2)};
                            tokenTypeT = (String) next.get(1);

                            /*****************SET OUT BRANCHes****************/
                            branchT = new ArrayList<String[]>();
                            getBack = branchCode(branchT,tokenT,tokenTypeT,false);
                            mainToken[5] = setBranchToString(branchT);
                            /*****************SET OUT BRANCHes****************/
                            myTree.rollBack(branchName[0]);
                            incrementIndex();

                            if( Index >= Tokenlist.size()) {
                                this.endBranch = true;
                                return 1;
                            }

                            next = Tokenlist.get(Index);
                            tokenT = new String[]{(String) next.get(2)};
                            tokenTypeT = (String) next.get(1);

                            if ((tokenT[0].equals("}")) && (tokenTypeT.equals("Grouping symbols"))) {
                                addNode(tokenT[0],tokenTypeT);
                                //System.out.println(getBack);
                                if(endValue==true)
                                    return 1;
                                    //endBranch(branch,mainToken,endValue);
                                else
                                    return 1;
                            }
                            else {
                                showError("Expected : Grouping symbol '}' : " + tokenT[0] + " found instead");
                            }
                        }
                        else {
                            showError("Expected : Grouping symbol '{' : " + tokenT[0] + " found instead");
                        }
                    } else {
                        showError("Expected : Grouping symbol ')' : " + tokenT[0] + " found instead");
                    }
                } else {
                    showError("Expected : Grouping symbol '(' : " + tokenT[0] + " found instead");
                }
            }
            else if(token[0].equals("for")){
                String[] mainToken = new String[7];
                addNode(token[0],tokenType);
                incrementIndex();
                List<Object> next = Tokenlist.get(Index);
                String[] tokenT = new String[]{(String) next.get(2)};
                String tokenTypeT = (String) next.get(1);

                if ((tokenT[0].equals("(")) && (tokenTypeT.equals("Grouping symbols"))) {
                    //endBranch(branch,tokenT);
                    addNode(tokenT[0],tokenTypeT);
                    incrementIndex();
                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    /*****************SET OUT BRANCHes****************/
                    List<String[]> branchT = new ArrayList<String[]>();
                    int getBack = branchVar(branchT,tokenT,tokenTypeT,false);
                    mainToken[2] = setBranchToString(branchT);
                    /*****************SET OUT BRANCHes****************/
                    myTree.rollBack(branchName[0]);
                    incrementIndex();

                    if( Index >= Tokenlist.size()) {
                        this.endBranch = true;
                        return 1;
                    }

                    next = Tokenlist.get(Index);
                    tokenT = new String[]{(String) next.get(2)};
                    tokenTypeT = (String) next.get(1);

                    if ((tokenT[0].equals("=")) && (tokenTypeT.equals("Assignment operator"))) {
                        addNode(tokenT[0],tokenTypeT);
                        incrementIndex();
                        next = Tokenlist.get(Index);
                        tokenT = new String[]{(String) next.get(2)};
                        tokenTypeT = (String) next.get(1);

                        if (tokenT[0].equals("0")) {
                            addNode(tokenT[0],tokenTypeT);
                            incrementIndex();
                            next = Tokenlist.get(Index);
                            tokenT = new String[]{(String) next.get(2)};
                            tokenTypeT = (String) next.get(1);

                            if (tokenT[0].equals(";")) {
                                addNode(tokenT[0],tokenTypeT);
                                incrementIndex();
                                next = Tokenlist.get(Index);
                                tokenT = new String[]{(String) next.get(2)};
                                tokenTypeT = (String) next.get(1);

                                /*****************SET OUT BRANCHes****************/
                                branchT = new ArrayList<String[]>();
                                getBack = branchVar(branchT,tokenT,tokenTypeT,false);
                                mainToken[6] = setBranchToString(branchT);
                                /*****************SET OUT BRANCHes****************/
                                myTree.rollBack(branchName[0]);

                                if( Index >= Tokenlist.size()) {
                                    this.endBranch = true;
                                    return 1;
                                }

                                incrementIndex();
                                next = Tokenlist.get(Index);
                                tokenT = new String[]{(String) next.get(2)};
                                tokenTypeT = (String) next.get(1);

                                if (tokenT[0].equals("<")) {
                                    addNode(tokenT[0],tokenTypeT);
                                    incrementIndex();
                                    next = Tokenlist.get(Index);
                                    tokenT = new String[]{(String) next.get(2)};
                                    tokenTypeT = (String) next.get(1);

                                    /*****************SET OUT BRANCHes****************/
                                    branchT = new ArrayList<String[]>();
                                    getBack = branchVar(branchT,tokenT,tokenTypeT,false);
                                    //mainToken[8] = setBranchToString(branchT);
                                    /*****************SET OUT BRANCHes****************/
                                    myTree.rollBack(branchName[0]);
                                    incrementIndex();

                                    if( Index >= Tokenlist.size()) {
                                        this.endBranch = true;
                                        return 1;
                                    }

                                    next = Tokenlist.get(Index);
                                    tokenT = new String[]{(String) next.get(2)};
                                    tokenTypeT = (String) next.get(1);

                                    if (tokenT[0].equals(";")) {
                                        addNode(tokenT[0],tokenTypeT);
                                        incrementIndex();
                                        next = Tokenlist.get(Index);
                                        tokenT = new String[]{(String) next.get(2)};
                                        tokenTypeT = (String) next.get(1);

                                        /*****************SET OUT BRANCHes****************/
                                        branchT = new ArrayList<String[]>();
                                        getBack = branchVar(branchT,tokenT,tokenTypeT,false);
                                        //mainToken[10] = setBranchToString(branchT);
                                        /*****************SET OUT BRANCHes****************/
                                        myTree.rollBack(branchName[0]);
                                        incrementIndex();

                                        if( Index >= Tokenlist.size()) {
                                            this.endBranch = true;
                                            return 1;
                                        }

                                        next = Tokenlist.get(Index);
                                        tokenT = new String[]{(String) next.get(2)};
                                        tokenTypeT = (String) next.get(1);

                                        if (tokenT[0].equals("=")) {
                                            addNode(tokenT[0],tokenTypeT);
                                            incrementIndex();
                                            next = Tokenlist.get(Index);
                                            tokenT = new String[]{(String) next.get(2)};
                                            tokenTypeT = (String) next.get(1);

                                            if (tokenT[0].equals("add")) {
                                                addNode(tokenT[0],tokenTypeT);
                                                incrementIndex();
                                                next = Tokenlist.get(Index);
                                                tokenT = new String[]{(String) next.get(2)};
                                                tokenTypeT = (String) next.get(1);

                                                if (tokenT[0].equals("(")) {
                                                    addNode(tokenT[0],tokenTypeT);
                                                    incrementIndex();
                                                    next = Tokenlist.get(Index);
                                                    tokenT = new String[]{(String) next.get(2)};
                                                    tokenTypeT = (String) next.get(1);

                                                    /*****************SET OUT BRANCHes****************/
                                                    branchT = new ArrayList<String[]>();
                                                    getBack = branchVar(branchT,tokenT,tokenTypeT,false);
                                                    //mainToken[14] = setBranchToString(branchT);
                                                    /*****************SET OUT BRANCHes****************/
                                                    myTree.rollBack(branchName[0]);
                                                    incrementIndex();

                                                    if( Index >= Tokenlist.size()) {
                                                        this.endBranch = true;
                                                        return 1;
                                                    }

                                                    next = Tokenlist.get(Index);
                                                    tokenT = new String[]{(String) next.get(2)};
                                                    tokenTypeT = (String) next.get(1);

                                                    if (tokenT[0].equals(",")) {
                                                        addNode(tokenT[0],tokenTypeT);
                                                        incrementIndex();
                                                        next = Tokenlist.get(Index);
                                                        tokenT = new String[]{(String) next.get(2)};
                                                        tokenTypeT = (String) next.get(1);

                                                        if (tokenT[0].equals("1")) {
                                                            addNode(tokenT[0],tokenTypeT);
                                                            incrementIndex();
                                                            next = Tokenlist.get(Index);
                                                            tokenT = new String[]{(String) next.get(2)};
                                                            tokenTypeT = (String) next.get(1);

                                                            if (tokenT[0].equals(")")) {
                                                                addNode(tokenT[0],tokenTypeT);
                                                                incrementIndex();
                                                                next = Tokenlist.get(Index);
                                                                tokenT = new String[]{(String) next.get(2)};
                                                                tokenTypeT = (String) next.get(1);

                                                                if (tokenT[0].equals(")")) {
                                                                    addNode(tokenT[0],tokenTypeT);
                                                                    incrementIndex();
                                                                    next = Tokenlist.get(Index);
                                                                    tokenT = new String[]{(String) next.get(2)};
                                                                    tokenTypeT = (String) next.get(1);

                                                                    if (tokenT[0].equals("{")) {
                                                                        addNode(tokenT[0],tokenTypeT);
                                                                        incrementIndex();
                                                                        next = Tokenlist.get(Index);
                                                                        tokenT = new String[]{(String) next.get(2)};
                                                                        tokenTypeT = (String) next.get(1);

                                                                        /*****************SET OUT BRANCHes****************/
                                                                        branchT = new ArrayList<String[]>();
                                                                        getBack = branchCode(branchT,tokenT,tokenTypeT,false);
                                                                        //mainToken[20] = setBranchToString(branchT);
                                                                        /*****************SET OUT BRANCHes****************/
                                                                        myTree.rollBack(branchName[0]);
                                                                        incrementIndex();

                                                                        if( Index >= Tokenlist.size()) {
                                                                            this.endBranch = true;
                                                                            return 1;
                                                                        }

                                                                        next = Tokenlist.get(Index);
                                                                        tokenT = new String[]{(String) next.get(2)};
                                                                        tokenTypeT = (String) next.get(1);

                                                                        if (tokenT[0].equals("}")) {
                                                                            addNode(tokenT[0],tokenTypeT);

                                                                            //System.out.println("Printing test array : FOR LOOL (23)");
                                                                            //printArray(mainToken);
                                                                            //System.out.println(getBack);
                                                                            if(endValue==true)
                                                                                return 1;
                                                                                //endBranch(branch,mainToken,endValue);
                                                                            else
                                                                                return 1;
                                                                        }
                                                                        else {
                                                                            showError("Expected : Grouping symbol '}' : " + tokenT[0] + " found instead");
                                                                        }
                                                                    }
                                                                    else {
                                                                        showError("Expected : Grouping symbol '{' : " + tokenT[0] + " found instead");
                                                                    }
                                                                }
                                                                else {
                                                                    showError("Expected : Grouping symbol ')' : " + tokenT[0] + " found instead");
                                                                }
                                                            }
                                                            else {
                                                                showError("Expected : Grouping symbol ')' : " + tokenT[0] + " found instead");
                                                            }
                                                        }
                                                        else {
                                                            showError("Expected : '1' : " + tokenT[0] + " found instead");
                                                        }
                                                    }
                                                    else {
                                                        showError("Expected : ',' : " + tokenT[0] + " found instead");
                                                    }
                                                }
                                                else {
                                                    showError("Expected : Grouping symbol '(' : " + tokenT[0] + " found instead");
                                                }
                                            }
                                            else {
                                                showError("Expected : 'add' : " + tokenT[0] + " found instead");
                                            }
                                        }
                                        else {
                                            showError("Expected : '=' : " + tokenT[0] + " found instead");
                                        }
                                    }
                                    else {
                                        showError("Expected : ';' : " + tokenT[0] + " found instead");
                                    }
                                }
                                else {
                                    showError("Expected : '<' : " + tokenT[0] + " found instead");
                                }
                            }
                            else {
                                showError("Expected : ';' : " + tokenT[0] + " found instead");
                            }
                        }
                        else {
                            showError("Expected : 0 (zero): " + tokenT[0] + " found instead");
                        }
                    }
                    else {
                        showError("Expected : Assignment operator '=' : " + tokenT[0] + " found instead");
                    }
                }
                else {
                    showError("Expected : Grouping symbol '(' : " + tokenT[0] + " found instead");
                }
            }
        }
        return 0;
    }




    /****************HELPER FUNCTIONS**********/
    /*private void //endBranch(List<String[]> branch, String[] token, boolean addValue) {

        /*branch.add(token);
        System.out.println("branch: " + printArray(token));
        for (int i = 0; i < branch.size(); i++) {
            System.out.print(printArray(branch.get(i)) + "--");
        }
        parserTree.add(branch);*

        if(addValue ==true) {
            incrementIndex();
            //addBranch(Index);
        }

        return;
    }*/

    private boolean incrementIndex() {
        if ((Index + 1) < Tokenlist.size()) {
            Index = Index + 1;
            return true;
        }
        else{
            this.endBranch = true;
            Index = Tokenlist.size();
            return false;
        }
    }

    private void decrementIndex() {
        if(this.endBranch == true){
            Index = Index;
        }
        else if ((Index - 1) >0) {
            Index = Index - 1;
        }
    }

    private void addDefaults(String s, String tokentype) {
        myTree.addNode(s,tokentype);
    }

    private void addNode(String s, String tokentype) {
        myTree.addTermNode(s,tokentype);
    }


    private void showError(String msg){
        myTree.reset();
        myTree.addTermNode(msg, "");
        Index = Tokenlist.size();
        System.out.println(Index + " : " + msg);
        this.endBranch = true;
        return;
    }

    private String setBranchToString(List<String[]> branch) {
        String output = "";
        for(int i = 0; i < branch.size(); i++){
            output = output + branch.get(i);
            if(i != branch.size()-1 )
                output = output + " - ";
        }
        return output;
    }

}
