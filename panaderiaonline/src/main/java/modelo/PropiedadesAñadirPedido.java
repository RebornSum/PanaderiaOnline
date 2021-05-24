package modelo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import services.conector.Conector;
import services.dao.Producto;
import services.manager.ProductoManager;

@Getter
public class PropiedadesA�adirPedido {

	private List<String>textosMenu;
	private int numeroSeccionesAnadirPedido;
	private List<String>textosSeccion1AnadirPedido;
	private List<String>textosSeccion2AnadirPedido;
	private int tamanoTelefono;
	private String promptText;
	private List<String>mensajesError;
	
	public PropiedadesA�adirPedido() {
		this.textosMenu = Arrays.asList("A�adir pedido","Mostrar pedidos","Ver caja del d�a","Entregar pedido");
		this.numeroSeccionesAnadirPedido = 3;
		this.textosSeccion1AnadirPedido = Arrays.asList("Volver al menu","Confirmar pedido");
		this.textosSeccion2AnadirPedido = Arrays.asList("Tel�fono","A�adir m�s productos");
		this.tamanoTelefono = 9;
		this.promptText = "Cantidad";
		this.mensajesError = Arrays.asList("Introduce un tel�fono real");
		
	}
	
	public List<Producto> obtenerListaDeProductos(){
		try(Connection con = new Conector().getMySQLConnection()) {
			List<Producto>listaDeProductos = new ProductoManager().obtenerProductos(con);
			
			return listaDeProductos;		
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
