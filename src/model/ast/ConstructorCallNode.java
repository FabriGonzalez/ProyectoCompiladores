package model.ast;

import Analyzers.SymbolTable;
import exceptions.SemanticException;
import model.*;
import sourcemanager.OutputManager;

import java.util.List;

public class ConstructorCallNode extends PrimaryNode{
    Token ctorTk;
    List<ExpressionNode> params;

    public ConstructorCallNode(Token tk, List<ExpressionNode> l){
        ctorTk = tk;
        params = l;
    }

    @Override
    public Token getExpressionToken() {
        return ctorTk;
    }

    public Type check(){
        SymbolTable ts = SymbolTable.getInstance();
        Classs associatedClass = ts.getClass(ctorTk.getLexeme());

        if(associatedClass == null){
            throw new SemanticException(ctorTk.getLineNumber(), "No hay una clase con nombre " + ctorTk.getLexeme(), ctorTk.getLexeme());
        }
        Constructor associatedCtor = associatedClass.getCtor();
        if(associatedCtor == null){
            throw new SemanticException(ctorTk.getLineNumber(), "La clase de nombre " + ctorTk.getLexeme() + " no tiene constructor", ctorTk.getLexeme());
        }

        List<Param> paramsExpected = associatedCtor.getParams();
        if(paramsExpected.size() != params.size()){
            throw new SemanticException(ctorTk.getLineNumber(), "Cantidad de parametros incorrecta en la llamada al constructor de nombre " + ctorTk.getLexeme(), ctorTk.getLexeme());
        }

        for(int i = 0; i < params.size(); i++){
            Type argType = params.get(i).check();
            Type expectedType = paramsExpected.get(i).getType();
            if(!argType.isCompatible(expectedType)){
                throw new SemanticException(ctorTk.getLineNumber(), "El argumento " + (i + 1) + " es de tipo " + argType + " y se esperaba " + expectedType, ctorTk.getLexeme());
            }
        }


        ReferenceType refType = new ReferenceType(new Token("idClase", associatedClass.getName(), associatedClass.getDeclaredLine()));
        refType.setAssociatedClass(associatedClass);

        if(chain != null){
            return chain.check(refType);
        } else{
            return refType;
        }
    }

    @Override
    public void generate(boolean a) {
        SymbolTable ts = SymbolTable.getInstance();
        Classs associatedClass = ts.getClass(ctorTk.getLexeme());
        OutputManager.gen("RMEM 1 ; Reservamos memoria para el resultado del malloc (la referencia al nuevo CIR de " + associatedClass.getName() + ")");
        int vtAndVars = associatedClass.getAttributesSize() + 1;
        OutputManager.gen("PUSH " + vtAndVars + " ; Apilo la cantidad de var de instancia del CIR de "+ associatedClass.getName() + " +1 por VT");
        OutputManager.gen("PUSH simple_malloc ; La direccion de la rutina para alojar memoria en el heap");
        OutputManager.gen("CALL ; Llamo a malloc");
        OutputManager.gen("DUP ; Para no perder la referencia al nuevo CIR");
        OutputManager.gen("PUSH lblVT" + associatedClass.getName() + " ; Apilo la direccion del cominezo de la VT de la clase " + associatedClass.getName());
        OutputManager.gen("STOREREF 0 ; Guardamos la Referencia a la VT en el CIR que creamos");
        OutputManager.gen("DUP");
        for(ExpressionNode p : params){
            p.generate(false);
            OutputManager.gen("SWAP");
        }
        OutputManager.gen("PUSH lblConstructor@" + associatedClass.getName());
        OutputManager.gen("CALL ; Llamo al constructor de " + associatedClass.getName());
        if (chain != null){
            chain.generate(a);
        }
    }


}
