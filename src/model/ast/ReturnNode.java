package model.ast;


import exceptions.SemanticException;
import model.*;
import sourcemanager.OutputManager;

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

            if(!methodType.isCompatible(expressionType)){
                throw new SemanticException(returnTk.getLineNumber(), "El tipo de retorno del return del metodo " + associatedMethod.getName() + " no es compatible con el declarado en la signatura", returnTk.getLexeme());
            }
        } else {
            if(!methodType.isCompatible(new VoidType(new Token ("void", "void" , 0)))){
                throw new SemanticException(returnTk.getLineNumber(), "El tipo de retorno de la signatura no es void y el return esta vacio", returnTk.getLexeme());
            }
        }
    }

    @Override
    public void generate() {
        if(!associatedMethod.getEffectiveReturnType().getName().equals("void")){
            expression.generate(false);
            if(associatedMethod.isStatic()){
                int offsetToStore = 2 + associatedMethod.getParams().size() + 1;
                OutputManager.gen("STORE " + offsetToStore + " ; guarda resultado");
            } else {
                int offsetToStore = 3 + associatedMethod.getParams().size() + 1;
                OutputManager.gen("STORE " + offsetToStore+ " ; guarda resultado");
            }
            OutputManager.gen("STOREFP  ; Actualizar el fp para que apunte al RA del llamador");
            int params = associatedMethod.getParams().size();
            int retCount = associatedMethod.isStatic() ? params : params + 1;
            OutputManager.gen("RET " + retCount);
        }
    }
}
