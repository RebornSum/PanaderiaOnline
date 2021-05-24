package services.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import services.dao.Pedido;
import services.dao.Producto;

/**
 * 
 * @author Christian Pons Hern�ndez
 *
 */

public class PedidoManager {


	//	M�TODOS
	
	
	/**
	 * recoge todos los pedidos de la base de datos y les a�ade los datos de sus productos.
	 * @param con Conexi�n a la base de datos.
	 * @return Lista con todos los pedidos
	 */
	public List<Pedido> ObtenerTodosLosPedidos(Connection con) {
		try(Statement stmt = con.createStatement()) {
			List<Pedido>todosLosPedidos = new ArrayList<>();
			List<Producto>productos = new ProductoManager().obtenerProductos(con);
			
			ResultSet result = stmt.executeQuery("SELECT * FROM pedidos");
			result.beforeFirst();
			
			while(result.next()) {
				todosLosPedidos.add(new Pedido(result));
			}
			
			todosLosPedidos.forEach(pedido -> pedido.setProducto(obtenerProductoDelPedido(pedido.getCodigoProducto(),productos)));
			
			return todosLosPedidos;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	
	/**
	 * A�ade un pedido a la base de datos.
	 * @param con Conexi�n con la base de datos,
	 * @param numPedido Numero del pedido.
	 * @param codgo C�digo del producto perteneciente al pedido.
	 * @param cantidad N�mero de unidades de dicho producto.
	 */
	public void a�adirPedido(Connection con, int numPedido, int codgo, int cantidad) {
		try(PreparedStatement stmt = con.prepareStatement("INSERT INTO pedidos VALUES(?,?,?)")) {
			stmt.setInt(1, numPedido);
			stmt.setInt(2, codgo);
			stmt.setInt(3, cantidad);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

	/**
	 * recoge �nicamente los pedidos de un cliente de la base de datos.
	 * @param con Conexi�n con la base de datos.
	 * @param numPedido n�mero del pedido.
	 * @return lista de todos los pedidos del cliente.
	 */
	public List<Pedido> obtener1SubPedido(Connection con, int numPedido) {
		try(PreparedStatement stmt = con.prepareStatement("SELECT * FROM pedidos WHERE num_pedido = ?")) {
			stmt.setInt(1, numPedido);
			ResultSet result = stmt.executeQuery();
			result.beforeFirst();
			
			List<Pedido>subPedidos = new ArrayList<>();
			List<Producto>productos = new ProductoManager().obtenerProductos(con);
	
			
			while(result.next()) {
				subPedidos.add(new Pedido(result));
			}
			
			subPedidos.forEach(pedido -> pedido.setProducto(obtenerProductoDelPedido(pedido.getCodigoProducto(),productos)));
			
			return subPedidos;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * obtiene para cada c�digo del producto todos l�s datos del producto.
	 * @param codigoProducto c�digo dl producto buscado.
	 * @param productos lista de todos los productos de la base de datos.
	 * @return Datos del producto.
	 */
	private Producto obtenerProductoDelPedido(int codigoProducto, List<Producto> productos) {
		
		return productos.stream().filter(producto -> producto.getCodigo() == codigoProducto).findFirst().orElse(null);
	}

}
