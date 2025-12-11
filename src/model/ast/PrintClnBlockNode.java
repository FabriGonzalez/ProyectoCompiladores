package model.ast;

import sourcemanager.OutputManager;

public class PrintClnBlockNode extends BlockNode{
    public void check(){

    }

    public void generate(){
        OutputManager.gen("LOAD 3 ; apila el parametro");
        OutputManager.gen("CPRINT  ; Imprime el char en el tope de la pila");
        OutputManager.gen("PRNLN  ; Imprime el caracter de nueva línea");
    }
}
