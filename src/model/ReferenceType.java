package model;

public class ReferenceType extends Type {
    public Classs associatedClass;

    public ReferenceType(Token tk){
        super(tk);
    }

    @Override
    public boolean isCompatible(Type t) {
        if(!(t instanceof ReferenceType)){
            return false;
        }

        if(this instanceof NullType){
            return true;
        }

        ReferenceType otherRef = (ReferenceType) t;

        Classs current = this.associatedClass;
        while (current != null) {
            if (current.getName().equals(otherRef.associatedClass.getName())) {
                return true;
            }
            current = current.getParentClass();
        }

        return false;

    }

    public void setAssociatedClass(Classs c){
        associatedClass = c;
    }
}
