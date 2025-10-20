///[SinErrores]

class Ejemplo {
    int contador;

    int procesar() {
        if (contador > 0) {
            contador = contador - 1;
        } else {
            contador = 0;
        }

        while (contador < 10) {
            contador = contador + 1;
        }

        return contador;
    }
}