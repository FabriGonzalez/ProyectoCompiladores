package model;

import Analyzers.SymbolTable;
import exceptions.SemanticException;

public class Attribute {
    String name;
    Type type;
    int declaredLine;

    public Attribute(Token tk, Type t){
        name = tk.getLexeme();
        type = t;
        declaredLine = tk.getLineNumber();
    }

    public String getName(){
        return name;
    }

    public int getDeclaredLine(){
        return declaredLine;
    }

    public void isCorrectDeclared(SymbolTable ts){
        Classs a = ts.getClass(type.getName());
        if(type instanceof ReferenceType){
            if(a != null){
                ((ReferenceType) type).setAssociatedClass(a);
            } else {
                throw new SemanticException(type.getDeclaredLine(), "la clase del atributo " + name + " no existe", type.getName());
            }
        }
    }

    public Type getType(){
        return type;
    }
}
