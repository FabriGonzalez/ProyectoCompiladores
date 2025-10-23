package Analyzers;

import exceptions.LexicalException;
import exceptions.SemanticException;
import exceptions.SyntacticException;
import model.*;
import model.ast.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
        BlockNode block = bloqueOpcional();
        ts.getCurrentMethod().setBlock(block);
    }

    void constructor() throws SyntacticException, LexicalException, IOException {
       match("public");
       Token tk = currentToken;
       match("idClase");
       Constructor ctor = new Constructor(tk, ts.getCurrentClass());
       ts.setCurrentMethod(ctor);
       if(ts.getCurrentClass().getCtor() != null){
           throw new SemanticException(ctor.getDeclaredLine(),"La clase actual " + ts.getCurrentClass().getName() + "ya tiene un constructor asociado", ctor.getName());
       }
       ts.getCurrentClass().setCtor(ctor);
       argsFormales();
       BlockNode block = bloque();
       ts.getCurrentMethod().setBlock(block);
       ts.getCurrentMethod().setHasBlock();
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

    BlockNode bloqueOpcional() throws LexicalException, SyntacticException, IOException {
        if(pBloque.contains(currentToken.getId())){
            BlockNode block = bloque();
            ts.currentMethodHasBlock();
            return block;
        } else if(currentToken.getId().equals(";")){
            match(";");
            return null;
        } else {
            throw new SyntacticException(currentToken, "{ o ;");
        }
    }

    BlockNode bloque() throws LexicalException, SyntacticException, IOException {
        match("{");
        BlockNode newBlock = new BlockNode();
        newBlock.setParentBlock(ts.getCurrentBlock());
        ts.setCurrentBlock(newBlock);
        List<StatementNode> sents = new LinkedList<>();
        listaSentencias(sents);
        match("}");
        ts.setCurrentBlock(newBlock.getParentBlock());
        newBlock.setSents(sents);
        return newBlock;
    }

    void listaSentencias(List<StatementNode> sents) throws LexicalException, SyntacticException, IOException {
        if(pSentencia.contains(currentToken.getId())){
            sents.add(sentencia());
            listaSentencias(sents);
        } else {

        }
    }

    StatementNode sentencia() throws LexicalException, SyntacticException, IOException {
        if(currentToken.getId().equals(";")){
            match(";");
            return new EmptyStatementNode();
        } else if (pVarLocal.contains(currentToken.getId())){
            StatementNode s = varLocal();
            match(";");
            return s;
        } else if(pReturn.contains(currentToken.getId())){
            StatementNode s = met_return();
            match(";");
            return s;
        } else if(pIf.contains(currentToken.getId())) {
            return met_if();
        } else if(pWhile.contains(currentToken.getId())){
            return met_while();
        } else if(pBloque.contains(currentToken.getId())){
            return bloque();
        } else if(pAsignacionOLlamada.contains(currentToken.getId())){
            StatementNode s = asignacionOLlamada();
            match(";");
            return s;
        } else {
            throw new SyntacticException(currentToken, "; o varLocal o return o if o while o bloque o asignacion o llamda");
        }
    }

    StatementNode asignacionOLlamada() throws LexicalException, SyntacticException, IOException {
        ExpressionNode exp = expresion();
        return new AssignmentOrCallNode(exp);
    }

    StatementNode varLocal() throws LexicalException, SyntacticException, IOException {
        match("var");
        Token id = currentToken;
        match("idMetVar");
        match("=");
        CompoundExpressionNode rightNode = expresionCompuesta();
        LocalVarNode localVar = new LocalVarNode(id, rightNode, ts.getCurrentMethod(),ts.getCurrentBlock());
        return localVar;
    }

    StatementNode met_return() throws LexicalException, SyntacticException, IOException {
        Token tk = currentToken;
        match("return");
        ExpressionNode exp = expresionOpcional();
        return new ReturnNode(tk, exp, ts.getCurrentMethod());
    }

    ExpressionNode expresionOpcional() throws LexicalException, SyntacticException, IOException {
        if(pExpresion.contains(currentToken.getId())){
            return expresion();
        } else {
            return null;
        }
    }

    StatementNode met_if() throws LexicalException, SyntacticException, IOException {
        Token tkIf = currentToken;
        match("if");
        match("(");
        ExpressionNode cond = expresion();
        match(")");
        StatementNode thenSent = sentencia();
        StatementNode elseOpt = r_if();
        return new IfNode(tkIf, cond, thenSent, elseOpt);
    }

    StatementNode r_if() throws LexicalException, SyntacticException, IOException {
        if(currentToken.getId().equals("else")){
            match("else");
            return sentencia();
        } else {
            return null;
        }
    }

    StatementNode met_while() throws LexicalException, SyntacticException, IOException {
        Token whileTk = currentToken;
        match("while");
        match("(");
        ExpressionNode cond = expresion();
        match(")");
        StatementNode body = sentencia();
        return new WhileNode(whileTk, cond, body);
    }

    ExpressionNode expresion() throws LexicalException, SyntacticException, IOException {
        CompoundExpressionNode base = expresionCompuesta();
        return r_expresion(base);
    }

    ExpressionNode r_expresion(CompoundExpressionNode leftNode) throws LexicalException, SyntacticException, IOException {
        if(pOperadorAsignacion.contains(currentToken.getId())){
            Token tk = currentToken;
            operadorAsignacion();
            CompoundExpressionNode rightNode = expresionCompuesta();
            return new AssignmentExpressionNode(leftNode, rightNode, tk);
        } else {
            return leftNode;
        }
    }

    void operadorAsignacion() throws LexicalException, SyntacticException, IOException {
        if (currentToken.getId().equals("=")) {
            match("=");
        } else {
            throw new SyntacticException(currentToken, "=");
        }
    }

    CompoundExpressionNode expresionCompuesta() throws LexicalException, SyntacticException, IOException {
        CompoundExpressionNode base = expresionBasica();
        return r_expresionCompuesta(base);
    }

    CompoundExpressionNode r_expresionCompuesta(CompoundExpressionNode leftNode) throws LexicalException, SyntacticException, IOException {
        if(pOperadorBinario.contains(currentToken.getId())){
            Token tk = currentToken;
            operadorBinario();
            CompoundExpressionNode rightNode = expresionCompuesta();
            return new BinaryExpressionNode(leftNode, rightNode, tk);
        } else {
            return leftNode;
        }
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

    CompoundExpressionNode expresionBasica() throws LexicalException, SyntacticException, IOException {
        if(pOperadorUnario.contains(currentToken.getId())){
            Token tk = currentToken;
            operadorUnario();
            OperandNode op = operando();
            return new UnaryExpressionNode(op, tk);
        } else if(pOperando.contains(currentToken.getId())){
            return operando();
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

    OperandNode operando() throws SyntacticException, LexicalException, IOException {
        if(pPrimitivo.contains(currentToken.getId())){
            return primitivo();
        } else if(pReferencia.contains(currentToken.getId())){
            return referencia();
        } else{
            throw new SyntacticException(currentToken, "operando");
        }
    }

    PrimitiveNode primitivo() throws LexicalException, SyntacticException, IOException {
        Token tk = currentToken;
        switch (currentToken.getId()){
            case "true" -> {
                match("true");
                return new BooleanLiteralNode(tk);
            }
            case "false" -> {
                match("false");
                return new BooleanLiteralNode(tk);
            }
            case "intLiteral" -> {
                match("intLiteral");
                return new IntLiteralNode(tk);
            }
            case "charLiteral" -> {
                match("charLiteral");
                return new CharLiteralNode(tk);
            }
            case "null" -> {
                Token nullTk = currentToken;
                match("null");
                return new NullLiteralNode(nullTk);
            }
            default -> throw new SyntacticException(currentToken, "true o false o intLiteral o charLiteral o null");
        }
    }

    OperandNode referencia() throws LexicalException, SyntacticException, IOException {
            PrimaryNode base = primario();
            ChainedNode chain = r_referencia();
            base.setChain(chain);
            return base;
    }

    ChainedNode r_referencia() throws LexicalException, SyntacticException, IOException {
        if(pVarOMetodoEncadenado.contains(currentToken.getId())){
            ChainedNode ch = varOMetodoEncadenado();
            ch.setChain(r_referencia());
            return ch;
        } else{
            return null;
        }
    }

    ChainedNode varOMetodoEncadenado() throws SyntacticException, LexicalException, IOException {
        match(".");
        Token id = currentToken;
        match("idMetVar");
        return r_varOMetodoEncadenado(id);
    }

    ChainedNode r_varOMetodoEncadenado(Token id) throws LexicalException, SyntacticException, IOException {
        if(pArgsActuales.contains(currentToken.getId())){
            List<ExpressionNode> args = argsActuales();
            return new MethodCallChainedNode(args, id);
        } else {
            return new VarChainedNode(id);
        }
    }

    PrimaryNode primario() throws LexicalException, SyntacticException, IOException {
        if(currentToken.getId().equals("this")){
            Token tk = currentToken;
            match("this");
            return new ThisNode(tk, ts.getCurrentClass(), ts.getCurrentMethod());
        } else if(currentToken.getId().equals("stringLiteral")){
            Token tk = currentToken;
            match("stringLiteral");
            return new StringNode(tk);
        } else if(pLlamadaConstructor.contains(currentToken.getId())){
            return llamadaConstructor();
        } else if(pLlamadaMetodoEstatico.contains(currentToken.getId())){
            return llamadaMetodoEstatico();
        } else if(pExpresionParentizada.contains(currentToken.getId())){
            return expresionParentizada();
        } else if(pAccesoVarOLlamadaAMetodo.contains(currentToken.getId())){
            return accesoVarOLlamadadaAMetodo();
        } else {
            throw new SyntacticException(currentToken, "this o stringLiteral o new o idClase o ( o idMetVar");
        }
    }

    PrimaryNode accesoVarOLlamadadaAMetodo() throws LexicalException, SyntacticException, IOException {
        Token tk = currentToken;
        match("idMetVar");
        return r_accesoVarOLlamadaAMetodo(tk);
    }

    PrimaryNode r_accesoVarOLlamadaAMetodo(Token tk) throws LexicalException, SyntacticException, IOException {
        if(pArgsActuales.contains(currentToken.getId())){
            List<ExpressionNode> params = argsActuales();
            return new MethodCallNode(tk, params, ts.getCurrentClass());
        } else {
            return new VarAccessNode(tk, ts.getCurrentClass(), ts.getCurrentMethod(), ts.getCurrentBlock());
        }
    }

    ConstructorCallNode llamadaConstructor() throws LexicalException, SyntacticException, IOException {
        match("new");
        Token tk = currentToken;
        match("idClase");
        List<ExpressionNode> params = argsActuales();
        return new ConstructorCallNode(tk, params);
    }

    ParenthesizedExpressionNode expresionParentizada() throws LexicalException, SyntacticException, IOException {
            match("(");
            ExpressionNode expression = expresion();
            match(")");
            return new ParenthesizedExpressionNode(expression);
    }
    
    StaticMethodCallNode llamadaMetodoEstatico() throws LexicalException, SyntacticException, IOException {
        Token className = currentToken;
        match("idClase");
        match(".");
        Token methodName = currentToken;
        match("idMetVar");
        List<ExpressionNode> params = argsActuales();
        return new StaticMethodCallNode(className, methodName, params);
    }

    List<ExpressionNode> argsActuales() throws LexicalException, SyntacticException, IOException {
        match("(");
        List<ExpressionNode> listExps = new LinkedList<ExpressionNode>();
        listaExpsOpcional(listExps);
        match(")");
        return listExps;
    }

    void listaExpsOpcional(List<ExpressionNode> listExps) throws LexicalException, SyntacticException, IOException {
        if(pListaExps.contains(currentToken.getId())){
            listaExps(listExps);
        }else {

        }
    }

    void listaExps(List<ExpressionNode> listExps) throws LexicalException, SyntacticException, IOException {
        listExps.add(expresion());
        r_listaExps(listExps);
    }

    void r_listaExps(List<ExpressionNode> listExps) throws LexicalException, SyntacticException, IOException {
        if(currentToken.getId().equals(",")){
            match(",");
            listaExps(listExps);
        } else {

        }
    }
}