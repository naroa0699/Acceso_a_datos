package listadodirectorio;

import java.text.SimpleDateFormat;
import java.io.File;

public class ListadoDirectorio {
    public static void main(String[] args){
        //inicializa la variable ruta con un punto, que representa el directorio actual
        String ruta=".";
        if(args.length>=1)ruta=args[0];
        //Creación del objeto File, crea una instancia de la clase File
        File fich =new File(ruta);

        if(!fich.exists()){//Verificante de la existencia del fichero o directorio
            System.out.println("No existe el fichero o directorio"+ruta);
    }else{
            if (fich.isFile()){
                System.out.println("Es un fichero"+ruta);
            }else{
            System.out.println(ruta+"es un directorio. Contenidos: ");
            //Creamos un array con los objetos File
            File[] ficheros=fich.listFiles();//Ojo, ficheros o directorios
            //Recorremos el array y añadimos un prefijo para mostrar si el objeto es un fichero o directorio
            for(File f: ficheros){
                mostrarinfo(f);
            }
        }
    }
}
private static void mostrarinfo(File f){
        String textoDescr = f.isDirectory() ? "DIR" : "FILE";

        long tamaño = f.isFile() ? f.length() : 0;

        String permisos = (f.canRead() ? "r" : "-")
                +(f.canWrite() ? "w" : "-")
                +(f.canExecute() ? "x" : "-");
        String fecha = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").format(f.lastModified());
    System.out.println("(" + textoDescr + ") "+f.getName()+" | Tamaño: "+tamaño
    +" bytes | Permisos: " + permisos+ " | Últ.modif: "+fecha);
}
}
