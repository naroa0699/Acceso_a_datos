package listadodirectorio;
// Excepción gestionada durante la ejecución de un programa.
public class ExcepcionesDivPorCeroGest {
    public int divide(int a, int b) {
        return a / b;
    }
    public static void main(String[] args) {
        int a=5;
        int b = 0;
        try {
            b=2;
            System.out.println(a + " / " + b + " = " + a / b);

            b = 0;
            System.out.println(a + " / " + b + " = " + a / b);

            b = 3;
            System.out.println(a + " / " + b + " = " + a / b);

        } catch (ArithmeticException e) {
            System.err.println("Error al dividir: " + a + " / " + b);
        }

        }
    }

