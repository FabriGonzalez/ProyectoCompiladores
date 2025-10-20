package model;

import Analyzers.SymbolTable;
import exceptions.SemanticException;

import java.util.List;

public class Method extends GenericMethod {
    String modifier;
    Type returnType;

    public Method(Token tk, Type t) {
        super(tk.getLexeme(), tk.getLineNumber());
        returnType = t;
    }

    public Method(Token tk, Type t, Token m) {
        super(tk.getLexeme(), tk.getLineNumber());
        returnType = t;
        modifier = m.getLexeme();
        hasBlock = false;
    }

    public String getModifier(){
        return modifier;
    }

    public void isCorrectDeclared(SymbolTable ts){

        if(hasBlock && isAbstract()){
            throw new SemanticException(declaredLine, "El metodo abstracto " + name + " tiene bloque", name);
        }

        if(!isAbstract() && !hasBlock){
            throw new SemanticException(declaredLine, "El metodo " + name + " no tiene bloque y no tiene modificador abstract", name);
        }

        Classs a = ts.getClass(returnType.getName());
        if(returnType instanceof ReferenceType){
            if(a != null){
                ((ReferenceType) returnType).setAssociatedClass(a);
            } else {
                throw new SemanticException(returnType.getDeclaredLine(), "el tipo de retorno del metodo " + name + " no existe", returnType.getName());
            }
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

    public Type getEffectiveReturnType() {
        return returnType;
    }
}
