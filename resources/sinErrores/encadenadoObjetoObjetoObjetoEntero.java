///1000&exitosamente

class A {
    int y;

    public A (int i){
        y = i;
    }

    int valorY(){
        return y;
    }
}

class B {
    int x;

    public B(int i){
        x = i;
    }

    int getX(){
        return x;
    }

    B nuevoB(int y){
        return new B(y);
    }

    A nuevaA(){
        return new A(1000);
    }

}

class Init {
    static void main() {
        var b = new B(10);
        var varLocal = b.nuevoB(20).nuevaA().valorY();
        debugPrint(varLocal);
    }
}


