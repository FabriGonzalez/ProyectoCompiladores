package model.ast;

import sourcemanager.OutputManager;

public class PrintSBlockNode extends BlockNode{
    public void check(){

    }

    public void generate(){
        OutputManager.gen("LOAD 3 ; apila el parametro");
        OutputManager.gen("SPRINT  ; Imprime el string en el tope de la pila");
    }
}
