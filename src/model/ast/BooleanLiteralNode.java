package model.ast;

import model.BooleanType;
import model.Token;
import model.Type;
import sourcemanager.OutputManager;

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

    @Override
    public void generate(boolean a) {
        if(boolTk.getLexeme().equals("false")){
            OutputManager.gen("PUSH 0 ; apila el booolean " + boolTk.getLexeme());
        } else {
            OutputManager.gen("PUSH 1 ; apila el booolean " + boolTk.getLexeme());
        }

    }
}
