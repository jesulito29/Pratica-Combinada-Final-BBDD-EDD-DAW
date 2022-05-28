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

import java.sql.ResultSet;

/**
 * Clase que contiene los metodos para manejer cualquier base de datos
 * 
 * @author Jesus Sanchez Torres
 * @version 5.0
 */
public class DBManager {

	// Conexión a la base de datos
	private static Connection conn = null;
	//guarda el nombre de las columnas
	static ArrayList<String> contadorColumnas = new ArrayList<String>();

	// Configuración de la conexión a la base de datos
	private static final String DB_HOST = "localhost";
	private static final String DB_PORT = "3306";
	private static String DB_NAME = "";
	private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/";
	private static final String DB_SER = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT;
	private static final String DB_USER = "root";
	private static final String DB_PASS = "root";
	private static final String DB_MSQ_CONN_OK = "CONEXIÓN CORRECTA";
	private static final String DB_MSQ_CONN_NO = "ERROR EN LA CONEXIÓN";

	//////////////////////////////////////////////////
	// MÉTODOS DE CONEXIÓN A LA BASE DE DATOS
	//////////////////////////////////////////////////
	;

	/**
	 * Intenta cargar el JDBC driver.
	 * 
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
			System.err.println("No se ha podido cargar el driver");
			return false;
		}
	}
	
	/**
	 * Muestra la base de datos a las que nos podemos conectarnos
	 */
	public static void baseDatosDisponibles() {
		PreparedStatement stmt;
		ResultSet rs;
		try {
			System.out.println("Listado de base de datos");
			conn = DriverManager.getConnection(DB_SER, DB_USER, DB_PASS);
			stmt = conn.prepareStatement("show databases");
			rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString("Database"));
			}
		} catch (SQLException e) {
			System.err.println("No se ha podido mostrar las bases de datos disponibles en el sistema");
		}
	}

	/**
	 * Intenta conectar con la base de datos.
	 * @param baseDatos: nombre de la base de datos a la que nos queramos conectar
	 * @return true si pudo conectarse, false en caso contrario
	 */
	public static boolean connect(String baseDatos) {
		try {
			DB_NAME = baseDatos;
			System.out.print("Conectando a la base de datos...");
			conn = DriverManager.getConnection(DB_URL.concat(baseDatos), DB_USER, DB_PASS);
			System.out.println("OK!");
			return true;
		} catch (SQLException ex) {
			System.err.println("No se ha podido conectar a la base de datos");
			return false;
		}
	}

	/**
	 * Muestra las tablas de la base de datos a la que estamos conectados
	 */
	public static void verTablas() {
		try {
			DatabaseMetaData datos = conn.getMetaData();
			System.out.println("Lista de tablas");
			//vemos las tabla de una base de datos
			ResultSet rs = datos.getTables(DB_NAME, null, null, null);
						
			while (rs.next()) {
				System.out.println(rs.getString("TABLE_NAME"));
			}
		} catch (SQLException ex) {
			System.out.println("No se ha podido ver las tablas");
		}
	}

	/**
	 * Muestra los campos de una tabla de la base de datos
	 * 
	 * @param tabla: tabla de la base de datos
	 * @return un texto con los campos de la tabla, null en caso contrario
	 */
	public static String verCamposTabla(String tabla) {
		contadorColumnas.clear();
		DatabaseMetaData datos;
		ResultSet rs;
		String nombreColumna;
		String resultado = "";
		try {
			datos = conn.getMetaData();
			//vemos las columnas de una tabla de una base de datos
			rs = datos.getColumns(DB_NAME, null, tabla, null);
			
			while (rs.next()) {
				nombreColumna = rs.getString("COLUMN_NAME");
				//añadimos esas columnas a un array
				contadorColumnas.add(nombreColumna);
				resultado += nombreColumna + "\t";
			}
			resultado += "\n";
			return resultado;
		} catch (SQLException ex) {
			System.err.println("No se ha podido ver los campos de la tabla");
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
			System.err.println("No estas conectado a ninguna base de datos");
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
			System.err.println("No se ha podido cerrar la conexion");
		}
	}

	/**
	 * Muestra el contenido de una tabla de la base de datos
	 * 
	 * @param tabla: tabla de la base de datos
	 * @return un texto con el contenido de las tablas, null en caso contrario
	 */
	public static String mostrarContenidoTabla(String tabla) {
		PreparedStatement stmt;
		ResultSet rs;
		String resultado;
		try {
			resultado = verCamposTabla(tabla);
			//consulta para ver el contenido de una tabla
			stmt = conn.prepareStatement("select * from " + tabla);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				//recoremos el arrayList con cada nombre de campo de esa tabla
				for (int i = 0; i < contadorColumnas.size(); i++) {
					String contadorAux = contadorColumnas.get(i);
					resultado += rs.getString(contadorAux) + "\t";
				}
				resultado += "\n";
			}
			return resultado;
		} catch (SQLException e) {
			System.err.println("No se ha podido mostrar el contenido de la tabla");
			return null;
		}
	}

	/**
	 * Pedimos los datos de la tabla al crear una nueva tabla en la base de datos
	 * 
	 * @param nombreCampo: nombre del campo
	 * @param tipoCampo:   tipo de dato que tendra el campo
	 * @return un texto con el nombre y el tipo de dato
	 */
	public static String pedirDatosTabla(String nombreCampo, String tipoCampo) {
		Scanner ent = new Scanner(System.in);
		String resultado = null;
		System.out.println("Nombre del campo:");
		nombreCampo = ent.nextLine();
		System.out.println("Tipo Campo:");
		System.out.println("Opciones: String | char | int | double | boolean");
		tipoCampo = ent.nextLine();
		switch (tipoCampo) {
		case "String": {
			resultado = nombreCampo + " varchar(50)";
			break;
		}
		case "char": {
			resultado = nombreCampo + " char";
			break;
		}
		case "int": {
			resultado = nombreCampo + " int";
			break;
		}
		case "double": {
			resultado = nombreCampo + " double";
			break;
		}
		case "boolean": {
			resultado = nombreCampo + " boolean";
			break;
		}
		default: {
			System.err.println("No existe el tipo de dato");
		}
		}
		return resultado;
	}

	/**
	 * Crea una tabla en la base de datos
	 * 
	 * @param tabla:       nombre que tendra la tabla
	 * @param numColumnas: numero columnas que tendra la tabla
	 * @return true si ha creado la tabla, en caso contrario false
	 */
	public static boolean crearTabla(String tabla, int numColumnas) {
		Scanner ent = new Scanner(System.in);
		String tipoCampo = null, nombreCampo = null, consulta, informacion = "";
		//creamos un array con el numero de columnas que le indicemos
		String crearTabla[] = new String[numColumnas];
		int rs;
		PreparedStatement stmt;
		try {
			//recomeros el array
			for (int i = 0; i < numColumnas; i++) {
				//alamcenamos en el array el nombre del campo y tipo de campo
				crearTabla[i] = pedirDatosTabla(nombreCampo, tipoCampo);
			}
			//recorremos el array
			for (int i = 0; i < crearTabla.length; i++) {
				//si es la ultima poscion que ponga el signo ")"
				if (i == crearTabla.length - 1) {
					informacion += crearTabla[i] + ");";
				} else {
					informacion += crearTabla[i] + ",";
				}
			}
			//consulta para crear la tabla
			consulta = "create table " + tabla + "(";
			stmt = conn.prepareStatement(consulta.concat(informacion));
			rs = stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println("No se ha podido crear la tabla");
			return false;
		}
	}

	/**
	 * Modificamos una tabla en la base de datos
	 * 
	 * @param tabla: tabla que queremos modificar
	 * @return true si se ha podido modificar, en caso contrario false
	 */
	public static boolean modificarTabla(String tabla) {
		int opcion;
		opcion = menuModificarTabla();

		switch (opcion) {
		case 1: {
			anadirNuevoRegistro(tabla);
			return true;
		}
		case 2: {
			cambiarTipoDatoRegistro(tabla);
			return true;
		}
		case 3: {
			renombarRegistro(tabla);
			return true;
		}
		default: {
			System.err.println("No existe esta opcion");
			return false;
		}
		}
	}

	/**
	 * Añade un nuevo registro a una tabla
	 * 
	 * @param tabla: tabla que queremos añadir el registro
	 */
	public static void anadirNuevoRegistro(String tabla) {
		PreparedStatement stmt;
		int rs;
		String nombreCampo = null, tipoCampo = null, resultado;
		resultado = pedirDatosTabla(nombreCampo, tipoCampo);
		try {
			//consulta para añadir un nuevo registro a la tabla
			stmt = conn.prepareStatement("alter table " + tabla + " add " + resultado);
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("No se ha podido añadir el nuevo registro");
		}
	}

	/**
	 * Cambiamos el tipo de dato de un registro en la tabla
	 * 
	 * @param tabla: tabla de la que queremos cambiar el registro
	 */
	public static void cambiarTipoDatoRegistro(String tabla) {
		PreparedStatement stmt;
		int rs;
		String nombreCampo = null, tipoCampo = null, resultado;
		resultado = pedirDatosTabla(nombreCampo, tipoCampo);
		try {
			//consulta para cambiar el tipo de dato a un registro
			stmt = conn.prepareStatement("alter table " + tabla + " modify " + resultado);
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("No se ha podido cambiar el tipo de dato del registro");
		}
	}

	/**
	 * Renombramos el registro de un campo de una tabla
	 * 
	 * @param tabla: tabla que queremos renombrar el registro
	 */
	public static void renombarRegistro(String tabla) {
		PreparedStatement stmt;
		Scanner ent = new Scanner(System.in);
		int rs;
		String nuevonombreCampo, viejonombreCampo;
		System.out.println("Nombre de campo a modificar: ");
		viejonombreCampo = ent.nextLine();
		System.out.println("Nuevo nombre: ");
		nuevonombreCampo = ent.nextLine();
		try {
			//consulta para renombrar el nombre de un campo
			stmt = conn.prepareStatement("alter table " + tabla + " change " + viejonombreCampo + nuevonombreCampo);
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("No se ha podido renombrar registro");
		}
	}

	/**
	 * Menu de modificacion de la tabla
	 * 
	 * @return numero segun la opcion elegida, en caso contrario 0
	 */
	public static int menuModificarTabla() {
		Scanner ent = new Scanner(System.in);
		int opcion;
		try {
			System.out.println("Que deseas hacer: ");
			System.out.println("1. Añadir nuevo registro");
			System.out.println("2. Cambiar tipo de dato registro");
			System.out.println("3. Renombrar columna");
			return opcion = ent.nextInt();
		} catch (InputMismatchException ex) {
			System.err.println("Debes introducir un valor entero");
		}
		return 0;
	}

	/**
	 * Borramos una tabla de la base de datos
	 * 
	 * @param tabla: tabla que queramos borrar
	 * @return true si se ha podido borrar, en caso contrario false
	 */
	public static boolean borradoTabla(String tabla) {
		int opcion;
		opcion = menuBorradoTabla();

		switch (opcion) {
			case 1: {
				borrarTablaEntera(tabla);
				return true;
			}
			case 2: {
				borrarCampoTabla(tabla);
				return true;
			}
			default: {
				System.err.println("No existe esta opcion");
				return false;
			}
		}
	}

	/**
	 * Menu de borrado de tabla
	 * 
	 * @return numero segun la opcion elegida, en caso contrario 0
	 */
	public static int menuBorradoTabla() {
		Scanner ent = new Scanner(System.in);
		int opcion;
		try {
			System.out.println("Que deseas hacer: ");
			System.out.println("1. Eliminar tabla");
			System.out.println("2. Eliminar campo de tabla");
			return opcion = ent.nextInt();
		} catch (InputMismatchException ex) {
			System.err.println("Debes introducir un valor entero");
		}
		return 0;
	}

	/**
	 * Borra una tabla entera de la base de datos
	 * 
	 * @param tabla: tabla que queramos borrar
	 */
	public static void borrarTablaEntera(String tabla) {
		PreparedStatement stmt;
		int rs;
		try {
			//consulta para eliminar una tabla
			stmt = conn.prepareStatement("drop table " + tabla);
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("No se puede borrar la tabla");
		}
	}

	/**
	 * Borra un campo de la tabla de la base de datos
	 * 
	 * @param tabla: tabla que queramos borrar
	 */
	public static void borrarCampoTabla(String tabla) {
		PreparedStatement stmt;
		Scanner ent = new Scanner(System.in);
		String campo;
		System.out.println("Introduce el nombre de la columna a eliminar: ");
		campo = ent.nextLine();
		int rs;
		try {
			//consulta para borrar un campo de una tabla
			stmt = conn.prepareStatement("alter table " + tabla + " drop " + campo);
			rs = stmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("No se puede borrar el campo de la tabla");
		}
	}

	/**
	 * Volcamos en un fichero una tabla de la base de datos
	 * 
	 * @param tabla: tabla que queramos volcar en un fichero
	 * @return true si se ha podido borrar, en caso contrario false
	 */
	public static boolean volcarTablaFichero(String tabla) {
		Scanner ent = new Scanner(System.in);
		String ruta = null, sql;

		try {
			System.out.println("Escribe la ruta donde quieres volcar la informacion: ");
			ruta = ent.nextLine();

			File archivo = new File(ruta);
			FileWriter archivoEscritura = new FileWriter(archivo);
			//escribimos en el fichero
			archivoEscritura.write(DB_NAME + " " + tabla + "\n");
			archivoEscritura.write(mostrarContenidoTabla(tabla));
			archivoEscritura.close();
			return true;
		} catch (FileNotFoundException ex) {
			System.err.println("No existe la ruta indicada");
			return false;
		} catch (IOException ex) {
			System.err.println("No se ha podido acceder al fichero");
			return false;
		}
	}

	/**
	 * Lee los datos del fichero
	 * 
	 * @param ruta: ruta del fichero que queremos leer los datos
	 * @return un array con los datos del fichero
	 */
	public static String[] leerDatosFichero(File ruta) {
		String informacion;
		Scanner lectorFichero = null;
		Scanner lectorFichero1 = null;
		String nombreBaseDatos, nombreTabla, nombreCampos, datosCampos;
		int numLineas = 0;
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
		return array;
	}

	/**
	 * Insertar los datos del fichero en la base de datos
	 * 
	 * @param ruta: ruta del fichero que queremos inertar los datos
	 * @return un true si ha podido insertar los datos, false en caso contrario
	 */
	public static boolean insertarDatosDesdeFichero(File ruta) {
		//metemos en un array la lectura del fichero
		String array[] = leerDatosFichero(ruta);
		String informacion = "";
		//almacenamos el nombre de la base de datos
		String nombreBaseDatos = array[0];
		//almacenamos el nombre de la tabla
		String nombreTabla = array[1];
		//almacenamos los nombres de los campos
		String nombreCampos = array[2];
		PreparedStatement miStatement;
		int setencia;
		//recoremos el array a partir de la posicion 3 que es a partir de donde estan los datos de los campos
		for (int j = 3; j < array.length; j++) {
			//guardamos los datos de los campos
			String datos[] = array[j].split(",");
			//recoremos el array de los datos de campos
			for (int i = 0; i < datos.length; i++) {
				//si es la ultima posicion que me lo guarde sin una "," al final
				if (i == datos.length - 1) {
					informacion += "'" + datos[i] + "'";
				} else {
					informacion += "'" + datos[i] + "',";
				}
			}
			try {
				//consulta para insertar los datos
				miStatement = conn.prepareStatement("insert into " + nombreBaseDatos + "." + nombreTabla + "("
						+ nombreCampos + ") values(" + informacion + ");");
				setencia = miStatement.executeUpdate();
			} catch (SQLException e) {
				System.err.println("No se ha podido insertar los datos del fichero");
				return false;
			}
			//borramos el contenido para guardar el siguiente dato a insertar
			informacion = "";
		}
		return true;
	}

	/**
	 * Mofiica los datos del fichero en la base de datos
	 * 
	 * @param ruta: ruta del fichero que queremos modificar los datos
	 * @return un true si ha podido modificar los datos, false en caso contrario
	 */
	public static boolean modificarDatosDesdeFichero(File ruta) {
		//metemos en un array la lectura del fichero
		String array[] = leerDatosFichero(ruta);
		String informacion = "";
		//almacenamos el nombre de la base de datos
		String nombreBaseDatos = array[0];
		//almacenamos el nombre de la tabla
		String nombreTabla = array[1];
		//almacenamos los nombres de los campos
		String nombreCampos[] = array[2].split(",");
		PreparedStatement miStatement;
		int setencia;

		//recoremos el array a partir de la posicion 3 que es a partir de donde estan los datos de los campos
		for (int j = 3; j < array.length; j++) {
			//guardamos los datos de los campos
			String datos[] = array[j].split(",");
			//recoremos el array de los datos de campos
			for (int i = 1; i < datos.length; i++) {
				//gauradamos el contendido de cada campo con comillas
				informacion = "'" + datos[i] + "'";
				try {
					//consulta para modficar los datos
					miStatement = conn.prepareStatement(
							"update " + nombreBaseDatos + "." + nombreTabla + " set " + nombreCampos[i] + " = "
									+ informacion + " where " + nombreCampos[0] + " = '" + datos[0] + "';");
					setencia = miStatement.executeUpdate();
				} catch (SQLException e) {
					System.err.println("No se ha podido modificar los datos del fichero");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Borrar los datos del fichero en la base de datos
	 * 
	 * @param ruta: ruta del fichero que queremos borrar los datos
	 * @return true si ha podido borrar los datos, false en caso contrario
	 */
	public static boolean borrarDatosDesdeFichero(File ruta) {
		//metemos en un array la lectura del fichero
		String array[] = leerDatosFichero(ruta);
		//almacenamos el nombre de la base de datos
		String nombreBaseDatos = array[0];
		//almacenamos el nombre de la tabla
		String nombreTabla = array[1];
		//almacenamos el nombre del campo clave
		String campoClave = array[2];
		//almacenamos los datos del campo
		String identificadorClave[] = array[3].split(",");
		String informacion = "";
		PreparedStatement miStatement;
		int setencia;
		//recorremos el array
		for (int i = 0; i < identificadorClave.length; i++) {
			//gauradamos el contendido de cada campo con comillas
			informacion += "'" + identificadorClave[i] + "'";
			try {
				//consulta para borrar los datos
				miStatement = conn.prepareStatement("delete from " + nombreBaseDatos + "." + nombreTabla + " where "
						+ campoClave + " = " + informacion + ";");
				setencia = miStatement.executeUpdate();
				//borramos el contenido para guardar el siguiente dato a borrar
				informacion = "";
			} catch (SQLException e) {
				System.err.println("No se ha podido borrar los datos del fichero");
				return false;
			}
		}
		return true;
	}
	/**
	 * Ejecuta un procedimiento alamcenado de mostrar los productos de los fabricante
	 * @param fabricante: nombre fabricante que queramos ver los productos
	 * @return true si se ha podido ejecutar el procedimeinto, false en caso contrario
	 */
	public static boolean procedimientoAlmacenadomostrarProductoFabricante(String fabricante)
	{
		java.sql.CallableStatement procedimiento;
		ResultSet setencia;
		try {
			//consulta para llamar a un procedimeinto
			procedimiento =  conn.prepareCall("{call " + DB_NAME +  ".mostrarProductoFabricante('" + fabricante + "')}");
			setencia = procedimiento.executeQuery();
			while (setencia.next())
			{
				System.out.println(setencia.getString(1));
			}
			return true;
		} catch (SQLException e) {
			System.err.println("No se ha podido ejecutar el procedimiento almacenado");
			return false;
		}
	}
	/**
	 * Ejecuta un procedimiento almacenado de mostrar los clientes
	 * @return true si ha podido ejecutar el procedimiento, false en caso contrario
	 */
	public static boolean procedimientoAlmacenadoMostrarClientes() {
		java.sql.CallableStatement procedimiento;
		ResultSet setencia;
		try {
			//consulta para llamar  a un procedimiento
			procedimiento =  conn.prepareCall("{call " + DB_NAME + ".mostrarClientes()}");
			setencia = procedimiento.executeQuery();
			while (setencia.next())
			{
				System.out.println(setencia.getInt(1) + " " + setencia.getString(2) + " " + setencia.getString(3));
			}
			return true;
		} catch (SQLException e) {
			System.err.println("No se ha podido ejecutar el procedimiento almacenado");
			return false;
		}
	}
	/**
	 * Filtra las filas de la tabla a traves de un campo y la informacion del campo
	 * @param tabla: tabla por la que quereamos filtrar
	 * @param campo: campo por el que queremos filtrar
	 * @param informacionCampo: informacion del campo
	 * @return true si se ha podido filtrar filas, false en caso contrario
	 */
	public static boolean filtrarFilasTabla(String tabla, String campo, String informacionCampo)
	{
		PreparedStatement stmt;
		ResultSet rs;
		try {
			//consulta para filtrar filas tabla
			stmt = conn.prepareStatement("select * from " + DB_NAME + "." + tabla + " where " + campo + " = '" + informacionCampo + "';");
			rs = stmt.executeQuery();
			while (rs.next())
			{
				//ejecutamos el arrayList en el que habra los nombres de los campos de la tabla a filtrar
				for (int i = 0; i < contadorColumnas.size(); i++) {
					String contadorAux = contadorColumnas.get(i);
					System.out.print(rs.getString(contadorAux) + "\t");
				}				
				System.out.println("");
			}
			return true;
		} catch (SQLException e) {
			System.err.println("No se ha podido realizar la consulta");
			return false;
		}
	}
	/**
	 * Cambia de base de datos
	 * 
	 * @param BaseDatos: nombre de la base de datos que queramos cambiar
	 * @return true si ha podido cambiar de base datos, false en caso contrario
	 */
	public static boolean cambiarBaseDatos(String BaseDatos) {
		PreparedStatement stmt;
		int rs;
		DB_NAME = BaseDatos;
		try {
			//consulta para cambiarnos de base datos
			stmt = conn.prepareStatement("use " + BaseDatos);
			rs = stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println("No se ha podido cambiar de base de datos");
			return false;
		}
	}

}