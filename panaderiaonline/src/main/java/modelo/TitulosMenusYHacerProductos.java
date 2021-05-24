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
public class TitulosMenusYHacerProductos {

	private List<String>textosMenu;
	private List<String>textosHacerProductos;
	private Insets insetsHacerProductos;
	
	public TitulosMenusYHacerProductos() {
		textosMenu = Arrays.asList("Añadir pedido","Mostrar pedidos","Hacer productos","Ver caja del día","Entregar pedido");
		textosHacerProductos = Arrays.asList("Hacen Falta:","Confirmar");
		insetsHacerProductos = new Insets(20,0,0,0);
		
	}
}
