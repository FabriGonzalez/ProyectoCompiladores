package model.ast;

import model.Token;
import model.Type;
import sourcemanager.OutputManager;

public class ParenthesizedExpressionNode extends PrimaryNode{
    ExpressionNode associatedExp;
    boolean isRigthNode;

    public ParenthesizedExpressionNode(ExpressionNode e){
        associatedExp = e;
        isRigthNode = false;
    }

    public void setIsRightNode(){
        isRigthNode = true;
    }

    public ExpressionNode getAssociatedExp(){
        return associatedExp;
    }

    @Override
    public Token getExpressionToken() {
        return associatedExp.getExpressionToken();
    }

    @Override
    public Type check() {
        Type expType = associatedExp.check();
        if(chain != null){
            return chain.check(expType);
        } else {
            return expType;
        }
    }

    @Override
    public void generate(boolean isLeftAssignment) {
        if(chain != null){
            associatedExp.generate(false);
            chain.generate(isLeftAssignment);
        } else {
            if(isRigthNode){
                if(associatedExp instanceof AssignmentExpressionNode a){
                    a.setIsRightNode();
                }
            }
            associatedExp.generate(isLeftAssignment);
        }
    }
}
