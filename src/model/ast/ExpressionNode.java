package model.ast;

import model.Token;
import model.Type;

public abstract class ExpressionNode {

    public abstract Token getExpressionToken();

    public abstract Type check();

    public abstract void generate(boolean isLeftAssignment);
}
