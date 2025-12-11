package model;

import Analyzers.SymbolTable;
import exceptions.SemanticException;
import sourcemanager.OutputManager;

public class Constructor extends GenericMethod{

    public Constructor(Token tk, Classs aC){
        super(tk.getLexeme(), tk.getLineNumber(), aC);
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

    public void generate(){
        OutputManager.gen("lblConstructor@" + associatedClass.getName()+": LOADFP; Apila el valor del registro fp");
        OutputManager.gen("LOADSP; Apila el valor del registro sp");
        OutputManager.gen("STOREFP; Almacena el tope de la pila en el registro fp");
        if(block != null){
            block.generate();
        }
        OutputManager.gen("STOREFP; Almacena el tope de la pila en el registro fp");
        int ret = params.size() + 1;
        OutputManager.gen("RET " + ret);
    }

}
