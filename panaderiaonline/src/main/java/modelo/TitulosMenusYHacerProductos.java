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
public class TitulosYmenus {

	private List<String>textosMenu;
	
	public TitulosYmenus() {
		textosMenu = Arrays.asList("Añadir pedido","Mostrar pedidos","Hacer productos","Ver caja del día","Entregar pedido");
	}
}
