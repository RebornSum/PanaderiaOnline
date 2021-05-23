package services.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Pedido {
	private int numPedido;
	@Setter Producto producto;
	private int cantidad;
	
	public Pedido(ResultSet result) {
		try {
			
			numPedido = result.getInt("num_pedido");
			cantidad = result.getInt("cantidad");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}