package model.ast;

import exceptions.SemanticException;
import model.Classs;
import model.Method;
import model.Token;
import model.Type;

import java.util.List;

public class MethodCallNode extends PrimaryNode{
    Token methodTk;
    List<ExpressionNode> params;
    Classs associatedClass;

    public MethodCallNode(Token tk, List<ExpressionNode> l, Classs c){
        methodTk = tk;
        params = l;
        associatedClass = c;
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

        if(params.size() != m.getParams().size()){
            throw new SemanticException(methodTk.getLineNumber(), "Cantidad de parametros incorrecta en llamada a " + methodTk.getLexeme(), methodTk.getLexeme());
        }

        for(int i = 0; i < params.size(); i++){
            Type argType = params.get(i).check();
            Type expectedType = m.getParams().get(i).getType();
            if(!argType.isCompatible(expectedType)){
                throw new SemanticException(methodTk.getLineNumber(), "El argumento " + (i + 1) + " es de tipo " + argType + " y se esperaba " + expectedType, methodTk.getLexeme());
            }
        }

        if(chain != null){
            return chain.check(m.getReturnType());
        } else{
            return m.getReturnType();
        }
    }
}
