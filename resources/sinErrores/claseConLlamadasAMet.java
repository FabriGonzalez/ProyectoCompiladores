///[SinErrores]
class Calculadora {
    int resultado;

    public Calculadora() {
        resultado = 0;
    }

    int sumar(int a, int b) {
        return a + b;
    }

    void probar() {
        var temp = this.sumar(5, 10) + sumar(2, 3);
        resultado = temp;
    }
}
