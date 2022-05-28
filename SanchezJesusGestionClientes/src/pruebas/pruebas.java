package pruebas;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
/**
 * Clase que contiene metodos de pruebas
 * @author Jesus Sanchez Torres
 *
 */
class pruebas {
	private static Connection conn = null;
	String DB_HOST = "localhost";
	String DB_PORT = "3306";
	String DB_USER = "root";
	String DB_PASS = "root";
	static String DB_NAME = "tienda";
	String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
	
	@Test
	/**
	 * Prueba de conexion correcta
	 */
	void testConnect() {
		
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			System.out.println("Conexion OK");
		} catch (SQLException e) {
			System.err.println("No se ha podido conectar a la base de datos");
		}
	}
	
	@Test
	/**
	 * Prueba de conexion erronea
	 */
	void testConnect2() {
		
		try {
			String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + "aaa";
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			System.out.println("Conexion OK");
		} catch (SQLException e) {
			System.err.println("No se ha podido conectar a la base de datos");
		}
	}
		

	@Test
	/**
	 * Preuba de ver tablas
	 */
	void testVerTablas() {
		
		DatabaseMetaData datos;
		try {
				Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				datos = conn.getMetaData();
				System.out.println("Lista de tablas");
				ResultSet rs = datos.getTables(DB_NAME, null, null, null);
				while (rs.next()) {
					System.out.println(rs.getString("TABLE_NAME"));
				}
		} catch (SQLException e) {
			System.err.println("No se ha podido mostrar las tablas");
		}
	}

	@Test
	/**
	 * Preuba de ejecutar un procedimiento almacenado
	 */
	void testProcedimientoAlmacenadoMostrarClientes() {
		java.sql.CallableStatement procedimiento;
		ResultSet setencia;
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			procedimiento =  conn.prepareCall("{call " + DB_NAME + ".mostrarClientes()}");
			setencia = procedimiento.executeQuery();
			while (setencia.next())
			{
				System.out.println(setencia.getInt(1) + " " + setencia.getString(2) + " " + setencia.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("No se ha podido ejecutar el procedimiento almacenado");
		}
	}

	@Test
	/**
	 * Preuba de volcar tabla en un fichero
	 */
	void testVolcarTablaFichero() {
		Scanner ent = new Scanner(System.in);
		String ruta = null, sql;
		String tabla = "clientes";

		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			ruta = "./prueba.txt";

			File archivo = new File(ruta);
			FileWriter archivoEscritura = new FileWriter(archivo);
			//escribimos en el fichero
			archivoEscritura.write(DB_NAME + " " + tabla + "\n");
			archivoEscritura.write("PRUEBA");
			archivoEscritura.close();
			System.out.println("Se ha insertado los datos en el fichero");
		} catch (FileNotFoundException ex) {
			System.err.println("No existe la ruta indicada");
		} catch (IOException ex) {
			System.err.println("No se ha podido acceder al fichero");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("No se ha podido ejecutar el procedimiento almacenado");
		}
	}
	
	@Test
	/**
	 * Prueba de filtra filas de una tabla
	 */
	void testFiltrarFilasTabla()
	{
		PreparedStatement stmt;
		ResultSet rs;
		String tabla = "clientes";
		String campo = "direccion";
		String informacionCampo = "Valencia";
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			stmt = conn.prepareStatement("select * from " + DB_NAME + "." + tabla + " where " + campo + " = '" + informacionCampo + "';");
			rs = stmt.executeQuery();
			while (rs.next())
			{
				System.out.print(rs.getString("id") + "\t");
				System.out.print(rs.getString("nombre") + "\t");
				System.out.print(rs.getString("direccion"));
				System.out.println();
			}			
		} catch (SQLException e) {
			System.err.println("No se ha podido realizar la consulta");
		}
	}
	
	@Test
	/**
	 * Prueba para leer los datos de un fichero
	 */
		void testLeerDatosFichero() {
		String informacion;
		Scanner lectorFichero = null;
		Scanner lectorFichero1 = null;
		String nombreBaseDatos, nombreTabla, nombreCampos, datosCampos;
		int numLineas = 0;
		File ruta = new File("./insertar.txt");
		try {
			lectorFichero = new Scanner(ruta);
			lectorFichero1 = new Scanner(ruta);

		} catch (FileNotFoundException e) {
			System.err.println("No existe la ruta indicada");
		}

		do {
			//leemos los datos del fichero
			informacion = lectorFichero1.nextLine();
			//almacenamos el numero de lineas del fichero
			numLineas++;
		} while (lectorFichero1.hasNext());
		lectorFichero1.close();
		//creamos un array con la posiciones de ese numero de lineas
		String array[] = new String[numLineas];
		//igualamos el numero de lineas a 0 para que empieze en la posicon 0 a guardar
		numLineas = 0;
		do {
			//leemos los datos del fichero
			informacion = lectorFichero.nextLine();
			//en cada posicion almacenara una linea del fichero
			array[numLineas] = informacion;
			//incrementamos el numero de lineas para que en cada posicon guarden los datos
			numLineas++;
		} while (lectorFichero.hasNext());
		lectorFichero.close();
		System.out.println("Se ha leido los datos");
	}
	
	@Test
	/**
	 * Prueba para  cambiar de base de datos
	 */
	void testCambiarBaseDatos() {
		PreparedStatement stmt;
		int rs;
		String baseDatos = "test";
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
			stmt = conn.prepareStatement("use " + baseDatos);
			rs = stmt.executeUpdate();
			System.out.println("Se ha cambiado de base de datos");
		} catch (SQLException e) {
			System.err.println("No se ha podido cambiar de base de datos");
		}
	}
	
	

}