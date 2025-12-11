///10&20&30&20&exitosamente

class TestPolimorfismo {
    static void main() {
        var a = new A();
        var b = new B();
        var refA = b;

        // Método de clase base
        debugPrint(a.getX()); // 10

        // Métodos de clase derivada
        debugPrint(b.getX()); // 20 (sobreescrito)
        debugPrint(b.getY()); // 30 (nuevo)

        // Llamada polimórfica [cite: 350-353]
        // refA es de tipo A, pero apunta a B
        // Debe llamar a getX() de B
        debugPrint(refA.getX()); // 20
    }
}

class A {
    int x;
    public A() {
        this.x = 10;
    }

    int getX() {
        return this.x;
    }
}

class B extends A {
    int y;
    public B() {
        // super()
        this.x = 20; // Sobreescribe atributo heredado
        this.y = 30;
    }

    int getX() { // Sobreescribe método
        return this.x;
    }

    int getY() {
        return this.y;
    }
}