///100&300&100&exitosamente

class TestObjetos {

    int a;
    int b;

    public TestObjetos(int paramA, int paramB) {
        this.a = paramA;
        this.b = paramB;
    }

    int getA() {
        return this.a;
    }

    static void main() {
        var obj = new TestObjetos(100, 200);
        debugPrint(obj.a); // Prueba LOADREF
        obj.b = 300; // Prueba STOREREF
        debugPrint(obj.b);
        debugPrint(obj.getA()); // Prueba 'this' en método
    }
}