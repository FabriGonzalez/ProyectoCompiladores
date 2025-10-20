package model.ast;

import model.Type;
import model.Token;
import model.CharType;

public class CharLiteralNode extends PrimitiveNode{
    Token charTk;

    public CharLiteralNode(Token tk){
        charTk = tk;
    }

    @Override
    public Token getExpressionToken() {
        return charTk;
    }

    public Type check(){
        return new CharType(charTk);
    }

}

