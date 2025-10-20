package Analyzers;

import exceptions.LexicalException;
import exceptions.SemanticException;
import exceptions.SyntacticException;
import model.*;

import java.io.IOException;

import static model.FirstSets.*;

public class SyntacticAnalyzer {
    LexicalAnalyzer aLex;
    Token currentToken;
    SymbolTable ts;

    public SyntacticAnalyzer(LexicalAnalyzer lex, SymbolTable ts) throws LexicalException, IOException, SyntacticException {
        aLex = lex;
        currentToken = aLex.nextToken();
        this.ts = ts;
        inicial();
    }

    void match(String tokenName) throws LexicalException, IOException, SyntacticException {
        if (tokenName.equals(currentToken.getId())) {
            currentToken = aLex.nextToken();
        } else {
            throw new SyntacticException(currentToken, tokenName);
        }
    }

    void inicial() throws LexicalException, SyntacticException, IOException {
        listaClases();
        match("$");
    }

    void listaClases() throws LexicalException, SyntacticException, IOException {
        if (pClase.contains(currentToken.getId())) {
            clase();
            listaClases();
        } else {

        }
    }

    void clase() throws LexicalException, SyntacticException, IOException {
        String modificador = modificadorOpcional();
        match("class");
        Token tk = currentToken;
        match("idClase");
        Classs newClass = new Classs(tk, ts);
        ts.setCurrentClass(newClass);
        ts.getCurrentClass().setModifier(modificador);
        Token parentName = herenciaOpcional();
        ts.getCurrentClass().setParent(parentName);
        match("{");
        listaMiembros();
        match("}");
    }

    String modificadorOpcional() throws LexicalException, SyntacticException, IOException {
        switch (currentToken.getId()) {
            case "abstract" -> {
                match("abstract");
                return "abstract";
            }
            case "static" -> {
                match("static");
                return "static";
            }
            case "final" -> {
                match("final");
                return "final";
            }
            default -> {
                return null;
            }
        }
    }

    Token herenciaOpcional() throws LexicalException, SyntacticException, IOException {
        if (currentToken.getId().equals("extends")) {
            match("extends");
            Token parentTk = currentToken;
            match("idClase");
            return parentTk;
        } else {
            return new Token("idClase", "Object", 0);
        }
    }

    void listaMiembros() throws SyntacticException, LexicalException, IOException {
        if (pMiembro.contains(currentToken.getId())) {
            miembro();
            listaMiembros();
        } else {

        }
    }

    void miembro() throws SyntacticException, LexicalException, IOException {
        if (pMetodoConModificador.contains(currentToken.getId())) {
            metodoConModificador();
        } else if (pMetodoOAtributo.contains(currentToken.getId())) {
            metodoOAtributo();
        } else if (pConstructor.contains(currentToken.getId())) {
            constructor();
        } else {
            throw new SyntacticException(currentToken, "metodo con o sin modificador, atributo o constructor");
        }
    }

    void metodoConModificador() throws SyntacticException, LexicalException, IOException {
        Token modifier = r_modificador();
        r_metodoConModificador(modifier);
    }

    Token r_modificador() throws SyntacticException, LexicalException, IOException {
        Token tk = currentToken;
        switch (currentToken.getId()) {
            case "abstract" -> match("abstract");
            case "static" -> match("static");
            case "final" -> match("final");
            default -> throw new SyntacticException(currentToken, "abstract, static o final");
        }
        return tk;
    }

    void r_metodoConModificador(Token modifier) throws LexicalException, SyntacticException, IOException {
        Token typeTk = tipoMetodo();
        Type type = ts.resolveType(typeTk);
        Token name = currentToken;
        match("idMetVar");
        Method newMethod = new Method(name, type, modifier);
        ts.setCurrentMethod(newMethod);

        ts.getCurrentClass().addMethod(newMethod);

        r_metodo();
    }

