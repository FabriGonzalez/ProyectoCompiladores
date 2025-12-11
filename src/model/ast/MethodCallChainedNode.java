package model.ast;

import Analyzers.SymbolTable;
import exceptions.SemanticException;
import model.*;
import sourcemanager.OutputManager;

import java.util.List;

public class MethodCallChainedNode extends ChainedNode{
    List<ExpressionNode> args;
    Token methodTk;
    Classs associatedClass;

    public MethodCallChainedNode(List<ExpressionNode> a, Token tk){
        args = a;
        methodTk = tk;
    }

    public String getReturnType(){
        Method m = associatedClass.getMethodByName(methodTk.getLexeme());
        return m.getReturnType().getName();
    }

    public Type check(Type baseType){

        if (!(baseType instanceof ReferenceType)) {
            throw new SemanticException(methodTk.getLineNumber(),
                    "No se puede invocar un método sobre un tipo no referencial",
                    methodTk.getLexeme());
        }

        ReferenceType refType = (ReferenceType) baseType;
        Classs baseClass = SymbolTable.getInstance().getClass(refType.getName());
        associatedClass = baseClass;

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
            Type argType = args.get(i).check();
            Type expectedType = expectedParams.get(i).getType();
            if (!expectedType.isCompatible(argType)) {
                throw new SemanticException(methodTk.getLineNumber(),
                        "Tipo incompatible en el argumento " + (i + 1) + " de '" + methodTk.getLexeme() + "'. Se esperaba " +
                                expectedType + " y se recibió " + argType,
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

    @Override
    public void generate(boolean a) {
        Method m = associatedClass.getMethodByName(methodTk.getLexeme());
        if(m.isStatic()){
            OutputManager.gen("POP ; El metodo es estatico no usara el load");
            if(!m.getReturnType().getName().equals("void")){
                OutputManager.gen("RMEM 1 ; Lugar para el retorno");;
            }
            for(ExpressionNode p : args){
                p.generate(false);
            }
            OutputManager.gen("PUSH lblMet" + methodTk.getLexeme() + "@" + m.getAssociatedClass().getName() +" ; apila el metodo");
            OutputManager.gen("CALL ; Llama al metodo en el tope de la pila");
        } else {
            if(!m.getReturnType().getName().equals("void")){
                OutputManager.gen("RMEM 1 ; Lugar para el retorno");
                OutputManager.gen("SWAP");
            }
            for(ExpressionNode p : args){
                p.generate(false);
                OutputManager.gen("SWAP");
            }
            OutputManager.gen("DUP");
            OutputManager.gen("LOADREF 0 ; Apilo el offset de la VT en el CIR (siempre 0)");
            OutputManager.gen("LOADREF " + m.getOffset() + " ; Apilo el offset del metodo " + methodTk.getLexeme() + " en la VT");
            OutputManager.gen("CALL ; Llama al metodo en el tope de la pila");
        }
        if(chain != null){
            chain.generate(a);
        }
    }

    public boolean isStatic(){
        Method m = associatedClass.getMethodByName(methodTk.getLexeme());
        return m.isStatic();
    }
}
