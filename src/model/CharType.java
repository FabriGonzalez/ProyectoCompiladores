package model;

public class CharType extends PrimType{

    public CharType(Token tk){
        super(tk);
    }

    public boolean isCompatible(Type t){
        return t instanceof CharType;
    }

    @Override
    public boolean isCompatibleForCompare(Type t) {
        return t instanceof CharType;
    }
}
