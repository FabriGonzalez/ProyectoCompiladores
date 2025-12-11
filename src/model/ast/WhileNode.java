package model.ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.Token;
import model.Type;
import sourcemanager.OutputManager;

public class WhileNode extends StatementNode{
    private static int cont = 0;
    Token whileTk;
    ExpressionNode condition;
    StatementNode body;
    String lblWhile;
    String lblEndWhile;

    public WhileNode(Token tk, ExpressionNode c, StatementNode b){
        whileTk = tk;
        condition = c;
        body = b;
        lblWhile = "lblWhile" + cont;
        lblEndWhile = "lblFinWhile" + cont;
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

    @Override
    public void generate() {
        OutputManager.gen(lblWhile +": NOP");
        condition.generate(false);
        OutputManager.gen("BF " + lblEndWhile);
        body.generate();
        OutputManager.gen("JUMP " + lblWhile);
        OutputManager.gen(lblEndWhile + ": NOP");
    }
}
