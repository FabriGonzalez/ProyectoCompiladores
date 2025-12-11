package model.ast;

import exceptions.SemanticException;
import model.*;
import sourcemanager.OutputManager;


public class LocalVarNode extends StatementNode implements VarEntry {
    Token tkLocalVar;
    CompoundExpressionNode rightNode;
    GenericMethod associatedMethod;
    BlockNode associatedBlock;
    Type localVarType;
    int offset;

    public LocalVarNode(Token tk, CompoundExpressionNode c, GenericMethod a, BlockNode b){
        tkLocalVar = tk;
        rightNode = c;
        associatedMethod = a;
        associatedBlock = b;
    }

    public void check(){
        if(associatedMethod.getParamByName(tkLocalVar.getLexeme()) != null) {
            throw new SemanticException(tkLocalVar.getLineNumber(), "El nombre de la variable local " + tkLocalVar.getLexeme() + " ya esta declarado como parametro", tkLocalVar.getLexeme());
        }

        if(associatedBlock.isLocalVarDeclared(tkLocalVar.getLexeme())){
            throw new SemanticException(tkLocalVar.getLineNumber(), "El nombre de la variable local " + tkLocalVar.getLexeme() + " ya esta declarado en el bloque actual o en algun ancestro", tkLocalVar.getLexeme());
        }

        Type expressionType = rightNode.check();
        if(expressionType instanceof NullType){
            throw new SemanticException(tkLocalVar.getLineNumber(), "La variable local " + tkLocalVar.getLexeme() + " es de tipo nulo", tkLocalVar.getLexeme());
        }
        associatedBlock.setLocalVar(this);
        localVarType = expressionType;
    }

    @Override
    public void generate() {
        OutputManager.gen("RMEM 1 ; Reservo un lugar en memoria para almacenar el valor de la var local " + tkLocalVar.getLexeme());
        rightNode.generate(false);
        OutputManager.gen("STORE " + offset + "; almaceno el valor en el tope de la pila al offset de " + getName());
    }

    public Type getLocalVarType() {
        return localVarType;
    }

    public int getOffset(){
        return offset;
    }

    public void setOffset(int off){
        offset = off;
    }

    public String getName(){
        return tkLocalVar.getLexeme();
    }

}
