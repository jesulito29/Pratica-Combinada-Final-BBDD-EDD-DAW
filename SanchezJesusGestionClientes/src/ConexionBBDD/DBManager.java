package ConexionBBDD;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.JTable;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * Clase que contiene los metodos para manejer cualquier base de datos
 * @author Jesus Sanchez Torres
 * @version 1.0 
 */
public class DBManager {

    // Conexión a la base de datos
    private static Connection conn = null;
    static ArrayList<String> contadorColumnas = new ArrayList<String>();
    
    // Configuración de la conexión a la base de datos
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static String DB_NAME = "tienda";
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static final String DB_MSQ_CONN_OK = "CONEXIÓN CORRECTA";
    private static final String DB_MSQ_CONN_NO = "ERROR EN LA CONEXIÓN";

    // Configuración de la tabla Clientes
    private static final String DB_CLI = "clientes";
    private static final String DB_CLI_SELECT = "SELECT * FROM " + DB_CLI;
    private static final String DB_CLI_ID = "id";
    private static final String DB_CLI_NOM = "nombre";
    private static final String DB_CLI_DIR = "direccion";

    //////////////////////////////////////////////////
    // MÉTODOS DE CONEXIÓN A LA BASE DE DATOS
    //////////////////////////////////////////////////
    ;
    
