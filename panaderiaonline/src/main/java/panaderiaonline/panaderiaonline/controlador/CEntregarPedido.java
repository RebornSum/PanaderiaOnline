package panaderiaonline.panaderiaonline.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import modelo.PropiedadesEntregarPedido;
import panaderiaonline.panaderiaonline.App;
import services.conector.Conector;
import services.dao.PedCliente;
import services.dao.Pedido;
import services.manager.PedClienteManager;

public class CEntregarPedido implements Initializable {

	@FXML private VBox ventana;
	private PropiedadesEntregarPedido datos;
	private TextField telefono;
	private TextField numPedido;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datos = new PropiedadesEntregarPedido();
		crearSecciones();
		prepararSeccion1((FlowPane)ventana.getChildren().get(0));
		
	}


	private void crearSecciones() {
		List<Node>hijosSecciones = ventana.getChildren();
		hijosSecciones.add(new FlowPane());
		hijosSecciones.add(new VBox());
		
	}
	
	
	private void prepararSeccion1(FlowPane panel) {
		List<Button>botones = crearBotones();
		List<Node>hijosSeccion = panel.getChildren();
		hijosSeccion.add(botones.get(0));
		hijosSeccion.add(new Label(datos.getTelefono()));
		
		telefono = new TextField();
		hijosSeccion.add(telefono);
		
		numPedido = new TextField();
		numPedido.setPromptText(datos.getPrompText());
		hijosSeccion.add(numPedido);
		
		hijosSeccion.add(botones.get(1));
		
	}
	
	
	private List<Button> crearBotones() {
		List<Button>botones = new ArrayList<>();
		for(int i = 0; i < datos.getTextosBotones().size(); i++) {
			Button boton = new Button(datos.getTextosBotones().get(i));
			boton.setId("" + i);
			boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
		}
		return botones;
	}
	
	
	private void imprimirRecivo() {
		List<Node> hijosSeccion = ((VBox) ventana.getChildren().get(1)).getChildren();
		PedCliente pedidoCliente = new PedCliente();
		
		hijosSeccion.removeAll(hijosSeccion);
		
		try (Connection con = new Conector().getMySQLConnection()) {
			pedidoCliente = new PedClienteManager().obtenerTodosLosPedidos(con).stream()
					.filter(pedido -> pedido.getTelefono() == telefono.getText()
							&& pedido.getNumPedido() == Integer.parseInt(numPedido.getText()))
					.findFirst().orElse(null);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		pedidoCliente.getPedidos().forEach(subPedido -> {
		FlowPane panel = new FlowPane();
		List<Node>hijosPanel = panel.getChildren();
		hijosPanel.add(new Label(datos.getTextosLabel().get(0)));
		hijosPanel.add(new TextField(subPedido.getProducto().getNombre()));
		hijosPanel.add(new Label(datos.getTextosLabel().get(1)));
		hijosPanel.add(new TextField("" + subPedido.getCantidad()));
		hijosPanel.add(new Label(datos.getTextosLabel().get(2)));
		hijosPanel.add(new TextField("" + subPedido.getProducto().getPrecio()));
		hijosSeccion.add(panel);
		});
		
		
	}

	
	EventHandler<MouseEvent> eventosBotones = new EventHandler<>() {

		@Override
		public void handle(MouseEvent evt) {
			
			if(((Control)evt.getSource()).getId() == "0") {
				try {
					App.setRoot("Inicio");
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				imprimirRecivo();
				
			}
		}
	};
	
}