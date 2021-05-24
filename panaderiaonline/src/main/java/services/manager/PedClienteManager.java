package services.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import services.dao.PedCliente;
import services.dao.Pedido;

/**
 * 
 * @author Christian Pons Hern�ndez
 *
 */

public class PedClienteManager {
	
	
	
	//	M�TODOS

	/**
	 * Obtiene todos los pedidos de todos los clientes de la base de datos.
	 * @param con Conexi�n con la base de datos.
	 * @return lista con todos los pedidos de todos los clientes.
	 */
	public List<PedCliente> obtenerTodosLosPedidos(Connection con) {
		try (Statement stmt = con.createStatement()) {
			ResultSet result = stmt.executeQuery("SELECT * FROM ped_cliente");
			result.beforeFirst();

			List<Pedido> todosLosPedidos = new PedidoManager().ObtenerTodosLosPedidos(con);
			List<PedCliente> pedidosClientes = new ArrayList<>();

			while (result.next()) {
				pedidosClientes.add(new PedCliente(result));
			}

			pedidosClientes.stream().forEach(
					pedido -> pedido.setPedidos(obtenerPedidosCliente(pedido.getNumPedido(), todosLosPedidos)));

			return pedidosClientes;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * a�ade un pedidio de un cliente a la base de datos.
	 * @param con Conexxi�n con la base de datos.
	 * @param telefono n�mero de tel�fono del cliente.
	 * @param numPedido n�mero del pedido.
	 * @return devuelve un boolean que indica si se ha podido o no introducir el pedido en la base de datos.
	 */
	public boolean a�adirPedido(Connection con, String telefono, int numPedido) {
		try (PreparedStatement stmt = con.prepareStatement(
				"INSERT INTO ped_cliente values(?,?,?)")){
			
			stmt.setString(1, telefono);
			stmt.setInt(2, numPedido);
			stmt.setDate(3, new Date(System.currentTimeMillis()));
			stmt.executeUpdate();
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	

	/**
	 * Recoge todos los subpedidos de 1 �nico cliente.
	 * @param numPedido n�mero de los pedidos del cliente.
	 * @param todosLosPedidos una lista con todos los subpedidos de todos los clientes.
	 * @return
	 */
	private List<Pedido> obtenerPedidosCliente(int numPedido, List<Pedido> todosLosPedidos) {
		List<Pedido> pedidosDelCliente = new ArrayList<>();
		
		/*for (int i = 0; i < todosLosPedidos.size(); i++) {
			if (todosLosPedidos.get(i).getNumPedido() == numPedido) {
				pedidosDelCliente.add(todosLosPedidos.get(i));
			}
		}*/
		
		pedidosDelCliente = todosLosPedidos.stream().filter(pedido -> pedido.getNumPedido() == numPedido).collect(Collectors.toList());

		return pedidosDelCliente;
	}
	

	/**
	 * recoge el valor m�ximo del n�mero de un pedido de la base de datos.
	 * @param con Conexi�n con la base de datos.
	 * @return valor m�ximo de un n�mero de pedido + 1;
	 */
	public int obtenerNumeroPedido(Connection con) {
		try (Statement stmt = con.createStatement()){
			ResultSet result = stmt.executeQuery("SELECT COALESCE(MAX(num_pedido),0) FROM ped_cliente");
			result.first();
			int valor = result.getInt("COALESCE(MAX(num_pedido),0)");
			return valor + 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	
	/**
	 * Obtiene todos los datos del pedido de 1 cliente.
	 * @param con Conexi�n con la base de datos.
	 * @param telefono tel�fono del cliente.
	 * @param numPedido n�mero del pedido.
	 * @return datos del pedido del cliente.
	 */
	public PedCliente obtenerPedido(Connection con, String telefono, int numPedido) {
		try(PreparedStatement stmt = con.prepareStatement("SELECT * FROM ped_cliente WHERE telefono = ? AND num_pedido = ?")) {
			stmt.setString(1, telefono);
			stmt.setInt(2, numPedido);
			ResultSet result = stmt.executeQuery();
			result.first();
			
			PedCliente pedido = new PedCliente(result);
			pedido.setPedidos(new PedidoManager().obtener1SubPedido(con, numPedido));
			
			return pedido;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
}
