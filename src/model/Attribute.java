package model;

import Analyzers.SymbolTable;
import exceptions.SemanticException;

public class Attribute {
    String name;
    Type type;

    public Attribute(Token tk, Type t){
        name = tk.getLexeme();
        type = t;
    }

    public String getName(){
        return name;
    }

    public void isCorrectDeclared(SymbolTable ts){
        boolean primitive = type instanceof PrimType;
        if(!(primitive || ts.getClass(type.getName()) != null)){
            throw new SemanticException("Error tipo de retorno en atributo no declarado");
        }
    }
}
