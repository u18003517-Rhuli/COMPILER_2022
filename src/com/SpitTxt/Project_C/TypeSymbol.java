package com.SpitTxt.Project_C;

import java.util.Objects;

public class TypeSymbol {
    /*String name;
    String type;
    int size;
    int dimension;
    String lineDeclaration;
    String lineUsage;
    String address;*/

    //private String type;
    //private boolean isSubtype;

    private boolean isNumber;
    private boolean isNotNegative;
    private boolean isBoolean;
    private boolean isTrue;
    private boolean isFalse;
    private boolean isString;
    private boolean isMixed;
    private boolean isUnknown;
    private String type;

    //private String checks = "N,NN,B T, F ,S, U,M";
    //private String[] checkValues = new String[] {"number","not-negative","boolean", "true", "false" ,"string","mixed","unknown"};

    public TypeSymbol() {
        this.isNumber = false;
        this.isNotNegative = false;
        this.isBoolean = false;
        this.isTrue = false;
        this.isFalse = false;
        this.isString = false;
        this.isMixed = false;
        this.isUnknown = true;
        this.type = "U";
    }

    public TypeSymbol(String type) {
        this.isNumber = false;
        this.isNotNegative = false;
        this.isBoolean = false;
        this.isTrue = false;
        this.isFalse = false;
        this.isString = false;
        this.isMixed = false;
        this.isUnknown = false;
        this.type = type;

        //N,NN,B T, F ,S, U,M
        if(type.equals("N") || type.equals("n")) {
            this.isNumber = true;
            this.isMixed = true;
        }

        if(type.equals("NN") || type.equals("nn")){
            this.isNumber = true;
            this.isNotNegative = true;
            this.isMixed = true;
        }

        if(type.equals("B") || type.equals("b")){
            this.isBoolean = true;
        }

        if(type.equals("T") || type.equals("t")){
            this.isBoolean = true;
            this.isTrue = true;
        }

        if(type.equals("F") || type.equals("f")){
            this.isBoolean = true;
            this.isFalse = true;
        }

        if(type.equals("S") || type.equals("s")){
            this.isString = true;
            this.isMixed = true;
        }

        if(type.equals("M") || type.equals("m")){
            this.isMixed = true;
        }

        if(type.equals("U") || type.equals("u")){
            this.isUnknown = true;
        }
    }

    public TypeSymbol(TypeSymbol typeSymbol) {

        this.isNumber = new Boolean(typeSymbol.isNumber());
        this.isNotNegative = new Boolean(typeSymbol.isNotNegative());
        this.isBoolean = new Boolean(typeSymbol.isBoolean());
        this.isTrue = new Boolean(typeSymbol.isTrue());
        this.isFalse = new Boolean(typeSymbol.isFalse());
        this.isString = new Boolean(typeSymbol.isString());
        this.isMixed = new Boolean(typeSymbol.isMixed());
        this.isUnknown = new Boolean(typeSymbol.isUnknown());
        this.type = new String(typeSymbol.getType());

    }


    /*public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSubtype() {
        return isSubtype;
    }

    public void setSubtype(boolean subtype) {
        isSubtype = subtype;
    }*/

    public boolean isNumber() {
        return isNumber;
    }

    public void setNumber(boolean number) {
        isNumber = number;
    }

    public boolean isNotNegative() {
        return isNotNegative;
    }

    public void setNotNegative(boolean notNegative) {
        isNotNegative = notNegative;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        isBoolean = aBoolean;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    public boolean isFalse() {
        return isFalse;
    }

    public void setFalse(boolean aFalse) {
        isFalse = aFalse;
    }

    public boolean isString() {
        return isString;
    }

    public void setString(boolean string) {
        isString = string;
    }

    public boolean isMixed() {
        return isMixed;
    }

    public void setMixed(boolean mixed) {
        isMixed = mixed;
    }

    public boolean isUnknown() {
        return isUnknown;
    }

    public void setUnknown(boolean unknown) {
        isUnknown = unknown;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSymbolType(){
        return "";
    }


    public void setSymbolType(String type){
        this.isNumber = false;
        this.isNotNegative = false;
        this.isBoolean = false;
        this.isTrue = false;
        this.isFalse = false;
        this.isString = false;
        this.isMixed = false;
        this.isUnknown = false;
        this.type = type;

        //N,NN,B T, F ,S, U,M
        if(type.equals("N") || type.equals("n")) {
            this.isNumber = true;
            this.isMixed = true;
        }

        if(type.equals("NN") || type.equals("nn")){
            this.isNumber = true;
            this.isNotNegative = true;
            this.isMixed = true;
        }

        if(type.equals("B") || type.equals("b")){
            this.isBoolean = true;
        }

        if(type.equals("T") || type.equals("t")){
            this.isBoolean = true;
            this.isTrue = true;
        }

        if(type.equals("F") || type.equals("f")){
            this.isBoolean = true;
            this.isFalse = true;
        }

        if(type.equals("S") || type.equals("s")){
            this.isString = true;
            this.isMixed = true;
        }

        if(type.equals("M") || type.equals("m")){
            this.isMixed = true;
        }

        if(type.equals("U") || type.equals("u")){
            this.isUnknown = true;
        }
    }

    public boolean checkEquals(TypeSymbol o) {
        /*if (this == o) {
            return true;
        }
        if (!(o instanceof TypeSymbol)) {
            return false;
        }*/
        TypeSymbol that = o;

        System.out.println(" MAHUUGA Part2");


        if(Objects.equals(type, that.type))
            return true;

        if( (isNumber || isNotNegative) && (that.isNumber || that.isNotNegative)) {

            return true;
        }

        if( ( isBoolean || isTrue || isFalse ) && (that.isBoolean || that.isTrue || that.isFalse))
            return true;

        if (isString && that.isString){
            return true;
        }

        if (isMixed && that.isMixed){
            return true;
        }

        if (isUnknown && that.isUnknown){
            return true;
        }

        //return isNumber == that.isNumber && isNotNegative == that.isNotNegative && isBoolean == that.isBoolean && isTrue == that.isTrue && isFalse == that.isFalse && isString == that.isString && isMixed == that.isMixed && isUnknown == that.isUnknown && Objects.equals(type, that.type);
        return false;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeSymbol)) {
            return false;
        }
        TypeSymbol that = (TypeSymbol) o;
        return isNumber == that.isNumber && isNotNegative == that.isNotNegative && isBoolean == that.isBoolean && isTrue == that.isTrue && isFalse == that.isFalse && isString == that.isString && isMixed == that.isMixed && isUnknown == that.isUnknown && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isNumber, isNotNegative, isBoolean, isTrue, isFalse, isString, isMixed, isUnknown, type);
    }*/

    /*public String getName() {   return name;  }
    public String getType() {  return type;  }
    public int getSize() { return size;  }
    public int getDimension() { return dimension; }
    public String getLineDeclaration() { return lineDeclaration; }
    public String getLineUsage() { return lineUsage; }
    public String getAddress() {  return address; }*/
}
