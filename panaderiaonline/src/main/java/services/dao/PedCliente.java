package services.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PedCliente {

	private int telefono;
	private int numPedido;
	private Date fecha;
	@Setter private List<Pedido> pedidos;
	
	public PedCliente(ResultSet result) {
		try {
			this.telefono = result.getInt("telefono");
			this.numPedido = result.getInt("num_pedido");
			this.fecha = result.getDate("fecha");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
