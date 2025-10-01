package listadodirectorio;

import java.io.*;

public class Actividad4 {
    public static void main(String[] args) {
        //Fichero de entrada
        String ficheroEntrada = "entrada_utf8.txt";
        //Ficheros de salida
        String ficheroISO="salida_iso8859_1.txt";
        String ficheroUTF16 = "salida_utf16.txt";

        try(
                //Lector en UTF-8
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ficheroEntrada), "UTF-8")
                );

                //Escritor en ISO-8859-1
                BufferedWriter bwISO = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ficheroISO), "ISO-8859-1")
                );

                //Escritor en UTF-16
                BufferedWriter bwUTF16 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ficheroUTF16),  "UTF-16")
                )
        ){
            String linea;
            while ((linea= br.readLine())!=null){
                bwISO.write(linea);
                bwISO.newLine();
                bwUTF16.write(linea);
                bwUTF16.newLine();
            }
            System.out.println("Conversión completada. Archivos generados: ");
            System.out.println(" - "+ficheroISO);
            System.out.println(" - "+ficheroUTF16);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
