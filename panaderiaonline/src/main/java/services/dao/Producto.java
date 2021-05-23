package services.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;

@Getter
public class Producto {
	private int codigo;
	private String nombre;
	private double precio;
	
	public Producto(ResultSet result) {
		try {
			
			this.codigo = result.getInt("codigo");
			this.nombre = result.getString("nombre");
			this.precio = result.getDouble("precio");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
