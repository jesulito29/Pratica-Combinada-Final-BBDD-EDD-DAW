# Pratica-Combinada-Final-BBDD-EDD-DAW
La versión 3.0 he borrado todos los métodos para gestionar la tabla clientes y he realizado una serie de métodos para gestionar cualquier tabla de cualquier base de datos.
El orden que he seguido para manejar cualquier tabla de cualquier base de datos es el siguiente:
-He realizado un método llamado nombreBaseDatos que preguntara al usuario a que base de datos quiere conectarse. Este método nos conectara a la base de datos escrita por teclado.
-He realizado un método llamado verTablas que nos mostrara los nombres de la tablas de la base de datos a la que estamos conectados. Con este metodo podré ver los nombres de las tablas de cualquier base de datos.
-He realizado un método llamado verCamposTabla que nos mostrara los campos de la tabla que le indicemos como parametro. Con este método podré ver los campos de cualquier tabla de la base de datos a la que esté conectada.
-He realizado un método llamado mostrarContenidoTabla que nos mostrara los datos de los campos de la tabla que le indiquemos como parámetro. Con este método podré ver el contenido de cualquier tabla de la base de datos a la que esté conectada.
 -He creado un método llamado crearTabla que creara una tabla con el nombre y el número de columnas le indicemos. Le pasaremos un método que pida el nombre de la columna y el tipo de dato. Con este método podremos crear una tabla en cualquier base de datos.
-He creado un método llamado pedirDatosCreaciónTabla. Este método será invocado en el metodo de crear una tabla para pedir el nombre del campo y el tipo de dato que tendrá ese campo.
