package services.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Pedido {
	private int numPedido;
	private int codigoProducto;
	@Setter Producto producto;
	private int cantidad;
	
	public Pedido(ResultSet result) {
		try {
			
			numPedido = result.getInt("num_pedido");
			codigoProducto = result.getInt("codigo_producto");
			cantidad = result.getInt("cantidad");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}