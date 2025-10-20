package model.ast;

import model.NullType;
import model.Token;
import model.Type;

public class NullLiteralNode extends PrimitiveNode{
    Token nullTk;


    public NullLiteralNode(Token tk){
        nullTk = tk;
    }

    @Override
    public Token getExpressionToken() {
        return nullTk;
    }

    public Type check(){
        return new NullType(nullTk);
    }
}
