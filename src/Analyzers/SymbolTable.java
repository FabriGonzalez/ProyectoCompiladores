package Analyzers;

import exceptions.SemanticException;
import model.*;

import java.util.HashMap;

public class SymbolTable {
    HashMap<String, Classs> classes = new HashMap<>();
    Classs currentClass;
    GenericMethod currentMethod;
    TypeTable tb = new TypeTable();

    public SymbolTable() {
        loadPredefinedClasses();
    }


    public void setCurrentClass(Classs c){
        currentClass = c;
        classes.put(c.getName(), c);
    }

    public Classs getCurrentClass(){
        return currentClass;
    }

    public void setCurrentMethod(GenericMethod m){
        currentMethod = m;
    }

    public GenericMethod getCurrentMethod(){
        return currentMethod;
    }

    public void checkDeclarations() {
        for (Classs c : classes.values()) {
            c.isCorrectDeclared();
        }
    }

    public void consolidateAllClasses(){
        for (Classs c : classes.values()){
            c.consolidate();
        }
    }

    public Classs getClass(String nameClass){
        Classs toReturn = classes.get(nameClass);
        if(toReturn == null && nameClass.equals("Object")){
            toReturn = new Classs(new Token("IdClase", "Object", 0), this);
        }
        return toReturn;
    }

    public boolean classExists(String className){
        Classs clase = classes.get(className);
        return clase != null;
    }

    void addClass(Classs c){
        if (classes.containsKey(c.getName())) {
            throw new SemanticException("Clase redeclarada: " + c.getName());
        }
        classes.put(c.getName(), c);
    }

    public Type resolveType(Token typeTk){
        return tb.getOrCreateRefType(typeTk.getLexeme());
    }

    private void loadPredefinedClasses(){
        Classs objectClass = new Classs(new Token("idClase", "Object", 0), this);
        Method debugPrint = new Method(new Token("idMetVar", "debugPrint", 0), new VoidType(), new Token("static", "static", 0));
        Param p = new Param(new Token("idMetVar", "i", 0));
        p.setType(new PrimType("int"));
        debugPrint.addParam(p);
        objectClass.addMethod(debugPrint);
        classes.put(objectClass.getName(), objectClass);

        Classs stringClass = new Classs(new Token("idClase", "String", 0), this);
        stringClass.setParent(new Token("idClase","Object",0));
        classes.put("String", stringClass);

        Classs systemClass = new Classs(new Token("idClase", "System", 0), this);
        systemClass.setParent(new Token("idClase","Object",0));
        Token staticTk = new Token("static", "static", 0);
        Method read = new Method(new Token("idMetVar", "read", 0), new PrimType("int"), staticTk);
        systemClass.addMethod(read);

        Method printB = new Method(new Token("idMetVar", "printB", 0), new VoidType(), staticTk);
        Param pb = new Param(new Token("idMetVar", "b", 0));
        pb.setType(new PrimType("boolean"));
        printB.addParam(pb);
        systemClass.addMethod(printB);

        Method printC = new Method(new Token("idMetVar", "printC", 0), new VoidType(), staticTk);
        Param pc = new Param(new Token("idMetVar", "c", 0));
        pc.setType(new PrimType("char"));
        printC.addParam(pc);
        systemClass.addMethod(printC);

        Method printI = new Method(new Token("idMetVar", "printI", 0), new VoidType(), staticTk);
        Param pi = new Param(new Token("idMetVar", "i", 0));
        pi.setType(new PrimType("int"));
        printI.addParam(pi);
        systemClass.addMethod(printI);

        Method printS = new Method(new Token("idMetVar", "printS", 0), new VoidType(), staticTk);
        Param ps = new Param(new Token("idMetVar", "s", 0));
        ps.setType(new ReferenceType("String"));
        printS.addParam(ps);
        systemClass.addMethod(printS);

        Method println = new Method(new Token("idMetVar","println",0), new VoidType(), staticTk);
        systemClass.addMethod(println);

        Method printBln = new Method(new Token("idMetVar","printBln",0), new VoidType(), staticTk);
        Param pbln = new Param(new Token("idMetVar","b",0));
        pbln.setType(new PrimType("boolean"));
        printBln.addParam(pbln);
        systemClass.addMethod(printBln);

        Method printCln = new Method(new Token("idMetVar","printCln",0), new VoidType(), staticTk);
        Param pcln = new Param(new Token("idMetVar","c",0));
        pcln.setType(new PrimType("char"));
        printCln.addParam(pcln);
        systemClass.addMethod(printCln);

        Method printIln = new Method(new Token("idMetVar","printIln",0), new VoidType(), staticTk);
        Param piln = new Param(new Token("idMetVar","i",0));
        piln.setType(new PrimType("int"));
        printIln.addParam(piln);
        systemClass.addMethod(printIln);

        Method printSln = new Method(new Token("idMetVar","printSln",0), new VoidType(), staticTk);
        Param psln = new Param(new Token("idMetVar","s",0));
        psln.setType(new ReferenceType("String"));
        printSln.addParam(psln);
        systemClass.addMethod(printSln);

        classes.put("System", systemClass);
    }
}
