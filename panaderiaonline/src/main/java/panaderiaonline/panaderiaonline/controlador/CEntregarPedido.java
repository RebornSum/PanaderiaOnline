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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import modelo.PropiedadesAnadirPedido;
import modelo.PropiedadesEntregarPedido;
import panaderiaonline.panaderiaonline.App;
import services.conector.Conector;
import services.dao.PedCliente;
import services.dao.Pedido;
import services.manager.PedClienteManager;

/**
 * 
 * @author Christian Pons Hern?ndez
 *
 */

public class CEntregarPedido implements Initializable {

	//	ATRIBUTOS
	
	@FXML private VBox ventana;
	private PropiedadesEntregarPedido datos;
	private TextField telefono;
	private TextField numPedido;
	private Double total;

	
	//	INICIALIZADOR
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datos = new PropiedadesEntregarPedido();
		crearSecciones();
		prepararSeccion1();
		
	}
	
	
	//	M?TODOS


	/**
	 * crea las secciones de la venana.
	 */
	private void crearSecciones() {
		List<Node>hijosSecciones = ventana.getChildren();
		hijosSecciones.add(new FlowPane());
		hijosSecciones.add(new VBox());
		
	}
	
	
	/**
	 * Prepara toda la primera zona llamando a los eventos necesarios y a?adiendo los nodos a la ventana.
	 */
	private void prepararSeccion1() {
		List<Button>botones = crearBotones();
		List<Node>hijosSeccion = ((FlowPane)ventana.getChildren().get(0)).getChildren();
		hijosSeccion.add(botones.get(0));
		hijosSeccion.add(new Label(datos.getTelefono()));
		
		telefono = new TextField();
		telefono.addEventHandler(KeyEvent.KEY_TYPED, eventosTeclado);
		hijosSeccion.add(telefono);
		
		numPedido = new TextField();
		numPedido.setPromptText(datos.getPrompText());
		hijosSeccion.add(numPedido);
		
		hijosSeccion.add(botones.get(1));
		
	}
	
	
	/**
	 * crea tantos botones como textos tenga la variable botonesTexto en el modelo.
	 * @return lista con todos los botones.
	 */
	private List<Button> crearBotones() {
		List<Button>botones = new ArrayList<>();
		for(int i = 0; i < datos.getTextosBotones().size(); i++) {
			Button boton = new Button(datos.getTextosBotones().get(i));
			boton.setId("" + i);
			boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
			botones.add(boton);
		}
		return botones;
	}
	
	
	/**
	 * A?ade a la ventana los datos del pedido junto con el precio de cada producto y el precio final en la secci?n 2
	 */
	private void imprimirRecivo() {
		List<Node> hijosSeccion = ((VBox) ventana.getChildren().get(1)).getChildren();
		PedCliente pedidoCliente = new PedCliente();
		
		hijosSeccion.removeAll(hijosSeccion);
		
		try (Connection con = new Conector().getMySQLConnection()) {
			pedidoCliente = new PedClienteManager().obtenerPedido(con,telefono.getText(),Integer.parseInt(numPedido.getText()));
			
			if(pedidoCliente == null) {
				throw new NullPointerException();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			
		} catch (NullPointerException a) {
			Alert m = new Alert(AlertType.ERROR);
			m.setContentText(datos.getError().get(0));
			m.show();
		}
		
		total = 0.0;
		
		pedidoCliente.getPedidos().forEach(subPedido -> {
		crearSeccion2(hijosSeccion, subPedido);
		 total += subPedido.getCantidad() * subPedido.getProducto().getPrecio();

		});
		
		hijosSeccion.add(new Label(datos.getTextosLabel().get(3)));
		
		TextField precioTotal = new TextField("" + total);
		precioTotal.setEditable(false);
		hijosSeccion.add(precioTotal);
		
		
	}

	
	/**
	 * Introduce en la secci?n 2 nodos con los datos del pedido.
	 * @param hijosSeccion Lista de los hijos de la secci?n 2.
	 * @param subPedido datos del prodcuto perteneciente al pedido.
	 */
	private void crearSeccion2(List<Node> hijosSeccion, Pedido subPedido) {
		FlowPane panel = new FlowPane();
		List<Node>hijosPanel = panel.getChildren();
		
		hijosPanel.add(new Label(datos.getTextosLabel().get(0)));
		hijosPanel.add(new TextField(subPedido.getProducto().getNombre()));
		
		((TextField)hijosPanel.get(1)).setEditable(false);
		
		hijosPanel.add(new Label(datos.getTextosLabel().get(1)));
		hijosPanel.add(new TextField("" + subPedido.getCantidad()));
		
		((TextField)hijosPanel.get(3)).setEditable(false);
		
		hijosPanel.add(new Label(datos.getTextosLabel().get(2)));
		hijosPanel.add(new TextField("" + subPedido.getProducto().getPrecio()));
		
		((TextField)hijosPanel.get(5)).setEditable(false);
		
		hijosSeccion.add(panel);
	}

	
	//	EVENTOS
	
	
	/**
	 * recoge la id del bot?n que ha realizado el evento y realiza una acci?n.
	 * <ul>
	 * 		<li>Si la id del bot?n es "0" vuelve al inicio</li>
	 * 		<li>En caso contrario llama al evento imprimirRecivo</li>
	 * </ul>
	 */
	EventHandler<MouseEvent> eventosBotones = new EventHandler<>() {

		@Override
		public void handle(MouseEvent evt) {
			
			if(((Control)evt.getSource()).getId().equals("0")) {
				try {
					App.setRoot("Inicio");
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				if(telefono.getText().length() != new PropiedadesAnadirPedido().getTamanoTelefono()) {
					Alert a = new Alert(AlertType.WARNING);
					a.setContentText(datos.getError().get(1));
				}
				imprimirRecivo();
				
			}
		}
	};
	
	/**
	 * Se asegura de que el tel?fono introducido contenga solo n?meros y no sea superior al tama?o indicado
	 */
	EventHandler<KeyEvent>eventosTeclado = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent evt) {
			if(((TextField)evt.getSource()).getText().length() >= new PropiedadesAnadirPedido().getTamanoTelefono()) {
				evt.consume();
			}
			
			if(!datos.getNumeros().contains(evt.getCharacter())) {
				evt.consume();
			}
			
		}
	};
	
}
