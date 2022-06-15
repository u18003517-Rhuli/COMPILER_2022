package com.SpitTxt.Project_C;

import com.SpitTxt.Project_B.lexer2.Lexer.cLexer;
import com.SpitTxt.Project_B.lexer2.Lexer.cLinkedList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SpitTxt {

    //public static LinkedList<List<Object>> Tokenlist = new LinkedList<List<Object>>();

    public int AdminLayer = 6;
    private boolean FoundError;


    //public static void main(String[] args) throws IOException {
    SpitTxt(String filePath) throws IOException {

        this.FoundError = false;
        //Scanner input = new Scanner(new File(args[0])); // ("C:/Users/Rhuli/IdeaProjects/COS_341_COMPILER_2022/src/com/SpitTxt/Project_B/Test/Accept_1.2.txt"));
        Scanner input = new Scanner(new File(filePath)); //
        //"C:\\Users\\Rhuli\\IdeaProjects\\Compiler_SpitTxt\\src\\com\\SpitTxt\\Assignment2\\Test\\Accept_1.txt"

        ArrayList<String> Code = new ArrayList<>();

        while ((input.hasNext())){
            Code.add(input.nextLine());
        }


        /*************** BEGIN ********************/

        LinkedList<List<Object>> tokenlist = null;

        ParseTree parseTree = null;
        ParseTree pruneParseTree = null;

        HashMap<String,ArrayList<Node>> symbolTable= new HashMap<>();

        ArrayList<String> errorList = new ArrayList<>();

        /*************** PRINT OUT Lexer ********************/
        Lexer lexer = null;
        if(AdminLayer >= 1) {

            lexer = new Lexer(Code, errorList);
            lexer.begin();


            errorList = lexer.getErrorList();
            /********patch lexer fix***********/
            try {
                if(errorList.isEmpty() == true) {
                    cLexer lexer2nd = new cLexer(filePath);
                    cLinkedList linkedList = lexer2nd.start();

                    String lines = linkedList.toString();
                    String[] realLine = lines.split("\n");
                    ArrayList<String> newCalc = new ArrayList<>();

                    for(int i=0; i< realLine.length; i++){
                        String[] info = realLine[i].split("-");
                        String extractInfo = info[2].substring(0,info[2].length()-1);
                        System.out.println(extractInfo + " : BOGGGA BOOOGA");
                        newCalc.add(extractInfo);
                    }


                    ArrayList<String> lineCount = lexer.getLineCount();
                    lexer = new Lexer(newCalc,lineCount, errorList);

                    //ArrayList<String> lexer2ndList = new ArrayList<>();
                    //lexer2ndList.add(lines);

                    //printMainFile(lexer2ndList, "1_Lexer_2nd");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            tokenlist = lexer.getTokenlist();

            /********patch lexer fix***********/


            Iterator<List<Object>> iterator = tokenlist.iterator();
            ArrayList<String> lexerList = new ArrayList<>();
            while (iterator.hasNext())
                lexerList.add(String.valueOf(iterator.next()));

            printMainFile(lexerList, "1_Lexer");

            errorList = lexer.getErrorList();

            if(AdminLayer == 1){
                printErrors(errorList);
            }else if(errorList.isEmpty() == false){
                System.out.println("BRO CHECK" + errorList.get(0));
                printErrors(errorList);
                FoundError = true;
            }
        }

        /*************** PRINT OUT SyntaxAnalyser ********************/
        SyntaxAnalyserSPL syntaxAnalyser = null;
        if(AdminLayer >= 2 ) {
            syntaxAnalyser =  new SyntaxAnalyserSPL(tokenlist,errorList);

            if(FoundError == false) {
                syntaxAnalyser.begin();
            }

            parseTree = syntaxAnalyser.getParseTree();
            ArrayList<String> syntaxList = parseTree.printTree();
            printMainFile(syntaxList, "2_SyntaxAnalyser");

            //pruneParseTree =  syntaxAnalyser.getPrunedParseTree();
            //syntaxList = pruneParseTree.printTree();
            //printMainFile(syntaxList, "2_SyntaxAnalyser_Pruned");

            pruneParseTree = new ParseTree(parseTree);
            pruneParseTree.prune();
            syntaxList = pruneParseTree.printTree();
            printMainFile(syntaxList, "2_SyntaxAnalyser_Pruned");


            /*parseTree = syntaxAnalyser.getParseTree();
            ParseList = parseTree.printTree();
            printMainFile(ParseList, "2_SyntaxAnalyser_Copy");

            ParseTree doubePruneParseTree =  pruneParseTree;
            doubePruneParseTree.prune();
            ParseList = doubePruneParseTree .printTree();
            printMainFile(ParseList, "2_SyntaxAnalyser_Pruned_Double");*/

            errorList = syntaxAnalyser.getErrorList();
            if(AdminLayer == 2){
                printErrors(errorList);
            }else if(errorList.isEmpty() == false){
                printErrors(errorList);
                FoundError = true;
            }


        }
        /**********************SET NODE ID's***********************

        parseTree.setIDs();

        /**************** PRINT OUT Scoper ****************/
        Scoper scoper = null;
        ArrayList<String> scopeList = null;
        if(AdminLayer >= 3) {
            scoper = new Scoper(parseTree, tokenlist); //TODO: MAIN MAN

            if(FoundError == false) {
                scoper.begin();
            }

            symbolTable = scoper.getSymbolTable();

            scopeList = parseTree.printTree();
            printMainFile(scopeList, "3_Scopes_found");

            //pruneParseTree = parseTree;
            //pruneParseTree.prune();
            //scopeList = scoper.printTree();

            if(AdminLayer == 3){
                printErrors(errorList);
            }else if(errorList.isEmpty() == false){
                printErrors(errorList);
                FoundError = true;
            }
        }

        /**************** PRINT OUT Renamed Tree ****************/
        NameRegulatorSPL nameRegulator =  null; //TODO: MAIN MAN
        ArrayList<String> nameList = null;
        if(AdminLayer >= 4) {
            nameRegulator = new NameRegulatorSPL(parseTree,symbolTable, errorList);
            if(FoundError == false) {
                nameRegulator.begin();
            }

            nameList = parseTree.printTree();
            printMainFile(nameList, "4_RegulatedNames_found");

            pruneParseTree = new ParseTree(parseTree);
            pruneParseTree.prune();
            nameList = pruneParseTree.printTree();
            printMainFile(nameList, "4_RegulatedNames_found_Pruned");

            printSymbolTable(symbolTable);

            errorList = nameRegulator.getErrorList();
            if(AdminLayer == 4){
                printErrors(errorList);
            }else if(errorList.isEmpty() == false){
                printErrors(errorList);
                FoundError = true;
            }
        }



        /**************** PRINT OUT Type Checked Tree ****************/
        TypeChecker typeChecker =  null; //TODO: MAIN MAN
        ArrayList<String> typeList = null;
        if(AdminLayer >= 5) {
            typeChecker = new TypeChecker( parseTree,symbolTable, errorList);
            if(FoundError == false) {
                typeChecker.begin();
            }

            typeList = parseTree.printTree();
            printMainFile(typeList, "5_TypeChecked_found");



            errorList = typeChecker.getErrorList();
            if(AdminLayer == 5){
                printErrors(errorList);
            }else if(errorList.isEmpty() == false){
                printErrors(errorList);
                FoundError = true;
            }
        }


        /**************** PRINT OUT Generated Code ****************/

        CodeGenerator codeGenerator =  null; //TODO: MAIN MAN
        ArrayList<String> codeList = null;
        if(AdminLayer >= 6) {
            codeGenerator = new CodeGenerator( parseTree,symbolTable, errorList);
            if(FoundError == false) {
                codeGenerator.begin();
            }

            //typeList = parseTree.printTree();
            codeList = codeGenerator.getGeneratedCode();
            printMainFile(codeList, "6_Generated_Code");



            errorList = codeGenerator.getErrorList();
            if(AdminLayer == 6){
                printErrors(errorList);
            }else if(errorList.isEmpty() == false){
                printErrors(errorList);
                FoundError = true;
            }
        }



    }

    private void printErrors(ArrayList<String> errorList) {

        /*ArrayList<String> output = new ArrayList<>();


        for (String value: symbolTable.keySet()) {

            ArrayList<Node> nodeList = symbolTable.get(value);

            String line = value;
            line = line + ":[";

            if(nodeList != null) {
                for (int j = 0; j < nodeList.size(); j++) {
                    line = line +"\n" + nodeList.get(j).printNode();
                }
            }

            line = line + "\n" + "]" + "\n";
            output.add(line);

        }*/

        if(errorList.isEmpty() == true){
            errorList.add("Success. No Errors were found in compilation.");
        }


        printMainFile(errorList, "CompiledError(s)");
    }


    private  void printMainFile(ArrayList<String> List, String fileName){

        try {
            //File fileO = new File(System.getProperty("user.dir"), "output.txt");

            File fileO = new File("C:/Users/Rhuli/IdeaProjects/COS_341_COMPILER_2022/src/com/SpitTxt/Project_C/Output",fileName + ".txt");
            //File fileO = new File("Output",fileName + ".txt");

            //System.out.println("RO CHECK2 : " + FoundError);
            if (FoundError == true && fileName.equals("CompiledError(s)") == false){
                List = new ArrayList<>();
            }

            FileWriter output = new FileWriter(fileO);

            //output.write("Number of ID \n");

            for(int i = 0; i < List.size(); i++){
                System.out.println(List.get(i));
                output.write(List.get(i));
                output.write("\n");
            }

            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printSymbolTable(HashMap<String,ArrayList<Node>> symbolTable){
        ArrayList<String> output = new ArrayList<>();


        for (String value: symbolTable.keySet()) {

            ArrayList<Node> nodeList = symbolTable.get(value);

            String line = value;
            line = line + ":[";

            if(nodeList != null) {
                for (int j = 0; j < nodeList.size(); j++) {
                    line = line +"\n" + nodeList.get(j).printNode();
                }
            }

            line = line + "\n" + "]" + "\n";
            output.add(line);

        }


        printMainFile(output, "_SymbolTable");
    }


}
