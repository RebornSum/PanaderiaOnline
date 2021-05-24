package modelo;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public class PropiedadesEntregarPedido {

	private List<String> textosBotones;
	private List<String> textosLabel;
	private String telefono;
	private String prompText;
	
	public PropiedadesEntregarPedido() {
		textosBotones = Arrays.asList("Volver","Imprimir recivo");
		textosLabel = Arrays.asList("Producto: ","Cantidad: ", "Precio unidad: ");
		telefono = "Telefono: ";
		prompText = "Número del pedido";
	}
}
