package model.ast;

import exceptions.SemanticException;
import model.Token;
import model.Type;

public class AssignmentExpressionNode extends ExpressionNode{
    CompoundExpressionNode leftNode;
    CompoundExpressionNode rightNode;
    Token operator;

    public AssignmentExpressionNode(CompoundExpressionNode l, CompoundExpressionNode r, Token o){
        leftNode = l;
        rightNode = r;
        operator = o;
    }

    @Override
    public Token getExpressionToken() {
        return operator;
    }

    public Type check(){
        Type leftType = leftNode.check();
        Type rightType = rightNode.check();

        if(leftNode instanceof PrimaryNode primaryRightNode){
            ChainedNode chain = primaryRightNode.getChain();

            if(chain == null){
                if(!(primaryRightNode instanceof VarAccessNode)){
                    throw new SemanticException(operator.getLineNumber(),
                            "El lado izquierdo de la asignación debe ser una variable", operator.getLexeme());
                }
            } else {
                ChainedNode lastChain = chain.getLastChain();

                if(!(lastChain instanceof VarChainedNode)){
                    throw new SemanticException(operator.getLineNumber(),
                            "El encadenado del lado izquierdo de la asignación debe terminar en una variable", operator.getLexeme());
                }
            }
        } else{
            throw new SemanticException(operator.getLineNumber(),
                    "El lado izquierdo de la asignación no es una expresión válida", operator.getLexeme());
        }

        if(!leftType.isCompatible(rightType)){
            throw new SemanticException(operator.getLineNumber(), "Los tipos del operador de asignacion no son compatibles", operator.getLexeme());
        }

        return leftType;
    }
}
