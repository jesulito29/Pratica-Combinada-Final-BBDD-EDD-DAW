package GestionBBDD;
import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

import ConexionBBDD.DBManager;
/**
 * Clase con la gestionaremos la base de datos a traves de un menu
 * @author Jesus Sanchez Torres
 */
public class Gestion {
	/**
	 * Metodo que contendra los argumentos para coenctarse a una base de datos y manerjala
	 * @param args
	 */
	public static void main(String[] args) {

		DBManager.baseDatosDisponibles();
		DBManager.loadDriver();
        DBManager.connect();
      
        boolean salir = false;
        do {
            salir = menuPrincipal();
        } while (!salir);

        DBManager.close();
    }
    /**
     * Menu principal para gesionar la base de datos
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
        System.out.println("11. Cambiar de bases de datos");
        System.out.println("12. Salir");
        
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
            	opcionModificarDatosDesdeFichero();
            	return false;
            case 10:
            	opcionBorrarDatosDesdeFichero();
            	return false;
            case 11:
            	opcionCambiarBaseDatos();
            	return false;
            case 12:
            	return true;
            default:
                System.out.println("Opción elegida incorrecta");
                return false;
        }
        
    }
   
    /**
     * Veremos los campos de la tabla
     */
    public static void opcionVerCamposTabla()
    {
        String tabla;
        String resultado;
        
       	 tabla = pideLinea("Que tabla deseas ver los campos: ");
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
    	String tabla;
    	String resultado;
    	
   	 	tabla = pideLinea("Que tabla deseas ver el contenido: ");
   	 	
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
    	String tabla;
    	int numeroColumnas;
    	boolean resultado;

    	tabla = pideLinea("Introduce el nombre de la tabla a crear: ");
    	numeroColumnas = pideInt("Introduce el numero de columnas que va a tener la tabla: ");
    	
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
    	String tabla;
    	boolean resultado;
    	
   	 	tabla = pideLinea("Que tabla deseas modificar: ");
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
    	String tabla;
    	boolean resultado;
    	
   	 	System.out.println();
   	 	tabla = pideLinea("Que tabla deseas eliminar: ");
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
    	String tabla;
    	boolean resultado;
    	
   	 	tabla = pideLinea("Que tabla deseas volcar en un fichero: ");;
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
    /**
     * Insertaremos los datos de un fichero en la tabla
     */
    public static void opcionInsertarDatosDesdeFichero()
    {
    	String ruta;
    	boolean resultado;
    	
    	ruta = pideLinea("Que fichero deseas importar los datos: ");
    	File rutaArchivo = new File(ruta);
    	resultado = DBManager.insertarDatosDesdeFichero(rutaArchivo);
    	if (!resultado)
    	{
   	 		System.err.println("No se ha podido insertar los datos");
    	}
    	else
    	{
    		System.out.println("Se ha insertado los datos correctamente");
    	}
    }
    /**
     * Modificaremos los datos de un fichero en la tabla
     */
    public static void opcionModificarDatosDesdeFichero()
    {
    	Scanner ent = new Scanner(System.in);
    	String ruta;
    	boolean resultado;
    	
    	ruta = pideLinea("Que fichero deseas importar los datos: ");
    	File rutaArchivo = new File(ruta);
    	
    	resultado = DBManager.modificarDatosDesdeFichero(rutaArchivo);
    	if (!resultado)
    	{
   	 		System.err.println("No se ha podido modificar los datos");
    	}
    	else
    	{
    		System.out.println("Se ha modificado los datos correctamente");
    	}
    }
    /**
     * Borraremos los datos de un fichero en la tabla
     */
    public static void opcionBorrarDatosDesdeFichero()
    {
    	String ruta;
    	boolean resultado;
    	
    	ruta = pideLinea("Que fichero deseas importar los datos: ");
    	File rutaArchivo = new File(ruta);
    	
    	resultado = DBManager.borrarDatosDesdeFichero(rutaArchivo);
    	if (!resultado)
    	{
   	 		System.err.println("No se ha podido borrar los datos");
    	}
    	else
    	{
    		System.out.println("Se ha borrado los datos correctamente");
    	}
    }
    /**
     * Cambiaremos de base de datos
     */
    public static void opcionCambiarBaseDatos()
    {
    	String baseDatos;
    	boolean resultado;
    	
    	DBManager.baseDatosDisponibles();
    	
    	baseDatos = pideLinea("A que base de datos quieres cambiarse? ");
    	resultado = DBManager.cambiarBaseDatos(baseDatos);
    	if (!resultado)
    	{
    		System.err.println("No se ha podido cambiar de base de datos");
    	}
    	else
    	{
    		System.out.println("Se ha cambiado de base de datos");
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
}