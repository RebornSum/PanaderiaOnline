package modelo;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

/**
 * 
 * @author Christian Pons Hern�ndez
 *
 */

@Getter
public class PropiedadesMostrarPedidos {
	private int vMostrarPedidosTamanoSecci�n1;
	private int vMostrarPedidosTamanoSeccion2;
	private int numeroBotones;
	private List<String>textosBotones;
	private List<String>textosLabels1;
	private List<String>textosLabels2;
	
	public PropiedadesMostrarPedidos() {
		vMostrarPedidosTamanoSecci�n1 = 80;
		vMostrarPedidosTamanoSeccion2 = 60;
		numeroBotones = 4;
		textosBotones = Arrays.asList("Volver al men�", "Ordenar por cliente", "Ordenar por fecha", "Ordenar por tipos de producto");
		textosLabels1 = Arrays.asList("Tel�fono:","Fecha:","Numero de Pedido:");
		textosLabels2 = Arrays.asList("Producto","Cantidad");
	}
}
