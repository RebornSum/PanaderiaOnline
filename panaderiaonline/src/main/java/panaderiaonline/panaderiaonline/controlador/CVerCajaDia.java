package panaderiaonline.panaderiaonline.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import modelo.PropiedadesVVerCajaDia;
import panaderiaonline.panaderiaonline.App;
import services.conector.Conector;
import services.dao.PedCliente;
import services.manager.PedClienteManager;

/**
 * 
 * @author Christian Pons Hernández
 *
 */

public class CVerCajaDia implements Initializable {

	//	ATRIBUTOS
	
	@FXML private FlowPane ventana;
	private PropiedadesVVerCajaDia datos;
	private TextField cajaDelDia;
	private Double dinero;

	
	//	INICIALIZADOR
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datos = new PropiedadesVVerCajaDia();
		PrepararVentana();
		
	}

	
	//	MÉTODOS
	
	/**
	 * hace toda la ventana llamando a los eventos correspondientes.
	 */
	private void PrepararVentana() {
		List<Node>hijosVentana = ventana.getChildren();
		List<Button> botones = crearBotones();
		hijosVentana.add(botones.get(0));
		hijosVentana.add(new Label(datos.getTextos().get(2)));
		cajaDelDia = new TextField();
		cajaDelDia.setEditable(false);
		hijosVentana.add(cajaDelDia);
		hijosVentana.add(botones.get(1));
	}
	
	
	/**
	 * Crea tantos botones como cantidad de textos en su modelo - 1 y les añade el evento.
	 * @return lista de todos los botones.
	 */
	private List<Button> crearBotones() {
		List<Button>botones = new ArrayList<>();
		
		for(int i = 0; i < datos.getTextos().size() - 1; i++) {
			Button boton = new Button(datos.getTextos().get(i));
			boton.setPadding(new Insets(0, 20, 0, 20));
			boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
			boton.setId("" + i);
			botones.add(boton);
		}
		return botones;
	}
	
	
	/**
	 * Recoge todos los pedidos que tengan la misma fecha que el día actual y pone el valor de la suma de todos los productos
	 * en el TextField cajaDelDia.
	 */
	private void calcularCaja() {
		try(Connection con = new Conector().getMySQLConnection()) {
			LocalDate fecha = LocalDate.now();
			Date fechaHoy = java.sql.Date.valueOf(fecha);
			
			List<PedCliente> pedidos = new PedClienteManager().obtenerTodosLosPedidos(con);
			
			dinero = 0.0;
			
			pedidos.stream().filter(pedido -> pedido.getFecha().toString().equals(fechaHoy.toString()) == true)
					.collect(Collectors.toList()).forEach(pedido -> {
						pedido.getPedidos().forEach(subPedido -> {
							dinero += subPedido.getProducto().getPrecio() * subPedido.getCantidad();
						});
			});
			
			cajaDelDia.setText("" + dinero);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * recoge la id del botón que ha realizado el evento.
	 * <ul>
	 * 		<li>si la id del bóton es 0 volverá al inicio.</li>
	 * 		<li>si no es así, llamará al evento calcularCaja.</li>
	 * </ul>
	 */
	EventHandler<MouseEvent>eventosBotones = new EventHandler<MouseEvent>() {
		
		@Override
		public void handle(MouseEvent evt) {
			int id = Integer.parseInt(((Control)evt.getSource()).getId());
			
			if(id == 0) {
				try {
					App.setRoot("Inicio");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}else {
				System.out.println(((Control)evt.getSource()).getId());
				calcularCaja();
			}
				
			
			
		}

	};

	
}
