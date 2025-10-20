package model.ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.Token;
import model.Type;

public class IfNode extends StatementNode{
    Token ifToken;
    ExpressionNode condition;
    StatementNode then;
    StatementNode elseBody;

    public IfNode(Token tk, ExpressionNode c, StatementNode t, StatementNode e){
        ifToken = tk;
        condition = c;
        then = t;
        elseBody = e;
    }

    public void check(){
        Type tCondition = condition.check();
        if(!tCondition.isCompatible(new BooleanType(new Token("boolean", "true", 0)))){
            Token conditionTk = condition.getExpressionToken();
            throw new SemanticException(conditionTk.getLineNumber(), "La condicion del if no es boolean", conditionTk.getLexeme());
        }
        then.check();
        elseBody.check();
    }
}
