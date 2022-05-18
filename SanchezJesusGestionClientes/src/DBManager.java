
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
import java.util.Scanner;

import javax.swing.JTable;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 *
 * @author jesus
 */
public class DBManager {

    // Conexión a la base de datos
    private static Connection conn = null;
    static ArrayList<String> contadorColumnas = new ArrayList<String>();
    
    // Configuración de la conexión a la base de datos
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static String DB_NAME = nombreBaseDatos();
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
    
    public static boolean verCamposTabla(String tabla)
    {
   	 	contadorColumnas.clear();
    	DatabaseMetaData datos;
		ResultSet rs;
		String nombreColumna;
		try {
			datos = conn.getMetaData();
			rs = datos.getColumns(DB_NAME, null, tabla, null);
			while(rs.next())
			{
				nombreColumna = rs.getString("COLUMN_NAME");
				contadorColumnas.add(nombreColumna);
				System.out.printf(nombreColumna + "\t");
			}
			System.out.println();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
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

    
    public static boolean mostrarContenidoTabla(String tabla)
    {
   	 	PreparedStatement stmt;
   	 	ResultSet rs;
   	 	try {
			stmt = conn.prepareStatement("select * from " + tabla);
			rs = stmt.executeQuery();
			while (rs.next())
			{
				for (int i=0;i<contadorColumnas.size();i++)
				{
					String contadorAux = contadorColumnas.get(i);
					System.out.printf(rs.getString(contadorAux) + "\t");
				}
				System.out.println();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
    }
    
    public static void pedirDatosCreacionTabla(int numColumnas, String nombreCampo, String tipoCampo, String crearTabla[])
    {
    	Scanner ent = new Scanner(System.in);
    	for (int i=0;i<numColumnas;i++)
		{
			System.out.println("Nombre del campo:");
			nombreCampo = ent.nextLine();
			System.out.println("Tipo Campo:");
			System.out.println("Opciones: String | char | int | double | boolean");
			tipoCampo = ent.nextLine();
			switch (tipoCampo) {
				case "String":
				{
					crearTabla[i] = nombreCampo + " varchar(50)";
					break;
				}
				case "char":
				{
					crearTabla[i] = nombreCampo + " char";
					break;
				}
				case "int":
				{
					crearTabla[i] = nombreCampo + " int";
					break;
				}
				case "double":
				{
					crearTabla[i] = nombreCampo + " double";
					break;
				}
				case "boolean":
				{
					crearTabla[i] = nombreCampo + " boolean";
					break;
				}
				default:
				{
					System.err.println("No existe el tipo de dato");
				}
			}
		}
    }
    
    public static boolean crearTabla(String tabla, int numColumnas) {
    	Scanner ent = new Scanner(System.in);
    	String tipoCampo = null, nombreCampo = null, consulta,  informacion = "";
    	String crearTabla[] = new String[numColumnas];
    	int rs;
    	PreparedStatement stmt;
    	try {
			pedirDatosCreacionTabla(numColumnas, nombreCampo, tipoCampo, crearTabla);
			
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

   /*
    public static void volcarInformacionFichero() {
    	Scanner ent = new Scanner(System.in);
    	String ruta = null , tabla, sql;
    	
    	System.out.println("Que tabla quieres volcar");
    	tabla = ent.nextLine();
    	System.out.println("Escribe la ruta donde quieres volcar la informacion");
    	ruta = ent.nextLine();
    	try {
    		File archivo = new File(ruta);
			FileWriter archivoEscritura = new FileWriter(archivo);
			
			archivoEscritura.write(DB_NAME + " " + tabla + "\n");
			
			
			archivoEscritura.close();
    	} catch (FileNotFoundException ex) {
    		ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
    }*/
    /*
    public static String[] mostrarCabeceraTabla(String tabla)
    {
		DatabaseMetaData datos;
		ResultSet rs;
		String[] resultado;
		try {
			datos = conn.getMetaData();
			rs = datos.getColumns(null, null, tabla, null);
			while(rs.next())
			{
				resultado = resultado[rs.getString("COLUMN_NAME")];
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
    }*/
}