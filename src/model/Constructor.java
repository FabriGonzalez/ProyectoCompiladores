package model;

import Analyzers.SymbolTable;
import exceptions.SemanticException;

public class Constructor extends GenericMethod{
    Classs associatedClass;

    public Constructor(Token tk, Classs aC){
        super(tk.getLexeme(), tk.getLineNumber());
        associatedClass = aC;
        hasBlock = false;
    }

    public void isCorrectDeclared(SymbolTable ts){
        if(!associatedClass.getName().equals(name)){
            throw new SemanticException(declaredLine, "el nombre del constructor no es igual al de la clase asociada " + associatedClass.getName() , name);
        }
        for(Param p : params){
            p.isCorrectDeclared(ts);
        }
    }

    public boolean isConstructor() {
        return true;
    }

}
