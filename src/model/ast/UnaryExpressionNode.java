package model.ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.IntType;
import model.Token;
import model.Type;

public class UnaryExpressionNode extends CompoundExpressionNode{
    OperandNode operand;
    Token operator;

    public UnaryExpressionNode(OperandNode op, Token tk){
        operand = op;
        operator = tk;
    }

    @Override
    public Token getExpressionToken() {
        return operator;
    }

    @Override
    public Type check() {
        Type operandType = operand.check();
        String op = operator.getLexeme();

        if(op.equals("++") || op.equals("--") || op.equals("+") || op.equals("-")){
            if(!operandType.isCompatible(new IntType(new Token("intLiteral", "0", 0)))){
                throw new SemanticException(operator.getLineNumber(), "El operador " + operator.getLexeme() + " no es compatible con el tipo de " + operand.getExpressionToken().getLexeme(), operator.getLexeme());
            }
        } else if(op.equals("!")){
            if(!operandType.isCompatible(new BooleanType(new Token("boolean", "true", 0)))){
                throw new SemanticException(operator.getLineNumber(), "El operador " + operator.getLexeme() + " no es compatible con el tipo de " + operand.getExpressionToken().getLexeme(), operator.getLexeme());
            }
        }

        return operandType;
    }
}
