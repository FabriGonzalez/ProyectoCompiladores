package model.ast;


import exceptions.SemanticException;
import model.*;

public class ReturnNode extends StatementNode {
    Token returnTk;
    ExpressionNode expression;
    GenericMethod associatedMethod;

    public ReturnNode(Token n, ExpressionNode e, GenericMethod m){
        returnTk = n;
        expression = e;
        associatedMethod = m;
    }

    @Override
    public void check() {
        if (associatedMethod.isConstructor()) {
            throw new SemanticException(returnTk.getLineNumber(),
                    "Los constructores no pueden tener sentencias return",
                    returnTk.getLexeme());
        }

        Type methodType = associatedMethod.getEffectiveReturnType();

        if(expression != null){
            Type expressionType = expression.check();

            if(!expressionType.isCompatible(methodType)){
                throw new SemanticException(returnTk.getLineNumber(), "El tipo de retorno del return del metodo " + associatedMethod.getName() + " no es compatible con el declarado en la signatura", returnTk.getLexeme());
            }
        } else {
            if(!methodType.isCompatible(new VoidType(new Token ("void", "void" , 0)))){
                throw new SemanticException(returnTk.getLineNumber(), "El tipo de retorno de la signatura no es void y el return esta vacio", returnTk.getLexeme());
            }
        }
    }
}
