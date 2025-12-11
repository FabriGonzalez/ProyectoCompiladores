package model.ast;

import exceptions.SemanticException;
import model.NullType;
import model.Token;
import model.Type;
import sourcemanager.OutputManager;

public class AssignmentExpressionNode extends ExpressionNode{
    CompoundExpressionNode leftNode;
    CompoundExpressionNode rightNode;
    Token operator;
    boolean isRigthNode;

    public AssignmentExpressionNode(CompoundExpressionNode l, CompoundExpressionNode r, Token o){
        leftNode = l;
        rightNode = r;
        operator = o;
        isRigthNode = false;
    }

    @Override
    public Token getExpressionToken() {
        return operator;
    }

    public Type check(){
        Type leftType = leftNode.check();
        Type rightType = rightNode.check();

        if(leftNode instanceof PrimaryNode primaryLeftNode){
            ChainedNode chain = primaryLeftNode.getChain();

            if(chain == null){
                boolean isVarAccesNode = primaryLeftNode instanceof VarAccessNode;
                boolean isParenthezideExp = primaryLeftNode instanceof ParenthesizedExpressionNode;
                if(isParenthezideExp){
                    ParenthesizedExpressionNode p = (ParenthesizedExpressionNode) primaryLeftNode;
                    ExpressionNode parExp = p.getAssociatedExp();
                    boolean isAssigNode = parExp instanceof AssignmentExpressionNode;
                    isVarAccesNode = parExp instanceof VarAccessNode;
                    if(!(isAssigNode || isVarAccesNode)){
                        throw new SemanticException(operator.getLineNumber(),
                                "El lado izquierdo de la asignación debe ser una variable", operator.getLexeme());
                    }
                }
                if(!(isParenthezideExp || isVarAccesNode)){
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

    public void setIsRightNode(){
        isRigthNode = true;
    }

    @Override
    public void generate(boolean isLeftAssignment) {
        if(rightNode instanceof ParenthesizedExpressionNode p){
           p.setIsRightNode();
        }
        rightNode.generate(false);
        if(isRigthNode){
            OutputManager.gen("DUP");
        }
        leftNode.generate(true);
    }
}
