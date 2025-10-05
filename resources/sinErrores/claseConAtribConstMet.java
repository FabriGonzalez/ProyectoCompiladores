
class Persona {
    int edad;
    char genero;

    public Persona(int e, char g) {
        edad = e;
        genero = g;
    }

    void actualizar(int e, char g, boolean validar) {
        if (validar) {
            edad = e;
            genero = g;
        } else {
            edad = 10;
            genero = 'f';
        }
    }
}
