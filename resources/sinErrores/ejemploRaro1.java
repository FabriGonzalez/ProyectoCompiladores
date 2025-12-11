///
class A{
    int a;

    public A(int x, int y){

        this.a = x + y;

    }

    static void getA(){
        debugPrint(10);
    }



    static void main(){
        (new B()).m1();
    }

}

class B {
    C atributoC;

    void m1(){
        this.estaticoB();
    }
    static int estaticoB(){
        return 10;
    }
}

class C {
    static void estaticoC(){
        debugPrint(10);
    }
}