    /**
     * Intenta cargar el JDBC driver.
     * @return true si pudo cargar el driver, false en caso contrario
     */
    public static boolean loadDriver() {
        try {
            System.out.print("Cargando Driver...");
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("OK!");
            return true;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Preguntara a la base de datos que queremos conectarse
     * @return el nombre de la base de datos, null en caso contrario
     */
    public static String nombreBaseDatos()
    {
             try {
            	 Scanner ent = new Scanner(System.in);
            	 System.out.println("A que base de datos quiere conectarse: ");
            	 DB_NAME = ent.nextLine();
                 return DB_NAME;
             } catch (Exception e) { 
            	 System.out.println("No has introducido una cadena de texto. Vuelve a intentarlo.");
            	 return null;
             }
    }

    /**
     * Intenta conectar con la base de datos.
     *
     * @return true si pudo conectarse, false en caso contrario
     */
    public static boolean connect() {
    	try {
    		
    		System.out.print("Conectando a la base de datos...");
    		conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    		System.out.println("OK!");
    		return true;
    	} catch (SQLException ex) {
    		ex.printStackTrace();
    		return false;
    	}
    }
    /**
     * Muestra las tablas de la base de datos a la que estamos conectados
     */
    public static void verTablas()
    {
    	  try {
    		  DatabaseMetaData datos = conn.getMetaData();
    		  System.out.println("Lista de tablas");
    		  ResultSet rs = datos.getTables(DB_NAME, null, null, null);
    		  
    		  while(rs.next())
    		  {
    			  System.out.println(rs.getString("TABLE_NAME"));
    		  }
          } catch (SQLException ex) {
              ex.printStackTrace();
          }		
    }
    
    /**
     * Muestra los campos de una tabla de la base de datos
     * @param tabla: tabla de la base de datos
     * @return un texto con los campos de la tabla, null en caso contrario
     */
    public static String verCamposTabla(String tabla)
    {
   	 	contadorColumnas.clear();
    	DatabaseMetaData datos;
		ResultSet rs;
		String nombreColumna;
		String resultado = "";
		try {
			datos = conn.getMetaData();
			rs = datos.getColumns(DB_NAME, null, tabla, null);
			while(rs.next())
			{
				nombreColumna = rs.getString("COLUMN_NAME");
				contadorColumnas.add(nombreColumna);
				resultado += nombreColumna + "\t";
			}
			resultado += "\n";
			return resultado;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
    }
    /**
     * Comprueba la conexión y muestra su estado por pantalla
     *
     * @return true si la conexión existe y es válida, false en caso contrario
     */
    public static boolean isConnected() {
        // Comprobamos estado de la conexión
        try {
            if (conn != null && conn.isValid(0)) {
                System.out.println(DB_MSQ_CONN_OK);
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println(DB_MSQ_CONN_NO);
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cierra la conexión con la base de datos
     */
    public static void close() {
        try {
            System.out.print("Cerrando la conexión...");
            conn.close();
            System.out.println("OK!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Muestra el contenido de una tabla de la base de datos
     * @param tabla: tabla de la base de datos
     * @return un texto con el contenido de las tablas, null en caso contrario
     */
    public static String mostrarContenidoTabla(String tabla)
    {
   	 	PreparedStatement stmt;
   	 	ResultSet rs;
   	 	String resultado;
   	 	try {
   	 		resultado = verCamposTabla(tabla);
			stmt = conn.prepareStatement("select * from " + tabla);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				for (int i=0;i<contadorColumnas.size();i++)
				{
					String contadorAux = contadorColumnas.get(i);
					resultado += rs.getString(contadorAux) + "\t";
				}
				resultado += "\n";
			}
			return resultado;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
    }
    /**
     * Pedimos los datos de la tabla al crear una nueva tabla en la base de datos
     * @param nombreCampo: nombre del campo
     * @param tipoCampo: tipo de dato que tendra el campo
     * @return un texto con el nombre y el tipo de dato
     */
    public static String pedirDatosTabla(String nombreCampo, String tipoCampo)
    {
    	Scanner ent = new Scanner(System.in);
    	String resultado = null;
		System.out.println("Nombre del campo:");
		nombreCampo = ent.nextLine();
		System.out.println("Tipo Campo:");
		System.out.println("Opciones: String | char | int | double | boolean");
		tipoCampo = ent.nextLine();			
		switch (tipoCampo) {
			case "String":
			{
				resultado = nombreCampo + " varchar(50)";
				break;
			}
			case "char":
			{
				resultado = nombreCampo + " char";
				break;
			}
			case "int":
			{
				resultado = nombreCampo + " int";
				break;
			}
			case "double":
			{
				resultado = nombreCampo + " double";
				break;
			}
			case "boolean":
			{
				resultado = nombreCampo + " boolean";
				break;
			}
			default:
			{
				System.err.println("No existe el tipo de dato");
			}
		}
		return resultado;
    }
    /**
     * Crea una tabla en la base de datos 
     * @param tabla: nombre que tendra la tabla
     * @param numColumnas: numero columnas que tendra la tabla
     * @return true si ha creado la tabla, en caso contrario false
     */
    public static boolean crearTabla(String tabla, int numColumnas) {
    	Scanner ent = new Scanner(System.in);
    	String tipoCampo = null, nombreCampo = null, consulta,  informacion = "";
    	String crearTabla[] = new String[numColumnas];
    	int rs;
    	PreparedStatement stmt;
    	try {
    	 	for (int i=0;i<numColumnas;i++)
    		{
    	 		crearTabla[i] = pedirDatosTabla(nombreCampo, tipoCampo);    	 		
    		}
			
			for (int i=0;i<crearTabla.length;i++)
			{
				if (i == crearTabla.length-1)
				{
					informacion += crearTabla[i] + ");";	
				}
				else
				{
					informacion += crearTabla[i] + ",";
				}
			}
			consulta = "create table " + tabla + "(";
			stmt = conn.prepareStatement(consulta.concat(informacion));
			rs = stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
    }
    /**
     * Modificamos una tabla en la base de datos
     * @param tabla: tabla que queremos modificar
     * @return true si se ha podido modificar, en caso contrario false
     */
    public static boolean modificarTabla(String tabla)
    {
    	int opcion;
    	opcion = menuModificarTabla();

    	switch(opcion)
    	{
    		case 1:
    		{
    			anadirNuevoRegistro(tabla);
    			return true;
    		} 
    		case 2:
    		{
    			cambiarTipoDatoRegistro(tabla);
    			return true;
    		}
    		case 3:
    		{
    			renombarRegistro(tabla);
    			return true;
    		}
    		default:
    		{
    			System.err.println("No existe esta opcion");
    			return false;
    		}
    	}
    }
    /**
     * Añade un nuevo registro a una tabla
     * @param tabla: tabla que queremos añadir el registro
     */
    public static void anadirNuevoRegistro(String tabla)
    {
    	PreparedStatement stmt;
    	int rs;
    	String nombreCampo = null, tipoCampo = null, resultado;
    	resultado = pedirDatosTabla(nombreCampo, tipoCampo);
    	try {
			stmt = conn.prepareStatement("alter table " + tabla + " add " + resultado);
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    /**
     * Cambiamos el tipo de dato de un registro en la tabla
     * @param tabla: tabla de la que queremos cambiar el registro
     */
    public static void cambiarTipoDatoRegistro(String tabla)
    {
    	PreparedStatement stmt;
    	int rs;
    	String nombreCampo = null, tipoCampo = null, resultado;
    	resultado = pedirDatosTabla(nombreCampo, tipoCampo);
    	try {
			stmt = conn.prepareStatement("alter table " + tabla + " modify " + resultado);
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
   /**
    * Renombramos el registro de un campo de una tabla
    * @param tabla: tabla que queremos renombrar el registro
    */
   public static void renombarRegistro(String tabla)
   {
	   	PreparedStatement stmt;
	   	Scanner ent = new Scanner(System.in);
   		int rs;
   		String nuevonombreCampo , viejonombreCampo;
   		System.out.println("Nombre de campo a modificar: ");
   		viejonombreCampo = ent.nextLine();
   		System.out.println("Nuevo nombre: ");
   		nuevonombreCampo = ent.nextLine();
   		try {
			stmt = conn.prepareStatement("alter table " + tabla + " change " + viejonombreCampo + nuevonombreCampo);
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
   }
   /**
    * Menu de modificacion de la tabla
    * @return numero segun la opcion elegida, en caso contrario 0
    */
   public static int menuModificarTabla()
   {
	   Scanner ent = new Scanner(System.in);
	   int opcion;
	   try
	   {
		   System.out.println("Que deseas hacer: ");
		   System.out.println("1. Añadir nuevo registro");
		   System.out.println("2. Cambiar tipo de dato registro");
		   System.out.println("3. Renombrar columna");
		   return opcion = ent.nextInt();
	   }
	   catch(InputMismatchException ex)
	   {
		   ex.printStackTrace();
	   }
	   return 0;
    }
    /**
     * Borramos una tabla de la base de datos
     * @param tabla: tabla que queramos borrar
     * @return true si se ha podido borrar, en caso contrario false
     */
    public static boolean borradoTabla(String tabla)
    {
    	int opcion;
    	opcion = menuBorradoTabla();

    	switch(opcion)
    	{
    		case 1:
    		{
    			borrarTablaEntera(tabla);
    			return true;
    		} 
    		case 2:
    		{
    			borrarCampoTabla(tabla);
    			return true;
    		}
    		default:
    		{
    			System.err.println("No existe esta opcion");
    			return false;
    		}
    	}
    }
    /**
     * Menu de borrado de tabla
     * @return numero segun la opcion elegida, en caso contrario 0
     */
    public static int menuBorradoTabla()
    {
    	Scanner ent = new Scanner(System.in);
    	int opcion;
    	try
    	{
    		System.out.println("Que deseas hacer: ");
    		System.out.println("1. Eliminar tabla");
    		System.out.println("2. Eliminar campo de tabla");
    		return opcion = ent.nextInt();
        }
    	catch(InputMismatchException ex)
    	{
    		ex.printStackTrace();
    	}
		return 0;
    }
    /**
     * Borra una tabla entera de la base de datos
     * @param tabla: tabla que queramos borrar
     */
    public static void borrarTablaEntera(String tabla)
    {
    	PreparedStatement stmt;
   		int rs;
   		try {
			stmt = conn.prepareStatement("drop table " + tabla);
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    /**
     * Borra un campo de la tabla de la base de datos
     * @param tabla: tabla que queramos borrar
     */
    public static void borrarCampoTabla(String tabla)
    {
    	PreparedStatement stmt;
	   	Scanner ent = new Scanner(System.in);
	   	String campo;
	   	System.out.println("Introduce el nombre de la columna a eliminar: ");
	   	campo = ent.nextLine();
   		int rs;
   		try {
			stmt = conn.prepareStatement("alter table " + tabla + " drop " + campo);
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
   /**
    * Volcamos en un fichero una tabla de la base de datos
    * @param tabla: tabla que queramos volcar en un fichero
    * @return true si se ha podido borrar, en caso contrario false
    */
    public static boolean volcarTablaFichero(String tabla) {
    	Scanner ent = new Scanner(System.in);
    	String ruta = null , sql;
    	
    	try {
    		System.out.println("Escribe la ruta donde quieres volcar la informacion");
    		ruta = ent.nextLine();
    		
    		File archivo = new File(ruta);
    		FileWriter archivoEscritura = new FileWriter(archivo);
			archivoEscritura.write(DB_NAME + " " + tabla + "\n");
			archivoEscritura.write(mostrarContenidoTabla(tabla));
			archivoEscritura.close();
			return true;
    	} catch (FileNotFoundException ex) {
    		ex.printStackTrace();
    		return false;
		} catch (IOException ex) {
			ex.printStackTrace();
    		return false;
		}
    }
    
    public static String[] leerDatosFichero(String ruta)
    {
    	String informacion;
    	Scanner lectorFichero = null;
    	Scanner lectorFichero1 = null;
    	File fichero = new File(ruta);
    	String nombreBaseDatos, nombreTabla, nombreCampos, datosCampos;
    	int numLineas = 0;
		try {
			lectorFichero = new Scanner(fichero);
			lectorFichero1 = new Scanner(fichero);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		do {
			informacion = lectorFichero1.nextLine();
			numLineas++;
		}
		while(lectorFichero1.hasNext());
		lectorFichero1.close();
		
		String array[] = new String[numLineas];
		numLineas = 0;
		do {
			informacion = lectorFichero.nextLine();

			array[numLineas] = informacion;
			numLineas++;
		}
		while(lectorFichero.hasNext());
		lectorFichero.close();
		return array;
    }
    
    public static boolean insertarDatosDesdeFichero(String ruta)
    {
    	String array[] = leerDatosFichero(ruta);
    	String informacion = "";
    	String nombreBaseDatos = array[0];
    	String nombreTabla = array[1];
    	String nombreCampos = array[2];
    	
    	for (int j=3;j<array.length;j++)
    	{
    		String datos[] = array[j].split(",");
    		for (int i=0;i<datos.length;i++)
    		{
    			if (i == datos.length-1)
    			{
    				informacion += "'" + datos[i] + "'"; 
    			}
    			else
    			{
    				informacion += "'" + datos[i] + "',"; 	
    			}
    		}
    			PreparedStatement miStatement;
				try {
					miStatement = conn.prepareStatement("insert into " + nombreBaseDatos + "." + nombreTabla + "(" + nombreCampos + ") values(" + informacion + ");" );
					int setencia = miStatement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
    			informacion = "";	
    	}
    	return true;
    }
    /*
    public static boolean modificarDatosDesdeFichero(String ruta)
    {
      	String array[] = leerDatosFichero(ruta);
    	String informacion = "";
    	String nombreBaseDatos = array[0];
    	String nombreTabla = array[1];
    	String nombreCampos = array[2];
    	
    	for (int j=3;j<array.length;j++)
    	{
    		String datos[] = array[j].split(",");
    		for (int i=0;i<datos.length;i++)
    		{
    			if (i == datos.length-1)
    			{
    				informacion += "'" + datos[i] + "'"; 
    			}
    			else
    			{
    				informacion += "'" + datos[i] + "',"; 	
    			}
    		}
    			PreparedStatement miStatement;
				try {
					miStatement = conn.prepareStatement("update set " + nombreBaseDatos + "." + nombreTabla + " set ");
					int setencia = miStatement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				System.out.println(informacion);
    			informacion = "";	
    	}
    	return true;
    }*/
    	    	
    	
    	
    public static void cambiarBaseDatos(String BaseDatos)
    {
    	PreparedStatement stmt;
    	int rs;
    	try {
			stmt = conn.prepareStatement("use " + BaseDatos);
			rs = stmt.executeUpdate();  	
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}