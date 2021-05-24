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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import modelo.DiseñoComboBox;
import modelo.PropiedadesAnadirPedido;
import panaderiaonline.panaderiaonline.App;
import services.conector.Conector;
import services.dao.Producto;
import services.manager.PedClienteManager;
import services.manager.PedidoManager;
import services.manager.ProductoManager;

public class CAnadirPedido implements Initializable{
	
	//	ATRIBUTOS
	
	@FXML VBox ventana;
	PropiedadesAnadirPedido datos;
	List<ComboBox>todosLosComboBox = new ArrayList<>();
	List<TextField>cantidades = new ArrayList<>(); 
	TextField telefono;

	
	//	INICIALIZADOR
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datos = new PropiedadesAnadirPedido();
		crearSecciones();
		
	}

	
	// MÉTODOS
	
	
	/**
	 * crea tantos VBox como lo indica la variable numeroSecciones en su modelo para estructurar la ventana en varias secciones.
	 */
	private void crearSecciones() {
		List<Node>hijosVentana = ventana.getChildren();
		for(int i = 0; i < datos.getNumeroSecciones(); i++) {
			VBox seccion = new VBox();
			hijosVentana.add(seccion);
		}
		prepararSeccion1();
		prepararSeccion2();
		anadirSeccionProducto();
	}

	
	/**
	 * Se encarga de hacer toda la primera seccion de la ventana añadiéndole los nodos que necesite.
	 * 
	 */
	private void prepararSeccion1() {
		List<Node>hijosSeccion = ((VBox)ventana.getChildren().get(0)).getChildren();
		
		FlowPane panel = new FlowPane();
		List<Node>hijosPanel = panel.getChildren();
		
		for(int i = 0; i < datos.getBotonesSeccion1().size();i++) {
			Button boton = new Button(datos.getBotonesSeccion1().get(i));
			boton.setId(datos.getBotonesSeccion1().get(i));
			boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
			hijosPanel.add(boton);
		}
		
		hijosSeccion.add(panel);
		
	}

	
	/**
	 * Se encarga de hacer toda la seccion 2 de la ventana.
	 */
	private void prepararSeccion2() {
		List<Node>hijosSeccion = ((VBox)ventana.getChildren().get(1)).getChildren();
		
		FlowPane panel = new FlowPane();
		List<Node>hijosPanel = panel.getChildren();
		
		hijosPanel.add(new Label(datos.getTextosSeccion2().get(0)));
				
		telefono = new TextField();
		telefono.addEventHandler(KeyEvent.KEY_TYPED, telefonoCorrecto);
		hijosPanel.add(telefono);
				
		Button boton = new Button(datos.getTextosSeccion2().get(1));
		boton.setId(datos.getTextosSeccion2().get(1));
		boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
		hijosPanel.add(boton);
				
		hijosSeccion.add(panel);		
		
	}
	
	
	/**
	 * Se encarga de añadir una línea más para escoger un producto y su cantidad a la última sección de la ventana.
	 */
	private void anadirSeccionProducto() {
		List<Node>hijosSeccion = ((VBox)ventana.getChildren().get(2)).getChildren();
		
		FlowPane panel = new FlowPane();
		panel.setPadding(new Insets(20, 0, 0, 0));
		List<Node>hijosPanel = panel.getChildren();
		
		ComboBox<Producto> itemMenu = new ComboBox<>();
		try(Connection con = new Conector().getMySQLConnection()) {
			itemMenu.getItems().addAll(new ProductoManager().obtenerProductos(con));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		itemMenu.setCellFactory(listView -> new DiseñoComboBox());
		itemMenu.setButtonCell(new DiseñoComboBox());
		todosLosComboBox.add(itemMenu);
		hijosPanel.add(itemMenu);
		
		TextField cajaTexto = new TextField();
		cajaTexto.setPromptText(datos.getPromptText());
		cantidades.add(cajaTexto);
		hijosPanel.add(cajaTexto);
		
		hijosSeccion.add(panel);
		
	}
	
	
	/**
	 * Intenta introducir los datos en la base de datos.
	 * <ul>
	 * 		<li>Si es correcto los datos se introducirán en la base de datos y saltará una alerta de información</li>
	 * 		<li>Si el mismo pedido ya ha sido introducido antes saltará un mensaje de error</li>
	 * 		<li>Si el número de teléfono es demasado corto saltará un mensaje de error</li>
	 * </ul>
	 */
	private void confirmarPedido() {
		try(Connection con = new Conector().getMySQLConnection()) {
			int numPedido = new PedClienteManager().obtenerNumeroPedido(con);
			boolean correcto = new PedClienteManager().añadirPedido(con, telefono.getText(), numPedido);
			
			if(!correcto) {
				throw new SQLException();
			}
			
			for(int i = 0; i < todosLosComboBox.size(); i++) {
				if(todosLosComboBox.get(i).getValue() != null && cantidades.get(i).getText() != null ) {
					int cantidad = Integer.parseInt(cantidades.get(i).getText());
					new PedidoManager().añadirPedido(con, numPedido, ((Producto) todosLosComboBox.get(i).getValue()).getCodigo(),
							cantidad);
				}
			}
			
			Alert a = new Alert(AlertType.INFORMATION);
			a.setContentText(datos.getMensajes().get(2));
			a.show();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			Alert a = new Alert(AlertType.INFORMATION);
			a.setContentText(datos.getMensajes().get(1));
			a.show();
			
		}
		
		
	}
	
	
	//	EVENTOS
	
	
	/**
	 * Se encarga de que el teléfono siendo introducido es correcto asegurándose de que solo se introducen números y
	 *  no se sobrepasa  el tamaño del teléfono.
	 */
	EventHandler<KeyEvent>telefonoCorrecto = new EventHandler<KeyEvent>() {
		
		@Override
		public void handle(KeyEvent evt) {
			if(((TextField)evt.getSource()).getText().length() >= datos.getTamanoTelefono()) {
				evt.consume();
			}
			
			if(!datos.getNumeros().contains(evt.getCharacter())) {
				evt.consume();
			}
			
		}
	};
	
	
	/**
	 * Coje la id del botón que ha realizado el evento y realiza una acción.
	 */
	EventHandler<MouseEvent>eventosBotones = new EventHandler<MouseEvent>() {
		
		@Override
		public void handle(MouseEvent evt) {
			if(((Control)evt.getSource()).getId() == datos.getTextosSeccion2().get(1)) {
				anadirSeccionProducto();	
			}
			
			if(((Control)evt.getSource()).getId() == datos.getBotonesSeccion1().get(0)) {
				try {
					App.setRoot("Inicio");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(((Control)evt.getSource()).getId() == datos.getBotonesSeccion1().get(1)) {
				if(telefono != null && telefono.getText().length() == datos.getTamanoTelefono()) {
					confirmarPedido();
					
				}else {
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText(datos.getMensajes().get(0));
					a.show();
				}
				
			}
			
		}

	
	};
	
	
	
	
	

}
