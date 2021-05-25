package modelo;

import java.util.Arrays;
import java.util.List;

import javafx.geometry.Insets;
import lombok.Getter;

/**
 * 
 * @author Christian Pons Hernández
 *
 */

@Getter
public class PropiedadesMostrarPedidos {
	private int vMostrarPedidosTamanoSección1;
	private int vMostrarPedidosTamanoSeccion2;
	private int numeroBotones;
	private List<String>textosBotones;
	private List<String>textosLabels1;
	private List<String>textosLabels2;
	private Insets insets;
	
	public PropiedadesMostrarPedidos() {
		vMostrarPedidosTamanoSección1 = 80;
		vMostrarPedidosTamanoSeccion2 = 60;
		numeroBotones = 4;
		textosBotones = Arrays.asList("Volver al menú", "Ordenar por cliente", "Ordenar por fecha", "Ordenar por tipos de producto");
		textosLabels1 = Arrays.asList("Teléfono:","Fecha:","Numero de Pedido:");
		textosLabels2 = Arrays.asList("Producto","Cantidad");
		insets = new Insets(15,0,10,0);
	}
}
