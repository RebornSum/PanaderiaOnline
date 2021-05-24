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

public class CVerCajaDia implements Initializable {

	@FXML private FlowPane ventana;
	private PropiedadesVVerCajaDia datos;
	private TextField cajaDelDia;
	private Double dinero;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datos = new PropiedadesVVerCajaDia();
		PrepararVentana();
		
	}

	private void PrepararVentana() {
		List<Node>hijosVentana = ventana.getChildren();
		List<Button> botones = crearBotones();
		hijosVentana.add(botones.get(0));
		hijosVentana.add(new Label(datos.getTextos().get(2)));
		cajaDelDia = new TextField();
		hijosVentana.add(cajaDelDia);
		hijosVentana.add(botones.get(1));
	}

	private List<Button> crearBotones() {
		List<Button>botones = new ArrayList<>();
		
		for(int i = 0; i < datos.getTextos().size() - 1; i++) {
			Button boton = new Button(datos.getTextos().get(i));
			boton.setPadding(new Insets(0, 20, 0, 20));
			boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
			botones.add(boton);
		}
		return botones;
	}
	
	private void calcularCaja() {
		try(Connection con = new Conector().getMySQLConnection()) {
			LocalDate fecha = LocalDate.now();
			Date fechaHoy = java.sql.Date.valueOf(fecha);
			
			List<PedCliente> pedidos = new PedClienteManager().obtenerTodosLosPedidos(con);
			pedidos.stream().filter(pedido -> pedido.getFecha().compareTo(fechaHoy) == 0).collect(Collectors.toList());
			dinero = 0.0;
			
			pedidos.forEach(pedido ->{
				pedido.getPedidos().forEach(subPedido -> {
					 dinero += subPedido.getProducto().getPrecio() * subPedido.getCantidad();
				});
			});
			
			cajaDelDia.setText("" + dinero);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	EventHandler<MouseEvent>eventosBotones = new EventHandler<MouseEvent>() {
		
		@Override
		public void handle(MouseEvent evt) {
			if(((Control)evt.getSource()).getId() == "0") {
				try {
					App.setRoot("Inicio");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}else {
				calcularCaja();
			}
				
			
			
		}

	};

	
}
