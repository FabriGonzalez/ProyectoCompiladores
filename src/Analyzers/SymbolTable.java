package Analyzers;

import exceptions.SemanticException;
import model.*;
import model.ast.*;
import sourcemanager.OutputManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SymbolTable {
    List<Classs> classes = new LinkedList<>();
    Classs currentClass;
    GenericMethod currentMethod;
    BlockNode currentBlock;
    private static SymbolTable instance;
    TypeTable tb = new TypeTable();
    OutputManager fileManager;



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
        classes.add(c);
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
        for (Classs c : classes) {
            c.isCorrectDeclared();
        }
    }

    public void consolidateAllClasses(){
        for (Classs c : classes){
            c.consolidate();
        }
    }

    public void generate(){
        OutputManager.gen(".CODE");
        Classs mainClass = getMainClass();
        if(mainClass == null){
            throw new SemanticException("Falta el metodo main en el archivo");
        }
        OutputManager.gen("PUSH lblMetmain@" + mainClass.getName());
        OutputManager.gen("CALL");
        OutputManager.gen("HALT");
        generatePrims();
        for(Classs c : classes){
            c.generate();
            OutputManager.gen("");
        }
    }

    private void generatePrims(){
        OutputManager.gen("simple_heap_init: RET 0	; Retorna inmediatamente");
        OutputManager.gen("simple_malloc: LOADFP	; Inicialización unidad");
        OutputManager.gen("LOADSP");
        OutputManager.gen("STOREFP ; Finaliza inicialización del RA");
        OutputManager.gen("LOADHL	; hl");
        OutputManager.gen("DUP	; hl");
        OutputManager.gen("PUSH 1	; 1");
        OutputManager.gen("ADD	; hl+1");
        OutputManager.gen("STORE 4 ; Guarda el resultado (un puntero a la primer celda de la región de memoria)");
        OutputManager.gen("LOAD 3	; Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)");
        OutputManager.gen("ADD");
        OutputManager.gen("STOREHL ; Mueve el heap limit (hl). Expande el heap");
        OutputManager.gen("STOREFP");
        OutputManager.gen("RET 1");
    }

    private Classs getMainClass(){
        Classs toReturn = null;
        for(Classs c : classes){
            if(c.getMethodByName("main") != null){
                toReturn = c;
                break;
            };
        }
        return toReturn;
    }

    public Classs getClass(String className){
        for(Classs c : classes){
            if(c.getName().equals(className)){
                return c;
            }
        }
        return null;
    }

    public boolean classExists(String className){
        for(Classs c : classes){
            if(c.getName().equals(className)){
                return true;
            }
        }
        return false;
    }

    public void check(){
        for (Classs c : classes){
            c.check();
        }
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
        Method debugPrint = new Method(new Token("idMetVar", "debugPrint", 0), typeVoid, tokenStatic, objectClass);
        Param p = new Param(new Token("idMetVar", "i", 0));
        p.setType(typeInt);
        debugPrint.addParam(p);
        debugPrint.setHasBlock();
        objectClass.addMethod(debugPrint);
        debugPrint.setBlock(new DebugPrintBlockNode());
        classes.add(objectClass);

        Classs stringClass = new Classs(tokenString, this);
        stringClass.setParent(tokenObject);
        classes.add( stringClass);

        Classs systemClass = new Classs(tokenSystem, this);
        systemClass.setParent(tokenObject);

        Method read = new Method(new Token("idMetVar", "read", 0), typeInt, tokenStatic, systemClass);
        systemClass.addMethod(read);
        read.setHasBlock();
        read.setBlock(new ReadBlockNode());

        Method printB = new Method(new Token("idMetVar", "printB", 0), typeVoid, tokenStatic, systemClass);
        Param pb = new Param(new Token("idMetVar", "b", 0));
        pb.setType(typeBoolean);
        printB.addParam(pb);
        systemClass.addMethod(printB);
        printB.setHasBlock();
        printB.setBlock(new PrintBBlockNode());

        Method printC = new Method(new Token("idMetVar", "printC", 0), typeVoid, tokenStatic, systemClass);
        Param pc = new Param(new Token("idMetVar", "c", 0));
        pc.setType(typeChar);
        printC.addParam(pc);
        systemClass.addMethod(printC);
        printC.setHasBlock();
        printC.setBlock(new PrintCBlockNode());

        Method printI = new Method(new Token("idMetVar", "printI", 0), typeVoid, tokenStatic, systemClass);
        Param pi = new Param(new Token("idMetVar", "i", 0));
        pi.setType(typeInt);
        printI.addParam(pi);
        systemClass.addMethod(printI);
        printI.setHasBlock();
        printI.setBlock(new PrintIBlockNode());

        Method printS = new Method(new Token("idMetVar", "printS", 0), typeVoid, tokenStatic, systemClass);
        Param ps = new Param(new Token("idMetVar", "s", 0));
        ps.setType(typeString);
        printS.addParam(ps);
        systemClass.addMethod(printS);
        printS.setHasBlock();
        printS.setBlock(new PrintSBlockNode());

        Method println = new Method(new Token("idMetVar", "println", 0), typeVoid, tokenStatic, systemClass);
        systemClass.addMethod(println);
        println.setHasBlock();
        println.setBlock(new PrintLnBlockNode());

        Method printBln = new Method(new Token("idMetVar", "printBln", 0), typeVoid, tokenStatic, systemClass);
        Param pbln = new Param(new Token("idMetVar", "b", 0));
        pbln.setType(typeBoolean);
        printBln.addParam(pbln);
        systemClass.addMethod(printBln);
        printBln.setHasBlock();
        printBln.setBlock(new PrintBlnBlockNode());

        Method printCln = new Method(new Token("idMetVar", "printCln", 0), typeVoid, tokenStatic, systemClass);
        Param pcln = new Param(new Token("idMetVar", "c", 0));
        pcln.setType(typeChar);
        printCln.addParam(pcln);
        systemClass.addMethod(printCln);
        printCln.setHasBlock();
        printCln.setBlock(new PrintClnBlockNode());

        Method printIln = new Method(new Token("idMetVar", "printIln", 0), typeVoid, tokenStatic, systemClass);
        Param piln = new Param(new Token("idMetVar", "i", 0));
        piln.setType(typeInt);
        printIln.addParam(piln);
        systemClass.addMethod(printIln);
        printIln.setHasBlock();
        printIln.setBlock(new PrintIlnBlockNode());

        Method printSln = new Method(new Token("idMetVar", "printSln", 0), typeVoid, tokenStatic, systemClass);
        Param psln = new Param(new Token("idMetVar", "s", 0));
        psln.setType(typeString);
        printSln.addParam(psln);
        systemClass.addMethod(printSln);
        printSln.setHasBlock();
        printSln.setBlock(new PrintSlnBlockNode());

        classes.add(systemClass);

        objectClass.isConsolidate();
        systemClass.isConsolidate();
        stringClass.isConsolidate();

        objectClass.setCtor(new Constructor(tokenObject, objectClass));
        systemClass.setCtor(new Constructor(tokenSystem, systemClass));
        stringClass.setCtor(new Constructor(tokenString, stringClass));
    }
}
