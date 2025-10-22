package dom_parser;

import java.io.File;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

public class DOMParser {

    private static final String INDENT_NIVEL = "  ";

    public static void muestraNodo(Node nodo, int nivel, PrintStream ps) {
        if (nodo.getNodeType() == Node.TEXT_NODE) {
            String text = nodo.getNodeValue();
            if (text.trim().length() == 0) {
                return;
            }
        }

        for (int i = 0; i < nivel; i++) {
            ps.print(INDENT_NIVEL);
        }

        switch (nodo.getNodeType()) {
            case Node.DOCUMENT_NODE:
                Document doc = (Document) nodo;
                ps.println("Documento DOM, versión: " + doc.getXmlVersion()
                        + ", codificación: " + doc.getXmlEncoding());
                break;

            case Node.ELEMENT_NODE:
                ps.print("<" + nodo.getNodeName());
                NamedNodeMap listaAtr = nodo.getAttributes();
                for (int i = 0; i < listaAtr.getLength(); i++) {
                    Node atr = listaAtr.item(i);
                    ps.print(" @" + atr.getNodeName() + "[" + atr.getNodeValue() + "]");
                }
                ps.println(">");
                break;

            case Node.TEXT_NODE:
                ps.println(nodo.getNodeName() + "[" + nodo.getNodeValue() + "]");
                break;

            default:
                ps.println("(nodo de tipo: " + nodo.getNodeType() + ")");
        }

        NodeList nodosHijos = nodo.getChildNodes();
        for (int i = 0; i < nodosHijos.getLength(); i++) {
            muestraNodo(nodosHijos.item(i), nivel + 1, ps);
        }
    }

    public static void main(String[] args) {
        String nomFich;
        if (args.length < 1) {
            System.out.println("Indicar por favor nombre de fichero XML a parsear");
            return;
        } else {
            nomFich = args[0];
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document domDoc = db.parse(new File(nomFich));

            // Generar nombre de fichero con fecha y hora
            String fechaHora = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String nombreSalida = "parsing_dom_" + fechaHora + ".txt";

            // Crear PrintStream hacia el fichero
            PrintStream ps = new PrintStream(new File(nombreSalida));

            // Mostrar resultado en el fichero
            muestraNodo(domDoc, 0, ps);

            ps.close();
            System.out.println("Salida escrita en el fichero: " + nombreSalida);

        } catch (FileNotFoundException e) {
            System.err.println("Error: no se puede crear el fichero de salida.");
        } catch (ParserConfigurationException | SAXException e) {
            System.err.println("Error al parsear el fichero XML: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}