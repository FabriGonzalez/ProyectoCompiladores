package model.ast;

import sourcemanager.OutputManager;

public class DebugPrintBlockNode extends BlockNode{
    public void check(){

    }

    public void generate(){
        OutputManager.gen("LOAD 3 ; apila el parametro");
        OutputManager.gen("IPRINT ; imprime el entero en el tope de la pila");
    }
}
