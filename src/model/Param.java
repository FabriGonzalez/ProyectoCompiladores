package model;

import Analyzers.SymbolTable;
import exceptions.SemanticException;

public class Param implements VarEntry{
    String name;
    Type type;
    int declaredLine;
    int offset;

    public Param(Token tk){
        name = tk.getLexeme();
        declaredLine = tk.getLineNumber();
    }

    public int getDeclaredLine(){
        return declaredLine;
    }

    public String getName(){
        return name;
    }

    public void setType(Type t){
        type = t;
    }

    public void isCorrectDeclared(SymbolTable ts){
        Classs a = ts.getClass(type.getName());
        if(type instanceof ReferenceType){
            if(a != null){
                ((ReferenceType) type).setAssociatedClass(a);
            } else {
                throw new SemanticException(type.getDeclaredLine(), "la clase del parametro " + name + " no existe", type.getName());
            }
        }
    }

    public Type getType(){
        return type;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void setOffset(int off) {
        offset = off;
    }
}
