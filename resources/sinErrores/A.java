///10&10&exitosamente


class A{
    int a;
    public A(int x, int y){
        this.a = x + y;
    }

    static void getA(){
        debugPrint(10);
    }

    static void main(){
        var j = 10;
        var l = 10;
        debugPrint(l);
        var h = new A(j,l);
        h.getA();
    }
}