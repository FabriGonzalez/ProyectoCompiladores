package model.ast;

import exceptions.SemanticException;
import model.*;
import sourcemanager.OutputManager;

import java.util.List;

public class MethodCallNode extends PrimaryNode{
    Token methodTk;
    List<ExpressionNode> params;
    Classs associatedClass;
    GenericMethod associatedMethod;

    public MethodCallNode(Token tk, List<ExpressionNode> l, Classs c, GenericMethod m){
        methodTk = tk;
        params = l;
        associatedClass = c;
        associatedMethod = m;
    }

    @Override
    public Token getExpressionToken() {
        return methodTk;
    }

    public Type check(){
        Method m = associatedClass.getMethodByName(methodTk.getLexeme());
        if(m == null){
            throw new SemanticException(methodTk.getLineNumber(), "El metodo " + methodTk.getLexeme() + " no esta declarado", methodTk.getLexeme());
        }
        if (associatedMethod instanceof Method associatedM && associatedM.isStatic() && !m.isStatic()) {
            throw new SemanticException(methodTk.getLineNumber(),
                    "No se puede invocar al metodo dinamico " + m.getName() + " dede el metodo estatico " + associatedM.getName(),
                    methodTk.getLexeme());
        }

        if(params.size() != m.getParams().size()){
            throw new SemanticException(methodTk.getLineNumber(), "Cantidad de parametros incorrecta en llamada a " + methodTk.getLexeme(), methodTk.getLexeme());
        }

        for(int i = 0; i < params.size(); i++){
            Type argType = params.get(i).check();
            Type expectedType = m.getParams().get(i).getType();
            if(!expectedType.isCompatible(argType)){
                throw new SemanticException(methodTk.getLineNumber(), "El argumento " + (i + 1) + " es de tipo " + argType.getName() + " y se esperaba " + expectedType.getName(), methodTk.getLexeme());
            }
        }

        if(chain != null){
            return chain.check(m.getReturnType());
        } else{
            return m.getReturnType();
        }
    }

    @Override
    public void generate(boolean a) {
        Method m = associatedClass.getMethodByName(methodTk.getLexeme());
        if(m.isStatic()){
            if(!m.getReturnType().getName().equals("void")){
                OutputManager.gen("RMEM 1 ; Lugar para el retorno");;
            }
            for(ExpressionNode p : params){
                p.generate(false);
            }
            OutputManager.gen("PUSH lblMet" + methodTk.getLexeme() + "@" + m.getAssociatedClass().getName() +" ; apila el metodo");
            OutputManager.gen("CALL ; Llama al metodo en el tope de la pila");
        } else {
            if(!m.getReturnType().getName().equals("void")){
                OutputManager.gen("RMEM 1 ; Lugar para el retorno");
            }
            for(ExpressionNode p : params){
                p.generate(false);
            }
            OutputManager.gen("LOAD 3; Cargo this para metodo");
            OutputManager.gen("DUP");
            OutputManager.gen("LOADREF 0 ; Apilo el offset de la VT en el CIR (siempre 0)");
            OutputManager.gen("LOADREF " + m.getOffset() + "; Apilo el offset del metodo " + methodTk.getLexeme() + " en la VT");
            OutputManager.gen("CALL ; Llama al metodo en el tope de la pila");
        }
        if(chain != null){
            chain.generate(a);
        }
    }

    public String getReturnType(){
        Method m = associatedClass.getMethodByName(methodTk.getLexeme());
        return m.getReturnType().getName();
    }
}
