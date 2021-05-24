package modelo;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

/**
 * 
 * @author Christian Pons Hernández
 *
 */

@Getter
public class PropiedadesEntregarPedido {

	private List<String> textosBotones;
	private List<String> textosLabel;
	private String telefono;
	private String prompText;
	private String error;
	
	public PropiedadesEntregarPedido() {
		textosBotones = Arrays.asList("Volver","Imprimir recivo");
		textosLabel = Arrays.asList("Producto: ","Cantidad: ", "Precio unidad: ","Total: ");
		telefono = "Telefono: ";
		prompText = "Número del pedido";
		error = "No se ha encontrado el pedido";
	}
}
