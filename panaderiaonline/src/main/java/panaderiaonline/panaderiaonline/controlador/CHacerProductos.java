package panaderiaonline.panaderiaonline.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import modelo.TitulosMenusYHacerProductos;
import panaderiaonline.panaderiaonline.App;
import services.conector.Conector;
import services.dao.PedCliente;
import services.dao.Producto;
import services.manager.PedClienteManager;
import services.manager.ProductoManager;

/**
 * 
 * @author Christian Pons Hernández
 *
 */

public class CHacerProductos implements Initializable {

	//	ATRIBUTOS
	
	@FXML
	VBox ventana;
	private TitulosMenusYHacerProductos datos;

	
	//	INICIALIZADOR
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datos = new TitulosMenusYHacerProductos();
		prepararVentana();
		hacerSeccion2();
		mostrarCantidadPreparar();

	}

	
	/**
	 * Crea las secciones de la ventana.
	 */
	private void prepararVentana() {
		List<Node> hijosVentana = ventana.getChildren();
		hijosVentana.add(new VBox());
		hijosVentana.add(new FlowPane());

	}

	
	/**
	 * Crea la sección 2.
	 */
	private void hacerSeccion2() {
		List<Node> hijosSeccion = ((FlowPane) ventana.getChildren().get(1)).getChildren();
		Button boton = new Button(datos.getTextosHacerProductos().get(1));
		boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventoBoton);
		hijosSeccion.add(boton);

	}

	
	/**
	 * Añade a la ventana la cantidad de cada producto que es necesaria.
	 */
	private void mostrarCantidadPreparar() {
		List<Node> hijosSeccion = ((VBox) ventana.getChildren().get(0)).getChildren();
		hijosSeccion.add(new Label(datos.getTextosHacerProductos().get(0)));

		List<PedCliente> pedidos = new ArrayList<>();
		List<Producto> productos = new ArrayList<>();
		List<Integer> cantidades = new ArrayList<>();

		try (Connection con = new Conector().getMySQLConnection()) {
			pedidos = new PedClienteManager().obtenerTodosLosPedidos(con);
			productos = new ProductoManager().obtenerProductos(con);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		LocalDate fecha = LocalDate.now();
		Date fechaHoy = java.sql.Date.valueOf(fecha);
		
		productos.forEach(producto -> {
			cantidades.add(0);
		});

		pedidos.stream().filter(pedido -> pedido.getFecha().toString().equals(fechaHoy.toString()) == true)
				.collect(Collectors.toList()).forEach(pedido -> {
			pedido.getPedidos().forEach(subPedido -> {
				int indice = cantidades.indexOf(cantidades.get(subPedido.getCodigoProducto() - 1));
				cantidades.set(indice, cantidades.get(indice) + subPedido.getCantidad());


				
			});
		});
		
		final List<Producto> productos2 = productos;
		
		cantidades.forEach(cantidad ->{
			Label texto = new Label(cantidad + "  " + productos2.get(cantidades.indexOf(cantidad)).getNombre());
			texto.setPadding(datos.getInsetsHacerProductos());
			hijosSeccion.add(texto);
		});

	}

	// EVENTOS

	/**
	 * evento que vuelve al menú.
	 */
	EventHandler<MouseEvent> eventoBoton = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			try {
				App.setRoot("Inicio");

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};
}
