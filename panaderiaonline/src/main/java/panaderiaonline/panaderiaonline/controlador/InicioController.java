package panaderiaonline.panaderiaonline.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import modelo.PropiedadesAnadirPedido;
import modelo.TitulosYmenus;
import panaderiaonline.panaderiaonline.App;

/**
 * 
 * @author Christian Pons Hernández
 *
 */

public class InicioController implements Initializable{

	// ATRIBUTOS
	
	@FXML HBox ventana;
	Button boton;
	private TitulosYmenus t;
	
	
	//	INICIALIZADOR
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		t = new TitulosYmenus();
		añadirBotones();
		
		
	}
	
	
	//	MÉTODOS
	

	/**
	 * 	Añade tantos botones a la ventana como textos haya en su modelo.
	 *  A cada botón se le añade un evento de ratón.
	 */
	private void añadirBotones() {
		List<Node> hijosVentana = ventana.getChildren();
		
		
		for(int i = 0; i < t.getTextosMenu().size(); i++) {
			this.boton = new Button(t.getTextosMenu().get(i));
			boton.setId("" + i);
			boton.addEventFilter(MouseEvent.MOUSE_CLICKED, accionBoton);
			hijosVentana.add(boton);
		}
		
	}
	
	
	//	EVENTOS
	
	
	/**
	 * Este evento recoge la id del botón que ha realizado el evento y llama a la ventana correspondiente.
	 */
	EventHandler<MouseEvent> accionBoton = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent evt) {
				
				int id = Integer.parseInt(((Control)evt.getSource()).getId());
				switch (id){
				case 0:
					try {
						App.setRoot("VAnadirPedido");
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
					
				case 1:
					try {
						App.setRoot("VMostrarPedidos");
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
					
				case 2:
					try {
						App.setRoot("VHacerProductos");
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
					
				case 3:
					try {
						App.setRoot("VVerCajaDia");
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
					
					default:
						try {
							App.setRoot("VEntregarPedido");
							
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		};
   
}
