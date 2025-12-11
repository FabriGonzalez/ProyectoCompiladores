///20&exitosamente


class B {
    int x;

    public B(int i){
        x = i;
    }

    B nuevoB(int y){
        return new B(y);
    }

}

class Init {
    static void main() {
        var b = new B(10);
        var nuevoB = b.nuevoB(20);
        debugPrint(nuevoB.x);
    }
}


