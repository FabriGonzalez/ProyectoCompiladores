package model.ast;

import sourcemanager.OutputManager;

public class PrintLnBlockNode extends BlockNode{
    public void check(){

    }

    public void generate(){
        OutputManager.gen("PRNLN  ; Imprime el caracter de nueva línea");
    }
}
