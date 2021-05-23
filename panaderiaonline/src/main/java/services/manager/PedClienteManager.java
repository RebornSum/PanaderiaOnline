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

			List<Pedido> todosLosPedidos = new PedidoManager().ObtenerTodosLosPedidos();
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

	public void añadirPedido(Connection con, String telefono) {
		try (PreparedStatement stmt = con.prepareStatement(
				"INSERT INTO ped_clientes values(?,(SELECT COALESCE(MAX(a.num_pedido),0) FROM ped_cliente a) + 1,?")){
			
			stmt.setString(1, telefono);
			stmt.setDate(2, new Date(System.currentTimeMillis()));
			stmt.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// MOVER AL CONTROLADOR DE LA VENTANA

	/*
	 * public List<PedCliente> obtenerPedidosOrdenadosPorCliente(Connection con){
	 * List<PedCliente> listaOrdenada = obtenerTodosLosPedidos(con).stream()
	 * .sorted(Comparator.comparing(PedCliente::getTelefono)).collect(Collectors.
	 * toList());
	 * 
	 * return listaOrdenada; }
	 * 
	 * public List<PedCliente> obtenerPedidosOrdenadosPorDia(Connection con){
	 * List<PedCliente> listaOrdenada = obtenerTodosLosPedidos(con).stream()
	 * .sorted(Comparator.comparing(PedCliente::getFecha)).collect(Collectors.toList
	 * ());
	 * 
	 * return listaOrdenada; }
	 * 
	 * public List<PedCliente> obtenerPedidosOrdenadosPorTipoDePanes(Connection
	 * con){ List<PedCliente> listaOrdenada = obtenerTodosLosPedidos(con).stream()
	 * .sorted(Comparator.comparing(Producto::getNombre)).collect(Collectors.toList(
	 * ));
	 * 
	 * return listaOrdenada; }
	 */

	private List<Pedido> obtenerPedidosCliente(int numPedido, List<Pedido> todosLosPedidos) {
		List<Pedido> pedidosDelCliente = new ArrayList<>();
		for (int i = 0; i < todosLosPedidos.size(); i++) {
			if (todosLosPedidos.get(i).getNumPedido() == numPedido) {
				pedidosDelCliente.add(todosLosPedidos.get(i));
			}
		}

		return pedidosDelCliente;
	}
}
