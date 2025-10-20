package model;

public class BooleanType extends PrimType{
    public BooleanType(Token tk){
        super(tk);
    }

    public boolean isCompatible(Type t){
        return t instanceof BooleanType;
    }
}
