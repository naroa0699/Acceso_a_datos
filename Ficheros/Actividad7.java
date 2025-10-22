// Almacenamiento de registros de longitud fija en fichero acceso aleatorio
/*
La clase desarrollada en el siguiente ejemplo permite almacenar registros con datos de clientes en un fichero de
acceso aleatorio. Los datos de cada cliente se almacenan en un registro, que es una estructura de longitud fija
dividida en campos de longitud fija. En este caso, los campos son DNI, nombre y código postal. El constructor
de la clase toma una lista con la definición del registro. Cada elemento de la lista contiene la definición
de un campo en un par <nombre, longitud>. Los valores de los campos para un registro se almacenan en un HashMap,
que contiene pares <nombre, valor>, cada uno de los cuales contiene el valor para un campo.
Al constructor se le proporciona un nombre de fichero. Si el fichero no existe, se crea.
Si el fichero existe, se calcula el número de registros que contiene, dividiendo la longitud del fichero
en bytes por la longitud de cada registro.
El método más interesante es insertar(). Tiene dos variantes. Si no se le indica la posición,
añade el registro al final del fichero. Si no, en la posición que se le indique. La posición
del primer registro es 0, no 1. Los textos se almacenan siempre codificados en UTF-8.
Como es relativamente habitual en los métodos, no gestionan las excepciones que puede generar (throws IOException),
y dejan esto para el programa principal.
 */

/*
Para añadir la librería siguiente:
https://openjfx.io/openjfx-docs/#install-javafx
https://gluonhq.com/products/javafx/
La versión javafx-sdk-21.0.8 es suficiente
 */
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actividad7 {

    private File f;
    private List<Pair<String, Integer>> campos;
    private long longReg;
    private long numReg = 0;

    /**
     * Constructor para inicializar la clase y el archivo de acceso aleatorio.
     * @param nomFich El nombre del archivo a utilizar.
     * @param campos  Una lista de pares (nombre del campo, longitud del campo)
     * @throws IOException Si ocurre un error al acceder al archivo.
     */
    Actividad7(String nomFich, List<Pair<String, Integer>> campos)
            throws IOException {
        this.campos = campos;
        this.f = new File(nomFich);
        longReg = 0;
        // Calcula la longitud total de un registro sumando las longitudes de todos los campos.
        for (Pair<String, Integer> campo: campos) {
            this.longReg += campo.getValue();
        }
        // Si el archivo ya existe, calcula el número de registros que contiene.
        if (f.exists()) {
            this.numReg=f.length()/this.longReg;
        }
    }

    /**
     * Devuelve el número de registros en el archivo.
     * @return El número de registros.
     */
    public long getNumReg() {
        return numReg;
    }

    /**
     * Inserta un nuevo registro al final del archivo.
     * @param reg Un mapa con los datos del registro (nombre del campo, valor).
     * @throws IOException Si ocurre un error de E/S.
     */
    public void insertar(Map<String, String> reg) throws IOException {
        insertar(reg, this.numReg++);
    }

    /**
     * Inserta un registro en una posición específica del archivo.
     * @param reg Un mapa con los datos del registro (nombre del campo, valor).
     * @param pos La posición (índice) donde se debe insertar el registro.
     * @throws IOException Si ocurre un error de E/S.
     */
    public void insertar(Map<String, String> reg, long pos) throws IOException {

        // Utiliza un bloque try-with-resources para asegurar el cierre automático del RandomAccessFile.
        try (RandomAccessFile faa = new RandomAccessFile(f, "rws")) {

            // Posiciona el puntero de escritura en el lugar correcto.
            // La posición se calcula multiplicando la posición del registro por la longitud de cada registro.
            faa.seek(pos * this.longReg);

            // Itera sobre los campos definidos para el registro.
            for (Pair<String, Integer> campo: this.campos) {
                String nomCampo=campo.getKey();
                Integer longCampo = campo.getValue();
                String valorCampo = reg.get(nomCampo);

                // Si el valor del campo no se encuentra en el mapa, se usa una cadena vacía.
                if (valorCampo == null) {
                    valorCampo = "";
                }

                // Formatea el valor del campo para que tenga la longitud fija.
                // %1$-...s significa que se alinea a la izquierda y se rellena con espacios.
                String valorCampoForm = String.format("%1$-" + longCampo + "s",
                        valorCampo);

                // Escribe los bytes del valor formateado en el archivo.
                faa.write(valorCampoForm.getBytes("UTF-8"), 0, longCampo);
            }
        }
    }

    public String obtenerValorCampo(long pos, String nombreCampo) throws IOException {
        // Verifica que la posición sea válida
        if (pos < 0 || pos >= this.numReg) {
            throw new IOException("Posición de registro fuera de rango: " + pos);
        }

        // Busca el campo dentro de la lista
        long desplazamientoCampo = 0;
        Integer longitudCampo = null;

        for (Pair<String, Integer> campo : campos) {
            if (campo.getKey().equalsIgnoreCase(nombreCampo)) {
                longitudCampo = campo.getValue();
                break;
            }
            desplazamientoCampo += campo.getValue();
        }

        // Si el campo no existe, lanza una excepción
        if (longitudCampo == null) {
            throw new IOException("Campo no encontrado: " + nombreCampo);
        }

        // Calcula la posición absoluta en el archivo
        long posicionByte = pos * this.longReg + desplazamientoCampo;

        // Lee los bytes correspondientes al campo
        byte[] buffer = new byte[longitudCampo];
        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
            raf.seek(posicionByte);
            raf.readFully(buffer);
        }

        // Convierte los bytes a String usando UTF-8 y elimina espacios
        return new String(buffer, java.nio.charset.StandardCharsets.UTF_8).trim();
    }
    public static void main(String[] args) {
        // Define la estructura de los campos para los registros.
        List<Pair<String, Integer>> campos = new ArrayList<>();
        campos.add(new Pair<>("DNI", 9));
        campos.add(new Pair<>("NOMBRE", 32));
        campos.add(new Pair<>("CP", 5));

        try {
            // Instancia la clase para trabajar con el archivo
            Actividad7 faa = new Actividad7("fic_acceso_aleat.dat", campos);

            // Crea e inserta el primer registro (SAMPER)
            Map<String, String> reg = new HashMap<>();
            reg.put("DNI", "56789012B");
            reg.put("NOMBRE", "SAMPER");
            reg.put("CP", "29730");
            faa.insertar(reg);

            // Segundo registro (ROJAS)
            reg.clear();
            reg.put("DNI", "89012345E");
            reg.put("NOMBRE", "ROJAS");
            faa.insertar(reg);

            // Tercer registro (DORCE)
            reg.clear();
            reg.put("DNI", "23456789D");
            reg.put("NOMBRE", "DORCE");
            reg.put("CP", "13700");
            faa.insertar(reg);

            // Insertar en posición 1 (NADALES)
            reg.clear();
            reg.put("DNI", "78901234X");
            reg.put("NOMBRE", "NADALES");
            reg.put("CP", "44126");
            faa.insertar(reg, 1);

            // Pruebas de obtención de valores
            System.out.println("Nombre del registro 0: " + faa.obtenerValorCampo(0, "NOMBRE"));
            System.out.println("CP del registro 1: " + faa.obtenerValorCampo(1, "CP"));
            System.out.println("DNI del registro 2: " + faa.obtenerValorCampo(2, "DNI"));

        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
