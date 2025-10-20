package model;

import java.util.Set;

public class FirstSets {
    public static final Set<String> pInicial = Set.of("abstract", "static", "final", "class", "$");

    public static final Set<String> pListaClases = Set.of("abstract", "static", "final", "class");
    public static final Set<String> pClase = Set.of("abstract", "static", "final", "class");
    public static final Set<String> pModificadorOpcional = Set.of("abstract", "static", "final");
    public static final Set<String> pHerenciaOpcional = Set.of("extends");

    public static final Set<String> pListaMiembros = Set.of("abstract", "static", "final", "boolean", "char", "int", "idClase", "void", "public");
    public static final Set<String> pMiembro = Set.of("abstract", "static", "final", "boolean", "char", "int", "idClase", "void", "public");

    public static final Set<String> pMetodoConModificador = Set.of("abstract", "static", "final");
    public static final Set<String> pRModificadorOpcional = Set.of("abstract", "static", "final");
    public static final Set<String> pRMetodoConModificador = Set.of("boolean", "char", "int", "void");
    public static final Set<String> pMetodoOAtributo = Set.of("boolean", "char", "int", "idClase", "void");
    public static final Set<String> pRMetodoOAtributo = Set.of("(", ";");
    public static final Set<String> pRMetodo = Set.of("(");
    public static final Set<String> pConstructor = Set.of("public");
    public static final Set<String> pTipoMetodo = Set.of("boolean", "char", "int", "idClase", "void");
    public static final Set<String> pTipo = Set.of("boolean", "char", "int", "idClase");
    public static final Set<String> pTipoPrimitivo = Set.of("boolean", "char", "int");
    public static final Set<String> pArgsFormales = Set.of("(");
    public static final Set<String> pListaArgsFormalesOpcional = Set.of("boolean", "char", "int", "idClase");
    public static final Set<String> pListaArgsFormales = Set.of("boolean", "char", "int", "idClase");
    public static final Set<String> pRListaArgsFormales = Set.of(",");
    public static final Set<String> pRArgFormalListaArgFormal = Set.of("boolean", "char", "int", "idClase");
    public static final Set<String> pArgFormal = Set.of("boolean", "char", "int", "idClase");

    public static final Set<String> pBloqueOpcional = Set.of("{", ";");
    public static final Set<String> pBloque = Set.of("{");

    public static final Set<String> pListaSentencias = Set.of(";", "var", "return", "if", "while", "{", "+", "-", "++", "--", "!",
            "true", "false", "intLiteral", "charLiteral", "null", "this", "stringLiteral", "new",
            "idClase", "(", "idMetVar");
    public static final Set<String> pSentencia = Set.of(";", "var", "return", "if", "while", "{", "+", "-", "++", "--", "!",
            "true", "false", "intLiteral", "charLiteral", "null", "this", "stringLiteral", "new",
            "idClase", "(", "idMetVar");
    public static final Set<String> pAsignacionOLlamada = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");

    public static final Set<String> pVarLocal = Set.of("var");
    public static final Set<String> pReturn = Set.of("return");
    public static final Set<String> pExpresionOpcional = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");

    public static final Set<String> pIf = Set.of("if");
    public static final Set<String> pRIf = Set.of("else");
    public static final Set<String> pWhile = Set.of("while");

    public static final Set<String> pExpresion = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    public static final Set<String> pRExpresion = Set.of("=");
    public static final Set<String> pOperadorAsignacion = Set.of("=");

    public static final Set<String> pExpresionCompuesta = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    public static final Set<String> pRExpresionCompuesta = Set.of("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%");

    public static final Set<String> pRExpresionBasicaCompuesta = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");

    public static final Set<String> pOperadorBinario = Set.of("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%");
    public static final Set<String> pExpresionBasica = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    public static final Set<String> pOperadorUnario = Set.of("+", "-", "++", "--", "!");
    public static final Set<String> pOperando = Set.of("true", "false", "intLiteral", "charLiteral", "null", "this",
            "stringLiteral", "new", "idClase", "(", "idMetVar");
    public static final Set<String> pPrimitivo = Set.of("true", "false", "intLiteral", "charLiteral", "null");
    public static final Set<String> pReferencia = Set.of("this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    public static final Set<String> pRReferencia = Set.of(".");
    public static final Set<String> pVarOMetodoEncadenado = Set.of(".");
    public static final Set<String> pRVarOMetodoEncadenado = Set.of("(");
    public static final Set<String> pPrimario = Set.of("this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    public static final Set<String> pAccesoVarOLlamadaAMetodo = Set.of("idMetVar");
    public static final Set<String> pRAccesoVarOLlamadaMetodo = Set.of("(");
    public static final Set<String> pLlamadaConstructor = Set.of("new");
    public static final Set<String> pExpresionParentizada = Set.of("(");
    public static final Set<String> pLlamadaMetodoEstatico = Set.of("idClase");
    public static final Set<String> pArgsActuales = Set.of("(");
    public static final Set<String> pListaExpsOpcional = Set.of("+", "++", "-", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    public static final Set<String> pListaExps = Set.of("+", "++", "-", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");
    public static final Set<String> pRListaExps = Set.of(",");
    public static final Set<String> pRExpListaExps = Set.of("+", "-", "++", "--", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "new", "idClase", "(", "idMetVar");
}
