/** Procedimiento almacenado que muestra los clientes */
delimiter $$
drop procedure if exists mostrarClientes$$
create procedure mostrarClientes()
begin
	select * from clientes;
end 
$$

/** Procedimiento almacenado que muestra los productos de un fabricante */
delimiter $$
drop procedure if exists mostrarProductoFabricante$$
create procedure mostrarProductoFabricante(in fabricante varchar(30))
begin
	select P.nombre from producto P cross join fabricante F on P.codigo_fabricante=F.codigo where F.nombre=fabricante;
end 
$$