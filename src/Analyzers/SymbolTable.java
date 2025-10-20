package Analyzers;

import exceptions.SemanticException;
import model.*;
import model.ast.BlockNode;

import java.util.HashMap;

public class SymbolTable {
    HashMap<String, Classs> classes = new HashMap<>();
    Classs currentClass;
    GenericMethod currentMethod;
    BlockNode currentBlock;
    private static SymbolTable instance;
    TypeTable tb = new TypeTable();


    private SymbolTable() {
        loadPredefinedClasses();
    }

    public static SymbolTable getInstance(){
        if (instance == null){
            instance = new SymbolTable();
        }
        return instance;
    }

    public static void removeInstance(){
        instance = null;
    }


    public void setCurrentClass(Classs c){
        if(classExists(c.getName())){
            throw new SemanticException(c.getDeclaredLine(),"La clase " + c.getName() + " ya existe",c.getName());
        }
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

    public void setCurrentBlock(BlockNode b){
        currentBlock = b;
    }

    public BlockNode getCurrentBlock(){
        return currentBlock;
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
        return classes.get(nameClass);
    }

    public boolean classExists(String className){
        Classs clase = classes.get(className);
        return clase != null;
    }

    void addClass(Classs c){
        if (classes.containsKey(c.getName())) {
            throw new SemanticException(c.getDeclaredLine(), "Clase redeclarada: " + c.getName(), c.getName());
        }
        classes.put(c.getName(), c);
    }


    public Type resolveType(Token typeTk){
        return tb.getOrCreateType(typeTk);
    }

    public void currentMethodHasBlock(){
        currentMethod.setHasBlock();
    }

    private void loadPredefinedClasses(){
        Token tokenObject = new Token("idClase", "Object", 0);
        Token tokenString = new Token("idClase", "String", 0);
        Token tokenSystem = new Token("idClase", "System", 0);
        Token tokenStatic = new Token("static", "static", 0);

        Token tokenInt = new Token("prInt", "int", 0);
        Token tokenBoolean = new Token("prBoolean", "boolean", 0);
        Token tokenChar = new Token("prChar", "char", 0);
        Token tokenVoid = new Token("prVoid", "void", 0);

        Type typeInt = tb.getOrCreateType(tokenInt);
        Type typeBoolean = tb.getOrCreateType(tokenBoolean);
        Type typeChar = tb.getOrCreateType(tokenChar);
        Type typeVoid = tb.getOrCreateType(tokenVoid);
        Type typeString = tb.getOrCreateType(tokenString);

        Classs objectClass = new Classs(tokenObject, this);
        Method debugPrint = new Method(new Token("idMetVar", "debugPrint", 0), typeVoid, tokenStatic);
        Param p = new Param(new Token("idMetVar", "i", 0));
        p.setType(typeInt);
        debugPrint.addParam(p);
        debugPrint.setHasBlock();
        objectClass.addMethod(debugPrint);
        classes.put(objectClass.getName(), objectClass);

        Classs stringClass = new Classs(tokenString, this);
        stringClass.setParent(tokenObject);
        classes.put("String", stringClass);

        Classs systemClass = new Classs(tokenSystem, this);
        systemClass.setParent(tokenObject);

        Method read = new Method(new Token("idMetVar", "read", 0), typeInt, tokenStatic);
        systemClass.addMethod(read);
        read.setHasBlock();

        Method printB = new Method(new Token("idMetVar", "printB", 0), typeVoid, tokenStatic);
        Param pb = new Param(new Token("idMetVar", "b", 0));
        pb.setType(typeBoolean);
        printB.addParam(pb);
        systemClass.addMethod(printB);
        printB.setHasBlock();

        Method printC = new Method(new Token("idMetVar", "printC", 0), typeVoid, tokenStatic);
        Param pc = new Param(new Token("idMetVar", "c", 0));
        pc.setType(typeChar);
        printC.addParam(pc);
        systemClass.addMethod(printC);
        printC.setHasBlock();

        Method printI = new Method(new Token("idMetVar", "printI", 0), typeVoid, tokenStatic);
        Param pi = new Param(new Token("idMetVar", "i", 0));
        pi.setType(typeInt);
        printI.addParam(pi);
        systemClass.addMethod(printI);
        printI.setHasBlock();

        Method printS = new Method(new Token("idMetVar", "printS", 0), typeVoid, tokenStatic);
        Param ps = new Param(new Token("idMetVar", "s", 0));
        ps.setType(typeString);
        printS.addParam(ps);
        systemClass.addMethod(printS);
        printS.setHasBlock();

        Method println = new Method(new Token("idMetVar", "println", 0), typeVoid, tokenStatic);
        systemClass.addMethod(println);
        println.setHasBlock();

        Method printBln = new Method(new Token("idMetVar", "printBln", 0), typeVoid, tokenStatic);
        Param pbln = new Param(new Token("idMetVar", "b", 0));
        pbln.setType(typeBoolean);
        printBln.addParam(pbln);
        systemClass.addMethod(printBln);
        printBln.setHasBlock();

        Method printCln = new Method(new Token("idMetVar", "printCln", 0), typeVoid, tokenStatic);
        Param pcln = new Param(new Token("idMetVar", "c", 0));
        pcln.setType(typeChar);
        printCln.addParam(pcln);
        systemClass.addMethod(printCln);
        printCln.setHasBlock();

        Method printIln = new Method(new Token("idMetVar", "printIln", 0), typeVoid, tokenStatic);
        Param piln = new Param(new Token("idMetVar", "i", 0));
        piln.setType(typeInt);
        printIln.addParam(piln);
        systemClass.addMethod(printIln);
        printIln.setHasBlock();

        Method printSln = new Method(new Token("idMetVar", "printSln", 0), typeVoid, tokenStatic);
        Param psln = new Param(new Token("idMetVar", "s", 0));
        psln.setType(typeString);
        printSln.addParam(psln);
        systemClass.addMethod(printSln);
        printSln.setHasBlock();

        classes.put("System", systemClass);

        objectClass.isConsolidate();
        systemClass.isConsolidate();
        stringClass.isConsolidate();

        objectClass.setCtor(new Constructor(tokenObject, objectClass));
        systemClass.setCtor(new Constructor(tokenSystem, systemClass));
        stringClass.setCtor(new Constructor(tokenString, stringClass));
    }


}
