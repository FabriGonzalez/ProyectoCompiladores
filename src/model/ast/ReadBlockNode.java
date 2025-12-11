package model.ast;

import sourcemanager.OutputManager;

public class ReadBlockNode extends BlockNode{
    public void check(){

    }

    public void generate(){
        OutputManager.gen("READ ; Lee un valor entero");
        OutputManager.gen("PUSH 48 ; Por ASCII");
        OutputManager.gen("SUB");
        OutputManager.gen("STORE 3 ; Devuelve el valor entero leido, poniendo el tope de la pila en la locacion reservada");
    }
}
