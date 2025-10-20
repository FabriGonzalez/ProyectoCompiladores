package model.ast;

import Analyzers.SymbolTable;
import exceptions.SemanticException;
import model.*;

import java.util.List;

public class StaticMethodCallNode extends PrimaryNode{
    Token classTk;
    Token methodTk;
    List<ExpressionNode> args;

    public StaticMethodCallNode(Token c, Token m, List<ExpressionNode> l){
        classTk = c;
        methodTk = m;
        args = l;
    }

    @Override
    public Token getExpressionToken() {
        return methodTk;
    }

    @Override
    public Type check() {
        Classs associatedClass = SymbolTable.getInstance().getClass(classTk.getLexeme());
        if(associatedClass == null){
            throw new SemanticException(methodTk.getLineNumber(), "La clase asociada al metodo " + methodTk.getLexeme() + " no existe", methodTk.getLexeme());
        }

        Method methodCall = associatedClass.getMethodByName(methodTk.getLexeme());
        if(methodCall == null){
            throw new SemanticException(methodTk.getLineNumber(), "El metodo de nombre " + methodTk.getLexeme() + " no existe en la clase de nombre " + classTk.getLexeme(), methodTk.getLexeme());
        }

        if (!methodCall.isStatic()) {
            throw new SemanticException(methodTk.getLineNumber(),
                    "El método '" + methodTk.getLexeme() + "' no es estático y no puede ser invocado desde una llamada estática.",
                    methodTk.getLexeme());
        }

        List<Param> expectedParams = methodCall.getParams();

        if(expectedParams.size() != args.size()){
            throw new SemanticException(methodTk.getLineNumber(),
                    "Cantidad incorrecta de argumentos para el método '" + methodTk.getLexeme() + "'. Se esperaban " +
                            expectedParams.size() + " y se pasaron " + args.size(),
                    methodTk.getLexeme());
        }

        for (int i = 0; i < args.size(); i++) {
            Type actualType = args.get(i).check();
            Type expectedType = expectedParams.get(i).getType();
            if (!actualType.isCompatible(expectedType)) {
                throw new SemanticException(methodTk.getLineNumber(),
                        "Tipo incompatible en el argumento " + (i + 1) + " de '" + methodTk.getLexeme() + "'. Se esperaba " +
                                expectedType + " y se recibió " + actualType,
                        methodTk.getLexeme());
            }
        }

        Type returnType = methodCall.getEffectiveReturnType();
        if (chain != null) {
            return chain.check(returnType);
        } else {
            return returnType;
        }
    }
}
