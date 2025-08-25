import exceptions.LexicalException;
import sourcemanager.SourceManager;

import java.io.IOException;

import static sourcemanager.SourceManager.END_OF_FILE;

public class AnalizadorLexico {
    String lexeme;
    char currentChar;
    SourceManager sourceManager;

    public Token nextToken() throws IOException, LexicalException {
        lexeme = "";
        updateCurrentChar();
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
            return openBrace();
        } else if (currentChar == '}') {
            updateLexeme();
            updateCurrentChar();
            return closedBrace();
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
            return multiplicate();
        }
        else if (currentChar == END_OF_FILE) {
            return new Token("EOF", "", sourceManager.getLineNumber());
        } else {
            updateLexeme();
            /*
            !!!!PREGUNTAR CUAL ES LA MEJOR MANERA...
            String errorMsg = "Error léxico en línea "
                    + sourceManager.getLineNumber()
                    + ": " + lexeme
                    + " no es un símbolo válido \n\n"
                    + "[Error:" + lexeme + "|" + sourceManager.getLineNumber() + "]";

             */
            throw new LexicalException(lexeme, sourceManager.getLineNumber());
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
            return new Token("idMetVar", lexeme, sourceManager.getLineNumber());
        }
    }

    //Preguntar si la idea del contador esta bien y si la lexicalexception esta bien.
    private Token intLiteral(int cont) throws IOException, LexicalException {
        if (cont < 9 && Character.isDigit(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            cont++;
            return intLiteral(cont);
        } else if (cont >= 9) {
            throw new LexicalException("El numero sobrepasa el limite de 9 digitos");
        } else {
            return new Token("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token charLiteral() throws IOException, LexicalException {
        if (currentChar == '/') {
            updateLexeme();
            updateCurrentChar();
            return charLiteral1();
        } else if (currentChar == END_OF_FILE) {
            throw new LexicalException("Error: EOF en medio del caracter");
        } else {
            updateLexeme();
            updateCurrentChar();
            return charLiteral2();
        }
    }

    private Token charLiteral1() throws IOException, LexicalException {
        updateLexeme();
        updateCurrentChar();
        return charLiteral2();
    }

    private Token charLiteral2() throws IOException, LexicalException {
        if (currentChar == '\'') {
            updateLexeme();
            updateCurrentChar();
            return endCharLiteral();
        } else {
            updateLexeme();
            throw new LexicalException(lexeme, sourceManager.getLineNumber());
        }
    }

    private Token endCharLiteral() throws IOException {
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
            throw new LexicalException("Error: EOF en medio del String");
        } else {
            updateLexeme();
            updateCurrentChar();
            return stringLiteral();
        }
    }

    /* Aca luego del slash \ deberia aceptar cualquier cosa y luego se ve en otra parte del analisis del codigo? */
    private Token stringLiteral1() throws IOException, LexicalException {
        if (currentChar == END_OF_FILE) {
            throw new LexicalException("Error: EOF en medio del String");
        }
        updateLexeme();
        updateCurrentChar();
        return stringLiteral();
    }

    private Token endStringLiteral() throws IOException {
        return new Token("stringLiteral", lexeme, sourceManager.getLineNumber());
    }

    private Token commentOrDivider() throws IOException, LexicalException {
        if (currentChar == '/') {
            updateLexeme();
            updateCurrentChar();
            return commentLine();
        } else if (currentChar == '*') {
            updateLexeme();
            updateCurrentChar();
            return commentMultiLine();
        } else {
            return new Token("/", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token commentLine() throws IOException, LexicalException {
        if (currentChar == '\n') {
            return start();
        } else {
            updateCurrentChar();
            return commentLine();
        }
    }

    private Token commentMultiLine() throws IOException, LexicalException {
        if (currentChar == '*') {
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

    private Token openBrace() { // {
        return new Token("{", lexeme, sourceManager.getLineNumber());
    }

    private Token closedBrace() { // }
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

    private Token unequals() throws IOException{
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
            throw new LexicalException("Error léxico en línea " + sourceManager.getLineNumber() +
                    ": '&' no es válido, use '&&'");
        }
    }

    private Token doubleAmpersand(){
        return new Token("&&", lexeme, sourceManager.getLineNumber());
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

    private Token multiplicate(){
        return new Token ("*", lexeme, sourceManager.getLineNumber());
    }
}

