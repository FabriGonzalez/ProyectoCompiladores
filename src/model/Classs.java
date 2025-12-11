package model;

import Analyzers.SymbolTable;
import exceptions.SemanticException;
import model.ast.BlockNode;
import model.ast.StatementNode;
import org.w3c.dom.Attr;
import sourcemanager.OutputManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class Classs {
    String name;
    String modifier;
    LinkedHashMap<String, Attribute> attributes = new LinkedHashMap<>();
    LinkedHashMap<String, Method> methods = new LinkedHashMap<>();
    Constructor ctor;
    String parent;
    int declaredLine;
    SymbolTable ts;
    boolean consolidated;
    int nextAttrOffset = 1;
    int nextMetOffset = 0;

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

    public void setNextAttrOffset(int off){
        nextAttrOffset = off;
    }

    public int getNextAttrOffset(){
        return nextAttrOffset;
    }

    public int getAttributesSize(){
        return attributes.size();
    }

    public void setNextMetOffset(int off){
        nextMetOffset = off;
    }

    public int getNextMetOffset(){
        return nextMetOffset;
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
                BlockNode b = new BlockNode();
                List<StatementNode> l = new LinkedList<>();
                b.setSents(l);
                ctor.setBlock(b);
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
                    if(a.getDeclaredLine() > declaredLine){
                        throw new SemanticException(a.getDeclaredLine(), "atributo heredado redeclarado", a.getName());
                    } else {
                        Attribute attr = attributes.get(a.getName());
                        throw new SemanticException(attr.getDeclaredLine(), "atributo heredado redeclarado", attr.getName());
                    }

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
                        throw new SemanticException(redef.getDeclaredLine(), "Se redefine un metodo usando static como modificador en la redefinicion", redef.getName());
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

        assignOffsets();
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

    public void generate() {
        OutputManager.gen(".DATA");

        if (hasDynamicMethods()) {

            List<Method> dynMethods = new LinkedList<>();

            for (Method m : methods.values()) {
                if (!m.isStatic()) {
                    dynMethods.add(m);
                }
            }
            dynMethods.sort((m1, m2) -> Integer.compare(m1.getOffset(), m2.getOffset()));

            boolean first = true;
            for (Method m : dynMethods) {
                String label =
                        "lblMet" + m.getName() + "@" + m.getAssociatedClass().getName();

                if (first) {
                    OutputManager.gen("lblVT" + name + ": DW " + label);
                    first = false;
                } else {
                    OutputManager.gen("DW " + label);
                }
            }

        } else {
            OutputManager.gen("lblVT" + name + ": NOP");
        }
        OutputManager.gen("");
        OutputManager.gen(".CODE");
        ctor.generate();
        for (Method m : methods.values()) {
            m.generate();
        }
    }


    private boolean hasDynamicMethods(){
        for(Method m : methods.values()){
            if(!m.isStatic()){
                return true;
            }
        }
        return false;
    }
    private void assignOffsets(){
        if(parent != null){
            Classs parentClass = ts.getClass(parent);
            nextAttrOffset = parentClass.getNextAttrOffset();
            nextMetOffset = parentClass.getNextMetOffset();
            for(Attribute a : attributes.values()){
                if(parentClass.getAttributeByName(a.getName()) == null){
                    a.setOffset(nextAttrOffset++);
                }
            }
            for(Method m : methods.values()){
                if(!m.isStatic()){
                    Method parentMethod = getParentMethod(m.getName());
                    if(parentMethod == null){
                        m.setOffset(nextMetOffset++);
                    }else {
                        m.setOffset(parentMethod.getOffset());
                    }
                }
            }
        }
    }

    private Method getParentMethod(String name){
        Classs current = getParentClass();
        while(current != null){
            Method m = current.getMethodByName(name);
            if(m != null){
                return m;
            }
            current = current.getParentClass();
        }
        return null;
    }
}