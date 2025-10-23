package model;

import Analyzers.SymbolTable;
import exceptions.SemanticException;

import java.util.HashMap;

public class Classs {
    String name;
    String modifier;
    HashMap<String, Attribute> attributes = new HashMap<>();
    Constructor ctor;
    HashMap<String, Method> methods = new HashMap<>();
    String parent;
    int declaredLine;
    SymbolTable ts;
    boolean consolidated;

    public Classs(Token t, SymbolTable st) {
        name = t.lexeme;
        declaredLine = t.lineNumber;
        ts = st;
        if (name.equals("Object")) {
            parent = null;
        } else {
            parent = "Object";
            consolidated = false;
        }
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getDeclaredLine() {
        return declaredLine;
    }

    public void setParent(Token tk) {
        parent = tk.getLexeme();
    }

    public Classs getParentClass(){
        return ts.getClass(parent);
    }

    public void addAttribute(Attribute a) {
        if(attributes.get(a.getName()) != null){
            throw new SemanticException(a.getDeclaredLine(), " Atributo duplicado " + a.getName() + " en la clase " + name, a.getName());
        }
        attributes.put(a.getName(), a);
    }

    public Constructor getCtor() {
        return ctor;
    }

    public void isCorrectDeclared() {
        if (!name.equals("Object")) {
            if (parent == null || !ts.classExists(parent)) {
                throw new SemanticException(declaredLine, "la clase padre de la clase " + name + " no esta declarada", parent);
            }
        } else if (parent != null && parent.equals(this.name)) {
            throw new SemanticException(declaredLine, "Error herencia circular en clase " + name, name);
        }

        for (Attribute a : attributes.values()) {
            a.isCorrectDeclared(ts);
        }

        for (Method m : methods.values()) {
            m.isCorrectDeclared(ts);
        }

        if (!isAbstract()) {
            for (Method m : methods.values()) {
                if (m.isAbstract()) {
                    throw new SemanticException(m.getDeclaredLine(), "metodo abstracto en clase concreta: " + name, m.getName());
                }
            }
        }

        checkCtors();
    }

    public void checkCtors() {
        if (isAbstract()) {
            if (ctor != null) {
                throw new SemanticException(ctor.getDeclaredLine(), "La clase abstracta " + name + " tiene constructor", name);
            }
        } else {
            if (ctor == null) {
                ctor = new Constructor(new Token("idClase", name, declaredLine), this);
            } else {
                ctor.isCorrectDeclared(ts);
            }
        }
    }

    public void consolidate() {
        if (consolidated) return;

        if (parent != null) {
            Classs parentClass = ts.getClass(parent);

            if (isAbstract()) {
                if (!parentClass.isAbstract() && !parentClass.getName().equals("Object")) {
                    throw new SemanticException(declaredLine, "La clase abstracta " + name + "no puede heredar de una clase concreta distinta de Object: " + parentClass.getName(), name);
                }
            }

            if (parentClass.isFinal() || parentClass.isStatic()) {
                throw new SemanticException(declaredLine," la clase " + name + " hereda de un padre static o final", name);
            }

            checkCircularInheritance();

            parentClass.consolidate();


            for (Attribute a : parentClass.attributes.values()) {
                if (this.attributes.containsKey(a.getName())) {
                    throw new SemanticException(declaredLine, "atributo heredado redeclarado", name);
                }
                this.attributes.put(a.getName(), a);
            }

            for (Method m : parentClass.methods.values()) {
                if (this.methods.containsKey(m.getName())) {
                    Method redef = this.methods.get(m.getName());

                    if (!redef.sameSignature(m)) {
                        throw new SemanticException(redef.getDeclaredLine(), "Metodo mal redefinido en clase " + name + ": " + redef.getName(), redef.getName());
                    }

                    if(m.isAbstract()){
                        if(!redef.getHasBlock()){
                            throw new SemanticException(redef.getDeclaredLine(), "El metodo " + redef.getName() + " redefine al metodo abstracto de mismo nombre pero no tiene bloque", redef.getName());
                        } else if(redef.isStatic() || redef.isAbstract()){
                            throw new SemanticException(redef.getDeclaredLine(), "El metodo " + redef.getName() + " redefine al metodo abstracto de mismo nombre pero tiene modificador abstract o static", redef.getName());
                        }
                    }


                    if (m.isFinal() || m.isStatic()) {
                        throw new SemanticException(redef.getDeclaredLine(), "en clase " + name + " se redefine el metodo " + m.getName() + " el cual es final o static ", m.getName());
                    }

                    if(redef.isStatic()){
                        throw new SemanticException(redef.getDeclaredLine(), "Se redefine un metodo usando statico como modificador en la redefinicion", redef.getName());
                    }
                } else {
                    if(m.isAbstract() && !isAbstract()){
                        throw new SemanticException(declaredLine, "la clase " + name + " es concreta y no redefine el metodo abstracto " + m.getName(), name);
                    }
                    this.methods.put(m.getName(), m);
                }
            }
        }

        consolidated = true;
    }

    boolean isFinal() {
        return modifier != null && modifier.equals("final");
    }

    boolean isStatic(){
        return modifier != null && modifier.equals("static");
    }

    void checkCircularInheritance(){
        Classs currentClass = this;
        while(!currentClass.getName().equals("Object") && currentClass.parent != null){
            Classs parentClass = ts.getClass(currentClass.parent);
            if(parentClass.getName().equals(this.getName())){
                throw new SemanticException(declaredLine,"herencia circular en clase " + name, name);
            }
            currentClass = parentClass;
        }
    }

    public void addMethod(Method m) {
        if(methods.containsKey(m.getName())) {
            throw new SemanticException(m.getDeclaredLine(), "Metodo " + m.getName() + " repetido en clase: " + name , m.getName());
        }
        methods.put(m.getName(), m);
    }

    public boolean isAbstract(){
        return modifier != null && modifier.equals("abstract");
    }

    public void setCtor(Constructor c){
        ctor = c;
    }

    public void isConsolidate(){
        consolidated = true;
    }

    public Attribute getAttributeByName(String name){
        return attributes.get(name);
    }

    public Method getMethodByName(String name){
        return methods.get(name);
    }

    public void check(){
        if(ctor != null){
            ctor.check();
        }

        for(Method m : methods.values()){
            m.check();
        }
    }
}
