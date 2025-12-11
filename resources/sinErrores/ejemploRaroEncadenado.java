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
        this.estaticoB().estaticoC();
    }
    static C estaticoB(){
        return new C();
    }
}

class C {
    static int estaticoC(){
        debugPrint(10);
        return 10;
    }
}
