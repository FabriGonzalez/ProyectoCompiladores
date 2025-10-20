///[SinErrores]
class Contador extends Producto {
    int contador;

    public Contador(int p, int c) {
        super(p, c); // llamada válida según tu gramática no existe, aquí lo simulo con this
        contador = 0;
    }

    void procesar(int limite) {
        if (contador < limite) {
            ++contador;
        } else {
            contador = 0;
        }

        while (contador < limite) {
            contador = 1;
        }
    }
}