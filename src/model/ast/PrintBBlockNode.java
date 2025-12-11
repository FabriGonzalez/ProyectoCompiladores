package model.ast;

import sourcemanager.OutputManager;

public class PrintBBlockNode extends BlockNode{
    public void check(){

    }

    public void generate(){
        OutputManager.gen("LOAD 3 ; apila el parametro");
        OutputManager.gen("BPRINT ; imprime el entero en el tope de la pila");
    }
}
