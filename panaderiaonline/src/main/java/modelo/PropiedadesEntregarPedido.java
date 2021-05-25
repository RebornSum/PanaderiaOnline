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
public class PropiedadesEntregarPedido {

	private List<String> textosBotones;
	private List<String> textosLabel;
	private String telefono;
	private String prompText;
	private List<String> error;
	private List<String> numeros;
	
	public PropiedadesEntregarPedido() {
		textosBotones = Arrays.asList("Volver","Imprimir recibo");
		textosLabel = Arrays.asList("Producto: ","Cantidad: ", "Precio unidad: ","Total: ");
		telefono = "Telefono: ";
		prompText = "N�mero del pedido";
		error = Arrays.asList("No se ha encontrado el pedido", "El n�mero de tel�fono no es v�lido");
		numeros = Arrays.asList("0","1","2","3","4","5","6","7","8","9");
	}
}
