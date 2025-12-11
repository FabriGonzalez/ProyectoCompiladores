package model.ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.IntType;
import model.Token;
import model.Type;
import sourcemanager.OutputManager;

public class BinaryExpressionNode extends CompoundExpressionNode {
    CompoundExpressionNode leftNode;
    CompoundExpressionNode rightNode;
    Token operator;

    public BinaryExpressionNode(CompoundExpressionNode l, CompoundExpressionNode r, Token o) {
        leftNode = l;
        rightNode = r;
        operator = o;
    }

    public Type check() {
        Type leftType = leftNode.check();
        Type rightType = rightNode.check();
        Type boolCompare = new BooleanType(new Token("boolean", "true", 0));
        Type intCompare = new IntType(new Token("intLiteral", "0", 0));

        String op = operator.getLexeme();

        switch (op) {
            case "&&", "||" -> {
                if (!leftType.isCompatible(boolCompare) || !rightType.isCompatible(boolCompare)) {
                    throw new SemanticException(operator.getLineNumber(),
                            "Los operadores lógicos " + op + " solo se aplican a booleanos", op);
                }
                return boolCompare;
            }
            case "==", "!=" -> {
                if (!leftType.isCompatible(rightType)) {
                    throw new SemanticException(operator.getLineNumber(),
                            "Los operadores de igualdad " + op + " requieren tipos iguales", op);
                }
                return boolCompare;
            }
            case "<", ">", "<=", ">=" -> {
                if (!leftType.isCompatible(intCompare) || !rightType.isCompatible(intCompare)) {
                    throw new SemanticException(operator.getLineNumber(),
                            "Los operadores relacionales " + op + " solo funcionan con tipo int", op);
                }
                return boolCompare;
            }
            case "+", "-", "*", "/", "%" -> {
                if (!leftType.isCompatible(intCompare) || !rightType.isCompatible(intCompare)) {
                    throw new SemanticException(operator.getLineNumber(),
                            "Los operadores aritméticos " + op + " solo funcionan con tipo int", op);
                }
                return intCompare;
            }
        }

        throw new SemanticException(operator.getLineNumber(), "Operador desconocido", op);
    }

    @Override
    public void generate(boolean a) {
        leftNode.generate(false);
        rightNode.generate(false);
        String op = operator.getLexeme();
        switch (op){
            case "+" -> {
                OutputManager.gen("ADD");
            }
            case "-" -> {
                OutputManager.gen("SUB");
            }
            case "*" -> {
                OutputManager.gen("MUL");
            }
            case "/" -> {
                OutputManager.gen("DIV");
            }
            case "%" -> {
                OutputManager.gen("MOD");
            }
            case "!=" -> {
                OutputManager.gen("NE");
            }
            case "&&" -> {
                OutputManager.gen("AND");
            }
            case "||" -> {
                OutputManager.gen("OR");
            }
            case "!" -> {
                OutputManager.gen("NOT");
            }
            case "==" -> {
                OutputManager.gen("EQ");
            }
            case "<" -> {
                OutputManager.gen("LT");
            }
            case ">" -> {
                OutputManager.gen("GT");
            }
            case "<=" -> {
                OutputManager.gen("LE");
            }
            case ">=" ->{
                OutputManager.gen("GE");
            }
        }
    }

    public Token getExpressionToken(){
        return operator;
    }
}

