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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import modelo.DiseñoComboBox;
import modelo.PropiedadesAñadirPedido;
import panaderiaonline.panaderiaonline.App;
import services.conector.Conector;
import services.dao.Producto;
import services.manager.PedClienteManager;
import services.manager.PedidoManager;

public class CAnadirPedido implements Initializable{
	
	@FXML VBox ventana;
	PropiedadesAñadirPedido datos;
	List<ComboBox>todosLosComboBox = new ArrayList<>();
	List<TextField>cantidades = new ArrayList<>(); 
	TextField telefono;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datos = new PropiedadesAñadirPedido();
		crearPaneles();
		
	}

	private void crearPaneles() {
		List<Node>hijosVentana = ventana.getChildren();
		for(int i = 0; i < datos.getNumeroSeccionesAnadirPedido(); i++) {
			FlowPane panel = new FlowPane();
			hijosVentana.add(panel);
		}
		prepararSeccion1((FlowPane)hijosVentana.get(0));
		prepararSeccion2((FlowPane)hijosVentana.get(1));
		anadirSeccionProducto((FlowPane)hijosVentana.get(2));
	}

	private void prepararSeccion1(FlowPane panel) {
		List<Node>hijosPanel = panel.getChildren();
		for(int i = 0; i < datos.getTextosSeccion1AnadirPedido().size();i++) {
			Button boton = new Button(datos.getTextosSeccion1AnadirPedido().get(i));
			boton.setId(datos.getTextosSeccion1AnadirPedido().get(i));
			boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
			hijosPanel.add(boton);
		}
		
	}

	private void prepararSeccion2(FlowPane panel) {
		List<Node>hijosPanel = panel.getChildren();
		
				hijosPanel.add(new Label(datos.getTextosSeccion2AnadirPedido().get(0)));
				
				telefono = new TextField();
				telefono.addEventHandler(KeyEvent.KEY_TYPED, telefonoCorrecto);
				hijosPanel.add(telefono);
				
				Button boton = new Button(datos.getTextosSeccion2AnadirPedido().get(1));
				boton.setId(datos.getTextosSeccion2AnadirPedido().get(1));
				boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
				hijosPanel.add(boton);
	}
	
	private void anadirSeccionProducto(FlowPane panel) {
		List<Node>hijosPanel = panel.getChildren();
		ComboBox<Producto> itemMenu = new ComboBox<>();


		itemMenu.getItems().addAll(datos.obtenerListaDeProductos());
		itemMenu.setCellFactory(listView -> new DiseñoComboBox());
		itemMenu.setButtonCell(new DiseñoComboBox());
		todosLosComboBox.add(itemMenu);
		hijosPanel.add(itemMenu);
		
		TextField cajaTexto = new TextField();
		cajaTexto.setPromptText(datos.getPromptText());
		cantidades.add(cajaTexto);
		hijosPanel.add(cajaTexto);
		
		
	}
	
	private void confirmarPedido() {
		try(Connection con = new Conector().getMySQLConnection()) {
			int numPedido = new PedClienteManager().obtenerNumeroPedido(con);
			new PedClienteManager().añadirPedido(con, telefono.getText(), numPedido);
			
			for(int i = 0; i < todosLosComboBox.size(); i++) {
				if(todosLosComboBox.get(i).getValue() != null && cantidades.get(i).getText() != null ) {
					new PedidoManager().añadirPedido(con, ((Producto) todosLosComboBox.get(0).getValue()).getCodigo(),
							cantidades.get(i).getText());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	EventHandler<KeyEvent>telefonoCorrecto = new EventHandler<KeyEvent>() {
		
		@Override
		public void handle(KeyEvent evt) {
			if(evt.getText().length() > 9) {
				evt.consume();
			}
			
			if(!evt.getCode().isDigitKey()) {
				evt.consume();
			}
			
		}
	};
	
	EventHandler<MouseEvent>eventosBotones = new EventHandler<MouseEvent>() {
		
		@Override
		public void handle(MouseEvent evt) {
			if(((Control)evt.getSource()).getId() == datos.getTextosSeccion2AnadirPedido().get(1)) {
				anadirSeccionProducto((FlowPane)ventana.getChildren().get(1));	
			}
			
			if(((Control)evt.getSource()).getId() == datos.getTextosSeccion1AnadirPedido().get(0)) {
				try {
					App.setRoot("Inicio");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(((Control)evt.getSource()).getId() == datos.getTextosSeccion1AnadirPedido().get(1)) {
				if(telefono != null && telefono.getText().length() == datos.getTamanoTelefono()) {
					confirmarPedido();
					
				}else {
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText(datos.getMensajesError().get(0));
				}
				
			}
			
		}

	
	};
	
	
	
	
	

}
