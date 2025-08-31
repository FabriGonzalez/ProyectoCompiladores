import exceptions.LexicalException;
import sourcemanager.SourceManager;

import java.io.IOException;
import java.util.HashMap;

import static sourcemanager.SourceManager.END_OF_FILE;

public class LexicalAnalyzer {
    String lexeme;
    char currentChar;
    SourceManager sourceManager;

    public LexicalAnalyzer(SourceManager sourceManager) throws IOException {
        this.sourceManager = sourceManager;
        this.currentChar = sourceManager.getNextChar();
    }

    private static final HashMap<String, String> RESERVED_WORDS = new HashMap<>();
    static{
        RESERVED_WORDS.put("class", "RW_CLASS");
        RESERVED_WORDS.put("extends", "RW_EXTENDS");
        RESERVED_WORDS.put("public", "RW_PUBLIC");
        RESERVED_WORDS.put("static", "RW_STATIC");
        RESERVED_WORDS.put("void", "RW_VOID");
        RESERVED_WORDS.put("boolean", "RW_BOOLEAN");
        RESERVED_WORDS.put("char", "RW_CHAR");
        RESERVED_WORDS.put("int", "RW_INT");
        RESERVED_WORDS.put("abstract", "RW_ABSTRACT");
        RESERVED_WORDS.put("final", "RW_FINAL");
        RESERVED_WORDS.put("if", "RW_IF");
        RESERVED_WORDS.put("else", "RW_ELSE");
        RESERVED_WORDS.put("while", "RW_WHILE");
        RESERVED_WORDS.put("return", "RW_RETURN");
        RESERVED_WORDS.put("var", "RW_VAR");
        RESERVED_WORDS.put("this", "RW_THIS");
        RESERVED_WORDS.put("new", "RW_NEW");
        RESERVED_WORDS.put("null", "RW_NULL");
        RESERVED_WORDS.put("true", "RW_TRUE");
        RESERVED_WORDS.put("false", "RW_FALSE");
    }



    public Token nextToken() throws IOException, LexicalException {
        lexeme = "";
        return start();
    }

    private void updateLexeme() {
        lexeme = lexeme + currentChar;
    }

    private void updateCurrentChar() throws IOException {
        currentChar = sourceManager.getNextChar();
    }

    private Token start() throws IOException, LexicalException {
        if (Character.isWhitespace(currentChar)) {
            updateCurrentChar();
            return start();
        }
        if (Character.isUpperCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return idClass();
        } else if (Character.isLowerCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return idMetVar();
        } else if (Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return intLiteral(1);
        } else if (currentChar == '\'') {
            updateLexeme();
            updateCurrentChar();
            return charLiteral();
        } else if (currentChar == '\"') {
            updateLexeme();
            updateCurrentChar();
            return stringLiteral();
        } else if (currentChar == '/') {
            updateLexeme();
            updateCurrentChar();
            return commentOrDivider();
        } else if (currentChar == '(') {
            updateLexeme();
            updateCurrentChar();
            return openPar();
        } else if (currentChar == ')') {
            updateLexeme();
            updateCurrentChar();
            return closedPar();
        } else if (currentChar == '{') {
            updateLexeme();
            updateCurrentChar();
            return openCurly();
        } else if (currentChar == '}') {
            updateLexeme();
            updateCurrentChar();
            return closedCurly();
        } else if (currentChar == ';') {
            updateLexeme();
            updateCurrentChar();
            return semicolon();
        } else if (currentChar == ',') {
            updateLexeme();
            updateCurrentChar();
            return comma();
        } else if (currentChar == '.') {
            updateLexeme();
            updateCurrentChar();
            return dot();
        } else if (currentChar == ':') {
            updateLexeme();
            updateCurrentChar();
            return colon();
        } else if (currentChar == '>') {
            updateLexeme();
            updateCurrentChar();
            return greaterThan();
        } else if (currentChar == '<') {
            updateLexeme();
            updateCurrentChar();
            return lessThan();
        } else if (currentChar == '!') {
            updateLexeme();
            updateCurrentChar();
            return exclamation();
        } else if (currentChar == '=') {
            updateLexeme();
            updateCurrentChar();
            return equalsSign();
        }else if (currentChar == '&'){
            updateLexeme();
            updateCurrentChar();
            return ampersand();
        } else if(currentChar == '|'){
            updateLexeme();
            updateCurrentChar();
            return pipe();
        }else if(currentChar == '%'){
            updateLexeme();
            updateCurrentChar();
            return percent();
        }else if(currentChar == '+'){
            updateLexeme();
            updateCurrentChar();
            return add();
        }else if(currentChar == '-'){
            updateLexeme();
            updateCurrentChar();
            return minus();
        } else if(currentChar == '*'){
            updateLexeme();
            updateCurrentChar();
            return multiply();
        }
        else if (currentChar == END_OF_FILE) {
            return new Token("EOF", "", sourceManager.getLineNumber());
        } else {
            updateLexeme();
            int lineNum = sourceManager.getLineNumber();
            int columnNum = sourceManager.getColumnNumber();
            String currentLine = sourceManager.getCurrentLine();
            updateCurrentChar();
            throw new LexicalException(lexeme, lineNum, columnNum, currentLine);
        }
    }

