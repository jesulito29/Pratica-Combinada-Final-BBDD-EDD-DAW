package GestionBBDD;
import java.io.File;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import ConexionBBDD.DBManager;
/**
 * Clase con la gestionaremos la base de datos a traves de un meni
 * @author Jesus Sanchez Torres
 */
public class Gestion {
	/**
	 * Metodo que contendra los argumentos para coenctarse a una base de datos y manerjala
	 * @param args
	 */
	public static void main(String[] args) {

        DBManager.loadDriver();
        DBManager.connect();
      
        boolean salir = false;
        do {
            salir = menuPrincipal();
        } while (!salir);

        DBManager.close();
    }
    /**
     * Menu principal para getsionar la base de datos
     * @return true si queremos cerrar la conexion, false si queremos seguir
     */
    public static boolean menuPrincipal() {
        System.out.println("---------------");
        System.out.println("MENU PRINCIPAL");
        System.out.println("1. Ver tablas ");
        System.out.println("2. Ver campos de una tabla");
        System.out.println("3. Mostrar contenido de una tabla ");
        System.out.println("4. Crear tabla");
        System.out.println("5. Modificar tabla");
        System.out.println("6. Borrar tabla");
        System.out.println("7. Volcar tabla en un fichero");
        System.out.println("8. Insertar datos desde un fichero");
        System.out.println("9. Modificar datos desde un fichero");
        System.out.println("10. Borrar datos desde un fichero");
        System.out.println("12. Cambiar de bases de datos");
        System.out.println("11. Salir");
        
        Scanner in = new Scanner(System.in);
            
        int opcion = pideInt("Elige una opción: ");
        
        switch (opcion) {
            case 1:
                DBManager.verTablas();
                return false;
            case 2:
                opcionVerCamposTabla();;
                return false;
            case 3:
            	opcionMostrarContenidoTabla();
                return false;
            case 4:
            	opcionCrearTabla();
                return false;
            case 5:
            	opcionModificarTabla();
                return false;
            case 6:
            	opcionBorrarTabla();
            	return false;
            case 7:
            	opcionVolcarTablaFichero();
            	return false;
            case 8:
            	opcionInsertarDatosDesdeFichero();
            	return false;
            case 9:
            	DBManager.modificarDatosDesdeFichero("./actualizacion.txt");
            	return false;
            case 10:
            	
            	return false;
            case 11:
            	return true;
            default:
                System.out.println("Opción elegida incorrecta");
                return false;
        }
        
    }
    /**
     * Pedimos un numero entero
     * @param mensaje: mensaje 
     * @return un numero segun el mensaje que le pasemos
     */
    public static int pideInt(String mensaje){
        
        while(true) {
            try {
                System.out.print(mensaje);
                Scanner in = new Scanner(System.in);
                int valor = in.nextInt();
                return valor;
            } catch (Exception e) {
                System.out.println("No has introducido un número entero. Vuelve a intentarlo.");
            }
        }
    }
    /**
     * Pedimos un texto
     * @param mensaje: mensaje
     * @return un texto segun el mensaje
     */
    public static String pideLinea(String mensaje){
        
        while(true) {
            try {
                System.out.print(mensaje);
                Scanner in = new Scanner(System.in);
                String linea = in.nextLine();
                return linea;
            } catch (Exception e) {
                System.out.println("No has introducido una cadena de texto. Vuelve a intentarlo.");
            }
        }
    }
    /**
     * Veremos los campos de la tabla
     */
    public static void opcionVerCamposTabla()
    {
       	 	Scanner ent = new Scanner(System.in);
        	String tabla;
        	String resultado;
        	
       	 	System.out.println("Que tabla deseas ver los campos: ");
       	 	tabla = ent.nextLine();
       	 	resultado = DBManager.verCamposTabla(tabla);
       	 	if (resultado == null)
       	 	{
       	 		System.err.println("No se ha podido mostar el contenido");
       	 	}

       	 		System.out.println(resultado);     	 	
    }
    /**
     * Veremos el contenido de la tabla
     */
    public static void opcionMostrarContenidoTabla()
    {
   	 	Scanner ent = new Scanner(System.in);
    	String tabla;
    	String resultado;
    	
   	 	System.out.println("Que tabla deseas ver el contenido: ");
   	 	tabla = ent.nextLine();
   	 	
   	 	resultado = DBManager.mostrarContenidoTabla(tabla);
   	 	if (resultado == null)
   	 	{
   	 		System.err.println("No se ha podido mostrar el contenido");
   	 	}
   	 	else
   	 	{
   	 		System.out.println(resultado);
   	 	}
    }
    /**
     * Creamos la tabla
     */
    public static void opcionCrearTabla()
    {
    	Scanner ent = new Scanner(System.in);
    	String tabla;
    	int numeroColumnas;
    	boolean resultado;
    	System.out.println("Introduce el nombre de la tabla a crear: ");
    	tabla = ent.nextLine();
    	try
    	{
    		System.out.println("Introduce el numero de columnas que va a tener la tabla: "); 
    	}
    	catch(InputMismatchException ex)
    	{
    		ex.printStackTrace();
    	}
    	numeroColumnas = ent.nextInt();
    	resultado = DBManager.crearTabla(tabla, numeroColumnas);
    	if (!resultado)
   	 	{
   	 		System.err.println("No se ha podido crear la tabla");
   	 	}
    	else
    	{
    		System.out.println("Tabla creada correctamente");
    	}
    }
    /**
     * Modificamos la tabla
     */
    public static void opcionModificarTabla()
    {
    	Scanner ent = new Scanner(System.in);
    	String tabla;
    	boolean resultado;
    	
   	 	System.out.println("Que tabla deseas modificar: ");
   	 	tabla = ent.nextLine();
   	 	resultado = DBManager.modificarTabla(tabla);
   	 	if (!resultado)
   	 	{
   	 		System.err.println("No se ha podido modificar la tabla");
   	 	}
   	 	else
   	 	{
   	 		System.out.println("Tabla modificada correctamente");
   	 	}
    }
    /**
     * Borraremos la tabla
     */
    public static void opcionBorrarTabla()
    {
    	Scanner ent = new Scanner(System.in);
    	String tabla;
    	boolean resultado;
    	
   	 	System.out.println("Que tabla deseas eliminar: ");
   	 	tabla = ent.nextLine();
   	 	resultado = DBManager.borradoTabla(tabla);
   	 	if (!resultado)
   	 	{
   	 		System.err.println("No se ha podido eliminar");
   	 	}
   	 	else
   	 	{
   	 		System.out.println("Se ha eliminado correctamente");
   	 	}
    }
    /**
     * Volcaremos una tabla en un fichero
     */
    public static void opcionVolcarTablaFichero()
    {
    	Scanner ent = new Scanner(System.in);
    	String tabla;
    	boolean resultado;
    	
   	 	System.out.println("Que tabla deseas volcar en un fichero: ");
   	 	tabla = ent.nextLine();
   	 	resultado = DBManager.volcarTablaFichero(tabla);
   	 	if (!resultado)
   	 	{
   	 		System.err.println("No se ha podido volcar en el fichero");
   	 	}
   	 	else
   	 	{
   	 		System.out.println("Se ha volcado correctamente");
   	 	}
    }
 
    public static void opcionInsertarDatosDesdeFichero()
    {
    	Scanner ent = new Scanner(System.in);
    	String ruta;
    	boolean resultado;
    	
    	System.out.println("Que fichero deseas importar los datos: ");
    	ruta = ent.nextLine();
    	File rutaArchivo = new File(ruta);
    	
    	resultado = DBManager.insertarDatosDesdeFichero(ruta);
    	if (!resultado)
    	{
   	 		System.err.println("No se ha podido insertar los datos");
    	}
    	else
    	{
    		System.out.println("Se ha insertado los datos correctamente");
    	}
    }
}