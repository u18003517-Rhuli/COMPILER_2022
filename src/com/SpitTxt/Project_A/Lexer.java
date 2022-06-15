package com.SpitTxt.Project_A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class Lexer {

    private LinkedList<List<Object>> Tokenlist = new LinkedList<List<Object>>();
    private int TokenCounter = 0;
    private int CurrentLine = 0;
    private int AdminLayer = 1;
    private boolean ErrorFound = false;
    private ArrayList<String> Code;
    private ArrayList<String> words = new ArrayList<>() ;
    private ArrayList<String> lineCount = new ArrayList<>();

    private String isQuote = "";


    private Pattern numberPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private Pattern userVarPattern = Pattern.compile("^[a-zA-Z0-9]*$");

    private List<String> checkTokens =  Arrays.asList(
            /*"eq", "<",">",
            "and", "or", "not",
            "add", "sub", "mult",
            "(", ")", "{", "}", ",", ";",
            ":", "=",
            "if", "then", "else", "while", "for",
            "input", "output",
            "halt",
            "proc",*/

        "main", "halt","proc",
        "return","call","true","false",
        "{", "}","(", ")", ",", ";","[","]",":=",
        "if", "then", "else", "while", "until","do",
        "input", "output",
        "not","and", "or", "eq",
        "larger","add", "sub", "mult",
        "arr", "num", "bool", "string"
    );


    public Lexer(ArrayList<String> Code){
        this.Code = Code;
    }

    public LinkedList<List<Object>> getTokenlist(){
        return Tokenlist;
    }

    public void begin(){
        while ((CurrentLine < Code.size() ) ){ //&& (ErrorFound == false)
            String line =  Code.get(CurrentLine);// input.nextLine();
            CurrentLine = CurrentLine + 1;
            makeLineWork(line);

            //while (!line.isEmpty() && (ErrorFound == false))
                //line =  makeLineWork(line); //TODO: MAIN MAN
        }

        for(int i =0; i < words.size(); i++) {
            //System.out.println(words.get(i));

            //System.out.println("");
            //System.out.println(words.get(i));

            ArrayList<String> list = scanWord(words.get(i));
            fixLineCount(i,list.size());


            //System.out.println("");
            //for(int j=0; j < list.size(); j++){
                //System.out.println(list.get(j));
            //}

            for(int j=0; j < list.size(); j++){
                //createToken(list.get(j));
                analyseToken(list.get(j));
            }
        }
    }

    private void fixLineCount(int index, int size) {
        ArrayList<String> newLineCount = new ArrayList<>();
        for (int i=0; i < lineCount.size(); i++){
            if(i == index){
                for(int j =0; j < size; j++){
                    newLineCount.add(lineCount.get(i));
                }
            }else{
                newLineCount.add(lineCount.get(i));
            }
        }

        lineCount = newLineCount;
    }


    private String makeLineWork(String line){

        if(line.isEmpty() && (isQuote.isEmpty() == true) )
            return "";

        line = RemoveTrailSpaces(line);

        if(words.isEmpty() == true) {
            words = getWords(line);
            for(int i=0; i<words.size(); i++){
                lineCount.add(String.valueOf(CurrentLine));
            }
        }
        else {
            ArrayList<String> temp = getWords(line);
            for(int i =0; i < temp.size(); i++) {
                words.add(temp.get(i));
                lineCount.add(String.valueOf(CurrentLine));
            }
        }

        /*for(int i =0; i < words.size(); i++) {
            if(numberOfQuote(words.get(i)) == 1){
                int index = i +1;
                while(numberOfQuote(words.get(index)) != 1){
                    index = index+1;
                }
            }
        }*/

        return "";
    }

    private ArrayList<String> scanWord(String word) {

        System.out.println("SCANNING");

        ArrayList<String> output = new ArrayList<>();
        boolean wordChanged = false;
        boolean wordSplit = false;

        int index =0;
        for(; index < word.length(); index++){
        //while( (word.isEmpty() == false) && (index<word.length())){

            //System.out.println("WORD!!!! - " + word + " " + index);

            String foundCheck = "" + word.charAt(index); //start in middle

            for(int i = 0; i < word.length(); i++ ){
                int right = index + i ;
                int left = index - i;

                if( left < 0) {
                    break;
                }

                if( (right >= word.length() ) ){
                    //right = s.length() -1;
                    break;
                }


                if(checkTokens.contains(foundCheck)){

                    wordSplit = true;

                    String firstPart= word.substring(0,word.indexOf(foundCheck.charAt(0)));
                    if(firstPart.isEmpty() == false) {
                        output.add(firstPart);
                        //System.out.println("1) First part : " + firstPart);
                    }

                    //second part {main part}
                    output.add(foundCheck);
                    //System.out.println("1) Found : " + foundCheck);


                    String lastPart= word.substring(word.indexOf(foundCheck.charAt(foundCheck.length() -1)) +1, word.length());

                    //System.out.println("1) Last past : " + lastPart);

                    //if(lastPart.isEmpty() == false) {
                        //output.add(lastPart);
                        word = lastPart;
                        //index = 0;
                        //wordChanged = true;
                    //}

                    break;

                }else{
                    foundCheck = word.charAt(left) + foundCheck + word.charAt(right);
                }
            }

            //TODO: HARD RESET
            //if( (index ==0) && (output.contains(foundCheck)) )
            //    break;

            if(wordSplit == true){
                wordSplit = false;
                index =-1;
                continue;
            }

            //OTHER CHECk

            foundCheck = "";

            for(int i = 0; i < word.length(); i++ ){
                int right = (index+1) + i ;
                int left = index - i;

                if( (right >= word.length() ) ){
                    //right = s.length() -1;
                    break;
                }

                if(i==0){
                    foundCheck =  word.charAt(index) + foundCheck + word.charAt(right);
                }

                if( left < 0) {
                    break;
                }

                if(checkTokens.contains(foundCheck)){
                    wordSplit = true;

                    String firstPart= word.substring(0,word.indexOf(foundCheck.charAt(0)));
                    if(firstPart.isEmpty() == false) {
                        output.add(firstPart);
                        //System.out.println("2) First part : " + firstPart);
                    }


                    output.add(foundCheck);
                    //System.out.println("2) Found : " + foundCheck);

                    String lastPart= word.substring(word.indexOf(foundCheck.charAt(foundCheck.length() -1 )) +1, word.length());

                    //System.out.println("2) Last past : " + lastPart);

                    //if(lastPart.isEmpty() == false) {
                        //output.add(lastPart);
                        word = lastPart;
                        //index = 0;
                        //wordChanged = true;
                    //}

                    break;
                }else{
                    foundCheck = word.charAt(left) + foundCheck + word.charAt(right);
                }

            }

            //TODO: HARD RESET
            if(wordSplit == true){
                wordSplit = false;
                index =-1;
            }

        }

        if(wordChanged == true){
            output.add(word);
        }
        else if( (word.isEmpty() == false) && (output.isEmpty() == true) ){
            output.add(word);
        }

        return output;
    }


    private void analyseToken(String word) {

        //System.out.println("Token : " + word);
        //System.out.println(word.substring(0,1));

        int index = 0;
        String tokenType = "";
        String newWord = "";

        if (checkTokens.contains(word) ) {
            //List<Object> tokenInfo = null; //checkWord(word.substring(0,1));
            //tokenType = (String)tokenInfo.get(0);
            //newWord = (String)tokenInfo.get(1);
            //index = 0;
            createToken(word);
        }
        else if(word.charAt(0) == '"'){ //short string
            tokenType = "Short String";
            newWord = newWord + word.charAt(0);

            //System.out.println("Short string : " + word);

            for (int i = 1; i < word.length(); i++) {

                if(i > 16){
                    lexerErrorFound("short string too long (max 15)", word);
                    //return null;
                }

                if ( (word.charAt(i) == ' ') || ((word.charAt(i) >= 'A') && (word.charAt(i) <= 'Z')) || ((word.charAt(i) >= '0') && (word.charAt(i) <= '9'))) { //(word.charAt(i) == '\n')  ||
                    index = i;
                    newWord = newWord + word.charAt(i);
                }
                else if ((word.charAt(i) >= 'a') && (word.charAt(i)  <= 'z')){
                    lexerErrorFound("illegal character (Please check for uppercase)", word);
                    //return null;
                    break;
                }
                else if (word.charAt(i) == '"') {
                    index = i;
                    newWord = newWord + word.charAt(i);

                    if(newWord.length() > 15){
                        lexerErrorFound("short string too long (max 15)", word);
                        //return null;
                    }

                    break;
                }
                else{
                    lexerErrorFound("illegal character found",word);
                    break;
                    //return null;
                }

                /*else if(i > 10){
                    lexalErrorFound("String too long", newWord);
                    return null;
                }*/

                /*
                else if((i == 10) && (word.charAt(i) != '"')){ //careful
                    lexalErrorFound("String too long", word);
                    return null;
                }*/

            }


            if(newWord.equals(word)){
                createToken(word);
            }
        }
        else if( (((word.charAt(0) >= '0') && (word.charAt(0) <= '9')) || (word.charAt(0) >= '-')) && (numberPattern.matcher(String.valueOf(word.charAt(0))).matches()) ){ //integers (numberPattern.matcher(word.substring(1)).matches())
            // if(numberPattern.matcher(word).matches()){ //integers
            tokenType = "Integer";
            newWord = newWord + word.charAt(0);

            //if(word.charAt(0) == '0')
            //return Arrays.asList(tokenType, "0", 0);


            if((word.charAt(0) == '-') && (word.length() <= 1) ){
                lexerErrorFound("illegal character found", "-");
                //return null;
            }

            if( (word.charAt(0) == '-')  && (word.charAt(1) == '0') ){ //TODO || (word.charAt(0) == '+')
                lexerErrorFound("illegal character, Zero Carry sign", "(sign)0");
                //return null;
            }

            for(int i=1 ; i < word.length(); i++){
                if ((word.charAt(i) >= '0') && (word.charAt(i)  <= '9')) {
                    index = i;
                    newWord = newWord + word.charAt(i);
                }
                else {
                    lexerErrorFound("number variable found to be illegal", word); //String.valueOf(word.charAt(i)));
                    break;
                }
            }

            if(newWord.equals(word)){
                createToken(word);
            }
        }
        else if ((word.charAt(0) >= 'a') && (word.charAt(0)  <= 'z')  ){ //user-define (userVarPattern.matcher(word.substring(1)).matches())
            //if(userVarPattern.matcher(word).matches()){ //user define
            tokenType = "User-defined name";
            newWord = newWord + word.charAt(0);


            for(int i=1 ; i < word.length(); i++){
                if (((word.charAt(i) >= 'a') && (word.charAt(i)  <= 'z')) || ((word.charAt(i) >= '0') && (word.charAt(i)  <= '9')) || ((word.charAt(i) >= 'A') && (word.charAt(i)  <= 'Z'))) {
                    index = i;
                    newWord = newWord + word.charAt(i);
                }else{
                    lexerErrorFound("user-defined variable found to be illegal", word); //String.valueOf(word.charAt(i)));
                    break;
                }
                /*else if ((word.charAt(0) >= 'A') && (word.charAt(0)  <= 'Z')){
                    lexalErrorFound("illegal character", newWord + word.charAt(i));
                    return null;
                }
                else {
                    if (checkTokens.contains(newWord)){ //(checkWord(newWord) != null) {
                        List<Object> tokenInfo = null;//checkWord(newWord);
                        tokenType = (String)tokenInfo.get(0);
                        newWord = (String)tokenInfo.get(1);
                    }
                    break;
                }*/
            }

            if(newWord.equals(word)){
                createToken(word);
            }
        }
        /*else if ((word.charAt(0) >= 'A') && (word.charAt(0)  <= 'Z')){
            lexerErrorFound("illegal character", word);
            //return null;
        }*/
        else{
            lexerErrorFound("illegal character found", word);
            //return null;
        }


        //return Arrays.asList(tokenType, newWord , index);
    }

    private  void createToken(String word) {

        if(ErrorFound == true){
            return;
        }

        TokenCounter = TokenCounter +1;
        //List<Object> finalTokenInfo =  Arrays.asList( (int) TokenCounter, (String) tokenInfo.get(0), (String) tokenInfo.get(1));

        ArrayList<String> info = new ArrayList<>();
        info.add("TokenCount - " + TokenCounter);


        if(lineCount.size() >TokenCounter -1 )
        info.add("LineCount - " + lineCount.get(TokenCounter -1));


        List<Object> finalTokenInfo =  Arrays.asList( (String) word, (ArrayList) info);
        Tokenlist.add(finalTokenInfo );
    }

    private  void lexerErrorFound(String error, String symbol){

        //List<Object> finalErrorInfo = Arrays.asList(-1,"Error @ Line - " +  CurrentLine + " : " + error," Symbol - " + symbol);
        //Tokenlist.add(finalErrorInfo);

        if(ErrorFound == true){
            return;
        }
        else {
            ErrorFound = true;
        }

        //createToken("Error @ Line - " +  CurrentLine + " : " + error + " Symbol - " + symbol);

        TokenCounter = TokenCounter +1;
        String word = "Error- '" + error + "' : Symbol- '" + symbol + "'";


        ArrayList<String> info = new ArrayList<>();
        info.add("TokenCount - " + TokenCounter);
        if(lineCount.size() > TokenCounter -1 )
            info.add("LineCount - " + lineCount.get(TokenCounter -1));


        List<Object> finalTokenInfo =  Arrays.asList( (String) word, (ArrayList) info);
        Tokenlist.add(finalTokenInfo );
    }

    /*************Helper Functions **********/

    private String RemoveTrailSpaces(String line) {

        if(checkIsOnlySpace(line) == false &&(isQuote.isEmpty() == false)){
            return line;
        }else if(checkIsOnlySpace(line) == true &&(isQuote.isEmpty() == false)){
            return "\n";
        }
        else if (checkIsOnlySpace(line) == true && (isQuote.isEmpty() == true) ){
            line = "";
            return line;
        }
        /*else if(checkIsOnlySpace(line) == true &&  ){
            return "\n";
        }*/

        if((line.length() >= 2) && (!line.equals(" "))) {
            while (line.charAt(line.length() - 1) == ' ') {
                if((line.length() >= 2) && (!line.equals(" ")))
                    line = line.substring(0, line.length() - 1);
                else
                    break;
            }
        }

        if(line.isEmpty() == false) {
            while (line.charAt(0) == ' ') { //remove first spaces
                line = line.substring(1, line.length());
            }
        }

        return line;
    }

    private ArrayList<String> getWords(String line) {

        ArrayList<String> output = new ArrayList<>();
        //ArrayList<String> outputWords = new ArrayList<>();

        while (line.isEmpty() == false) {

            System.out.println("LINE edits : " + line);

            if (numberOfQuote(line) == 0 && (isQuote.isEmpty() == true) ) {
                String[] splitLine = line.split(" ");
                for (int i = 0; i < splitLine.length; i++)
                    if (checkIsOnlySpace(splitLine[i]) == false)
                        output.add(splitLine[i]);

                line = "";
            }
            else if (numberOfQuote(line) == 0 && (isQuote.isEmpty() == false) ) {
                isQuote = isQuote + line;
                line = "";
            }
            else {
                int index = line.indexOf("\"");

                if(isQuote.isEmpty() == true) {
                    String[] splitLine = line.substring(0, index).split(" ");

                    for (int i = 0; i < splitLine.length; i++)
                        if (checkIsOnlySpace(splitLine[i]) == false)
                            output.add(splitLine[i]);

                    String qValue = String.valueOf(line.charAt(index));
                    index = index + 1;
                    while ((index < line.length()) && (line.charAt(index) != '\"')){
                        qValue = qValue + line.charAt(index);
                        index = index + 1;
                    }
                    ; //continue until 2nd or length found

                    if (index < line.length() && line.charAt(index) == '\"') {
                        qValue = qValue + line.charAt(index);
                        System.out.println("CHECK ME HERE BROOOO::   " + qValue);
                        output.add(qValue);
                        index = index+1;
                    }
                    else{
                        //qValue = qValue + line.charAt(index);
                        isQuote = qValue;
                    }

                    line = line.substring(index, line.length());
                }else{
                    index = index+1;
                    isQuote = isQuote + line.substring(0, index );
                    output.add(isQuote);
                    isQuote = "";

                    line = line.substring(index, line.length());
                }
            }
        }



        /*String firstPart= "";
        for(int i=0; i < output.size(); i++){

            if( (numberOfQuote(output.get(i)) == 1) && (firstPart.isEmpty() == true) ){
                String first = output.get(i).substring(0, output.indexOf("\""));
                if (first.isEmpty() == false)
                    outputWords.add(first);

                firstPart = output.get(i).substring(output.indexOf("\""), output.get(i).length());

            } else if((numberOfQuote(output.get(i)) == 1) && (firstPart.isEmpty() == false)){

                String lastPart = output.get(i).substring(0, output.indexOf("\""));
                outputWords.add(firstPart + lastPart);

                String last = output.get(i).substring(output.indexOf("\""), output.get(i).length());
                if (last.isEmpty() == false)
                    outputWords.add(last);

                firstPart = "";
            }
            else{
                outputWords.add(output.get(i));
            }
        }

        if (firstPart.isEmpty() == false)
            outputWords.add(firstPart);*/


        return output;
    }

    private  boolean checkIsOnlySpace(String line) {
        int index = 0;
        while (index <line.length() ){
            if(line.charAt(index) != ' ')
                return false;
            index = index +1;
        }
        return true;
    }

    private int numberOfQuote(String word){

        //System.out.println("bring the man out3 : " + word);
        int count = 0;

        for (int i = 0; i < word.length();i++){
            if(word.charAt(i)== '"')
                count = count +1;
        }

        //System.out.println(count);

        return count;
    }

}
