

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author lionel
 */
public class GestionClientes {

    public static void main(String[] args) {

        DBManager.loadDriver();
        DBManager.connect();
      
        boolean salir = false;
        do {
            salir = menuPrincipal();
        } while (!salir);

        DBManager.close();
    }

    public static boolean menuPrincipal() {
        System.out.println("---------------");
        System.out.println("MENU PRINCIPAL");
        System.out.println("1. Ver tablas ");
        System.out.println("2. Ver campos de una tabla");
        System.out.println("3. Mostrar contenido de una tabla ");
        System.out.println("4. Crear tabla");
        System.out.println("5. Modificar tabla");
        System.out.println("6. Borrar tabla");
        System.out.println("7. Salir");
        
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
            	return true;
            default:
                System.out.println("Opción elegida incorrecta");
                return false;
        }
        
    }
    
    public static int pideInt(String mensaje){
        
        while(true) {
            try {
                System.out.print(mensaje);
                Scanner in = new Scanner(System.in);
                int valor = in.nextInt();
                //in.nextLine();
                return valor;
            } catch (Exception e) {
                System.out.println("No has introducido un número entero. Vuelve a intentarlo.");
            }
        }
    }
    
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
    public static void opcionVerCamposTabla()
    {
    	{
       	 	Scanner ent = new Scanner(System.in);
        	String tabla;
        	boolean resultado;
        	
       	 	System.out.println("Que tabla deseas ver los campos: ");
       	 	tabla = ent.nextLine();
       	 	resultado = DBManager.verCamposTabla(tabla);;
       	 	if (!resultado)
       	 	{
       	 		System.err.println("No se ha podido mostar el contenido");
       	 	}
        }
    }
    
    public static void opcionMostrarContenidoTabla()
    {
   	 	Scanner ent = new Scanner(System.in);
    	String tabla;
    	boolean resultado;
    	
   	 	System.out.println("Que tabla deseas ver el contenido: ");
   	 	tabla = ent.nextLine();
   	 	resultado = DBManager.verCamposTabla(tabla);
   	 	if (!resultado)
   	 	{
   	 		System.err.println("No se ha podido mostar el contenido");
   	 	}
   	 	
   	 	resultado = DBManager.mostrarContenidoTabla(tabla);
   	 	if (!resultado)
   	 	{
   	 		System.err.println("No se ha podido mostar el contenido");
   	 	}
    }

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
    public static void opcionVolcarInformacionFichero()
    {
    	
    }
}