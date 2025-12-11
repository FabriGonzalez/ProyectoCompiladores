package model.ast;

import model.Type;
import model.Token;
import model.CharType;
import sourcemanager.OutputManager;

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

    @Override
    public void generate(boolean a) {
        char charSt = charTk.getLexeme().substring(1, charTk.getLexeme().length() - 1).charAt(0);
        OutputManager.gen("PUSH " + (int) charSt + " ; apila el char " + charTk.getLexeme());
    }
}

