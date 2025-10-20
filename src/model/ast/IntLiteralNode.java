package model.ast;

import model.IntType;
import model.Token;
import model.Type;

public class IntLiteralNode extends PrimitiveNode{
    Token intTk;

    public IntLiteralNode(Token tk){
        intTk = tk;
    }

    public Token getExpressionToken() {
        return intTk;
    }

    public Type check(){
        return new IntType(intTk);
    }
}
