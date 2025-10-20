package model.ast;

import exceptions.SemanticException;
import model.GenericMethod;
import model.NullType;
import model.Token;
import model.Type;

import javax.swing.plaf.nimbus.State;

public class LocalVarNode extends StatementNode {
    Token tkLocalVar;
    CompoundExpressionNode rightNode;
    GenericMethod associatedMethod;
    BlockNode associatedBlock;
    Type localVarType;

    public LocalVarNode(Token tk, CompoundExpressionNode c, GenericMethod a, BlockNode b){
        tkLocalVar = tk;
        rightNode = c;
        associatedMethod = a;
        associatedBlock = b;
    }

    public void check(){
        if(associatedMethod.getParamByName(tkLocalVar.getLexeme()) != null){
            throw new SemanticException(tkLocalVar.getLineNumber(), "El nombre de la variable local " + tkLocalVar.getLexeme() + " ya esta declarado como parametro", tkLocalVar.getLexeme());
        }

        if(associatedBlock.isLocalVarDeclared(tkLocalVar.getLexeme())){
            throw new SemanticException(tkLocalVar.getLineNumber(), "El nombre de la variable local " + tkLocalVar.getLexeme() + " ya esta declarado en el bloque actual o en algun ancestro", tkLocalVar.getLexeme());
        }

        Type expressionType = rightNode.check();
        if(expressionType instanceof NullType){
            throw new SemanticException(tkLocalVar.getLineNumber(), "La variable local " + tkLocalVar.getLexeme() + " es de tipo nulo", tkLocalVar.getLexeme());
        }

        localVarType = expressionType;
    }

    public Type getLocalVarType() {
        return localVarType;
    }

    public String getName(){
        return tkLocalVar.getLexeme();
    }

}
