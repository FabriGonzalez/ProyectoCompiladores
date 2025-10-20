package model.ast;

import model.Token;
import model.Type;

public class ParenthesizedExpressionNode extends PrimaryNode{
    ExpressionNode associatedExp;

    public ParenthesizedExpressionNode(ExpressionNode e){
        associatedExp = e;
    }

    @Override
    public Token getExpressionToken() {
        return associatedExp.getExpressionToken();
    }

    @Override
    public Type check() {
        Type expType = associatedExp.check();
        if(chain != null){
            return chain.check(expType);
        } else {
            return expType;
        }
    }
}
