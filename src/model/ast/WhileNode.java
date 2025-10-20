package model.ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.Token;
import model.Type;

public class WhileNode extends StatementNode{
    Token whileTk;
    ExpressionNode condition;
    StatementNode body;

    public WhileNode(Token tk, ExpressionNode c, StatementNode b){
        whileTk = tk;
        condition = c;
        body = b;
    }

    @Override
    public void check() {
        Type conditionType = condition.check();
        if(!conditionType.isCompatible(new BooleanType(new Token("boolean", "true", 0)))){
            Token conditionTk = condition.getExpressionToken();
            throw new SemanticException(conditionTk.getLineNumber(), "La condicion del if no es boolean", conditionTk.getLexeme());
        }
        body.check();
    }
}
