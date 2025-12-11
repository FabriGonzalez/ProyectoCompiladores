package model.ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.Token;
import model.Type;
import sourcemanager.OutputManager;

public class IfNode extends StatementNode{
    private static int cont = 0;

    Token ifToken;
    ExpressionNode condition;
    StatementNode then;
    StatementNode elseBody;
    String lblStartElse;
    String lblEndIf;

    public IfNode(Token tk, ExpressionNode c, StatementNode t, StatementNode e){
        ifToken = tk;
        condition = c;
        then = t;
        elseBody = e;
        lblStartElse = "lblFinElse" + cont;
        lblEndIf = "lblEndIf" + cont;
        cont ++;
    }

    public void check(){
        Type tCondition = condition.check();
        if(!tCondition.isCompatible(new BooleanType(new Token("boolean", "true", 0)))){
            Token conditionTk = condition.getExpressionToken();
            throw new SemanticException(conditionTk.getLineNumber(), "La condicion del if no es boolean", conditionTk.getLexeme());
        }
        then.check();
        if(elseBody != null){
            elseBody.check();
        }
    }

    @Override
    public void generate() {
        if(elseBody != null){
            condition.generate(false);
            OutputManager.gen("BF " + lblStartElse);
            then.generate();
            OutputManager.gen("JUMP " + lblEndIf);
            OutputManager.gen(lblStartElse + ": NOP");
            elseBody.generate();
            OutputManager.gen(lblEndIf + ": NOP");
        } else {
            condition.generate(false);
            OutputManager.gen("BF " + lblEndIf);
            then.generate();
            OutputManager.gen("JUMP " + lblEndIf);
            OutputManager.gen(lblEndIf + ": NOP");
        }
    }
}
