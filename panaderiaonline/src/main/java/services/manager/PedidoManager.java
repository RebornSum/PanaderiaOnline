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

public class PedidoManager {

	public List<Pedido> obtenerListaPedidosCliente(Connection con, int numPedido) {
		// TODO Auto-generated method stub
		return null;
	}

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

	public void añadirPedido(Connection con, Object object, String string) {
		try(PreparedStatement stmt = con.prepareStatement("INSERT INTO pedidos")) {
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private Producto obtenerProductoDelPedido(int codigoProducto, List<Producto> productos) {
		
		return productos.stream().filter(producto -> producto.getCodigo() == codigoProducto).findFirst().orElse(null);
	}

}
