package model.ast;

import model.BooleanType;
import model.Token;
import model.Type;

public class BooleanLiteralNode extends PrimitiveNode{
    Token boolTk;

    public BooleanLiteralNode(Token tk){
        boolTk = tk;
    }

    @Override
    public Token getExpressionToken() {
        return boolTk;
    }

    public Type check(){
        return new BooleanType(boolTk);
    }
}
