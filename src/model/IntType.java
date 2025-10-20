package model;

public class IntType extends PrimType {

    public IntType(Token tk){
        super(tk);
    }

    public boolean isCompatible(Type t){
        return t instanceof IntType;
    }
}