package panaderiaonline.panaderiaonline.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import lombok.ToString;
import modelo.Textos;
import panaderiaonline.panaderiaonline.App;
import services.conector.Conector;
import services.dao.Producto;
import services.manager.PedClienteManager;
import services.manager.PedidoManager;

public class CAnadirPedido implements Initializable{
	
	@FXML VBox ventana;
	Textos t;
	List<ComboBox>todosLosComboBox = new ArrayList<>();
	List<TextField>cantidades = new ArrayList<>(); 
	TextField telefono;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		t = new Textos();
		crearPaneles();
		
	}

	private void crearPaneles() {
		List<Node>hijosVentana = ventana.getChildren();
		for(int i = 0; i < t.getNumeroSeccionesAnadirPedido(); i++) {
			FlowPane panel = new FlowPane();
			hijosVentana.add(panel);
		}
		prepararSeccion1((FlowPane)hijosVentana.get(0));
		prepararSeccion2((FlowPane)hijosVentana.get(1));
		anadirSeccionProducto((FlowPane)hijosVentana.get(2));
	}

	private void prepararSeccion1(FlowPane panel) {
		List<Node>hijosPanel = panel.getChildren();
		for(int i = 0; i < t.getTextosSeccion1AnadirPedido().size();i++) {
			Button boton = new Button(t.getTextosSeccion1AnadirPedido().get(i));
			boton.setId(t.getTextosSeccion1AnadirPedido().get(i));
			boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
			hijosPanel.add(boton);
		}
		
	}

	private void prepararSeccion2(FlowPane panel) {
		List<Node>hijosPanel = panel.getChildren();
		
				hijosPanel.add(new Label(t.getTextosSeccion2AnadirPedido().get(0)));
				
				telefono = new TextField();
				telefono.addEventHandler(KeyEvent.KEY_TYPED, telefonoCorrecto);
				hijosPanel.add(telefono);
				
				Button boton = new Button(t.getTextosSeccion2AnadirPedido().get(1));
				boton.setId(t.getTextosSeccion2AnadirPedido().get(1));
				boton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventosBotones);
				hijosPanel.add(boton);
	}
	
	private void anadirSeccionProducto(FlowPane panel) {
		List<Node>hijosPanel = panel.getChildren();
		ComboBox<Producto> itemMenu = new ComboBox<>();


		itemMenu.getItems().addAll(t.obtenerListaDeProductos());
		itemMenu.setCellFactory(listView -> new DiseñoComboBox());
		itemMenu.setButtonCell(new DiseñoComboBox());
		todosLosComboBox.add(itemMenu);
		hijosPanel.add(itemMenu);
		
		TextField cajaTexto = new TextField();
		cajaTexto.setPromptText(t.getPromptText());
		cantidades.add(cajaTexto);
		hijosPanel.add(cajaTexto);
		
		
	}
	
	private void confirmarPedido() {
		String numTelefono = telefono.getText();
		try(Connection con = new Conector().getMySQLConnection()) {
			new PedClienteManager().añadirPedido(con, numTelefono);
			
			for(int i = 0; i < todosLosComboBox.size(); i++) {
				new PedidoManager().añadirPedido(con,((Producto)todosLosComboBox.get(0).getValue()).getCodigo(), cantidades.get(i).getText());
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
			if(((Control)evt.getSource()).getId() == t.getTextosSeccion2AnadirPedido().get(1)) {
				anadirSeccionProducto((FlowPane)ventana.getChildren().get(1));	
			}
			
			if(((Control)evt.getSource()).getId() == t.getTextosSeccion1AnadirPedido().get(0)) {
				try {
					App.setRoot("Inicio");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(((Control)evt.getSource()).getId() == t.getTextosSeccion1AnadirPedido().get(1)) {
				if(telefono != null) {
					confirmarPedido();
					
				}else {
					Alert a = new Alert(AlertType.ERROR);
					a.setContentText(t.getMensajesError().get(0));
				}
				
			}
			
		}

	
	};
	
	
	
	
	

}