    void metodoOAtributo() throws LexicalException, SyntacticException, IOException {
        Token typeToken;
        Token currentTk;
        Type type;

        if(pTipo.contains(currentToken.getId())){
            typeToken = tipo();
            type = ts.resolveType(typeToken);
            currentTk = currentToken;

            match("idMetVar");

            r_metodoOAtributo(currentTk, type);
        } else if(currentToken.getId().equals("void")){
            currentTk = currentToken;

            match("void");

            type = ts.resolveType(currentTk);
            currentTk = currentToken;

            match("idMetVar");

            Method newMethod = new Method(currentTk, type);
            ts.setCurrentMethod(newMethod);
            ts.getCurrentClass().addMethod(newMethod);

            r_metodo();
        } else {
            throw new SyntacticException(currentToken, "boolean o char o int o void o idClase");
        }
    }

    void r_metodoOAtributo(Token currentTk, Type type) throws LexicalException, SyntacticException, IOException {
        if(pRMetodo.contains(currentToken.getId())){

            Method newMethod = new Method(currentTk, type);
            ts.setCurrentMethod(newMethod);
            ts.getCurrentClass().addMethod(newMethod);

            r_metodo();
        } else if(currentToken.getId().equals(";")){
            Attribute newAttrib = new Attribute(currentTk, type);

            ts.getCurrentClass().addAttribute(newAttrib);
            match(";");
        } else {
            throw new SyntacticException(currentToken, "( o ;");
        }
    }

    void r_metodo() throws LexicalException, SyntacticException, IOException {
        argsFormales();
        bloqueOpcional();
    }

    void constructor() throws SyntacticException, LexicalException, IOException {
       match("public");
       Token tk = currentToken;
       match("idClase");
       Constructor ctor = new Constructor(tk, ts.getCurrentClass());
       ts.setCurrentMethod(ctor);
       if(ts.getCurrentClass().getCtor() != null){
           throw new SemanticException("La clase actual " + ts.getCurrentClass().getName() + "ya tiene un constructor asociado");
       }
       ts.getCurrentClass().setCtor(ctor);
       argsFormales();
       bloque();
    }

    Token tipoMetodo() throws SyntacticException, LexicalException, IOException {
        if (pTipo.contains(currentToken.getId())) {
            return tipo();
        } else if (currentToken.getId().equals("void")) {
            Token tk = currentToken;
            match("void");
            return tk;
        } else {
            throw new SyntacticException(currentToken, "void o idClase o boolean o char o int");
        }
    }

    Token tipo() throws LexicalException, SyntacticException, IOException {
        if (pTipoPrimitivo.contains(currentToken.getId())) {
            return tipoPrimitivo();
        } else if (currentToken.getId().equals("idClase")) {
            Token tk = currentToken;
            match("idClase");
            return tk;
        } else {
            throw new SyntacticException(currentToken, "boolean o char o int o idClase");
        }
    }

    Token tipoPrimitivo() throws SyntacticException, LexicalException, IOException {
        Token tk = currentToken;
        switch (currentToken.getId()) {
            case "boolean" -> match("boolean");
            case "char" -> match("char");
            case "int" -> match("int");
            default -> throw new SyntacticException(currentToken, "boolean o char o int");
        }
        return tk;
    }

    void argsFormales() throws SyntacticException, LexicalException, IOException {
        match("(");
        listaArgsFormalesOpcional();
        match(")");
    }

    void listaArgsFormalesOpcional() throws LexicalException, SyntacticException, IOException {
        if(pListaArgsFormales.contains(currentToken.getId())){
            listaArgsFormales();
        } else {

        }
    }

    void listaArgsFormales() throws LexicalException, SyntacticException, IOException {
        r_argFormal_listaArgFormal();
    }

    void r_listaArgsFormales() throws LexicalException, SyntacticException, IOException {
        if(currentToken.getId().equals(",")){
            match(",");
            r_argFormal_listaArgFormal();
        } else {

        }
    }

    void r_argFormal_listaArgFormal() throws LexicalException, SyntacticException, IOException {
        argFormal();
        r_listaArgsFormales();
    }

    void argFormal() throws LexicalException, SyntacticException, IOException {
        Token typeTk = tipo();
        Type type = ts.resolveType(typeTk);

        Token tk = currentToken;
        match("idMetVar");

        Param newParam = new Param(tk);
        newParam.setType(type);
        ts.getCurrentMethod().addParam(newParam);
    }

