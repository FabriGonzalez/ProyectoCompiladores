package model;

abstract public class Type {
    String name;
    int declaredLine;

    public Type(Token tk){
        name = tk.getLexeme();
        declaredLine = tk.getLineNumber();
    }

    public String getName(){
        return name;
    }

    public int getDeclaredLine(){
        return declaredLine;
    }

    public abstract boolean isCompatible(Type t);

}