    private Token idClass() throws IOException {
        if (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
            updateLexeme();
            updateCurrentChar();
            return idClass();
        } else {
            return new Token("idClase", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token idMetVar() throws IOException {
        if (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
            updateLexeme();
            updateCurrentChar();
            return idMetVar();
        } else {
            String tokenType = RESERVED_WORDS.getOrDefault(lexeme, "idMetVar");
            return new Token(tokenType, lexeme, sourceManager.getLineNumber());
        }
    }

    private Token intLiteral(int cont) throws IOException, LexicalException {
        if (Character.isDigit(currentChar)) {
            if(cont == 9){
                updateLexeme();
                throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), sourceManager.getCurrentLine());
            }
            updateLexeme();
            updateCurrentChar();
            cont++;
            return intLiteral(cont);
        }else{
            return new Token("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token charLiteral() throws IOException, LexicalException {
        if (currentChar == '\\') {
            updateLexeme();
            updateCurrentChar();
            return charLiteral1();
        } else if (currentChar == END_OF_FILE) {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), sourceManager.getCurrentLine());
        } else if (currentChar == '\n') {
            int lineNum = sourceManager.getLineNumber();
            int columnNum = sourceManager.getColumnNumber();
            String currentLine = sourceManager.getCurrentLine();
            throw new LexicalException(lexeme, lineNum, columnNum, currentLine);
        } else {
            updateLexeme();
            updateCurrentChar();
            return charLiteral2();
        }
    }

    private Token charLiteral1() throws IOException, LexicalException {
        if (currentChar == END_OF_FILE) {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), sourceManager.getCurrentLine());
        } else if (currentChar == '\n') {
            int lineNum = sourceManager.getLineNumber();
            int columnNum = sourceManager.getColumnNumber();
            String currentLine = sourceManager.getCurrentLine();
            throw new LexicalException(lexeme, lineNum, columnNum, currentLine);
        } else {
            updateLexeme();
            updateCurrentChar();
            return charLiteral2();
        }
    }

    private Token charLiteral2() throws IOException, LexicalException {
        if (currentChar == '\'') {
            updateLexeme();
            updateCurrentChar();
            return endCharLiteral();
        } else {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), sourceManager.getCurrentLine());
        }
    }

    private Token endCharLiteral() {
        return new Token("charLiteral", lexeme, sourceManager.getLineNumber());
    }

    private Token stringLiteral() throws IOException, LexicalException {
        if (currentChar == '\\') {
            updateLexeme();
            updateCurrentChar();
            return stringLiteral1();
        } else if (currentChar == '\"') {
            updateLexeme();
            updateCurrentChar();
            return endStringLiteral();
        } else if (currentChar == END_OF_FILE) {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), sourceManager.getCurrentLine());
        } else if (currentChar == '\n'){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), sourceManager.getCurrentLine());
        }else{
            updateLexeme();
            updateCurrentChar();
            return stringLiteral();
        }
    }

    private Token stringLiteral1() throws IOException, LexicalException {
        if (currentChar == END_OF_FILE) {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), sourceManager.getCurrentLine());
        }else if (currentChar == '\n'){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), sourceManager.getCurrentLine());
        }else {
            updateLexeme();
            updateCurrentChar();
            return stringLiteral();
        }
    }

    private Token endStringLiteral() {
        return new Token("stringLiteral", lexeme, sourceManager.getLineNumber());
    }

    private Token commentOrDivider() throws IOException, LexicalException {
        if (currentChar == '/') {
            updateCurrentChar();
            lexeme = "";
            return commentLine();
        } else if (currentChar == '*') {
            updateCurrentChar();
            lexeme = "";
            return commentMultiLine();
        } else {
            return new Token("/", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token commentLine() throws IOException, LexicalException {
        if (currentChar == '\n') {
            updateCurrentChar();
            return start();
        } else {
            updateCurrentChar();
            return commentLine();
        }
    }

    private Token commentMultiLine() throws IOException, LexicalException {
        if (currentChar == '*') {
            updateCurrentChar();
            return endComment();
        } else if (currentChar == END_OF_FILE) {
            throw new LexicalException("Error: EOF en medio de comentario multi-linea");
        } else {
            updateCurrentChar();
            return commentMultiLine();
        }
    }

    private Token endComment() throws IOException, LexicalException {
        if (currentChar == '/') {
            updateCurrentChar();
            return start();
        } else if (currentChar == END_OF_FILE) {
            throw new LexicalException("Error: EOF en medio de comentario multi-linea");
        } else {
            return commentMultiLine();
        }
    }

    private Token openPar() {   // (
        return new Token("(", lexeme, sourceManager.getLineNumber());
    }

    private Token closedPar() { // )
        return new Token(")", lexeme, sourceManager.getLineNumber());
    }

    private Token openCurly() { // {
        return new Token("{", lexeme, sourceManager.getLineNumber());
    }

    private Token closedCurly() { // }
        return new Token("}", lexeme, sourceManager.getLineNumber());
    }

    private Token semicolon() { // ;
        return new Token(";", lexeme, sourceManager.getLineNumber());
    }

    private Token comma() { // ,
        return new Token(",", lexeme, sourceManager.getLineNumber());
    }

    private Token dot() { // .
        return new Token(".", lexeme, sourceManager.getLineNumber());
    }

    private Token colon() { // :
        return new Token(":", lexeme, sourceManager.getLineNumber());
    }

    private Token greaterThan() throws IOException { // >
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return greaterOrEqualsThan();
        }
        return new Token(">", lexeme, sourceManager.getLineNumber());
    }

    private Token greaterOrEqualsThan(){
        return new Token(">=", lexeme, sourceManager.getLineNumber());
    }

    private Token lessThan() throws IOException { // <
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return lessOrEqualsThan();
        }
        return new Token("<", lexeme, sourceManager.getLineNumber());
    }

    private Token lessOrEqualsThan(){
        return new Token("<=", lexeme, sourceManager.getLineNumber());
    }

    private Token exclamation() throws IOException {
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return unequals();
        }
        return new Token("!", lexeme, sourceManager.getLineNumber());
    }

    private Token unequals(){
        return new Token("!=", lexeme, sourceManager.getLineNumber());
    }

    private Token equalsSign() throws IOException {
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return doubleEqualsSign();
        } else
            return new Token("=", lexeme, sourceManager.getLineNumber());
    }

    private Token doubleEqualsSign(){
        return new Token("==", lexeme, sourceManager.getLineNumber());
    }

    private Token ampersand() throws LexicalException, IOException {
        if(currentChar == '&'){
            updateLexeme();
            updateCurrentChar();
            return doubleAmpersand();
        } else {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), sourceManager.getCurrentLine());
        }
    }

    private Token doubleAmpersand(){
        return new Token("&&", lexeme, sourceManager.getLineNumber());
    }

    private Token pipe() throws LexicalException, IOException {
        if(currentChar == '|'){
            updateLexeme();
            updateCurrentChar();
            return doublePipe();
        } else {
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), sourceManager.getColumnNumber(), sourceManager.getCurrentLine());
        }
    }

    private Token doublePipe(){
        return new Token("||", lexeme, sourceManager.getLineNumber());
    }

    private Token percent(){
        return new Token("%", lexeme, sourceManager.getLineNumber());
    }

    private Token add() throws IOException {
        if(currentChar == '+'){
            updateLexeme();
            updateCurrentChar();
            return doubleAdd();
        } else {
            return new Token("+", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token doubleAdd(){
        return new Token("++", lexeme, sourceManager.getLineNumber());
    }

    private Token minus() throws IOException{
        if(currentChar == '-'){
            updateLexeme();
            updateCurrentChar();
            return doubleMinus();
        } else {
            return new Token("-", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token doubleMinus(){
        return new Token("--", lexeme, sourceManager.getLineNumber());
    }

    private Token multiply(){
        return new Token ("*", lexeme, sourceManager.getLineNumber());
    }
}