    void bloqueOpcional() throws LexicalException, SyntacticException, IOException {
        if(pBloque.contains(currentToken.getId())){
            bloque();
        } else if(currentToken.getId().equals(";")){
            match(";");
        } else {
            throw new SyntacticException(currentToken, "{ o ;");
        }
    }

    void bloque() throws LexicalException, SyntacticException, IOException {
            match("{");
            listaSentencias();
            match("}");
    }

    void listaSentencias() throws LexicalException, SyntacticException, IOException {
        if(pSentencia.contains(currentToken.getId())){
            sentencia();
            listaSentencias();
        } else {

        }
    }

    void sentencia() throws LexicalException, SyntacticException, IOException {
        if(currentToken.getId().equals(";")){
            match(";");
        } else if (pVarLocal.contains(currentToken.getId())){
            varLocal();
            match(";");
        } else if(pReturn.contains(currentToken.getId())){
            met_return();
            match(";");
        } else if(pIf.contains(currentToken.getId())) {
            met_if();
        } else if(pWhile.contains(currentToken.getId())){
            met_while();
        } else if(pBloque.contains(currentToken.getId())){
            bloque();
        } else if(pAsignacionOLlamada.contains(currentToken.getId())){
            asignacionOLlamada();
            match(";");
        } else {
            throw new SyntacticException(currentToken, "; o varLocal o return o if o while o bloque o asignacion o llamda");
        }
    }

    void asignacionOLlamada() throws LexicalException, SyntacticException, IOException {
        expresion();
    }

    void varLocal() throws LexicalException, SyntacticException, IOException {
        match("var");
        match("idMetVar");
        match("=");
        expresionCompuesta();
    }

    void met_return() throws LexicalException, SyntacticException, IOException {
        match("return");
        expresionOpcional();
    }

    void expresionOpcional() throws LexicalException, SyntacticException, IOException {
        if(pExpresion.contains(currentToken.getId())){
            expresion();
        } else {

        }
    }

    void met_if() throws LexicalException, SyntacticException, IOException {
            match("if");
            match("(");
            expresion();
            match(")");
            sentencia();
            r_if();
    }

    void r_if() throws LexicalException, SyntacticException, IOException {
        if(currentToken.getId().equals("else")){
            match("else");
            sentencia();
        } else {

        }
    }

    void met_while() throws LexicalException, SyntacticException, IOException {
        match("while");
        match("(");
        expresion();
        match(")");
        sentencia();
    }

    void expresion() throws LexicalException, SyntacticException, IOException {
        expresionCompuesta();
        r_expresion();
    }

    void r_expresion() throws LexicalException, SyntacticException, IOException {
        if(pOperadorAsignacion.contains(currentToken.getId())){
            operadorAsignacion();
            expresionCompuesta();
        } else {

        }
    }

    void operadorAsignacion() throws LexicalException, SyntacticException, IOException {
        if (currentToken.getId().equals("=")) {
            match("=");
        } else {
            throw new SyntacticException(currentToken, "=");
        }
    }

    void expresionCompuesta() throws LexicalException, SyntacticException, IOException {
        expresionBasica();
        r_expresionCompuesta();
    }

    void r_expresionCompuesta() throws LexicalException, SyntacticException, IOException {
        if(pOperadorBinario.contains(currentToken.getId())){
            operadorBinario();
            expresionCompuesta();
        } else {

        }
    }

    void r_expresionBasica_compuesta() throws LexicalException, SyntacticException, IOException {
        expresionBasica();
        r_expresionCompuesta();
    }

    void operadorBinario() throws LexicalException, SyntacticException, IOException {
        switch (currentToken.getId()) {
            case "||" -> match("||");
            case "&&" -> match("&&");
            case "==" -> match("==");
            case "!=" -> match("!=");
            case "<" -> match("<");
            case ">" -> match(">");
            case "<=" -> match("<=");
            case ">=" -> match(">=");
            case "+" -> match("+");
            case "-" -> match("-");
            case "*" -> match("*");
            case "/" -> match("/");
            case "%" -> match("%");
            default -> throw new SyntacticException(currentToken, "operador binario");
        }
    }

    void expresionBasica() throws LexicalException, SyntacticException, IOException {
        if(pOperadorUnario.contains(currentToken.getId())){
            operadorUnario();
            operando();
        } else if(pOperando.contains(currentToken.getId())){
            operando();
        } else {
            throw new SyntacticException(currentToken, "un operador unario o un operando");
        }
    }

