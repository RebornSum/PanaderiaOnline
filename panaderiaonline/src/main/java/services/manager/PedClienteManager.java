package services.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import services.dao.PedCliente;
import services.dao.Pedido;

public class PedClienteManager {

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

	// Pendiente de creación

	public void añadirPedido(Connection con, String telefono, int numPedido) {
		try (PreparedStatement stmt = con.prepareStatement(
				"INSERT INTO ped_clientes values(?,?,?")){
			
			stmt.setString(1, telefono);
			stmt.setInt(2, numPedido);
			stmt.setDate(3, new Date(System.currentTimeMillis()));
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	

	private List<Pedido> obtenerPedidosCliente(int numPedido, List<Pedido> todosLosPedidos) {
		List<Pedido> pedidosDelCliente = new ArrayList<>();
		for (int i = 0; i < todosLosPedidos.size(); i++) {
			if (todosLosPedidos.get(i).getNumPedido() == numPedido) {
				pedidosDelCliente.add(todosLosPedidos.get(i));
			}
		}

		return pedidosDelCliente;
	}

	public int obtenerNumeroPedido(Connection con) {
		try (Statement stmt = con.createStatement()){
			ResultSet result = stmt.executeQuery("SELECT COALESCE(MAX(num_pedido),0) FROM ped_cliente");
			return result.getInt(0) + 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
	}
}
