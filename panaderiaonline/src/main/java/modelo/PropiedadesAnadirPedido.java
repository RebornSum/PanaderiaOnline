package modelo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.geometry.Insets;
import lombok.Getter;
import services.conector.Conector;
import services.dao.Producto;
import services.manager.ProductoManager;

/**
 * 
 * @author Christian Pons Hernández
 *
 */

@Getter
public class PropiedadesAnadirPedido {

	private int numeroSecciones;
	private List<String>botonesSeccion1;
	private List<String>textosSeccion2;
	private int tamanoTelefono;
	private String promptText;
	private List<String>mensajes;
	private List<String>numeros;
	private Insets instets;
	
	public PropiedadesAnadirPedido() {
		numeroSecciones = 3;
		botonesSeccion1 = Arrays.asList("Volver al menu","Confirmar pedido");
		textosSeccion2 = Arrays.asList("Teléfono","Añadir más productos");
		tamanoTelefono = 9;
		promptText = "Cantidad";
		mensajes = Arrays.asList("Introduce un teléfono real","El pedido ya ha sido introducido","Pedido añadido corréctamente");
		numeros = Arrays.asList("0","1","2","3","4","5","6","7","8","9");
		instets = new Insets(20, 0, 0, 0);
		
	}
}