    void operadorUnario() throws LexicalException, SyntacticException, IOException {
        switch (currentToken.getId()){
            case "+" -> match("+");
            case "-" -> match("-");
            case "++" -> match("++");
            case "!" -> match("!");
            default -> throw new SyntacticException(currentToken, "operador unario");
        }
    }

    void operando() throws SyntacticException, LexicalException, IOException {
        if(pPrimitivo.contains(currentToken.getId())){
            primitivo();
        } else if(pReferencia.contains(currentToken.getId())){
            referencia();
        } else{
            throw new SyntacticException(currentToken, "operando");
        }
    }

    void primitivo() throws LexicalException, SyntacticException, IOException {
        switch (currentToken.getId()){
            case "true" -> match("true");
            case "false" -> match("false");
            case "intLiteral" -> match("intLiteral");
            case "charLiteral" -> match("charLiteral");
            case "null" -> match("null");
            default -> throw new SyntacticException(currentToken, "true o false o intLiteral o charLiteral o null");
        }
    }

    void referencia() throws LexicalException, SyntacticException, IOException {
            primario();
            r_referencia();
    }

    void r_referencia() throws LexicalException, SyntacticException, IOException {
        if(pVarOMetodoEncadenado.contains(currentToken.getId())){
            varOMetodoEncadenado();
            r_referencia();
        } else{

        }
    }

    void varOMetodoEncadenado() throws SyntacticException, LexicalException, IOException {
        match(".");
        match("idMetVar");
        r_varOMetodoEncadenado();
    }

    void r_varOMetodoEncadenado() throws LexicalException, SyntacticException, IOException {
        if(pArgsActuales.contains(currentToken.getId())){
            argsActuales();
        } else {

        }
    }

    void primario() throws LexicalException, SyntacticException, IOException {
        if(currentToken.getId().equals("this")){
            match("this");
        } else if(currentToken.getId().equals("stringLiteral")){
            match("stringLiteral");
        } else if(pLlamadaConstructor.contains(currentToken.getId())){
            llamadaConstructor();
        } else if(pLlamadaMetodoEstatico.contains(currentToken.getId())){
            llamadaMetodoEstatico();
        } else if(pExpresionParentizada.contains(currentToken.getId())){
            expresionParentizada();
        } else if(pAccesoVarOLlamadaAMetodo.contains(currentToken.getId())){
            accesoVarOLlamadadaAMetodo();
        } else {
            throw new SyntacticException(currentToken, "this o stringLiteral o new o idClase o ( o idMetVar");
        }
    }

    void accesoVarOLlamadadaAMetodo() throws LexicalException, SyntacticException, IOException {
        match("idMetVar");
        r_accesoVarOLlamadaAMetodo();
    }

    void r_accesoVarOLlamadaAMetodo() throws LexicalException, SyntacticException, IOException {
        if(pArgsActuales.contains(currentToken.getId())){
            argsActuales();
        } else {

        }
    }

    void llamadaConstructor() throws LexicalException, SyntacticException, IOException {
        match("new");
        match("idClase");
        argsActuales();
    }

    void expresionParentizada() throws LexicalException, SyntacticException, IOException {
            match("(");
            expresion();
            match(")");
    }

    void llamadaMetodoEstatico() throws LexicalException, SyntacticException, IOException {
        match("idClase");
        match(".");
        match("idMetVar");
        argsActuales();
    }

    void argsActuales() throws LexicalException, SyntacticException, IOException {
        match("(");
        listaExpsOpcional();
        match(")");
    }

    void listaExpsOpcional() throws LexicalException, SyntacticException, IOException {
        if(pListaExps.contains(currentToken.getId())){
            listaExps();
        }else {

        }
    }

    void listaExps() throws LexicalException, SyntacticException, IOException {
        expresion();
        r_listaExps();
    }

    void r_listaExps() throws LexicalException, SyntacticException, IOException {
        if(currentToken.getId().equals(",")){
            match(",");
            listaExps();
        } else {

        }
    }

    void r_exp_listaExps() throws LexicalException, SyntacticException, IOException {
        expresion();
        r_listaExps();
    }
}