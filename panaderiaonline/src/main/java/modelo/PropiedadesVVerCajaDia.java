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
public class PropiedadesVVerCajaDia {
	
	private List<String>textos;
	
	public PropiedadesVVerCajaDia() {
		textos = Arrays.asList("Volver","calcular", "El dinero obtenido hoy es: ");
	}
}
