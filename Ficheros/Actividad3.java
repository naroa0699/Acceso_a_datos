package listadodirectorio;

import java.io.*;
import java.util.Scanner;

public class BusquedaFicheroTexto {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        //El archivo ya esta definido
        String nombreFichero="f_texto.txt";

        //Preguntamos al cliente/usuario que texto quiere buscar
        System.out.println("Introduce el texto a buscar");
        String textoBuscado= sc.nextLine();

        try{
            BufferedReader br = new BufferedReader(new FileReader(nombreFichero));
            String linea;
            int numeroLinea=1;

            while ((linea=br.readLine())!=null){
                int columna = linea.indexOf(textoBuscado);
                while (columna != -1){
                    System.out.println("Texto encontrado en línea "+numeroLinea+ ", columna"+(columna+1));
                    columna = linea.indexOf(textoBuscado, columna+1);
                }
                numeroLinea++;
            }
            br.close();
        }catch (IOException e){
            System.out.println("Error al leer el fichero");
        }
        sc.close();



    }
}
