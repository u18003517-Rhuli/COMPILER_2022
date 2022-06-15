package com.SpitTxt.Project_A;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SpitTxt {

    //public static LinkedList<List<Object>> Tokenlist = new LinkedList<List<Object>>();

    public static int AdminLayer = 2;


    public static void main(String[] args) throws IOException {
        //Scanner input = new Scanner(new File(args[0])); // "C:\Users\Rhuli\IdeaProjects\Compiler_SpitTxt\src\com\SpitTxt\Assignment1\Test\accept-1.txt"
        Scanner input = new Scanner(new File("C:/Users/Rhuli/IdeaProjects/COS_341_COMPILER_2022/src/com/SpitTxt/Project_A/Test/Accept_1.1.txt"));
        //"C:\\Users\\Rhuli\\IdeaProjects\\Compiler_SpitTxt\\src\\com\\SpitTxt\\Assignment2\\Test\\Accept_1.txt"

        ArrayList<String> Code = new ArrayList<>();

        while ((input.hasNext())){
            Code.add(input.nextLine());
        }

        /*while ((input.hasNext()) && (ErrorFound == false)){
            String line = input.nextLine();
            CurrentLine = CurrentLine + 1;

            while (!line.isEmpty() && (ErrorFound == false))
                line = AnalyseLine(line); //TODO: MAIN MAN
        }*/

        /*************** PRINT OUT Lexer ********************/

        LinkedList<List<Object>> Tokenlist = null;

        ParseTree parseTree = null;
        ParseTree pruneParseTree = null;

        //HashMap<String,List<Object>> SymbolTable= new HashMap<>();

        Lexer lexer = new Lexer(Code);

        if(AdminLayer >= 1) {

            lexer.begin();

            Tokenlist = lexer.getTokenlist();
            Iterator<List<Object>> iterator = Tokenlist.iterator();
            ArrayList<String> lexerList = new ArrayList<>();

            while (iterator.hasNext())
                lexerList.add(String.valueOf(iterator.next()));

            printMainFile(lexerList, "1_Lexer");
        }

        /*************** PRINT OUT SyntaxAnalyser ********************/
        SyntaxAnalyserSPL syntaxAnalyser =  new SyntaxAnalyserSPL(Tokenlist);
        if(AdminLayer >= 2) {
            //analyser = new SyntaxAnalyser(Tokenlist); //TODO: MAIN MAN
            syntaxAnalyser.begin();

            parseTree = syntaxAnalyser.getParseTree();
            ArrayList<String> ParseList = parseTree.printTree();
            printMainFile(ParseList, "2_SyntaxAnalyser");

            pruneParseTree =  syntaxAnalyser.getPrunedParseTree();
            ParseList = pruneParseTree.printTree();
            printMainFile(ParseList, "2_SyntaxAnalyser_Pruned");

            /*parseTree = syntaxAnalyser.getParseTree();
            ParseList = parseTree.printTree();
            printMainFile(ParseList, "2_SyntaxAnalyser_Copy");

            ParseTree doubePruneParseTree =  pruneParseTree;
            doubePruneParseTree.prune();
            ParseList = doubePruneParseTree .printTree();
            printMainFile(ParseList, "2_SyntaxAnalyser_Pruned_Double");*/


        }

        /**************** PRINT OUT Scoper ****************
        Scoper scoper = null;
        ArrayList<String> scopeList = null;
        if(AdminLayer >= 3) {
            //ParseTree ParseTree = analyser.getParseTree();
            scoper = new Scoper(parseTree); //TODO: MAIN MAN

            //pruneParseTree = parseTree;
            //pruneParseTree.prune();
            scopeList = scoper.printTree();
            SymbolTable = scoper.getSymbolTable();
            //scopeList = scoper.printTree();

            //printMainFile(scopeList);
        }

        /**************** PRINT OUT Renamed Tree ****************
        if(AdminLayer >= 5) {
            scoper.nameRegulateTree(); //TODO: MAIN MAN
            //scopeList = scoper.printTree();

            //pruneParseTree = parseTree;
            //pruneParseTree.prune();
            //scopeList = pruneParseTree.printTree(); ;

            //printMainFile(scopeList);
        }

        /**************** PRINT OUT Renamed Tree ****************
        if(AdminLayer >= 5) {
            scoper.nameRegulateTree(); //TODO: MAIN MAN
            scopeList = scoper.printTree();

            printMainFile(scopeList);
        }*

        if(AdminLayer >= 6) {
            TypeInferencer typeinfer = new TypeInferencer( parseTree, SymbolTable);//TODO: MAIN MAN

            typeinfer.start();
                    //scopeList = scoper.printTree();

            //pruneParseTree = parseTree;
            //pruneParseTree.prune();
            scopeList = scoper.printTree();//pruneParseTree.printTree(); ;

            printMainFile(scopeList);
        }
        */


    }



    private static void printMainFile(ArrayList<String> List, String fileName){
        try {
            //File fileO = new File(System.getProperty("user.dir"), "output.txt");

            File fileO = new File("C:/Users/Rhuli/IdeaProjects/COS_341_COMPILER_2022/src/com/SpitTxt/Project_A/Output",fileName + ".txt");

            FileWriter output = new FileWriter(fileO);

            output.write("Number of ID \n");

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


}
