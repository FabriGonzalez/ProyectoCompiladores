package model;

import Analyzers.SymbolTable;
import exceptions.SemanticException;

import java.util.List;

public class Method extends GenericMethod{
    String modifier;
    Type returnType;

    public Method(Token tk, Type t){
        super(tk.getLexeme());
        returnType = t;
    }

    public Method(Token tk, Type t, Token m) {
        super(tk.getLexeme());
        returnType = t;
        modifier = m.getLexeme();
    }

    public void isCorrectDeclared(SymbolTable ts){
        boolean primitive = returnType instanceof PrimType;
        boolean voidType = returnType instanceof VoidType;
        if(!(primitive || voidType || ts.getClass(returnType.getName()) != null )){
            throw new SemanticException("Error tipo de retorno en metodo no declarado");
        }

        for (Param p : params) {
            p.isCorrectDeclared(ts);
        }
    }

    public boolean isAbstract(){
        return (modifier != null && modifier.equals("abstract"));
    }

    public Type getReturnType(){
        return returnType;
    }

    public List<Param> getParams(){
        return params;
    }

    public boolean isFinal(){
        return modifier != null && modifier.equals("final");
    }

    public boolean isStatic(){
        return modifier != null && modifier.equals("static");
    }

    public boolean sameSignature(Method m){
        if(!this.name.equals(m.getName())) return false;
        if(!this.returnType.getName().equals(m.getReturnType().getName())) return false;
        if(this.params.size() != m.getParams().size()) return false;

        for(int i = 0; i < this.params.size(); i++){
            Param p1 = this.params.get(i);
            Param p2 = m.getParams().get(i);
            if(! (p1.type.getName().equals(p2.type.getName())) ) return false;
        }

        return true;
    }
}
