package listadodirectorio;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class Actividad5 {
    static int TAM_FILA=32;
    static int MAX_BYTES=2048;

    InputStream is=null;
    PrintStream out=null;

    public Actividad5(InputStream is, PrintStream out) {
        this.is = is;
        this.out = out;
    }

    public void volcar() throws IOException {
        byte buffer[]=new byte[TAM_FILA];
        int bytesLeidos;
        int offset=0;

        do {
            bytesLeidos = is.read(buffer);
            if (bytesLeidos == -1) break; // fin de fichero

            out.format("[%5d] ", offset);
            for(int i=0; i<bytesLeidos; i++) {
                out.format("%02x ", buffer[i]);
            }
            offset += bytesLeidos;
            out.println();
        } while (bytesLeidos==TAM_FILA && offset<MAX_BYTES);
    }

    public static void main(String[] args) {
        if(args.length<1) {
            System.out.println("Uso: java Actividad5 <fichero>");
            return;
        }

        String nomFich=args[0];

        try (FileInputStream fis = new FileInputStream(nomFich);
             PrintStream ps = new PrintStream("volcado.txt")) { // salida a fichero


            Actividad5 vb = new Actividad5(fis, ps);
            vb.volcar();

            System.out.println("Volcado realizado en volcado.txt");

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: no existe fichero "+nomFich);
        } catch (IOException e) {
            System.err.println("ERROR de E/S: "+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

