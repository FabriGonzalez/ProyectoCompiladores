package model;

import Analyzers.SymbolTable;
import exceptions.SemanticException;

public class Param {
    String name;
    Type type;

    public Param(Token tk){
        name = tk.getLexeme();
    }

    public String getName(){
        return name;
    }

    public void setType(Type t){
        type = t;
    }

    public void isCorrectDeclared(SymbolTable ts){
        boolean primitive = type instanceof PrimType;
        if(!(primitive || ts.getClass(type.getName()) != null)){
            throw new SemanticException("Error tipo de retorno en atributo no declarado");
        }
    }
}
