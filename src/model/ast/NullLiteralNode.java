package model.ast;

import model.NullType;
import model.Token;
import model.Type;
import sourcemanager.OutputManager;

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

    @Override
    public void generate(boolean a) {
        OutputManager.gen("PUSH 0 ; apila null");
    }
}
