import exceptions.LexicalException;
import exceptions.SyntacticException;
import model.Token;

import java.io.IOException;
import java.util.Set;

public class SyntacticAnalyzer {
    LexicalAnalyzer aLex;
    Token currentToken;

    private static final Set<String> pInicial = Set.of("abstract", "static", "final", "class", "$");

    private static final Set<String> pListaClases = Set.of("abstract", "static", "final", "class");
    private static final Set<String> pClase = Set.of("abstract", "static", "final", "class");
    private static final Set<String> pModificadorOpcional = Set.of("abstract", "static", "final");
    private static final Set<String> pHerenciaOpcional = Set.of("extends");

    private static final Set<String> pListaMiembros = Set.of("abstract", "static", "final", "boolean", "char", "int", "idClase", "void", "public");
    private static final Set<String> pMiembro = Set.of("abstract", "static", "final", "boolean", "char", "int", "idClase", "void", "public");

    private static final Set<String> pMetodoConModificador = Set.of("abstract", "static", "final");
    private static final Set<String> pRModificadorOpcional = Set.of("abstract", "static", "final");
    private static final Set<String> pRMetodoConModificador = Set.of("boolean", "char", "int", "void");
    private static final Set<String> pMetodoOAtributo = Set.of("boolean", "char", "int", "idClase", "void");
    private static final Set<String> pRMetodoOAtributo = Set.of("(", ";");
    private static final Set<String> pRMetodo = Set.of("(");
    private static final Set<String> pConstructor = Set.of("public");
    private static final Set<String> pTipoMetodo = Set.of("boolean", "char", "int", "idClase", "void");
    private static final Set<String> pTipo = Set.of("boolean", "char", "int", "idClase");
    private static final Set<String> pTipoPrimitivo = Set.of("boolean", "char", "int");
    private static final Set<String> pArgsFormales = Set.of("(");
    private static final Set<String> pListaArgsFormalesOpcional = Set.of("boolean", "char", "int", "idClase");
    private static final Set<String> pListaArgsFormales = Set.of("boolean", "char", "int", "idClase");
    private static final Set<String> pRListaArgsFormales = Set.of(",");
    private static final Set<String> pRArgFormalListaArgFormal = Set.of("boolean", "char", "int", "idClase");
    private static final Set<String> pArgFormal = Set.of("boolean", "char", "int", "idClase");

    private static final Set<String> pBloqueOpcional = Set.of("{", ";");
    private static final Set<String> pBloque = Set.of("{");

    private static final Set<String> pListaSentencias = Set.of(";", "var", "return", "if", "while", "{", "+", "-", "++", "--", "!",
            "true", "false", "intLiteral", "charLiteral", "null", "this", "stringLiteral", "new",
            "idClase", "(", "idMetVar");
    private static final Set<String> pSentencia = Set.of(";", "var", "return", "if", "while", "{", "+", "-", "++", "--", "!",
            "true", "false", "intLiteral", "charLiteral", "null", "this", "stringLiteral", "new",
            "idClase", "(", "idMetVar");
    private static final Set<String> pAsignacionOLlamada = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");

    private static final Set<String> pVarLocal = Set.of("var");
    private static final Set<String> pReturn = Set.of("return");
    private static final Set<String> pExpresionOpcional = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");

    private static final Set<String> pIf = Set.of("if");
    private static final Set<String> pRIf = Set.of("else");
    private static final Set<String> pWhile = Set.of("while");

    private static final Set<String> pExpresion = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    private static final Set<String> pRExpresion = Set.of("=");
    private static final Set<String> pOperadorAsignacion = Set.of("=");

    private static final Set<String> pExpresionCompuesta = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    private static final Set<String> pRExpresionCompuesta = Set.of("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%");

    private static final Set<String> pRExpresionBasicaCompuesta = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");

