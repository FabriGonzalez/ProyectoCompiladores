///100&exitosamente

class A{
    int x;

    public A(){
        x = 100;
    }
}


class Init{
    static void main() {
        var a = new A();
        debugPrint(a.x);
    }
}


