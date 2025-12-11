package model.ast;

import sourcemanager.OutputManager;

public class PrintIBlockNode extends BlockNode{
    public void check(){

    }

    public void generate(){
        OutputManager.gen("LOAD 3 ; apila el parametro");
        OutputManager.gen("IPRINT  ; Imprime el entero en el tope de la pila");
    }
}
