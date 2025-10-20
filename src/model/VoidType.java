package model;

public class VoidType extends Type{

    public VoidType(Token tk){
        super(tk);
    }

    @Override
    public boolean isCompatible(Type t){
        return t instanceof VoidType;
    }
}
