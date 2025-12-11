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
        (new B().atributoC).estaticoC();
    }
}

class B {
    C atributoC;

}

class C {
    static void estaticoC(){
        debugPrint(10);
    }
}
