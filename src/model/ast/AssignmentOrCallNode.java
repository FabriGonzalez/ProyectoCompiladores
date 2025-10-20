package model.ast;

import exceptions.SemanticException;
import model.Token;

public class AssignmentOrCallNode extends StatementNode{
    ExpressionNode associatedExpression;

    public AssignmentOrCallNode(ExpressionNode exp){
        associatedExpression = exp;
    }

    public void check(){
        associatedExpression.check();

        if (associatedExpression instanceof AssignmentExpressionNode) {
            return;
        }

        Token expressionTk = associatedExpression.getExpressionToken();

        if(associatedExpression instanceof PrimaryNode primaryExpression){
            ChainedNode chain = primaryExpression.getChain();

            if(chain != null){
                ChainedNode lastChain = chain.getLastChain();

                if(!(lastChain instanceof MethodCallChainedNode)){
                    throw new SemanticException(expressionTk.getLineNumber(), "La expresion encadenada debe terminar en llamada a metodo", expressionTk.getLexeme());
                }
            } else {
                if(!(primaryExpression instanceof MethodCallNode || primaryExpression instanceof StaticMethodCallNode || primaryExpression instanceof ConstructorCallNode)){
                    throw new SemanticException(expressionTk.getLineNumber() ,"La expresión debe ser una llamada a método, método estático o constructor", expressionTk.getLexeme());
                }
            }
            return;
        }
        throw new SemanticException(expressionTk.getLineNumber(), "La expresión no es válida como sentencia", expressionTk.getLexeme());
    }
}