    private static final Set<String> pOperadorBinario = Set.of("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%");
    private static final Set<String> pExpresionBasica = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    private static final Set<String> pOperadorUnario = Set.of("+", "-", "++", "--", "!");
    private static final Set<String> pOperando = Set.of("true", "false", "intLiteral", "charLiteral", "null", "this",
            "stringLiteral", "new", "idClase", "(", "idMetVar");
    private static final Set<String> pPrimitivo = Set.of("true", "false", "intLiteral", "charLiteral", "null");
    private static final Set<String> pReferencia = Set.of("this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    private static final Set<String> pRReferencia = Set.of(".");
    private static final Set<String> pVarOMetodoEncadenado = Set.of(".");
    private static final Set<String> pRVarOMetodoEncadenado = Set.of("(");
    private static final Set<String> pPrimario = Set.of("this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    private static final Set<String> pAccesoVarOLlamadaAMetodo = Set.of("idMetVar");
    private static final Set<String> pRAccesoVarOLlamadaMetodo = Set.of("(");
    private static final Set<String> pLlamadaConstructor = Set.of("new");
    private static final Set<String> pExpresionParentizada = Set.of("(");
    private static final Set<String> pLlamadaMetodoEstatico = Set.of("idClase");
    private static final Set<String> pArgsActuales = Set.of("(");
    private static final Set<String> pListaExpsOpcional = Set.of("+", "++", "-", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    private static final Set<String> pListaExps = Set.of("+", "++", "-", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    private static final Set<String> pRListaExps = Set.of(",");
    private static final Set<String> pRExpListaExps = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");


    public SyntacticAnalyzer(LexicalAnalyzer lex) throws LexicalException, IOException, SyntacticException {
        aLex = lex;
        currentToken = aLex.nextToken();
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
        modificadorOpcional();
        match("class");
        match("idClase");
        herenciaOpcional();
        match("{");
        listaMiembros();
        match("}");
    }

    void modificadorOpcional() throws LexicalException, SyntacticException, IOException {
        switch (currentToken.getId()) {
            case "abstract" -> match("abstract");
            case "static" -> match("static");
            case "final" -> match("final");
            default -> {
            }
        }
    }

    void herenciaOpcional() throws LexicalException, SyntacticException, IOException {
        if (currentToken.getId().equals("extends")) {
            match("extends");
            match("idClase");
        } else {

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
        r_modificadorOpcional();
        r_metodoConModificador();
    }

    void r_modificadorOpcional() throws SyntacticException, LexicalException, IOException {
        switch (currentToken.getId()) {
            case "abstract" -> match("abstract");
            case "static" -> match("static");
            case "final" -> match("final");
            default -> throw new SyntacticException(currentToken, "abstract, static o final");
        }
    }

    void r_metodoConModificador() throws LexicalException, SyntacticException, IOException {
        tipoMetodo();
        match("idMetVar");
        r_metodo();
    }

    void metodoOAtributo() throws LexicalException, SyntacticException, IOException {
        if(pTipo.contains(currentToken.getId())){
            tipo();
            match("idMetVar");
            r_metodoOAtributo();
        } else if(currentToken.getId().equals("void")){
            match("void");
            match("idMetVar");
            r_metodo();
        } else {
            throw new SyntacticException(currentToken, "boolean o char o int o void o idClase");
        }
    }

    void r_metodoOAtributo() throws LexicalException, SyntacticException, IOException {
        if(pRMetodo.contains(currentToken.getId())){
            r_metodo();
        } else if(currentToken.getId().equals(";")){
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
       match("idClase");
       argsFormales();
       bloque();
    }

    void tipoMetodo() throws SyntacticException, LexicalException, IOException {
        if (pTipo.contains(currentToken.getId())) {
            tipo();
        } else if (currentToken.getId().equals("void")) {
            match("void");
        } else {
            throw new SyntacticException(currentToken, "void o idClase o boolean o char o int");
        }
    }

    void tipo() throws LexicalException, SyntacticException, IOException {
        if (pTipoPrimitivo.contains(currentToken.getId())) {
            tipoPrimitivo();
        } else if (currentToken.getId().equals("idClase")) {
            match("idClase");
            argumentoGenericoOpcional();
        } else {
            throw new SyntacticException(currentToken, "boolean o char o int o idClase");
        }
    }

    void argumentoGenericoOpcional() throws LexicalException, SyntacticException, IOException {
        if(currentToken.getId().equals("<")){
            match("<");
            //Seguir logro
        } else {

        }
    }

    void tipoPrimitivo() throws SyntacticException, LexicalException, IOException {
        switch (currentToken.getId()) {
            case "boolean" -> match("boolean");
            case "char" -> match("char");
            case "int" -> match("int");
            default -> throw new SyntacticException(currentToken, "boolean o char o int");
        }
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
        tipo();
        match("idMetVar");
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