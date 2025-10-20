package model;

import Analyzers.SymbolTable;
import exceptions.SemanticException;
import exceptions.SyntacticException;

import java.util.HashMap;

public class Classs {
    String name;
    String modifier;
    HashMap<String, Attribute> attributes = new HashMap<>();
    Constructor ctor;
    HashMap<String, Method> methods = new HashMap<>();
    String parent;
    int declaredInLine;
    SymbolTable ts;
    boolean consolidated;

    public Classs(Token t, SymbolTable st){
        name = t.lexeme;
        declaredInLine = t.lineNumber;
        ts = st;
        if(name.equals("Object")){
            parent = null;
        } else {
            parent = "Object";
            consolidated = true;
        }
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setDeclaredInLine(int declaredInLine) {
        this.declaredInLine = declaredInLine;
    }

    public void setParent(Token tk){
        parent = tk.getLexeme();
    }

    public void addAttribute(Attribute a){
        attributes.put(a.getName(), a);
    }

    public Constructor getCtor(){
        return ctor;
    }

    public void isCorrectDeclared() {
        if (!name.equals("Object")) {
            if (parent == null || !ts.classExists(parent)) {
                throw new SemanticException("Error clase padre no esta declarada");
            }
        } else if (parent != null && parent.equals(this.name)) {
            throw new SemanticException("Error herencia circular en clase " + this.name);
        }

        for (Attribute a : attributes.values()) {
            a.isCorrectDeclared(ts);
        }

        for (Method m : methods.values()) {
            m.isCorrectDeclared(ts);
        }

        if(!isAbstract()){
            for (Method m : methods.values()) {
                if(m.isAbstract()){
                    throw new SemanticException("Metodo no abstractos en clases abstractas");
                }
            }
        }

        checkCtors();
    }

    public void checkCtors(){
        if(isAbstract()){
            if(ctor != null){
                throw new SemanticException("La clase abstracta tiene constructor");
            }
        } else {
            if(ctor == null){
                ctor = new Constructor(new Token("idClase", name, declaredInLine), this);
            } else {
                System.out.println(ctor.getName());
                if(!ctor.getName().equals(name)){
                    throw new SemanticException("El constructor de " + name + "debe tener el mismo nombre que la clase");
                }
            }
        }
    }

    public void consolidate(){
        if(consolidated) return;

        if(parent != null){
            Classs parentClass = ts.getClass(parent);

            if (isAbstract()) {
                if (!parentClass.isAbstract() && !parentClass.getName().equals("Object")) {
                    throw new SemanticException(
                            "Una clase abstracta no puede heredar de una clase concreta distinta de Object: " + parentClass.getName()
                    );
                }
            }

            parentClass.consolidate();

            checkCircularInheritance();

            for(Attribute a : parentClass.attributes.values()){
                if(this.attributes.containsKey(a.getName())){
                    throw new SemanticException("Error atributo heredado redeclarado");
                }
                this.attributes.put(a.getName(), a);
            }

            for(Method m: parentClass.methods.values()){
                if(this.methods.containsKey(m.getName())){
                    Method redef = this.methods.get(m.getName());

                    if(!redef.sameSignature(m)){
                        throw new SemanticException("Metodo mal redefinido en clase " + name + ": " + m.getName());
                    }

                    if(m.isFinal() || m.isStatic()){
                        throw new SemanticException("No se puede redefinir método final o static: " + m.getName());
                    }
                } else {
                    this.methods.put(m.getName(), m);
                }
            }

            if (!isAbstract()) {
                for (Method m : parentClass.methods.values()) {
                    if (m.isAbstract() && !this.methods.containsKey(m.getName())) {
                        throw new SemanticException("La clase " + name + " debe implementar el método abstracto " + m.getName());
                    }
                }
            }
        }

        consolidated = true;
    }

    void checkCircularInheritance(){
        Classs currentClass = this;
        while(currentClass.parent != null){
            Classs parentClass = ts.getClass(currentClass.parent);
            if(parentClass == this){
                throw new SemanticException("Error herencia circular en clase " + name);
            }
            currentClass = parentClass;
        }
    }

    public void addMethod(Method m){
        methods.put(m.getName(), m);
    }

    public boolean isAbstract(){
        return modifier != null && modifier.equals("abstract");
    }

    public void setCtor(Constructor c){
        ctor = c;
    }
}
