package model.ast;

import Analyzers.SymbolTable;
import exceptions.SemanticException;
import model.*;

import java.util.List;

public class MethodCallChainedNode extends ChainedNode{
    List<ExpressionNode> args;
    Token methodTk;

    public MethodCallChainedNode(List<ExpressionNode> a, Token tk){
        args = a;
        methodTk = tk;
    }

    public Type check(Type baseType){

        if (!(baseType instanceof ReferenceType)) {
            throw new SemanticException(methodTk.getLineNumber(),
                    "No se puede invocar un método sobre un tipo no referencial: " + baseType,
                    methodTk.getLexeme());
        }

        ReferenceType refType = (ReferenceType) baseType;
        Classs baseClass = SymbolTable.getInstance().getClass(refType.getName());

        if (baseClass == null) {
            throw new SemanticException(methodTk.getLineNumber(),
                    "La clase " + refType.getName() + " no está definida.",
                    methodTk.getLexeme());
        }

        GenericMethod methodCall = baseClass.getMethodByName(methodTk.getLexeme());
        if(methodCall == null){
            throw new SemanticException(methodTk.getLineNumber(),
                    "El método '" + methodTk.getLexeme() + "' no existe en la clase " + baseClass.getName(),
                    methodTk.getLexeme());
        }

        List<Param> expectedParams = methodCall.getParams();

        if (expectedParams.size() != args.size()) {
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
