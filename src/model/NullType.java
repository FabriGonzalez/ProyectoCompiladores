package model;

public class NullType extends ReferenceType{
    public NullType(Token tk){
        super(tk);
    }

    public boolean isCompatible(Type t){
        return t instanceof ReferenceType;
    }

}
