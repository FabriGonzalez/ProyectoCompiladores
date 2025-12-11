package model.ast;

import sourcemanager.OutputManager;

public class PrintCBlockNode extends BlockNode{
    public void check(){

    }

    public void generate(){
        OutputManager.gen("LOAD 3 ; apila el parametro");
        OutputManager.gen("CPRINT  ; Imprime el char en el tope de la pila");
    }
}
