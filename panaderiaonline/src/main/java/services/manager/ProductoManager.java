package services.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import services.dao.Producto;

public class ProductoManager {

	public List<Producto> obtenerProductos(Connection con){
		try(Statement stmt = con.createStatement()) {
			ResultSet result = stmt.executeQuery("SELECT * FROM productos");
			result.beforeFirst();
			
			List<Producto> listaDeProductos = new ArrayList<>();
			
			while(result.next()) {
				listaDeProductos.add(new Producto(result));
				
			}

			return listaDeProductos;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
