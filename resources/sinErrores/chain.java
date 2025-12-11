///46&exitosamente

class A {
    int valor;


    public A(int valorInicial) {
        this.valor = valorInicial;
    }

    // m2: suma 5
    A m2() {
        this.valor = valor + 5;
        return this;
    }

    // m3: imprime valor
    A m3() {
        debugPrint(valor);
        return this;
    }

    // Extra: sumar un valor
    A sumar(int x) {
        this.valor = x + valor;
        return this;
    }

    // Extra: multiplicar un valor
    A multiplicar(int x) {
        this.valor = x * valor;
        return this;
    }

    // m1: factory estática sin public
    static A m1() {
        return new A(3);
    }
}


class Main {
     static void main() {
        A.m1()
                .m2()
                .m2()
                .sumar(10)
                .multiplicar(2)
                .m3();
    }
}
